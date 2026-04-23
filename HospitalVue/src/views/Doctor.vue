<template>
  <div class="doctor-layout-management">
    <!-- 顶部标题区域 -->
    <div class="doctor-header-section">
      <div class="doctor-title-content">
        <div class="logo-section">
          <div class="logo-icon">
            <img src="@/assets/img/1.png" alt="医院管理系统" class="logo-image" />
          </div>
          <div class="logo-text">
            <h1 class="doctor-page-title">医院管理系统</h1>
            <p class="doctor-page-subtitle">医生工作台</p>
          </div>
        </div>
      </div>
      
      <div class="doctor-user-section">
        <div class="user-info-card">
          <div class="user-avatar-section">
            <div class="avatar-container">
              <img src="../assets/11.jpg" alt="医生头像" class="user-avatar-img" />
              <div class="online-status"></div>
            </div>
            <div class="user-details">
              <div class="user-welcome">欢迎您，</div>
              <div class="user-name">{{ userName }}</div>
              <div class="user-role">医生</div>
            </div>
          </div>
          
          <el-dropdown 
            @command="handleCommand" 
            class="user-dropdown" 
            trigger="click"
            placement="bottom-end"
          >
            <div class="dropdown-trigger">
              <i class="el-icon-more more-icon"></i>
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
              <el-dropdown-item command="logout" class="dropdown-item logout-item">
                <i class="el-icon-switch-button dropdown-icon"></i>
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <div class="doctor-decoration-element">
        <div class="doctor-circle doctor-circle-1"></div>
        <div class="doctor-circle doctor-circle-2"></div>
        <div class="doctor-circle doctor-circle-3"></div>
      </div>
    </div>

    <div class="doctor-content-container">
      <!-- 侧边栏 -->
      <div class="doctor-sidebar-section">
        <div class="modern-sidebar">
          <div class="sidebar-header">
            <div class="sidebar-title">
              <i class="el-icon-suitcase sidebar-title-icon"></i>
              <h3 class="sidebar-title-text">医生工作台</h3>
            </div>
            <div class="sidebar-subtitle">医疗工作导航</div>
          </div>
          
          <div class="sidebar-menu">
            <div 
              v-for="item in menuItems" 
              :key="item.path"
              :class="['menu-item', activePath === item.path ? 'active' : '']"
              @click="menuClick(item.path)"
            >
              <div class="menu-icon-wrapper">
                <i :class="item.icon"></i>
              </div>
              <div class="menu-content">
                <span class="menu-text">{{ item.name }}</span>
                <span class="menu-desc">{{ item.desc }}</span>
              </div>
              <div class="menu-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
          
          <div class="sidebar-footer">
            <div class="system-info">
              <i class="el-icon-time info-icon"></i>
              <span class="info-text">当前时间：{{ currentTime }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 主内容区域 -->
      <div class="doctor-main-section">
        <div class="main-content-card">
          <router-view>
            <div class="welcome-content">
              <div class="welcome-icon">
                <i class="el-icon-first-aid-kit"></i>
              </div>
              <h2 class="welcome-title">欢迎使用医生工作台</h2>
              <p class="welcome-subtitle">请从左侧菜单选择您要操作的功能模块</p>
              <div class="welcome-stats">
                <div class="stat-item">
                  <div class="stat-icon today">
                    <i class="el-icon-date"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">今日挂号</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon history">
                    <i class="el-icon-notebook-2"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">历史挂号</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon hospital">
                    <i class="el-icon-office-building"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">住院申请</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon profile">
                    <i class="el-icon-user"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">1</div>
                    <div class="stat-label">个人信息</div>
                  </div>
                </div>
              </div>
            </div>
          </router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken, clearToken, getActivePath, setActivePath} from "@/utils/storage.js";

export default {
  name: "Doctor",
  data() {
    return {
      userName: "",
      activePath: "",
      currentTime: "",
      menuItems: [
        {
          name: "首页",
          path: "doctorLayout",
          icon: "el-icon-s-home",
          desc: "工作台概览与统计"
        },
        {
          name: "今日挂号列表",
          path: "orderToday",
          icon: "el-icon-news",
          desc: "今日患者挂号管理"
        },
        {
          name: "历史挂号列表",
          path: "doctorOrder",
          icon: "el-icon-monitor",
          desc: "历史挂号记录查询"
        },
        {
          name: "住院申请管理",
          path: "inBed",
          icon: "el-icon-postcard",
          desc: "患者住院申请处理"
        },
        {
          name: "个人信息查询",
          path: "doctorCard",
          icon: "el-icon-user-solid",
          desc: "个人资料与信息"
        }
      ]
    };
  },
  methods: {
    handleCommand(command) {
      if (command === "logout") {
        this.$confirm("确定要退出登录吗？", "安全退出", {
          confirmButtonText: "确定退出",
          cancelButtonText: "取消",
          type: "warning",
          confirmButtonClass: "el-button--danger",
          customClass: "logout-confirm-dialog"
        }).then(() => {
          clearToken();
          this.$message({
            type: "success",
            message: "退出登录成功！",
            duration: 1500
          });
          setTimeout(() => {
            this.$router.push("/login");
          }, 500);
        }).catch(() => {
          this.$message.info("已取消退出操作");
        });
      }
    },
    
    tokenDecode(token){
      if (token !== null)
      return jwtDecode(token);
    },
    
    menuClick(path){
      this.activePath = path;
      setActivePath(path);
      if(this.$route.path !== "/" + path) this.$router.push(path);
      console.log("导航至:", path);
    },
    
    logout() {
      this.$confirm("此操作将退出登录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          clearToken();
          this.$message({
            type: "success",
            message: "退出登录成功!",
          });
          this.$router.push("login");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    
    updateCurrentTime() {
      const now = new Date();
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        weekday: 'short'
      });
    }
  },
  created() {
    this.activePath = getActivePath() || "doctorLayout";
    this.userName = this.tokenDecode(getToken()).dName || "医生";
    
    // 更新时间
    this.updateCurrentTime();
    setInterval(() => {
      this.updateCurrentTime();
    }, 1000);
  }
};
</script>

<style lang="scss" scoped>
.doctor-layout-management {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  display: flex;
  flex-direction: column;
}

/* 顶部标题区域 */
.doctor-header-section {
  background: linear-gradient(135deg, #1a6bc5 0%, #2c8bd9 100%);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  padding: 0 30px;
  height: 80px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.doctor-title-content {
  display: flex;
  align-items: center;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: linear-gradient(135deg, #1a6bc5 0%, #2c8bd9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
  box-shadow: 0 4px 15px rgba(255, 255, 255, 0.3);
}

.logo-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.doctor-page-title {
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 0 0 2px 0;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.doctor-page-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-weight: 300;
  letter-spacing: 0.5px;
}

/* 用户信息区域 */
.doctor-user-section {
  display: flex;
  align-items: center;
}

.user-info-card {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  padding: 8px 15px 8px 8px;
  border: 1px solid rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.user-info-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.6s ease;
}

.user-info-card:hover::before {
  left: 100%;
}

.user-info-card:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.35);
  transform: translateY(-1px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.user-avatar-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar-container {
  position: relative;
  width: 45px;
  height: 45px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.4);
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.online-status {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #67c23a;
  border: 2px solid #1a6bc5;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-welcome {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 300;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: white;
  line-height: 1;
}

.user-role {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.3);
  padding: 2px 6px;
  border-radius: 8px;
  font-weight: 500;
}

/* 下拉菜单 */
.user-dropdown {
  margin-left: 10px;
}

.dropdown-trigger {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.9);
}

.dropdown-trigger:hover {
  background: rgba(255, 255, 255, 0.3);
  color: white;
  transform: rotate(90deg);
}

.more-icon {
  font-size: 14px;
}

/* 下拉菜单样式 */
.user-dropdown-menu {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  padding: 8px 0;
  background: white;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  font-size: 13px;
  color: #606266;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
  color: #409eff;
}

.dropdown-icon {
  font-size: 14px;
  width: 16px;
  text-align: center;
}

.logout-item {
  color: #f56c6c;
}

.logout-item:hover {
  background: linear-gradient(135deg, #fef0f0 0%, #fde2e2 100%);
  color: #f56c6c;
}

/* 装饰元素 */
.doctor-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
  pointer-events: none;
}

.doctor-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.doctor-circle-1 {
  width: 120px;
  height: 120px;
  background: #ffffff;
  top: -60px;
  right: -30px;
}

.doctor-circle-2 {
  width: 80px;
  height: 80px;
  background: #a5d8ff;
  top: -20px;
  right: 60px;
}

.doctor-circle-3 {
  width: 60px;
  height: 60px;
  background: #74c0fc;
  top: 20px;
  right: 30px;
}

/* 主内容布局 */
.doctor-content-container {
  display: flex;
  flex: 1;
  min-height: calc(100vh - 80px);
}

/* 侧边栏样式 */
.doctor-sidebar-section {
  width: 280px;
  flex-shrink: 0;
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  z-index: 10;
}

.modern-sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  border-right: 1px solid #e6e8eb;
}

.sidebar-header {
  padding: 25px 20px 15px;
  border-bottom: 1px solid #f0f2f5;
  background: linear-gradient(135deg, #f8fafc 0%, #f0f7ff 100%);
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.sidebar-title-icon {
  font-size: 20px;
  color: #1a6bc5;
}

.sidebar-title-text {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.sidebar-subtitle {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

.sidebar-menu {
  flex: 1;
  padding: 15px 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  margin: 0 10px 4px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  border: 1px solid transparent;
}

.menu-item:hover {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
  border-color: #d9ecff;
  transform: translateX(5px);
  box-shadow: 0 4px 12px rgba(26, 107, 197, 0.15);
}

.menu-item.active {
  background: linear-gradient(135deg, #1a6bc5 0%, #2c8bd9 100%);
  box-shadow: 0 4px 15px rgba(26, 107, 197, 0.3);
  border-color: #1a6bc5;
}

.menu-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 30px;
  background: white;
  border-radius: 0 2px 2px 0;
}

.menu-icon-wrapper {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: rgba(26, 107, 197, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  transition: all 0.3s ease;
}

.menu-item:hover .menu-icon-wrapper {
  background: rgba(26, 107, 197, 0.2);
  transform: scale(1.1);
}

.menu-item.active .menu-icon-wrapper {
  background: rgba(255, 255, 255, 0.2);
}

.menu-icon-wrapper i {
  font-size: 18px;
  color: #1a6bc5;
  transition: all 0.3s ease;
}

.menu-item:hover .menu-icon-wrapper i {
  color: #1a6bc5;
  transform: scale(1.1);
}

.menu-item.active .menu-icon-wrapper i {
  color: white;
}

.menu-content {
  flex: 1;
}

.menu-text {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 4px;
  transition: all 0.3s ease;
}

.menu-desc {
  display: block;
  font-size: 11px;
  color: #909399;
  transition: all 0.3s ease;
}

.menu-item:hover .menu-text {
  color: #1a6bc5;
}

.menu-item:hover .menu-desc {
  color: #606266;
}

.menu-item.active .menu-text,
.menu-item.active .menu-desc {
  color: white;
}

.menu-arrow {
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
  color: #c0c4cc;
}

.menu-item:hover .menu-arrow {
  opacity: 1;
  transform: translateX(0);
}

.menu-item.active .menu-arrow {
  opacity: 1;
  transform: translateX(0);
  color: white;
}

.sidebar-footer {
  padding: 15px 20px;
  border-top: 1px solid #f0f2f5;
}

.system-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f8fafc;
  border-radius: 6px;
  font-size: 11px;
  color: #909399;
}

.info-icon {
  font-size: 12px;
}

.info-text {
  font-weight: 500;
}

/* 主内容区域 */
.doctor-main-section {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  overflow-y: auto;
}

.main-content-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  min-height: calc(100vh - 120px);
  overflow: hidden;
}

/* 欢迎页面样式 */
.welcome-content {
  padding: 40px;
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1a6bc5 0%, #2c8bd9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  font-size: 36px;
  color: white;
  box-shadow: 0 4px 15px rgba(26, 107, 197, 0.3);
}

.welcome-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 10px 0;
}

.welcome-subtitle {
  font-size: 16px;
  color: #606266;
  margin: 0 0 30px 0;
}

.welcome-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  max-width: 600px;
  width: 100%;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e6e8eb;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.stat-icon.today {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-icon.history {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.stat-icon.hospital {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.stat-icon.profile {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1f2d3d;
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .doctor-sidebar-section {
    width: 250px;
  }
  
  .welcome-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .doctor-header-section {
    padding: 0 20px;
    height: 70px;
  }
  
  .logo-icon {
    width: 50px;
    height: 50px;
  }
  
  .doctor-page-title {
    font-size: 20px;
  }
  
  .doctor-page-subtitle {
    font-size: 11px;
  }
  
  .doctor-sidebar-section {
    width: 220px;
  }
  
  .doctor-main-section {
    padding: 15px;
  }
  
  .menu-item {
    padding: 10px 15px;
    margin: 0 8px 4px;
  }
  
  .menu-icon-wrapper {
    width: 36px;
    height: 36px;
    margin-right: 12px;
  }
  
  .menu-text {
    font-size: 13px;
  }
  
  .menu-desc {
    font-size: 10px;
  }
  
  .welcome-content {
    padding: 30px 20px;
  }
  
  .welcome-stats {
    grid-template-columns: 1fr;
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .doctor-header-section {
    padding: 0 15px;
    flex-direction: column;
    height: auto;
    padding: 10px 15px;
  }
  
  .doctor-title-content {
    margin-bottom: 10px;
  }
  
  .doctor-user-section {
    width: 100%;
    justify-content: center;
  }
  
  .doctor-content-container {
    flex-direction: column;
  }
  
  .doctor-sidebar-section {
    width: 100%;
    height: auto;
  }
  
  .modern-sidebar {
    height: auto;
  }
  
  .sidebar-menu {
    display: flex;
    flex-wrap: wrap;
    padding: 10px;
    gap: 8px;
  }
  
  .menu-item {
    flex: 1;
    min-width: 140px;
    flex-direction: column;
    text-align: center;
    padding: 15px 10px;
  }
  
  .menu-icon-wrapper {
    margin-right: 0;
    margin-bottom: 8px;
  }
  
  .menu-arrow {
    display: none;
  }
  
  .welcome-content {
    padding: 20px 15px;
  }
  
  .welcome-title {
    font-size: 20px;
  }
  
  .welcome-subtitle {
    font-size: 14px;
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

.doctor-header-section {
  animation: fadeInUp 0.5s ease forwards;
}

.menu-item {
  animation: fadeInUp 0.4s ease forwards;
}

.menu-item:nth-child(1) { animation-delay: 0.1s; }
.menu-item:nth-child(2) { animation-delay: 0.2s; }
.menu-item:nth-child(3) { animation-delay: 0.3s; }
.menu-item:nth-child(4) { animation-delay: 0.4s; }
.menu-item:nth-child(5) { animation-delay: 0.5s; }

.welcome-content {
  animation: fadeInUp 0.6s ease forwards;
}

.stat-item {
  animation: fadeInUp 0.4s ease forwards;
}

.stat-item:nth-child(1) { animation-delay: 0.2s; }
.stat-item:nth-child(2) { animation-delay: 0.3s; }
.stat-item:nth-child(3) { animation-delay: 0.4s; }
.stat-item:nth-child(4) { animation-delay: 0.5s; }
</style>