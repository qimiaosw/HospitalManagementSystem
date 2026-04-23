package com.bear.hospital.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * AI诊断记录实体类
 */
@Data
@TableName("ai_diagnosis_record")
public class DiagnosisRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @JsonProperty("id")
    private Integer id;

    /**
     * 患者ID
     */
    @JsonProperty("pId")
    private Integer pId;

    /**
     * 症状描述
     */
    @JsonProperty("symptoms")
    private String symptoms;

    /**
     * 可能的疾病（逗号分隔）
     */
    @JsonProperty("diseases")
    private String diseases;

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
     * 是否需要立即就医（0否/1是）
     */
    @JsonProperty("needImmediateCare")
    private Integer needImmediateCare;

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
     * 诊断时间
     */
    @JsonProperty("diagnosisTime")
    private Date diagnosisTime;

    /**
     * 会话ID（用于多轮对话）
     */
    @JsonProperty("sessionId")
    private String sessionId;
}
