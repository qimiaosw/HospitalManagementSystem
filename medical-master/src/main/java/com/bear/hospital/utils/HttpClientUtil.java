// HttpClientUtil.java
package com.bear.hospital.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class HttpClientUtil {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
            .readTimeout(10, TimeUnit.SECONDS)     // 读取超时
            .writeTimeout(10, TimeUnit.SECONDS)    // 写入超时
            .build();

    /**
     * GET请求
     * @param url 请求地址
     * @return 响应字符串
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * GET请求（带请求头）
     * @param url 请求地址
     * @param headers 请求头
     * @return 响应字符串
     */
    public static String doGet(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                System.err.println("GET请求失败: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET请求返回JSON对象
     * @param url 请求地址
     * @return JSON对象
     */
    public static JSONObject doGetJson(String url) {
        return doGetJson(url, null);
    }

    /**
     * GET请求返回JSON对象（带请求头）
     * @param url 请求地址
     * @param headers 请求头
     * @return JSON对象
     */
    public static JSONObject doGetJson(String url, Map<String, String> headers) {
        String result = doGet(url, headers);
        if (result != null && !result.isEmpty()) {
            try {
                return JSON.parseObject(result);
            } catch (Exception e) {
                System.err.println("JSON解析失败: " + e.getMessage());
                System.err.println("响应内容: " + result);
            }
        }
        return null;
    }

    /**
     * POST请求（表单形式）
     * @param url 请求地址
     * @param params 表单参数
     * @return 响应字符串
     */
    public static String doPostForm(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        return executeRequest(request);
    }

    /**
     * POST请求（JSON形式）
     * @param url 请求地址
     * @param jsonBody JSON字符串
     * @return 响应字符串
     */
    public static String doPostJson(String url, String jsonBody) {
        return doPostJson(url, jsonBody, null);
    }

    /**
     * POST请求（JSON形式，带请求头）
     * @param url 请求地址
     * @param jsonBody JSON字符串
     * @param headers 请求头
     * @return 响应字符串
     */
    public static String doPostJson(String url, String jsonBody, Map<String, String> headers) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        return executeRequest(request);
    }

    /**
     * POST请求返回JSON对象
     * @param url 请求地址
     * @param jsonBody JSON字符串
     * @return JSON对象
     */
    public static JSONObject doPostJsonReturnJson(String url, String jsonBody) {
        String result = doPostJson(url, jsonBody);
        if (result != null && !result.isEmpty()) {
            try {
                return JSON.parseObject(result);
            } catch (Exception e) {
                System.err.println("JSON解析失败: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 执行请求
     */
    private static String executeRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                System.err.println("请求失败: " + response.code() + " " + response.message());
                if (response.body() != null) {
                    String errorBody = response.body().string();
                    System.err.println("错误响应体: " + errorBody);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步GET请求
     * @param url 请求地址
     * @param callback 回调接口
     */
    public static void doGetAsync(String url, Callback callback) {
        doGetAsync(url, null, callback);
    }

    /**
     * 异步GET请求（带请求头）
     * @param url 请求地址
     * @param headers 请求头
     * @param callback 回调接口
     */
    public static void doGetAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步POST请求（表单形式）
     * @param url 请求地址
     * @param params 表单参数
     * @param callback 回调接口
     */
    public static void doPostFormAsync(String url, Map<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 获取微信access_token
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param code 授权码
     * @return access_token和openid
     */
    public static JSONObject getWechatAccessToken(String appId, String appSecret, String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + appId +
                "&secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code";

        return doGetJson(url);
    }

    /**
     * 获取微信用户信息
     * @param accessToken 访问令牌
     * @param openId 用户openid
     * @return 用户信息
     */
    public static JSONObject getWechatUserInfo(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + accessToken +
                "&openid=" + openId +
                "&lang=zh_CN";

        return doGetJson(url);
    }

    /**
     * 验证access_token是否有效
     * @param accessToken 访问令牌
     * @param openId 用户openid
     * @return 是否有效
     */
    public static boolean validateWechatToken(String accessToken, String openId) {
        String url = "https://api.weixin.qq.com/sns/auth" +
                "?access_token=" + accessToken +
                "&openid=" + openId;

        JSONObject json = doGetJson(url);
        if (json != null && json.containsKey("errcode")) {
            return json.getInteger("errcode") == 0;
        }
        return false;
    }

    /**
     * 刷新access_token
     * @param appId 应用ID
     * @param refreshToken 刷新令牌
     * @return 新的access_token
     */
    public static JSONObject refreshWechatToken(String appId, String refreshToken) {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
                "?appid=" + appId +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        return doGetJson(url);
    }

    /**
     * 发送短信验证码（示例，需要根据你的短信服务商调整）
     * @param phone 手机号
     * @param code 验证码
     * @param templateId 模板ID
     * @return 发送结果
     */
    public static JSONObject sendSmsCode(String phone, String code, String templateId) {
        // 这里需要根据你的短信服务商接口实现
        String url = "你的短信服务商接口地址";

        Map<String, String> params = new java.util.HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("templateId", templateId);

        String jsonBody = JSON.toJSONString(params);
        return doPostJsonReturnJson(url, jsonBody);
    }
}