import request from "./request.js";

/**
 * 调用AI导诊接口
 * @param {Object} data - 导诊请求数据
 * @param {String} data.symptoms - 症状描述
 * @param {String} data.patientInfo - 患者信息（可选）
 * @returns {Promise}
 */
export function getAiDiagnosis(data) {
    return request({
        url: "/ai/diagnosis",
        method: "post",
        data: data
    });
}

/**
 * 简化版AI导诊接口（GET请求）
 * @param {String} symptoms - 症状描述
 * @returns {Promise}
 */
export function getSimpleAiDiagnosis(symptoms) {
    return request({
        url: "/ai/diagnosis/simple",
        method: "get",
        params: { symptoms: symptoms }
    });
}
