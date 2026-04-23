// WechatQrStateMapper.java
package com.bear.hospital.mapper;

import com.bear.hospital.pojo.WechatQrState;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WechatQrStateMapper {

    // 插入二维码状态
    int insert(WechatQrState qrState);

    // 根据qrUuid查询
    WechatQrState findByQrUuid(String qrUuid);

    // 根据qrState查询
    WechatQrState findByQrState(String qrState);

    // 更新状态
    int update(WechatQrState qrState);

    // 删除过期记录
    int deleteExpired(long currentTime);
}