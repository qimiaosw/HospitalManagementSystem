package com.bear.hospital.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * AI诊断结果实体类
 */
@Data
public class DiagnosisResult {

    /**
     * 可能的疾病列表
     */
    @JsonProperty("diseases")
    private List<String> diseases;

    /**
     * 推荐科室
     */
    @JsonProperty("department")
    private String department;

    /**
     * 严重程度（轻微/中等/严重/紧急）
     */
    @JsonProperty("severity")
    private String severity;

    /**
     * 是否需要立即就医
     */
    @JsonProperty("needImmediateCare")
    private Boolean needImmediateCare;

    /**
     * 就医建议
     */
    @JsonProperty("advice")
    private String advice;

    /**
     * 推荐理由
     */
    @JsonProperty("reasoning")
    private String reasoning;

    /**
     * 推荐的医生列表
     */
    @JsonProperty("recommendedDoctors")
    private List<Doctor> recommendedDoctors;

    /**
     * 会话ID
     */
    @JsonProperty("sessionId")
    private String sessionId;
}
