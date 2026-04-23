<template>
    <div class="doctor-card-container">
        <!-- 顶部标题栏 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-user-solid title-icon"></i>
                <h1 class="page-title">个人信息中心</h1>
                <p class="page-subtitle">查看和管理您的个人信息</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <!-- 医生信息卡片 -->
        <div class="card-layout">
            <el-card class="doctor-card" shadow="hover">
                <div class="card-header">
                    <div class="avatar-section">
                        <div class="avatar-wrapper">
                            <i class="el-icon-user-solid avatar-icon"></i>
                        </div>
                        <div class="basic-info">
                            <h2 class="doctor-name">{{ doctorData.dName || '医生姓名' }}</h2>
                            <p class="doctor-id">ID: {{ doctorData.dId || '--' }}</p>
                        </div>
                    </div>
                    <div class="status-badge">
                        <span class="badge online">在线</span>
                    </div>
                </div>

                <div class="card-body">
                    <div class="info-grid">
                        <!-- 第一行信息 -->
                        <div class="info-item">
                            <div class="info-icon gender-icon">
                                <i class="el-icon-male" v-if="doctorData.dGender === '男'"></i>
                                <i class="el-icon-female" v-else></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">性别</label>
                                <p class="info-value">{{ doctorData.dGender || '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-phone"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">手机号</label>
                                <p class="info-value">{{ doctorData.dPhone || '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-postcard"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">身份证号</label>
                                <p class="info-value">{{ doctorData.dCard || '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-message"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">邮箱</label>
                                <p class="info-value">{{ doctorData.dEmail || '--' }}</p>
                            </div>
                        </div>

                        <!-- 第二行信息 -->
                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-suitcase"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">职位</label>
                                <p class="info-value">{{ doctorData.dPost || '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-office-building"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">所属科室</label>
                                <p class="info-value">{{ doctorData.dSection || '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-coin"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">挂号价格</label>
                                <p class="info-value">{{ doctorData.dPrice ? '¥' + doctorData.dPrice : '--' }}</p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-icon">
                                <i class="el-icon-star-off"></i>
                            </div>
                            <div class="info-content">
                                <label class="info-label">评分</label>
                                <div class="rating-display">
                                    <span class="rating-value">{{ doctorData.dAvgStar || '--' }}</span>
                                    <div class="stars">
                                        <i v-for="n in 5" :key="n" 
                                           class="el-icon-star-on star" 
                                           :class="{ 'active': n <= Math.round(doctorData.dAvgStar || 0) }">
                                        </i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 医生简介 -->
                    <div class="introduction-section">
                        <div class="section-header">
                            <i class="el-icon-notebook-2"></i>
                            <h3>医生简介</h3>
                        </div>
                        <div class="introduction-content">
                            <p>{{ doctorData.dIntroduction || '暂无简介信息' }}</p>
                        </div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
    name: "DoctorCard",
    data() {
        return {
            userId: "",
            doctorData: {},
        };
    },
    methods: {
        //请求医生信息
        requestDoctor() {
            request
                .get("admin/findDoctor", {
                    params: {
                        dId: this.userId,
                    },
                })
                .then((res) => {
                    if (res.data.status != 200)
                        return this.$message.error("获取数据失败");
                    this.doctorData = res.data.data;
                });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        //解码token信息
        this.userId = this.tokenDecode(getToken()).dId;
        this.requestDoctor();
        console.log(this.userId);
    },
};
</script>

<style lang="scss" scoped>
.doctor-card-container {
    width: 100%;
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

/* 卡片布局 */
.card-layout {
    max-width: 1000px;
    margin: 0 auto;
}

.doctor-card {
    border-radius: 16px;
    border: none;
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.doctor-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 30px rgba(66, 124, 179, 0.15);
}

/* 卡片头部 */
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30px;
    background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
    color: white;
    border-radius: 16px 16px 0 0;
}

.avatar-section {
    display: flex;
    align-items: center;
}

.avatar-wrapper {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20px;
}

.avatar-icon {
    font-size: 40px;
    color: white;
}

.basic-info {
    display: flex;
    flex-direction: column;
}

.doctor-name {
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 5px 0;
}

.doctor-id {
    font-size: 14px;
    opacity: 0.8;
    margin: 0;
}

.status-badge .badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
}

.badge.online {
    background: rgba(76, 175, 80, 0.2);
    color: #4caf50;
    border: 1px solid rgba(76, 175, 80, 0.3);
}

/* 卡片主体 */
.card-body {
    padding: 30px;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
}

.info-item {
    display: flex;
    align-items: center;
    padding: 15px;
    background: #f8fafc;
    border-radius: 10px;
    transition: all 0.3s ease;
}

.info-item:hover {
    background: #f0f7ff;
    transform: translateY(-2px);
}

.info-icon {
    width: 40px;
    height: 40px;
    border-radius: 10px;
    background: #e6f3ff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 15px;
    color: #427cb3;
    font-size: 18px;
}

.info-icon.gender-icon {
    background: #fff0f0;
    color: #f56c6c;
}

.info-content {
    flex: 1;
}

.info-label {
    font-size: 12px;
    color: #909399;
    margin: 0 0 5px 0;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.info-value {
    font-size: 16px;
    font-weight: 500;
    color: #1f2d3d;
    margin: 0;
}

.rating-display {
    display: flex;
    align-items: center;
}

.rating-value {
    font-size: 16px;
    font-weight: 600;
    color: #e6a23c;
    margin-right: 10px;
}

.stars {
    display: flex;
}

.star {
    font-size: 16px;
    color: #dcdfe6;
    margin-right: 2px;
}

.star.active {
    color: #e6a23c;
}

/* 简介区域 */
.introduction-section {
    border-top: 1px solid #e6e8eb;
    padding-top: 20px;
}

.section-header {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.section-header i {
    font-size: 20px;
    color: #427cb3;
    margin-right: 10px;
}

.section-header h3 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2d3d;
    margin: 0;
}

.introduction-content {
    background: #f8fafc;
    padding: 20px;
    border-radius: 10px;
    line-height: 1.6;
}

.introduction-content p {
    margin: 0;
    color: #5a6376;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .doctor-card-container {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .decoration-element {
        position: relative;
        margin-top: 20px;
    }
    
    .card-header {
        flex-direction: column;
        text-align: center;
        padding: 20px;
    }
    
    .avatar-section {
        flex-direction: column;
        margin-bottom: 15px;
    }
    
    .avatar-wrapper {
        margin-right: 0;
        margin-bottom: 15px;
    }
    
    .info-grid {
        grid-template-columns: 1fr;
    }
}
</style>