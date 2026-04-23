<template>
  <div class="admin-dashboard">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-s-home title-icon"></i>
        <h1 class="page-title">医院管理概览</h1>
        <p class="page-subtitle">今日医院运营数据统计</p>
      </div>
      <div class="decoration-element">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <!-- 数据统计卡片区域 -->
    <div class="stats-container">
      <el-row :gutter="30">
        <el-col :span="12">
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
        
        <el-col :span="12">
          <div class="stat-card patient-card">
            <div class="card-icon">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="card-content">
              <div class="stat-number">{{ bedPeople }}</div>
              <div class="stat-label">当前住院总人数</div>
              <div class="stat-trend">
                <i class="el-icon-caret-bottom"></i>
                <span>较上周 -2%</span>
              </div>
            </div>
            <div class="card-decoration"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 医院形象展示 -->
    <div class="hospital-showcase">
      <el-card shadow="hover" class="showcase-card">
        <div class="showcase-content">
          <div class="showcase-text">
            <h3>欢迎使用医院智能管理系统</h3>
            <p>为您提供高效、便捷的医疗管理体验，助力优质医疗服务</p>
            <div class="stats-grid">
              <div class="mini-stat">
                <div class="mini-stat-value">{{ orderPeople }}</div>
                <div class="mini-stat-label">今日挂号</div>
              </div>
              <div class="mini-stat">
                <div class="mini-stat-value">{{ bedPeople }}</div>
                <div class="mini-stat-label">在院患者</div>
              </div>
              <div class="mini-stat">
                <div class="mini-stat-value">98%</div>
                <div class="mini-stat-label">系统运行率</div>
              </div>
            </div>
          </div>
          <div class="showcase-image">
            <img src="@/assets/16.png" alt="医院管理系统" />
          </div>
        </div>
      </el-card>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="action-card" @click="$router.push('doctorList')">
            <div class="action-icon doctor-icon">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="action-content">
              <h3>医生管理</h3>
              <p>管理医生信息</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('patientList')">
            <div class="action-icon patient-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="action-content">
              <h3>患者管理</h3>
              <p>管理患者信息</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('orderList')">
            <div class="action-icon order-icon">
              <i class="el-icon-tickets"></i>
            </div>
            <div class="action-content">
              <h3>挂号管理</h3>
              <p>查看挂号记录</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="$router.push('bedList')">
            <div class="action-icon bed-icon">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="action-content">
              <h3>床位管理</h3>
              <p>管理住院床位</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "AdminLayout",
    data() {
        return {
            orderPeople: 0,
            bedPeople: 0,
        };
    },
    methods: {
        requestPeople() {
            request
                .get("order/orderPeople")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.orderPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
        requestBed() {
            request
                .get("bed/bedPeople")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.bedPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
    },
    created() {
        this.requestPeople();
        this.requestBed();
    },
};
</script>

<style lang="scss" scoped>
.admin-dashboard {
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
  cursor: pointer;
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

.card-decoration {
  position: absolute;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  right: -30px;
  bottom: -30px;
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

.showcase-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}

.showcase-content {
  display: flex;
  align-items: center;
  padding: 30px;
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

.stats-grid {
  display: flex;
  gap: 20px;
}

.mini-stat {
  text-align: center;
  padding: 15px;
  background: #f8fafc;
  border-radius: 8px;
  min-width: 100px;
}

.mini-stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #427cb3;
  margin-bottom: 5px;
}

.mini-stat-label {
  font-size: 12px;
  color: #909399;
}

.showcase-image {
  width: 300px;
}

.showcase-image img {
  width: 100%;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 快速操作区域 */
.quick-actions-section {
  margin-bottom: 30px;
}

.action-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  height: 140px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.action-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
}

.action-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
  font-size: 28px;
  color: white;
}

.doctor-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.patient-icon {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.order-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.bed-icon {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.action-content h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
}

.action-content p {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
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
    max-width: 400px;
  }
}

@media (max-width: 768px) {
  .admin-dashboard {
    padding: 10px;
  }
  
  .header-section {
    flex-direction: column;
    text-align: center;
  }
  
  .stats-container .el-col {
    margin-bottom: 20px;
  }
  
  .stat-card {
    min-height: 150px;
    padding: 20px;
  }
  
  .card-icon {
    font-size: 40px;
    margin-right: 15px;
  }
  
  .stat-number {
    font-size: 32px;
  }
  
  .showcase-content {
    padding: 20px;
  }
  
  .stats-grid {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .quick-actions-section .el-col {
    margin-bottom: 15px;
  }
  
  .action-card {
    padding: 20px;
    height: 120px;
  }
  
  .action-icon {
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
}
</style>