package com.bear.hospital.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智谱AI服务类
 * 用于调用智谱AI的GLM模型进行智能导诊
 */
@Service
public class ZhipuAIService {

    @Value("${zhipu.api.key:}")
    private String apiKey;

    @Value("${zhipu.api.url:https://open.bigmodel.cn/api/paas/v4/chat/completions}")
    private String apiUrl;

    private final OkHttpClient client;

    public ZhipuAIService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 调用智谱AI API进行智能导诊
     *
     * @param symptoms 患者症状描述
     * @param patientInfo 患者信息（可选）
     * @return AI诊断结果（JSON格式字符串）
     * @throws RuntimeException API调用失败时抛出异常
     */
    public String getAiDiagnosis(String symptoms, String patientInfo) {
        try {
            // 生成JWT Token
            String jwtToken = generateJwtToken(apiKey);

            // 构建请求体
            JSONObject requestBody = new JSONObject();

            // 设置模型（使用智谱GLM-4）
            requestBody.put("model", "glm-4");

            // 构建系统提示词 - 增强JSON格式要求
            String systemPrompt = "你是一个专业的医院智能导诊助手。请根据患者的症状描述，提供以下信息：\n" +
                    "1. 推荐就诊科室\n" +
                    "2. 初步判断严重程度（轻微/中等/严重）\n" +
                    "3. 是否需要紧急就医（是/否）\n" +
                    "4. 医疗建议\n\n" +
                    "【重要】输出格式要求：\n" +
                    "1. 必须严格返回JSON格式，不要有任何其他文字\n" +
                    "2. 不要使用Markdown代码块标记（如```json或```）\n" +
                    "3. 直接返回纯JSON文本，示例格式如下：\n" +
                    "{\n" +
                    "  \"recommendedDepartment\": \"神经内科\",\n" +
                    "  \"severityLevel\": \"中等\",\n" +
                    "  \"needEmergency\": \"否\",\n" +
                    "  \"advice\": \"建议及时就医检查\"\n" +
                    "}\n" +
                    "4. 如果无法判断，请根据症状给出合理的推测\n" +
                    "5. 严重程度只能是：轻微、中等、严重 三个选项之一\n" +
                    "6. 是否紧急只能是：是、否 两个选项之一\n\n" +
                    "注意事项：\n" +
                    "- 只提供导诊建议，不进行确诊\n" +
                    "- 对于严重症状，建议立即就医\n" +
                    "- 回复要简洁明了，不要添加任何解释性文字\n" +
                    "- 必须严格返回JSON格式，不要包含任何Markdown标记";

            // 构建消息数组
            JSONArray messages = new JSONArray();

            // 添加系统消息
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            messages.add(systemMessage);

            // 构建用户消息
            String userContent = "患者症状：" + symptoms;
            if (patientInfo != null && !patientInfo.isEmpty()) {
                userContent += "\n患者信息：" + patientInfo;
            }

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userContent);
            messages.add(userMessage);

            requestBody.put("messages", messages);

            // 设置参数
            requestBody.put("temperature", 0.3);  // 降低温度以获得更稳定的输出
            requestBody.put("max_tokens", 300);   // 限制token数量
            requestBody.put("top_p", 0.8);         // 添加top_p参数

            System.out.println("发送到智谱AI API的请求: " + requestBody.toJSONString());

            // 创建HTTP请求
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    requestBody.toJSONString()
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Authorization", "Bearer " + jwtToken)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            // 发送请求
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "无错误信息";
                throw new IOException("API请求失败，状态码: " + response.code() + ", 错误信息: " + errorBody);
            }

            // 解析响应
            String responseBody = response.body().string();
            System.out.println("智谱AI API原始响应: " + responseBody);

            JSONObject jsonResponse = JSON.parseObject(responseBody);

            // 提取AI返回的内容
            if (jsonResponse.containsKey("choices") &&
                    jsonResponse.getJSONArray("choices").size() > 0) {

                JSONObject firstChoice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                String content = message.getString("content");

                System.out.println("提取到的AI原始内容: " + content);

                // 清理Markdown代码块标记
                String cleanedContent = cleanMarkdownCodeBlock(content);
                System.out.println("清理后的内容: " + cleanedContent);

                // 验证是否为有效JSON
                if (!isValidJson(cleanedContent)) {
                    System.out.println("AI返回的内容不是有效的JSON格式，尝试提取JSON...");
                    cleanedContent = extractJsonFromText(cleanedContent);
                }

                // 如果仍然不是有效JSON，使用默认值
                if (!isValidJson(cleanedContent)) {
                    System.out.println("无法提取有效JSON，使用默认响应");
                    cleanedContent = getDefaultResponse(symptoms);
                }

                return cleanedContent;
            } else {
                throw new RuntimeException("API返回数据格式异常，未找到choices字段");
            }

        } catch (Exception e) {
            System.err.println("调用智谱AI API失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("调用智谱AI API失败：" + e.getMessage(), e);
        }
    }

    /**
     * 清理Markdown代码块标记
     *
     * @param content 原始内容
     * @return 清理后的纯JSON内容
     */
    private String cleanMarkdownCodeBlock(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        // 移除 ```json 标记
        content = content.replaceAll("```json", "");

        // 移除 ``` 标记
        content = content.replaceAll("```", "");

        // 移除首尾的空白字符
        content = content.trim();

        return content;
    }

    /**
     * 从文本中提取JSON内容
     *
     * @param text 包含JSON的文本
     * @return 提取的JSON字符串
     */
    private String extractJsonFromText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        // 使用正则表达式提取JSON对象
        Pattern pattern = Pattern.compile("\\{[^{}]*\"recommendedDepartment\"[^{}]*\\}");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    /**
     * 验证字符串是否为有效的JSON
     *
     * @param str 要验证的字符串
     * @return 是否为有效JSON
     */
    private boolean isValidJson(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            JSON.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成默认响应（当AI无法返回有效JSON时使用）
     *
     * @param symptoms 患者症状
     * @return 默认JSON响应
     */
    private String getDefaultResponse(String symptoms) {
        JSONObject defaultResponse = new JSONObject();
        defaultResponse.put("recommendedDepartment", "普通内科");
        defaultResponse.put("severityLevel", "中等");
        defaultResponse.put("needEmergency", "否");
        defaultResponse.put("advice", "根据您的症状，建议到普通内科就诊。如症状持续或加重，请及时就医。");

        return defaultResponse.toJSONString();
    }

    /**
     * 生成智谱AI的JWT Token
     *
     * @param apiKey API Key（格式：id.secret）
     * @return JWT Token
     */
    private String generateJwtToken(String apiKey) {
        try {
            // API Key格式：id.secret
            String[] parts = apiKey.split("\\.");
            if (parts.length != 2) {
                throw new IllegalArgumentException("API Key格式不正确，应为 id.secret 格式");
            }

            String id = parts[0];
            String secret = parts[1];

            // 智谱AI支持直接使用API Key
            return apiKey;

        } catch (Exception e) {
            System.err.println("生成JWT Token失败: " + e.getMessage());
            throw new RuntimeException("生成JWT Token失败：" + e.getMessage());
        }
    }
}
