<template>
  <div class="doctor-dashboard">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-s-home title-icon"></i>
        <h1 class="page-title">医生工作台</h1>
        <p class="page-subtitle">今日工作概况与数据统计</p>
      </div>
      <div class="decoration-element">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <!-- 数据统计卡片区域 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-card appointment-card">
            <div class="card-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-content">
              <div class="stat-number">{{ orderPeople }}</div>
              <div class="stat-label">今日预约挂号人数</div>
              <div class="stat-trend">
                <i class="el-icon-caret-top"></i>
                <span>较昨日 +5%</span>
              </div>
            </div>
            <div class="card-decoration"></div>
          </div>
        </el-col>
        
        <el-col :span="8">
          <div class="stat-card patient-card">
            <div class="card-icon">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="card-content">
              <div class="stat-number">{{ inPatientCount }}</div>
              <div class="stat-label">当前住院患者</div>
              <div class="stat-trend">
                <i class="el-icon-caret-bottom"></i>
                <span>较上周 -2%</span>
              </div>
            </div>
            <div class="card-decoration"></div>
          </div>
        </el-col>
        
        <el-col :span="8">
          <div class="stat-card rating-card">
            <div class="card-icon">
              <i class="el-icon-star-off"></i>
            </div>
            <div class="card-content">
              <div class="stat-number">{{ avgRating }}</div>
              <div class="stat-label">平均患者评分</div>
              <div class="rating-stars">
                <i v-for="n in 5" :key="n" 
                   class="el-icon-star-on" 
                   :class="{ 'active': n <= Math.round(avgRating) }">
                </i>
              </div>
            </div>
            <div class="card-decoration"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能区域 -->
    <div class="features-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="feature-card quick-actions" shadow="hover">
            <div slot="header" class="card-header">
              <i class="el-icon-thumb"></i>
              <span>快速操作</span>
            </div>
            <div class="actions-grid">
              <div class="action-item" @click="$router.push('orderToday')">
                <div class="action-icon">
                  <i class="el-icon-tickets"></i>
                </div>
                <div class="action-text">今日挂号列表</div>
              </div>
              <div class="action-item" @click="$router.push('doctorOrder')">
                <div class="action-icon">
                  <i class="el-icon-notebook-2"></i>
                </div>
                <div class="action-text">历史挂号记录</div>
              </div>
              <div class="action-item" @click="$router.push('inBed')">
                <div class="action-icon">
                  <i class="el-icon-document-add"></i>
                </div>
                <div class="action-text">住院申请管理</div>
              </div>
              <div class="action-item" @click="$router.push('doctorCard')">
                <div class="action-icon">
                  <i class="el-icon-user-solid"></i>
                </div>
                <div class="action-text">个人信息管理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card class="feature-card schedule-card" shadow="hover">
            <div slot="header" class="card-header">
              <i class="el-icon-date"></i>
              <span>今日排班</span>
            </div>
            <div class="schedule-content">
              <div class="schedule-item">
                <div class="time-slot">上午 08:00-12:00</div>
                <div class="schedule-detail">门诊就诊</div>
                <div class="schedule-status active">进行中</div>
              </div>
              <div class="schedule-item">
                <div class="time-slot">下午 14:00-18:00</div>
                <div class="schedule-detail">专家门诊</div>
                <div class="schedule-status upcoming">即将开始</div>
              </div>
              <div class="schedule-item">
                <div class="time-slot">晚上 19:00-21:00</div>
                <div class="schedule-detail">急诊值班</div>
                <div class="schedule-status upcoming">待开始</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 医院形象展示 -->
    <div class="hospital-showcase">
      <el-card shadow="hover" class="showcase-card">
        <div class="showcase-content">
          <div class="showcase-text">
            <h3>欢迎使用医院智能管理系统</h3>
            <p>为您提供高效、便捷的医疗工作体验，助力优质医疗服务</p>
          </div>
          <div class="showcase-image">
            <img src="@/assets/16.png" alt="医院管理系统" />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";

export default {
  name: "DoctorLayout",
  data() {
    return {
      userId: 1,
      orderPeople: 0,
      inPatientCount: 12,
      avgRating: 4.7
    };
  },
  methods: {
    //token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
    requestPeople() {
      request
        .get("order/orderPeopleByDid", {
          params: {
            dId: this.userId,
          },
        })
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("数据请求失败");
          this.orderPeople = res.data.data;
        });
    },
  },
  created() {
    this.userId = this.tokenDecode(getToken()).dId;
    this.requestPeople();
  },
};
</script>

<style lang="scss" scoped>
.doctor-dashboard {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

/* 顶部标题区域 */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  padding: 20px 0;
}

.title-content {
  z-index: 2;
}

.title-icon {
  font-size: 36px;
  color: #427cb3;
  margin-bottom: 10px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
}

.page-subtitle {
  font-size: 16px;
  color: #5a6376;
  margin: 0;
}

.decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 100px;
  height: 100px;
  background: #427cb3;
  top: -30px;
  right: 0;
}

.circle-2 {
  width: 70px;
  height: 70px;
  background: #67c23a;
  top: 10px;
  right: 80px;
}

.circle-3 {
  width: 50px;
  height: 50px;
  background: #e6a23c;
  top: 40px;
  right: 40px;
}

/* 数据统计卡片 */
.stats-container {
  margin-bottom: 30px;
}

.stat-card {
  position: relative;
  padding: 30px;
  border-radius: 16px;
  color: white;
  overflow: hidden;
  min-height: 180px;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.appointment-card {
  background: linear-gradient(135deg, #58b9ae 0%, #7ad9cc 100%);
}

.patient-card {
  background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
}

.rating-card {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.card-icon {
  font-size: 60px;
  margin-right: 20px;
  opacity: 0.9;
}

.card-content {
  flex: 1;
}

.stat-number {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 10px;
}

.stat-trend {
  display: flex;
  align-items: center;
  font-size: 14px;
  opacity: 0.8;
}

.rating-stars {
  display: flex;
  margin-top: 5px;
}

.rating-stars .el-icon-star-on {
  font-size: 18px;
  margin-right: 2px;
  color: rgba(255, 255, 255, 0.5);
}

.rating-stars .el-icon-star-on.active {
  color: white;
}

.card-decoration {
  position: absolute;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  right: -30px;
  bottom: -30px;
}

/* 功能区域 */
.features-section {
  margin-bottom: 30px;
}

.feature-card {
  border-radius: 16px;
  border: none;
  overflow: hidden;
}

.feature-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  font-weight: 600;
  font-size: 18px;
  color: #1f2d3d;
  border-bottom: 1px solid #f0f2f5;
  padding-bottom: 15px;
}

.card-header i {
  font-size: 20px;
  color: #427cb3;
  margin-right: 10px;
}

.actions-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-radius: 10px;
  background: #f8fafc;
  transition: all 0.3s ease;
  cursor: pointer;
}

.action-item:hover {
  background: #f0f7ff;
  transform: translateY(-2px);
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: #e6f3ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  font-size: 24px;
  color: #427cb3;
}

.action-text {
  font-size: 14px;
  font-weight: 500;
  color: #1f2d3d;
  text-align: center;
}

.schedule-content {
  padding: 10px 0;
}

.schedule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  background: #f8fafc;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.schedule-item:hover {
  background: #f0f7ff;
}

.time-slot {
  font-weight: 500;
  color: #1f2d3d;
  width: 120px;
}

.schedule-detail {
  flex: 1;
  color: #5a6376;
}

.schedule-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.schedule-status.active {
  background: #f0f9ff;
  color: #427cb3;
}

.schedule-status.upcoming {
  background: #fff6e6;
  color: #e6a23c;
}

/* 医院形象展示 */
.hospital-showcase {
  margin-bottom: 30px;
}

.showcase-card {
  border-radius: 16px;
  border: none;
  overflow: hidden;
}

.showcase-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.showcase-text {
  flex: 1;
  padding-right: 30px;
}

.showcase-text h3 {
  font-size: 22px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 10px;
}

.showcase-text p {
  font-size: 14px;
  color: #5a6376;
  margin-bottom: 20px;
  line-height: 1.6;
}

.guide-btn {
  background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
}

.showcase-image {
  width: 300px;
}

.showcase-image img {
  width: 100%;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .doctor-dashboard {
    padding: 10px;
  }
  
  .header-section {
    flex-direction: column;
    text-align: center;
  }
  
  .stats-container .el-col {
    margin-bottom: 20px;
  }
  
  .features-section .el-col {
    margin-bottom: 20px;
  }
  
  .showcase-content {
    flex-direction: column;
    text-align: center;
  }
  
  .showcase-text {
    padding-right: 0;
    margin-bottom: 20px;
  }
  
  .showcase-image {
    width: 100%;
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>