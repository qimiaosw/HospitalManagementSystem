// WechatLoginService.java
package com.bear.hospital.service;

import com.bear.hospital.pojo.WechatUser;
import com.bear.hospital.pojo.WechatQrState;
import com.bear.hospital.utils.ResponseData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface WechatLoginService {

    /**
     * 生成微信登录二维码
     */
    ResponseData generateQrCode();

    /**
     * 处理微信回调
     */
    ResponseData handleCallback(String code, String state);

    /**
     * 检查登录状态
     */
    ResponseData checkLoginStatus(String qrUuid);

    /**
     * 绑定系统用户
     */
    ResponseData bindSystemUser(String qrUuid, String phone, String email,
                                String realName, String userType);

    /**
     * 根据openid查询微信用户
     */
    WechatUser findByOpenId(String wOpenId);

    /**
     * 根据患者ID查询微信绑定
     */
    WechatUser findByPatientId(Integer pId);
}