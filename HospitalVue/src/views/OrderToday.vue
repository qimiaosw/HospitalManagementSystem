<template>
    <div class="order-today-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-date title-icon"></i>
                <h1 class="page-title">今日挂号处理</h1>
                <p class="page-subtitle">今日日期：{{ today }} - 处理患者挂号信息</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 统计概览 -->
            <div class="stats-overview">
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="stat-card total-stat">
                            <div class="stat-icon">
                                <i class="el-icon-tickets"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-number">{{ totalOrders }}</div>
                                <div class="stat-label">总挂号数</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card pending-stat">
                            <div class="stat-icon">
                                <i class="el-icon-time"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-number">{{ pendingOrders }}</div>
                                <div class="stat-label">待处理</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card processing-stat">
                            <div class="stat-icon">
                                <i class="el-icon-loading"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-number">{{ processingOrders }}</div>
                                <div class="stat-label">处理中</div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="6">
                        <div class="stat-card completed-stat">
                            <div class="stat-icon">
                                <i class="el-icon-success"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-number">{{ completedOrders }}</div>
                                <div class="stat-label">已完成</div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="orderData" 
                    stripe 
                    border
                    class="data-table"
                    v-loading="loading"
                    empty-text="今日暂无挂号记录"
                >
                    <el-table-column
                        label="序号"
                        type="index"
                        width="60"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="index-display">
                                <span class="index-number">{{ scope.$index + 1 }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="挂号单号" 
                        prop="oId"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="order-id">
                                <i class="el-icon-tickets id-icon"></i>
                                <span class="id-text">{{ scope.row.oId }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="患者ID" 
                        prop="pId"
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.pId }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="患者姓名" 
                        prop="pName"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="patient-name">
                                <i class="el-icon-user patient-icon"></i>
                                <span>{{ scope.row.pName || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="医生姓名" 
                        prop="dName"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="doctor-name">
                                <i class="el-icon-user-solid doctor-icon"></i>
                                <span>{{ scope.row.dName || userName }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="挂号时间" 
                        prop="oStart"
                        width="180"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="time-display">
                                <i class="el-icon-time time-icon"></i>
                                <span class="time-text">{{ formatTime(scope.row.oStart) }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="当前状态" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getStatusType(scope.row)"
                                class="status-tag"
                            >
                                <i :class="getStatusIcon(scope.row)"></i>
                                {{ getStatusText(scope.row) }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="操作" 
                        width="120"
                        fixed="right"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="action-btns">
                                <el-button
                                    type="warning"
                                    size="small"
                                    @click="dealClick(scope.row.oId, scope.row.pId)"
                                    class="action-btn process-btn"
                                    :loading="scope.row.processing"
                                >
                                    <i class="el-icon-monitor"></i>
                                    处理
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 空状态提示 -->
            <div v-if="orderData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-date empty-icon"></i>
                <h3>今日暂无挂号记录</h3>
                <p>当前没有需要处理的挂号信息</p>
            </div>

            <!-- 医生信息提示 -->
            <div class="doctor-info">
                <el-alert
                    title="医生工作台"
                    :description="`当前登录医生：${userName} (工号：${userId}) | 今日日期：${today}`"
                    type="info"
                    :closable="false"
                    show-icon
                    class="info-alert"
                >
                </el-alert>
            </div>
        </el-card>
    </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
    name: "orderToday",
    data() {
        return {
            loading: false,
            userId: 1,
            userName: "dada",
            today: "",
            orderData: [],
        };
    },
    computed: {
        // 计算统计信息
        totalOrders() {
            return this.orderData.length;
        },
        pendingOrders() {
            return this.orderData.filter(order => this.getStatusType(order) === 'warning').length;
        },
        processingOrders() {
            return this.orderData.filter(order => this.getStatusType(order) === 'primary').length;
        },
        completedOrders() {
            return this.orderData.filter(order => this.getStatusType(order) === 'success').length;
        }
    },
    methods: {
        // 格式化时间显示
        formatTime(timeString) {
            if (!timeString) return '--';
            return timeString.replace('T', ' ').substring(0, 16);
        },
        
        // 获取状态类型
        getStatusType(order) {
            // 根据实际业务逻辑判断状态
            if (order.oState === 1) {
                return 'success'; // 已完成
            } else if (order.oState === 0) {
                return 'warning'; // 待处理
            } else {
                return 'primary'; // 处理中
            }
        },
        
        // 获取状态图标
        getStatusIcon(order) {
            if (this.getStatusType(order) === 'success') {
                return 'el-icon-success';
            } else if (this.getStatusType(order) === 'warning') {
                return 'el-icon-time';
            } else {
                return 'el-icon-loading';
            }
        },
        
        // 获取状态文本
        getStatusText(order) {
            if (this.getStatusType(order) === 'success') {
                return '已完成';
            } else if (this.getStatusType(order) === 'warning') {
                return '待处理';
            } else {
                return '处理中';
            }
        },

        //挂号处理//页面跳转传值
        dealClick(oId, pId) {
            this.$confirm('确定要处理该挂号记录吗？', '处理确认', {
                confirmButtonText: '确认处理',
                cancelButtonText: '取消',
                type: 'warning',
                confirmButtonClass: 'el-button--warning'
            }).then(() => {
                // 标记为处理中状态
                const order = this.orderData.find(item => item.oId === oId);
                if (order) {
                    order.processing = true;
                }
                
                setTimeout(() => {
                    this.$router.push({
                        path: "/dealOrder",
                        query: {
                            oId: oId,
                            pId: pId
                        }
                    });
                }, 500);
                
            }).catch(() => {
                this.$message.info('已取消处理');
            });
        },
        
        //获取挂号信息
        requestOrder() {
            this.loading = true;
            request.get("doctor/findOrderByNull", {
                params: {
                    dId: this.userId,
                    oStart: this.today
                }
            })
            .then(res => {
                this.loading = false;
                if (res.data.status != 200) {
                    this.$message.error("获取数据失败");
                    return;
                }
                this.orderData = res.data.data || [];
                this.$message.success(`已加载 ${this.orderData.length} 条挂号记录`);
            })
            .catch(() => {
                this.loading = false;
                this.$message.error("网络错误，数据加载失败");
            });
        },
        
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        
        //获取当天日期
        nowDay() {
            const nowDate = new Date();
            let date = {
                year: nowDate.getFullYear(),
                month: nowDate.getMonth() + 1,
                date: nowDate.getDate(),
            };
            if (date.date < 10) {
                date.date = "0" + date.date;
            }
            if (date.month < 10) {
                date.month = "0" + date.month;
            }
            this.today = date.year + "-" + date.month + "-" + date.date;
        },
    },
    created() {
        //解码token信息
        this.userId = this.tokenDecode(getToken()).dId;
        this.userName = this.tokenDecode(getToken()).dName;
        console.log("医生ID:", this.userId);
        console.log("医生姓名:", this.userName);
        //获取当天日期
        this.nowDay();
        //获取订单信息
        this.requestOrder();
    },
};
</script>

<style lang="scss" scoped>
.order-today-management {
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
    color: #e6a23c;
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
    background: #e6a23c;
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
    background: #67c23a;
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

/* 统计概览 */
.stats-overview {
    padding: 20px;
    background: #f8fafc;
    border-bottom: 1px solid #e6e8eb;
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
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.pending-stat .stat-icon {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.processing-stat .stat-icon {
    background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
}

.completed-stat .stat-icon {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
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

/* 表格样式 */
.table-section {
    padding: 20px;
}

.data-table {
    border-radius: 8px;
    overflow: hidden;
    margin: 20px 0;
}

.index-display {
    display: flex;
    align-items: center;
    justify-content: center;
}

.index-number {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    font-size: 14px;
}

.order-id {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.id-icon {
    font-size: 16px;
    color: #409eff;
}

.id-text {
    font-weight: 600;
    color: #409eff;
}

.id-display {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #606266;
    background: #f5f7fa;
    padding: 4px 8px;
    border-radius: 4px;
}

.patient-name, .doctor-name {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.patient-icon {
    font-size: 16px;
    color: #67c23a;
}

.doctor-icon {
    font-size: 16px;
    color: #409eff;
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

.action-btns {
    display: flex;
    justify-content: center;
}

.action-btn {
    border-radius: 6px;
    font-weight: 500;
    padding: 8px 12px;
    font-size: 12px;
    transition: all 0.3s ease;
}

.action-btn:hover:not(.is-disabled) {
    transform: translateY(-1px);
}

.process-btn {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
    border: none;
    color: white;
}

.process-btn:hover:not(.is-disabled) {
    box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

/* 空状态提示 */
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
    color: #606266;
    margin: 0 0 10px 0;
}

.empty-state p {
    font-size: 14px;
    margin: 0;
    color: #909399;
}

/* 医生信息提示 */
.doctor-info {
    padding: 0 20px 20px;
}

.info-alert {
    border-radius: 8px;
    border: 1px solid #ebeef5;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .order-today-management {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .stats-overview {
        padding: 15px;
    }
    
    .stat-card {
        flex-direction: column;
        text-align: center;
        padding: 15px;
        height: auto;
        margin-bottom: 15px;
    }
    
    .stat-icon {
        margin-right: 0;
        margin-bottom: 10px;
        width: 50px;
        height: 50px;
        font-size: 24px;
    }
    
    .table-section {
        padding: 15px;
    }
    
    .data-table {
        font-size: 12px;
    }
    
    .action-btn {
        padding: 6px 8px;
        font-size: 11px;
    }
}

@media (max-width: 480px) {
    .stat-card {
        padding: 12px;
    }
    
    .stat-number {
        font-size: 20px;
    }
    
    .stat-label {
        font-size: 12px;
    }
    
    .data-table {
        font-size: 11px;
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

.data-table {
    animation: fadeInUp 0.6s ease forwards;
}
</style>