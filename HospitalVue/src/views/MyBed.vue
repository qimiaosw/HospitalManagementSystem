<template>
    <div class="my-bed-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-office-building title-icon"></i>
                <h1 class="page-title">我的住院信息</h1>
                <p class="page-subtitle">查看您的住院记录和床位信息</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <!-- 主要内容区域 -->
        <el-card class="management-card" shadow="hover">
            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="bedData" 
                    stripe 
                    border 
                    class="data-table"
                    v-loading="loading"
                    empty-text="暂无住院记录"
                >
                    <el-table-column 
                        label="床号" 
                        prop="bId" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="bed-number-display">
                                <i class="el-icon-office-building bed-icon"></i>
                                <span class="bed-id">{{ scope.row.bId }}号床</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="患者ID" 
                        prop="pId" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.pId }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="医生ID" 
                        prop="dId" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.dId }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="住院原因" 
                        prop="bReason"
                        min-width="200"
                    >
                        <template slot-scope="scope">
                            <div class="reason-display">
                                <i class="el-icon-document reason-icon"></i>
                                <span class="reason-text">{{ scope.row.bReason || '暂无原因说明' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="入院时间" 
                        prop="bStart" 
                        width="180"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="time-display">
                                <i class="el-icon-time time-icon"></i>
                                <span class="time-text">{{ formatTime(scope.row.bStart) }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="状态" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag 
                                type="success" 
                                class="status-tag"
                            >
                                <i class="el-icon-success"></i>
                                住院中
                            </el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 空状态提示 -->
            <div v-if="bedData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-office-building empty-icon"></i>
                <h3>暂无住院记录</h3>
                <p>您当前没有住院记录，如需住院请联系医生申请</p>
            </div>

            <!-- 统计信息 -->
            <div v-if="bedData.length > 0" class="stats-section">
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="stat-card">
                            <div class="stat-icon current-bed">
                                <i class="el-icon-office-building"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-value">{{ bedData.length }}</div>
                                <div class="stat-label">当前床位</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card">
                            <div class="stat-icon days-in">
                                <i class="el-icon-date"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-value">{{ calculateDaysInHospital() }}</div>
                                <div class="stat-label">住院天数</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card">
                            <div class="stat-icon doctor">
                                <i class="el-icon-user-solid"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-value">{{ getUniqueDoctors().length }}</div>
                                <div class="stat-label">负责医生</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card">
                            <div class="stat-icon latest">
                                <i class="el-icon-alarm-clock"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-value">{{ getLatestAdmission() }}</div>
                                <div class="stat-label">最近入院</div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </el-card>
    </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import request from "@/utils/request.js";
import { getToken } from "@/utils/storage.js";

export default {
    name: "MyBed",
    data() {
        return {
            bedData: [],
            userId: 1,
            loading: false
        };
    },
    methods: {
        // 格式化时间显示
        formatTime(timeString) {
            if (!timeString) return '--';
            return timeString.replace('T', ' ').substring(0, 16);
        },
        
        // 计算住院天数
        calculateDaysInHospital() {
            if (this.bedData.length === 0) return 0;
            
            const now = new Date();
            const admissionTime = new Date(this.bedData[0].bStart);
            const diffTime = Math.abs(now - admissionTime);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            
            return diffDays;
        },
        
        // 获取唯一的医生列表
        getUniqueDoctors() {
            const doctors = this.bedData.map(bed => bed.dId);
            return [...new Set(doctors)];
        },
        
        // 获取最近入院时间
        getLatestAdmission() {
            if (this.bedData.length === 0) return '--';
            
            const latest = this.bedData.reduce((latest, current) => {
                return new Date(current.bStart) > new Date(latest.bStart) ? current : latest;
            });
            
            return this.formatTime(latest.bStart).split(' ')[0];
        },
        
        // 请求病床信息
        requestBed() {
            this.loading = true;
            request.get("bed/findBedByPid", {
                params: {
                    pId: this.userId
                }
            })
            .then(res => {
                this.loading = false;
                if (res.data.status !== 200) {
                    this.$message.error("请求数据失败");
                    return;
                }
                this.bedData = res.data.data || [];
            })
            .catch(error => {
                this.loading = false;
                this.$message.error("网络错误，数据加载失败");
                console.error(error);
            });
        },
        
        // token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        // 解码token
        this.userId = this.tokenDecode(getToken()).pId;
        this.requestBed();
    }
};
</script>

<style lang="scss" scoped>
.my-bed-management {
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

/* 表格样式 */
.table-section {
    padding: 20px;
}

.data-table {
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 20px;
}

.bed-number-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.bed-icon {
    font-size: 18px;
    color: #67c23a;
}

.bed-id {
    font-weight: 600;
    color: #67c23a;
}

.id-display {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #409eff;
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
}

.reason-display {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    padding: 8px 0;
}

.reason-icon {
    font-size: 16px;
    color: #e6a23c;
    margin-top: 2px;
    flex-shrink: 0;
}

.reason-text {
    line-height: 1.5;
    color: #606266;
}

.time-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.time-icon {
    color: #909399;
    font-size: 14px;
}

.time-text {
    color: #606266;
    font-size: 13px;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

/* 空状态样式 */
.empty-state {
    text-align: center;
    padding: 60px 20px;
    color: #909399;
}

.empty-icon {
    font-size: 64px;
    color: #dcdfe6;
    margin-bottom: 20px;
}

.empty-state h3 {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 10px 0;
    color: #606266;
}

.empty-state p {
    font-size: 14px;
    margin: 0;
    color: #909399;
}

/* 统计信息样式 */
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
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
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

.stat-icon.current-bed {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-icon.days-in {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.stat-icon.doctor {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.stat-icon.latest {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.stat-content {
    flex: 1;
}

.stat-value {
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
@media (max-width: 768px) {
    .my-bed-management {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .stats-section .el-col {
        margin-bottom: 15px;
    }
    
    .stat-card {
        flex-direction: column;
        text-align: center;
        padding: 15px;
    }
    
    .stat-icon {
        margin-right: 0;
        margin-bottom: 10px;
    }
    
    .reason-display {
        flex-direction: column;
        align-items: flex-start;
    }
    
    .reason-icon {
        align-self: flex-start;
    }
}
</style>