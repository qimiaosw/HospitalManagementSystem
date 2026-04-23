package com.bear.hospital.pojo;


import lombok.Data;

import java.util.Date;

@Data
public class WechatUser {
    private Integer wId;            // 主键ID
    private String wOpenId;         // 微信openid
    private String wUnionId;        // 微信unionid
    private String wNickname;       // 昵称
    private String wAvatar;         // 头像
    private Integer wGender;        // 性别：0=未知，1=男，2=女
    private String wCountry;        // 国家
    private String wProvince;       // 省份
    private String wCity;           // 城市
    private String wAccessToken;    // 访问令牌
    private String wRefreshToken;   // 刷新令牌
    private Integer pId;            // 绑定的患者ID
    private Integer dId;            // 绑定的医生ID
    private Integer aId;            // 绑定的管理员ID
    private String wBindType;       // 绑定类型：patient/doctor/admin
    private Date wCreateTime;       // 创建时间
    private Date wUpdateTime;       // 更新时间

    @Override
    public String toString() {
        return "WechatUser{" +
                "wId=" + wId +
                ", wOpenId='" + wOpenId + '\'' +
                ", wUnionId='" + wUnionId + '\'' +
                ", wNickname='" + wNickname + '\'' +
                ", wAvatar='" + wAvatar + '\'' +
                ", wGender=" + wGender +
                ", wCountry='" + wCountry + '\'' +
                ", wProvince='" + wProvince + '\'' +
                ", wCity='" + wCity + '\'' +
                ", wAccessToken='" + wAccessToken + '\'' +
                ", wRefreshToken='" + wRefreshToken + '\'' +
                ", pId=" + pId +
                ", dId=" + dId +
                ", aId=" + aId +
                ", wBindType='" + wBindType + '\'' +
                ", wCreateTime=" + wCreateTime +
                ", wUpdateTime=" + wUpdateTime +
                '}';
    }


}