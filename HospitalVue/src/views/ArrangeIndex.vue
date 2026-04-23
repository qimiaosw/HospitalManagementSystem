<template>
    <div class="arrange-index">
        <!-- 顶部标题区域 -->
        <div class="header-section">
            <div class="title-content">
                <i class="el-icon-date title-icon"></i>
                <h1 class="page-title">医生排班管理</h1>
                <p class="page-subtitle">选择日期并安排医生值班</p>
            </div>
            <div class="decoration-element">
                <div class="circle circle-1"></div>
                <div class="circle circle-2"></div>
                <div class="circle circle-3"></div>
            </div>
        </div>

        <el-card class="management-card" shadow="hover">
            <!-- 日期选择区域 -->
            <div class="date-selection-section">
                <div class="section-header">
                    <i class="el-icon-date section-icon"></i>
                    <h3 class="section-title">请选择值班日期</h3>
                    <div class="date-range-info">
                        <span>可选日期范围：今日起25天内</span>
                    </div>
                </div>
                
                <div class="date-grid">
                    <div 
                        v-for="monthDay in monthDays" 
                        :key="monthDay"
                        class="date-card"
                        :class="{ 'active': isDateActive(monthDay) }"
                        @click="dateClick(monthDay)"
                    >
                        <div class="date-icon">
                            <i class="el-icon-date"></i>
                        </div>
                        <div class="date-content">
                            <div class="date-display">{{ monthDay }}</div>
                            <div class="day-of-week">{{ getDayOfWeek(monthDay) }}</div>
                        </div>
                        <div class="date-badge" v-if="isToday(monthDay)">今日</div>
                        <div class="date-hover-effect"></div>
                    </div>
                </div>

                <div class="selected-date-info" v-if="selectedDate">
                    <i class="el-icon-success selected-icon"></i>
                    <span class="selected-text">已选择日期：</span>
                    <span class="selected-date">{{ selectedDate }}</span>
                </div>
            </div>

            <!-- 路由视图区域 -->
            <div class="router-view-section" v-if="activePath">
                <div class="router-header">
                    <i class="el-icon-office-building router-icon"></i>
                    <h3 class="router-title">科室选择</h3>
                    <div class="router-tips">请选择科室进行医生排班</div>
                </div>
                <div class="router-content">
                    <router-view></router-view>
                </div>
            </div>

            <!-- 空状态提示 -->
            <div v-else class="empty-state">
                <div class="empty-content">
                    <i class="el-icon-date empty-icon"></i>
                    <h3>请选择值班日期</h3>
                    <p>选择上方日期后，将显示科室选择界面</p>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
import { getActivePath, setActivePath } from "@/utils/storage.js";
const ARRANGEDATE = "arrangeDate";

export default {
    name: "ArrangeIndex",
    data() {
        return {
            monthDays: [],
            selectedDate: "",
            activePath: "",
            loading: false
        };
    },
    methods: {
        // 日期点击事件
        dateClick(monthDay) {
            this.loading = true;
            console.log(monthDay);

            const nowDate = new Date();
            let year = nowDate.getFullYear();
            let dateTime = year + "-" + monthDay;
            sessionStorage.setItem(ARRANGEDATE, dateTime);
            this.selectedDate = dateTime;

            // 添加点击动画效果
            setTimeout(() => {
                this.activePath = "sectionIndex";
                setActivePath("sectionIndex");
                if (this.$route.path !== "/sectionIndex") {
                    this.$router.push("sectionIndex");
                }
                this.loading = false;
                
                this.$message.success(`已选择 ${dateTime} 进行排班`);
            }, 300);
        },
        
        // 获取星期几
        getDayOfWeek(monthDay) {
            const nowDate = new Date();
            const [month, day] = monthDay.split('-');
            const date = new Date(nowDate.getFullYear(), parseInt(month) - 1, parseInt(day));
            const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
            return days[date.getDay()];
        },
        
        // 检查是否是今天
        isToday(monthDay) {
            const today = new Date();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            return monthDay === `${month}-${day}`;
        },
        
        // 检查日期是否激活
        isDateActive(monthDay) {
            if (!this.selectedDate) return false;
            const nowDate = new Date();
            let year = nowDate.getFullYear();
            return this.selectedDate === year + "-" + monthDay;
        },
        
        // 获取当天及后N天的日期
        nowDay(num) {
            var nowDate = new Date();
            nowDate.setDate(nowDate.getDate() + num);
            var month = nowDate.getMonth() + 1;
            var date = nowDate.getDate();
            if (date < 10) {
                date = "0" + date;
            }
            if (month < 10) {
                month = "0" + month;
            }
            var time = month + "-" + date;
            this.monthDays.push(time);
        }
    },
    created() {
        // 获取当天的后25天
        for (var i = 0; i < 25; i++) {
            this.nowDay(i);
        }
        // 获取激活路径
        this.activePath = getActivePath();
        
        // 检查是否有已选择的日期
        const savedDate = sessionStorage.getItem(ARRANGEDATE);
        if (savedDate) {
            this.selectedDate = savedDate;
        }
    }
};
</script>

<style lang="scss" scoped>
.arrange-index {
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
    transition: all 0.3s ease;
}

.management-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 日期选择区域 */
.date-selection-section {
    padding: 30px;
    border-bottom: 1px solid #e6e8eb;
}

.section-header {
    display: flex;
    align-items: center;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 1px solid #f0f2f5;
}

.section-icon {
    font-size: 24px;
    color: #e6a23c;
    margin-right: 12px;
}

.section-title {
    font-size: 20px;
    font-weight: 600;
    color: #1f2d3d;
    margin: 0;
    flex: 1;
}

.date-range-info {
    font-size: 14px;
    color: #909399;
    background: #f5f7fa;
    padding: 6px 12px;
    border-radius: 6px;
}

/* 日期网格布局 */
.date-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 15px;
    margin-bottom: 25px;
}

.date-card {
    position: relative;
    background: white;
    border: 2px solid #f0f2f5;
    border-radius: 12px;
    padding: 20px 15px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;
    overflow: hidden;
    min-height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.date-card:hover {
    transform: translateY(-3px);
    border-color: #e6a23c;
    box-shadow: 0 4px 15px rgba(230, 162, 60, 0.15);
}

.date-card.active {
    border-color: #e6a23c;
    background: linear-gradient(135deg, #fff9f0 0%, #fff5eb 100%);
    box-shadow: 0 4px 15px rgba(230, 162, 60, 0.2);
}

.date-icon {
    font-size: 24px;
    color: #e6a23c;
    margin-bottom: 10px;
    transition: all 0.3s ease;
}

.date-card:hover .date-icon {
    transform: scale(1.1);
    color: #d48813;
}

.date-content {
    z-index: 2;
}

.date-display {
    font-size: 16px;
    font-weight: 600;
    color: #1f2d3d;
    margin-bottom: 5px;
}

.day-of-week {
    font-size: 12px;
    color: #909399;
}

.date-badge {
    position: absolute;
    top: 8px;
    right: 8px;
    background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    color: white;
    font-size: 10px;
    padding: 2px 6px;
    border-radius: 8px;
    font-weight: 500;
}

.date-hover-effect {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(230, 162, 60, 0.1), transparent);
    transition: left 0.6s ease;
}

.date-card:hover .date-hover-effect {
    left: 100%;
}

/* 已选择日期信息 */
.selected-date-info {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 15px;
    background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
    border: 1px solid #d9ecff;
    border-radius: 8px;
    color: #409eff;
}

.selected-icon {
    font-size: 18px;
    margin-right: 8px;
}

.selected-text {
    font-size: 14px;
    font-weight: 500;
}

.selected-date {
    font-size: 16px;
    font-weight: 600;
    margin-left: 5px;
}

/* 路由视图区域 */
.router-view-section {
    padding: 30px;
}

.router-header {
    display: flex;
    align-items: center;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 1px solid #f0f2f5;
}

.router-icon {
    font-size: 24px;
    color: #409eff;
    margin-right: 12px;
}

.router-title {
    font-size: 20px;
    font-weight: 600;
    color: #1f2d3d;
    margin: 0;
    flex: 1;
}

.router-tips {
    font-size: 14px;
    color: #909399;
    background: #f5f7fa;
    padding: 6px 12px;
    border-radius: 6px;
}

.router-content {
    min-height: 200px;
}

/* 空状态提示 */
.empty-state {
    padding: 60px 30px;
    text-align: center;
}

.empty-content {
    max-width: 400px;
    margin: 0 auto;
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
    color: #909399;
    margin: 0;
    line-height: 1.5;
}

/* 加载状态 */
.loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 16px;
    z-index: 10;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .arrange-index {
        padding: 10px;
    }
    
    .header-section {
        flex-direction: column;
        text-align: center;
    }
    
    .date-selection-section {
        padding: 20px;
    }
    
    .section-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
    }
    
    .date-range-info {
        align-self: flex-start;
    }
    
    .date-grid {
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 10px;
    }
    
    .date-card {
        padding: 15px 10px;
        min-height: 100px;
    }
    
    .router-view-section {
        padding: 20px;
    }
    
    .router-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
    }
    
    .selected-date-info {
        flex-direction: column;
        text-align: center;
        gap: 8px;
    }
}

@media (max-width: 480px) {
    .date-grid {
        grid-template-columns: repeat(2, 1fr);
    }
    
    .date-card {
        min-height: 90px;
    }
    
    .date-display {
        font-size: 14px;
    }
    
    .day-of-week {
        font-size: 11px;
    }
}

/* 动画效果 */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.date-card {
    animation: fadeIn 0.5s ease forwards;
}

/* 为每个日期卡片添加延迟动画 */
.date-card:nth-child(1) { animation-delay: 0.1s; }
.date-card:nth-child(2) { animation-delay: 0.2s; }
.date-card:nth-child(3) { animation-delay: 0.3s; }
.date-card:nth-child(4) { animation-delay: 0.4s; }
.date-card:nth-child(5) { animation-delay: 0.5s; }
.date-card:nth-child(6) { animation-delay: 0.6s; }
.date-card:nth-child(7) { animation-delay: 0.7s; }
/* 可以继续添加更多延迟 */
</style>