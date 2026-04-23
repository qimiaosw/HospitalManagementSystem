package com.bear.hospital.service;

import com.bear.hospital.pojo.DiagnosisResult;

/**
 * AI诊断服务接口
 */
public interface AIService {

    /**
     * AI智能导诊（单次诊断）
     *
     * @param symptoms 症状描述
     * @param patientId 患者ID
     * @return 诊断结果
     */
    DiagnosisResult smartDiagnosis(String symptoms, Integer patientId);

    /**
     * AI智能导诊（多轮对话）
     *
     * @param sessionId 会话ID
     * @param symptoms 症状描述
     * @param patientId 患者ID
     * @return 诊断结果
     */
    DiagnosisResult smartDiagnosisWithChat(String sessionId, String symptoms, Integer patientId);

    /**
     * 生成会话ID
     *
     * @return 会话ID
     */
    String generateSessionId();
}
