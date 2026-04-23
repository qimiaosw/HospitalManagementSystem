package com.bear.hospital.service.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bear.hospital.mapper.ChatMessageMapper;
import com.bear.hospital.mapper.DiagnosisRecordMapper;
import com.bear.hospital.mapper.DoctorMapper;
import com.bear.hospital.pojo.*;
import com.bear.hospital.service.AIService;
import com.bear.hospital.service.ChatService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * AI诊断服务实现类
 */
@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Value("${zhipu.api.key}")
    private String apiKey;

    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final String MODEL_NAME = "glm-4";
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * AI智能导诊（单次诊断）
     */
    @Override
    public DiagnosisResult smartDiagnosis(String symptoms, Integer patientId) {
        try {
            // 生成会话ID
            String sessionId = generateSessionId();

            // 保存用户消息
            chatService.saveUserMessage(sessionId, patientId, symptoms);

            // 构建Prompt
            String prompt = buildSingleDiagnosisPrompt(symptoms);

            // 调用智谱AI API
            String response = callZhipuAPI(prompt);

            // 解析响应
            DiagnosisResult result = parseDiagnosisResponse(response, sessionId);

            // 保存AI回复
            if (result != null) {
                String aiResponse = formatAIResponse(result);
                chatService.saveAssistantMessage(sessionId, patientId, aiResponse);
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AI智能导诊（多轮对话）
     */
    @Override
    public DiagnosisResult smartDiagnosisWithChat(String sessionId, String symptoms, Integer patientId) {
        try {
            // 如果sessionId为空，生成新的会话ID
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = generateSessionId();
            }

            // 保存用户消息
            chatService.saveUserMessage(sessionId, patientId, symptoms);

            // 获取对话历史
            List<ChatMessage> chatHistory = chatService.getChatHistory(sessionId, patientId);

            // 构建多轮对话Prompt
            String prompt = buildMultiTurnDiagnosisPrompt(chatHistory, symptoms);

            // 调用智谱AI API
            String response = callZhipuAPI(prompt);

            // 解析响应
            DiagnosisResult result = parseDiagnosisResponse(response, sessionId);

            // 保存AI回复
            if (result != null) {
                String aiResponse = formatAIResponse(result);
                chatService.saveAssistantMessage(sessionId, patientId, aiResponse);

                // 保存诊断记录
                if (result.getDepartment() != null) {
                    DiagnosisRecord record = new DiagnosisRecord();
                    record.setPId(patientId);
                    record.setSymptoms(symptoms);
                    record.setDiseases(String.join(",", result.getDiseases()));
                    record.setDepartment(result.getDepartment());
                    record.setSeverity(result.getSeverity());
                    record.setNeedImmediateCare(result.getNeedImmediateCare() ? 1 : 0);
                    record.setAdvice(result.getAdvice());
                    record.setReasoning(result.getReasoning());
                    record.setSessionId(sessionId);
                    chatService.saveDiagnosisRecord(record);
                }
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成会话ID
     */
    @Override
    public String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 构建单次诊断Prompt
     */
    private String buildSingleDiagnosisPrompt(String symptoms) {
        return "你是一位经验丰富的医生，擅长病情分析和科室推荐。\n\n" +
                "患者症状描述：\n" +
                symptoms + "\n\n" +
                "请根据以上症状，完成以下分析任务：\n" +
                "1. 识别患者可能患有的疾病（列出3-5种最可能的疾病，按可能性排序）\n" +
                "2. 推荐最合适的挂号科室\n" +
                "3. 评估病情严重程度（轻微/中等/严重/紧急）\n" +
                "4. 判断是否需要立即就医（true/false）\n" +
                "5. 提供3-5条就医建议或注意事项\n\n" +
                "重要提示：\n" +
                "- 仅供参考，不能替代专业医生的诊断\n" +
                "- 如果症状严重，建议立即就医\n" +
                "- 保持客观，不做主观臆断\n\n" +
                "请严格按照以下JSON格式返回结果，不要包含任何其他内容：\n\n" +
                "{\n" +
                "  \"diseases\": [\"疾病1\", \"疾病2\", \"疾病3\", \"疾病4\", \"疾病5\"],\n" +
                "  \"department\": \"科室名称\",\n" +
                "  \"severity\": \"严重程度\",\n" +
                "  \"needImmediateCare\": true/false,\n" +
                "  \"advice\": \"就医建议1；就医建议2；就医建议3\",\n" +
                "  \"reasoning\": \"简要说明推荐该科室的理由\"\n" +
                "}";
    }

    /**
     * 构建多轮对话诊断Prompt
     */
    private String buildMultiTurnDiagnosisPrompt(List<ChatMessage> chatHistory, String currentSymptoms) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位经验丰富的医生，擅长病情分析和科室推荐。");
        prompt.append("请根据患者的症状描述和之前的对话历史，进行诊断分析。\n\n");

        // 添加对话历史
        if (chatHistory != null && !chatHistory.isEmpty()) {
            prompt.append("对话历史：\n");
            for (ChatMessage message : chatHistory) {
                if ("user".equals(message.getRole())) {
                    prompt.append("患者：").append(message.getContent()).append("\n");
                } else if ("assistant".equals(message.getRole())) {
                    prompt.append("医生：").append(message.getContent()).append("\n");
                }
            }
            prompt.append("\n");
        }

        // 添加当前症状
        prompt.append("患者最新症状描述：\n");
        prompt.append(currentSymptoms);
        prompt.append("\n\n");

        prompt.append("请根据以上信息，完成以下分析任务：\n");
        prompt.append("1. 识别患者可能患有的疾病（列出3-5种最可能的疾病）\n");
        prompt.append("2. 推荐最合适的挂号科室\n");
        prompt.append("3. 评估病情严重程度（轻微/中等/严重/紧急）\n");
        prompt.append("4. 判断是否需要立即就医（true/false）\n");
        prompt.append("5. 提供3-5条就医建议或注意事项\n");
        prompt.append("6. 如果需要更多信息，可以提出追问\n\n");

        prompt.append("请严格按照以下JSON格式返回结果：\n\n");
        prompt.append("{\n");
        prompt.append("  \"diseases\": [\"疾病1\", \"疾病2\", \"疾病3\"],\n");
        prompt.append("  \"department\": \"科室名称\",\n");
        prompt.append("  \"severity\": \"严重程度\",\n");
        prompt.append("  \"needImmediateCare\": true/false,\n");
        prompt.append("  \"advice\": \"就医建议\",\n");
        prompt.append("  \"reasoning\": \"推荐理由\",\n");
        prompt.append("  \"followUpQuestion\": \"追问问题（如果需要更多信息）\"\n");
        prompt.append("}");

        return prompt.toString();
    }

    /**
     * 调用智谱AI API
     */
    private String callZhipuAPI(String prompt) throws IOException {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL_NAME);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        requestBody.put("messages", messages);

        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        // 发送请求
        String jsonBody = JSON.toJSONString(requestBody);
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("API调用失败: " + response);
        }

        return response.body().string();
    }

    /**
     * 解析诊断响应
     */
    private DiagnosisResult parseDiagnosisResponse(String response, String sessionId) {
        try {
            JSONObject json = JSON.parseObject(response);

            // 提取AI返回的内容
            String content = json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            // 解析JSON格式的诊断结果
            JSONObject diagnosis = JSON.parseObject(content);

            DiagnosisResult result = new DiagnosisResult();
            result.setDiseases(diagnosis.getJSONArray("diseases").toJavaList(String.class));
            result.setDepartment(diagnosis.getString("department"));
            result.setSeverity(diagnosis.getString("severity"));
            result.setNeedImmediateCare(diagnosis.getBoolean("needImmediateCare"));
            result.setAdvice(diagnosis.getString("advice"));
            result.setReasoning(diagnosis.getString("reasoning"));

            // 确保 sessionId 是字符串
            if (sessionId != null && sessionId instanceof String) {
                result.setSessionId(sessionId);
            } else {
                // 如果不是字符串，生成一个新的 sessionId
                result.setSessionId(generateSessionId());
            }

            // 查询推荐医生
            if (result.getDepartment() != null && !result.getDepartment().isEmpty()) {
                List<Doctor> doctors = doctorMapper.findDoctorBySection(result.getDepartment());
                result.setRecommendedDoctors(doctors);
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 格式化AI回复（用于保存到对话历史）
     */
    private String formatAIResponse(DiagnosisResult result) {
        StringBuilder response = new StringBuilder();

        response.append("根据您的症状，我的分析如下：\n\n");

        // 可能的疾病
        response.append("【可能的疾病】\n");
        for (int i = 0; i < result.getDiseases().size(); i++) {
            response.append((i + 1)).append(". ").append(result.getDiseases().get(i)).append("\n");
        }
        response.append("\n");

        // 推荐科室
        response.append("【推荐科室】").append(result.getDepartment()).append("\n");
        response.append("【严重程度】").append(result.getSeverity()).append("\n");

        // 是否需要立即就医
        if (result.getNeedImmediateCare()) {
            response.append("【紧急程度】需要立即就医\n");
        } else {
            response.append("【紧急程度】无需紧急就医\n");
        }

        // 就医建议
        response.append("\n【就医建议】\n");
        String[] adviceArray = result.getAdvice().split("；");
        for (String advice : adviceArray) {
            response.append("• ").append(advice.trim()).append("\n");
        }

        // 推荐理由
        response.append("\n【推荐理由】\n").append(result.getReasoning());

        return response.toString();
    }
}
