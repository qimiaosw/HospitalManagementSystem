package com.bear.hospital.service;

import com.alipay.api.AlipayApiException;
import java.util.Map;

/**
 * 支付宝服务接口
 */
public interface AlipayService {

    /**
     * 创建支付宝支付订单
     * @param outTradeNo 商户订单号（挂号订单ID）
     * @param totalAmount 支付金额
     * @param subject 订单标题
     * @param body 订单描述
     * @return 支付表单HTML字符串
     * @throws AlipayApiException 支付宝API异常
     */
    String createPayment(String outTradeNo, String totalAmount, String subject, String body) throws AlipayApiException;

    /**
     * 处理支付宝异步通知
     * @param params 支付宝异步通知参数
     * @return 是否验证成功
     * @throws AlipayApiException 支付宝API异常
     */
    String handleNotify(Map<String, String> params) throws AlipayApiException;

    /**
     * 验证支付宝同步返回
     * @param params 支付宝同步返回参数
     * @return 是否验证成功
     * @throws AlipayApiException 支付宝API异常
     */
    boolean verifyReturn(Map<String, String> params) throws AlipayApiException;
}
