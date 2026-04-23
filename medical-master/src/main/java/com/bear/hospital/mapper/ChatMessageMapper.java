package com.bear.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.hospital.pojo.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI对话消息Mapper接口
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 根据会话ID查询所有消息（按时间正序）
     *
     * @param sessionId 会话ID
     * @return 消息列表
     */
    @Select("SELECT * FROM ai_chat_message WHERE session_id = #{sessionId} ORDER BY message_time ASC")
    List<ChatMessage> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据会话ID和患者ID查询所有消息
     *
     * @param sessionId 会话ID
     * @param pId 患者ID
     * @return 消息列表
     */
    @Select("SELECT * FROM ai_chat_message WHERE session_id = #{sessionId} AND p_id = #{pId} ORDER BY message_time ASC")
    List<ChatMessage> findBySessionIdAndPId(@Param("sessionId") String sessionId, @Param("pId") Integer pId);

    /**
     * 查询患者最近的会话列表
     *
     * @param pId 患者ID
     * @return 会话ID列表
     */
    @Select("SELECT DISTINCT session_id FROM ai_chat_message WHERE p_id = #{pId} ORDER BY message_time DESC LIMIT 10")
    List<String> findRecentSessionsByPId(@Param("pId") Integer pId);

    /**
     * 根据会话ID删除所有消息
     *
     * @param sessionId 会话ID
     * @return 删除的记录数
     */
    @Select("DELETE FROM ai_chat_message WHERE session_id = #{sessionId}")
    Integer deleteBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据会话ID和患者ID删除所有消息
     *
     * @param sessionId 会话ID
     * @param pId 患者ID
     * @return 删除的记录数
     */
    @Select("DELETE FROM ai_chat_message WHERE session_id = #{sessionId} AND p_id = #{pId}")
    Integer deleteBySessionIdAndPId(@Param("sessionId") String sessionId, @Param("pId") Integer pId);
}
