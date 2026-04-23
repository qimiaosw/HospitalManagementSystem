package com.bear.hospital.controller;

import com.bear.hospital.pojo.ChatMessage;
import com.bear.hospital.service.ChatService;
import com.bear.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 对话控制器
 */
@RestController
@RequestMapping("chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 查询对话历史
     *
     * 接口地址：GET /chat/history
     *
     * @param sessionId 会话ID
     * @param patientId 患者ID
     * @return 对话历史
     */
    @GetMapping("history")
    public ResponseData getChatHistory(
            @RequestParam String sessionId,
            @RequestParam Integer patientId) {

        if (sessionId == null || sessionId.trim().isEmpty()) {
            return ResponseData.fail("会话ID不能为空");
        }

        if (patientId == null) {
            return ResponseData.fail("患者ID不能为空");
        }

        try {
            List<ChatMessage> history = chatService.getChatHistory(sessionId, patientId);
            return ResponseData.success("查询成功", history);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询最近的会话列表
     *
     * 接口地址：GET /chat/sessions
     *
     * @param patientId 患者ID
     * @return 会话列表
     */
    @GetMapping("sessions")
    public ResponseData getRecentSessions(@RequestParam Integer patientId) {

        if (patientId == null) {
            return ResponseData.fail("患者ID不能为空");
        }

        try {
            List<String> sessions = chatService.getRecentSessions(patientId);
            return ResponseData.success("查询成功", sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 清空会话
     *
     * 接口地址：DELETE /chat/session
     *
     * @param params 请求参数
     * @return 删除结果
     */
    @DeleteMapping("session")
    public ResponseData clearSession(@RequestBody Map<String, Object> params) {
        String sessionId = (String) params.get("sessionId");
        Integer patientId = null;

        if (params.get("patientId") != null) {
            patientId = Integer.parseInt(params.get("patientId").toString());
        }

        if (sessionId == null || sessionId.trim().isEmpty()) {
            return ResponseData.fail("会话ID不能为空");
        }

        try {
            Integer result;
            if (patientId != null) {
                // 带患者ID校验的删除
                result = chatService.clearSession(sessionId, patientId);
            } else {
                // 直接删除会话
                result = chatService.clearSession(sessionId);
            }

            if (result > 0) {
                return ResponseData.success("会话清空成功");
            } else {
                return ResponseData.fail("会话不存在或已被删除");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("清空失败：" + e.getMessage());
        }
    }
}
