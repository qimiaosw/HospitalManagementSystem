package com.bear.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.hospital.pojo.Patient;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 邮箱验证码相关数据访问接口
 */
public interface EmailMapper extends BaseMapper<Patient> {
/**
    根据手机号查询患者邮箱
     */
    String selectEmailByPhone(@Param("phone") String phone);

    /**
     * 根据手机号查询患者信息
     */
    Patient selectByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询患者信息
     */
    Patient selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询患者ID
     */
    Integer selectIdByPhone(@Param("phone") String phone);

    /**
     * 更新邮箱验证状态
     */
    int updateEmailVerified(@Param("phone") String phone, @Param("verified") Integer verified);

    /**
     * 检查手机号是否已存在
     */
    int countByPhone(@Param("phone") String phone);

    /**
     * 检查邮箱是否已存在
     */
    int countByEmail(@Param("email") String email);

    /**
     * 绑定手机号和邮箱
     */
    int bindPhoneAndEmail(@Param("phone") String phone, @Param("email") String email);

    /**
     * 根据手机号更新邮箱
     */
    int updateEmailByPhone(@Param("phone") String phone, @Param("email") String email);

    /**
     * 获取所有绑定邮箱的患者
     */
    List<Patient> selectAllBoundPatients();

    /**
     * 根据手机号删除患者（软删除）
     */
    int deleteByPhone(@Param("phone") String phone);

    /**
     * 验证手机号和邮箱是否匹配
     */
    int verifyPhoneAndEmail(@Param("phone") String phone, @Param("email") String email);

    /**
     * 批量查询手机号绑定的邮箱
     */
    @MapKey("p_phone")
    List<Map<String, Object>> selectEmailsByPhones(@Param("phones") List<String> phones);
    /**
     * 统计已绑定邮箱的患者数量
     */
    int countBoundPatients();
    /**
     * 根据邮箱查询手机号（新增）
     */
    String selectPhoneByEmail(@Param("email") String email);

}