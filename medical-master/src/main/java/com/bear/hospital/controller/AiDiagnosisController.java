package com.bear.hospital.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bear.hospital.dto.AiDiagnosisRequest;
import com.bear.hospital.dto.AiDiagnosisResponse;
import com.bear.hospital.service.ZhipuAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AiDiagnosisController {

    @Autowired
    private ZhipuAIService zhipuAIService;

    /**
     * AI智能导诊接口
     * 接收前端用户输入的症状描述和患者信息，调用智谱AI API返回诊断建议
     *
     * @param request 导诊请求，包含症状和患者信息
     * @return 导诊结果，包含推荐科室、严重程度、建议措施等
     */
    @PostMapping("/diagnosis")
    public AiDiagnosisResponse getDiagnosis(@RequestBody AiDiagnosisRequest request) {
        try {
            // 参数验证
            if (request.getSymptoms() == null || request.getSymptoms().trim().isEmpty()) {
                return new AiDiagnosisResponse(400, "症状描述不能为空");
            }

            System.out.println("收到AI导诊请求 - 症状: " + request.getSymptoms());
            if (request.getPatientInfo() != null && !request.getPatientInfo().isEmpty()) {
                System.out.println("患者信息: " + request.getPatientInfo());
            }

            // 调用智谱AI API
            String aiResponse = zhipuAIService.getAiDiagnosis(
                    request.getSymptoms(),
                    request.getPatientInfo()
            );

            System.out.println("智谱AI API返回: " + aiResponse);

            // 解析AI返回的JSON
            JSONObject aiResult = JSON.parseObject(aiResponse);

            // 构建响应对象
            AiDiagnosisResponse response = new AiDiagnosisResponse();
            response.setCode(200);
            response.setMessage("导诊成功");
            response.setRecommendedDepartment(aiResult.getString("recommendedDepartment"));
            response.setSeverityLevel(aiResult.getString("severityLevel"));
            response.setNeedEmergency(aiResult.getString("needEmergency"));
            response.setAdvice(aiResult.getString("advice"));

            System.out.println("导诊结果 - 推荐科室: " + response.getRecommendedDepartment());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            AiDiagnosisResponse response = new AiDiagnosisResponse();
            response.setCode(500);
            response.setMessage("导诊失败：" + e.getMessage());
            return response;
        }
    }
}
