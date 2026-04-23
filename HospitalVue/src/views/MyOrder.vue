<template>
  <div class="my-order-management">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-tickets title-icon"></i>
        <h1 class="page-title">我的挂号记录</h1>
        <p class="page-subtitle">查看和管理您的挂号信息</p>
      </div>
      <div class="decoration-element">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <el-card class="management-card" shadow="hover">
      <!-- 统计概览 -->
      <div class="stats-overview">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
            <div class="stat-card total-stat">
              <div class="stat-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ totalOrders }}</div>
                <div class="stat-label">总挂号数</div>
              </div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
            <div class="stat-card completed-stat">
              <div class="stat-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ completedOrders }}</div>
                <div class="stat-label">已完成</div>
              </div>
            </div>
          </el-col>
          <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
            <div class="stat-card pending-stat">
              <div class="stat-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ pendingPayment }}</div>
                <div class="stat-label">待缴费</div>
              </div>
            </div>
          </el-col>

        </el-row>
      </div>

      <!-- 数据表格 -->
      <div class="table-section">
        <el-table
            :data="orderData"
            stripe
            border
            class="data-table"
            v-loading="loading"
            empty-text="暂无挂号记录"
            style="width: 100%"
        >
          <el-table-column
              prop="oId"
              label="挂号单号"
              width="150"
              align="center"
          >
            <template slot-scope="scope">
              <div class="order-id">
                <span class="id-text">{{ scope.row.oId }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="pName"
              label="患者姓名"
              width="85"
              align="center"
          >
            <template slot-scope="scope">
              <div class="patient-name">
                <span>{{ scope.row.pName }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dName"
              label="医生姓名"
              width="85"
              align="center"
          >
            <template slot-scope="scope">
              <div class="doctor-name">
                <span>{{ scope.row.dName }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="oStart"
              label="挂号时间"
              width="145"
              align="center"
          >
            <template slot-scope="scope">
              <div class="time-display">
                <span class="time-text">{{ formatTime(scope.row.oStart) }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="oEnd"
              label="结束时间"
              width="145"
              align="center"
          >
            <template slot-scope="scope">
              <div class="time-display">
                <span class="time-text">{{ formatTime(scope.row.oEnd) }}</span>
              </div>
            </template>
          </el-table-column>

          <!-- 待缴费金额 -->
          <el-table-column
              prop="oTotalPrice"
              label="待缴费"
              width="90"
              align="center"
          >
            <template slot-scope="scope">
              <div v-if="scope.row.oPriceState === 0" class="price-display">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ scope.row.oTotalPrice || 0 }}</span>
              </div>
              <span v-else class="paid-text">已缴费</span>
            </template>
          </el-table-column>

          <!-- 缴费状态 -->
          <el-table-column
              label="缴费状态"
              width="180"
              align="center"
          >
            <template slot-scope="scope">
              <div class="payment-status">
                <el-tag
                    v-if="scope.row.oPriceState === 1"
                    type="success"
                    class="status-tag paid-tag"
                >
                  <i class="el-icon-success"></i>
                  已缴费
                </el-tag>
                <el-button
                    v-if="scope.row.oPriceState === 0 && scope.row.oState === 1"
                    type="warning"
                    size="mini"
                    @click="priceClick(scope.row.oId, scope.row.dId)"
                    class="payment-btn"
                >
                  <i class="el-icon-money"></i>
                  立即缴费
                </el-button>
                <el-tag
                    v-if="scope.row.oState !== 1"
                    type="info"
                    class="status-tag"
                >
                  <i class="el-icon-time"></i>
                  进行中
                </el-tag>
              </div>
            </template>
          </el-table-column>

          <!-- 挂号状态 -->
          <el-table-column
              label="挂号状态"
              width="180"
              align="center"
          >
            <template slot-scope="scope">
              <el-tag
                  :type="getStatusType(scope.row)"
                  class="status-tag"
              >
                <i :class="getStatusIcon(scope.row)"></i>
                {{ getStatusText(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column
              label="操作"
              width="200"
              align="center"
              fixed="right"
          >
            <template slot-scope="scope">
              <div class="action-btns">
                <!-- 已完成且已缴费的记录显示操作按钮 -->
                <div v-if="scope.row.oState === 1 && scope.row.oPriceState === 1" class="action-buttons">
                  <el-button
                      type="warning"
                      size="mini"
                      @click="showRatingDialog(scope.row)"
                      class="action-btn rating-btn"
                  >
                    <i class="el-icon-star-on"></i>
                    评价
                  </el-button>
                  <el-button
                      type="success"
                      size="mini"
                      @click="seeReport(scope.row.oId)"
                      class="action-btn report-btn"
                  >
                    <i class="el-icon-document"></i>
                    查看报告
                  </el-button>
                </div>
                <!-- 未完成的记录显示横线 -->
                <span v-else class="no-action">-</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 空状态提示 -->
      <div v-if="orderData.length === 0 && !loading" class="empty-state">
        <i class="el-icon-tickets empty-icon"></i>
        <h3>暂无挂号记录</h3>
        <p>您当前没有挂号记录，请先进行挂号</p>
      </div>
    </el-card>

    <!-- 评价对话框 -->
    <el-dialog
        title="医生服务评价"
        :visible.sync="starVisible"
        width="500px"
        class="custom-dialog"
    >
      <div class="dialog-header">
        <i class="el-icon-chat-dot-round dialog-icon"></i>
        <h3>医生服务评价</h3>
      </div>

      <div class="evaluation-content">
        <div class="doctor-info">
          <div class="info-item">
            <span class="info-label">医生工号：</span>
            <span class="info-value">{{ dId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">医生姓名：</span>
            <span class="info-value">{{ dName }}</span>
          </div>
        </div>

        <div class="rating-section">
          <h4 class="rating-title">请对医生的服务进行评价</h4>
          <el-rate
              v-model="star"
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              show-text
              text-color="#ff9900"
              class="rating-stars"
          >
            <template slot-scope="{ score }">
              <span class="rating-text">{{ getRatingText(score) }}</span>
            </template>
          </el-rate>
        </div>

        <div class="rating-tips">
          <i class="el-icon-info"></i>
          <span>您的评价将帮助我们提升服务质量</span>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button
            @click="starVisible = false"
            class="cancel-btn"
        >
          <i class="el-icon-close"></i>
          取消
        </el-button>
        <el-button
            type="primary"
            @click="starClick"
            class="confirm-btn"
            :loading="submitting"
        >
          <i class="el-icon-check"></i>
          提交评价
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";

export default {
  name: "MyOrder",
  data() {
    return {
      loading: false,
      submitting: false,
      userId: 1,
      orderData: [],
      star: 5,
      starVisible: false,
      dId: 1,
      dName: "",
    };
  },
  mounted() {
    this.getUserInfo();
    this.requestOrder();
  },
  computed: {
    // 计算统计信息
    totalOrders() {
      return this.orderData.length;
    },
    completedOrders() {
      return this.orderData.filter((order) =>
          order.oState === 1 && order.oPriceState === 1
      ).length;
    },
    pendingPayment() {
      return this.orderData.filter((order) =>
          order.oPriceState === 0 && order.oState === 1
      ).length;
    },
    totalAmount() {
      // 计算累计缴费金额
      return this.orderData.reduce((total, order) => {
        const cumulative = this.getCumulativeFee(order);
        return total + parseFloat(cumulative || 0);
      }, 0).toFixed(2);
    },
  },
  methods: {
    // 解析累计费用
    getCumulativeFee(order) {
      let total = 0;

      try {
        // 解析药物费用
        if (order.oDrug && order.oDrug !== "") {
          const drugMatches = order.oDrug.match(/药物总价(\d+(?:\.\d+)?)元/g);
          if (drugMatches) {
            drugMatches.forEach((match) => {
              const priceStr = match.replace("药物总价", "").replace("元", "");
              const price = parseFloat(priceStr);
              if (!isNaN(price)) {
                total += price;
              }
            });
          }
        }

        // 解析检查费用
        if (order.oCheck && order.oCheck !== "") {
          const checkMatches = order.oCheck.match(/项目总价(\d+(?:\.\d+)?)元/g);
          if (checkMatches) {
            checkMatches.forEach((match) => {
              const priceStr = match.replace("项目总价", "").replace("元", "");
              const price = parseFloat(priceStr);
              if (!isNaN(price)) {
                total += price;
              }
            });
          }
        }
      } catch (error) {
        console.error("解析累计费用出错:", error);
        total = 0;
      }

      return total.toFixed(2);
    },

    // 格式化时间显示
    formatTime(timeString) {
      if (!timeString) return "--";
      return timeString.replace("T", " ").substring(0, 16);
    },

    // 获取用户信息
    getUserInfo() {
      const token = getToken();
      if (token) {
        try {
          const decoded = jwtDecode(token);
          this.userId = decoded.pId || 1;
        } catch (error) {
          console.error("解析 token 失败:", error);
          this.userId = 1;
        }
      }
    },

    // 获取状态类型
    getStatusType(order) {
      if (order.oState === 1 && order.oPriceState === 1) {
        return "success";
      } else if (order.oState === 1 && order.oPriceState === 0) {
        return "warning";
      } else {
        return "info";
      }
    },

    // 获取状态图标
    getStatusIcon(order) {
      if (order.oState === 1 && order.oPriceState === 1) {
        return "el-icon-success";
      } else if (order.oState === 1 && order.oPriceState === 0) {
        return "el-icon-money";
      } else {
        return "el-icon-time";
      }
    },

    // 获取状态文本
    getStatusText(order) {
      if (order.oState === 1 && order.oPriceState === 1) {
        return "已完成";
      } else if (order.oState === 1 && order.oPriceState === 0) {
        return "待缴费";
      } else {
        return "进行中";
      }
    },

    // 获取评分文本
    getRatingText(score) {
      const texts = {
        1: "非常不满意",
        2: "不满意",
        3: "一般",
        4: "满意",
        5: "非常满意",
      };
      return texts[score] || "请评分";
    },

    // 评价点击确认
    starClick() {
      this.submitting = true;
      request
          .get("doctor/updateStar", {
            params: {
              dId: this.dId,
              dStar: this.star,
            },
          })
          .then((res) => {
            this.submitting = false;
            if (res.data.status !== 200) {
              this.$message.error("评价失败，请重试");
              return;
            }
            this.$message.success("感谢您的评价！");
            this.starVisible = false;
            this.star = 5; // 重置评分
          })
          .catch(() => {
            this.submitting = false;
            this.$message.error("网络错误，评价失败");
          });
    },

    // 查看报告单
    seeReport(id) {
      this.$message.info("正在生成报告单，请稍候...");
      window.open(
          "http://localhost:9999/patient/pdf?oId=" + id,
          "_blank"
      );
    },

    // 点击缴费按钮
    priceClick(oId, dId) {
      this.$confirm("确认要支付该笔费用吗？", "缴费确认", {
        confirmButtonText: "去支付",
        cancelButtonText: "取消",
        type: "warning",
        confirmButtonClass: "el-button--warning",
      })
          .then(() => {
            this.loading = true;

            request
                .get("payment/create", {
                  params: {
                    oId: oId,
                  },
                })
                .then((res) => {
                  this.loading = false;
                  if (res.data.status !== 200) {
                    this.$message.error(
                        "创建支付订单失败：" + (res.data.msg || "未知错误")
                    );
                    return;
                  }

                  // 获取支付表单HTML
                  const paymentForm = res.data.data;

                  if (paymentForm) {
                    // 创建一个隐藏的表单容器
                    const formDiv = document.createElement("div");
                    formDiv.innerHTML = paymentForm;
                    document.body.appendChild(formDiv);

                    // 提交表单跳转到支付宝支付页面
                    const form = formDiv.querySelector("form");
                    if (form) {
                      form.submit();
                    } else {
                      this.$message.error("支付表单生成失败");
                      document.body.removeChild(formDiv);
                    }
                  } else {
                    this.$message.error("支付表单为空");
                  }
                })
                .catch((error) => {
                  this.loading = false;
                  this.$message.error(
                      "网络错误：" + (error.message || "未知错误")
                  );
                });
          })
          .catch(() => {
            this.$message.info("已取消支付");
          });
    },

    // 请求挂号信息
    requestOrder() {
      this.loading = true;
      request
          .get("patient/findOrderByPid", {
            params: {
              pId: this.userId,
            },
          })
          .then((res) => {
            this.loading = false;
            if (res.data.status !== 200) {
              this.$message.error("数据加载失败");
              return;
            }
            this.orderData = res.data.data || [];
          })
          .catch(() => {
            this.loading = false;
            this.$message.error("网络错误，数据加载失败");
          });
    },

    // 刷新订单列表
    refreshOrderList() {
      this.loading = true;
      request
          .get("patient/findOrderByPid", {
            params: {
              pId: this.userId,
            },
          })
          .then((res) => {
            this.loading = false;
            if (res.data.status === 200) {
              this.orderData = res.data.data || [];
              this.$message.success("订单列表已更新");
            } else {
              this.$message.error("获取订单列表失败");
            }
          })
          .catch(() => {
            this.loading = false;
            this.$message.error("网络错误，获取订单列表失败");
          });
    },

    // 显示评价对话框
    showRatingDialog(order) {
      this.dId = order.dId;
      this.dName = order.dName;
      this.starVisible = true;
      this.star = 5;
    },

    // token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
  },
  created() {
    this.userId = this.tokenDecode(getToken()).pId || 1;
    this.requestOrder();

    // 检查是否是从支付成功页面返回的
    const paid = this.$route.query.paid;
    const oId = this.$route.query.oId;

    if (paid === "true" && oId) {
      console.log("检测到支付成功，订单ID：" + oId);

      // 延迟1秒刷新订单列表，确保数据库已更新
      setTimeout(() => {
        this.refreshOrderList();
        // 显示支付成功提示
        this.$message.success("订单 " + oId + " 支付成功！");
      }, 1000);
    }
  },
};
</script>

<style scoped>
.my-order-management {
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
  min-height: 600px;
}

.management-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 统计概览 */
.stats-overview {
  padding: 20px;
  background: #f8fafc;
  border-bottom: 1px solid #e6e8eb;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100px;
  margin-bottom: 10px;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 28px;
  color: white;
}

.total-stat .stat-icon {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
}

.completed-stat .stat-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.pending-stat .stat-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.amount-stat .stat-icon {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1f2d3d;
  margin-bottom: 5px;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  line-height: 1.2;
}

/* 表格样式 */
.table-section {
  padding: 20px;
  width: 100%;
  overflow-x: auto;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
  margin: 0;
  width: 100%;
  min-width: 950px;
  font-size: 13px;
}

/* 确保表格列宽固定 */
.data-table >>> .el-table__header-wrapper th {
  background-color: #f8fafc;
  color: #1f2d3d;
  font-weight: 600;
  padding: 12px 0;
  font-size: 13px;
  white-space: nowrap;
}

.data-table >>> .el-table__body-wrapper td {
  padding: 10px 0;
  font-size: 13px;
  white-space: nowrap;
}

/* 优化表头样式，使表格更紧凑 */
.data-table >>> .el-table__header th {
  padding: 10px 0;
  background-color: #f8fafc;
  border-bottom: 1px solid #ebeef5;
}

.data-table >>> .el-table__body td {
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.order-id {
  display: flex;
  align-items: center;
  justify-content: center;
}

.id-text {
  font-weight: 600;
  color: #409eff;
  font-size: 13px;
}

.patient-name,
.doctor-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.patient-name span,
.doctor-name span {
  font-size: 13px;
  color: #303133;
  font-weight: 500;
}

.time-display {
  display: flex;
  align-items: center;
  justify-content: center;
}

.time-text {
  color: #606266;
  font-size: 13px;
}

.price-display {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 2px;
}

.price-symbol {
  font-size: 12px;
  color: #f56c6c;
  font-weight: 600;
}

.price-value {
  font-size: 14px;
  font-weight: 700;
  color: #f56c6c;
}

.paid-text {
  color: #67c23a;
  font-size: 13px;
  font-weight: 500;
}

.payment-status {
  display: flex;
  justify-content: center;
  align-items: center;
}

.status-tag {
  border-radius: 12px;
  font-weight: 500;
  padding: 5px 10px;
  font-size: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  line-height: 1.2;
  border: none;
  height: 28px;
}

.paid-tag {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: white;
}

.status-tag.info {
  background: linear-gradient(135deg, #909399 0%, #a6a9ad 100%);
  color: white;
}

.status-tag.warning {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
  color: white;
}

.payment-btn {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
  border: none;
  color: white;
  font-weight: 500;
  padding: 5px 12px;
  font-size: 12px;
  height: 28px;
  line-height: 1.2;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.payment-btn:hover:not(.is-disabled) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

.action-btns {
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-buttons {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.action-btn {
  border-radius: 10px;
  font-weight: 500;
  padding: 6px 12px;
  font-size: 12px;
  height: 30px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  line-height: 1.2;
  margin: 0;
  border: none;
  min-width: 80px;
}

.action-btn:hover:not(.is-disabled) {
  transform: translateY(-1px);
}

.rating-btn {
  background: linear-gradient(135deg, #f7ba2a 0%, #ff9900 100%);
  color: white;
}

.rating-btn:hover:not(.is-disabled) {
  box-shadow: 0 2px 8px rgba(247, 186, 42, 0.3);
}

.report-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: white;
}

.report-btn:hover:not(.is-disabled) {
  box-shadow: 0 2px 8px rgba(103, 194, 58, 0.3);
}

.no-action {
  color: #c0c4cc;
  font-size: 13px;
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
.custom-dialog >>> .el-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.custom-dialog >>> .el-dialog__header {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  padding: 20px;
}

.custom-dialog >>> .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.custom-dialog >>> .el-dialog__body {
  padding: 30px;
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

.evaluation-content {
  text-align: center;
}

.doctor-info {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 30px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 8px;
}

.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.info-label {
  font-size: 14px;
  color: #909399;
}

.info-value {
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
}

.rating-section {
  margin-bottom: 20px;
}

.rating-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 20px 0;
}

.rating-stars {
  margin-bottom: 10px;
}

.rating-text {
  font-size: 14px;
  color: #ff9900;
  font-weight: 500;
}

.rating-tips {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px;
  background: #f0f9ff;
  border-radius: 4px;
  color: #409eff;
  font-size: 14px;
}

.dialog-footer {
  text-align: right;
  padding: 20px 0 0;
}

.cancel-btn,
.confirm-btn {
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
}

.confirm-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .table-section {
    overflow-x: auto;
  }

  .data-table {
    min-width: 950px;
  }
}

@media (max-width: 768px) {
  .my-order-management {
    padding: 10px;
  }

  .header-section {
    flex-direction: column;
    text-align: center;
  }

  .stats-overview {
    padding: 15px;
  }

  .stat-card {
    flex-direction: row;
    text-align: left;
    padding: 15px;
    height: auto;
    margin-bottom: 10px;
  }

  .stat-icon {
    margin-right: 15px;
    margin-bottom: 0;
    width: 50px;
    height: 50px;
    font-size: 24px;
  }

  .table-section {
    padding: 10px;
  }

  .data-table {
    min-width: 950px;
    font-size: 12px;
  }

  .action-buttons {
    flex-direction: row;
    gap: 8px;
  }

  .action-btn {
    padding: 5px 10px;
    font-size: 11px;
    min-width: 70px;
    height: 28px;
  }

  .doctor-info {
    flex-direction: column;
    gap: 15px;
  }
}

@media (max-width: 576px) {
  .stat-card {
    flex-direction: column;
    text-align: center;
  }

  .stat-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }

  .stat-number {
    font-size: 20px;
  }

  .stat-label {
    font-size: 12px;
  }

  .data-table {
    min-width: 950px;
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

.stat-card {
  animation: fadeInUp 0.5s ease forwards;
}

.stat-card:nth-child(1) {
  animation-delay: 0.1s;
}

.stat-card:nth-child(2) {
  animation-delay: 0.2s;
}

.stat-card:nth-child(3) {
  animation-delay: 0.3s;
}

.stat-card:nth-child(4) {
  animation-delay: 0.4s;
}

.data-table {
  animation: fadeInUp 0.6s ease forwards;
}
</style>
