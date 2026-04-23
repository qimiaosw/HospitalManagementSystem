package com.bear.hospital.service;

import com.bear.hospital.pojo.ChatMessage;
import com.bear.hospital.pojo.DiagnosisRecord;

import java.util.List;

/**
 * 对话服务接口
 */
public interface ChatService {

    /**
     * 保存用户消息
     *
     * @param sessionId 会话ID
     * @param patientId 患者ID
     * @param content 消息内容
     * @return 保存的消息对象
     */
    ChatMessage saveUserMessage(String sessionId, Integer patientId, String content);

    /**
     * 保存AI回复
     *
     * @param sessionId 会话ID
     * @param patientId 患者ID
     * @param content 消息内容
     * @return 保存的消息对象
     */
    ChatMessage saveAssistantMessage(String sessionId, Integer patientId, String content);

    /**
     * 查询会话的所有消息
     *
     * @param sessionId 会话ID
     * @return 消息列表
     */
    List<ChatMessage> getChatHistory(String sessionId);

    /**
     * 查询会话的所有消息（带患者ID校验）
     *
     * @param sessionId 会话ID
     * @param patientId 患者ID
     * @return 消息列表
     */
    List<ChatMessage> getChatHistory(String sessionId, Integer patientId);

    /**
     * 查询患者最近的会话列表
     *
     * @param patientId 患者ID
     * @return 会话ID列表
     */
    List<String> getRecentSessions(Integer patientId);

    /**
     * 清空会话
     *
     * @param sessionId 会话ID
     * @return 删除的记录数
     */
    Integer clearSession(String sessionId);

    /**
     * 清空会话（带患者ID校验）
     *
     * @param sessionId 会话ID
     * @param patientId 患者ID
     * @return 删除的记录数
     */
    Integer clearSession(String sessionId, Integer patientId);

    /**
     * 保存诊断记录
     *
     * @param record 诊断记录
     * @return 保存结果
     */
    Boolean saveDiagnosisRecord(DiagnosisRecord record);
}
