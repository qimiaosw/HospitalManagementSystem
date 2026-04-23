<template>
  <div class="patient-layout">
    <!-- 顶部标题区域 -->
    <div class="layout-header-section">
      <div class="layout-title-content">
        <i class="el-icon-user-solid layout-title-icon"></i>
        <h1 class="layout-page-title">患者服务系统</h1>
        <p class="layout-page-subtitle">欢迎使用医院患者服务系统</p>
      </div>
      <div class="layout-decoration-element">
        <div class="layout-circle layout-circle-1"></div>
        <div class="layout-circle layout-circle-2"></div>
        <div class="layout-circle layout-circle-3"></div>
      </div>
    </div>

    <div class="layout-container">
      <!-- 侧边栏 -->
      <div class="layout-sidebar">
        <div class="sidebar-content">
          <!-- 用户信息卡片 -->
          <div class="user-profile-card">
            <div class="user-avatar-section">
              <div class="avatar-container">
                <img src="../assets/yonghu.jpg" alt="患者头像" class="user-avatar-img" />
                <div class="online-indicator"></div>
              </div>
              <div class="user-info-section">
                <h3 class="user-name-text">{{ userName }}</h3>
                <p class="user-role-text">患者</p>
              </div>
            </div>
          </div>

          <!-- 导航菜单 -->
          <div class="navigation-menu">
            <div
                v-for="item in menuItems"
                :key="item.path"
                :class="['nav-menu-item', activePath === item.path ? 'nav-active' : '']"
                @click="menuClick(item.path)"
            >
              <div class="nav-icon-container">
                <i :class="item.icon"></i>
              </div>
              <div class="nav-text-container">
                <span class="nav-primary-text">{{ item.name }}</span>
                <span class="nav-secondary-text">{{ item.desc }}</span>
              </div>
              <div class="nav-arrow-icon">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>

          <!-- 退出按钮 -->
          <div class="sidebar-footer-section">
            <button class="logout-button" @click="handleLogout">
              <i class="el-icon-switch-button"></i>
              <span>安全退出</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 主内容区域 -->
      <div class="layout-main-content">
        <div class="content-wrapper">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken, clearToken, getActivePath, setActivePath } from "@/utils/storage.js";

export default {
  name: "Patient",
  data() {
    return {
      userName: "",
      activePath: "",
      showWelcome: true,
      menuItems: [
        {
          name: "首页",
          path: "patientLayout",
          icon: "el-icon-s-home",
          desc: "系统概览与快速入口"
        },
        {
          name: "AI导诊",
          path: "aiDiagnosis",
          desc: "智能分诊与科室推荐",
          icon: "el-icon-cpu"
        },
        {
          name: "预约挂号",
          path: "orderOperate",
          icon: "el-icon-date",
          desc: "在线预约医生门诊"
        },
        {
          name: "我的挂号",
          path: "myOrder",
          icon: "el-icon-postcard",
          desc: "查看挂号记录与状态"
        },
        {
          name: "住院信息",
          path: "myBed",
          icon: "el-icon-news",
          desc: "住院详情与费用查询"
        },
        {
          path: "patientCard",
          name: "个人信息",
          desc: "管理个人资料",
          icon: "el-icon-user"
        },

      ]
    };
  },

  watch: {
    '$route'(to, from) {
      this.updateActivePath(to.path);
      this.showWelcome = to.path === '/patientLayout' || to.path === '/patient';
      this.refreshUserInfo();
    }
  },

  methods: {
    refreshUserInfo(){
      try {
        const tokenData = this.tokenDecode(getToken());
        if (tokenData && tokenData.pId) {
          this.userName = tokenData.pName;
          this.userId = tokenData.pId;
          console.log('刷新用户信息成功：', tokenData)
        }
      }
      catch(e){
        console.log('刷新用户信息失败：', e)
      }
    },


    menuClick(path) {
      this.activePath = path;
      setActivePath(path);
      if (this.$route.path !== "/" + path) {
        this.$router.push(path);
      }
      this.showWelcome = path === 'patientLayout';
    },

    updateActivePath(path) {
      this.activePath = path.replace(/^\//, "");
      setActivePath(path);
    },

    // Token解码
    tokenDecode(token) {
      try {
        if (token) {
          return jwtDecode(token);
        }
      } catch (error) {
        console.error("Token解码失败:", error);
      }
      return {};
    },

    handleLogout() {
      this.$confirm("确定要退出登录吗？", "安全退出", {
        confirmButtonText: "确定退出",
        cancelButtonText: "取消",
        type: "warning",
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
    },

    // 处理用户信息更新事件
    handleUserInfoUpdate(userInfo) {
      if (userInfo && userInfo.pName) {
        this.userName = userInfo.pName;
        this.$message.success('用户信息已同步更新');
        console.log('侧边栏用户名已更新为:', userInfo.pName);
      }
    }
  },

  created() {
    this.activePath = getActivePath() || "patientLayout";
    this.refreshUserInfo();
    const tokenData = this.tokenDecode(getToken());
    this.userName = tokenData.pName || "患者";
    this.updateActivePath(this.$route.path);
    this.showWelcome = this.$route.path === '/patientLayout' || this.$route.path === '/patient';

    // 监听用户信息更新事件
    this.$bus.$on('userInfoUpdated', this.handleUserInfoUpdate);
  },

  beforeDestroy() {
    // 组件销毁前移除事件监听
    this.$bus.$off('userInfoUpdated', this.handleUserInfoUpdate);
  }
};
</script>

<style lang="scss" scoped>
.patient-layout {
  min-height: 100vh;  /* 改为 min-height */
  background: linear-gradient(135deg, #f8fafc 0%, #e6f0fa 100%);
  padding: 20px;
  overflow-x: hidden;  /* 只防止水平滚动 */
}

/* 顶部标题区域 */
.layout-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  position: relative;
  padding: 15px 0;
}

.layout-title-content {
  z-index: 2;
}

.layout-title-icon {
  font-size: 32px;
  color: #409eff;
  margin-bottom: 8px;
}

.layout-page-title {
  font-size: 26px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 4px 0;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.layout-page-subtitle {
  font-size: 14px;
  color: #5a6376;
  margin: 0;
}

.layout-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.layout-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.layout-circle-1 {
  width: 80px;
  height: 80px;
  background: #409eff;
  top: -20px;
  right: 0;
}

.layout-circle-2 {
  width: 60px;
  height: 60px;
  background: #67c23a;
  top: 10px;
  right: 70px;
}

.layout-circle-3 {
  width: 45px;
  height: 45px;
  background: #e6a23c;
  top: 35px;
  right: 35px;
}

/* 主布局容器 */
.layout-container {
  display: flex;
  gap: 20px;
  position: relative;
}

/* 侧边栏样式 */
.layout-sidebar {
  width: 280px;
  flex-shrink: 0;  /* 防止侧边栏收缩 */
}

.sidebar-content {
  background: white;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* 用户信息卡片 */
.user-profile-card {
  padding: 25px 20px 20px;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-radius: 16px 16px 0 0;
  flex-shrink: 0;  /* 防止用户信息被压缩 */
}

.user-avatar-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar-container {
  position: relative;
  width: 55px;
  height: 55px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.15);
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.online-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 11px;
  height: 11px;
  border-radius: 50%;
  background: #67c23a;
  border: 2px solid white;
}

.user-info-section {
  flex: 1;
}

.user-name-text {
  font-size: 16px;
  font-weight: 600;
  color: white;
  margin: 0 0 4px 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.user-role-text {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  background: rgba(255, 255, 255, 0.2);
  padding: 3px 10px;
  border-radius: 10px;
  display: inline-block;
}

/* 导航菜单 */
.navigation-menu {
  flex: 1;
  padding: 15px 0;
  overflow-y: auto;  /* 允许垂直滚动 */
  max-height: calc(100vh - 200px);  /* 限制最大高度 */
}

.nav-menu-item {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  margin: 0 12px 6px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  border: 1px solid transparent;
}

.nav-menu-item:hover {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
  border-color: #d9ecff;
  transform: translateX(3px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.12);
}

.nav-menu-item.nav-active {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  box-shadow: 0 3px 12px rgba(64, 158, 255, 0.25);
  border-color: #409eff;
}

.nav-menu-item.nav-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 25px;
  background: white;
  border-radius: 0 2px 2px 0;
}

.nav-icon-container {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: rgba(64, 158, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  transition: all 0.3s ease;
}

.nav-menu-item:hover .nav-icon-container {
  background: rgba(64, 158, 255, 0.15);
  transform: scale(1.05);
}

.nav-menu-item.nav-active .nav-icon-container {
  background: rgba(255, 255, 255, 0.2);
}

.nav-icon-container i {
  font-size: 18px;
  color: #409eff;
  transition: all 0.3s ease;
}

.nav-menu-item:hover .nav-icon-container i {
  color: #409eff;
  transform: scale(1.05);
}

.nav-menu-item.nav-active .nav-icon-container i {
  color: white;
}

.nav-text-container {
  flex: 1;
}

.nav-primary-text {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #1f2d3d;
  margin-bottom: 3px;
  transition: all 0.3s ease;
}

.nav-secondary-text {
  display: block;
  font-size: 11px;
  color: #909399;
  transition: all 0.3s ease;
}

.nav-menu-item:hover .nav-primary-text {
  color: #409eff;
}

.nav-menu-item:hover .nav-secondary-text {
  color: #606266;
}

.nav-menu-item.nav-active .nav-primary-text,
.nav-menu-item.nav-active .nav-secondary-text {
  color: white;
}

.nav-arrow-icon {
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.3s ease;
  color: #c0c4cc;
}

.nav-menu-item:hover .nav-arrow-icon {
  opacity: 1;
  transform: translateX(0);
}

.nav-menu-item.nav-active .nav-arrow-icon {
  opacity: 1;
  transform: translateX(0);
  color: white;
}

/* 侧边栏底部 */
.sidebar-footer-section {
  padding: 15px 20px;
  border-top: 1px solid #f0f2f5;
  flex-shrink: 0;  /* 防止底部被压缩 */
}

.logout-button {
  width: 100%;
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
  border: none;
  border-radius: 8px;
  padding: 11px;
  font-weight: 500;
  color: white;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 14px;
}

.logout-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 3px 10px rgba(245, 108, 108, 0.25);
}

/* 主内容区域 */
.layout-main-content {
  flex: 1;
  min-width: 0;  /* 防止 flex 子元素溢出 */
  overflow: hidden;
}

.content-wrapper {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  overflow-y: auto;  /* 允许垂直滚动 */
  overflow-x: hidden;
  min-height: calc(100vh - 200px);  /* 设置最小高度 */
  max-height: calc(100vh - 200px);  /* 设置最大高度 */
  position: relative;
}

/* 美化滚动条样式 */
.content-wrapper::-webkit-scrollbar,
.navigation-menu::-webkit-scrollbar {
  width: 6px;
}

.content-wrapper::-webkit-scrollbar-track,
.navigation-menu::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.content-wrapper::-webkit-scrollbar-thumb,
.navigation-menu::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
  transition: background 0.3s ease;
}

.content-wrapper::-webkit-scrollbar-thumb:hover,
.navigation-menu::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Firefox滚动条样式 */
.content-wrapper,
.navigation-menu {
  scrollbar-width: thin;
  scrollbar-color: #c1c1c1 #f1f1f1;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .patient-layout {
    padding: 15px;
  }

  .layout-container {
    flex-direction: column;
  }

  .layout-sidebar {
    width: 100%;
    margin-bottom: 15px;
  }

  .sidebar-content {
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
    gap: 15px;
  }

  .user-profile-card {
    border-radius: 10px;
    padding: 15px;
    flex-shrink: 0;
  }

  .navigation-menu {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    padding: 10px;
    max-height: none;
    overflow: visible;
  }

  .nav-menu-item {
    flex: 1;
    min-width: 140px;
    margin: 0;
    flex-direction: column;
    text-align: center;
    padding: 12px;
  }

  .nav-icon-container {
    margin-right: 0;
    margin-bottom: 6px;
  }

  .nav-arrow-icon {
    display: none;
  }

  .sidebar-footer-section {
    border-top: none;
    padding: 10px;
    flex-shrink: 0;
  }

  .logout-button {
    width: auto;
    padding: 8px 20px;
  }

  .content-wrapper {
    min-height: 400px;
    max-height: none;
  }
}

@media (max-width: 768px) {
  .patient-layout {
    padding: 12px;
  }

  .layout-header-section {
    flex-direction: column;
    text-align: center;
    margin-bottom: 15px;
    padding: 12px 0;
  }

  .layout-page-title {
    font-size: 22px;
  }

  .nav-menu-item {
    min-width: 100px;
    padding: 10px;
  }

  .nav-primary-text {
    font-size: 13px;
  }

  .nav-secondary-text {
    font-size: 10px;
  }
}

@media (max-width: 480px) {
  .patient-layout {
    padding: 10px;
  }

  .layout-page-title {
    font-size: 20px;
  }

  .sidebar-content {
    flex-direction: column;
  }

  .navigation-menu {
    flex-direction: column;
  }

  .nav-menu-item {
    min-width: 100%;
    flex-direction: row;
    text-align: left;
  }

  .nav-icon-container {
    margin-right: 10px;
    margin-bottom: 0;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(15px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.nav-menu-item {
  animation: fadeInUp 0.4s ease forwards;
}

.nav-menu-item:nth-child(1) { animation-delay: 0.1s; }
.nav-menu-item:nth-child(2) { animation-delay: 0.15s; }
.nav-menu-item:nth-child(3) { animation-delay: 0.2s; }
.nav-menu-item:nth-child(4) { animation-delay: 0.25s; }
.nav-menu-item:nth-child(5) { animation-delay: 0.3s; }
.nav-menu-item:nth-child(6) { animation-delay: 0.35s; }
</style>
