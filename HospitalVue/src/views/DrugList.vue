<template>
    <div class="drug-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-first-aid-kit title-icon"></i>
                <h1 class="page-title">药物信息管理</h1>
                <p class="page-subtitle">管理医院药品库存和信息</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <!-- 主要内容区域 -->
        <el-card class="management-card" shadow="hover">
            <!-- 操作工具栏 -->
            <div class="toolbar-section">
                <el-row :gutter="20" type="flex" align="middle">
                    <el-col :span="8">
                        <div class="search-box">
                            <el-input 
                                v-model="query" 
                                placeholder="请输入药品名称查询"
                                size="medium"
                                class="search-input"
                            >
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestDrugs"
                                    class="search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                    
                    <el-col :span="16">
                        <div class="action-buttons">
                            <el-button 
                                type="primary" 
                                @click="addFormVisible = true"
                                class="action-btn add-btn"
                            >
                                <i class="el-icon-circle-plus-outline"></i>
                                增加药品
                            </el-button>
                            
                            <div class="stats-info">
                                <div class="stat-item">
                                    <span class="stat-label">药品总数:</span>
                                    <span class="stat-value">{{ totalDrugs }}</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-label">库存不足:</span>
                                    <span class="stat-value warning">{{ lowStockCount }}</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-label">库存充足:</span>
                                    <span class="stat-value success">{{ normalStockCount }}</span>
                                </div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="drugData" 
                    stripe 
                    style="width: 100%" 
                    border
                    class="data-table"
                    v-loading="loading"
                >
                    <el-table-column 
                        prop="drId" 
                        label="药品编号" 
                        width="100"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column 
                        prop="drName" 
                        label="药品名称" 
                        width="150"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column 
                        prop="drNumber" 
                        label="库存数量" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="stock-display">
                                <span 
                                    class="stock-value"
                                    :class="getStockLevel(scope.row.drNumber)"
                                >
                                    {{ scope.row.drNumber }}
                                </span>
                                <span class="stock-unit">{{ scope.row.drUnit }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="drUnit" 
                        label="单位" 
                        width="80"
                        align="center"
                    >
                    </el-table-column>
                    
                    <el-table-column 
                        prop="drPrice" 
                        label="单价" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="price-tag">¥{{ scope.row.drPrice }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        prop="drPublisher" 
                        label="生产厂商" 
                        width="200"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="publisher-text">
                                {{ scope.row.drPublisher || '--' }}
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="库存状态" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="getStockStatusType(scope.row.drNumber)"
                                class="status-tag"
                            >
                                <i :class="getStockStatusIcon(scope.row.drNumber)"></i>
                                {{ getStockStatusText(scope.row.drNumber) }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="操作" 
                        width="150" 
                        fixed="right"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <div class="action-btns">
                                <el-button
                                    type="primary"
                                    size="mini"
                                    @click="modifyDialog(scope.row.drId)"
                                    class="action-btn modify-btn"
                                >
                                    <i class="el-icon-edit"></i>
                                    编辑
                                </el-button>
                                <el-button
                                    type="danger"
                                    size="mini"
                                    @click="deleteDialog(scope.row.drId)"
                                    class="action-btn delete-btn"
                                >
                                    <i class="el-icon-delete"></i>
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
        </el-card>

        <!-- 增加药品对话框 -->
        <el-dialog 
            title="增加药品" 
            :visible.sync="addFormVisible" 
            width="600px"
            class="custom-dialog"
        >
            <el-form 
                :model="addForm" 
                :rules="rules" 
                ref="addRuleForm" 
                label-width="100px"
            >
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="药品编号" prop="drId">
                            <el-input
                                v-model.number="addForm.drId"
                                placeholder="请输入药品编号"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="药品名称" prop="drName">
                            <el-input
                                v-model="addForm.drName"
                                placeholder="请输入药品名称"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="库存数量" prop="drNumber">
                            <el-input-number
                                v-model="addForm.drNumber"
                                :min="0"
                                :max="1000"
                                controls-position="right"
                                style="width: 100%"
                            ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="计量单位" prop="drUnit">
                            <el-radio-group v-model="addForm.drUnit">
                                <el-radio label="盒">盒</el-radio>
                                <el-radio label="袋">袋</el-radio>
                                <el-radio label="剂">剂</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="单价" prop="drPrice">
                            <el-input
                                v-model="addForm.drPrice"
                                placeholder="请输入药品单价"
                                autocomplete="off"
                            >
                                <template slot="append">元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="生产厂商" prop="drPublisher">
                            <el-input
                                v-model="addForm.drPublisher"
                                placeholder="请输入生产厂商"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" class="cancel-btn">取消</el-button>
                <el-button type="primary" @click="addDrug('addRuleForm')" class="confirm-btn">确定</el-button>
            </div>
        </el-dialog>

        <!-- 修改药品对话框 -->
        <el-dialog 
            title="修改药品信息" 
            :visible.sync="modifyFormVisible" 
            width="600px"
            class="custom-dialog"
        >
            <el-form 
                :model="modifyForm" 
                :rules="rules" 
                ref="modifyRuleForm" 
                label-width="100px"
            >
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="药品编号" prop="drId">
                            <el-input
                                v-model.number="modifyForm.drId"
                                disabled
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="药品名称" prop="drName">
                            <el-input
                                v-model="modifyForm.drName"
                                placeholder="请输入药品名称"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="库存数量" prop="drNumber">
                            <el-input-number
                                v-model="modifyForm.drNumber"
                                :min="0"
                                :max="1000"
                                controls-position="right"
                                style="width: 100%"
                            ></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="计量单位" prop="drUnit">
                            <el-radio-group v-model="modifyForm.drUnit">
                                <el-radio label="盒">盒</el-radio>
                                <el-radio label="袋">袋</el-radio>
                                <el-radio label="剂">剂</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="单价" prop="drPrice">
                            <el-input
                                v-model="modifyForm.drPrice"
                                placeholder="请输入药品单价"
                                autocomplete="off"
                            >
                                <template slot="append">元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="生产厂商" prop="drPublisher">
                            <el-input
                                v-model="modifyForm.drPublisher"
                                placeholder="请输入生产厂商"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="modifyFormVisible = false" class="cancel-btn">取消</el-button>
                <el-button type="primary" @click="modifyDrug('modifyRuleForm')" class="confirm-btn">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "DrugList",
    data() {
        return {
            loading: false,
            pageNumber: 1,
            size: 8,
            query: "",
            drugData: [],
            total: 0,
            totalDrugs: 0,
            lowStockCount: 0,
            normalStockCount: 0,
            addFormVisible: false,
            addForm: {
                drNumber: 0
            },
            rules: {
                drId: [
                    { required: true, message: "请输入药品编号", trigger: "blur" },
                    {
                        type: "number",
                        message: "编号必须为数字类型",
                        trigger: "blur",
                    },
                ],
                drName: [
                    { required: true, message: "请输入药品名称", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "药品名称长度为1-50个字符",
                        trigger: "blur",
                    },
                ],
                drUnit: [
                    { required: true, message: "请选择计量单位", trigger: "change" },
                ],
                drPrice: [
                    { required: true, message: "请输入药品单价", trigger: "blur" },
                    {
                        pattern: /^\d+(\.\d{1,2})?$/,
                        message: "请输入正确的价格格式",
                        trigger: "blur"
                    }
                ],
                drPublisher: [
                    {
                        required: true,
                        message: "请输入生产厂商",
                        trigger: "blur",
                    },
                    {
                        min: 1,
                        max: 50,
                        message: "生产厂商名称长度为1-50个字符",
                        trigger: "blur",
                    },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        // 获取库存状态
        getStockLevel(number) {
            if (number < 10) return 'low';
            if (number < 50) return 'medium';
            return 'high';
        },
        
        getStockStatusType(number) {
            if (number < 10) return 'danger';
            if (number < 50) return 'warning';
            return 'success';
        },
        
        getStockStatusIcon(number) {
            if (number < 10) return 'el-icon-warning';
            if (number < 50) return 'el-icon-info';
            return 'el-icon-success';
        },
        
        getStockStatusText(number) {
            if (number < 10) return '库存不足';
            if (number < 50) return '库存一般';
            return '库存充足';
        },
        
        // 计算统计信息
        calculateStats() {
            this.totalDrugs = this.drugData.length;
            this.lowStockCount = this.drugData.filter(drug => drug.drNumber < 10).length;
            this.normalStockCount = this.drugData.filter(drug => drug.drNumber >= 10).length;
        },
        
        //点击修改药品信息
        modifyDrug(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("drug/modifyDrug", {
                            params: this.modifyForm
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("修改信息失败！");
                            this.modifyFormVisible = false;
                            this.requestDrugs();
                            this.$message.success("修改药品信息成功！");
                        });
                }
            });
        },
        
        //打开修改对话框
        modifyDialog(id) {
            this.loading = true;
            request
                .get("drug/findDrug", {
                    params: { drId: id }
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200)
                        return this.$message.error("请求数据失败");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                });
        },
        
        //删除药品操作
        deleteDrug(id) {
            request
                .get("drug/deleteDrug", {
                    params: { drId: id }
                })
                .then((res) => {
                    this.requestDrugs();
                });
        },
        
        //删除对话框
        deleteDialog(id) {
            this.$confirm("此操作将删除该药品信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteDrug(id);
                    this.$message.success("删除成功!");
                })
                .catch(() => {
                    this.$message.info("已取消删除");
                });
        },
        
        //点击增加确认按钮
        addDrug(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("drug/addDrug", {
                            params: this.addForm
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("编号不合法或已被占用！");
                            this.addFormVisible = false;
                            this.requestDrugs();
                            this.$message.success("增加药品成功！");
                        });
                }
            });
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestDrugs();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestDrugs();
        },
        
        // 加载药品列表
        requestDrugs() {
            this.loading = true;
            request
                .get("drug/findAllDrugs", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.drugData = res.data.data.drugs;
                    this.total = res.data.data.total;
                    this.calculateStats();
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("数据加载失败");
                });
        },
    },
    created() {
        this.requestDrugs();
    },
};
</script>

<style lang="scss" scoped>
.drug-management {
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
    background: #427cb3;
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

.add-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    border-radius: 8px;
    font-weight: 500;
}

.stats-info {
    display: flex;
    gap: 20px;
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
    color: #67c23a;
}

.stat-value.warning {
    color: #e6a23c;
}

.stat-value.success {
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

.stock-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
}

.stock-value {
    font-weight: 600;
}

.stock-value.low {
    color: #f56c6c;
}

.stock-value.medium {
    color: #e6a23c;
}

.stock-value.high {
    color: #67c23a;
}

.stock-unit {
    color: #909399;
    font-size: 12px;
}

.price-tag {
    font-weight: 600;
    color: #f56c6c;
    background: #fef0f0;
    padding: 4px 8px;
    border-radius: 4px;
}

.publisher-text {
    max-width: 180px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 4px 8px;
}

.action-btns {
    display: flex;
    justify-content: center;
    gap: 8px;
}

.action-btn {
    border-radius: 6px;
    font-size: 12px;
}

.modify-btn {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    border: none;
    color: white;
}

.delete-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
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
    .drug-management {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .toolbar-section .el-col {
        margin-bottom: 15px;
    }
    
    .action-buttons {
        flex-direction: column;
        gap: 15px;
    }
    
    .stats-info {
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
    }
    
    .stat-item {
        min-width: 100px;
    }
}
</style>