package com.bear.hospital.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 多轮对话消息实体类
 */
@Data
@TableName("ai_chat_message")
public class ChatMessage {

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
     * 会话ID
     */
    @JsonProperty("sessionId")
    private String sessionId;

    /**
     * 消息角色（user/assistant/system）
     */
    @JsonProperty("role")
    private String role;

    /**
     * 消息内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 消息类型（text/image）
     */
    @JsonProperty("type")
    private String type;

    /**
     * 消息时间
     */
    @JsonProperty("messageTime")
    private Date messageTime;
}
