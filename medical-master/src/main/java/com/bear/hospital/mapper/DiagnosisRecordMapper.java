package com.bear.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.hospital.pojo.DiagnosisRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI诊断记录Mapper接口
 */
@Mapper
public interface DiagnosisRecordMapper extends BaseMapper<DiagnosisRecord> {

    /**
     * 根据会话ID查询诊断记录
     *
     * @param sessionId 会话ID
     * @return 诊断记录列表
     */
    @Select("SELECT * FROM ai_diagnosis_record WHERE session_id = #{sessionId} ORDER BY diagnosis_time DESC LIMIT 10")
    List<DiagnosisRecord> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * 根据患者ID查询最近的诊断记录
     *
     * @param pId 患者ID
     * @return 诊断记录列表
     */
    @Select("SELECT * FROM ai_diagnosis_record WHERE p_id = #{pId} ORDER BY diagnosis_time DESC LIMIT 10")
    List<DiagnosisRecord> findRecentByPId(@Param("pId") Integer pId);

    /**
     * 统计患者的诊断次数
     *
     * @param pId 患者ID
     * @return 诊断次数
     */
    @Select("SELECT COUNT(*) FROM ai_diagnosis_record WHERE p_id = #{pId}")
    Integer countByPId(@Param("pId") Integer pId);
}
