<template>
  <div class="patient-card-management">
    <!-- 顶部标题区域 -->
    <div class="card-header-section">
      <div class="card-title-content">
        <i class="el-icon-user-solid card-title-icon"></i>
        <h1 class="card-page-title">个人信息管理</h1>
        <p class="card-page-subtitle">查看和管理您的个人基本信息</p>
      </div>
      <div class="card-decoration-element">
        <div class="card-circle card-circle-1"></div>
        <div class="card-circle card-circle-2"></div>
        <div class="card-circle card-circle-3"></div>
      </div>
    </div>

    <el-card class="card-management-card" shadow="hover">
      <!-- 个人信息概览 -->
      <div class="card-profile-overview">
        <div class="profile-header">
          <div class="profile-avatar-section">
            <div class="avatar-container">
              <i class="el-icon-user-solid avatar-icon"></i>
            </div>
            <div class="profile-info">
              <h3 class="patient-name">{{ patientData.pName || '--' }}</h3>
              <p class="patient-id">患者ID: {{ patientData.pId || '--' }}</p>
              <el-tag
                  :type="getGenderType(patientData.pGender)"
                  class="gender-tag"
              >
                <i :class="getGenderIcon(patientData.pGender)"></i>
                {{ patientData.pGender || '--' }}
              </el-tag>
            </div>
          </div>
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-icon age">
                <i class="el-icon-c-scale-to-original"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ patientData.pAge || '--' }}</div>
                <div class="stat-label">年龄</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon birthday">
                <i class="el-icon-date"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ formatBirthday(patientData.pBirthday) }}</div>
                <div class="stat-label">出生日期</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 详细信息表格 -->
      <div class="card-detail-section">
        <div class="section-header">
          <i class="el-icon-document section-icon"></i>
          <h3 class="section-title">基本信息详情</h3>
        </div>

        <div class="info-table-container">
          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-user label-icon"></i>
              <span class="label-text">姓名</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pName"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-s-custom label-icon"></i>
              <span class="label-text">账号ID</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pId"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-s-custom"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-male label-icon"></i>
              <span class="label-text">性别</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pGender"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-male"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-phone label-icon"></i>
              <span class="label-text">手机号码</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pPhone"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-phone"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-credit-card label-icon"></i>
              <span class="label-text">身份证号</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pCard"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-credit-card"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-message label-icon"></i>
              <span class="label-text">邮箱地址</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pEmail"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-message"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-date label-icon"></i>
              <span class="label-text">出生日期</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pBirthday"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-date"></i>
                </template>
              </el-input>
            </div>
          </div>

          <div class="info-row">
            <div class="info-label">
              <i class="el-icon-time label-icon"></i>
              <span class="label-text">年龄</span>
            </div>
            <div class="info-value">
              <el-input
                  disabled
                  v-model="patientData.pAge"
                  placeholder="未填写"
                  class="info-input"
              >
                <template slot="prepend">
                  <i class="el-icon-time"></i>
                </template>
              </el-input>
            </div>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="card-action-section">
        <el-button
            type="primary"
            class="action-btn edit-btn"
            @click="handleEdit"
        >
          <i class="el-icon-edit"></i>
          编辑信息
        </el-button>
        <el-button
            type="success"
            class="action-btn refresh-btn"
            @click="requestPatient"
            :loading="loading"
        >
          <i class="el-icon-refresh"></i>
          刷新数据
        </el-button>

      </div>

      <!-- 空状态提示 -->
      <div v-if="!patientData.pId && !loading" class="card-empty-state">
        <i class="el-icon-user-solid empty-icon"></i>
        <h3>暂无个人信息</h3>
        <p>未能获取到您的个人信息，请刷新或联系管理员</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="card-loading-state">
        <i class="el-icon-loading loading-icon"></i>
        <p>正在加载个人信息...</p>
      </div>
    </el-card>

    <!-- 编辑信息对话框 -->
    <el-dialog
        title="编辑个人信息"
        :visible.sync="editDialogVisible"
        width="600px"
        :close-on-click-modal="false"
        @close="resetEditForm"
    >
      <el-form
          :model="editForm"
          :rules="editRules"
          ref="editForm"
          label-width="100px"
      >
        <el-form-item label="姓名" prop="pName">
          <el-input v-model="editForm.pName" placeholder="请输入姓名" maxlength="8"></el-input>
        </el-form-item>

        <el-form-item label="手机号码" prop="pPhone">
          <el-input v-model="editForm.pPhone" placeholder="请输入手机号码" maxlength="11"></el-input>
        </el-form-item>

        <el-form-item label="邮箱地址" prop="pEmail">
          <el-input v-model="editForm.pEmail" placeholder="请输入邮箱地址"></el-input>
        </el-form-item>

        <el-form-item label="身份证号" prop="pCard">
          <el-input v-model="editForm.pCard" placeholder="请输入身份证号" maxlength="18"></el-input>
        </el-form-item>

        <el-form-item label="出生日期" prop="pBirthday">
          <el-date-picker
              v-model="editForm.pBirthday"
              type="date"
              placeholder="选择出生日期"
              value-format="yyyy-MM-dd"
              :picker-options="datePickerOptions"
              style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEditForm" :loading="submitting">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
  name: "PatientCard",
  data() {
    return {
      loading: false,
      userId: "",
      patientData: {},

      // 编辑相关
      editDialogVisible: false,      // 编辑对话框是否显示
      submitting: false,             // 提交按钮加载状态
      editForm: {                   // 编辑表单数据
        pId: "",
        pName: "",
        pPhone: "",
        pEmail: "",
        pCard: "",
        pBirthday: ""
      },

      // 日期选择器选项
      datePickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      },

      // 表单验证规则
      editRules: {
        pName: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          { min: 2, max: 8, message: "姓名长度在 2 到 8 个字符", trigger: "blur" }
        ],
        pPhone: [
          { required: true, message: "请输入手机号码", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        pEmail: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        pCard: [
          { required: true, message: "请输入身份证号", trigger: "blur" },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: "请输入正确的身份证号码", trigger: "blur" }
        ],
        pBirthday: [
          { required: true, message: "请选择出生日期", trigger: "change" }
        ]
      }
    };
  },
  methods: {
    // 格式化生日显示
    formatBirthday(birthday) {
      if (!birthday) return '--';
      return birthday;
    },

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

    // 编辑信息
    handleEdit() {
      // 将当前患者数据复制到编辑表单
      this.editForm = {
        pId: this.patientData.pId,
        pName: this.patientData.pName,
        pPhone: this.patientData.pPhone,
        pEmail: this.patientData.pEmail,
        pCard: this.patientData.pCard,
        pBirthday: this.patientData.pBirthday
      };

      // 显示编辑对话框
      this.editDialogVisible = true;
    },

    // 提交编辑表单
    submitEditForm() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.submitting = true;

          // 调用后端接口更新
          request.post("patient/updatePatientInfo", this.editForm)
              .then(res => {
                this.submitting = false;

                if (res.data && res.data.status === 200) {
                  this.$message.success("个人信息更新成功");

                  // 关闭对话框
                  this.editDialogVisible = false;

                  // 刷新数据
                  this.requestPatient();
                } else {
                  this.$message.error(res.data.msg || "更新失败，请稍后重试");
                }
              })
              .catch(error => {
                this.submitting = false;
                this.$message.error("网络错误，更新失败：" + (error.message || "未知错误"));
              });
        } else {
          this.$message.warning("请检查表单信息是否正确");
          return false;
        }
      });
    },

    // 重置编辑表单
    resetEditForm() {
      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields();
      }
      this.submitting = false;
    },

    // 打印信息
    handlePrint() {
      this.$message.info('打印功能开发中...');
    },

    // 请求患者信息
    requestPatient() {
      this.loading = true;
      request.get("doctor/findPatientById", {
        params: {
          pId: this.userId
        }
      })
          .then(res => {
            this.loading = false;
            if (res.data.status !== 200) {
              this.$message.error("获取个人信息失败");
              return;
            }
            this.patientData = res.data.data || {};
            this.$message.success("个人信息加载成功");
          })
          .catch(() => {
            this.loading = false;
            this.$message.error("网络错误，数据加载失败");
          });
    },

    // token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    }
  },
  created() {
    // 解码token信息
    this.userId = this.tokenDecode(getToken()).pId;
    this.requestPatient();
    console.log("患者ID:", this.userId);
  }
};
</script>

<style lang="scss" scoped>
.patient-card-management {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

/* 顶部标题区域 */
.card-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  padding: 20px 0;
}

.card-title-content {
  z-index: 2;
}

.card-title-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 10px;
}

.card-page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
}

.card-page-subtitle {
  font-size: 16px;
  color: #5a6376;
  margin: 0;
}

.card-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.card-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.card-circle-1 {
  width: 100px;
  height: 100px;
  background: #409eff;
  top: -30px;
  right: 0;
}

.card-circle-2 {
  width: 70px;
  height: 70px;
  background: #67c23a;
  top: 10px;
  right: 80px;
}

.card-circle-3 {
  width: 50px;
  height: 50px;
  background: #e6a23c;
  top: 40px;
  right: 40px;
}

/* 主卡片样式 */
.card-management-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-management-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 个人信息概览 */
.card-profile-overview {
  padding: 30px;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
  border-bottom: 1px solid #e6e8eb;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-container {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}

.avatar-icon {
  font-size: 36px;
  color: white;
}

.profile-info {
  flex: 1;
}

.patient-name {
  font-size: 24px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 8px 0;
}

.patient-id {
  font-size: 14px;
  color: #606266;
  margin: 0 0 10px 0;
}

.gender-tag {
  border-radius: 12px;
  padding: 6px 12px;
  font-weight: 500;
  border: none;
}

.profile-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  min-width: 140px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.stat-icon.age {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-icon.birthday {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-number {
  font-size: 20px;
  font-weight: 700;
  color: #1f2d3d;
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 详细信息区域 */
.card-detail-section {
  padding: 30px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e6e8eb;
}

.section-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 10px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.info-table-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f2f5;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 120px;
  min-width: 120px;
}

.label-icon {
  font-size: 16px;
  color: #409eff;
  width: 20px;
}

.label-text {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  white-space: nowrap;
}

.info-value {
  flex: 1;
}

.info-input {
  width: 100%;
}

.info-input .el-input__inner {
  background: #f8fafc;
  border: 1px solid #e6e8eb;
  border-radius: 8px;
  color: #606266;
  font-weight: 500;
}

.info-input .el-input-group__prepend {
  background: #f0f2f5;
  border: none;
  color: #909399;
}

/* 操作按钮区域 */
.card-action-section {
  padding: 30px;
  border-top: 1px solid #e6e8eb;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.action-btn {
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
}

.action-btn:hover:not(.is-disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.edit-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
  color: white;
}

.refresh-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
  color: white;
}

.print-btn {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
  border: none;
  color: white;
}

/* 空状态提示 */
.card-empty-state {
  text-align: center;
  padding: 60px 30px;
  color: #909399;
}

.empty-icon {
  font-size: 64px;
  color: #dcdfe6;
  margin-bottom: 20px;
}

.card-empty-state h3 {
  font-size: 18px;
  font-weight: 600;
  color: #606266;
  margin: 0 0 10px 0;
}

.card-empty-state p {
  font-size: 14px;
  margin: 0;
  color: #909399;
}

/* 加载状态 */
.card-loading-state {
  text-align: center;
  padding: 60px 30px;
  color: #409eff;
}

.loading-icon {
  font-size: 36px;
  margin-bottom: 15px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.card-loading-state p {
  font-size: 14px;
  margin: 0;
  color: #606266;
}

/* 对话框样式 */
.dialog-footer {
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .info-table-container {
    grid-template-columns: 1fr;
  }

  .profile-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }

  .profile-stats {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .patient-card-management {
    padding: 15px;
  }

  .card-header-section {
    flex-direction: column;
    text-align: center;
  }

  .card-profile-overview {
    padding: 20px;
  }

  .profile-avatar-section {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }

  .profile-stats {
    flex-direction: column;
    gap: 15px;
  }

  .stat-item {
    min-width: 100%;
  }

  .card-detail-section {
    padding: 20px;
  }

  .info-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .info-label {
    width: 100%;
  }

  .card-action-section {
    flex-direction: column;
    padding: 20px;
  }

  .action-btn {
    width: 100%;
  }

  .el-dialog {
    width: 90% !important;
  }
}

@media (max-width: 480px) {
  .patient-card-management {
    padding: 10px;
  }

  .card-page-title {
    font-size: 24px;
  }

  .card-profile-overview {
    padding: 15px;
  }

  .avatar-container {
    width: 60px;
    height: 60px;
  }

  .avatar-icon {
    font-size: 24px;
  }

  .patient-name {
    font-size: 20px;
  }

  .card-detail-section {
    padding: 15px;
  }

  .info-table-container {
    grid-template-columns: 1fr;
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

.profile-header {
  animation: fadeInUp 0.5s ease forwards;
}

.info-row {
  animation: fadeInUp 0.6s ease forwards;
}

.info-row:nth-child(1) { animation-delay: 0.1s; }
.info-row:nth-child(2) { animation-delay: 0.2s; }
.info-row:nth-child(3) { animation-delay: 0.3s; }
.info-row:nth-child(4) { animation-delay: 0.4s; }
.info-row:nth-child(5) { animation-delay: 0.5s; }
.info-row:nth-child(6) { animation-delay: 0.6s; }
.info-row:nth-child(7) { animation-delay: 0.7s; }
.info-row:nth-child(8) { animation-delay: 0.8s; }

.card-action-section {
  animation: fadeInUp 0.7s ease forwards;
}
</style>
