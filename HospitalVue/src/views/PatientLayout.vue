<template>
  <div class="patient-overview-management">
      <!-- 顶部标题区域 -->
      <div class="overview-header-section">
          <div class="overview-title-content">
              <i class="el-icon-s-data overview-title-icon"></i>
              <h1 class="overview-page-title">数据概览</h1>
              <p class="overview-page-subtitle">今日医院运营数据统计</p>
          </div>
          <div class="overview-decoration-element">
              <div class="overview-circle overview-circle-1"></div>
              <div class="overview-circle overview-circle-2"></div>
              <div class="overview-circle overview-circle-3"></div>
          </div>
      </div>

      <div class="overview-container">
          <!-- 数据统计卡片 -->
          <div class="stats-grid">
              <!-- 预约挂号统计 -->
              <div class="stat-card appointment-card">
                  <div class="card-decoration">
                      <div class="decoration-circle decoration-1"></div>
                      <div class="decoration-circle decoration-2"></div>
                  </div>
                  <div class="card-content">
                      <div class="card-icon-section">
                          <div class="icon-wrapper appointment-icon">
                              <i class="el-icon-user"></i>
                          </div>
                      </div>
                      <div class="card-info-section">
                          <h3 class="card-title">就诊概况</h3>
                          <div class="card-value">{{ orderPeople }}</div>
                          <p class="card-label">今日预约挂号总人数</p>
                      </div>
                      <div class="card-trend-section">
                          <div class="trend-indicator positive">
                              <i class="el-icon-top"></i>
                              <span>今日数据</span>
                          </div>
                      </div>
                  </div>
                  <div class="card-footer">
                      <div class="footer-info">
                          <i class="el-icon-time"></i>
                          <span>实时更新</span>
                      </div>
                      <el-button 
                          type="text" 
                          class="detail-btn"
                          @click="$router.push('/orderOperate')"
                      >
                          查看详情
                          <i class="el-icon-arrow-right"></i>
                      </el-button>
                  </div>
              </div>

              <!-- 住院统计 -->
              <div class="stat-card hospitalization-card">
                  <div class="card-decoration">
                      <div class="decoration-circle decoration-1"></div>
                      <div class="decoration-circle decoration-2"></div>
                  </div>
                  <div class="card-content">
                      <div class="card-icon-section">
                          <div class="icon-wrapper hospitalization-icon">
                              <i class="el-icon-office-building"></i>
                          </div>
                      </div>
                      <div class="card-info-section">
                          <h3 class="card-title">住院概况</h3>
                          <div class="card-value">{{ bedPeople }}</div>
                          <p class="card-label">今日住院总人数</p>
                      </div>
                      <div class="card-trend-section">
                          <div class="trend-indicator positive">
                              <i class="el-icon-top"></i>
                              <span>今日数据</span>
                          </div>
                      </div>
                  </div>
                  <div class="card-footer">
                      <div class="footer-info">
                          <i class="el-icon-time"></i>
                          <span>实时更新</span>
                      </div>
                      <el-button 
                          type="text" 
                          class="detail-btn"
                          @click="$router.push('/myBed')"
                      >
                          查看详情
                          <i class="el-icon-arrow-right"></i>
                      </el-button>
                  </div>
              </div>

              <!-- 医生统计（示例数据） -->
              <div class="stat-card doctor-card">
                  <div class="card-decoration">
                      <div class="decoration-circle decoration-1"></div>
                      <div class="decoration-circle decoration-2"></div>
                  </div>
                  <div class="card-content">
                      <div class="card-icon-section">
                          <div class="icon-wrapper doctor-icon">
                              <i class="el-icon-user-solid"></i>
                          </div>
                      </div>
                      <div class="card-info-section">
                          <h3 class="card-title">医生资源</h3>
                          <div class="card-value">42</div>
                          <p class="card-label">今日在岗医生数量</p>
                      </div>
                      <div class="card-trend-section">
                          <div class="trend-indicator stable">
                              <i class="el-icon-right"></i>
                              <span>正常</span>
                          </div>
                      </div>
                  </div>
                  <div class="card-footer">
                      <div class="footer-info">
                          <i class="el-icon-info"></i>
                          <span>资源充足</span>
                      </div>
                      <el-button type="text" class="detail-btn" disabled>
                          系统管理
                          <i class="el-icon-arrow-right"></i>
                      </el-button>
                  </div>
              </div>

              <!-- 科室统计（示例数据） -->
              <div class="stat-card department-card">
                  <div class="card-decoration">
                      <div class="decoration-circle decoration-1"></div>
                      <div class="decoration-circle decoration-2"></div>
                  </div>
                  <div class="card-content">
                      <div class="card-icon-section">
                          <div class="icon-wrapper department-icon">
                              <i class="el-icon-office-building"></i>
                          </div>
                      </div>
                      <div class="card-info-section">
                          <h3 class="card-title">科室分布</h3>
                          <div class="card-value">15</div>
                          <p class="card-label">开放就诊科室数量</p>
                      </div>
                      <div class="card-trend-section">
                          <div class="trend-indicator stable">
                              <i class="el-icon-right"></i>
                              <span>正常</span>
                          </div>
                      </div>
                  </div>
                  <div class="card-footer">
                      <div class="footer-info">
                          <i class="el-icon-success"></i>
                          <span>全部开放</span>
                      </div>
                      <el-button 
                          type="text" 
                          class="detail-btn"
                          @click="$router.push('/orderOperate')"
                      >
                          选择科室
                          <i class="el-icon-arrow-right"></i>
                      </el-button>
                  </div>
              </div>
          </div>

          <!-- 快速操作区域 -->
          <div class="quick-actions-section">
              <div class="section-header">
                  <i class="el-icon-thumb quick-action-icon"></i>
                  <h3 class="section-title">快速操作</h3>
              </div>
              <div class="action-grid">
                  <div class="action-card" @click="$router.push('/orderOperate')">
                      <div class="action-icon appointment-action">
                          <i class="el-icon-date"></i>
                      </div>
                      <div class="action-content">
                          <h4>预约挂号</h4>
                          <p>在线预约医生门诊</p>
                      </div>
                      <div class="action-arrow">
                          <i class="el-icon-arrow-right"></i>
                      </div>
                  </div>

                  <div class="action-card" @click="$router.push('/myOrder')">
                      <div class="action-icon record-action">
                          <i class="el-icon-document"></i>
                      </div>
                      <div class="action-content">
                          <h4>我的挂号</h4>
                          <p>查看挂号记录状态</p>
                      </div>
                      <div class="action-arrow">
                          <i class="el-icon-arrow-right"></i>
                      </div>
                  </div>

                  <div class="action-card" @click="$router.push('/myBed')">
                      <div class="action-icon bed-action">
                          <i class="el-icon-office-building"></i>
                      </div>
                      <div class="action-content">
                          <h4>住院信息</h4>
                          <p>住院详情与费用</p>
                      </div>
                      <div class="action-arrow">
                          <i class="el-icon-arrow-right"></i>
                      </div>
                  </div>

                  <div class="action-card" @click="$router.push('/patientCard')">
                      <div class="action-icon profile-action">
                          <i class="el-icon-user"></i>
                      </div>
                      <div class="action-content">
                          <h4>个人信息</h4>
                          <p>个人资料管理</p>
                      </div>
                      <div class="action-arrow">
                          <i class="el-icon-arrow-right"></i>
                      </div>
                  </div>
              </div>
          </div>

          <!-- 数据更新时间 -->
          <div class="update-time-section">
              <el-card shadow="hover" class="update-card">
                  <div class="update-content">
                      <i class="el-icon-refresh update-icon"></i>
                      <div class="update-info">
                          <span class="update-text">数据最后更新时间：</span>
                          <span class="update-time">{{ currentTime }}</span>
                      </div>
                      <el-button 
                          type="primary" 
                          size="small" 
                          @click="refreshData"
                          :loading="refreshing"
                          class="refresh-btn"
                      >
                          <i class="el-icon-refresh"></i>
                          刷新数据
                      </el-button>
                  </div>
              </el-card>
          </div>
      </div>
  </div>
</template>

<script>
import request from "@/utils/request.js";

export default {
  name: "PatientLayout",
  data() {
      return {
          orderPeople: 0,
          bedPeople: 0,
          currentTime: '',
          refreshing: false
      };
  },
  methods: {
      // 请求预约人数
      requestPeople() {
          this.refreshing = true;
          request
              .get("order/orderPeople")
              .then((res) => {
                  this.refreshing = false;
                  if (res.data.status !== 200) {
                      this.$message.error("获取预约数据失败");
                      return;
                  }
                  this.orderPeople = res.data.data;
                  this.updateTime();
              })
              .catch((err) => {
                  this.refreshing = false;
                  console.error(err);
                  this.$message.error("网络错误，数据加载失败");
              });
      },

      // 请求住院人数
      requestBed() {
          request
              .get("bed/bedPeople")
              .then((res) => {
                  if (res.data.status !== 200) {
                      this.$message.error("获取住院数据失败");
                      return;
                  }
                  this.bedPeople = res.data.data;
                  this.updateTime();
              })
              .catch((err) => {
                  console.error(err);
                  this.$message.error("网络错误，数据加载失败");
              });
      },

      // 刷新数据
      refreshData() {
          this.requestPeople();
          this.requestBed();
          this.$message.success("数据刷新成功");
      },

      // 更新时间
      updateTime() {
          const now = new Date();
          this.currentTime = now.toLocaleString('zh-CN', {
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit'
          });
      }
  },
  created() {
      this.requestPeople();
      this.requestBed();
      this.updateTime();
      
      // 每分钟自动更新一次时间
      setInterval(() => {
          this.updateTime();
      }, 60000);
  }
};
</script>

<style lang="scss" scoped>
.patient-overview-management {
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #e4e8f0 100%);
  min-height: 100vh;
}

/* 顶部标题区域 */
.overview-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  padding: 20px 0;
}

.overview-title-content {
  z-index: 2;
}

.overview-title-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 10px;
}

.overview-page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.overview-page-subtitle {
  font-size: 16px;
  color: #5a6376;
  margin: 0;
}

.overview-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.overview-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.overview-circle-1 {
  width: 100px;
  height: 100px;
  background: #409eff;
  top: -30px;
  right: 0;
}

.overview-circle-2 {
  width: 70px;
  height: 70px;
  background: #67c23a;
  top: 10px;
  right: 80px;
}

.overview-circle-3 {
  width: 50px;
  height: 50px;
  background: #e6a23c;
  top: 40px;
  right: 40px;
}

/* 主容器 */
.overview-container {
  max-width: 1200px;
  margin: 0 auto;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

/* 统计卡片 */
.stat-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #f0f2f5;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-decoration {
  position: absolute;
  top: 0;
  right: 0;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.decoration-1 {
  width: 80px;
  height: 80px;
  background: currentColor;
  top: -40px;
  right: -40px;
}

.decoration-2 {
  width: 60px;
  height: 60px;
  background: currentColor;
  top: -20px;
  right: -20px;
  opacity: 0.2;
}

/* 卡片内容 */
.card-content {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.card-icon-section {
  margin-right: 20px;
}

.icon-wrapper {
  width: 70px;
  height: 70px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
}

.appointment-card .icon-wrapper {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.hospitalization-card .icon-wrapper {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.doctor-card .icon-wrapper {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.department-card .icon-wrapper {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.card-info-section {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.card-value {
  font-size: 32px;
  font-weight: 700;
  color: #1f2d3d;
  margin: 0 0 5px 0;
  line-height: 1;
}

.card-label {
  font-size: 12px;
  color: #c0c4cc;
  margin: 0;
}

.card-trend-section {
  text-align: right;
}

.trend-indicator {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.trend-indicator.positive {
  background: rgba(103, 194, 58, 0.1);
  color: #67c23a;
}

.trend-indicator.stable {
  background: rgba(144, 147, 153, 0.1);
  color: #909399;
}

/* 卡片底部 */
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #f0f2f5;
}

.footer-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #c0c4cc;
}

.detail-btn {
  color: #409eff;
  font-size: 12px;
  padding: 4px 8px;
}

.detail-btn:hover {
  color: #66b1ff;
  background: rgba(64, 158, 255, 0.1);
}

/* 快速操作区域 */
.quick-actions-section {
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 10px;
}

.quick-action-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 10px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
}

.action-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f0f2f5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.action-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.appointment-action {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.record-action {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.bed-action {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.profile-action {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.action-content {
  flex: 1;
}

.action-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 4px 0;
}

.action-content p {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

.action-arrow {
  color: #c0c4cc;
  transition: all 0.3s ease;
}

.action-card:hover .action-arrow {
  color: #409eff;
  transform: translateX(3px);
}

/* 更新时间区域 */
.update-time-section {
  margin-top: 30px;
}

.update-card {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
}

.update-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
}

.update-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 10px;
}

.update-info {
  flex: 1;
}

.update-text {
  font-size: 14px;
  color: #606266;
}

.update-time {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}

.refresh-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
  color: white;
  border-radius: 6px;
}

.refresh-btn:hover {
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .stats-grid {
      grid-template-columns: repeat(2, 1fr);
  }
  
  .action-grid {
      grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .patient-overview-management {
      padding: 15px;
  }
  
  .overview-header-section {
      flex-direction: column;
      text-align: center;
      margin-bottom: 20px;
  }
  
  .stats-grid {
      grid-template-columns: 1fr;
      gap: 15px;
  }
  
  .action-grid {
      grid-template-columns: 1fr;
  }
  
  .update-content {
      flex-direction: column;
      gap: 15px;
      text-align: center;
  }
  
  .card-content {
      flex-direction: column;
      text-align: center;
      gap: 15px;
  }
  
  .card-icon-section {
      margin-right: 0;
  }
  
  .card-trend-section {
      text-align: center;
  }
}

@media (max-width: 480px) {
  .patient-overview-management {
      padding: 10px;
  }
  
  .overview-page-title {
      font-size: 24px;
  }
  
  .stat-card {
      padding: 20px;
  }
  
  .icon-wrapper {
      width: 60px;
      height: 60px;
      font-size: 28px;
  }
  
  .card-value {
      font-size: 28px;
  }
  
  .action-card {
      padding: 15px;
  }
  
  .action-icon {
      width: 40px;
      height: 40px;
      font-size: 18px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
      opacity: 0;
      transform: translateY(20px);
  }
  to {
      opacity: 1;
      transform: translateY(0);
  }
}

.stat-card {
  animation: fadeInUp 0.5s ease forwards;
}

.stat-card:nth-child(1) { animation-delay: 0.1s; }
.stat-card:nth-child(2) { animation-delay: 0.2s; }
.stat-card:nth-child(3) { animation-delay: 0.3s; }
.stat-card:nth-child(4) { animation-delay: 0.4s; }

.action-card {
  animation: fadeInUp 0.6s ease forwards;
}

.action-card:nth-child(1) { animation-delay: 0.2s; }
.action-card:nth-child(2) { animation-delay: 0.3s; }
.action-card:nth-child(3) { animation-delay: 0.4s; }
.action-card:nth-child(4) { animation-delay: 0.5s; }

.update-card {
  animation: fadeInUp 0.7s ease forwards;
}

/* 加载动画 */
.refresh-btn .el-icon-loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>