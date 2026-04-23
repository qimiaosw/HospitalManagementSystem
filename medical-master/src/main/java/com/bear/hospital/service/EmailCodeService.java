// EmailCodeService.java
package com.bear.hospital.service;

/**
 * 邮箱验证码服务接口
 */
public interface EmailCodeService {

    /**
     * 发送邮箱验证码
     * @param phone 手机号
     * @param email 邮箱地址
     * @return 成功返回true，失败返回false
     */
    boolean sendEmailCode(String phone, String email);

    /**
     * 获取错误信息
     */
    String getErrorMessage();

    /**
     * 获取发送验证码的详细信息
     */
    String getSendCodeDetail();

    /**
     * 验证邮箱验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 成功返回绑定的邮箱，失败返回null
     */
    String verifyEmailCode(String phone, String code);

    /**
     * 获取验证错误信息
     */
    String getVerifyError();

    /**
     * 获取手机号绑定的邮箱
     */
    String getEmailByPhone(String phone);

    /**
     * 检查手机号是否已绑定邮箱
     */
    boolean isPhoneBound(String phone);
    /**
     * 根据邮箱获取手机号
     * @param email 邮箱地址
     * @return 手机号，失败返回null
     */
    String getPhoneByEmail(String email);

    /**
     * 根据邮箱发送验证码（新增）
     * @param email 邮箱地址
     * @return 成功返回true，失败返回false
     */
    boolean sendEmailCodeByEmail(String email);

}