<template>
    <div class="patient-list-management">
        <!-- 顶部标题区域 -->
        <div class="list-header-section">
            <div class="list-title-content">
                <i class="el-icon-user-solid list-title-icon"></i>
                <h1 class="list-page-title">患者信息管理</h1>
                <p class="list-page-subtitle">管理系统所有患者信息</p>
            </div>
            <div class="list-decoration-element">
                <div class="list-circle list-circle-1"></div>
                <div class="list-circle list-circle-2"></div>
                <div class="list-circle list-circle-3"></div>
            </div>
        </div>

        <el-card class="list-management-card" shadow="hover">
            <!-- 搜索和统计区域 -->
            <div class="list-toolbar-section">
                <el-row :gutter="20" type="flex" align="middle">
                    <el-col :span="8">
                        <div class="list-search-box">
                            <el-input 
                                v-model="query" 
                                placeholder="请输入患者姓名查询"
                                size="medium"
                                class="list-search-input"
                            >
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestPatients"
                                    class="list-search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                    
                    <el-col :span="16">
                        <div class="list-stats-info">
                            <div class="list-stat-item">
                                <span class="list-stat-label">总患者数：</span>
                                <span class="list-stat-value">{{ total }}</span>
                            </div>
                            <div class="list-stat-item">
                                <span class="list-stat-label">正常状态：</span>
                                <span class="list-stat-value success">{{ activePatients }}</span>
                            </div>
                            <div class="list-stat-item">
                                <span class="list-stat-label">已删除：</span>
                                <span class="list-stat-value danger">{{ deletedPatients }}</span>
                            </div>
                            <div class="list-stat-item">
                                <span class="list-stat-label">当前页：</span>
                                <span class="list-stat-value info">{{ currentPageInfo }}</span>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="list-table-section">
                <el-table 
                    :data="patientData" 
                    stripe 
                    border
                    class="list-data-table"
                    v-loading="loading"
                    empty-text="暂无患者数据"
                >
                    <el-table-column 
                        prop="pId" 
                        label="患者账号" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="list-id-display">
                                <i class="el-icon-user list-id-icon"></i>
                                <span class="list-id-text">{{ scope.row.pId || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pName" 
                        label="姓名" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="list-name-display">
                                <i class="el-icon-user-solid list-name-icon"></i>
                                <span class="list-name-text">{{ scope.row.pName || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pGender" 
                        label="性别" 
                        width="80"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getGenderType(scope.row.pGender)"
                                class="list-gender-tag"
                            >
                                <i :class="getGenderIcon(scope.row.pGender)"></i>
                                {{ scope.row.pGender || '--' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pAge" 
                        label="年龄" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="list-age-display">
                                <span class="list-age-number">{{ scope.row.pAge || '--' }}</span>
                                <span class="list-age-unit">岁</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pCard" 
                        label="证件号码" 
                        min-width="180"
                    >
                        <template slot-scope="scope">
                            <div class="list-card-display">
                                <i class="el-icon-credit-card list-card-icon"></i>
                                <span class="list-card-text">{{ scope.row.pCard || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pPhone" 
                        label="手机号码" 
                        width="140"
                    >
                        <template slot-scope="scope">
                            <div class="list-phone-display">
                                <i class="el-icon-phone list-phone-icon"></i>
                                <span class="list-phone-text">{{ scope.row.pPhone || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pEmail" 
                        label="邮箱地址" 
                        min-width="200"
                    >
                        <template slot-scope="scope">
                            <div class="list-email-display">
                                <i class="el-icon-message list-email-icon"></i>
                                <span class="list-email-text">{{ scope.row.pEmail || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="pState" 
                        label="账户状态" 
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getStatusType(scope.row.pState)"
                                class="list-status-tag"
                            >
                                <i :class="getStatusIcon(scope.row.pState)"></i>
                                {{ getStatusText(scope.row.pState) }}
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
                                @click="deleteDialog(scope.row.pId)"
                                class="list-delete-btn"
                                :disabled="scope.row.pState === 0"
                            >
                                <i class="el-icon-delete"></i>
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分页 -->
            <div class="list-pagination-section">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page="pageNumber"
                    :page-size="size"
                    :page-sizes="[8, 16, 24, 32]"
                    :total="total"
                    class="list-custom-pagination"
                ></el-pagination>
            </div>

            <!-- 空状态提示 -->
            <div v-if="patientData.length === 0 && !loading" class="list-empty-state">
                <i class="el-icon-user-solid list-empty-icon"></i>
                <h3>暂无患者数据</h3>
                <p>当前没有患者信息，请添加或调整搜索条件</p>
            </div>

            <!-- 批量操作区域 -->
            <div class="list-batch-actions" v-if="patientData.length > 0">
                <el-card shadow="hover" class="batch-card">
                    <div class="batch-content">
                        <div class="batch-info">
                            <i class="el-icon-info batch-icon"></i>
                            <span class="batch-text">已选择 <strong>{{ selectedCount }}</strong> 位患者</span>
                        </div>

                    </div>
                </el-card>
            </div>
        </el-card>

        <!-- 删除确认对话框 -->
        <el-dialog 
            title="删除确认" 
            :visible.sync="deleteDialogVisible" 
            width="400px"
            class="list-custom-dialog"
        >
            <div class="dialog-content">
                <i class="el-icon-warning dialog-icon"></i>
                <h3 class="dialog-title">确认删除患者信息？</h3>
                <p class="dialog-message">此操作将删除患者 <strong>{{ deletePatientId }}</strong> 的所有信息，且无法恢复</p>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button 
                    @click="deleteDialogVisible = false" 
                    class="dialog-cancel-btn"
                >
                    取消
                </el-button>
                <el-button 
                    type="danger" 
                    @click="confirmDelete" 
                    class="dialog-confirm-btn"
                    :loading="deleting"
                >
                    确认删除
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";

export default {
    name: "PatientList",
    data() {
        return {
            loading: false,
            deleting: false,
            deleteDialogVisible: false,
            deletePatientId: '',
            pageNumber: 1,
            size: 8,
            query: "",
            patientData: [],
            total: 0,
            selectedPatients: []
        };
    },
    computed: {
        // 计算活跃患者数量
        activePatients() {
            return this.patientData.filter(patient => patient.pState === 1).length;
        },
        // 计算已删除患者数量
        deletedPatients() {
            return this.patientData.filter(patient => patient.pState === 0).length;
        },
        // 当前页信息
        currentPageInfo() {
            const start = (this.pageNumber - 1) * this.size + 1;
            const end = Math.min(this.pageNumber * this.size, this.total);
            return `${start}-${end}`;
        },
        // 选中患者数量
        selectedCount() {
            return this.selectedPatients.length;
        }
    },
    methods: {
        // 获取性别类型
        getGenderType(gender) {
            if (gender === '男') return 'primary';
            if (gender === '女') return 'danger';
            return 'info';
        },
        
        // 获取性别图标
        getGenderIcon(gender) {
            if (gender === '男') return 'el-icon-male';
            if (gender === '女') return 'el-icon-female';
            return 'el-icon-question';
        },
        
        // 获取状态类型
        getStatusType(state) {
            return state === 1 ? 'success' : 'danger';
        },
        
        // 获取状态图标
        getStatusIcon(state) {
            return state === 1 ? 'el-icon-success' : 'el-icon-error';
        },
        
        // 获取状态文本
        getStatusText(state) {
            return state === 1 ? '正常' : '已删除';
        },

        //删除患者操作
        deletePatient(id) {
            this.deleting = true;
            request
                .get("admin/deletePatient", {
                    params: {
                        pId: id,
                    },
                })
                .then((res) => {
                    this.deleting = false;
                    this.deleteDialogVisible = false;
                    this.requestPatients();
                    this.$message.success("患者信息删除成功！");
                })
                .catch(() => {
                    this.deleting = false;
                    this.$message.error("删除失败，请重试");
                });
        },
        
        //删除对话框
        deleteDialog(id) {
            this.deletePatientId = id;
            this.deleteDialogVisible = true;
        },
        
        // 确认删除
        confirmDelete() {
            this.deletePatient(this.deletePatientId);
        },
        
        // 批量删除对话框
        batchDeleteDialog() {
            this.$confirm(`确定要删除选中的 ${this.selectedCount} 位患者信息吗？`, "批量删除确认", {
                confirmButtonText: "确认删除",
                cancelButtonText: "取消",
                type: "warning",
                confirmButtonClass: "el-button--danger"
            }).then(() => {
                this.$message.info("批量删除功能开发中...");
            }).catch(() => {
                this.$message.info("已取消批量删除");
            });
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.pageNumber = 1;
            this.requestPatients();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestPatients();
        },
        
        // 加载患者列表
        requestPatients() {
            this.loading = true;
            request
                .get("admin/findAllPatients", {
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
                    this.patientData = res.data.data.patients || [];
                    this.total = res.data.data.total || 0;
                    this.$message.success(`已加载 ${this.patientData.length} 条患者信息`);
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("网络错误，数据加载失败");
                });
        },
    },
    created() {
        this.requestPatients();
    },
};
</script>

<style lang="scss" scoped>
.patient-list-management {
    padding: 20px;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
    min-height: 100vh;
}

/* 顶部标题区域 */
.list-header-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    position: relative;
    padding: 20px 0;
}

.list-title-content {
    z-index: 2;
}

.list-title-icon {
    font-size: 36px;
    color: #409eff;
    margin-bottom: 10px;
}

.list-page-title {
    font-size: 28px;
    font-weight: 600;
    color: #1f2d3d;
    margin: 0 0 5px 0;
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.list-page-subtitle {
    font-size: 16px;
    color: #5a6376;
    margin: 0;
}

.list-decoration-element {
    position: absolute;
    right: 0;
    top: 0;
}

.list-circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;
}

.list-circle-1 {
    width: 100px;
    height: 100px;
    background: #409eff;
    top: -30px;
    right: 0;
}

.list-circle-2 {
    width: 70px;
    height: 70px;
    background: #67c23a;
    top: 10px;
    right: 80px;
}

.list-circle-3 {
    width: 50px;
    height: 50px;
    background: #e6a23c;
    top: 40px;
    right: 40px;
}

/* 主卡片样式 */
.list-management-card {
    border-radius: 16px;
    border: none;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    overflow: hidden;
}

.list-management-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 工具栏样式 */
.list-toolbar-section {
    padding: 20px;
    background: #f8fafc;
    border-bottom: 1px solid #e6e8eb;
}

.list-search-box {
    width: 100%;
}

.list-search-input {
    border-radius: 8px;
    overflow: hidden;
}

.list-search-btn {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    border: none;
    color: white;
    font-weight: 500;
}

.list-stats-info {
    display: flex;
    justify-content: flex-end;
    gap: 30px;
}

.list-stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px 15px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    min-width: 100px;
}

.list-stat-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 5px;
}

.list-stat-value {
    font-size: 16px;
    font-weight: 600;
}

.list-stat-value.success {
    color: #67c23a;
}

.list-stat-value.danger {
    color: #f56c6c;
}

.list-stat-value.info {
    color: #409eff;
}

/* 表格样式 */
.list-table-section {
    padding: 0 20px;
}

.list-data-table {
    border-radius: 8px;
    overflow: hidden;
    margin: 20px 0;
}

.list-id-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.list-id-icon {
    font-size: 16px;
    color: #409eff;
}

.list-id-text {
    font-weight: 600;
    color: #409eff;
    font-family: 'Courier New', monospace;
}

.list-name-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.list-name-icon {
    font-size: 16px;
    color: #67c23a;
}

.list-name-text {
    font-weight: 500;
    color: #1f2d3d;
}

.list-gender-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

.list-age-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 2px;
}

.list-age-number {
    font-size: 16px;
    font-weight: 600;
    color: #e6a23c;
}

.list-age-unit {
    font-size: 12px;
    color: #909399;
}

.list-card-display, .list-phone-display, .list-email-display {
    display: flex;
    align-items: center;
    gap: 8px;
}

.list-card-icon, .list-phone-icon, .list-email-icon {
    font-size: 14px;
    color: #909399;
    width: 16px;
}

.list-card-text, .list-phone-text, .list-email-text {
    font-family: 'Courier New', monospace;
    font-size: 13px;
    color: #606266;
}

.list-status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

.list-delete-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
    font-weight: 500;
    padding: 8px 12px;
    border-radius: 6px;
}

.list-delete-btn:hover:not(.is-disabled) {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.list-delete-btn.is-disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

/* 分页样式 */
.list-pagination-section {
    display: flex;
    justify-content: center;
    padding: 20px;
    border-top: 1px solid #e6e8eb;
}

.list-custom-pagination {
    .el-pagination.is-background .el-pager li:not(.disabled).active {
        background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
        color: white;
    }
}

/* 批量操作区域 */
.list-batch-actions {
    padding: 0 20px 20px;
}

.batch-card {
    border-radius: 8px;
    border: 1px solid #e6e8eb;
}

.batch-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
}

.batch-info {
    display: flex;
    align-items: center;
    gap: 10px;
}

.batch-icon {
    font-size: 16px;
    color: #409eff;
}

.batch-text {
    font-size: 14px;
    color: #606266;
}

.batch-text strong {
    color: #409eff;
}

.batch-buttons {
    display: flex;
    gap: 10px;
}

.batch-delete-btn, .batch-export-btn {
    border-radius: 6px;
    font-weight: 500;
    padding: 8px 12px;
}

.batch-delete-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
}

.batch-export-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    color: white;
}

/* 空状态提示 */
.list-empty-state {
    text-align: center;
    padding: 60px 20px;
    color: #909399;
}

.list-empty-icon {
    font-size: 64px;
    color: #dcdfe6;
    margin-bottom: 20px;
}

.list-empty-state h3 {
    font-size: 18px;
    font-weight: 600;
    color: #606266;
    margin: 0 0 10px 0;
}

.list-empty-state p {
    font-size: 14px;
    margin: 0;
    color: #909399;
}

/* 对话框样式 */
.list-custom-dialog {
    .el-dialog {
        border-radius: 16px;
        overflow: hidden;
    }
    
    .el-dialog__header {
        background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
        padding: 20px;
    }
    
    .el-dialog__title {
        color: white;
        font-size: 18px;
        font-weight: 600;
    }
    
    .el-dialog__body {
        padding: 30px;
    }
}

.dialog-content {
    text-align: center;
    padding: 20px 0;
}

.dialog-icon {
    font-size: 48px;
    color: #e6a23c;
    margin-bottom: 15px;
}

.dialog-title {
    font-size: 18px;
    font-weight: 600;
    color: #1f2d3d;
    margin: 0 0 10px 0;
}

.dialog-message {
    font-size: 14px;
    color: #606266;
    margin: 0;
    line-height: 1.5;
}

.dialog-message strong {
    color: #f56c6c;
}

.dialog-footer {
    text-align: right;
    padding: 20px 0 0;
}

.dialog-cancel-btn, .dialog-confirm-btn {
    border-radius: 8px;
    padding: 10px 20px;
    font-weight: 500;
}

.dialog-confirm-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
}

/* 响应式设计 */
@media (max-width: 1200px) {
    .patient-list-management {
        padding: 15px;
    }
    
    .list-stats-info {
        gap: 15px;
    }
    
    .list-stat-item {
        min-width: 80px;
        padding: 8px 12px;
    }
}

@media (max-width: 768px) {
    .patient-list-management {
        padding: 10px;
    }
    
    .list-header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .list-toolbar-section {
        flex-direction: column;
        gap: 15px;
    }
    
    .list-stats-info {
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
    }
    
    .list-stat-item {
        min-width: 70px;
        padding: 6px 10px;
    }
    
    .list-stat-label {
        font-size: 11px;
    }
    
    .list-stat-value {
        font-size: 14px;
    }
    
    .list-data-table {
        font-size: 12px;
    }
    
    .list-delete-btn {
        padding: 6px 8px;
        font-size: 11px;
    }
    
    .batch-content {
        flex-direction: column;
        gap: 15px;
        text-align: center;
    }
}

@media (max-width: 480px) {
    .list-management-card {
        margin: 0 -10px;
    }
    
    .list-table-section {
        padding: 0 10px;
    }
    
    .list-data-table {
        font-size: 11px;
    }
    
    .list-delete-btn {
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

.list-data-table {
    animation: fadeInUp 0.5s ease forwards;
}

.list-stat-item {
    animation: fadeInUp 0.3s ease forwards;
}

.list-stat-item:nth-child(1) { animation-delay: 0.1s; }
.list-stat-item:nth-child(2) { animation-delay: 0.2s; }
.list-stat-item:nth-child(3) { animation-delay: 0.3s; }
.list-stat-item:nth-child(4) { animation-delay: 0.4s; }
</style>