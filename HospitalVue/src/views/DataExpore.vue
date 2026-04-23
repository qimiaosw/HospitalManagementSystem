<template>
  <div class="data-explore">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-data-analysis title-icon"></i>
        <h1 class="page-title">数据统计分析</h1>
        <p class="page-subtitle">医院运营数据可视化分析</p>
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
          <el-col :span="6">
            <div class="stat-card total-stat">
              <div class="stat-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ totalPatients }}</div>
                <div class="stat-label">累计患者数量</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card today-stat">
              <div class="stat-icon">
                <i class="el-icon-date"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ todayAppointments }}</div>
                <div class="stat-label">今日挂号人数</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card gender-stat">
              <div class="stat-icon">
                <i class="el-icon-male"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ maleRatio }}%</div>
                <div class="stat-label">男性患者比例</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card age-stat">
              <div class="stat-icon">
                <i class="el-icon-present"></i>
              </div>
              <div class="stat-content">
                <div class="stat-number">{{ avgAge }}</div>
                <div class="stat-label">平均年龄</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 挂号趋势图表 -->
        <el-card class="chart-card" shadow="hover">
          <div class="chart-header">
            <i class="el-icon-trend-charts chart-icon"></i>
            <h3 class="chart-title">挂号趋势分析</h3>
            <div class="chart-subtitle">近20天挂号人数变化趋势</div>
          </div>
          <div id="orderPeople" class="chart-content"></div>
        </el-card>

        <!-- 科室分布图表 -->
        <el-card class="chart-card" shadow="hover">
          <div class="chart-header">
            <i class="el-icon-office-building chart-icon"></i>
            <h3 class="chart-title">科室挂号分布</h3>
            <div class="chart-subtitle">各科室挂号人数统计</div>
          </div>
          <div id="orderSection" class="chart-content"></div>
        </el-card>

        <!-- 性别和年龄分布 -->
        <el-row :gutter="20" class="double-charts">
          <el-col :span="12">
            <el-card class="chart-card" shadow="hover">
              <div class="chart-header">
                <i class="el-icon-user chart-icon"></i>
                <h3 class="chart-title">患者性别比例</h3>
                <div class="chart-subtitle">男女患者分布情况</div>
              </div>
              <div id="orderGender" class="chart-content"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="chart-card" shadow="hover">
              <div class="chart-header">
                <i class="el-icon-present chart-icon"></i>
                <h3 class="chart-title">患者年龄分布</h3>
                <div class="chart-subtitle">各年龄段患者数量</div>
              </div>
              <div id="patientAge" class="chart-content"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 数据更新时间 -->
      <div class="update-time">
        <i class="el-icon-time update-icon"></i>
        <span class="update-text">数据更新时间：{{ updateTime }}</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request.js";
export default {
  name: "dataExplore",
  data() {
    return {
      sevenDate: [],
      sevenOrder: [],
      totalPatients: 0,
      todayAppointments: 0,
      maleRatio: 0,
      avgAge: 0,
      updateTime: ''
    };
  },
  methods: {
    // 获取当前时间
    getCurrentTime() {
      const now = new Date();
      this.updateTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },

    //统计患者年龄分布
    patientAge(){
      var myChart = this.$echarts.init(document.getElementById("patientAge"));
      request.get("patient/patientAge")
      .then(res => {
        // 计算平均年龄（模拟数据）
        this.calculateAverageAge(res.data.data);
        
        var option = {
          backgroundColor: 'transparent',
          title: {
            text: '患者年龄段分布',
            left: 'center',
            textStyle: {
              color: '#1f2d3d',
              fontSize: 16,
              fontWeight: '600'
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}人 ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e4e7ed',
            textStyle: {
              color: '#606266'
            }
          },
          legend: {
            orient: 'horizontal',
            bottom: 10,
            left: 'center',
            textStyle: {
              color: '#606266'
            }
          },
          series: [
            {
              name: '年龄段',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['50%', '45%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 8,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 18,
                  fontWeight: 'bold',
                  color: '#1f2d3d'
                },
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              labelLine: {
                show: false
              },
              data: [
                {value: res.data.data[0] || 0, name: '0-9岁', itemStyle: {color: '#5470c6'}},
                {value: res.data.data[1] || 0, name: '10-19岁', itemStyle: {color: '#91cc75'}},
                {value: res.data.data[2] || 0, name: '20-29岁', itemStyle: {color: '#fac858'}},
                {value: res.data.data[3] || 0, name: '30-39岁', itemStyle: {color: '#ee6666'}},
                {value: res.data.data[4] || 0, name: '40-49岁', itemStyle: {color: '#73c0de'}},
                {value: res.data.data[5] || 0, name: '50-59岁', itemStyle: {color: '#3ba272'}},
                {value: res.data.data[6] || 0, name: '60-69岁', itemStyle: {color: '#fc8452'}},
                {value: res.data.data[7] || 0, name: '70-79岁', itemStyle: {color: '#9a60b4'}},
                {value: res.data.data[8] || 0, name: '80-89岁', itemStyle: {color: '#ea7ccc'}},
                {value: res.data.data[9] || 0, name: '90-99岁', itemStyle: {color: '#6e7074'}},
              ]
            }
          ]
        };
       
        myChart.setOption(option);
      })
      .catch(err => {
        console.error(err); 
      })
    },

    // 计算平均年龄（模拟计算）
    calculateAverageAge(ageData) {
      let total = 0;
      let count = 0;
      const ageRanges = [5, 15, 25, 35, 45, 55, 65, 75, 85, 95];
      
      ageData.forEach((value, index) => {
        total += value * ageRanges[index];
        count += value;
      });
      
      this.avgAge = count > 0 ? Math.round(total / count) : 0;
    },

    //统计挂号科室人数
    orderSection(){
      var myChart = this.$echarts.init(document.getElementById("orderSection"));
      request.get("order/orderSection")
      .then(res => {
        var option = {
          backgroundColor: 'transparent',
          title: {
            text: '挂号科室人数统计',
            left: 'center',
            textStyle: {
              color: '#1f2d3d',
              fontSize: 16,
              fontWeight: '600'
            }
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            },
            formatter: '{b}<br/>挂号人数: {c}人',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e4e7ed'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            top: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: res.data.data.map((item) => item.doctor.dSection),
            axisLabel: {
              interval: 0,
              rotate: 45,
              textStyle: {
                color: '#606266'
              }
            },
            axisLine: {
              lineStyle: {
                color: '#e4e7ed'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLine: {
              lineStyle: {
                color: '#e4e7ed'
              }
            },
            splitLine: {
              lineStyle: {
                color: '#f0f2f5',
                type: 'dashed'
              }
            }
          },
          series: [{
            name: '挂号人数',
            type: 'bar',
            data: res.data.data.map((item) => item.countSection),
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {offset: 0, color: '#83bff6'},
                {offset: 1, color: '#188df0'}
              ]),
              borderRadius: [4, 4, 0, 0]
            },
            emphasis: {
              itemStyle: {
                color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {offset: 0, color: '#2378f7'},
                  {offset: 1, color: '#83bff6'}
                ])
              }
            }
          }]
        };
        
        myChart.setOption(option);
      })
      .catch(err => {
        console.error(err); 
      })
    },

    //挂号男女比例
    orderGender(){
      var myChart = this.$echarts.init(document.getElementById("orderGender"));
      request.get("order/orderGender")
      .then(res => {
        const maleData = res.data.data.find(item => item.patient.pGender === '男');
        const femaleData = res.data.data.find(item => item.patient.pGender === '女');
        
        // 计算男性比例
        const maleCount = maleData ? maleData.countGender : 0;
        const femaleCount = femaleData ? femaleData.countGender : 0;
        const total = maleCount + femaleCount;
        this.maleRatio = total > 0 ? Math.round((maleCount / total) * 100) : 0;
        this.totalPatients = total;

        var option = {
          backgroundColor: 'transparent',
          title: {
            text: '患者性别比例',
            left: 'center',
            textStyle: {
              color: '#1f2d3d',
              fontSize: 16,
              fontWeight: '600'
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}人 ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e4e7ed'
          },
          legend: {
            orient: 'vertical',
            right: 10,
            top: 'center',
            textStyle: {
              color: '#606266'
            }
          },
          series: [
            {
              name: '患者性别',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['40%', '50%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 8,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 16,
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: [
                {value: maleCount, name: '男性', itemStyle: {color: '#5470c6'}},
                {value: femaleCount, name: '女性', itemStyle: {color: '#ee6666'}}
              ]
            }
          ]
        };
        
        myChart.setOption(option);
      })
      .catch(err => {
        console.error(err); 
      });
    },

    //获取过去num天日期
    pastSeven(num) {
      var date = new Date();
      date.setDate(date.getDate() - num);
      var time = date.getMonth() + 1 + "-" + date.getDate();
      return time;
    },

    //挂号人数图表
    orderPeople() {
      var myChart = this.$echarts.init(document.getElementById("orderPeople"));
      request
        .get("order/orderSeven")
        .then((res) => {
          if (res.data.status !== 200)
            return this.$message.error("数据请求失败！");
          
          // 计算今日挂号人数
          this.todayAppointments = res.data.data[res.data.data.length - 1] || 0;

          var option = {
            backgroundColor: 'transparent',
            title: {
              text: '近20天挂号人数趋势',
              left: 'center',
              textStyle: {
                color: '#1f2d3d',
                fontSize: 16,
                fontWeight: '600'
              }
            },
            tooltip: {
              trigger: 'axis',
              backgroundColor: 'rgba(255, 255, 255, 0.95)',
              borderColor: '#e4e7ed',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              }
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '10%',
              top: '15%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: this.sevenDate,
              axisLine: {
                lineStyle: {
                  color: '#e4e7ed'
                }
              },
              axisLabel: {
                color: '#606266'
              }
            },
            yAxis: {
              type: 'value',
              axisLine: {
                lineStyle: {
                  color: '#e4e7ed'
                }
              },
              splitLine: {
                lineStyle: {
                  color: '#f0f2f5',
                  type: 'dashed'
                }
              },
              axisLabel: {
                color: '#606266'
              }
            },
            series: [
              {
                name: '挂号人数',
                type: 'line',
                smooth: true,
                lineStyle: {
                  width: 3,
                  color: '#5470c6'
                },
                areaStyle: {
                  color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
                    { offset: 1, color: 'rgba(84, 112, 198, 0.1)' }
                  ])
                },
                itemStyle: {
                  color: '#5470c6'
                },
                data: res.data.data
              }
            ]
          };
          
          myChart.setOption(option);
        })
        .catch((err) => {
          console.error(err);
        });
    },

    // 窗口调整时重新渲染图表
    handleResize() {
      if (this.orderPeopleChart) this.orderPeopleChart.resize();
      if (this.orderSectionChart) this.orderSectionChart.resize();
      if (this.orderGenderChart) this.orderGenderChart.resize();
      if (this.patientAgeChart) this.patientAgeChart.resize();
    }
  },
  mounted() {
    this.orderPeople();
    this.orderGender();
    this.orderSection();
    this.patientAge();
    this.getCurrentTime();
    
    // 添加窗口调整事件监听
    window.addEventListener('resize', this.handleResize);
    
    // 保存图表实例引用
    this.$nextTick(() => {
      this.orderPeopleChart = this.$echarts.getInstanceByDom(document.getElementById('orderPeople'));
      this.orderSectionChart = this.$echarts.getInstanceByDom(document.getElementById('orderSection'));
      this.orderGenderChart = this.$echarts.getInstanceByDom(document.getElementById('orderGender'));
      this.patientAgeChart = this.$echarts.getInstanceByDom(document.getElementById('patientAge'));
    });
  },
  beforeDestroy() {
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize);
  },
  created() {
    //获取过去20天日期
    for (var i = 20; i > 0; i--) {
      this.sevenDate.push(this.pastSeven(i));
    }
  },
};
</script>

<style lang="scss" scoped>
.data-explore {
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

.today-stat .stat-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.gender-stat .stat-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f2b64c 100%);
}

.age-stat .stat-icon {
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
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 图表容器 */
.charts-container {
  padding: 20px;
}

.chart-card {
  border-radius: 12px;
  border: none;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.chart-header {
  padding: 20px 20px 0;
  border-bottom: 1px solid #f0f2f5;
  margin-bottom: 10px;
}

.chart-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 10px;
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0 0 5px 0;
  display: inline;
}

.chart-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.chart-content {
  width: 100%;
  height: 300px;
  padding: 10px;
}

.double-charts {
  margin-top: 20px;
}

.double-charts .chart-content {
  height: 280px;
}

/* 数据更新时间 */
.update-time {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 15px 20px;
  background: #f8fafc;
  border-top: 1px solid #e6e8eb;
  color: #909399;
  font-size: 14px;
}

.update-icon {
  margin-right: 8px;
  font-size: 16px;
}

.update-text {
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .charts-container {
    padding: 15px;
  }
  
  .chart-content {
    height: 250px;
  }
  
  .double-charts .chart-content {
    height: 220px;
  }
}

@media (max-width: 768px) {
  .data-explore {
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
    flex-direction: column;
    text-align: center;
    padding: 15px;
    height: auto;
    margin-bottom: 15px;
  }
  
  .stat-icon {
    margin-right: 0;
    margin-bottom: 10px;
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
  
  .charts-container {
    padding: 10px;
  }
  
  .chart-content {
    height: 200px;
  }
  
  .double-charts .chart-content {
    height: 180px;
  }
  
  .double-charts .el-col {
    margin-bottom: 15px;
  }
}

@media (max-width: 480px) {
  .stat-card {
    padding: 12px;
  }
  
  .stat-number {
    font-size: 20px;
  }
  
  .stat-label {
    font-size: 12px;
  }
  
  .chart-content {
    height: 180px;
  }
  
  .double-charts .chart-content {
    height: 160px;
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

.stat-card:nth-child(1) { animation-delay: 0.1s; }
.stat-card:nth-child(2) { animation-delay: 0.2s; }
.stat-card:nth-child(3) { animation-delay: 0.3s; }
.stat-card:nth-child(4) { animation-delay: 0.4s; }

.chart-card {
  animation: fadeInUp 0.6s ease forwards;
}

.chart-card:nth-child(1) { animation-delay: 0.2s; }
.chart-card:nth-child(2) { animation-delay: 0.3s; }
.chart-card:nth-child(3) { animation-delay: 0.4s; }
</style>