package com.bear.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.hospital.mapper.OrderMapper;
import com.bear.hospital.pojo.Orders;
import com.bear.hospital.pojo.Doctor;
import com.bear.hospital.service.OrderService;
import com.bear.hospital.service.DoctorService;
import com.bear.hospital.utils.RandomUtil;
import com.bear.hospital.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private JedisPool jedisPool;//redis连接池
    @Autowired
    private DoctorService doctorService; // 添加DoctorService依赖


    /**
     * 分页模糊查询所有挂号信息
     */
    @Override
    public HashMap<String, Object> findAllOrders(int pageNumber, int size, String query) {
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query);
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除挂号信息
     */
    @Override
    public Boolean deleteOrder(int oId) {
        this.orderMapper.deleteById(oId);
        return true;
    }

    /**
     * 增加挂号信息
     */
    @Override
    public Boolean addOrder(Orders order, String arId) {
        Jedis jedis = jedisPool.getResource();

        try {
            String timeStr = order.getOStart().substring(11, 16);
            String slotField = getTimeSlotField(timeStr);

            if (slotField == null) {
                return false;
            }

            synchronized (this) {
                // 1. 检查余号
                String remaining = jedis.hget(arId, slotField);
                if (remaining == null || Integer.parseInt(remaining) <= 0) {
                    return false;
                }

                // 2. 减少余号
                jedis.hincrBy(arId, slotField, -1);

                // 3. 获取医生信息，计算挂号费
                Doctor doctor = doctorService.findDoctor(order.getdId());
                Double registrationFee = doctor != null ? doctor.getdPrice() : 0.0;

                // 4. 插入数据库
                order.setOId(RandomUtil.randomOid(order.getPId()));
                order.setOState(0);
                order.setOPriceState(0);
                order.setOStart(order.getOStart().substring(0, 19));
                // 设置初始总费用为挂号费
                order.setOTotalPrice(registrationFee);

                int result = this.orderMapper.insert(order);

                // 5. 如果数据库插入失败，回滚 Redis
                if (result <= 0) {
                    jedis.hincrBy(arId, slotField, 1);  // 加回余号
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    private String getTimeSlotField(String timeStr) {
        int hour = Integer.parseInt(timeStr.substring(0, 2));
        int minute = Integer.parseInt(timeStr.substring(3, 5));
        int totalMinutes = hour * 60 + minute;

        if (totalMinutes >= 510 && totalMinutes < 570) return "eTOn";
        if (totalMinutes >= 570 && totalMinutes < 630) return "nTOt";
        if (totalMinutes >= 630 && totalMinutes < 690) return "tTOe";
        if (totalMinutes >= 870 && totalMinutes < 930) return "fTOf";
        if (totalMinutes >= 930 && totalMinutes < 990) return "fTOs";
        if (totalMinutes >= 990 && totalMinutes < 1050) return "sTOs";

        return null;
    }

    /**
     * 根据pId查询挂号
     */
    @Override
    public List<Orders> findOrderByPid(int pId) {
        return this.orderMapper.findOrderByPid(pId);
    }

    /**
     * 根据商户订单号查询订单（支持时间戳后缀）
     */
    @Override
    public Orders findOrderByOutTradeNo(String outTradeNo) {
        try {
            Integer oId;
            if (outTradeNo.contains("_")) {
                // 追诊订单，提取原始订单ID
                oId = Integer.parseInt(outTradeNo.split("_")[0]);
                System.out.println("解析追诊订单号：" + outTradeNo + " -> 原始订单ID：" + oId);
            } else {
                // 普通订单
                oId = Integer.parseInt(outTradeNo);
            }
            return orderMapper.findOrderByOid(oId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看当天挂号列表
     */
    @Override
    public List<Orders> findOrderByNull(int dId, String oStart){
        return this.orderMapper.findOrderByNull(dId, oStart);
    }

    /**
     * 根据id更新挂号信息
     */
    @Override
    public Boolean updateOrder(Orders orders) {
        orders.setOState(1);
        orders.setOEnd(TodayUtil.getToday());
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("o_id", orders.getOId());
        this.orderMapper.update(orders, wrapper);
        return true;
    }

    /**
     * 计算总费用
     */
    private Double calculateTotalPrice(Orders order) {
        Double totalPrice = 0.0;

        // 1. 添加挂号费
        Doctor doctor = doctorService.findDoctor(order.getdId());
        if (doctor != null && doctor.getdPrice() != null) {
            totalPrice += doctor.getdPrice();
        }

        // 2. 添加药品费用
        if (order.getODrug() != null && !order.getODrug().isEmpty()) {
            // 解析药品费用字符串，例如："青霉素*23*1 苯唑西林*11*1 药物总价34元"
            String[] drugParts = order.getODrug().split("药物总价");
            if (drugParts.length > 1) {
                String drugPriceStr = drugParts[1].replaceAll("[^0-9.]", "");
                if (!drugPriceStr.isEmpty()) {
                    try {
                        Double drugPrice = Double.parseDouble(drugPriceStr);
                        totalPrice += drugPrice;
                    } catch (NumberFormatException e) {
                        System.err.println("解析药品费用失败: " + drugPriceStr);
                    }
                }
            }
        }

        // 3. 添加检查项目费用
        if (order.getOCheck() != null && !order.getOCheck().isEmpty()) {
            // 解析检查费用字符串，例如："CT*435(元) 项目总价435元"
            String[] checkParts = order.getOCheck().split("项目总价");
            if (checkParts.length > 1) {
                String checkPriceStr = checkParts[1].replaceAll("[^0-9.]", "");
                if (!checkPriceStr.isEmpty()) {
                    try {
                        Double checkPrice = Double.parseDouble(checkPriceStr);
                        totalPrice += checkPrice;
                    } catch (NumberFormatException e) {
                        System.err.println("解析检查费用失败: " + checkPriceStr);
                    }
                }
            }
        }

        return totalPrice;
    }

    /**
     * 更新订单费用（用于追诊）
     */
    private Boolean updateOrderTotalPrice(int oId) {
        Orders order = this.orderMapper.selectById(oId);
        if (order == null) {
            return false;
        }

        // 计算总费用
        Double totalPrice = calculateTotalPrice(order);

        // 更新总费用
        UpdateWrapper<Orders> wrapper = new UpdateWrapper<>();
        wrapper.eq("o_id", oId).set("o_total_price", totalPrice);
        int result = this.orderMapper.update(null, wrapper);

        return result > 0;
    }

    /**
     * 根据id设置缴费状态
     */
    @Override
    public Boolean updatePrice(int oId){
        /**
         * 用QueryWrapper如果不把外键的值也传进来，会报错
         * 用UpdateWrapper就正常
         */
        UpdateWrapper<Orders> wrapper = new UpdateWrapper<>();
        // 修改：不再重置总金额为0，而是保持原值
        wrapper.eq("o_id", oId).set("o_price_state", 1);
        int i = this.orderMapper.update(null, wrapper);
        System.out.println("影响行数"+i);
        return true;
    }

    /**
     * 查找医生已完成的挂号单
     */
    @Override
    public HashMap<String, Object> findOrderFinish(int pageNumber, int size, String query, int dId){
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query).eq("d_id", dId).orderByDesc("o_start").eq("o_state", 1);
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录

        return hashMap;
    }

    /**
     * 根据dId查询挂号
     */
    @Override
    public HashMap<String, Object> findOrderByDid(int pageNumber, int size, String query, int dId){
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query).eq("d_id", dId).orderByDesc("o_start");
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 统计今天挂号人数
     */
    @Override
    public int orderPeople(String oStart){
        return this.orderMapper.orderPeople(oStart);
    }

    /**
     * 统计今天某个医生挂号人数
     */
    @Override
    public int orderPeopleByDid(String oStart, int dId){
        return this.orderMapper.orderPeopleByDid(oStart, dId);
    }

    /**
     * 统计挂号男女人数
     */
    @Override
    public List<String> orderGender(){
        return this.orderMapper.orderGender();
    }

    /**
     * 增加诊断及医生意见
     */
    @Override
    public Boolean updateOrderByAdd(Orders order){
        if (this.orderMapper.updateOrderByAdd(order) == 0){
            return false;
        }

        // 更新总费用
        return updateOrderTotalPrice(order.getOId());
    }


    /**
     * 判断诊断之后再次购买药物是否已缴费
     */
    @Override
    public Boolean findTotalPrice(int oId){
        Orders order = this.orderMapper.selectById(oId);
        System.out.println("findTotalPrice - 订单ID: " + oId);
        System.out.println("findTotalPrice - oTotalPrice: " + order.getOTotalPrice());
        System.out.println("findTotalPrice - oPriceState: " + order.getOPriceState());

        if (order.getOTotalPrice() != null && order.getOTotalPrice() > 0.00){
            System.out.println("findTotalPrice - 有费用，设置 oPriceState = 0");
            order.setOPriceState(0);
            this.orderMapper.updateById(order);
            return true;
        }

        System.out.println("findTotalPrice - 无费用");
        return false;
    }

    /**
     * 请求挂号时间段
     */
    @Override
    public HashMap<String, String> findOrderTime(String arId){
        Jedis jedis = jedisPool.getResource();
        HashMap<String, String> map = (HashMap<String, String>) jedis.hgetAll(arId);

        if(map == null) {
            map = new HashMap<>();
            map.put("tTOe", "40");
            map.put("nTOt", "40");
            map.put("sTOs", "40");
            map.put("eTOn", "40");
            map.put("fTOf", "40");
            map.put("fTOs", "40");
        }

        map.putIfAbsent("tTOe", "40");
        map.putIfAbsent("nTOt", "40");
        map.putIfAbsent("sTOs", "40");
        map.putIfAbsent("eTOn", "40");
        map.putIfAbsent("fTOf", "40");
        map.putIfAbsent("fTOs", "40");

        jedis.hmset(arId, map);
        jedis.expire(arId, 604800);

        return map;
    }

    /**
     * 统计过去20天挂号科室人数
     */
    @Override
    public List<String> orderSection(){
        String startTime = TodayUtil.getPastDate(20);
        String endTime = TodayUtil.getTodayYmd();
        return this.orderMapper.orderSection(startTime, endTime);
    }

    /**
     * 根据订单ID查询订单
     */
    @Override
    public Orders findOrderByOid(int oId) {
        return orderMapper.findOrderByOid(oId);
    }
}
