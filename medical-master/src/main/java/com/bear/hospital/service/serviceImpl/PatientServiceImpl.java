// PatientServiceImpl.java - 实现新方法
package com.bear.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.hospital.mapper.PatientMapper;
import com.bear.hospital.pojo.Patient;
import com.bear.hospital.service.EmailCodeService;
import com.bear.hospital.service.PatientService;
import com.bear.hospital.utils.Md5Util;
import com.bear.hospital.utils.TodayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("PatientService")
public class PatientServiceImpl implements PatientService {
    protected static final Logger Log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Resource
    private PatientMapper patientMapper;
    @Autowired
    private EmailCodeService emailCodeService;
    /**
     * 登录数据校验
     * */
    @Override
    public Patient login(int pId, String pPassword){
        Patient patient = this.patientMapper.selectById(pId);
        String password = Md5Util.getMD5(pPassword);
        if (patient == null) {
            return null;
        } else {
            if ((patient.getPPassword()).equals(password)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * 根据手机号查询患者信息（新增实现）
     */
    @Override
    public Patient findPatientByPhone(String phone) {
        try {
            Log.info("根据手机号查询患者信息: phone={}", phone);

            QueryWrapper<Patient> wrapper = new QueryWrapper<>();
            wrapper.eq("p_phone", phone)
                    .eq("p_state", 1); // 只查询状态正常的患者

            Patient patient = this.patientMapper.selectOne(wrapper);

            if (patient != null) {
                Log.info("找到患者信息: pId={}, pName={}", patient.getPId(), patient.getPName());
            } else {
                Log.info("未找到手机号对应的患者: phone={}", phone);
            }

            return patient;
        } catch (Exception e) {
            Log.error("根据手机号查询患者信息异常: phone={}", phone, e);
            return null;
        }
    }

    /**
     * 分页模糊查询所有患者信息
     */
    @Override
    public HashMap<String, Object> findAllPatients(int pageNumber, int size, String query) {
        Page<Patient> page = new Page<>(pageNumber, size);
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like("p_name", query).orderByDesc("p_state");
        IPage<Patient> iPage = this.patientMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("patients", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除患者信息
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        patient.setPState(0);
        this.patientMapper.updateById(patient);
        return true;
    }

    /**
     * 根据患者id查询患者信息
     */
    @Override
    public Patient findPatientById(int pId){
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.patientMapper.selectOne(wrapper);
    }

    /**
     * 增加患者信息
     */
    @Override
    public Boolean addPatient(Patient patient) {
        //如果账号已存在则返回false
        List<Patient> patients = this.patientMapper.selectList(null);
        for (Patient patient1 : patients) {
            if (patient.getPId() == patient1.getPId()) {
                return false;
            }
            if ((patient.getPEmail()).equals(patient1.getPEmail()) ){
                return false;
            }
        }
        int yourYear = Integer.parseInt(patient.getPBirthday().substring(0, 4));
        int todayYear = Integer.parseInt(TodayUtil.getTodayYmd().substring(0,4));
        //密码md5加密
        String password = Md5Util.getMD5(patient.getPPassword());
        patient.setPPassword(password);
        patient.setPAge(todayYear-yourYear);
        patient.setPState(1);
        this.patientMapper.insert(patient);
        return true;
    }

    /**
     * 统计患者男女人数
     */
    public List<Integer> patientAge(){
        List<Integer> ageList = new ArrayList<>();
        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);
        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);
        return ageList;
    }
    /**
     * 更新患者个人信息
     * @param patient 包含需要更新的患者信息（pId必须传入）
     * @return 是否更新成功
     */
    @Override
    public Boolean updatePatientInfo(Patient patient) {
        try {
            Log.info("开始更新患者个人信息: pId={}", patient.getPId());

            // 1. 检查患者是否存在
            Patient existingPatient = this.findPatientById(patient.getPId());
            if (existingPatient == null) {
                Log.error("患者不存在: pId={}", patient.getPId());
                return false;
            }

            // 2. 检查手机号是否被其他用户使用
            if (patient.getPPhone() != null && !patient.getPPhone().equals(existingPatient.getPPhone())) {
                QueryWrapper<Patient> phoneWrapper = new QueryWrapper<>();
                phoneWrapper.eq("p_phone", patient.getPPhone())
                        .ne("p_id", patient.getPId());
                Patient phonePatient = this.patientMapper.selectOne(phoneWrapper);
                if (phonePatient != null) {
                    Log.error("手机号已被使用: pPhone={}", patient.getPPhone());
                    return false;
                }
            }

            // 3. 检查邮箱是否被其他用户使用
            if (patient.getPEmail() != null && !patient.getPEmail().equals(existingPatient.getPEmail())) {
                QueryWrapper<Patient> emailWrapper = new QueryWrapper<>();
                emailWrapper.eq("p_email", patient.getPEmail())
                        .ne("p_id", patient.getPId());
                Patient emailPatient = this.patientMapper.selectOne(emailWrapper);
                if (emailPatient != null) {
                    Log.error("邮箱已被使用: pEmail={}", patient.getPEmail());
                    return false;
                }
            }

            // 4. 检查身份证号是否被其他用户使用
            if (patient.getPCard() != null && !patient.getPCard().equals(existingPatient.getPCard())) {
                QueryWrapper<Patient> cardWrapper = new QueryWrapper<>();
                cardWrapper.eq("p_card", patient.getPCard())
                        .ne("p_id", patient.getPId());
                Patient cardPatient = this.patientMapper.selectOne(cardWrapper);
                if (cardPatient != null) {
                    Log.error("身份证号已被使用: pCard={}", patient.getPCard());
                    return false;
                }
            }

            // 5. 如果更新了出生日期，自动更新年龄
            if (patient.getPBirthday() != null && !patient.getPBirthday().equals(existingPatient.getPBirthday())) {
                int patientYear = Integer.parseInt(patient.getPBirthday().substring(0, 4));
                int todayYear = Integer.parseInt(TodayUtil.getTodayYmd().substring(0, 4));
                patient.setPAge(todayYear - patientYear);
            }

            // 6. 保护密码字段不被修改
            patient.setPPassword(existingPatient.getPPassword());

            // 7. 保护账号状态不被修改
            patient.setPState(existingPatient.getPState());

            // 8. 执行更新
            int result = this.patientMapper.updateById(patient);

            if (result > 0) {
                Log.info("患者个人信息更新成功: pId={}", patient.getPId());
                return true;
            } else {
                Log.error("患者个人信息更新失败: pId={}", patient.getPId());
                return false;
            }

        } catch (Exception e) {
            Log.error("更新患者个人信息异常: pId={}", patient.getPId(), e);
            return false;
        }
    }
    /**
     * 找回密码
     * @param pEmail 邮箱
     * @param pPassword 新密码
     * @param code 验证码
     * @return 是否修改成功
     */
    @Override
    public Boolean findPassword(String pEmail, String pPassword, String code) {
        try {
            Log.info("开始找回密码: pEmail={}", pEmail);

            // 1. 参数校验
            if (!org.springframework.util.StringUtils.hasText(pEmail) ||
                    !org.springframework.util.StringUtils.hasText(pPassword) ||
                    !org.springframework.util.StringUtils.hasText(code)) {
                Log.error("找回密码失败: 参数不能为空");
                return false;
            }

            // 2. 根据邮箱获取手机号
            String phone = emailCodeService.getPhoneByEmail(pEmail);
            if (phone == null) {
                Log.error("找回密码失败: 未找到该邮箱绑定的手机号");
                return false;
            }

            Log.info("找回密码: 邮箱={}, 手机号={}", pEmail, phone);

            // 3. 验证验证码
            String verifiedEmail = emailCodeService.verifyEmailCode(phone, code);
            if (verifiedEmail == null || !verifiedEmail.equals(pEmail)) {
                Log.error("找回密码失败: 验证码错误或已过期");
                return false;
            }

            // 4. 根据手机号查询患者信息
            Patient patient = this.findPatientByPhone(phone);
            if (patient == null) {
                Log.error("找回密码失败: 未找到患者信息");
                return false;
            }

            // 5. 更新密码（MD5加密）
            String newPassword = Md5Util.getMD5(pPassword);
            patient.setPPassword(newPassword);

            // 6. 执行更新，将 int 转换为 boolean
            int updateResult = this.patientMapper.updateById(patient);
            boolean result = updateResult > 0;  // 如果影响的行数大于0，则表示更新成功

            if (result) {
                Log.info("找回密码成功: pEmail={}, phone={}, pId={}", pEmail, phone, patient.getPId());
            } else {
                Log.error("找回密码失败: 更新密码失败");
            }

            return result;

        } catch (Exception e) {
            Log.error("找回密码异常: {}", e.getMessage(), e);
            return false;
        }
    }

}