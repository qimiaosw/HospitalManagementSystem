<template>
    <div class="order-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-notebook-2 title-icon"></i>
                <h1 class="page-title">挂号信息管理</h1>
                <p class="page-subtitle">查看和管理患者挂号信息</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <!-- 主要内容区域 -->
        <el-card class="management-card" shadow="hover">
            <!-- 搜索栏 -->
            <div class="toolbar-section">
                <el-row :gutter="20" type="flex" align="middle">
                    <el-col :span="8">
                        <div class="search-box">
                            <el-input 
                                v-model="query" 
                                placeholder="请输入患者ID查询"
                                size="medium"
                                class="search-input"
                            >
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestOrders"
                                    class="search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                    <el-col :span="16">
                        <div class="stats-info">
                            <div class="stat-item">
                                <span class="stat-label">今日挂号数:</span>
                                <span class="stat-value">{{ todayCount }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">待处理:</span>
                                <span class="stat-value pending">{{ pendingCount }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">已完成:</span>
                                <span class="stat-value completed">{{ completedCount }}</span>
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
                    style="width: 100%" 
                    border
                    class="data-table"
                    v-loading="loading"
                >
                    <el-table-column
                        prop="oId"
                        label="挂号单号"
                        width="100"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column
                        prop="dId"
                        label="医生ID"
                        width="90"
                        align="center"
                    >
                    </el-table-column>

                    <el-table-column 
                        prop="pId" 
                        label="患者ID" 
                        width="100"
                        align="center"
                    >
                    </el-table-column>

                    <el-table-column
                        prop="oStart"
                        label="挂号时间"
                        width="160"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column
                        prop="oEnd"
                        label="结束时间"
                        width="160"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column
                        prop="oRecord"
                        label="病因"
                        width="200"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="cause-text">
                                {{ scope.row.oRecord || '暂无记录' }}
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oDrug"
                        label="药物"
                        width="150"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="drug-tags">
                                <el-tag 
                                    v-if="scope.row.oDrug" 
                                    size="mini" 
                                    type="info"
                                >
                                    {{ scope.row.oDrug }}
                                </el-tag>
                                <span v-else class="no-data">暂无</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oCheck"
                        label="检查项目"
                        width="150"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="check-tags">
                                <el-tag 
                                    v-if="scope.row.oCheck" 
                                    size="mini" 
                                    type="warning"
                                >
                                    {{ scope.row.oCheck }}
                                </el-tag>
                                <span v-else class="no-data">暂无</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oTotalPrice"
                        label="费用"
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="price-tag">¥{{ scope.row.oTotalPrice || 0 }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oPriceState"
                        label="缴费状态"
                        width="110"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="scope.row.oPriceState === 1 ? 'success' : 'danger'"
                                class="status-tag"
                            >
                                <i :class="scope.row.oPriceState === 1 ? 'el-icon-success' : 'el-icon-error'"></i>
                                {{ scope.row.oPriceState === 1 ? '已缴费' : '未缴费' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="oState" 
                        label="挂号状态" 
                        width="110"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getOrderStatusType(scope.row)"
                                class="status-tag"
                            >
                                {{ getOrderStatusText(scope.row) }}
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
                                    size="mini"
                                    @click="dealClick(scope.row.oId, scope.row.pId)"
                                    v-if="scope.row.oState === 1 && scope.row.oPriceState === 1"
                                    class="action-btn follow-up-btn"
                                >
                                    <i class="el-icon-refresh-right"></i>
                                    追诊
                                </el-button>
                                <span v-else class="no-action">-</span>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分页 -->
            <div class="pagination-section">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page="pageNumber"
                    :page-size="size"
                    :page-sizes="[8, 16, 24, 32]"
                    :total="total"
                    class="custom-pagination"
                >
                </el-pagination>
            </div>
        </el-card>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";

export default {
    name: "DoctorOrder",
    data() {
        return {
            userId: 1,
            orderData: [],
            pageNumber: 1,
            size: 8,
            query: "",
            total: 0,
            loading: false,
            todayCount: 0,
            pendingCount: 0,
            completedCount: 0
        };
    },
    methods: {
        // 获取订单状态类型
        getOrderStatusType(order) {
            if (order.oState === 1 && order.oPriceState === 1) {
                return 'success'; // 已完成
            } else if (order.oState === 0) {
                return 'warning'; // 进行中
            } else {
                return 'danger'; // 未完成
            }
        },
        
        // 获取订单状态文本
        getOrderStatusText(order) {
            if (order.oState === 1 && order.oPriceState === 1) {
                return '已完成';
            } else if (order.oState === 0) {
                return '进行中';
            } else {
                return '未完成';
            }
        },
        
        //点击追诊按钮
        dealClick(oId, pId) {
            this.$router.push({
                path: "/dealOrderAgain",
                query: {
                    oId: oId,
                    pId: pId,
                },
            });
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestOrders();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestOrders();
        },

        //请求挂号信息
        requestOrders() {
            this.loading = true;
            request
                .get("order/findOrderByDid", {
                    params: {
                        dId: this.userId,
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200) {
                        this.$message.error("请求数据失败");
                        return;
                    }
                    this.orderData = res.data.data.orders;
                    this.total = res.data.data.total;
                    
                    // 计算统计信息
                    this.calculateStats();
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("网络请求失败");
                });
        },
        
        // 计算统计信息
        calculateStats() {
            const today = new Date().toISOString().split('T')[0];
            this.todayCount = this.orderData.filter(order => 
                order.oStart && order.oStart.includes(today)
            ).length;
            
            this.pendingCount = this.orderData.filter(order => 
                order.oState === 0
            ).length;
            
            this.completedCount = this.orderData.filter(order => 
                order.oState === 1 && order.oPriceState === 1
            ).length;
        },
        
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        this.userId = this.tokenDecode(getToken()).dId;
        this.requestOrders();
    },
};
</script>

<style lang="scss" scoped>
.order-management {
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

/* 工具栏样式 */
.toolbar-section {
    padding: 20px;
    background: #f8fafc;
    border-bottom: 1px solid #e6e8eb;
}

.search-box {
    display: flex;
    align-items: center;
}

.search-input {
    border-radius: 8px;
    overflow: hidden;
}

.search-btn {
    background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
    border: none;
    color: white;
    font-weight: 500;
}

.stats-info {
    display: flex;
    justify-content: flex-end;
    gap: 30px;
}

.stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px 15px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 5px;
}

.stat-value {
    font-size: 18px;
    font-weight: 600;
    color: #427cb3;
}

.stat-value.pending {
    color: #e6a23c;
}

.stat-value.completed {
    color: #67c23a;
}

/* 表格样式 */
.table-section {
    padding: 0 20px;
}

.data-table {
    border-radius: 8px;
    overflow: hidden;
    margin: 20px 0;
}

.data-table::before {
    display: none;
}

.cause-text {
    max-width: 180px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: help;
}

.cause-text:hover {
    white-space: normal;
    overflow: visible;
    position: absolute;
    background: white;
    padding: 8px;
    border-radius: 4px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    z-index: 10;
    max-width: 300px;
}

.drug-tags, .check-tags {
    display: flex;
    justify-content: center;
}

.no-data {
    color: #c0c4cc;
    font-size: 12px;
}

.price-tag {
    font-weight: 600;
    color: #f56c6c;
    background: #fef0f0;
    padding: 4px 8px;
    border-radius: 4px;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 4px 8px;
}

.action-btns {
    display: flex;
    justify-content: center;
}

.follow-up-btn {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
    border: none;
    border-radius: 6px;
    color: white;
    font-size: 12px;
    padding: 6px 10px;
}

.follow-up-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

.no-action {
    color: #c0c4cc;
    font-size: 12px;
}

/* 分页样式 */
.pagination-section {
    display: flex;
    justify-content: center;
    padding: 20px;
    border-top: 1px solid #e6e8eb;
}

.custom-pagination {
    .el-pagination.is-background .el-pager li:not(.disabled).active {
        background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
        color: white;
    }
}

/* 响应式设计 */
@media (max-width: 1200px) {
    .table-section {
        overflow-x: auto;
    }
    
    .data-table {
        min-width: 1000px;
    }
}

@media (max-width: 768px) {
    .order-management {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .toolbar-section .el-col {
        margin-bottom: 15px;
    }
    
    .stats-info {
        justify-content: center;
        flex-wrap: wrap;
        gap: 15px;
    }
    
    .stat-item {
        min-width: 100px;
    }
}
</style>