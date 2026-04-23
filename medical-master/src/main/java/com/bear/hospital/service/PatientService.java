// PatientService.java - 添加新方法
package com.bear.hospital.service;

import com.bear.hospital.pojo.Patient;

import java.util.HashMap;
import java.util.List;

public interface PatientService {
    /**
     * 登录数据校验
     * */
    Patient login(int pId, String pPassword);
    /**
     * 分页模糊查询所有患者信息
     */
    HashMap<String, Object> findAllPatients(int pageNumber, int size, String query);
    /**
     * 删除患者信息
     */
    Boolean deletePatient(int pId);
    /**
     * 根据患者id查询患者信息
     */
    Patient findPatientById(int pId);
    /**
     * 增加患者信息
     */
    Boolean addPatient(Patient patient);
    /**
     * 统计患者男女人数
     */
    List<Integer> patientAge();

    /**
     * 根据手机号查询患者信息（新增）
     */
    Patient findPatientByPhone(String phone);

    /**
     * 更新患者个人信息
     * @param patient 包含需要更新的患者信息（pId必须传入）
     * @return 是否更新成功
     */
    Boolean updatePatientInfo(Patient patient);

    /**
     * 找回密码（新增）
     * @param pEmail 邮箱
     * @param pPassword 新密码
     * @param code 验证码
     * @return 是否修改成功
     */
    Boolean findPassword(String pEmail, String pPassword, String code);

}