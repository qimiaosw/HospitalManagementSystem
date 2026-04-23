<template>
  <div class="payment-fail">
    <div class="fail-container">
      <div class="fail-icon">✗</div>
      <h2>支付失败</h2>
      <p class="fail-message">{{ failMessage }}</p>

      <div class="fail-info" v-if="failReason">
        <h3>失败原因：</h3>
        <p>{{ failReason }}</p>
      </div>

      <div class="actions">
        <el-button type="primary" @click="retryPayment">重新支付</el-button>
        <el-button @click="goToOrders">查看订单</el-button>
        <el-button @click="goToHome">返回首页</el-button>
      </div>

      <div class="contact-info">
        <p>如您已扣款但显示支付失败，请保留支付凭证并联系客服：</p>
        <p>客服电话：400-8888-9999</p>
        <p>客服邮箱：support@hospital.com</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaymentFail",
  data() {
    return {
      failMessage: "支付处理失败，请稍后重试",
      failReason: ""
    };
  },
  mounted() {
    // 从路由参数获取失败信息
    const params = this.$route.query;
    if (params.message) {
      this.failMessage = decodeURIComponent(params.message);
    }
    if (params.reason) {
      this.failReason = decodeURIComponent(params.reason);
    }

    // 从sessionStorage获取失败信息
    const failData = sessionStorage.getItem("paymentFailData");
    if (failData) {
      try {
        const data = JSON.parse(failData);
        this.failMessage = data.message || this.failMessage;
        this.failReason = data.reason || this.failReason;
        sessionStorage.removeItem("paymentFailData");
      } catch (error) {
        console.error("解析失败数据出错:", error);
      }
    }
  },
  methods: {
    retryPayment() {
      const orderNo = this.$route.query.orderNo;
      if (orderNo) {
        this.$router.push({
          path: "/orderOperate",
          query: { orderNo: orderNo }
        });
      } else {
        this.$router.push("/orderOperate");
      }
    },

    goToOrders() {
      this.$router.push("/myOrder");
    },

    goToHome() {
      const userRole = localStorage.getItem("userRole") || "patient";
      if (userRole === "admin") {
        this.$router.push("/adminLayout");
      } else if (userRole === "doctor") {
        this.$router.push("/doctorLayout");
      } else {
        this.$router.push("/patientLayout");
      }
    }
  }
};
</script>

<style scoped>
.payment-fail {
  min-height: 60vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background: #f5f7fa;
}

.fail-container {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 500px;
  width: 100%;
}

.fail-icon {
  font-size: 60px;
  color: #f5222d;
  margin-bottom: 20px;
}

h2 {
  color: #333;
  margin-bottom: 20px;
  font-size: 24px;
}

.fail-message {
  color: #f5222d;
  font-size: 16px;
  margin-bottom: 30px;
  padding: 15px;
  background: #fff1f0;
  border-radius: 6px;
}

.fail-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin: 20px 0;
  text-align: left;
}

.fail-info h3 {
  color: #666;
  margin-bottom: 10px;
}

.fail-info p {
  color: #333;
  margin: 0;
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.contact-info {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  color: #666;
  font-size: 14px;
}

.contact-info p {
  margin: 5px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .fail-container {
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