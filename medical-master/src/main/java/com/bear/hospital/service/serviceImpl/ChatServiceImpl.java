package com.bear.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bear.hospital.mapper.ChatMessageMapper;
import com.bear.hospital.mapper.DiagnosisRecordMapper;
import com.bear.hospital.pojo.ChatMessage;
import com.bear.hospital.pojo.DiagnosisRecord;
import com.bear.hospital.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 对话服务实现类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private DiagnosisRecordMapper diagnosisRecordMapper;

    /**
     * 保存用户消息
     */
    @Override
    public ChatMessage saveUserMessage(String sessionId, Integer patientId, String content) {
        ChatMessage message = new ChatMessage();
        message.setPId(patientId);
        message.setSessionId(sessionId);
        message.setRole("user");
        message.setContent(content);
        message.setType("text");
        message.setMessageTime(new Date());

        chatMessageMapper.insert(message);
        return message;
    }

    /**
     * 保存AI回复
     */
    @Override
    public ChatMessage saveAssistantMessage(String sessionId, Integer patientId, String content) {
        ChatMessage message = new ChatMessage();
        message.setPId(patientId);
        message.setSessionId(sessionId);
        message.setRole("assistant");
        message.setContent(content);
        message.setType("text");
        message.setMessageTime(new Date());

        chatMessageMapper.insert(message);
        return message;
    }

    /**
     * 查询会话的所有消息
     */
    @Override
    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageMapper.findBySessionId(sessionId);
    }

    /**
     * 查询会话的所有消息（带患者ID校验）
     */
    @Override
    public List<ChatMessage> getChatHistory(String sessionId, Integer patientId) {
        return chatMessageMapper.findBySessionIdAndPId(sessionId, patientId);
    }

    /**
     * 查询患者最近的会话列表
     */
    @Override
    public List<String> getRecentSessions(Integer patientId) {
        return chatMessageMapper.findRecentSessionsByPId(patientId);
    }

    /**
     * 清空会话
     */
    @Override
    public Integer clearSession(String sessionId) {
        return chatMessageMapper.deleteBySessionId(sessionId);
    }

    /**
     * 清空会话（带患者ID校验）
     */
    @Override
    public Integer clearSession(String sessionId, Integer patientId) {
        return chatMessageMapper.deleteBySessionIdAndPId(sessionId, patientId);
    }

    /**
     * 保存诊断记录
     */
    @Override
    public Boolean saveDiagnosisRecord(DiagnosisRecord record) {
        try {
            // 设置诊断时间
            record.setDiagnosisTime(new Date());

            // 插入记录
            int result = diagnosisRecordMapper.insert(record);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
