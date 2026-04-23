// EmailCodeServiceImpl.java
package com.bear.hospital.service.serviceImpl;

import com.bear.hospital.mapper.EmailMapper;
import com.bear.hospital.pojo.Patient;
import com.bear.hospital.service.EmailCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱验证码服务实现类
 */
@Service
@Slf4j
public class EmailCodeServiceImpl implements EmailCodeService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.code-expire-minutes:10}")
    private int codeExpireMinutes;

    @Value("${app.email.bind-expire-days:30}")
    private int bindExpireDays;

    @Value("${sms.code.resend-interval:60}")
    private int resendInterval;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 错误信息存储
    private String errorMessage = "";
    private String verifyError = "";
    private String sendCodeDetail = "";
    @Autowired
    private EmailMapper emailMapper;
    /**
     * 测试数据库查询
     */
    public void testDatabaseQuery(String phone) {
        System.out.println("=== 测试数据库查询 ===");
        System.out.println("测试手机号: " + phone);

        try {
            // 1. 直接调用Mapper查询
            String email = emailMapper.selectEmailByPhone(phone);
            System.out.println("直接查询结果: " + email);

            // 2. 查询完整患者信息
            Patient patient = emailMapper.selectByPhone(phone);
            System.out.println("患者信息查询结果: " + (patient != null ? patient.getPEmail() : "null"));

            // 3. 查询患者ID
            Integer patientId = emailMapper.selectIdByPhone(phone);
            System.out.println("患者ID查询结果: " + patientId);

        } catch (Exception e) {
            System.out.println("查询异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=================");
    }

    @Override
    public boolean sendEmailCode(String phone, String email) {
        // 清空前一次的错误信息
        errorMessage = "";
        sendCodeDetail = "";

        try {
            // 1. 参数验证
            if (!isValidPhone(phone)) {
                errorMessage = "请输入正确的11位手机号";
                return false;
            }
            if (!isValidEmail(email)) {
                errorMessage = "邮箱格式不正确";
                return false;
            }

            // 2. 检查发送频率限制
            String frequencyError = checkSendFrequency(phone);
            if (StringUtils.hasText(frequencyError)) {
                errorMessage = frequencyError;
                return false;
            }

            // 3. 生成6位随机验证码
            String code = generateCode();

            // 4. 存储到Redis
            String codeKey = getCodeKey(phone);
            String frequencyKey = getFrequencyKey(phone);
            String bindKey = getBindKey(phone);

            redisTemplate.opsForValue().set(codeKey, code, codeExpireMinutes, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(frequencyKey, "1", resendInterval, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(bindKey, email, bindExpireDays, TimeUnit.DAYS);

            // 5. 发送邮件
            sendEmail(email, code, phone);
            log.info("验证码发送成功: phone={}, email={}", phone, maskEmail(email));

            // 修复这里：使用正确的参数
            sendCodeDetail = String.format("验证码已发送到%s，有效期%d分钟",
                    maskEmail(email), codeExpireMinutes);
            return true;

        } catch (MessagingException e) {
            errorMessage = "邮件发送失败：" + e.getMessage();
            log.error("邮件发送失败", e);
            return false;
        } catch (Exception e) {
            errorMessage = "系统异常：" + e.getMessage();
            log.error("发送验证码异常", e);
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getSendCodeDetail() {
        return sendCodeDetail;
    }

    @Override
    public String verifyEmailCode(String phone, String inputCode) {
        // 清空前一次的验证错误
        verifyError = "";

        try {
            // 1. 参数验证
            if (!isValidPhone(phone)) {
                verifyError = "手机号格式错误";
                return null;
            }
            if (!StringUtils.hasText(inputCode) || inputCode.length() != 6) {
                verifyError = "验证码必须为6位数字";
                return null;
            }

            // 2. 从Redis获取验证码
            String codeKey = getCodeKey(phone);
            String storedCode = redisTemplate.opsForValue().get(codeKey);

            if (storedCode == null) {
                verifyError = "验证码已过期，请重新获取";
                return null;
            }

            // 3. 检查错误次数
            String errorKey = getErrorCountKey(phone);
            Long errorCount = redisTemplate.opsForValue().increment(errorKey);
            redisTemplate.expire(errorKey, 1, TimeUnit.HOURS);

            if (errorCount != null && errorCount >= 5) {
                redisTemplate.delete(codeKey);
                redisTemplate.delete(errorKey);
                verifyError = "验证错误次数过多，请重新获取验证码";
                return null;
            }

            // 4. 验证验证码
            if (!storedCode.equals(inputCode)) {
                int remaining = 5 - errorCount.intValue();
                verifyError = "验证码错误，还剩" + remaining + "次尝试机会";
                return null;
            }

            // 5. 验证成功，清理数据
            redisTemplate.delete(codeKey);
            redisTemplate.delete(errorKey);

            // 6. 获取绑定的邮箱
            String bindKey = getBindKey(phone);
            String email = redisTemplate.opsForValue().get(bindKey);

            if (email != null) {
                // 绑定到数据库
                boolean bindSuccess = bindPhoneAndEmailToDatabase(phone, email);
                if (!bindSuccess) {
                    log.warn("绑定手机号和邮箱到数据库失败: phone={}, email={}", phone, email);
                }
            }

            return email;

        } catch (Exception e) {
            verifyError = "验证失败：" + e.getMessage();
            log.error("验证验证码异常", e);
            return null;
        }
    }

    @Override
    public String getVerifyError() {
        return verifyError;
    }

    @Override
    public String getEmailByPhone(String phone) {
        if (!isValidPhone(phone)) {
            return null;
        }

        String bindKey = getBindKey(phone);
        String email = redisTemplate.opsForValue().get(bindKey);

        if (StringUtils.hasText(email)) {
            return email;
        }

        // 从数据库查询
        return getEmailFromDatabase(phone);
    }

    @Override
    public boolean isPhoneBound(String phone) {
        System.out.println("=== 调试 isPhoneBound 方法 ===");
        System.out.println("输入手机号: " + phone);

        if (!isValidPhone(phone)) {
            System.out.println("手机号格式无效");
            return false;
        }

        // 1. 先检查Redis
        String bindKey = getBindKey(phone);
        String redisEmail = redisTemplate.opsForValue().get(bindKey);
        System.out.println("Redis查询结果: " + redisEmail);

        if (StringUtils.hasText(redisEmail)) {
            System.out.println("从Redis找到绑定邮箱");
            return true;
        }

        // 2. 从数据库查询
        String dbEmail = getEmailFromDatabase(phone);
        System.out.println("数据库查询结果: " + dbEmail);

        if (StringUtils.hasText(dbEmail)) {
            // 查到后应该缓存到Redis
            System.out.println("从数据库找到绑定邮箱，将缓存到Redis");
            redisTemplate.opsForValue().set(bindKey, dbEmail, bindExpireDays, TimeUnit.DAYS);
            return true;
        }

        System.out.println("未找到绑定邮箱");
        return false;
    }
    // ========== 私有方法 ==========

    /**
     * 发送验证码邮件
     */
    private void sendEmail(String toEmail, String code, String phone) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 修复这里：使用简单的setFrom
        helper.setFrom(fromEmail);

        helper.setTo(toEmail);
        helper.setSubject("【医院管理系统】登录验证码");
        helper.setText(buildEmailContent(code, phone), true);

        mailSender.send(message);
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(String code, String phone) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        String timeStr = now.format(formatter);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px; background: #f5f5f5;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background: white; border-radius: 8px; padding: 30px;'>" +
                "<h2 style='color: #1890ff;'>🏥 医院管理系统</h2>" +
                "<p style='color: #666;'>尊敬的用户，您好！</p>" +
                "<p style='color: #666; margin: 20px 0;'>您正在登录医院管理系统，验证码为：</p>" +
                "<div style='background: #f6ffed; border: 2px dashed #52c41a; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px;'>" +
                "<div style='font-size: 36px; font-weight: bold; color: #1890ff; letter-spacing: 8px;'>" + code + "</div>" +
                "<p style='color: #666; margin-top: 10px;'>有效期：" + codeExpireMinutes + "分钟</p>" +
                "</div>" +
                "<div style='background: #e6f7ff; padding: 15px; border-left: 4px solid #1890ff; margin: 20px 0;'>" +
                "<p style='margin: 0 0 10px 0; color: #1890ff;'><strong>安全提示：</strong></p>" +
                "<p style='margin: 5px 0; color: #666; font-size: 14px;'>• 请勿泄露验证码给他人</p>" +
                "<p style='margin: 5px 0; color: #666; font-size: 14px;'>• 绑定手机号：" + maskPhone(phone) + "</p>" +
                "<p style='margin: 5px 0; color: #666; font-size: 14px;'>• 发送时间：" + timeStr + "</p>" +
                "</div>" +
                "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #f0f0f0; text-align: center; color: #999; font-size: 12px;'>" +
                "<p>© " + now.getYear() + " 医院管理系统</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * 检查发送频率
     */
    private String checkSendFrequency(String phone) {
        String frequencyKey = getFrequencyKey(phone);
        String lastSend = redisTemplate.opsForValue().get(frequencyKey);

        if (lastSend != null) {
            Long ttl = redisTemplate.getExpire(frequencyKey, TimeUnit.SECONDS);
            if (ttl != null && ttl > 0) {
                return "请求过于频繁，请" + ttl + "秒后重试";
            }
        }
        return null;
    }

    /**
     * 绑定手机号和邮箱到数据库
     */
    private boolean bindPhoneAndEmailToDatabase(String phone, String email) {
        try {
            // TODO: 实现数据库绑定逻辑
            log.info("绑定手机号和邮箱: phone={}, email={}", phone, email);
            return true;
        } catch (Exception e) {
            log.error("绑定失败", e);
            return false;
        }
    }

    /**
     * 从数据库查询邮箱
     */

    private String getEmailFromDatabase(String phone) {
        System.out.println("=== 调试 getEmailFromDatabase ===");
        System.out.println("查询手机号: " + phone);

        try {
            // 使用和测试接口完全相同的Mapper方法
            String email = emailMapper.selectEmailByPhone(phone);
            System.out.println("Mapper查询结果: " + email);

            if (email == null) {
                System.out.println("注意: 数据库查询返回null");
                System.out.println("SQL条件: p_phone = " + phone + " AND p_state = 1");
            }

            return email;
        } catch (Exception e) {
            System.out.println("数据库查询异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成6位随机验证码
     */
    private String generateCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
    }

    // Redis Key生成器
    private String getCodeKey(String phone) {
        return "email:code:" + phone;
    }

    private String getFrequencyKey(String phone) {
        return "email:freq:" + phone;
    }

    private String getBindKey(String phone) {
        return "bind:phone:email:" + phone;
    }

    private String getErrorCountKey(String phone) {
        return "email:error:" + phone;
    }

    // 验证工具方法
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    private boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * 脱敏邮箱
     */
    private String maskEmail(String email) {
        if (!StringUtils.hasText(email) || email.length() < 5) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex < 3) {
            return email;
        }
        return email.substring(0, 3) + "***" + email.substring(atIndex);
    }

    /**
     * 脱敏手机号
     */
    private String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    /**
     * 根据邮箱获取手机号
     */
    @Override
    public String getPhoneByEmail(String email) {
        System.out.println("=== 调试 getPhoneByEmail ===");
        System.out.println("查询邮箱: " + email);

        if (!StringUtils.hasText(email) || !email.contains("@")) {
            System.out.println("邮箱格式无效");
            return null;
        }

        try {
            // 从数据库查询手机号
            String phone = emailMapper.selectPhoneByEmail(email);
            System.out.println("Mapper查询结果: " + phone);

            if (phone == null) {
                System.out.println("注意: 数据库查询返回null");
                System.out.println("SQL条件: p_email = " + email + " AND p_state = 1");
            }

            return phone;
        } catch (Exception e) {
            System.out.println("数据库查询异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据邮箱发送验证码（新增）
     * @param email 邮箱地址
     * @return 成功返回true，失败返回false
     */
    public boolean sendEmailCodeByEmail(String email) {
        System.out.println("=== 调试 sendEmailCodeByEmail ===");
        System.out.println("邮箱: " + email);

        try {
            // 1. 邮箱格式校验
            if (!email.contains("@")) {
                errorMessage = "邮箱格式不正确";
                return false;
            }

            // 2. 根据邮箱获取手机号
            String phone = getPhoneByEmail(email);

            if (phone == null) {
                errorMessage = "该邮箱未注册";
                sendCodeDetail = "邮箱 " + email + " 未在系统中注册";
                return false;
            }

            // 3. 检查发送频率（使用 Redis）
            String rateLimitKey = "email:rate:limit:" + email;
            String lastSendTime = redisTemplate.opsForValue().get(rateLimitKey);

            if (lastSendTime != null) {
                long elapsed = System.currentTimeMillis() - Long.parseLong(lastSendTime);
                if (elapsed < resendInterval * 1000) {
                    errorMessage = "发送过于频繁，请" + (resendInterval - elapsed / 1000) + "秒后重试";
                    return false;
                }
            }

            // 4. 发送验证码
            boolean sendSuccess = sendEmailCode(phone, email);

            if (sendSuccess) {
                // 5. 记录发送时间
                redisTemplate.opsForValue().set(rateLimitKey, String.valueOf(System.currentTimeMillis()), resendInterval, TimeUnit.SECONDS);
            }

            return sendSuccess;

        } catch (Exception e) {
            System.out.println("发送验证码异常: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "发送验证码失败，请稍后重试";
            return false;
        }
    }

}