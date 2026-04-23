import request from '@/utils/request'

/**
 * AI智能导诊（单次诊断）
 */
export function diagnosis(data) {
    return request({
        url: '/ai/diagnosis',
        method: 'post',
        data
    })
}

/**
 * AI智能导诊（多轮对话）
 */
export function diagnosisWithChat(data) {
    return request({
        url: '/ai/diagnosis/chat',
        method: 'post',
        data
    })
}

/**
 * 创建新会话
 */
export function createSession() {
    return request({
        url: '/ai/session/new',
        method: 'get'
    })
}

/**
 * 查询对话历史
 */
export function getChatHistory(params) {
    return request({
        url: '/chat/history',
        method: 'get',
        params
    })
}

/**
 * 查询最近的会话列表
 */
export function getRecentSessions(params) {
    return request({
        url: '/chat/sessions',
        method: 'get',
        params
    })
}

/**
 * 清空会话
 */
export function clearSession(data) {
    return request({
        url: '/chat/session',
        method: 'delete',
        data
    })
}
