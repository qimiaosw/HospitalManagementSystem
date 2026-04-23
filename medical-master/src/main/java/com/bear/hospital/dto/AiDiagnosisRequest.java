package com.bear.hospital.dto;

/**
 * AI导诊请求DTO
 */
public class AiDiagnosisRequest {
    /**
     * 症状描述
     */
    private String symptoms;

    /**
     * 患者信息（可选）
     */
    private String patientInfo;

    public AiDiagnosisRequest() {}

    public AiDiagnosisRequest(String symptoms, String patientInfo) {
        this.symptoms = symptoms;
        this.patientInfo = patientInfo;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(String patientInfo) {
        this.patientInfo = patientInfo;
    }
}
