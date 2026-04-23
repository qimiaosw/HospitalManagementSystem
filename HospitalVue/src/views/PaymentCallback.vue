<template>
  <div class="payment-callback">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="6" animated />
    </div>

    <div v-else-if="paymentData" class="result-container">
      <!-- 支付成功 -->
      <div v-if="paymentData.success" class="success-card">
        <div class="success-icon">✓</div>
        <h2>支付成功！</h2>

        <div class="payment-info">
          <div class="info-item">
            <span class="label">订单号：</span>
            <span class="value">{{ paymentData.outTradeNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">支付宝交易号：</span>
            <span class="value">{{ paymentData.tradeNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">支付金额：</span>
            <span class="value amount">¥{{ paymentData.totalAmount }}</span>
          </div>
          <div class="info-item">
            <span class="label">支付时间：</span>
            <span class="value">{{ paymentData.timestamp }}</span>
          </div>
        </div>

        <div class="actions">
          <el-button type="primary" @click="goToOrderDetail">查看订单详情</el-button>
          <el-button @click="goToHome">返回首页</el-button>
        </div>
      </div>

      <!-- 支付失败 -->
      <div v-else class="error-card">
        <div class="error-icon">✗</div>
        <h2>支付处理失败</h2>
        <p class="error-message">{{ paymentData.message || '支付验证失败，请稍后重试' }}</p>

        <div class="actions">
          <el-button type="primary" @click="retryPayment">重新支付</el-button>
          <el-button @click="goToOrders">查看订单</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PaymentCallback',
  data() {
    return {
      loading: true,
      paymentData: null
    };
  },
  mounted() {
    this.handlePaymentCallback();
  },
  methods: {
    async handlePaymentCallback() {
      try {
        // 1. 首先检查URL参数
        const params = this.$route.query;

        if (params.out_trade_no) {
          console.log('从URL获取支付宝参数:', params);
          await this.verifyPayment(params);
          return;
        }

        // 2. 如果没有URL参数，检查sessionStorage
        const storedParams = sessionStorage.getItem('alipayCallbackParams');
        if (storedParams) {
          const alipayParams = JSON.parse(storedParams);
          console.log('从sessionStorage获取支付宝参数:', alipayParams);
          await this.verifyPayment(alipayParams);

          // 清理sessionStorage
          sessionStorage.removeItem('alipayCallbackParams');
          return;
        }

        // 3. 都没有找到参数，显示错误
        this.paymentData = {
          success: false,
          message: '未找到支付信息'
        };

      } catch (error) {
        console.error('支付回调处理失败:', error);
        this.paymentData = {
          success: false,
          message: '处理支付结果时发生错误'
        };
      } finally {
        this.loading = false;
      }
    },

    async verifyPayment(params) {
      try {
        const response = await this.$axios.post('/api/payment/verify', {
          outTradeNo: params.out_trade_no,
          tradeNo: params.trade_no,
          totalAmount: params.total_amount,
          sign: params.sign
        });

        if (response.data.success) {
          // 获取当前时间作为支付时间
          const currentTime = new Date().toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
          });

          this.paymentData = {
            success: true,
            outTradeNo: params.out_trade_no,
            tradeNo: params.trade_no,
            totalAmount: params.total_amount,
            timestamp: currentTime, // 直接使用格式化的当前时间
            ...response.data.data
          };
        } else {
          this.paymentData = {
            success: false,
            message: response.data.message || '支付验证失败',
            outTradeNo: params.out_trade_no
          };
        }
      } catch (error) {
        console.error('验证支付失败:', error);
        this.paymentData = {
          success: false,
          message: '网络错误，请检查支付状态',
          outTradeNo: params.out_trade_no
        };
      }
    },

    goToOrderDetail() {
      if (this.paymentData && this.paymentData.outTradeNo) {
        this.$router.push({
          path: '/order/detail',
          query: { orderNo: this.paymentData.outTradeNo }
        });
      } else {
        this.$router.push('/orders');
      }
    },

    goToHome() {
      this.$router.push('/');
    },

    goToOrders() {
      this.$router.push('/orders');
    },

    retryPayment() {
      if (this.paymentData && this.paymentData.outTradeNo) {
        this.$router.push({
          path: '/payment',
          query: { orderNo: this.paymentData.outTradeNo }
        });
      } else {
        this.$router.push('/payment');
      }
    }
  }
};
</script>

<style scoped>
.payment-callback {
  min-height: 60vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.loading-container {
  width: 100%;
  max-width: 600px;
}

.success-card,
.error-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 500px;
  width: 100%;
}

.success-icon {
  font-size: 60px;
  color: #52c41a;
  margin-bottom: 20px;
}

.error-icon {
  font-size: 60px;
  color: #f5222d;
  margin-bottom: 20px;
}

h2 {
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
}

.payment-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin: 20px 0;
  text-align: left;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  padding: 5px 0;
  border-bottom: 1px solid #eee;
}

.info-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.label {
  color: #666;
  font-weight: 500;
}

.value {
  color: #333;
  font-weight: 600;
}

.amount {
  color: #1890ff;
  font-size: 18px;
}

.error-message {
  color: #f5222d;
  background: #fff1f0;
  padding: 15px;
  border-radius: 6px;
  margin: 20px 0;
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
  justify-content: center;
}

.actions .el-button {
  min-width: 120px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .success-card,
  .error-card {
    padding: 20px;
  }

  .actions {
    flex-direction: column;
  }

  .actions .el-button {
    width: 100%;
  }
}
</style>