<template>
  <div class="payment-success-container">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-success title-icon"></i>
        <h1 class="page-title">支付成功</h1>
        <p class="page-subtitle">您的挂号费用已成功支付</p>
      </div>
      <div class="decoration-element">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <el-card class="success-card" shadow="hover">
      <!-- 成功图标 -->
      <div class="success-icon-wrapper">
        <div class="success-icon-circle">
          <i class="el-icon-success success-icon"></i>
        </div>
      </div>

      <!-- 成功信息 -->
      <div class="success-info">
        <h2 class="success-title">支付成功！</h2>
        <p class="success-desc">感谢您的支付，您的挂号信息已确认</p>
      </div>

      <!-- 支付详情 -->
      <div class="payment-details" v-loading="loading">
        <div v-if="paymentInfo">
          <el-divider content-position="left">
            <span class="divider-text">支付详情</span>
          </el-divider>

          <el-row :gutter="20" class="detail-row">
            <el-col :span="12">
              <div class="detail-item">
                <span class="detail-label">订单编号：</span>
                <span class="detail-value">{{ paymentInfo.oId }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <span class="detail-label">支付宝交易号：</span>
                <span class="detail-value">{{ paymentInfo.tradeNo }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="detail-row">
            <el-col :span="12">
              <div class="detail-item">
                <span class="detail-label">支付状态：</span>
                <el-tag type="success" class="status-tag">
                  <i class="el-icon-success"></i>
                  {{ paymentInfo.status }}
                </el-tag>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <span class="detail-label">支付时间：</span>
                <span class="detail-value">{{ paymentInfo.payTime }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div v-else>
          <el-empty description="正在获取支付信息..."></el-empty>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button
            type="primary"
            size="large"
            class="action-btn primary-btn"
            @click="goToMyOrder"
        >
          <i class="el-icon-tickets"></i>
          查看我的挂号
        </el-button>
        <el-button
            type="default"
            size="large"
            class="action-btn default-btn"
            @click="goToHome"
        >
          <i class="el-icon-s-home"></i>
          返回首页
        </el-button>
      </div>

      <!-- 温馨提示 -->
      <div class="tips-section">
        <el-alert
            title="温馨提示"
            type="info"
            :closable="false"
            show-icon
        >
          <template slot="default">
            <ul class="tips-list">
              <li>支付成功后，请按时到医院就诊</li>
              <li>如需退费，请提前联系医院客服</li>
              <li>请妥善保管您的挂号信息</li>
            </ul>
          </template>
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request.js";

export default {
  name: "PaymentSuccess",
  data() {
    return {
      loading: false,
      paymentInfo: null,
      paymentInfoError: false
    };
  },
  methods: {
    // 获取支付信息
    async fetchPaymentInfo() {
      this.loading = true;

      // 从URL参数中获取订单ID
      const oId = this.$route.query.oId;
      const tradeNo = this.$route.query.trade_no;

      if (oId) {
        try {
          // 调用后端接口获取订单信息
          const response = await request.get("/payment/query", {
            params: {
              oId: oId
            }
          });

          if (response.data.status === 200 && response.data.data) {
            const order = response.data.data;

            this.paymentInfo = {
              oId: order.oId,
              tradeNo: tradeNo || '暂无',
              status: order.oPrice > 0 ? '已支付' : '已支付',
              payTime: order.oPrice > 0 ? this.formatTime(order.oPriceTime) : '暂无'
            };
          } else {
            this.$message.error("获取订单信息失败");
            this.paymentInfoError = true;
          }
        } catch (error) {
          console.error("获取订单信息失败：", error);
          this.$message.error("获取订单信息失败：" + (error.message || "未知错误"));
          this.paymentInfoError = true;
        }
      } else {
        this.$message.warning("未找到订单信息");
        this.paymentInfoError = true;
      }

      this.loading = false;
    },

    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp) return '暂无';

      const date = new Date(timestamp);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },

    // 获取当前时间
    getCurrentTime() {
      const now = new Date();
      return now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },

    // 跳转到我的挂号页面
    goToMyOrder() {
      this.$router.push('/myOrder');
    },

    // 返回首页
    goToHome() {
      this.$router.push('/patientLayout');
    }
  },
  created() {
    console.log("支付成功页面加载，URL参数：", this.$route.query);
    this.fetchPaymentInfo();

    // 显示成功消息
    this.$message.success("支付成功！");
  }
};
</script>


<style lang="scss" scoped>
.payment-success-container {
  padding: 20px;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
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
  text-align: center;
  width: 100%;
}

.title-icon {
  font-size: 48px;
  color: #67c23a;
  margin-bottom: 10px;
}

.page-title {
  font-size: 32px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 10px 0;
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

/* 成功卡片 */
.success-card {
  max-width: 800px;
  margin: 0 auto;
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 成功图标 */
.success-icon-wrapper {
  padding: 40px 0;
  text-align: center;
}

.success-icon-circle {
  width: 120px;
  height: 120px;
  margin: 0 auto;
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 30px rgba(103, 194, 58, 0.3);
  animation: scaleIn 0.6s ease;
}

.success-icon {
  font-size: 60px;
  color: white;
}

@keyframes scaleIn {
  from {
    transform: scale(0);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

/* 成功信息 */
.success-info {
  text-align: center;
  margin-bottom: 40px;
}

.success-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 10px 0;
}

.success-desc {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

/* 支付详情 */
.payment-details {
  margin-bottom: 40px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
}

.divider-text {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}

.detail-row {
  margin-bottom: 20px;
}

.detail-item {
  padding: 10px;
  background: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
}

.detail-label {
  font-size: 14px;
  color: #909399;
  margin-right: 10px;
  white-space: nowrap;
}

.detail-value {
  font-size: 14px;
  font-weight: 600;
  color: #1f2d3d;
}

.status-tag {
  font-size: 14px;
  padding: 6px 12px;
  border-radius: 12px;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
}

.action-btn {
  border-radius: 8px;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.primary-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
  color: white;
}

.primary-btn:hover {
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
}

.default-btn {
  background: white;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.default-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 温馨提示 */
.tips-section {
  margin-top: 20px;
}

.tips-list {
  margin: 0;
  padding-left: 20px;
}

.tips-list li {
  margin-bottom: 8px;
  color: #606266;
  font-size: 14px;
}

.tips-list li:last-child {
  margin-bottom: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .payment-success-container {
    padding: 15px;
  }

  .page-title {
    font-size: 24px;
  }

  .success-icon-circle {
    width: 80px;
    height: 80px;
  }

  .success-icon {
    font-size: 40px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .payment-success-container {
    padding: 10px;
  }

  .detail-row {
    flex-direction: column;
  }

  .detail-item {
    width: 100%;
  }
}
</style>
