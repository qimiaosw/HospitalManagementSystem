<template>
    <div class="arrange-management">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-date title-icon"></i>
                <h1 class="page-title">医生排班管理</h1>
                <p class="page-subtitle">{{ section }} - 医生排班安排</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 面包屑和搜索栏 -->
            <div class="toolbar-section">
                <div class="breadcrumb-section">
                    <el-breadcrumb separator-class="el-icon-arrow-right" class="custom-breadcrumb">
                        <el-breadcrumb-item :to="{ path: '/sectionIndex' }">
                            <i class="el-icon-back"></i>
                            返回科室管理
                        </el-breadcrumb-item>
                        <el-breadcrumb-item>{{ section }}</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                
                <div class="search-section">
                    <div class="search-box">
                        <el-input 
                            v-model="query" 
                            placeholder="请输入医生姓名查询"
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
                </div>
            </div>

            <!-- 排班信息提示 -->
            <div class="arrange-info">
                <div class="info-card">
                    <i class="el-icon-info info-icon"></i>
                    <div class="info-content">
                        <span class="info-text">当前排班日期：</span>
                        <span class="info-date">{{ getArrangeDate() }}</span>
                    </div>
                </div>
            </div>

            <!-- 数据表格 -->
            <div class="table-section">
                <el-table 
                    :data="doctorData" 
                    border 
                    stripe
                    class="data-table"
                    v-loading="loading"
                    empty-text="暂无医生数据"
                >
                    <el-table-column
                        label="医生账号"
                        prop="dId"
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="id-display">{{ scope.row.dId }}</span>
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
                                <i class="el-icon-user doctor-icon"></i>
                                <span>{{ scope.row.dName }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        label="性别"
                        prop="dGender"
                        width="100"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag 
                                :type="scope.row.dGender === '男' ? 'primary' : 'danger'"
                                class="gender-tag"
                            >
                                {{ scope.row.dGender }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        label="职位"
                        prop="dPost"
                        width="150"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <span class="post-display">{{ scope.row.dPost }}</span>
                        </template>
                    </el-table-column>
                    
                    <el-table-column
                        label="所属部门"
                        prop="dSection"
                        width="150"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag class="section-tag">{{ scope.row.dSection }}</el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column 
                        label="排班状态" 
                        width="120"
                        align="center"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                :type="scope.row.arrangeId == null ? 'info' : 'success'"
                                class="status-tag"
                            >
                                <i :class="scope.row.arrangeId == null ? 'el-icon-time' : 'el-icon-success'"></i>
                                {{ scope.row.arrangeId == null ? '未排班' : '已排班' }}
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
                                    v-if="scope.row.arrangeId == null"
                                    type="primary"
                                    size="small"
                                    @click="arrangeClick(scope.row.dId)"
                                    class="action-btn arrange-btn"
                                >
                                    <i class="el-icon-date"></i>
                                    安排排班
                                </el-button>
                                
                                <el-button
                                    v-if="scope.row.arrangeId != null"
                                    type="danger"
                                    size="small"
                                    @click="deleteArrange(scope.row.arrangeId)"
                                    class="action-btn cancel-btn"
                                >
                                    <i class="el-icon-delete"></i>
                                    取消排班
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
            <div v-if="doctorData.length === 0 && !loading" class="empty-state">
                <i class="el-icon-user-solid empty-icon"></i>
                <h3>暂无医生数据</h3>
                <p>当前科室暂无医生信息，请先添加医生</p>
            </div>
        </el-card>
    </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
    name: "ArrangeDoctor",
    data() {
        return {
            section: this.$route.query.section,
            doctorData: [],
            total: 0,
            pageNumber: 1,
            size: 8,
            query: "",
            loading: false
        };
    },
    methods: {
        // 获取排班日期
        getArrangeDate() {
            const date = sessionStorage.getItem("arrangeDate");
            return date || '未选择日期';
        },
        
        //排班点击
        arrangeClick(dId) {
            this.loading = true;
            request
                .get("arrange/addArrange", {
                    params: {
                        arId: dId + sessionStorage.getItem("arrangeDate"),
                        arTime: sessionStorage.getItem("arrangeDate"),
                        dId: dId,
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200) {
                        this.$message.error("该医生已排班");
                        return;
                    }
                    this.$message.success("排班成功！");
                    this.requestDoctors();
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("排班失败，请重试");
                });
        },
        
        deleteArrange(arrangeId) {
            this.$confirm('确定要取消该医生的排班吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                confirmButtonClass: 'el-button--danger'
            }).then(() => {
                this.loading = true;
                request
                    .get("arrange/deleteArrange", {
                        params: {
                            arId: arrangeId,
                        },
                    })
                    .then((res) => {
                        this.loading = false;
                        if (res.data.status !== 200) {
                            this.$message.error("排班信息不存在");
                            return;
                        }
                        this.$message.success("取消排班成功！");
                        this.requestDoctors();
                    })
                    .catch(() => {
                        this.loading = false;
                        this.$message.error("取消排班失败，请重试");
                    });
            }).catch(() => {
                this.$message.info('已取消操作');
            });
        },
        
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestDoctors();
        },
        
        //页码改变时触发
        handleCurrentChange(num) {
            this.pageNumber = num;
            this.requestDoctors();
        },
        
        //根据部门请求医生信息
        requestDoctors() {
            this.loading = true;
            request
                .get("doctor/findDoctorBySectionPage", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                        dSection: this.section,
                        arrangeDate: sessionStorage.getItem("arrangeDate"),
                    },
                })
                .then((res) => {
                    this.loading = false;
                    if (res.data.status !== 200) {
                        this.$message.error("数据请求失败");
                        return;
                    }
                    this.doctorData = res.data.data.doctors || [];
                    this.total = res.data.data.total || 0;
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error("网络错误，数据加载失败");
                });
        },
    },
    created() {
        this.requestDoctors();
    },
};
</script>

<style lang="scss" scoped>
.arrange-management {
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
    background: #409eff;
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
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background: #f8fafc;
    border-bottom: 1px solid #e6e8eb;
}

.custom-breadcrumb {
    font-size: 14px;
    
    .el-breadcrumb__item {
        .el-breadcrumb__inner {
            display: flex;
            align-items: center;
            gap: 5px;
            color: #5a6376;
            transition: color 0.3s ease;
        }
        
        &:hover .el-breadcrumb__inner {
            color: #e6a23c;
        }
    }
}

.search-section {
    display: flex;
    align-items: center;
}

.search-box {
    width: 300px;
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

/* 排班信息提示 */
.arrange-info {
    padding: 0 20px;
    margin-bottom: 20px;
}

.info-card {
    display: flex;
    align-items: center;
    padding: 12px 16px;
    background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
    border: 1px solid #d9ecff;
    border-radius: 8px;
    color: #409eff;
}

.info-icon {
    font-size: 18px;
    margin-right: 10px;
}

.info-content {
    display: flex;
    align-items: center;
    gap: 8px;
}

.info-text {
    font-size: 14px;
}

.info-date {
    font-weight: 600;
    background: white;
    padding: 2px 8px;
    border-radius: 4px;
    border: 1px solid #d9ecff;
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
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #409eff;
    background: #f0f9ff;
    padding: 4px 8px;
    border-radius: 4px;
}

.doctor-name {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
}

.doctor-icon {
    font-size: 16px;
    color: #67c23a;
}

.gender-tag {
    border-radius: 12px;
    font-weight: 500;
    padding: 6px 12px;
}

.post-display {
    font-weight: 500;
    color: #1f2d3d;
    background: #f5f7fa;
    padding: 4px 8px;
    border-radius: 4px;
}

.section-tag {
    background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
    color: #409eff;
    border: 1px solid #d9ecff;
    border-radius: 12px;
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

.arrange-btn {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border: none;
    color: white;
}

.arrange-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(103, 194, 58, 0.3);
}

.cancel-btn {
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    border: none;
    color: white;
}

.cancel-btn:hover {
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
        background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
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
    margin: 0 0 10px 0;
    color: #606266;
}

.empty-state p {
    font-size: 14px;
    margin: 0;
    color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .arrange-management {
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
    
    .search-box {
        width: 100%;
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