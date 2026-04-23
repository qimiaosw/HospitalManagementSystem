<template>
  <div class="department-management">
      <!-- 顶部标题区域 -->
      <div class="header-section">
          <div class="title-content">
              <i class="el-icon-office-building title-icon"></i>
              <h1 class="page-title">科室选择</h1>
              <p class="page-subtitle">请选择您需要就诊的科室</p>
          </div>
          <div class="decoration-element">
              <div class="circle circle-1"></div>
              <div class="circle circle-2"></div>
              <div class="circle circle-3"></div>
          </div>
      </div>

      <el-card class="management-card" shadow="hover">
          <!-- 面包屑导航 -->
          <div class="breadcrumb-section">
              <el-breadcrumb separator-class="el-icon-arrow-right" class="custom-breadcrumb">
                  <el-breadcrumb-item :to="{ path: '/' }">
                      <i class="el-icon-s-home"></i>
                      首页
                  </el-breadcrumb-item>
                  <el-breadcrumb-item :to="{ path: '/orderOperate' }">科室选择</el-breadcrumb-item>
                  <el-breadcrumb-item>日期选择</el-breadcrumb-item>
                  <el-breadcrumb-item>挂号</el-breadcrumb-item>
              </el-breadcrumb>
          </div>

          <!-- 科室分类展示 -->
          <div class="department-container">
              <!-- 内科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-first-aid-kit section-icon"></i>
                      <h3 class="section-title">内科</h3>
                      <span class="section-count">{{ inters.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="inter in inters" 
                          :key="inter"
                          class="department-card"
                          @click="sectionClick(inter)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-first-aid-kit"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ inter }}</h4>
                              <p class="department-desc">内科专科诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 外科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-scissors section-icon"></i>
                      <h3 class="section-title">外科</h3>
                      <span class="section-count">{{ outs.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="out in outs" 
                          :key="out"
                          class="department-card"
                          @click="sectionClick(out)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-scissors"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ out }}</h4>
                              <p class="department-desc">外科手术诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 妇产科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-female section-icon"></i>
                      <h3 class="section-title">妇产科</h3>
                      <span class="section-count">{{ women.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="woman in women" 
                          :key="woman"
                          class="department-card"
                          @click="sectionClick(woman)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-female"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ woman }}</h4>
                              <p class="department-desc">妇产专科诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 儿科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-child section-icon"></i>
                      <h3 class="section-title">儿科</h3>
                      <span class="section-count">{{ kids.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="kid in kids" 
                          :key="kid"
                          class="department-card"
                          @click="sectionClick(kid)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-child"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ kid }}</h4>
                              <p class="department-desc">儿童专科诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 五官科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-view section-icon"></i>
                      <h3 class="section-title">五官科</h3>
                      <span class="section-count">{{ fives.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="five in fives" 
                          :key="five"
                          class="department-card"
                          @click="sectionClick(five)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-view"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ five }}</h4>
                              <p class="department-desc">五官专科诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 中医科 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-orange section-icon"></i>
                      <h3 class="section-title">中医科</h3>
                      <span class="section-count">{{ chineses.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="chinese in chineses" 
                          :key="chinese"
                          class="department-card"
                          @click="sectionClick(chinese)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-orange"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ chinese }}</h4>
                              <p class="department-desc">中医传统诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>

              <!-- 其他科室 -->
              <div class="department-section">
                  <div class="section-header">
                      <i class="el-icon-more-outline section-icon"></i>
                      <h3 class="section-title">其他科室</h3>
                      <span class="section-count">{{ orthers.length }}个科室</span>
                  </div>
                  <div class="department-grid">
                      <div 
                          v-for="orther in orthers" 
                          :key="orther"
                          class="department-card"
                          @click="sectionClick(orther)"
                      >
                          <div class="card-icon">
                              <i class="el-icon-more-outline"></i>
                          </div>
                          <div class="card-content">
                              <h4 class="department-name">{{ orther }}</h4>
                              <p class="department-desc">综合专科诊疗</p>
                          </div>
                          <div class="card-arrow">
                              <i class="el-icon-arrow-right"></i>
                          </div>
                      </div>
                  </div>
              </div>
          </div>

          <!-- 统计信息 -->
          <div class="stats-section">
              <el-row :gutter="20">
                  <el-col :span="6">
                      <div class="stat-card total-stat">
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
                      <div class="stat-card internal-stat">
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
                      <div class="stat-card surgery-stat">
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
                      <div class="stat-card other-stat">
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
      </el-card>
  </div>
</template>

<script>
export default {
  name: "orderOperate",
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
      //点击科室
      sectionClick(sectionOpt){
          this.$router.push({path: '/sectionMessage?sectionOpt=' + sectionOpt});
      }
  },
};
</script>

<style lang="scss" scoped>
.department-management {
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
  color: #67c23a;
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
  background: #67c23a;
  top: -30px;
  right: 0;
}

.circle-2 {
  width: 70px;
  height: 70px;
  background: #409eff;
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

/* 主卡片样式 */
.management-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.management-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 面包屑导航 */
.breadcrumb-section {
  padding: 20px;
  background: #f8fafc;
  border-bottom: 1px solid #e6e8eb;
}

.custom-breadcrumb {
  .el-breadcrumb__item {
      .el-breadcrumb__inner {
          display: flex;
          align-items: center;
          gap: 5px;
          color: #5a6376;
          transition: color 0.3s ease;
          font-weight: 500;
      }
      
      &:hover .el-breadcrumb__inner {
          color: #67c23a;
      }
      
      &:last-child .el-breadcrumb__inner {
          color: #67c23a;
          font-weight: 600;
      }
  }
}

/* 科室容器 */
.department-container {
  padding: 20px;
}

.department-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f2f5;
  
  &:last-child {
      border-bottom: none;
      margin-bottom: 0;
  }
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 0;
}

.section-icon {
  font-size: 24px;
  color: #67c23a;
  margin-right: 12px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
  flex: 1;
}

.section-count {
  font-size: 14px;
  color: #909399;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 12px;
  font-weight: 500;
}

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
  border-color: #67c23a;
}

.department-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(103, 194, 58, 0.1), transparent);
  transition: left 0.6s ease;
}

.department-card:hover::before {
  left: 100%;
}

.card-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
}

.card-icon i {
  font-size: 24px;
  color: white;
}

.card-content {
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

.card-arrow {
  color: #c0c4cc;
  transition: all 0.3s ease;
}

.department-card:hover .card-arrow {
  color: #67c23a;
  transform: translateX(3px);
}

/* 统计信息区域 */
.stats-section {
  padding: 20px;
  background: #f8fafc;
  border-top: 1px solid #e6e8eb;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100px;
}

.stat-card:hover {
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
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.internal-stat .stat-icon {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
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

/* 响应式设计 */
@media (max-width: 1200px) {
  .department-grid {
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 768px) {
  .department-management {
      padding: 10px;
  }
  
  .header-section {
      flex-direction: column;
      text-align: center;
  }
  
  .breadcrumb-section {
      padding: 15px;
  }
  
  .department-container {
      padding: 15px;
  }
  
  .department-grid {
      grid-template-columns: 1fr;
      gap: 12px;
  }
  
  .department-card {
      padding: 15px;
  }
  
  .card-icon {
      width: 40px;
      height: 40px;
      margin-right: 12px;
  }
  
  .card-icon i {
      font-size: 20px;
  }
  
  .department-name {
      font-size: 14px;
  }
  
  .stats-section {
      padding: 15px;
  }
  
  .stat-card {
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
  
  .card-icon {
      margin-right: 0;
      margin-bottom: 10px;
  }
  
  .card-arrow {
      display: none;
  }
  
  .section-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;
  }
  
  .section-count {
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

.department-section {
  animation: fadeInUp 0.5s ease forwards;
}

.department-section:nth-child(1) { animation-delay: 0.1s; }
.department-section:nth-child(2) { animation-delay: 0.2s; }
.department-section:nth-child(3) { animation-delay: 0.3s; }
.department-section:nth-child(4) { animation-delay: 0.4s; }
.department-section:nth-child(5) { animation-delay: 0.5s; }
.department-section:nth-child(6) { animation-delay: 0.6s; }
.department-section:nth-child(7) { animation-delay: 0.7s; }

.stat-card {
  animation: fadeInUp 0.4s ease forwards;
}

.stat-card:nth-child(1) { animation-delay: 0.2s; }
.stat-card:nth-child(2) { animation-delay: 0.3s; }
.stat-card:nth-child(3) { animation-delay: 0.4s; }
.stat-card:nth-child(4) { animation-delay: 0.5s; }
</style>