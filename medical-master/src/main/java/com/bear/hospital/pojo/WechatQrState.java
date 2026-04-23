package com.bear.hospital.pojo;

import lombok.Data;

@Data
public class WechatQrState {
    private Integer qrId;           // 主键ID
    private String qrUuid;          // 二维码UUID
    private String qrState;         // 防CSRF的state
    private Integer qrStatus;       // 状态：0=等待扫码，1=已扫码，2=登录成功，3=已取消
    private String wOpenId;         // 微信openid
    private String qrUserInfo;      // 用户信息JSON
    private String qrSystemToken;   // 系统token
    private Long qrCreateTime;      // 创建时间戳
    private Long qrExpireTime;      // 过期时间戳
    private Long qrScanTime;        // 扫码时间戳
    private Long qrLoginTime;       // 登录时间戳

    @Override
    public String toString() {
        return "WechatQrState{" +
                "qrId=" + qrId +
                ", qrUuid='" + qrUuid + '\'' +
                ", qrState='" + qrState + '\'' +
                ", qrStatus=" + qrStatus +
                ", wOpenId='" + wOpenId + '\'' +
                ", qrUserInfo='" + qrUserInfo + '\'' +
                ", qrSystemToken='" + qrSystemToken + '\'' +
                ", qrCreateTime=" + qrCreateTime +
                ", qrExpireTime=" + qrExpireTime +
                ", qrScanTime=" + qrScanTime +
                ", qrLoginTime=" + qrLoginTime +
                '}';
    }
}