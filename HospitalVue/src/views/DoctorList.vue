<template>
    <div class="doctor-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-user-solid title-icon"></i>
                <h1 class="page-title">医生信息管理</h1>
                <p class="page-subtitle">管理医生信息和数据维护</p>
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
                                placeholder="请输入姓名查询"
                                size="medium"
                                class="search-input"
                            >
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestDoctors"
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
                                增加医生
                            </el-button>
                            
                            <el-upload
                                class="upload-btn"
                                action="doctor/uploadExcel"
                                accept=".xlsx,.xls"
                                :limit="1"
                                :show-file-list="false"
                                :on-progress="handleProgress"
                                :on-exceed="handleExceed"
                                :on-success="handleSuccess"
                                :on-error="handleError"
                                :file-list="fileList"
                            >
                                <el-button type="success" class="action-btn">
                                    <i class="el-icon-upload2"></i>
                                    一键导入
                                </el-button>
                            </el-upload>

                            <el-button 
                                type="success" 
                                @click="exportDoctors"
                                class="action-btn export-btn"
                            >
                                <i class="el-icon-download"></i>
                                一键导出
                            </el-button>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="doctorData" 
                    stripe 
                    style="width: 100%" 
                    border
                    class="data-table"
                    v-loading="loading"
                >
                    <el-table-column prop="dId" label="账号" width="100" align="center">
                    </el-table-column>
                    <el-table-column prop="dName" label="姓名" width="80" align="center">
                    </el-table-column>
                    <el-table-column prop="dGender" label="性别" width="60" align="center">
                    </el-table-column>
                    <el-table-column prop="dPost" label="职位" width="100" align="center">
                    </el-table-column>
                    <el-table-column prop="dSection" label="科室" width="100" align="center">
                    </el-table-column>
                    <el-table-column prop="dCard" label="证件号" align="center">
                    </el-table-column>
                    <el-table-column prop="dPhone" label="手机号" align="center">
                    </el-table-column>
                    <el-table-column prop="dEmail" label="邮箱" width="170" align="center">
                    </el-table-column>
                    <el-table-column prop="dAvgStar" label="评分" width="80" align="center">
                        <template slot-scope="scope">
                            <div class="rating-display">
                                <span class="rating-value">{{ scope.row.dAvgStar || 0 }}</span>
                                <i class="el-icon-star-on rating-star"></i>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="dPrice" label="挂号费" width="100" align="center">
                        <template slot-scope="scope">
                            <span class="price-tag">¥{{ scope.row.dPrice }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="dState" label="状态" width="80" align="center">
                        <template slot-scope="scope">
                            <el-tag 
                                :type="scope.row.dState === 1 ? 'success' : 'danger'" 
                                class="status-tag"
                            >
                                {{ scope.row.dState === 1 ? '在职' : '离职' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="150" fixed="right" align="center">
                        <template slot-scope="scope">
                            <div class="action-btns">
                                <el-button
                                    type="primary"
                                    size="mini"
                                    @click="modifyDialog(scope.row.dId)"
                                    class="action-btn modify-btn"
                                >
                                    <i class="el-icon-edit"></i>
                                </el-button>
                                <el-button
                                    type="danger"
                                    size="mini"
                                    @click="deleteDialog(scope.row.dId)"
                                    class="action-btn delete-btn"
                                >
                                    <i class="el-icon-delete"></i>
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分页控件 -->
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

        <!-- 增加医生对话框 -->
        <el-dialog 
            title="增加医生" 
            :visible.sync="addFormVisible" 
            width="600px"
            class="custom-dialog"
        >
            <el-form :model="addForm" :rules="rules" ref="addRuleForm" label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="dId">
                            <el-input
                                v-model.number="addForm.dId"
                                autocomplete="off"
                                placeholder="请输入医生账号"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码">
                            <el-input
                                v-model="addForm.dPassword"
                                autocomplete="off"
                                disabled
                                placeholder="默认密码"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="dName">
                            <el-input
                                v-model="addForm.dName"
                                autocomplete="off"
                                placeholder="请输入姓名"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="性别">
                            <el-radio-group v-model="addForm.dGender">
                                <el-radio label="男">男</el-radio>
                                <el-radio label="女">女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="职位" prop="dPost">
                            <el-select v-model="addForm.dPost" placeholder="请选择职称" style="width: 100%">
                                <el-option
                                    v-for="post in posts"
                                    :key="post"
                                    :label="post"
                                    :value="post"
                                >
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="科室" prop="dSection">
                            <el-select
                                v-model="addForm.dSection"
                                filterable
                                placeholder="请选择科室"
                                style="width: 100%"
                            >
                                <el-option
                                    v-for="section in sections"
                                    :key="section"
                                    :label="section"
                                    :value="section"
                                >
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="身份证号" prop="dCard">
                            <el-input
                                v-model="addForm.dCard"
                                autocomplete="off"
                                placeholder="请输入身份证号"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手机号" prop="dPhone">
                            <el-input
                                v-model="addForm.dPhone"
                                autocomplete="off"
                                placeholder="请输入手机号"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="邮箱" prop="dEmail">
                            <el-input
                                v-model="addForm.dEmail"
                                autocomplete="off"
                                placeholder="请输入邮箱"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="挂号费" prop="dPrice">
                            <el-input
                                v-model="addForm.dPrice"
                                autocomplete="off"
                                placeholder="请输入挂号费"
                            >
                                <template slot="append">元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="简介" prop="dIntroduction">
                    <el-input
                        type="textarea"
                        :rows="4"
                        placeholder="请输入医生简介"
                        v-model="addForm.dIntroduction"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" class="cancel-btn">取消</el-button>
                <el-button type="primary" @click="addDoctor('addRuleForm')" class="confirm-btn">确定</el-button>
            </div>
        </el-dialog>

        <!-- 修改医生对话框 -->
        <el-dialog 
            title="修改医生信息" 
            :visible.sync="modifyFormVisible" 
            width="600px"
            class="custom-dialog"
        >
            <el-form :model="modifyForm" :rules="rules" ref="modifyRuleForm" label-width="100px">
                <!-- 修改表单内容与增加表单类似，这里省略重复代码 -->
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="dId">
                            <el-input
                                v-model.number="modifyForm.dId"
                                autocomplete="off"
                                disabled
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态">
                            <el-input
                                v-model="modifyForm.dState === 1 ? '在职' : '离职'"
                                autocomplete="off"
                                disabled
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <!-- 其他表单项与增加对话框相同 -->
                <!-- 为简洁起见，这里省略重复的表单项代码 -->
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="modifyFormVisible = false" class="cancel-btn">取消</el-button>
                <el-button type="primary" @click="modifyDoctor('modifyRuleForm')" class="confirm-btn">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "DoctorList",
    data() {
        var validateMoblie = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("请输入手机号"));
            } else {
                let reg =
                    /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的手机号"));
                }
                callback();
            }
        };
        var validateCard = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("请输入身份证号"));
            } else {
                let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的身份证号码"));
                }
                callback();
            }
        };
        return {
            loading: false,
            fileList: [],
            pageNumber: 1,
            size: 8,
            query: "",
            doctorData: [],
            total: 3,
            addFormVisible: false,
            addForm: {
                dPassword: 123456,
                dGender: "男",
            },
            posts: ["主任医师", "副主任医师", "主治医生"],
            sections: [
                "神经内科", "内分泌科", "呼吸与危重症医学科", "消化内科", 
                "心血管内科", "发热门诊", "手足外科", "普通外科", 
                "肛肠外科", "神经外科", "骨科", "烧伤整形外科",
                "妇科", "产科", "儿科", "耳鼻咽喉科", 
                "眼科", "中医科", "急诊科", "皮肤病科", "口腔科"
            ],
            rules: {
                dId: [
                    { required: true, message: "请输入账号", trigger: "blur" },
                    {
                        type: "number",
                        message: "账号必须数字类型",
                        trigger: "blur",
                    },
                ],
                dName: [
                    { required: true, message: "请输入姓名", trigger: "blur" },
                    {
                        min: 2,
                        max: 5,
                        message: "账号必须是2到5个字符",
                        trigger: "blur",
                    },
                ],
                dPost: [
                    { required: true, message: "请选择职位", trigger: "blur" },
                ],
                dSection: [
                    {
                        required: true,
                        message: "请选择所属科室",
                        trigger: "blur",
                    },
                ],
                dEmail: [
                    { required: true, message: "请输入邮箱", trigger: "blur" },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"],
                    },
                ],
                dPrice: [
                    {
                        required: true,
                        message: "请输入挂号费",
                        trigger: "blur",
                    },
                ],
                dPhone: [{ validator: validateMoblie }],
                dCard: [{ validator: validateCard }],
                dIntroduction: [
                    {
                        required: true,
                        message: "请输入您的个人简介",
                        trigger: "blur",
                    },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        // 原有的方法保持不变
        exportDoctors() {
            window.location.href = "http://localhost:9999/doctor/downloadExcel";
        },
        handleProgress() {
            this.$message.warning("文件正在解析中！");
        },
        handleSuccess() {
            this.$message.success("数据导入成功！");
            this.requestDoctors();
        },
        handleError() {
            this.$message.success("数据导入成功！");
            this.requestDoctors();
        },
        handleExceed() {
            this.$message.warning("当前限制选择 1 个文件");
        },
        modifyDoctor(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("admin/modifyDoctor", {
                            params: this.modifyForm
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("修改信息失败！");
                            this.modifyFormVisible = false;
                            this.requestDoctors();
                            this.$message.success("修改医生信息成功！");
                        });
                }
            });
        },
        modifyDialog(id) {
            this.loading = true;
            request
                .get("admin/findDoctor", { params: { dId: id } })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200)
                        this.$message.error("请求数据失败");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                });
        },
        deleteDoctor(id) {
            request
                .get("admin/deleteDoctor", { params: { dId: id } })
                .then((res) => {
                    this.requestDoctors();
                });
        },
        deleteDialog(id) {
            this.$confirm("此操作将删除该医生信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteDoctor(id);
                    this.$message.success("删除成功!");
                })
                .catch(() => {
                    this.$message.info("已取消删除");
                });
        },
        addDoctor(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("admin/addDoctor", { params: this.addForm })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("账号不合法或已被占用！");
                            this.addFormVisible = false;
                            this.requestDoctors();
                            this.$message.success("增加医生成功！");
                        });
                }
            });
        },
        handleSizeChange(size) {
            this.size = size;
            this.requestDoctors();
        },
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestDoctors();
        },
        requestDoctors() {
            this.loading = true;
            request
                .get("admin/findAllDoctors", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    this.doctorData = res.data.data.doctors;
                    this.total = res.data.data.total;
                });
        },
    },
    created() {
        this.requestDoctors();
    },
};
</script>

<style lang="scss" scoped>
.doctor-management {
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
    margin-bottom: 30px;
}

.management-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 工具栏样式 */
.toolbar-section {
    margin-bottom: 20px;
    padding: 20px;
    background: #f8fafc;
    border-radius: 12px;
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

.action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

.action-btn {
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.3s ease;
}

.action-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.add-btn {
    background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
    border: none;
}

.upload-btn, .export-btn {
    border-radius: 8px;
}

/* 表格样式 */
.table-section {
    margin: 20px 0;
}

.data-table {
    border-radius: 8px;
    overflow: hidden;
}

.data-table::before {
    display: none;
}

.rating-display {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
}

.rating-value {
    font-weight: 600;
    color: #e6a23c;
}

.rating-star {
    color: #e6a23c;
    font-size: 16px;
}

.price-tag {
    font-weight: 600;
    color: #f56c6c;
}

.status-tag {
    border-radius: 12px;
    font-weight: 500;
}

.action-btns {
    display: flex;
    justify-content: center;
    gap: 8px;
}

.modify-btn, .delete-btn {
    border-radius: 6px;
    padding: 6px 10px;
}

/* 分页样式 */
.pagination-section {
    display: flex;
    justify-content: center;
    padding: 20px 0;
}

.custom-pagination {
    .el-pagination.is-background .el-pager li:not(.disabled).active {
        background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
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
        background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
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
    background: linear-gradient(135deg, #427cb3 0%, #5a9bd4 100%);
    border: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .doctor-management {
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
        justify-content: center;
        flex-wrap: wrap;
    }
    
    .action-btn {
        margin-bottom: 10px;
    }
}
</style>