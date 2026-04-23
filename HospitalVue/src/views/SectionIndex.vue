<template>
  <div class="section-management">
      <!-- 顶部标题区域 -->
      <div class="section-header-section">
          <div class="section-title-content">
              <i class="el-icon-office-building section-title-icon"></i>
              <h1 class="section-page-title">科室门诊选择</h1>
              <p class="section-page-subtitle">请选择您需要就诊的科室</p>
          </div>
          <div class="section-decoration-element">
              <div class="section-circle section-circle-1"></div>
              <div class="section-circle section-circle-2"></div>
              <div class="section-circle section-circle-3"></div>
          </div>
      </div>

      <el-card class="section-management-card" shadow="hover">
          <!-- 科室分类展示 -->
          <div class="section-category-container">
              <!-- 内科 -->
              <div class="category-section internal-medicine">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-first-aid-kit"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">内科</h3>
                          <span class="category-count">{{ inters.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="inter in inters" 
                          :key="inter"
                          class="department-card"
                          @click="sectionClick(inter)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-first-aid-kit"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ inter }}</h4>
                              <p class="department-desc">内科专科诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 外科 -->
              <div class="category-section surgery">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-scissors"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">外科</h3>
                          <span class="category-count">{{ outs.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="out in outs" 
                          :key="out"
                          class="department-card"
                          @click="sectionClick(out)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-scissors"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ out }}</h4>
                              <p class="department-desc">外科手术诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 妇产科 -->
              <div class="category-section obstetrics-gynecology">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-female"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">妇产科</h3>
                          <span class="category-count">{{ women.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="woman in women" 
                          :key="woman"
                          class="department-card"
                          @click="sectionClick(woman)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-female"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ woman }}</h4>
                              <p class="department-desc">妇产专科诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 儿科 -->
              <div class="category-section pediatrics">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-child"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">儿科</h3>
                          <span class="category-count">{{ kids.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="kid in kids" 
                          :key="kid"
                          class="department-card"
                          @click="sectionClick(kid)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-child"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ kid }}</h4>
                              <p class="department-desc">儿童专科诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 五官科 -->
              <div class="category-section ent-ophthalmology">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-view"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">五官科</h3>
                          <span class="category-count">{{ fives.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="five in fives" 
                          :key="five"
                          class="department-card"
                          @click="sectionClick(five)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-view"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ five }}</h4>
                              <p class="department-desc">五官专科诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 中医科 -->
              <div class="category-section traditional-chinese">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-orange"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">中医科</h3>
                          <span class="category-count">{{ chineses.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="chinese in chineses" 
                          :key="chinese"
                          class="department-card"
                          @click="sectionClick(chinese)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-orange"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ chinese }}</h4>
                              <p class="department-desc">中医传统诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 其他科室 -->
              <div class="category-section other-departments">
                  <div class="category-header">
                      <div class="category-icon">
                          <i class="el-icon-more-outline"></i>
                      </div>
                      <div class="category-info">
                          <h3 class="category-title">其他科室</h3>
                          <span class="category-count">{{ orthers.length }}个科室</span>
                      </div>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="orther in orthers" 
                          :key="orther"
                          class="department-card"
                          @click="sectionClick(orther)"
                      >
                          <div class="department-icon">
                              <i class="el-icon-more-outline"></i>
                          </div>
                          <div class="department-content">
                              <h4 class="department-name">{{ orther }}</h4>
                              <p class="department-desc">综合专科诊疗</p>
                          </div>
                          <div class="department-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>
          </div>

          <!-- 统计信息 -->
          <div class="section-stats-section">
              <el-row :gutter="20">
                  <el-col :span="6">
                      <div class="section-stat-card total-stat">
                          <div class="stat-icon">
                              <i class="el-icon-office-building"></i>
                          </div>
                          <div class="stat-content">
                              <div class="stat-number">{{ totalDepartments }}</div>
                              <div class="stat-label">总科室数</div>
                          </div>
                      </div>
                  </el-col>
                  <el-col :span="6">
                      <div class="section-stat-card internal-stat">
                          <div class="stat-icon">
                              <i class="el-icon-first-aid-kit"></i>
                          </div>
                          <div class="stat-content">
                              <div class="stat-number">{{ inters.length }}</div>
                              <div class="stat-label">内科科室</div>
                          </div>
                      </div>
                  </el-col>
                  <el-col :span="6">
                      <div class="section-stat-card surgery-stat">
                          <div class="stat-icon">
                              <i class="el-icon-scissors"></i>
                          </div>
                          <div class="stat-content">
                              <div class="stat-number">{{ outs.length }}</div>
                              <div class="stat-label">外科科室</div>
                          </div>
                      </div>
                  </el-col>
                  <el-col :span="6">
                      <div class="section-stat-card other-stat">
                          <div class="stat-icon">
                              <i class="el-icon-more-outline"></i>
                          </div>
                          <div class="stat-content">
                              <div class="stat-number">{{ otherDepartmentsCount }}</div>
                              <div class="stat-label">其他科室</div>
                          </div>
                      </div>
                  </el-col>
              </el-row>
          </div>

          <!-- 操作提示 -->
          <div class="section-tips-section">
              <el-alert
                  title="温馨提示"
                  description="请根据您的症状选择合适的科室，点击科室卡片可查看该科室的医生排班信息"
                  type="info"
                  :closable="false"
                  show-icon
                  class="section-tips-alert"
              >
              </el-alert>
          </div>
      </el-card>
  </div>
</template>

<script>
export default {
  name: "SectionIndex",
  data() {
      return {
          inters: [
              "神经内科",
              "呼吸与危重症医学科",
              "内分泌科",
              "消化内科",
              "心血管内科",
              "肾内科",
              "发热门诊",
          ],
          outs: [
              "手足外科",
              "普通外科",
              "肛肠外科",
              "神经外科",
              "泌尿外科",
              "骨科",
              "烧伤整形外科",
          ],
          women: ["妇科", "产科"],
          kids: ["儿科", "儿童保健科"],
          fives: ["耳鼻咽喉科", "眼科", "口腔科"],
          chineses: ["中医科"],
          orthers: ["康复医学科", "急诊科", "皮肤性病科", "功能科"],
      };
  },
  computed: {
      // 计算总科室数
      totalDepartments() {
          return this.inters.length + this.outs.length + this.women.length + 
                 this.kids.length + this.fives.length + this.chineses.length + 
                 this.orthers.length;
      },
      // 计算其他科室数（除内外科外）
      otherDepartmentsCount() {
          return this.women.length + this.kids.length + this.fives.length + 
                 this.chineses.length + this.orthers.length;
      }
  },
  methods: {
      // 科室点击
      sectionClick(section) {
          this.$message.success(`正在跳转到 ${section} 医生排班页面`);
          this.$router.push({ path: '/arrangeDoctor?section=' + section });
      }
  }
};
</script>

<style lang="scss" scoped>
.section-management {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

/* 顶部标题区域 */
.section-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  padding: 20px 0;
}

.section-title-content {
  z-index: 2;
}

.section-title-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 10px;
}

.section-page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-page-subtitle {
  font-size: 16px;
  color: #5a6376;
  margin: 0;
}

.section-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.section-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.section-circle-1 {
  width: 100px;
  height: 100px;
  background: #409eff;
  top: -30px;
  right: 0;
}

.section-circle-2 {
  width: 70px;
  height: 70px;
  background: #67c23a;
  top: 10px;
  right: 80px;
}

.section-circle-3 {
  width: 50px;
  height: 50px;
  background: #e6a23c;
  top: 40px;
  right: 40px;
}

/* 主卡片样式 */
.section-management-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.section-management-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 科室分类容器 */
.section-category-container {
  padding: 20px;
}

.category-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f2f5;
  
  &:last-child {
      border-bottom: none;
      margin-bottom: 0;
  }
}

.category-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 0;
}

.category-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.internal-medicine .category-icon {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.surgery .category-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.obstetrics-gynecology .category-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.pediatrics .category-icon {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.ent-ophthalmology .category-icon {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.traditional-chinese .category-icon {
  background: linear-gradient(135deg, #8e44ad 0%, #9b59b6 100%);
}

.other-departments .category-icon {
  background: linear-gradient(135deg, #34495e 0%, #2c3e50 100%);
}

.category-info {
  flex: 1;
}

.category-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
}

.category-count {
  font-size: 14px;
  color: #909399;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 12px;
  font-weight: 500;
}

/* 科室网格布局 */
.department-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.department-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  border: 1px solid #e6e8eb;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.department-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.department-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(64, 158, 255, 0.1), transparent);
  transition: left 0.6s ease;
}

.department-card:hover::before {
  left: 100%;
}

.department-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
}

.department-icon i {
  font-size: 24px;
  color: white;
}

.department-content {
  flex: 1;
}

.department-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
}

.department-desc {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

.department-arrow {
  color: #c0c4cc;
  transition: all 0.3s ease;
}

.department-card:hover .department-arrow {
  color: #409eff;
  transform: translateX(3px);
}

/* 统计信息区域 */
.section-stats-section {
  padding: 20px;
  background: #f8fafc;
  border-top: 1px solid #e6e8eb;
}

.section-stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100px;
}

.section-stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 28px;
  color: white;
}

.total-stat .stat-icon {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.internal-stat .stat-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.surgery-stat .stat-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.other-stat .stat-icon {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1f2d3d;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 提示信息区域 */
.section-tips-section {
  padding: 20px;
  border-top: 1px solid #e6e8eb;
}

.section-tips-alert {
  border-radius: 8px;
  border: none;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .department-grid {
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 768px) {
  .section-management {
      padding: 10px;
  }
  
  .section-header-section {
      flex-direction: column;
      text-align: center;
  }
  
  .section-category-container {
      padding: 15px;
  }
  
  .department-grid {
      grid-template-columns: 1fr;
      gap: 12px;
  }
  
  .department-card {
      padding: 15px;
  }
  
  .department-icon {
      width: 40px;
      height: 40px;
      margin-right: 12px;
  }
  
  .department-icon i {
      font-size: 20px;
  }
  
  .department-name {
      font-size: 14px;
  }
  
  .section-stats-section {
      padding: 15px;
  }
  
  .section-stat-card {
      padding: 15px;
      height: 80px;
      margin-bottom: 15px;
  }
  
  .stat-icon {
      width: 50px;
      height: 50px;
      font-size: 24px;
      margin-right: 12px;
  }
  
  .stat-number {
      font-size: 20px;
  }
  
  .stat-label {
      font-size: 12px;
  }
}

@media (max-width: 480px) {
  .department-card {
      flex-direction: column;
      text-align: center;
      padding: 20px 15px;
  }
  
  .department-icon {
      margin-right: 0;
      margin-bottom: 10px;
  }
  
  .department-arrow {
      display: none;
  }
  
  .category-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;
  }
  
  .category-count {
      align-self: flex-start;
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

.category-section {
  animation: fadeInUp 0.5s ease forwards;
}

.category-section:nth-child(1) { animation-delay: 0.1s; }
.category-section:nth-child(2) { animation-delay: 0.2s; }
.category-section:nth-child(3) { animation-delay: 0.3s; }
.category-section:nth-child(4) { animation-delay: 0.4s; }
.category-section:nth-child(5) { animation-delay: 0.5s; }
.category-section:nth-child(6) { animation-delay: 0.6s; }
.category-section:nth-child(7) { animation-delay: 0.7s; }

.section-stat-card {
  animation: fadeInUp 0.4s ease forwards;
}

.section-stat-card:nth-child(1) { animation-delay: 0.2s; }
.section-stat-card:nth-child(2) { animation-delay: 0.3s; }
.section-stat-card:nth-child(3) { animation-delay: 0.4s; }
.section-stat-card:nth-child(4) { animation-delay: 0.5s; }
</style>