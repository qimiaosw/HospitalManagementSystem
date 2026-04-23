<template>
    <div class="bed-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-office-building title-icon"></i>
                <h1 class="page-title">床位管理</h1>
                <p class="page-subtitle">管理医院床位信息和状态</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 操作工具栏 -->
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
                                    @click="requestBeds"
                                    class="search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                    
                    <el-col :span="16">
                        <div class="action-buttons">
                            <div class="stats-info">
                                <div class="stat-item">
                                    <span class="stat-label">总床位:</span>
                                    <span class="stat-value">{{ total }}</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-label">已占用:</span>
                                    <span class="stat-value occupied">{{ occupiedCount }}</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-label">空闲:</span>
                                    <span class="stat-value available">{{ availableCount }}</span>
                                </div>
                            </div>
                            
                            <el-button 
                                type="primary" 
                                @click="addFormVisible = true"
                                class="action-btn add-btn"
                            >
                                <i class="el-icon-circle-plus-outline"></i>
                                增加床位
                            </el-button>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="bedData" 
                    stripe 
                    style="width: 100%" 
                    border
                    class="data-table"
                    v-loading="loading"
                    empty-text="暂无床位数据"
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
                                <span class="bed-id">{{ scope.row.bId }}</span>
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
                            <span class="id-display" :class="{ 'empty-patient': scope.row.pId === -1 }">
                                {{ scope.row.pId === -1 ? '--' : scope.row.pId }}
                            </span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="医生ID" 
                        prop="dId"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display" :class="{ 'empty-doctor': scope.row.dId === -1 }">
                                {{ scope.row.dId === -1 ? '--' : scope.row.dId }}
                            </span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        label="开始时间"
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
                        label="申请理由"
                        prop="bReason"
                        min-width="200"
                    >
                        <template slot-scope="scope">
                            <div class="reason-display">
                                <i class="el-icon-document reason-icon"></i>
                                <span class="reason-text">{{ scope.row.bReason || '--' }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="状态" 
                        prop="bState"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getStatusType(scope.row.bState)"
                                class="status-tag"
                            >
                                <i :class="getStatusIcon(scope.row.bState)"></i>
                                {{ getStatusText(scope.row.bState) }}
                            </el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column 
                        label="操作" 
                        width="200" 
                        fixed="right"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="action-btns">
                                <el-button
                                    type="success"
                                    size="small"
                                    @click="deleteDialog(scope.row.bId)"
                                    class="action-btn edit-btn"
                                    :disabled="scope.row.bState === 0"
                                >
                                    <i class="el-icon-edit-outline"></i>
                                    编辑
                                </el-button>
                                <el-button
                                    type="danger"
                                    size="small"
                                    @click="emptyDialog(scope.row.bId)"
                                    class="action-btn empty-btn"
                                    :disabled="scope.row.bState === 0"
                                >
                                    <i class="el-icon-delete"></i>
                                    清空
                                </el-button>
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

            <!-- 空状态提示 -->
            <div v-if="bedData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-office-building empty-icon"></i>
                <h3>暂无床位数据</h3>
                <p>当前没有床位信息，请先添加床位</p>
            </div>
        </el-card>

        <!-- 增加床位对话框 -->
        <el-dialog 
            title="增加床位" 
            :visible.sync="addFormVisible" 
            width="500px"
            class="custom-dialog"
        >
            <div class="dialog-header">
                <i class="el-icon-circle-plus-outline dialog-icon"></i>
                <h3>新增床位信息</h3>
            </div>
            
            <el-form 
                :model="addForm" 
                :rules="rules" 
                ref="ruleForm"
                label-width="100px"
                class="add-form"
            >
                <el-form-item label="床号" prop="bId">
                    <el-input 
                        v-model.number="addForm.bId" 
                        placeholder="请输入床位编号"
                        autocomplete="off"
                        class="bed-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-office-building"></i>
                        </template>
                    </el-input>
                    <div class="form-tips">
                        <i class="el-icon-info"></i>
                        请输入数字类型的床位编号
                    </div>
                </el-form-item>
            </el-form>
            
            <div slot="footer" class="dialog-footer">
                <el-button 
                    @click="addFormVisible = false" 
                    class="cancel-btn"
                >
                    <i class="el-icon-close"></i>
                    取消
                </el-button>
                <el-button 
                    type="primary" 
                    @click="addBed('ruleForm')"
                    class="confirm-btn"
                    :loading="submitting"
                >
                    <i class="el-icon-check"></i>
                    确定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "CheckList",
    data() {
        return {
            loading: false,
            submitting: false,
            pageNumber: 1,
            size: 8,
            query: "",
            bedData: [],
            total: 0,
            occupiedCount: 0,
            availableCount: 0,
            addFormVisible: false,
            addForm: {},
            rules: {
                bId: [
                    { required: true, message: "请输入床号", trigger: "blur" },
                    {
                        type: "number",
                        message: "床号必须为数字类型",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        // 格式化时间显示
        formatTime(timeString) {
            if (!timeString) return '--';
            return timeString.replace('T', ' ').substring(0, 16);
        },
        
        // 获取状态类型
        getStatusType(state) {
            return state === 1 ? 'danger' : 'success';
        },
        
        // 获取状态图标
        getStatusIcon(state) {
            return state === 1 ? 'el-icon-user-solid' : 'el-icon-circle-check';
        },
        
        // 获取状态文本
        getStatusText(state) {
            return state === 1 ? '已占用' : '空闲';
        },
        
        // 计算统计信息
        calculateStats() {
            this.occupiedCount = this.bedData.filter(bed => bed.bState === 1).length;
            this.availableCount = this.bedData.filter(bed => bed.bState === 0).length;
        },

        //清空床位操作
        emptyBed(id) {
            this.loading = true;
            request
                .get("bed/emptyBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.requestBeds();
                    this.$message.success("床位清空成功！");
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("清空失败，请重试");
                });
        },
        
        //清空对话框
        emptyDialog(id) {
            this.$confirm("此操作将清空该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                confirmButtonClass: 'el-button--danger'
            })
                .then(() => {
                    this.emptyBed(id);
                })
                .catch(() => {
                    this.$message.info("已取消清空");
                });
        },

        //删除床位操作
        deleteBed(id) {
            this.loading = true;
            request
                .get("bed/deleteBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.requestBeds();
                    this.$message.success("床位删除成功！");
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("删除失败，请重试");
                });
        },
        
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该床位, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                confirmButtonClass: 'el-button--danger'
            })
                .then(() => {
                    this.deleteBed(id);
                })
                .catch(() => {
                    this.$message.info("已取消删除");
                });
        },
        
        //点击增加确认按钮
        addBed(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.submitting = true;
                    request
                        .get("bed/addBed", {
                            params: {
                                bId: this.addForm.bId,
                                pId: -1,
                                dId: -1,
                            },
                        })
                        .then((res) => {
                            this.submitting = false;
                            if (res.data.status !== 200) {
                                this.$message.error("床号已存在，请使用其他编号！");
                                return;
                            }
                            this.addFormVisible = false;
                            this.$message.success("增加床位成功！");
                            this.requestBeds();
                            this.addForm = {}; // 清空表单
                        })
                        .catch(() => {
                            this.submitting = false;
                            this.$message.error("添加失败，请重试");
                        });
                }
            });
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestBeds();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestBeds();
        },
        
        // 加载床位列表
        requestBeds() {
            this.loading = true;
            request
                .get("bed/findAllBeds", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.bedData = res.data.data.beds || [];
                    this.total = res.data.data.total || 0;
                    this.calculateStats();
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("数据加载失败");
                });
        },
    },
    created() {
        this.requestBeds();
    },
};
</script>

<style lang="scss" scoped>
.bed-management {
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
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    color: white;
    font-weight: 500;
}

.action-buttons {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.stats-info {
    display: flex;
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
    color: #67c23a;
}

.stat-value.occupied {
    color: #f56c6c;
}

.stat-value.available {
    color: #67c23a;
}

.add-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    border-radius: 8px;
    font-weight: 500;
    padding: 10px 20px;
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

.bed-number-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.bed-icon {
    font-size: 16px;
    color: #67c23a;
}

.bed-id {
    font-weight: 600;
    color: #67c23a;
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
}

.id-display {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #409eff;
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
}

.id-display.empty-patient,
.id-display.empty-doctor {
    color: #c0c4cc;
    background: #f5f7fa;
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
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

.action-btns {
    display: flex;
    justify-content: center;
    gap: 8px;
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

.edit-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    color: white;
}

.edit-btn:hover:not(.is-disabled) {
    box-shadow: 0 2px 8px rgba(103, 194, 58, 0.3);
}

.empty-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
}

.empty-btn:hover:not(.is-disabled) {
    box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.action-btn.is-disabled {
    opacity: 0.6;
    cursor: not-allowed;
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
        background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
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

/* 对话框样式 */
.custom-dialog {
    .el-dialog {
        border-radius: 16px;
        overflow: hidden;
    }
    
    .el-dialog__header {
        background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
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

.dialog-header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #e6e8eb;
}

.dialog-icon {
    font-size: 24px;
    color: #67c23a;
    margin-right: 10px;
}

.dialog-header h3 {
    margin: 0;
    color: #1f2d3d;
    font-size: 18px;
    font-weight: 600;
}

.bed-input {
    .el-input__inner {
        border-radius: 8px;
    }
}

.form-tips {
    margin-top: 8px;
    padding: 8px 12px;
    background: #f0f9ff;
    border-radius: 4px;
    color: #409eff;
    font-size: 12px;
    
    i {
        margin-right: 5px;
    }
}

.dialog-footer {
    text-align: right;
    padding: 20px 0 0;
}

.cancel-btn, .confirm-btn {
    border-radius: 8px;
    padding: 10px 20px;
    font-weight: 500;
}

.confirm-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .bed-management {
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
    
    .action-buttons {
        flex-direction: column;
        gap: 15px;
    }
    
    .stats-info {
        justify-content: center;
        flex-wrap: wrap;
        gap: 15px;
    }
    
    .stat-item {
        min-width: 100px;
    }
    
    .data-table {
        font-size: 12px;
    }
    
    .action-btns {
        flex-direction: column;
        gap: 5px;
    }
    
    .action-btn {
        padding: 6px 8px;
        font-size: 11px;
    }
}
</style>