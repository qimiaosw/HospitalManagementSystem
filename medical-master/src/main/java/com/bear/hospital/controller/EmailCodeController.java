// EmailCodeController.java（去掉Swagger注解的版本）
package com.bear.hospital.controller;

import com.bear.hospital.mapper.EmailMapper;
import com.bear.hospital.service.EmailCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮箱验证码控制器
 */
@RestController
@RequestMapping("/email-code")
@Slf4j
public class EmailCodeController {

    @Autowired
    private EmailCodeService emailCodeService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/send")
    public Map<String, Object> sendEmailCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String email = params.get("email");

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone) || !StringUtils.hasText(email)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号和邮箱不能为空");
            return result;
        }

        boolean sendSuccess = emailCodeService.sendEmailCode(phone, email);

        if (sendSuccess) {
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "验证码发送成功");
            result.put("detail", emailCodeService.getSendCodeDetail());
        } else {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", emailCodeService.getErrorMessage());
        }

        log.info("发送验证码结果: phone={}, success={}", phone, sendSuccess);
        return result;
    }

    /**
     * 验证邮箱验证码
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyEmailCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号和验证码不能为空");
            return result;
        }

        String email = emailCodeService.verifyEmailCode(phone, code);

        if (email != null) {
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "验证成功");
            result.put("phone", phone);
            result.put("email", email);
        } else {
            result.put("success", false);
            result.put("code", 401);
            result.put("message", emailCodeService.getVerifyError());
        }

        log.info("验证验证码结果: phone={}, success={}", phone, email != null);
        return result;
    }

    /**
     * 检查手机号是否已绑定邮箱
     */
    @GetMapping("/check-bound")
    public Map<String, Object> checkPhoneBound(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(phone)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "手机号不能为空");
            return result;
        }

        boolean isBound = emailCodeService.isPhoneBound(phone);
        String boundEmail = emailCodeService.getEmailByPhone(phone);

        result.put("success", true);
        result.put("code", 200);
        result.put("isBound", isBound);
        result.put("boundEmail", boundEmail != null ? maskEmail(boundEmail) : null);

        return result;
    }

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

    @Autowired
    private EmailMapper emailMapper;
    @GetMapping("/test-simple")
    public Map<String, Object> testSimpleQuery(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 只测试最基本的查询
            String email = emailMapper.selectEmailByPhone(phone);

            result.put("success", true);
            result.put("phone", phone);
            result.put("email", email);
            result.put("message", email != null ? "查询成功" : "未找到邮箱");

            // 调试输出
            System.out.println("简单查询测试 - 手机号: " + phone + ", 邮箱: " + email);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }/**
     * 根据邮箱发送验证码（新增）
     */
    @PostMapping("/send-by-email")
    public Map<String, Object> sendEmailCodeByEmail(@RequestBody Map<String, String> params) {
        String email = params.get("email");

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(email)) {
            result.put("success", false);
            result.put("code", 400);
            result.put("message", "邮箱不能为空");
            return result;
        }

        boolean sendSuccess = emailCodeService.sendEmailCodeByEmail(email);

        if (sendSuccess) {
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "验证码发送成功");
            result.put("detail", emailCodeService.getSendCodeDetail());
        } else {
            result.put("success", false);
            result.put("code", 500);
            result.put("message", emailCodeService.getErrorMessage());
        }

        log.info("根据邮箱发送验证码结果: email={}, success={}", email, sendSuccess);
        return result;
    }


}