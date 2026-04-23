// WechatLoginServiceImpl.java
package com.bear.hospital.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bear.hospital.mapper.WechatUserMapper;
import com.bear.hospital.mapper.WechatQrStateMapper;
import com.bear.hospital.pojo.*;
import com.bear.hospital.service.WechatLoginService;
import com.bear.hospital.service.PatientService;
import com.bear.hospital.service.DoctorService;
import com.bear.hospital.service.AdminService;
import com.bear.hospital.utils.JwtUtil;
import com.bear.hospital.utils.ResponseData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class WechatLoginServiceImpl implements WechatLoginService {

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Autowired
    private WechatQrStateMapper wechatQrStateMapper;

    @Autowired
    private PatientService PatientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    // 微信测试号配置 - 需要替换为你的测试号信息
    private static final String TEST_APP_ID = "wx42a908e361f510c1";
    private static final String TEST_APP_SECRET = "a986d920d9aebea1521b37de194a71cb";
    private static final String REDIRECT_URI = "http://11qenr8159118.vicp.fun";

    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    public ResponseData generateQrCode() {
        try {
            // 生成唯一ID
            String qrUuid = UUID.randomUUID().toString().replace("-", "");
            String state = "STATE_" + qrUuid;

            // 构建微信授权URL
            String authUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                    "?appid=" + TEST_APP_ID +
                    "&redirect_uri=" + java.net.URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=" + state;

            // 保存二维码状态到数据库
            WechatQrState qrState = new WechatQrState();
            qrState.setQrUuid(qrUuid);
            qrState.setQrState(state);
            qrState.setQrStatus(0); // 等待扫码
            qrState.setQrCreateTime(System.currentTimeMillis());
            qrState.setQrExpireTime(System.currentTimeMillis() + 5 * 60 * 1000); // 5分钟过期

            wechatQrStateMapper.insert(qrState);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("qrId", qrUuid);
            result.put("authUrl", authUrl);
            result.put("qrUrl", "https://open.weixin.qq.com/connect/qrcode/" + TEST_APP_ID + "?state=" + state);
            result.put("expireTime", 300);

            return ResponseData.success("生成二维码成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("生成二维码失败");
        }
    }

    @Transactional
    @Override
    public ResponseData handleCallback(String code, String state) {
        try {
            // 1. 验证state
            WechatQrState qrState = wechatQrStateMapper.findByQrState(state);
            if (qrState == null) {
                return ResponseData.fail("无效的state参数");
            }

            // 2. 通过code获取access_token
            String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=" + TEST_APP_ID +
                    "&secret=" + TEST_APP_SECRET +
                    "&code=" + code +
                    "&grant_type=authorization_code";

            String tokenResponse = httpGet(tokenUrl);
            if (tokenResponse == null) {
                return ResponseData.fail("获取微信token失败");
            }

            JSONObject tokenJson = JSON.parseObject(tokenResponse);
            if (tokenJson.containsKey("errcode")) {
                return ResponseData.fail("微信授权失败: " + tokenJson.getString("errmsg"));
            }

            String accessToken = tokenJson.getString("access_token");
            String openId = tokenJson.getString("openid");

            // 3. 获取用户信息
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + accessToken +
                    "&openid=" + openId +
                    "&lang=zh_CN";

            String userInfoResponse = httpGet(userInfoUrl);
            if (userInfoResponse == null) {
                return ResponseData.fail("获取用户信息失败");
            }

            JSONObject userInfoJson = JSON.parseObject(userInfoResponse);
            if (userInfoJson.containsKey("errcode")) {
                return ResponseData.fail("获取用户信息失败: " + userInfoJson.getString("errmsg"));
            }

            // 4. 解析用户信息
            String nickname = userInfoJson.getString("nickname");
            String avatar = userInfoJson.getString("headimgurl");
            Integer gender = userInfoJson.getInteger("sex");
            String country = userInfoJson.getString("country");
            String province = userInfoJson.getString("province");
            String city = userInfoJson.getString("city");

            // 5. 保存/更新微信用户信息
            WechatUser wechatUser = wechatUserMapper.findByOpenId(openId);
            if (wechatUser == null) {
                wechatUser = new WechatUser();
                wechatUser.setWOpenId(openId);
                wechatUser.setWNickname(nickname);
                wechatUser.setWAvatar(avatar);
                wechatUser.setWGender(gender);
                wechatUser.setWCountry(country);
                wechatUser.setWProvince(province);
                wechatUser.setWCity(city);
                wechatUser.setWAccessToken(accessToken);
                wechatUserMapper.insert(wechatUser);
            } else {
                wechatUser.setWNickname(nickname);
                wechatUser.setWAvatar(avatar);
                wechatUser.setWGender(gender);
                wechatUser.setWAccessToken(accessToken);
                wechatUserMapper.update(wechatUser);
            }

            // 6. 更新二维码状态
            qrState.setWOpenId(openId);
            qrState.setQrStatus(1); // 已扫码
            qrState.setQrScanTime(System.currentTimeMillis());

            // 保存用户信息JSON
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("openId", openId);
            userInfoMap.put("nickname", nickname);
            userInfoMap.put("avatar", avatar);
            userInfoMap.put("gender", gender);
            userInfoMap.put("country", country);
            userInfoMap.put("province", province);
            userInfoMap.put("city", city);

            String userInfoJsonStr = JSON.toJSONString(userInfoMap);
            qrState.setQrUserInfo(userInfoJsonStr);
            wechatQrStateMapper.update(qrState);

            // 7. 检查是否已绑定系统用户
            Map<String, Object> result = new HashMap<>();
            result.put("qrId", qrState.getQrUuid());
            result.put("status", 1);
            result.put("userInfo", userInfoMap);

            if (wechatUser.getPId() != null || wechatUser.getDId() != null || wechatUser.getAId() != null) {
                // 已绑定，直接生成token
                String token = generateSystemToken(wechatUser);
                qrState.setQrSystemToken(token);
                qrState.setQrStatus(2); // 登录成功
                qrState.setQrLoginTime(System.currentTimeMillis());
                wechatQrStateMapper.update(qrState);

                result.put("status", 2);
                result.put("token", token);
                result.put("needBind", false);
            } else {
                // 未绑定，需要绑定
                result.put("needBind", true);
            }

            return ResponseData.success("处理回调成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("处理回调失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseData checkLoginStatus(String qrUuid) {
        try {
            WechatQrState qrState = wechatQrStateMapper.findByQrUuid(qrUuid);
            if (qrState == null) {
                return ResponseData.fail("二维码不存在或已过期");
            }

            // 检查是否过期
            if (System.currentTimeMillis() > qrState.getQrExpireTime()) {
                qrState.setQrStatus(3); // 已取消/过期
                wechatQrStateMapper.update(qrState);
                return ResponseData.fail("二维码已过期");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("qrId", qrUuid);
            result.put("status", qrState.getQrStatus());

            if (qrState.getQrStatus() == 2) { // 登录成功
                result.put("status", 2);
                result.put("token", qrState.getQrSystemToken());

                // 解析用户信息
                if (qrState.getQrUserInfo() != null) {
                    Map<String, Object> userInfo = JSON.parseObject(qrState.getQrUserInfo(), Map.class);
                    result.put("userInfo", userInfo);
                }

                result.put("needBind", false);
            } else if (qrState.getQrStatus() == 1 && qrState.getWOpenId() != null) {
                // 已扫码，检查是否已绑定
                WechatUser wechatUser = wechatUserMapper.findByOpenId(qrState.getWOpenId());
                if (wechatUser != null && (wechatUser.getPId() != null || wechatUser.getDId() != null || wechatUser.getAId() != null)) {
                    // 已绑定，自动登录
                    String token = generateSystemToken(wechatUser);
                    qrState.setQrSystemToken(token);
                    qrState.setQrStatus(2);
                    qrState.setQrLoginTime(System.currentTimeMillis());
                    wechatQrStateMapper.update(qrState);

                    result.put("status", 2);
                    result.put("token", token);
                    result.put("needBind", false);

                    // 添加用户信息
                    if (qrState.getQrUserInfo() != null) {
                        Map<String, Object> userInfo = JSON.parseObject(qrState.getQrUserInfo(), Map.class);
                        result.put("userInfo", userInfo);
                    }
                } else {
                    result.put("needBind", true);

                    // 返回用户信息供绑定页面显示
                    if (qrState.getQrUserInfo() != null) {
                        Map<String, Object> userInfo = JSON.parseObject(qrState.getQrUserInfo(), Map.class);
                        result.put("userInfo", userInfo);
                    }
                }
            }

            return ResponseData.success("检查登录状态成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("检查登录状态失败");
        }
    }

    @Transactional
    @Override
    public ResponseData bindSystemUser(String qrUuid, String phone, String email,
                                       String realName, String userType) {
        try {
            WechatQrState qrState = wechatQrStateMapper.findByQrUuid(qrUuid);
            if (qrState == null) {
                return ResponseData.fail("绑定超时");
            }

            if (qrState.getWOpenId() == null) {
                return ResponseData.fail("未获取到微信用户信息");
            }

            WechatUser wechatUser = wechatUserMapper.findByOpenId(qrState.getWOpenId());
            if (wechatUser == null) {
                return ResponseData.fail("微信用户不存在");
            }

            // 检查是否已绑定
            if (wechatUser.getPId() != null || wechatUser.getDId() != null || wechatUser.getAId() != null) {
                return ResponseData.fail("该微信已绑定其他账号");
            }

            Integer systemUserId = null;
            String username = null;
            String token = null;

            // 这里需要根据你的实际情况实现用户查找和绑定逻辑
            // 以下是示例代码，需要你根据实际业务修改

            switch (userType) {
                case "patient":
                    // 查找患者
                    Patient patient = null;
                    // patient = patientService.findByPhone(phone); // 需要你实现这个方法

                    if (patient == null) {
                        // 创建新患者
                        // 需要调用你的patientService注册方法
                        // patient = new Patient();
                        // patient.setPName(realName);
                        // patient.setPPhone(phone);
                        // patient.setPEmail(email);
                        // patientService.addPatient(patient);
                        // systemUserId = patient.getPId();
                        systemUserId = 202301; // 临时示例
                    } else {
                        systemUserId = patient.getPId();
                    }

                    // 绑定患者
                    wechatUserMapper.bindPatient(qrState.getWOpenId(), systemUserId);
                    break;

                case "doctor":
                    // 医生绑定逻辑
                    // 需要你根据实际情况实现
                    break;

                case "admin":
                    // 管理员绑定逻辑
                    // 需要你根据实际情况实现
                    break;

                default:
                    return ResponseData.fail("无效的用户类型");
            }

            if (systemUserId == null) {
                return ResponseData.fail("绑定系统用户失败");
            }

            // 重新查询绑定后的用户信息
            wechatUser = wechatUserMapper.findByOpenId(qrState.getWOpenId());

            // 生成系统token
            token = generateSystemToken(wechatUser);

            // 更新二维码状态
            qrState.setQrSystemToken(token);
            qrState.setQrStatus(2);
            qrState.setQrLoginTime(System.currentTimeMillis());
            wechatQrStateMapper.update(qrState);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userType", userType);
            result.put("userId", systemUserId);
            result.put("username", realName);
            result.put("realName", realName);
            result.put("phone", phone);
            result.put("email", email);

            return ResponseData.success("绑定成功", result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.fail("绑定失败: " + e.getMessage());
        }
    }

    @Override
    public WechatUser findByOpenId(String wOpenId) {
        return wechatUserMapper.findByOpenId(wOpenId);
    }

    @Override
    public WechatUser findByPatientId(Integer pId) {
        return wechatUserMapper.findByPatientId(pId).stream().findFirst().orElse(null);
    }

    /**
     * HTTP GET请求
     */
    private String httpGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        }
        return null;
    }

    /**
     * 生成系统token
     */
    private String generateSystemToken(WechatUser wechatUser) {
        Map<String, String> claims = new HashMap<>();
        claims.put("openId", wechatUser.getWOpenId());
        claims.put("nickname", wechatUser.getWNickname());

        if (wechatUser.getPId() != null) {
            claims.put("userId", wechatUser.getPId().toString());
            claims.put("userType", "patient");
        } else if (wechatUser.getDId() != null) {
            claims.put("userId", wechatUser.getDId().toString());
            claims.put("userType", "doctor");
        } else if (wechatUser.getAId() != null) {
            claims.put("userId", wechatUser.getAId().toString());
            claims.put("userType", "admin");
        }

        return JwtUtil.getToken(claims);
    }
}