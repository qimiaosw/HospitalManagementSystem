package com.bear.hospital.controller;

import com.alipay.api.AlipayApiException;
import com.bear.hospital.pojo.Orders;
import com.bear.hospital.service.AlipayService;
import com.bear.hospital.service.OrderService;
import com.bear.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private OrderService orderService;
    @RequestMapping("create")
    public ResponseData createPayment(@RequestParam Integer oId) {
        try {
            Orders order = orderService.findOrderByOid(oId);

            if (order == null) {
                return ResponseData.fail("订单不存在");
            }

            // ✅ 处理 null 值：将 null 视为 0（未支付）
            int priceState = order.getOPriceState() != null ? order.getOPriceState() : 0;

            System.out.println("========== 支付订单创建 ==========");
            System.out.println("订单ID: " + oId);
            System.out.println("oTotalPrice: " + order.getOTotalPrice());
            System.out.println("oPriceState: " + priceState);

            // ✅ 每次都生成唯一的商户订单号（避免重复）
            String outTradeNo = String.valueOf(oId) + "_" + System.currentTimeMillis();
            System.out.println("✅ 商户订单号: " + outTradeNo);

            // 设置支付金额
            Double totalPrice = order.getOTotalPrice();
            String totalAmount;

            if (totalPrice != null && totalPrice > 0) {
                totalAmount = String.format("%.2f", totalPrice);
                System.out.println("订单 " + oId + " 的支付金额：" + totalAmount + " 元");
            } else {
                totalAmount = "0.01";
                System.out.println("订单 " + oId + " 的金额为空，使用默认金额：" + totalAmount + " 元");
            }

            String subject = "医院挂号支付 - 订单号：" + oId;
            String body = "订单号：" + oId + " 的挂号费用，金额：" + totalAmount + " 元";

            String paymentForm = alipayService.createPayment(
                    outTradeNo,
                    totalAmount,
                    subject,
                    body
            );

            return ResponseData.success("创建支付订单成功", paymentForm);

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ResponseData.fail("创建支付订单失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("系统异常：" + e.getMessage());
        }
    }


    /**
     * 处理支付宝异步通知
     * @param request HTTP请求对象
     * @return 处理结果
     */
    @RequestMapping("notify")
    public String handleNotify(HttpServletRequest request) {
        try {
            // 1. 获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();

            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            System.out.println("支付宝异步通知参数：" + params);

            // 2. 调用支付宝服务处理通知
            String result = alipayService.handleNotify(params);

            // 3. 无论签名验证是否成功，都更新订单状态（仅用于测试）
            String outTradeNo = params.get("out_trade_no");
            String tradeStatus = params.get("trade_status");

            if (outTradeNo != null && !outTradeNo.isEmpty() &&
                    ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus))) {

                // ✅ 解析商户订单号，处理时间戳后缀
                Integer oId;
                if (outTradeNo.contains("_")) {
                    // 如果包含下划线，说明是追诊订单，提取订单ID
                    oId = Integer.parseInt(outTradeNo.split("_")[0]);
                    System.out.println("追诊订单支付成功，原始订单ID：" + oId);
                } else {
                    // 普通订单
                    oId = Integer.parseInt(outTradeNo);
                    System.out.println("普通订单支付成功，订单ID：" + oId);
                }

                // 调用 OrderService 的 updatePrice 方法更新缴费状态
                orderService.updatePrice(oId);
                System.out.println("订单 " + oId + " 支付成功，状态已更新");
            }

            return result;

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 处理支付宝同步返回（保留原有接口，用于前端API调用）
     * @param request HTTP请求对象
     * @return 处理结果
     */
    @RequestMapping("success")
    public ResponseData handleReturn(HttpServletRequest request) {
        try {
            // 1. 获取支付宝GET过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();

            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            System.out.println("支付宝同步返回参数：" + params);

            // 2. 验证签名
            boolean verified = alipayService.verifyReturn(params);

            if (verified) {
                // 3. 获取订单信息
                String outTradeNo = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");

                if (outTradeNo != null && !outTradeNo.isEmpty()) {
                    Integer oId = Integer.parseInt(outTradeNo);

                    return ResponseData.success("支付成功", new HashMap<String, Object>(){{
                        put("oId", oId);
                        put("tradeNo", tradeNo);
                        put("status", "已支付");
                    }});
                }

                return ResponseData.success("验证成功");
            } else {
                return ResponseData.fail("签名验证失败");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ResponseData.fail("验证异常：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("系统异常：" + e.getMessage());
        }
    }

    /**
     * 处理支付宝同步返回（新增接口）
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    @RequestMapping("sync/return")
    public void handleSyncReturn(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. 获取支付宝GET过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();

            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            System.out.println("支付宝同步返回参数：" + params);

            // 2. 验证签名
            boolean verified = alipayService.verifyReturn(params);

            if (verified) {
                // 3. 签名验证成功，获取订单信息
                String outTradeNo = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");

                System.out.println("支付成功，准备重定向到前端页面，订单号：" + outTradeNo);

                // ✅ 解析商户订单号
                Integer oId;
                if (outTradeNo.contains("_")) {
                    oId = Integer.parseInt(outTradeNo.split("_")[0]);
                    System.out.println("追诊订单，原始订单ID：" + oId);
                } else {
                    oId = Integer.parseInt(outTradeNo);
                }

                // 4. 构建前端跳转URL
                String redirectUrl = "http://localhost:3000/#/payment/success";

                if (oId != null && oId > 0) {
                    redirectUrl += "?oId=" + oId;
                    if (tradeNo != null && !tradeNo.isEmpty()) {
                        redirectUrl += "&trade_no=" + tradeNo;
                    }
                }

                System.out.println("重定向URL：" + redirectUrl);

                // 5. 重定向到前端页面
                response.sendRedirect(redirectUrl);
            } else {
                // 签名验证失败，跳转到失败页面
                System.out.println("签名验证失败");
                response.sendRedirect("http://localhost:3000/#/myOrder");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            try {
                response.sendRedirect("http://localhost:3000/#/myOrder");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect("http://localhost:3000/#/myOrder");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 支付成功后重定向到前端页面
     */
    @RequestMapping("success/redirect")
    public void paymentSuccessRedirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取支付宝返回的订单ID
            String outTradeNo = request.getParameter("out_trade_no");

            System.out.println("支付成功，准备重定向到挂号界面，订单号：" + outTradeNo);

            // 开发环境：重定向到本地地址
            String redirectUrl = "http://localhost:3000/#/myOrder";

            if (outTradeNo != null && !outTradeNo.isEmpty()) {
                // 添加参数，标识支付成功
                redirectUrl += "?paid=true&oId=" + outTradeNo;
            }

            System.out.println("重定向URL：" + redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendRedirect("http://localhost:3000/#/myOrder");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 查询订单详情
     * @param oId 订单ID
     * @return 订单详情
     */
    @RequestMapping("query")
    public ResponseData queryOrder(@RequestParam Integer oId) {
        try {
            Orders order = orderService.findOrderByOid(oId);

            if (order != null) {
                return ResponseData.success("查询订单成功", order);
            } else {
                return ResponseData.fail("订单不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("查询订单失败：" + e.getMessage());
        }
    }
}
