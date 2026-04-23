<template>
  <div class="admin-layout-management">
    <!-- 顶部标题区域 -->
    <div class="admin-header-section">
      <div class="admin-title-content">
        <div class="logo-section">
          <div class="logo-icon">
            <img src="@/assets/img/1.png" alt="医院管理系统" class="logo-image" />
          </div>
          <div class="logo-text">
            <h1 class="admin-page-title">医院管理系统</h1>
            <p class="admin-page-subtitle">Hospital Management System</p>
          </div>
        </div>
      </div>

      <div class="admin-user-section">
        <div class="user-info-card">
          <div class="user-avatar-section">
            <div class="avatar-container">
              <img src="../assets/guanliyuan.jpg" alt="管理员头像" class="user-avatar-img" />
              <div class="online-status"></div>
            </div>
            <div class="user-details">
              <div class="user-welcome">欢迎您，</div>
              <div class="user-name">{{ userName }}</div>
              <div class="user-role">管理员</div>
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

      <div class="admin-decoration-element">
        <div class="admin-circle admin-circle-1"></div>
        <div class="admin-circle admin-circle-2"></div>
        <div class="admin-circle admin-circle-3"></div>
      </div>
    </div>

    <div class="admin-content-container">
      <!-- 侧边栏 -->
      <div class="admin-sidebar-section">
        <div class="modern-sidebar">
          <div class="sidebar-header">
            <div class="sidebar-title">
              <i class="el-icon-s-management sidebar-title-icon"></i>
              <h3 class="sidebar-title-text">管理菜单</h3>
            </div>
            <div class="sidebar-subtitle">系统功能导航</div>
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
              <i class="el-icon-info info-icon"></i>
              <span class="info-text">医院管理系统 v2.0</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 主内容区域 -->
      <div class="admin-main-section">
        <div class="main-content-card">
          <router-view>
            <div class="welcome-content">
              <div class="welcome-icon">
                <i class="el-icon-s-home"></i>
              </div>
              <h2 class="welcome-title">欢迎使用医院管理系统</h2>
              <p class="welcome-subtitle">请从左侧菜单选择您要管理的功能模块</p>
              <div class="welcome-stats">
                <div class="stat-item">
                  <div class="stat-icon doctors">
                    <i class="el-icon-user"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">医生数量</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon patients">
                    <i class="el-icon-user-solid"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">患者数量</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon orders">
                    <i class="el-icon-tickets"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">今日挂号</div>
                  </div>
                </div>
                <div class="stat-item">
                  <div class="stat-icon beds">
                    <i class="el-icon-office-building"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-number">0</div>
                    <div class="stat-label">病床使用</div>
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
  name: "Admin",
  data() {
    return {
      userName: "",
      activePath: "",
      menuItems: [
        {
          name: "首页",
          path: "adminLayout",
          icon: "el-icon-s-home",
          desc: "系统概览与数据统计"
        },
        {
          name: "医生信息管理",
          path: "doctorList",
          icon: "el-icon-user",
          desc: "医生档案与信息维护"
        },
        {
          name: "患者信息管理",
          path: "patientList",
          icon: "el-icon-user-solid",
          desc: "患者档案与信息管理"
        },
        {
          name: "挂号信息管理",
          path: "orderList",
          icon: "el-icon-postcard",
          desc: "挂号记录与状态管理"
        },
        {
          name: "药物信息管理",
          path: "drugList",
          icon: "el-icon-first-aid-kit",
          desc: "药品库存与信息管理"
        },
        {
          name: "检查项目管理",
          path: "checkList",
          icon: "el-icon-monitor",
          desc: "检查项目与结果管理"
        },
        {
          name: "病床信息管理",
          path: "bedList",
          icon: "el-icon-office-building",
          desc: "病床分配与使用管理"
        },
        {
          name: "排班信息管理",
          path: "arrangeIndex",
          icon: "el-icon-news",
          desc: "医生排班与调度管理"
        },
        {
          name: "数据统计分析",
          path: "dataExpore",
          icon: "el-icon-s-data",
          desc: "数据报表与统计分析"
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
  },
  created() {
    this.activePath = getActivePath() || "adminLayout";
    this.userName = this.tokenDecode(getToken()).aName || "管理员";
  }
};
</script>

<style lang="scss" scoped>
.admin-layout-management {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
}

/* 顶部标题区域 */
.admin-header-section {
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

.admin-title-content {
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

.admin-page-title {
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 0 0 2px 0;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.admin-page-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
  font-weight: 300;
  letter-spacing: 0.5px;
}

/* 用户信息区域 */
.admin-user-section {
  display: flex;
  align-items: center;
}

.user-info-card {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 8px 15px 8px 8px;
  border: 1px solid rgba(255, 255, 255, 0.15);
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
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.25);
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
  border: 2px solid rgba(255, 255, 255, 0.3);
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
  border: 2px solid #1a2b3c;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-welcome {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.7);
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
  color: rgba(255, 255, 255, 0.8);
  background: rgba(64, 158, 255, 0.3);
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
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.8);
}

.dropdown-trigger:hover {
  background: rgba(255, 255, 255, 0.2);
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
.admin-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
  pointer-events: none;
}

.admin-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.admin-circle-1 {
  width: 120px;
  height: 120px;
  background: #409eff;
  top: -60px;
  right: -30px;
}

.admin-circle-2 {
  width: 80px;
  height: 80px;
  background: #67c23a;
  top: -20px;
  right: 60px;
}

.admin-circle-3 {
  width: 60px;
  height: 60px;
  background: #e6a23c;
  top: 20px;
  right: 30px;
}

/* 主内容布局 */
.admin-content-container {
  display: flex;
  flex: 1;
  min-height: calc(100vh - 80px);
}

/* 侧边栏样式 */
.admin-sidebar-section {
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
  flex-shrink: 0;
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.sidebar-title-icon {
  font-size: 20px;
  color: #409eff;
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
  max-height: calc(100vh - 200px);
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
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.menu-item.active {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
  border-color: #409eff;
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
  background: rgba(64, 158, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  transition: all 0.3s ease;
}

.menu-item:hover .menu-icon-wrapper {
  background: rgba(64, 158, 255, 0.2);
  transform: scale(1.1);
}

.menu-item.active .menu-icon-wrapper {
  background: rgba(255, 255, 255, 0.2);
}

.menu-icon-wrapper i {
  font-size: 18px;
  color: #409eff;
  transition: all 0.3s ease;
}

.menu-item:hover .menu-icon-wrapper i {
  color: #409eff;
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
  color: #409eff;
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
  flex-shrink: 0;
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
.admin-main-section {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  overflow: hidden;
  min-width: 0;
}

.main-content-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow-y: auto;
  overflow-x: hidden;
  min-height: calc(100vh - 120px);
  max-height: calc(100vh - 120px);
  position: relative;
}

/* 美化滚动条样式 */
.sidebar-menu::-webkit-scrollbar,
.main-content-card::-webkit-scrollbar {
  width: 6px;
}

.sidebar-menu::-webkit-scrollbar-track,
.main-content-card::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.sidebar-menu::-webkit-scrollbar-thumb,
.main-content-card::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
  transition: background 0.3s ease;
}

.sidebar-menu::-webkit-scrollbar-thumb:hover,
.main-content-card::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Firefox滚动条样式 */
.sidebar-menu,
.main-content-card {
  scrollbar-width: thin;
  scrollbar-color: #c1c1c1 #f1f1f1;
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
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  font-size: 36px;
  color: white;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
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

.stat-icon.doctors {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.stat-icon.patients {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-icon.orders {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.stat-icon.beds {
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
  .admin-sidebar-section {
    width: 250px;
  }

  .sidebar-menu {
    max-height: none;
    overflow: visible;
  }

  .welcome-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .admin-header-section {
    padding: 0 20px;
    height: 70px;
  }

  .logo-icon {
    width: 50px;
    height: 50px;
  }

  .admin-page-title {
    font-size: 20px;
  }

  .admin-page-subtitle {
    font-size: 11px;
  }

  .admin-sidebar-section {
    width: 220px;
  }

  .admin-main-section {
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
  .admin-header-section {
    padding: 0 15px;
    flex-direction: column;
    height: auto;
    padding: 10px 15px;
  }

  .admin-title-content {
    margin-bottom: 10px;
  }

  .admin-user-section {
    width: 100%;
    justify-content: center;
  }

  .admin-content-container {
    flex-direction: column;
  }

  .admin-sidebar-section {
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
    max-height: none;
    overflow: visible;
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

.admin-header-section {
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
.menu-item:nth-child(6) { animation-delay: 0.6s; }
.menu-item:nth-child(7) { animation-delay: 0.7s; }
.menu-item:nth-child(8) { animation-delay: 0.8s; }
.menu-item:nth-child(9) { animation-delay: 0.9s; }

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
