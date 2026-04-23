package com.bear.hospital.service.serviceImpl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.bear.hospital.config.AlipayConfig;
import com.bear.hospital.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝服务实现类
 */
@Service("AlipayService")
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 获取支付宝客户端
     */
    private AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(
                alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getMerchantPrivateKey(),
                "json",
                alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType()
        );
    }

    /**
     * 创建支付宝支付订单
     */
    @Override
    public String createPayment(String outTradeNo, String totalAmount, String subject, String body) throws AlipayApiException {
        // 1. 创建支付宝客户端
        AlipayClient alipayClient = getAlipayClient();

        // 2. 创建API请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 3. 设置异步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl());

        // 4. 设置同步返回地址（使用后端中转页面）
        request.setReturnUrl(alipayConfig.getReturnUrl());

        // 5. 设置请求参数
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"total_amount\":\"" + totalAmount + "\"," +
                "\"subject\":\"" + subject + "\"," +
                "\"body\":\"" + body + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                "}");

        System.out.println("支付订单参数：out_trade_no=" + outTradeNo + ", total_amount=" + totalAmount);

        // 6. 调用API生成支付表单
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

        if (response.isSuccess()) {
            // 返回支付表单HTML
            return response.getBody();
        } else {
            throw new AlipayApiException("创建支付订单失败：" + response.getSubMsg());
        }
    }

    /**
     * 处理支付宝异步通知
     */
    @Override
    public String handleNotify(Map<String, String> params) throws AlipayApiException {
        try {
            // 1. 验证签名（使用 AlipaySignature 类）
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );

            if (signVerified) {
                // 验证成功，处理业务逻辑
                String tradeStatus = params.get("trade_status");
                String outTradeNo = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");

                // 判断交易状态
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    System.out.println("订单支付成功！商户订单号：" + outTradeNo + "，支付宝交易号：" + tradeNo);
                    return "success";
                }
            } else {
                System.out.println("签名验证失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "fail";
    }

    /**
     * 验证支付宝同步返回
     */
    @Override
    public boolean verifyReturn(Map<String, String> params) throws AlipayApiException {
        try {
            // 使用 AlipaySignature 验证签名
            return AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }
}
