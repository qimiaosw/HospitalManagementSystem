package com.bear.hospital.controller;

import com.bear.hospital.mapper.OrderMapper;
import com.bear.hospital.pojo.Orders;
import com.bear.hospital.pojo.Patient;
import com.bear.hospital.service.DoctorService;
import com.bear.hospital.service.OrderService;
import com.bear.hospital.service.PatientService;
import com.bear.hospital.utils.JwtUtil;
import com.bear.hospital.utils.PdfUtil;
import com.bear.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("patient")
public class PatientController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 登录数据验证
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestParam(value = "pId") int pId, @RequestParam(value = "pPassword") String pPassword) {
        Patient patient = this.patientService.login(pId, pPassword);
        if (patient != null) {
            Map<String,String> map = new HashMap<>();
            map.put("pName", patient.getPName());
            map.put("pId", String.valueOf(patient.getPId()));
            map.put("pCard", patient.getPCard());
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            //response.setHeader("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }
    /**
     * 根据科室查询所有医生信息
     */
    @RequestMapping("findDoctorBySection")
    public ResponseData findDoctorBySection(@RequestParam(value = "dSection") String dSection){
        return ResponseData.success("根据科室查询所有医生信息成功", this.doctorService.findDoctorBySection(dSection));
    }
    /**
     * 增加挂号信息
     */
    @RequestMapping("addOrder")
    @ResponseBody
    public ResponseData addOrder(Orders order, String arId){
        System.out.println(arId);
        if (this.orderService.addOrder(order, arId))
            return ResponseData.success("插入挂号信息成功");
        return ResponseData.fail("插入挂号信息失败");
    }
    /**
     * 根据pId查询挂号
     */
    @RequestMapping("findOrderByPid")
    public ResponseData findOrderByPid(@RequestParam(value = "pId") int pId){
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByPid(pId)) ;
    }

    /**
     * 增加患者信息
     */
    @RequestMapping("addPatient")
    @ResponseBody
    public ResponseData addPatient(Patient patient) {
        Boolean bo = this.patientService.addPatient(patient);
        if (bo) {
            return ResponseData.success("注册成功");
        }
        return ResponseData.fail("注册失败！账号或邮箱已被占用");
    }
    @GetMapping("/pdf")
    public void downloadPDF(HttpServletRequest request, HttpServletResponse response, int oId) throws Exception {
        Orders order = this.orderMapper.findOrderByOid(oId);
        PdfUtil.ExportPdf(request, response, order);
    }
    /**
     * 统计患者男女人数
     */
    @RequestMapping("patientAge")
    public ResponseData patientAge(){
        return  ResponseData.success("统计患者男女人数成功", this.patientService.patientAge());

    }
    /**
     * 更新患者个人信息
     * @param patient 包含需要更新的患者信息（pId必须传入）
     * @return 操作结果
     */
    @RequestMapping("updatePatientInfo")
    @ResponseBody
    public ResponseData updatePatientInfo(@RequestBody Patient patient) {
        // 1. 参数校验
        if (patient.getPId() <= 0) {
            return ResponseData.fail("患者ID不能为空");
        }

        // 2. 调用服务层更新
        Boolean result = this.patientService.updatePatientInfo(patient);

        // 3. 返回结果
        if (result) {
            return ResponseData.success("个人信息更新成功");
        } else {
            return ResponseData.fail("个人信息更新失败，请检查信息是否正确或已被他人使用");
        }
    }
    /**
     * 找回密码
     * @param pEmail 邮箱
     * @param pPassword 新密码
     * @param code 验证码
     * @return 操作结果
     */
    @RequestMapping("findPassword")
    @ResponseBody
    public ResponseData findPassword(
            @RequestParam(value = "pEmail") String pEmail,
            @RequestParam(value = "pPassword") String pPassword,
            @RequestParam(value = "code") String code
    ) {
        // 1. 参数校验
        if (pEmail == null || pEmail.trim().isEmpty()) {
            return ResponseData.fail("邮箱不能为空");
        }

        if (pPassword == null || pPassword.trim().isEmpty()) {
            return ResponseData.fail("新密码不能为空");
        }

        if (code == null || code.trim().isEmpty()) {
            return ResponseData.fail("验证码不能为空");
        }

        // 2. 密码长度校验
        if (pPassword.length() < 6) {
            return ResponseData.fail("密码长度不能少于6位");
        }

        // 3. 调用服务层进行找回密码
        Boolean result = patientService.findPassword(pEmail, pPassword, code);

        // 4. 返回结果
        if (result) {
            return ResponseData.success("密码修改成功，请使用新密码登录");
        } else {
            return ResponseData.fail("验证码错误或已过期，请重试");
        }
    }

}
