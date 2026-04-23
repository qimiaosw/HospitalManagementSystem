<template>
    <div class="bed-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-office-building title-icon"></i>
                <h1 class="page-title">住院申请管理</h1>
                <p class="page-subtitle">管理患者住院申请和床位分配</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 搜索栏 -->
            <div class="toolbar-section">
                <el-row type="flex" align="middle">
                    <el-col :span="6">
                        <div class="search-box">
                            <el-input v-model="query" placeholder="请输入患者id查询" class="search-input">
                                <el-button
                                    slot="append"
                                    icon="el-icon-search"
                                    @click="requestOrders"
                                    class="search-btn"
                                >搜索</el-button>
                            </el-input>
                        </div>
                    </el-col>
                </el-row>
            </div>

            <!-- 表格 -->
            <div class="table-section">
                <el-table :data="orderData" stripe border class="data-table" v-loading="loading">
                    <el-table-column label="挂号单号" prop="oId" width="100" align="center"></el-table-column>
                    <el-table-column label="患者id" prop="pId" width="100" align="center"></el-table-column>
                    <el-table-column label="医生id" prop="dId" width="100" align="center"></el-table-column>
                    <el-table-column label="挂号时间" prop="oStart" width="180" align="center">
                        <template slot-scope="scope">
                            <div class="time-display">{{ scope.row.oStart }}</div>
                        </template>
                    </el-table-column>
                    <el-table-column label="结束时间" prop="oEnd" width="180" align="center">
                        <template slot-scope="scope">
                            <div class="time-display">{{ scope.row.oEnd }}</div>
                        </template>
                    </el-table-column>
                    <el-table-column label="挂号状态" prop="oState" width="120" align="center">
                        <template slot-scope="scope">
                            <el-tag 
                                :type="scope.row.oState === 1 ? 'success' : 'danger'" 
                                class="status-tag"
                            >
                                <i :class="scope.row.oState === 1 ? 'el-icon-success' : 'el-icon-error'"></i>
                                {{ scope.row.oState === 1 ? '已完成' : '未完成' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="160" fixed="right" align="center">
                        <template slot-scope="scope">
                            <div class="action-btns">
                                <el-button
                                    type="warning"
                                    @click="BedDiag(scope.row.pId, scope.row.dId)"
                                    class="action-btn bed-btn"
                                    :disabled="scope.row.oState !== 1"
                                >
                                    <i class="el-icon-house"></i>
                                    申请住院
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
                    :page-sizes="[4, 8, 16, 24]"
                    :total="total"
                    class="custom-pagination"
                >
                </el-pagination>
            </div>
        </el-card>

        <!-- 住院对话框 -->
        <el-dialog title="申请住院" :visible.sync="BedFormVisible" class="custom-dialog">
            <div class="dialog-header">
                <i class="el-icon-house dialog-icon"></i>
                <h3>住院申请</h3>
            </div>
            
            <el-form class="findPassword" :model="bedForm" label-width="100px">
                <el-form-item label="患者账号" prop="pId">
                    <el-input v-model="bedForm.pId" disabled class="disabled-input">
                        <template slot="prepend">
                            <i class="el-icon-user"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="医生账号">
                    <el-input v-model="bedForm.dId" disabled class="disabled-input">
                        <template slot="prepend">
                            <i class="el-icon-user-solid"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="申请原因">
                    <el-input
                        v-model="bedForm.bReason"
                        type="textarea"
                        :rows="4"
                        placeholder="请输入详细的住院申请原因"
                        class="reason-textarea"
                    ></el-input>
                </el-form-item>

                <el-form-item label="病床号" prop="bId">
                    <el-select v-model="bedForm.bId" class="bed-select" placeholder="请选择病床号">
                        <el-option
                            v-for="item in nullBed"
                            :key="item.bId"
                            :label="item.bId + '号床'"
                            :value="item.bId"
                            class="bed-option"
                        >
                            <span class="bed-number">{{ item.bId }}号床</span>
                        </el-option>
                    </el-select>
                    <div class="bed-tips" v-if="nullBed.length > 0">
                        <i class="el-icon-info"></i>
                        当前有 {{ nullBed.length }} 个空余床位可供选择
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="BedFormVisible = false" class="cancel-btn">
                    <i class="el-icon-close"></i>
                    取消
                </el-button>
                <el-button type="primary" @click="bedClick" class="confirm-btn">
                    <i class="el-icon-check"></i>
                    确定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
    name: "InBed",
    data() {
        return {
            loading: false,
            userId: 1,
            userName: "",
            pageNumber: 1,
            size: 4,
            query: "",
            total: 3,
            orderData: [],
            //申请住院对话框
            BedFormVisible: false,
            bedForm: {},
            nullBed: [],
        };
    },
    methods: {
        //点击申请床位确认按钮
        bedClick() {
            request
                .get("bed/updateBed", {
                    params: {
                        bId: this.bedForm.bId,
                        dId: this.bedForm.dId,
                        pId: this.bedForm.pId,
                        bReason: this.bedForm.bReason,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("来晚了...该床位已被占用");
                    this.BedFormVisible = false;
                    this.$message.success("申请住院成功！");
                    this.requestOrders();
                    console.log(res);
                });
        },

        //请求所有空床位
        requestBeds() {
            request
                .get("bed/findNullBed")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.nullBed = res.data.data;
                    console.log(res.data.data);
                })
                .catch((err) => {
                    console.error(err);
                });
        },
        
        //打开申请住院对话框
        BedDiag(pId, dId) {
            this.bedForm = {
                pId: pId,
                dId: dId,
                bId: '',
                bReason: ''
            };
            this.BedFormVisible = true;
            this.requestBeds();
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            console.log(size);
            this.size = size;
            this.requestOrders();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestOrders();
        },
        
        //获取已完成的订单信息
        requestOrders() {
            this.loading = true;
            request
                .get("order/findOrderFinish", {
                    params: {
                        dId: this.userId,
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200)
                        return this.$message.error("数据请求失败");
                    this.orderData = res.data.data.orders;
                    this.total = res.data.data.total;
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
        this.userName = this.tokenDecode(getToken()).dName;
        console.log(this.userId);
        console.log(this.userName);
        //获取订单信息
        this.requestOrders();
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
    background: #67c23a;
    top: 10px;
    right: 80px;
}

.circle-3 {
    width: 50px;
    height: 50px;
    background: #f56c6c;
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
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
    border: none;
    color: white;
    font-weight: 500;
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

.time-display {
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

.bed-btn {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
    border: none;
    border-radius: 6px;
    color: white;
    font-weight: 500;
    padding: 8px 12px;
    font-size: 12px;
}

.bed-btn:hover:not(.is-disabled) {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

.bed-btn.is-disabled {
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
        background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
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
        background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
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
    color: #e6a23c;
    margin-right: 10px;
}

.dialog-header h3 {
    margin: 0;
    color: #1f2d3d;
    font-size: 18px;
    font-weight: 600;
}

.disabled-input {
    .el-input__inner {
        background-color: #f5f7fa;
        color: #909399;
    }
}

.reason-textarea {
    .el-textarea__inner {
        border-radius: 8px;
        resize: none;
    }
}

.bed-select {
    width: 100%;
    
    .el-input__inner {
        border-radius: 8px;
    }
}

.bed-option {
    padding: 8px 12px;
}

.bed-number {
    font-weight: 500;
    color: #1f2d3d;
}

.bed-tips {
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
    font-size: 14px;
}

.confirm-btn {
    background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
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
    
    .toolbar-section .el-col {
        margin-bottom: 15px;
    }
    
    .data-table {
        font-size: 12px;
    }
    
    .bed-btn {
        padding: 6px 8px;
        font-size: 12px;
    }
}
</style>