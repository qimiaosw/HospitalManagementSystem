
package com.bear.hospital.controller;

import com.bear.hospital.service.WechatLoginService;
import com.bear.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth/wechat")
@CrossOrigin(origins = "*")
public class WechatLoginController {

    @Autowired
    private WechatLoginService wechatLoginService;

    /**
     * 生成微信登录二维码
     */
    @GetMapping("/qrcode")
    public ResponseData generateQrCode() {
        return wechatLoginService.generateQrCode();
    }

    /**
     * 微信回调接口
     */
    @GetMapping("/callback")
    public void wechatCallback(@RequestParam String code,
                               @RequestParam String state,
                               HttpServletResponse response) throws IOException {
        ResponseData result = wechatLoginService.handleCallback(code, state);

        if (result.getStatus() == 200) {
            // 获取二维码ID
            String qrId = (String) ((java.util.Map) result.getData()).get("qrId");
            Boolean needBind = (Boolean) ((java.util.Map) result.getData()).get("needBind");

            if (needBind != null && needBind) {
                // 需要绑定，重定向到前端页面
                response.sendRedirect("http://localhost:8080/#/login?wechat=bind&qrId=" + qrId);
            } else {
                // 已绑定，直接登录成功
                response.sendRedirect("http://localhost:8080/#/login?wechat=success&qrId=" + qrId);
            }
        } else {
            response.sendRedirect("http://localhost:8080/#/login?error=微信登录失败");
        }
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check")
    public ResponseData checkLoginStatus(@RequestParam String qrId) {
        return wechatLoginService.checkLoginStatus(qrId);
    }

    /**
     * 绑定系统用户
     */
    @PostMapping("/bind")
    public ResponseData bindSystemUser(@RequestParam String qrId,
                                       @RequestParam String phone,
                                       @RequestParam String email,
                                       @RequestParam(required = false) String realName,
                                       @RequestParam String userType) {
        return wechatLoginService.bindSystemUser(qrId, phone, email, realName, userType);
    }
}
