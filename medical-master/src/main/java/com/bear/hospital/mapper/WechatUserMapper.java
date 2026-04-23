// WechatUserMapper.java
package com.bear.hospital.mapper;

import com.bear.hospital.pojo.WechatUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WechatUserMapper {

    // 根据openid查询微信用户
    WechatUser findByOpenId(String wOpenId);

    // 根据unionid查询微信用户
    WechatUser findByUnionId(String wUnionId);

    // 根据患者ID查询微信绑定
    List<WechatUser> findByPatientId(Integer pId);

    // 根据医生ID查询微信绑定
    List<WechatUser> findByDoctorId(Integer dId);

    // 根据管理员ID查询微信绑定
    List<WechatUser> findByAdminId(Integer aId);

    // 插入微信用户
    int insert(WechatUser wechatUser);

    // 更新微信用户信息
    int update(WechatUser wechatUser);

    // 绑定患者
    int bindPatient(String wOpenId, Integer pId);

    // 绑定医生
    int bindDoctor(String wOpenId, Integer dId);

    // 绑定管理员
    int bindAdmin(String wOpenId, Integer aId);

    // 解除绑定
    int unbind(String wOpenId);
}