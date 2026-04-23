package com.bear.hospital.dto;

/**
 * AI导诊响应DTO
 */
public class AiDiagnosisResponse {
    /**
     * 状态码：200-成功，400-参数错误，500-服务器错误
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 推荐科室
     */
    private String recommendedDepartment;

    /**
     * 医疗建议
     */
    private String advice;

    /**
     * 严重程度：轻微/中等/严重
     */
    private String severityLevel;

    /**
     * 是否需要紧急就医：是/否
     */
    private String needEmergency;

    public AiDiagnosisResponse() {}

    public AiDiagnosisResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecommendedDepartment() {
        return recommendedDepartment;
    }

    public void setRecommendedDepartment(String recommendedDepartment) {
        this.recommendedDepartment = recommendedDepartment;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getNeedEmergency() {
        return needEmergency;
    }

    public void setNeedEmergency(String needEmergency) {
        this.needEmergency = needEmergency;
    }
}
