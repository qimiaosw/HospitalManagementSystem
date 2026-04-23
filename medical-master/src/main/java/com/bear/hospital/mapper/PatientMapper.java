// PatientMapper.java - 添加查询方法
package com.bear.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.hospital.pojo.Patient;
import org.apache.ibatis.annotations.Param;

public interface PatientMapper extends BaseMapper<Patient> {
    /**
     * 统计患者男女人数
     */
    Integer patientAge(@Param("startAge") int startAge, @Param("endAge") int endAge);

    /**
     * 根据手机号查询患者信息（新增）
     */
    Patient selectByPhone(@Param("phone") String phone);
}