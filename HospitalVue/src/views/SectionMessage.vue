<template>
  <div class="doctor-schedule-management">
    <!-- 顶部标题区域 -->
    <div class="schedule-header-section">
      <div class="schedule-title-content">
        <i class="el-icon-date schedule-title-icon"></i>
        <h1 class="schedule-page-title">医生排班预约</h1>
        <p class="schedule-page-subtitle">选择日期并预约医生门诊</p>
      </div>
      <div class="schedule-decoration-element">
        <div class="schedule-circle schedule-circle-1"></div>
        <div class="schedule-circle schedule-circle-2"></div>
        <div class="schedule-circle schedule-circle-3"></div>
      </div>
    </div>

    <el-card class="schedule-management-card" shadow="hover">
      <!-- 面包屑导航 -->
      <div class="schedule-breadcrumb-section">
        <el-breadcrumb separator-class="el-icon-arrow-right" class="custom-breadcrumb">
          <el-breadcrumb-item :to="{ path: '/orderOperate' }">
            <i class="el-icon-s-home"></i>
            科室选择
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <i class="el-icon-date"></i>
            日期选择
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <i class="el-icon-tickets"></i>
            挂号预约
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 科室信息和日期选择 -->
      <div class="schedule-info-section">
        <div class="department-info-card">
          <div class="department-icon">
            <i class="el-icon-office-building"></i>
          </div>
          <div class="department-content">
            <h3 class="department-title">{{ sectionOpt }}</h3>
            <p class="department-subtitle">医生列表</p>
          </div>
          <div class="doctor-count">
            <span class="count-number">{{ sectionData.length }}</span>
            <span class="count-label">位医生</span>
          </div>
        </div>

        <div class="date-selection-card">
          <div class="date-header">
            <i class="el-icon-calendar date-icon"></i>
            <h4 class="date-title">请选择就诊日期</h4>
          </div>
          <div class="date-grid">
            <div
                v-for="monthDay in monthDays"
                :key="monthDay"
                :class="['date-card', isSelectedDate(monthDay) ? 'date-active' : '']"
                @click="dateClick(monthDay)"
            >
              <div class="date-content">
                <div class="date-month">{{ getMonth(monthDay) }}月</div>
                <div class="date-day">{{ getDay(monthDay) }}</div>
                <div class="date-week">{{ getWeekday(monthDay) }}</div>
              </div>
              <div class="date-indicator" v-if="isSelectedDate(monthDay)">
                <i class="el-icon-check"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 医生表格 -->
      <div class="doctor-table-section" v-if="clickTag">
        <div class="table-header">
          <i class="el-icon-user-solid table-icon"></i>
          <h3 class="table-title">可预约医生列表</h3>
          <span class="table-subtitle">选择日期：{{ selectedDateFormatted }}</span>
        </div>

        <el-table
            :data="sectionData"
            stripe
            border
            class="doctor-data-table"
            v-loading="loading"
            empty-text="该日期暂无医生排班"
        >
          <el-table-column
              type="index"
              label="序号"
              width="80"
              align="center"
          >
            <template slot-scope="scope">
              <div class="index-badge">
                {{ scope.$index + 1 }}
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dId"
              label="工号"
              width="100"
              align="center"
          >
            <template slot-scope="scope">
              <div class="id-display">
                <i class="el-icon-s-custom id-icon"></i>
                <span>{{ scope.row.dId || '--' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dName"
              label="姓名"
              width="100"
              align="center"
          >
            <template slot-scope="scope">
              <div class="name-display">
                <i class="el-icon-user name-icon"></i>
                <span class="name-text">{{ scope.row.dName || '--' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dGender"
              label="性别"
              width="80"
              align="center"
          >
            <template slot-scope="scope">
              <el-tag
                  :type="getGenderType(scope.row.dGender)"
                  class="gender-tag"
              >
                <i :class="getGenderIcon(scope.row.dGender)"></i>
                {{ scope.row.dGender || '--' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column
              prop="dPost"
              label="职位"
              width="120"
              align="center"
          >
            <template slot-scope="scope">
              <span class="post-text">{{ scope.row.dPost || '--' }}</span>
            </template>
          </el-table-column>

          <el-table-column
              prop="dSection"
              label="科室"
              width="120"
              align="center"
          >
            <template slot-scope="scope">
              <span class="section-text">{{ scope.row.dSection || '--' }}</span>
            </template>
          </el-table-column>

          <el-table-column
              prop="dIntroduction"
              label="医生简介"
              min-width="200"
          >
            <template slot-scope="scope">
              <div class="intro-display">
                <i class="el-icon-document intro-icon"></i>
                <span class="intro-text">{{ scope.row.dIntroduction || '暂无简介' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dPrice"
              label="挂号费"
              width="100"
              align="center"
          >
            <template slot-scope="scope">
              <div class="price-display">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ scope.row.dPrice || '--' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              prop="dAvgStar"
              label="评分"
              width="100"
              align="center"
          >
            <template slot-scope="scope">
              <div class="rating-display">
                <el-rate
                    v-model="scope.row.dAvgStar"
                    disabled
                    show-score
                    text-color="#ff9900"
                    score-template="{value}"
                    class="rating-stars"
                ></el-rate>
              </div>
            </template>
          </el-table-column>

          <el-table-column
              label="操作"
              width="120"
              fixed="right"
              align="center"
          >
            <template slot-scope="scope">
              <el-button
                  type="warning"
                  size="small"
                  @click="openClick(scope.row.dId, scope.row.dName)"
                  class="appointment-btn"
                  :loading="scope.row.loading"
              >
                <i class="el-icon-tickets"></i>
                预约挂号
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 空状态提示 -->
      <div v-if="!clickTag" class="schedule-empty-state">
        <div class="empty-content">
          <i class="el-icon-date empty-icon"></i>
          <h3>请选择就诊日期</h3>
          <p>选择上方日期查看医生排班信息</p>
        </div>
      </div>

      <!-- 挂号对话框 -->
      <el-dialog
          title="门诊预约挂号"
          :visible.sync="orderFormVisible"
          width="500px"
          class="appointment-dialog"
      >
        <div class="dialog-header">
          <i class="el-icon-tickets dialog-icon"></i>
          <h3>填写挂号信息</h3>
        </div>

        <el-form
            :model="orderForm"
            ref="orderForm"
            :rules="orderRules"
            class="appointment-form"
        >
          <div class="form-section">
            <h4 class="section-title">就诊信息</h4>
            <el-form-item
                label="挂号时间段"
                label-width="100px"
                prop="oTime"
            >
              <el-select
                  v-model="orderForm.oTime"
                  placeholder="请选择就诊时间段"
                  class="time-select"
                  no-data-text="该时间段暂无号源"
              >
                <el-option
                    v-for="time in times"
                    :key="time"
                    :label="time"
                    :value="time"
                    class="time-option"
                >
                  <div class="time-option-content">
                    <span class="time-slot">{{ getTimeSlot(time) }}</span>
                    <span class="time-remaining">{{ getRemaining(time) }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
          </div>

          <div class="form-section">
            <h4 class="section-title">基本信息</h4>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="就诊日期">
                  <el-input
                      v-model="orderForm.orderDate"
                      disabled
                      class="disabled-input"
                  >
                    <i slot="prefix" class="el-icon-date"></i>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="医生工号">
                  <el-input
                      v-model="orderForm.dId"
                      disabled
                      class="disabled-input"
                  >
                    <i slot="prefix" class="el-icon-s-custom"></i>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="医生姓名">
                  <el-input
                      v-model="orderForm.dName"
                      disabled
                      class="disabled-input"
                  >
                    <i slot="prefix" class="el-icon-user"></i>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="患者姓名">
                  <el-input
                      v-model="orderForm.pName"
                      disabled
                      class="disabled-input"
                  >
                    <i slot="prefix" class="el-icon-user-solid"></i>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="身份证号">
              <el-input
                  v-model="orderForm.pCard"
                  disabled
                  class="disabled-input"
              >
                <i slot="prefix" class="el-icon-credit-card"></i>
              </el-input>
            </el-form-item>
          </div>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button
              @click="orderFormVisible = false"
              class="cancel-btn"
          >
            <i class="el-icon-close"></i>
            取消
          </el-button>
          <el-button
              type="primary"
              @click="orderSuccess"
              :loading="submitting"
              class="confirm-btn"
          >
            <i class="el-icon-check"></i>
            确认预约
          </el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import request from "@/utils/request.js";
import { getToken } from "@/utils/storage.js";

export default {
  name: "sectionMessage",
  data() {
    return {
      loading: false,
      submitting: false,
      sectionOpt: this.$route.query.sectionOpt,
      sectionData: [],
      monthDays: [],
      clickTag: false,
      orderFormVisible: false,
      selectedDate: "",
      orderForm: {
        orderDate: "",
        dId: "",
        dName: "",
        pName: "",
        pCard: "",
        pId: "",
        oTime: "",
        dPrice:0,
      },
      times: [],
      orderRules: {
        oTime: [
          { required: true, message: "请选择就诊时间段", trigger: "change" },
        ],
      },
      idTime: "",
    };
  },
  computed: {
    // 格式化选中日期显示
    selectedDateFormatted() {
      if (!this.selectedDate) return '';
      const now = new Date();
      const year = now.getFullYear();
      return `${year}-${this.selectedDate}`;
    }
  },
  methods: {
    // 获取医生信息（获取挂号费）
    async fetchDoctorPrice(dId) {
      try {
        const response = await request.get("doctor/findDoctorById", {
          params: { dId }
        });
        if (response.data.status === 200) {
          this.dPrice = response.data.data.dPrice || 0;
          console.log('医生挂号费:', this.dPrice);
        }
      } catch (error) {
        console.error('获取医生信息失败:', error);
      }
    },

    // 获取月份
    getMonth(dateStr) {
      return dateStr.split('-')[0];
    },

    // 获取日期
    getDay(dateStr) {
      return dateStr.split('-')[1];
    },

    // 获取星期几
    getWeekday(dateStr) {
      const now = new Date();
      const year = now.getFullYear();
      const date = new Date(`${year}-${dateStr}`);
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      return weekdays[date.getDay()];
    },

    // 检查是否为选中日期
    isSelectedDate(dateStr) {
      return this.selectedDate === dateStr;
    },

    // 获取时间段
    getTimeSlot(timeStr) {
      return timeStr.split('    余号 ')[0];
    },

    // 获取剩余号数
    getRemaining(timeStr) {
      return timeStr.split('余号 ')[1] || '';
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

    // 生成22位的arId（适配Redis键名）
    generateArId(doctorId, dateStr) {
      try {
        // 清理日期格式：移除所有非数字字符
        const cleanDate = dateStr.replace(/[^\d]/g, '');
        const doctorIdStr = doctorId.toString();
        const baseStr = doctorIdStr + cleanDate;

        console.log('生成arId参数:', { doctorId, dateStr, cleanDate, baseStr });

        // 确保长度22位
        if (baseStr.length < 22) {
          const padding = '0'.repeat(22 - baseStr.length);
          const result = baseStr + padding;
          console.log('填充后arId:', result, '长度:', result.length);
          return result;
        } else if (baseStr.length > 22) {
          const result = baseStr.substring(0, 22);
          console.log('截取后arId:', result, '长度:', result.length);
          return result;
        }

        console.log('原始arId:', baseStr, '长度:', baseStr.length);
        return baseStr;
      } catch (error) {
        console.error('生成arId失败:', error);
        return '0000000000000000000000';
      }
    },

    // 检查当前时间是否超过指定时间
    isTimeAfterTarget(timeString) {
      try {
        const currentTime = new Date();
        const [targetHour, targetMinute] = timeString.split(":").map(Number);
        const targetTime = new Date();
        targetTime.setHours(targetHour, targetMinute, 0, 0);
        return currentTime > targetTime;
      } catch (error) {
        console.error('时间比较错误:', error);
        return false;
      }
    },

    // 打开挂号对话框触发，获取Redis中的余号信息
    async requestTime(id) {
      // 确保日期已设置
      if (!this.orderForm.orderDate) {
        this.$message.error("请先选择日期");
        return;
      }

      console.log('生成arId，医生ID:', id, '日期:', this.orderForm.orderDate);

      // 生成arId
      this.idTime = this.generateArId(id, this.orderForm.orderDate);
      console.log('生成的arId:', this.idTime);

      this.loading = true;
      try {
        const res = await request.get("order/findOrderTime", {
          params: {
            arId: this.idTime,
          },
        });

        this.loading = false;
        const date = new Date(this.orderForm.orderDate);
        const today = new Date();
        const isToday =
            date.getFullYear() === today.getFullYear() &&
            date.getMonth() === today.getMonth() &&
            date.getDate() === today.getDate();

        let array = [];
        const timeSlots = res.data.data || {};

        // 上午时间段
        if (!this.isTimeAfterTarget("09:30") || !isToday) {
          array.push(
              "08:30-09:30    余号 " + (timeSlots.eTOn || 0)
          );
        }
        if (!this.isTimeAfterTarget("10:30") || !isToday) {
          array.push(
              "09:30-10:30    余号 " + (timeSlots.nTOt || 0)
          );
        }
        if (!this.isTimeAfterTarget("11:30") || !isToday) {
          array.push(
              "10:30-11:30    余号 " + (timeSlots.tTOe || 0)
          );
        }

        // 下午时间段
        if (!this.isTimeAfterTarget("15:30") || !isToday) {
          array.push(
              "14:30-15:30    余号 " + (timeSlots.fTOf || 0)
          );
        }
        if (!this.isTimeAfterTarget("16:30") || !isToday) {
          array.push(
              "15:30-16:30    余号 " + (timeSlots.fTOs || 0)
          );
        }
        if (!this.isTimeAfterTarget("17:30") || !isToday) {
          array.push(
              "16:30-17:30    余号 " + (timeSlots.sTOs || 0)
          );

        }


        this.times = array;
        console.log('可用时间段:', this.times);
      } catch (error) {
        this.loading = false;
        console.error('获取时间段失败:', error);
        this.$message.error("获取可预约时间段失败");
      }
    },

    // 格式化时间，确保符合后端要求
    formatTimeForBackend(dateStr, timeSlot) {
      try {
        // 提取开始时间（如 "08:30-09:30" -> "08:30"）
        const startTime = timeSlot.split('-')[0].trim();
        // 格式化为 yyyy-MM-dd HH:mm:00.000
        return `${dateStr} ${startTime}:00.000`;
      } catch (error) {
        console.error('格式化时间失败:', error);
        return `${dateStr} 00:00:00.000`;
      }
    },

    // 挂号点击确认
// 挂号点击确认
    async orderSuccess() {
      try {
        // 1. 表单验证
        await this.$refs.orderForm.validate();

        if (!this.orderForm.oTime) {
          this.$message.error("请选择就诊时间段");
          return;
        }

        // 2. 构建就诊时间
        const visitTime = this.formatTimeForBackend(this.orderForm.orderDate, this.orderForm.oTime);
        console.log('格式化后的就诊时间:', visitTime);

        // 3. 获取选择的时间段和余号信息
        const timeParts = this.orderForm.oTime.split('    余号 ');
        const selectedTimeSlot = timeParts[0];
        const remainingCount = timeParts[1] ? parseInt(timeParts[1]) : 0;

        console.log('选择的时间段:', selectedTimeSlot);
        console.log('当前余号:', remainingCount);

        // 4. 检查余号是否充足
        if (remainingCount <= 0) {
          this.$message.error("该时间段已无余号，请选择其他时间段");
          return;
        }

        // 5. 根据选择的时间段确定Redis字段
        let timeField = '';
        switch(selectedTimeSlot) {
          case '08:30-09:30':
            timeField = 'eTOn';
            break;
          case '09:30-10:30':
            timeField = 'nTOt';
            break;
          case '10:30-11:30':
            timeField = 'tTOe';
            break;
          case '14:30-15:30':
            timeField = 'fTOf';
            break;
          case '15:30-16:30':
            timeField = 'fTOs';
            break;
          case '16:30-17:30':
            timeField = 'sTOs';
            break;
          default:
            this.$message.error("无效的时间段");
            return;
        }

        // 6. 构建请求参数，包括时间段字段
        const params = {
          dId: this.orderForm.dId,
          pId: this.orderForm.pId,
          oStart: visitTime,
          arId: this.idTime,
          timeField: timeField,  // 添加时间段字段
          selectedTime: selectedTimeSlot,  // 添加选择的时间段
          oState: 0,
          oPriceState: 0,
          oTotalPrice:this.dPrice||0
        };

        // 7. 参数验证
        if (!params.dId || !params.pId || !params.oStart) {
          this.$message.error("缺少必要参数");
          return;
        }

        console.log('挂号请求参数详情:');
        console.log('- dId:', params.dId);
        console.log('- pId:', params.pId);
        console.log('- oStart:', params.oStart);
        console.log('- arId:', params.arId);
        console.log('- timeField:', timeField);
        console.log('- selectedTime:', selectedTimeSlot);
        console.log('- 当前余号:', remainingCount);

        // 8. 显示加载状态
        this.submitting = true;

        // 9. 发送挂号请求（后端会处理Redis更新）
        const response = await request.get("patient/addOrder", {
          params: params
        });

        console.log('挂号响应:', response.data);

        // 10. 处理响应
        this.submitting = false;

        if (response.data && response.data.status === 200) {
          // 11. 成功处理
          this.orderFormVisible = false;
          this.$message({
            message: "挂号成功！",
            type: "success",
            duration: 3000,
            showClose: true
          });

          // 12. 清空表单
          this.orderForm.oTime = '';

          // 13. 刷新医生列表数据
          this.dateClick(this.selectedDate);

        } else {
          // 失败
          const errorMsg = (response.data && response.data.msg) || (response.data && response.data.message) || "挂号失败";
          this.$message.error(errorMsg);
        }

      } catch (error) {
        this.submitting = false;
        console.error('挂号失败:', error);

        if (error.response) {
          if (error.response.status === 500) {
            if (error.response.data && error.response.data.message && error.response.data.message.includes('out of bounds')) {
              this.$message.error("参数格式错误，请检查日期和时间格式");
            } else {
              this.$message.error("服务器内部错误，请联系管理员");
            }
          } else {
            this.$message.error(`请求失败: ${error.response.status}`);
          }
        } else if (error.request) {
          this.$message.error("网络连接失败，请检查网络连接");
        } else {
          this.$message.error(`请求失败: ${error.message}`);
        }
      }
    },

// 移除 updateRedisRemaining 和 rollbackRedisRemaining 方法 // 即使更新失败也不影响主流程，只是记录日志


    // token解码
    tokenDecode(token) {
      try {
        if (token) {
          return jwtDecode(token);
        }
      } catch (error) {
        console.error('Token解码失败:', error);
      }
      return {};
    },

    // 打开挂号对话框
    openClick(id, name) {
      this.orderForm.dId = id;
      this.orderForm.dName = name;
      this.orderFormVisible = true;

      // 设置日期
      if (!this.orderForm.orderDate && this.selectedDate) {
        const now = new Date();
        const year = now.getFullYear();
        this.orderForm.orderDate = `${year}-${this.selectedDate}`;
      }

      console.log('打开挂号对话框:');
      console.log('- 医生ID:', id);
      console.log('- 医生姓名:', name);
      console.log('- 患者ID:', this.orderForm.pId);
      console.log('- 就诊日期:', this.orderForm.orderDate);

      // 获取Redis中的余号信息
      this.requestTime(id);
    },

    // 选择日期触发
    dateClick(date) {
      this.selectedDate = date;
      const now = new Date();
      const year = now.getFullYear();
      this.orderForm.orderDate = year + "-" + date;

      console.log('选择日期:', date, '完整日期:', this.orderForm.orderDate);

      this.loading = true;
      request
          .get("/arrange/findByTime", {
            params: {
              arTime: year + "-" + date,
              dSection: this.sectionOpt,
            },
          })
          .then((res) => {
            this.loading = false;
            if (res.data && res.data.data) {
              this.sectionData = res.data.data.map((item) => item.doctor || item);
              this.clickTag = true;
              this.$message.success(`已加载 ${this.sectionData.length} 位医生的排班信息`);
            } else {
              this.sectionData = [];
              this.$message.warning("该日期暂无医生排班");
            }
          })
          .catch((error) => {
            this.loading = false;
            console.error('加载医生排班失败:', error);
            this.$message.error("加载医生排班信息失败");
          });
    },

    // 获取当天及后7天的日期星期（适配Redis键名生成）
    nowDay(num) {
      var nowDate = new Date();
      var currentHour = nowDate.getHours();
      var currentMinute = nowDate.getMinutes();

      // 如果当前时间超过17:30，从明天开始
      if (currentHour > 17 || (currentHour === 17 && currentMinute > 30)) {
        num++;
      }

      nowDate.setDate(nowDate.getDate() + num);
      var month = nowDate.getMonth() + 1;
      var date = nowDate.getDate();
      if (date < 10) date = "0" + date;
      if (month < 10) month = "0" + month;
      this.monthDays.push(month + "-" + date);
    },

    // 请求部门医生信息
    requestSection() {
      if (!this.sectionOpt) {
        console.warn('科室参数为空');
        return;
      }

      this.loading = true;
      request
          .get("patient/findDoctorBySection", {
            params: {
              dSection: this.sectionOpt,
            },
          })
          .then((res) => {
            this.loading = false;
            if (res.data && res.data.status === 200 && res.data.data) {
              this.sectionData = res.data.data.doctors || res.data.data || [];
              console.log('加载医生数据:', this.sectionData.length, '条');
            } else {
              this.sectionData = [];
              this.$message.warning("暂无医生数据");
            }
          })
          .catch((error) => {
            this.loading = false;
            console.error('请求医生数据失败:', error);
            this.$message.error("网络错误，数据加载失败");
          });
    },
  },
  created() {
    // 初始化未来7天日期
    for (var i = 0; i < 7; i++) {
      this.nowDay(i);
    }

    // 加载科室医生信息
    this.requestSection();

    // 从token中获取患者信息
    const token = getToken();
    if (token) {
      const tokenData = this.tokenDecode(token);
      this.orderForm.pName = tokenData.pName || '';
      this.orderForm.pCard = tokenData.pCard || '';
      this.orderForm.pId = tokenData.pId || '';
      console.log('患者信息:', this.orderForm);
    } else {
      console.warn('未找到token信息');
    }
  },
};
</script>


<style lang="scss" scoped>
.doctor-schedule-management {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  min-height: 100vh;
}

/* 顶部标题区域 */
.schedule-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  padding: 20px 0;
}

.schedule-title-content {
  z-index: 2;
}

.schedule-title-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 10px;
}

.schedule-page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.schedule-page-subtitle {
  font-size: 16px;
  color: #5a6376;
  margin: 0;
}

.schedule-decoration-element {
  position: absolute;
  right: 0;
  top: 0;
}

.schedule-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.schedule-circle-1 {
  width: 100px;
  height: 100px;
  background: #409eff;
  top: -30px;
  right: 0;
}

.schedule-circle-2 {
  width: 70px;
  height: 70px;
  background: #67c23a;
  top: 10px;
  right: 80px;
}

.schedule-circle-3 {
  width: 50px;
  height: 50px;
  background: #e6a23c;
  top: 40px;
  right: 40px;
}

/* 主卡片样式 */
.schedule-management-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.schedule-management-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 面包屑导航 */
.schedule-breadcrumb-section {
  padding: 20px;
  background: #f8fafc;
  border-bottom: 1px solid #e6e8eb;
}

.custom-breadcrumb {
  .el-breadcrumb__item {
    .el-breadcrumb__inner {
      display: flex;
      align-items: center;
      gap: 5px;
      color: #5a6376;
      transition: color 0.3s ease;
      font-weight: 500;
    }

    &:hover .el-breadcrumb__inner {
      color: #409eff;
    }

    &:last-child .el-breadcrumb__inner {
      color: #409eff;
      font-weight: 600;
    }
  }
}

/* 信息区域 */
.schedule-info-section {
  padding: 20px;
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 20px;
}

.department-info-card {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-radius: 12px;
  padding: 20px;
  color: white;
  display: flex;
  align-items: center;
  gap: 15px;
}

.department-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.department-content {
  flex: 1;
}

.department-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 5px 0;
}

.department-subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.doctor-count {
  text-align: center;
}

.count-number {
  font-size: 24px;
  font-weight: 700;
  display: block;
}

.count-label {
  font-size: 12px;
  opacity: 0.9;
}

.date-selection-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e6e8eb;
}

.date-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.date-icon {
  font-size: 20px;
  color: #409eff;
}

.date-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.date-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 10px;
}

.date-card {
  background: #f8fafc;
  border: 2px solid transparent;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.date-card:hover {
  border-color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.date-card.date-active {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  border-color: #409eff;
}

.date-content {
  position: relative;
  z-index: 2;
}

.date-month {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.date-card.date-active .date-month {
  color: rgba(255, 255, 255, 0.9);
}

.date-day {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 3px;
}

.date-week {
  font-size: 11px;
  color: #c0c4cc;
}

.date-card.date-active .date-week {
  color: rgba(255, 255, 255, 0.8);
}

.date-indicator {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 16px;
  height: 16px;
  background: #67c23a;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  color: white;
}

/* 医生表格区域 */
.doctor-table-section {
  padding: 20px;
  border-top: 1px solid #e6e8eb;
}

.table-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f2f5;
}

.table-icon {
  font-size: 20px;
  color: #409eff;
}

.table-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.table-subtitle {
  font-size: 14px;
  color: #909399;
  margin-left: auto;
}

.doctor-data-table {
  border-radius: 8px;
  overflow: hidden;
}

.index-badge {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin: 0 auto;
}

.id-display, .name-display, .intro-display {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.id-icon, .name-icon, .intro-icon {
  color: #909399;
  font-size: 14px;
}

.name-text {
  font-weight: 500;
}

.gender-tag {
  border-radius: 12px;
  padding: 4px 8px;
  font-weight: 500;
}

.post-text, .section-text {
  font-weight: 500;
  color: #606266;
}

.intro-text {
  color: #909399;
  font-size: 13px;
  line-height: 1.4;
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
}

.price-value {
  font-size: 16px;
  font-weight: 600;
  color: #f56c6c;
}

.rating-stars .el-rate__icon {
  font-size: 14px;
}

.appointment-btn {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
  border: none;
  color: white;
  border-radius: 6px;
  font-weight: 500;
  padding: 8px 12px;
}

.appointment-btn:hover:not(.is-disabled) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
}

/* 空状态提示 */
.schedule-empty-state {
  padding: 60px 20px;
  text-align: center;
}

.empty-content {
  color: #909399;
}

.empty-icon {
  font-size: 64px;
  color: #dcdfe6;
  margin-bottom: 20px;
}

.empty-content h3 {
  font-size: 18px;
  font-weight: 600;
  color: #606266;
  margin: 0 0 10px 0;
}

.empty-content p {
  font-size: 14px;
  margin: 0;
  color: #909399;
}

/* 对话框样式 */
.appointment-dialog {
  .el-dialog {
    border-radius: 16px;
    overflow: hidden;
  }

  .el-dialog__header {
    padding: 0;
  }

  .el-dialog__headerbtn {
    top: 20px;
    right: 20px;
  }
}

.dialog-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
}

.dialog-icon {
  font-size: 24px;
}

.dialog-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.appointment-form {
  padding: 20px;
}

.form-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f2f5;
}

.time-select {
  width: 100%;
}

.time-option-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.time-slot {
  font-weight: 500;
}

.time-remaining {
  color: #f56c6c;
  font-size: 12px;
}

.disabled-input .el-input__inner {
  background: #f5f7fa;
  color: #909399;
  cursor: not-allowed;
}

.dialog-footer {
  padding: 0 20px 20px;
  text-align: right;
}

.cancel-btn, .confirm-btn {
  border-radius: 6px;
  padding: 10px 20px;
  font-weight: 500;
}

.confirm-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
  color: white;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .schedule-info-section {
    grid-template-columns: 1fr;
  }

  .date-grid {
    grid-template-columns: repeat(4, 1fr);
  }

  .doctor-data-table {
    font-size: 12px;
  }
}
</style>