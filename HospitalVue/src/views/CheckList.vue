<template>
    <div class="check-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-suitcase title-icon"></i>
                <h1 class="page-title">检查项目管理</h1>
                <p class="page-subtitle">管理医院检查项目信息</p>
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
                                placeholder="请输入项目名称查询"
                                size="medium"
                                class="search-input"
                            >
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestChecks"
                                    class="search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                    
                    <el-col :span="16">
                        <div class="action-buttons">
                            <div class="stats-info">
                                <div class="stat-item">
                                    <span class="stat-label">项目总数:</span>
                                    <span class="stat-value">{{ total }}</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-label">平均价格:</span>
                                    <span class="stat-value price">¥{{ averagePrice }}</span>
                                </div>
                            </div>
                            
                            <el-button 
                                type="primary" 
                                @click="addFormVisible = true"
                                class="action-btn add-btn"
                            >
                                <i class="el-icon-circle-plus-outline"></i>
                                增加项目
                            </el-button>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="checkData" 
                    stripe 
                    border
                    class="data-table"
                    v-loading="loading"
                    empty-text="暂无检查项目数据"
                >
                    <el-table-column 
                        label="项目编号" 
                        prop="chId"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="id-display">
                                <i class="el-icon-suitcase id-icon"></i>
                                <span class="id-text">{{ scope.row.chId }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="项目名称" 
                        prop="chName"
                        min-width="200"
                    >
                        <template slot-scope="scope">
                            <div class="name-display">
                                <i class="el-icon-document name-icon"></i>
                                <span class="name-text">{{ scope.row.chName }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        label="价格"
                        prop="chPrice"
                        width="150"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="price-display">
                                <span class="price-symbol">¥</span>
                                <span class="price-value">{{ scope.row.chPrice }}</span>
                                <span class="price-unit">元</span>
                            </div>
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
                                    @click="modifyDialog(scope.row.chId)"
                                    class="action-btn edit-btn"
                                >
                                    <i class="el-icon-edit-outline"></i>
                                    编辑
                                </el-button>
                                <el-button
                                    type="danger"
                                    size="small"
                                    @click="deleteDialog(scope.row.chId)"
                                    class="action-btn delete-btn"
                                >
                                    <i class="el-icon-delete"></i>
                                    删除
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
            <div v-if="checkData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-suitcase empty-icon"></i>
                <h3>暂无检查项目</h3>
                <p>当前没有检查项目信息，请先添加项目</p>
            </div>
        </el-card>

        <!-- 增加检查项目对话框 -->
        <el-dialog 
            title="增加检查项目" 
            :visible.sync="addFormVisible" 
            width="500px"
            class="custom-dialog"
        >
            <div class="dialog-header">
                <i class="el-icon-circle-plus-outline dialog-icon"></i>
                <h3>新增检查项目</h3>
            </div>
            
            <el-form 
                :model="addForm" 
                :rules="rules" 
                ref="addRuleForm"
                label-width="100px"
                class="add-form"
            >
                <el-form-item label="项目编号" prop="chId">
                    <el-input 
                        v-model.number="addForm.chId" 
                        placeholder="请输入项目编号"
                        autocomplete="off"
                        class="form-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-suitcase"></i>
                        </template>
                    </el-input>
                    <div class="form-tips">
                        <i class="el-icon-info"></i>
                        请输入数字类型的项目编号
                    </div>
                </el-form-item>
                
                <el-form-item label="项目名称" prop="chName">
                    <el-input 
                        v-model="addForm.chName" 
                        placeholder="请输入项目名称"
                        autocomplete="off"
                        class="form-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-document"></i>
                        </template>
                    </el-input>
                </el-form-item>
                
                <el-form-item label="项目价格" prop="chPrice">
                    <el-input 
                        v-model="addForm.chPrice" 
                        placeholder="请输入项目价格"
                        autocomplete="off"
                        class="form-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-coin"></i>
                        </template>
                        <template slot="append">元</template>
                    </el-input>
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
                    @click="addCheck('addRuleForm')"
                    class="confirm-btn"
                    :loading="submitting"
                >
                    <i class="el-icon-check"></i>
                    确定
                </el-button>
            </div>
        </el-dialog>

        <!-- 修改检查项目对话框 -->
        <el-dialog 
            title="修改检查项目" 
            :visible.sync="modifyFormVisible" 
            width="500px"
            class="custom-dialog"
        >
            <div class="dialog-header">
                <i class="el-icon-edit-outline dialog-icon"></i>
                <h3>修改检查项目</h3>
            </div>
            
            <el-form 
                :model="modifyForm" 
                :rules="rules" 
                ref="modifyRuleForm"
                label-width="100px"
                class="modify-form"
            >
                <el-form-item label="项目编号" prop="chId">
                    <el-input 
                        v-model.number="modifyForm.chId" 
                        disabled
                        class="form-input disabled-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-suitcase"></i>
                        </template>
                    </el-input>
                </el-form-item>
                
                <el-form-item label="项目名称" prop="chName">
                    <el-input 
                        v-model="modifyForm.chName" 
                        placeholder="请输入项目名称"
                        autocomplete="off"
                        class="form-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-document"></i>
                        </template>
                    </el-input>
                </el-form-item>
                
                <el-form-item label="项目价格" prop="chPrice">
                    <el-input 
                        v-model="modifyForm.chPrice" 
                        placeholder="请输入项目价格"
                        autocomplete="off"
                        class="form-input"
                    >
                        <template slot="prepend">
                            <i class="el-icon-coin"></i>
                        </template>
                        <template slot="append">元</template>
                    </el-input>
                </el-form-item>
            </el-form>
            
            <div slot="footer" class="dialog-footer">
                <el-button 
                    @click="modifyFormVisible = false" 
                    class="cancel-btn"
                >
                    <i class="el-icon-close"></i>
                    取消
                </el-button>
                <el-button 
                    type="primary" 
                    @click="modifyCheck('modifyRuleForm')"
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
            checkData: [],
            total: 0,
            averagePrice: 0,
            addFormVisible: false,
            addForm: {},
            rules: {
                chId: [
                    { required: true, message: "请输入项目编号", trigger: "blur" },
                    {
                        type: "number",
                        message: "编号必须为数字类型",
                        trigger: "blur",
                    },
                ],
                chName: [
                    { required: true, message: "请输入项目名称", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "项目名称长度为1-50个字符",
                        trigger: "blur",
                    },
                ],
                chPrice: [
                    { required: true, message: "请输入项目价格", trigger: "blur" },
                    {
                        pattern: /^\d+(\.\d{1,2})?$/,
                        message: "请输入正确的价格格式",
                        trigger: "blur"
                    }
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    computed: {
        // 计算平均价格
        calculateAveragePrice() {
            if (this.checkData.length === 0) return 0;
            const total = this.checkData.reduce((sum, item) => {
                return sum + parseFloat(item.chPrice || 0);
            }, 0);
            return (total / this.checkData.length).toFixed(2);
        }
    },
    methods: {
        // 计算统计信息
        calculateStats() {
            this.averagePrice = this.calculateAveragePrice;
        },

        //点击修改项目信息
        modifyCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.submitting = true;
                    request
                        .get("check/modifyCheck", {
                            params: {
                                chId: this.modifyForm.chId,
                                chName: this.modifyForm.chName,
                                chPrice: this.modifyForm.chPrice,
                            },
                        })
                        .then((res) => {
                            this.submitting = false;
                            if (res.data.status !== 200) {
                                this.$message.error("修改信息失败！");
                                return;
                            }
                            this.modifyFormVisible = false;
                            this.requestChecks();
                            this.$message.success("修改检查项目信息成功！");
                        })
                        .catch(() => {
                            this.submitting = false;
                            this.$message.error("修改失败，请重试");
                        });
                }
            });
        },
        
        //打开修改对话框
        modifyDialog(id) {
            this.loading = true;
            request
                .get("check/findCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200) {
                        this.$message.error("请求数据失败");
                        return;
                    }
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("数据加载失败");
                });
        },
        
        //删除检查操作
        deleteCheck(id) {
            this.loading = true;
            request
                .get("check/deleteCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.requestChecks();
                    this.$message.success("删除成功！");
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("删除失败，请重试");
                });
        },
        
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该检查项目信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                confirmButtonClass: 'el-button--danger'
            })
                .then(() => {
                    this.deleteCheck(id);
                })
                .catch(() => {
                    this.$message.info("已取消删除");
                });
        },
        
        //点击增加确认按钮
        addCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.submitting = true;
                    request
                        .get("check/addCheck", {
                            params: {
                                chId: this.addForm.chId,
                                chName: this.addForm.chName,
                                chPrice: this.addForm.chPrice,
                            },
                        })
                        .then((res) => {
                            this.submitting = false;
                            if (res.data.status !== 200) {
                                this.$message.error("编号不合法或已被占用！");
                                return;
                            }
                            this.addFormVisible = false;
                            this.$message.success("增加检查项目成功！");
                            this.requestChecks();
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
            this.requestChecks();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestChecks();
        },
        
        // 加载检查列表
        requestChecks() {
            this.loading = true;
            request
                .get("check/findAllChecks", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.checkData = res.data.data.checks || [];
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
        this.requestChecks();
    },
};
</script>

<style lang="scss" scoped>
.check-management {
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
    padding: 8px 12px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-label {
    font-size: 12px;
    color: #909399;
    margin-bottom: 4px;
}

.stat-value {
    font-size: 16px;
    font-weight: 600;
    color: #409eff;
}

.stat-value.price {
    color: #67c23a;
}

.add-btn {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
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

.id-display {
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
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
}

.name-display {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
}

.name-icon {
    font-size: 16px;
    color: #e6a23c;
    flex-shrink: 0;
}

.name-text {
    font-weight: 500;
    color: #1f2d3d;
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

.price-unit {
    font-size: 12px;
    color: #909399;
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

.delete-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
}

.delete-btn:hover:not(.is-disabled) {
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

/* 对话框样式 */
.custom-dialog {
    .el-dialog {
        border-radius: 16px;
        overflow: hidden;
    }
    
    .el-dialog__header {
        background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
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
    color: #409eff;
    margin-right: 10px;
}

.dialog-header h3 {
    margin: 0;
    color: #1f2d3d;
    font-size: 18px;
    font-weight: 600;
}

.form-input {
    .el-input__inner {
        border-radius: 8px;
    }
}

.disabled-input {
    .el-input__inner {
        background-color: #f5f7fa;
        color: #909399;
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
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    border: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .check-management {
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