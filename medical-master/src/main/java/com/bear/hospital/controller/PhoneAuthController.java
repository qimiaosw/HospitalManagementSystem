package com.bear.hospital.controller;

import com.bear.hospital.service.EmailCodeService;
import com.bear.hospital.service.PatientService;
import com.bear.hospital.pojo.Patient;
import com.bear.hospital.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号登录注册控制器
 */
@RestController
@RequestMapping("/auth/phone")
@Slf4j
public class PhoneAuthController {

    @Autowired
    private EmailCodeService emailCodeService;

    @Autowired
    private PatientService patientService;

    /**
     * 第一步：发送登录验证码
     */
    @PostMapping("/send-login-code")
    public Map<String, Object> sendLoginCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号不能为空");
            return result;
        }

        // 1. 检查手机号是否绑定邮箱
        String boundEmail = emailCodeService.getEmailByPhone(phone);

        if (!StringUtils.hasText(boundEmail)) {
            result.put("success", false);
            result.put("code", 404);
            result.put("message", "该手机号未绑定邮箱");
            result.put("needRegister", true);
            return result;
        }

        // 2. 发送验证码到绑定的邮箱
        boolean sendSuccess = emailCodeService.sendEmailCode(phone, boundEmail);

        if (sendSuccess) {
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "验证码已发送到绑定邮箱");
            result.put("maskedEmail", maskEmail(boundEmail));
        } else {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", emailCodeService.getErrorMessage());
        }

        return result;
    }

    /**
     * 第二步：验证码登录（使用真实数据库查询）
     */
    @PostMapping("/login")
    public Map<String, Object> loginWithCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");
        String userType = params.get("userType"); // 目前只支持患者

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号和验证码不能为空");
            return result;
        }

        // 只支持患者登录
        if (!"患者".equals(userType) && !"patient".equals(userType)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "目前只支持患者手机号登录");
            return result;
        }

        // 1. 验证验证码
        String email = emailCodeService.verifyEmailCode(phone, code);

        if (email == null) {
            result.put("success", false);
            result.put("code", 401);
            result.put("message", emailCodeService.getVerifyError());
            return result;
        }

        // 2. 根据手机号查询真实的患者信息
        Patient patient = patientService.findPatientByPhone(phone);
        if (patient == null) {
            result.put("success", false);
            result.put("code", 404);
            result.put("message", "未找到该手机号对应的患者信息，请先注册");
            return result;
        }

        // 3. 验证邮箱是否匹配
        if (!email.equals(patient.getPEmail())) {
            log.warn("邮箱不匹配: 输入邮箱={}, 数据库邮箱={}", email, patient.getPEmail());
            result.put("success", false);
            result.put("code", 403);
            result.put("message", "邮箱验证失败，请重新获取验证码");
            return result;
        }

        // 4. 生成标准JWT token（与账号密码登录完全一致）
        Map<String, String> map = new HashMap<>();
        map.put("pName", patient.getPName());
        map.put("pId", String.valueOf(patient.getPId())); // 真实的数字ID
        map.put("pCard", patient.getPCard());
        String token = JwtUtil.getToken(map);

        // 5. 返回登录结果（与账号密码登录完全一致的结构）
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("pName", patient.getPName());
        userInfo.put("pId", patient.getPId());
        userInfo.put("pCard", patient.getPCard());
        userInfo.put("pPhone", patient.getPPhone());
        userInfo.put("pEmail", patient.getPEmail());
        userInfo.put("token", token);

        result.put("success", true);
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", userInfo); // 与账号密码登录完全一致
        result.put("token", token);

        log.info("手机号登录成功: phone={}, pId={}, pName={}", phone, patient.getPId(), patient.getPName());
        return result;
    }

    /**
     * 注册并绑定邮箱（真实数据库版本）
     */
    @PostMapping("/register")
    public Map<String, Object> registerAndBind(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String email = params.get("email");
        String code = params.get("code");
        String userType = params.get("userType");
        String name = params.get("name");
        String password = params.get("password");

        Map<String, Object> result = new HashMap<>();

        // 1. 参数验证
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号、邮箱和验证码不能为空");
            return result;
        }

        // 2. 验证验证码
        String verifiedEmail = emailCodeService.verifyEmailCode(phone, code);

        if (verifiedEmail == null || !verifiedEmail.equals(email)) {
            result.put("success", false);
            result.put("code", 401);
            result.put("message", "验证码错误或已过期");
            return result;
        }

        // 3. 检查手机号是否已注册
        Patient existingPatient = patientService.findPatientByPhone(phone);
        if (existingPatient != null) {
            result.put("success", false);
            result.put("code", 409);
            result.put("message", "该手机号已注册");
            return result;
        }

        // 4. 创建患者（这里需要您提供生成患者ID的逻辑）
        // 暂时返回提示信息
        result.put("success", false);
        result.put("code", 501);
        result.put("message", "患者注册功能需要完善ID生成逻辑");

        log.info("患者注册请求: phone={}, email={}, name={}", phone, email, name);
        return result;
    }

    /**
     * 发送绑定邮箱的验证码
     */
    @PostMapping("/send-bind-code")
    public Map<String, Object> sendBindEmailCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String newEmail = params.get("newEmail");

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone) || !StringUtils.hasText(newEmail)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号和新邮箱不能为空");
            return result;
        }

        // 发送验证码到新邮箱
        boolean sendSuccess = emailCodeService.sendEmailCode(phone, newEmail);

        if (sendSuccess) {
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "验证码已发送到新邮箱");
            result.put("maskedEmail", maskEmail(newEmail));
        } else {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", emailCodeService.getErrorMessage());
        }

        return result;
    }

    /**
     * 检查手机号是否可用
     */
    @GetMapping("/check-phone")
    public Map<String, Object> checkPhoneAvailable(@RequestParam String phone,
                                                   @RequestParam(required = false) String userType) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号不能为空");
            return result;
        }

        Patient patient = patientService.findPatientByPhone(phone);
        boolean isRegistered = (patient != null);
        boolean isBound = emailCodeService.isPhoneBound(phone);

        result.put("success", true);
        result.put("code", 200);
        result.put("phone", phone);
        result.put("isRegistered", isRegistered);
        result.put("isBound", isBound);
        result.put("available", !isRegistered);
        result.put("message", isRegistered ? "手机号已注册" : "手机号可用");

        return result;
    }

    // ========== 私有方法 ==========

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
}