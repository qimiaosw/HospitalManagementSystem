<template>
    <div class="order-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-tickets title-icon"></i>
                <h1 class="page-title">挂号管理</h1>
                <p class="page-subtitle">管理系统所有挂号记录</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 搜索和统计区域 -->
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
                                <span class="stat-label">总挂号数：</span>
                                <span class="stat-value">{{ total }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">已完成：</span>
                                <span class="stat-value success">{{ completedOrders }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">待缴费：</span>
                                <span class="stat-value warning">{{ pendingPayment }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">待缴总费用：</span>
                                <span class="stat-value price">¥{{ totalAmount }}</span>
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
                    empty-text="暂无挂号记录"
                >
                    <el-table-column
                        prop="oId"
                        label="挂号单号"
                        width="100"
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
                        prop="pId"
                        label="患者ID"
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.pId }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="dId" 
                        label="医生ID" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.dId }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oStart"
                        label="挂号时间"
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
                        prop="oEnd"
                        label="结束时间"
                        width="180"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="time-display">
                                <i class="el-icon-time time-icon"></i>
                                <span class="time-text">{{ formatTime(scope.row.oEnd) }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oRecord"
                        label="病因"
                        width="200"
                        min-width="200"
                    >
                        <template slot-scope="scope">
                            <div class="reason-display">
                                <i class="el-icon-document reason-icon"></i>
                                <span class="reason-text">{{ scope.row.oRecord || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oDrug"
                        label="药物"
                        width="150"
                        min-width="150"
                    >
                        <template slot-scope="scope">
                            <div class="drug-display">
                                <i class="el-icon-medal drug-icon"></i>
                                <span class="drug-text">{{ scope.row.oDrug || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oCheck"
                        label="检查项目"
                        width="150"
                        min-width="150"
                    >
                        <template slot-scope="scope">
                            <div class="check-display">
                                <i class="el-icon-suitcase check-icon"></i>
                                <span class="check-text">{{ scope.row.oCheck || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        prop="oTotalPrice"
                        label="待缴费用"
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="price-display">
                                <span class="price-symbol">¥</span>
                                <span class="price-value">{{ scope.row.oTotalPrice || 0 }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="缴费状态" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="payment-status">
                                <el-tag
                                    v-if="scope.row.oPriceState === 1"
                                    type="success"
                                    class="status-tag paid-tag"
                                >
                                    <i class="el-icon-success"></i>
                                    已缴费
                                </el-tag>
                                <el-button
                                    v-if="scope.row.oPriceState === 0 && scope.row.oState === 1"
                                    type="warning"
                                    size="mini"
                                    @click="priceClick(scope.row.oId)"
                                    class="payment-btn"
                                >
                                    <i class="el-icon-money"></i>
                                    点击缴费
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="挂号状态" 
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
                            <el-button
                                type="danger"
                                size="mini"
                                @click="deleteDialog(scope.row.oId)"
                                class="delete-btn"
                            >
                                <i class="el-icon-delete"></i>
                                删除
                            </el-button>
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
                ></el-pagination>
            </div>

            <!-- 空状态提示 -->
            <div v-if="orderData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-tickets empty-icon"></i>
                <h3>暂无挂号记录</h3>
                <p>当前没有挂号记录</p>
            </div>
        </el-card>
    </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "OrderList",
    data() {
        return {
            loading: false,
            pageNumber: 1,
            size: 8,
            query: "",
            orderData: [],
            total: 0,
        };
    },
    computed: {
        // 计算统计信息
        completedOrders() {
            return this.orderData.filter(order => 
                order.oState === 1 && order.oPriceState === 1
            ).length;
        },
        pendingPayment() {
            return this.orderData.filter(order => 
                order.oPriceState === 0 && order.oState === 1
            ).length;
        },
        totalAmount() {
            return this.orderData.reduce((total, order) => {
                return total + (parseFloat(order.oTotalPrice) || 0);
            }, 0).toFixed(2);
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
            if (order.oState === 1 && order.oPriceState === 1) {
                return 'success';
            } else if (order.oState === 1 && order.oPriceState === 0) {
                return 'warning';
            } else {
                return 'info';
            }
        },
        
        // 获取状态图标
        getStatusIcon(order) {
            if (order.oState === 1 && order.oPriceState === 1) {
                return 'el-icon-success';
            } else if (order.oState === 1 && order.oPriceState === 0) {
                return 'el-icon-money';
            } else {
                return 'el-icon-time';
            }
        },
        
        // 获取状态文本
        getStatusText(order) {
            if (order.oState === 1 && order.oPriceState === 1) {
                return '已完成';
            } else if (order.oState === 1 && order.oPriceState === 0) {
                return '待缴费';
            } else {
                return '进行中';
            }
        },

        //删除挂号操作
        deleteOrder(id) {
            this.loading = true;
            request
                .get("admin/deleteOrder", {
                    params: {
                        oId: id,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.requestOrders();
                    this.$message.success("挂号记录删除成功！");
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("删除失败，请重试");
                });
        },
        
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将永久删除该挂号信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                confirmButtonClass: 'el-button--danger',
                title: '删除确认'
            })
                .then(() => {
                    this.deleteOrder(id);
                })
                .catch(() => {
                    this.$message.info("已取消删除");
                });
        },
        
        //点击缴费按钮
        priceClick(oId) {
            this.$confirm('确认要为此挂号记录缴费吗？', '缴费确认', {
                confirmButtonText: '确认缴费',
                cancelButtonText: '取消',
                type: 'warning',
                confirmButtonClass: 'el-button--warning'
            }).then(() => {
                this.loading = true;
                request
                    .get("order/updatePrice", {
                        params: {
                            oId: oId,
                        },
                    })
                    .then((res) => {
                        this.loading = false;
                        if (res.data.status !== 200) {
                            this.$message.error("缴费失败，请重试");
                            return;
                        }
                        this.$message.success("缴费成功！");
                        this.requestOrders();
                    })
                    .catch(() => {
                        this.loading = false;
                        this.$message.error("网络错误，缴费失败");
                    });
            }).catch(() => {
                this.$message.info("已取消缴费");
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
        
        // 加载订单列表
        requestOrders() {
            this.loading = true;
            request
                .get("admin/findAllOrders", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200) {
                        this.$message.error("数据加载失败");
                        return;
                    }
                    this.orderData = res.data.data.orders || [];
                    this.total = res.data.data.total || 0;
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("网络错误，数据加载失败");
                });
        },
    },
    created() {
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
    color: #409eff;
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
    background: #409eff;
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
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
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
    min-width: 100px;
}

.stat-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 5px;
}

.stat-value {
    font-size: 16px;
    font-weight: 600;
    color: #409eff;
}

.stat-value.success {
    color: #67c23a;
}

.stat-value.warning {
    color: #e6a23c;
}

.stat-value.price {
    color: #f56c6c;
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

.reason-display, .drug-display, .check-display {
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

.drug-icon {
    font-size: 16px;
    color: #67c23a;
    margin-top: 2px;
    flex-shrink: 0;
}

.check-icon {
    font-size: 16px;
    color: #409eff;
    margin-top: 2px;
    flex-shrink: 0;
}

.reason-text, .drug-text, .check-text {
    line-height: 1.5;
    color: #606266;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.price-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 2px;
}

.price-symbol {
    font-size: 14px;
    color: #f56c6c;
    font-weight: 600;
}

.price-value {
    font-size: 16px;
    font-weight: 700;
    color: #f56c6c;
}

.payment-status {
    display: flex;
    justify-content: center;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

.paid-tag {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    color: white;
}

.payment-btn {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
    border: none;
    color: white;
    font-weight: 500;
    padding: 8px 12px;
}

.payment-btn:hover:not(.is-disabled) {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

.delete-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
    font-weight: 500;
    padding: 8px 12px;
}

.delete-btn:hover:not(.is-disabled) {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
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
        background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
        color: white;
    }
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

/* 响应式设计 */
@media (max-width: 1200px) {
    .order-management {
        padding: 15px;
    }
    
    .stats-info {
        gap: 15px;
    }
    
    .stat-item {
        min-width: 80px;
        padding: 8px 12px;
    }
    
    .reason-text, .drug-text, .check-text {
        max-width: 150px;
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
    
    .toolbar-section {
        flex-direction: column;
        gap: 15px;
    }
    
    .stats-info {
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
    }
    
    .stat-item {
        min-width: 70px;
        padding: 6px 10px;
    }
    
    .stat-label {
        font-size: 11px;
    }
    
    .stat-value {
        font-size: 14px;
    }
    
    .data-table {
        font-size: 12px;
    }
    
    .reason-text, .drug-text, .check-text {
        max-width: 120px;
    }
    
    .payment-btn, .delete-btn {
        padding: 6px 8px;
        font-size: 11px;
    }
}

@media (max-width: 480px) {
    .management-card {
        margin: 0 -10px;
    }
    
    .table-section {
        padding: 0 10px;
    }
    
    .data-table {
        font-size: 11px;
    }
    
    .reason-text, .drug-text, .check-text {
        max-width: 100px;
    }
    
    .payment-btn, .delete-btn {
        padding: 4px 6px;
        font-size: 10px;
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

.data-table {
    animation: fadeInUp 0.5s ease forwards;
}

.stat-item {
    animation: fadeInUp 0.3s ease forwards;
}

.stat-item:nth-child(1) { animation-delay: 0.1s; }
.stat-item:nth-child(2) { animation-delay: 0.2s; }
.stat-item:nth-child(3) { animation-delay: 0.3s; }
.stat-item:nth-child(4) { animation-delay: 0.4s; }
</style>