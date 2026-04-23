<template>
  <div class="ai-diagnosis-container">
    <!-- 顶部标题区域 -->
    <div class="diagnosis-header-section">
      <div class="diagnosis-title-content">
        <i class="el-icon-cpu diagnosis-title-icon"></i>
        <h1 class="diagnosis-page-title">AI智能导诊</h1>
        <p class="diagnosis-page-subtitle">基于人工智能的智能分诊与科室推荐</p>
      </div>
      <div class="diagnosis-decoration-element">
        <div class="diagnosis-circle diagnosis-circle-1"></div>
        <div class="diagnosis-circle diagnosis-circle-2"></div>
        <div class="diagnosis-circle diagnosis-circle-3"></div>
      </div>
    </div>

    <div class="diagnosis-container">
      <div class="diagnosis-content">
        <!-- AI对话区域 -->
        <div class="chat-section">
          <div class="chat-header">
            <div class="ai-avatar">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="ai-info">
              <h3>AI导诊助手</h3>
              <p>智能分析您的症状，推荐合适的科室</p>
            </div>
            <div class="ai-status">
              <span class="status-dot online"></span>
              <span class="status-text">在线服务中</span>
            </div>
          </div>

          <!-- 对话内容区域 -->
          <div class="chat-messages" ref="chatMessages">
            <div v-for="(message, index) in messages" :key="index" :class="['message-item', message.type]">
              <div class="message-avatar">
                <i v-if="message.type === 'ai'" class="el-icon-cpu"></i>
                <i v-else class="el-icon-user"></i>
              </div>
              <div class="message-content">
                <div class="message-text" v-html="message.content"></div>
                <div class="message-time">{{ message.time }}</div>
              </div>
            </div>

            <!-- 加载动画 -->
            <div v-if="isLoading" class="message-item ai">
              <div class="message-avatar">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="message-content">
                <div class="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          </div>

          <!-- 快捷症状选择 -->
          <div class="quick-symptoms" v-if="messages.length === 1 && messages[0].type === 'ai'">
            <h4>常见症状快速选择</h4>
            <div class="symptoms-grid">
              <div
                  v-for="symptom in commonSymptoms"
                  :key="symptom.id"
                  class="symptom-card"
                  @click="selectSymptom(symptom)"
              >
                <div class="symptom-icon">
                  <i :class="symptom.icon"></i>
                </div>
                <div class="symptom-info">
                  <h5>{{ symptom.name }}</h5>
                  <p>{{ symptom.description }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="chat-input-section">
            <div class="input-container">
              <el-input
                  v-model="userInput"
                  type="textarea"
                  :rows="3"
                  placeholder="请详细描述您的症状，如：头痛、发热、咳嗽等...（按回车发送，Shift+回车换行）"
                  @keydown.enter.native="handleEnterKey"
                  :disabled="isLoading"
              ></el-input>
              <div class="input-actions">
                <el-button
                    type="primary"
                    @click="sendMessage"
                    :disabled="!userInput.trim() || isLoading"
                    :loading="isLoading"
                >
                  <i class="el-icon-s-promotion"></i>
                  发送
                </el-button>
                <el-button @click="clearChatWithConfirm">
                  <i class="el-icon-refresh-left"></i>
                  重置
                </el-button>
              </div>
            </div>
            <div class="input-tips">
              <span><i class="el-icon-info"></i> 请详细描述症状，AI将为您推荐合适的科室</span>
            </div>
          </div>
        </div>

        <!-- 诊断结果区域 -->
        <div class="diagnosis-result" v-if="diagnosisResult">
          <div class="result-header">
            <h3><i class="el-icon-document-checked"></i> 诊断建议</h3>
            <div class="result-timestamp" v-if="diagnosisResult.timestamp">
              分析时间: {{ formatDiagnosisTime(diagnosisResult.timestamp) }}
            </div>
          </div>

          <div class="result-content">
            <!-- 推荐科室 -->
            <div class="result-section">
              <h4><i class="el-icon-office-building"></i> 推荐科室</h4>
              <div class="department-recommendations">
                <div
                    v-for="dept in diagnosisResult.departments"
                    :key="dept.id"
                    class="dept-card"
                    @click="goToAppointment(dept)"
                >
                  <div class="dept-header">
                    <h5>{{ dept.name }}</h5>
                    <div class="match-score">
                      <span>匹配度</span>
                      <el-progress
                          :percentage="dept.matchScore"
                          :color="getScoreColor(dept.matchScore)"
                          :show-text="false"
                          :stroke-width="6"
                      ></el-progress>
                      <span class="score-text">{{ dept.matchScore }}%</span>
                    </div>
                  </div>
                  <p class="dept-desc">{{ dept.description }}</p>
                  <div class="dept-actions">
                    <el-button type="primary" size="small">
                      <i class="el-icon-date"></i>
                      立即预约
                    </el-button>
                    <el-button type="text" size="small">
                      <i class="el-icon-info"></i>
                      科室详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 严重程度 -->
            <div class="result-section" v-if="diagnosisResult.severityLevel">
              <h4><i class="el-icon-warning"></i> 严重程度</h4>
              <el-alert
                  :title="'判断结果：' + diagnosisResult.severityLevel"
                  :type="getSeverityType(diagnosisResult.severityLevel)"
                  :closable="false"
                  show-icon
              ></el-alert>
            </div>

            <!-- 建议措施 -->
            <div class="result-section">
              <h4><i class="el-icon-warning"></i> 建议措施</h4>
              <div class="suggestions">
                <div
                    v-for="(suggestion, index) in diagnosisResult.suggestions"
                    :key="index"
                    class="suggestion-item"
                >
                  <div class="suggestion-icon">
                    <i :class="suggestion.icon"></i>
                  </div>
                  <div class="suggestion-content">
                    <h5>{{ suggestion.title }}</h5>
                    <p>{{ suggestion.content }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 免责声明 -->
            <div class="disclaimer">
              <el-alert
                  title="免责声明"
                  type="warning"
                  :closable="false"
                  show-icon
              >
                <template slot="default">
                  <p>AI导诊仅供参考，不能替代专业医生的诊断。如症状严重或持续，请及时就医。</p>
                  <p>紧急情况请立即拨打急救电话：120</p>
                  <p>聊天记录将自动保存，24小时后自动清除</p>
                </template>
              </el-alert>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getAiDiagnosis } from "@/utils/aiDiagnosis.js";

export default {
  name: "AiDiagnosis",
  data() {
    return {
      userInput: '',
      isLoading: false,
      messages: [],
      diagnosisResult: null,
      commonSymptoms: [
        {
          id: 1,
          name: '发热',
          description: '体温升高，可能伴有寒战',
          icon: 'el-icon-sunny'
        },
        {
          id: 2,
          name: '头痛',
          description: '头部疼痛，可能伴有眩晕',
          icon: 'el-icon-headache'
        },
        {
          id: 3,
          name: '咳嗽',
          description: '呼吸道症状，可能伴有痰',
          icon: 'el-icon-cough'
        },
        {
          id: 4,
          name: '腹痛',
          description: '腹部不适或疼痛',
          icon: 'el-icon-stomach'
        },
        {
          id: 5,
          name: '胸闷',
          description: '胸部压迫感，呼吸困难',
          icon: 'el-icon-lungs'
        },
        {
          id: 6,
          name: '皮肤问题',
          description: '皮疹、瘙痒、红肿等',
          icon: 'el-icon-skin'
        }
      ],
      // 本地存储的key
      storageKey: 'ai_diagnosis_chat_history'
    };
  },
  mounted() {
    this.loadChatHistory();
  },
  methods: {
    // 加载保存的聊天历史
    loadChatHistory() {
      try {
        const savedData = localStorage.getItem(this.storageKey);
        if (savedData) {
          const { messages, diagnosisResult, lastUpdated } = JSON.parse(savedData);

          // 检查是否在今天内（24小时内有效）
          if (lastUpdated && this.isToday(new Date(lastUpdated))) {
            this.messages = messages;
            this.diagnosisResult = diagnosisResult;

            // 如果有历史记录，自动滚动到底部
            this.$nextTick(() => {
              this.scrollToBottom();
            });
            return;
          } else {
            // 如果数据不是今天的，清除并重新初始化
            console.log('聊天记录已过期，自动重置');
            this.clearStoredData();
          }
        }

        // 如果没有历史记录，添加欢迎消息
        this.addWelcomeMessage();
      } catch (error) {
        console.error('加载聊天历史失败:', error);
        this.clearStoredData();
        this.addWelcomeMessage();
      }
    },

    // 保存聊天历史到localStorage
    saveChatHistory() {
      try {
        const chatData = {
          messages: this.messages,
          diagnosisResult: this.diagnosisResult,
          lastUpdated: new Date().toISOString(),
          version: '1.0.0'
        };
        localStorage.setItem(this.storageKey, JSON.stringify(chatData));
        console.log('聊天记录已保存');
      } catch (error) {
        console.error('保存聊天历史失败:', error);
        // 如果localStorage已满，清理旧的存储项
        this.handleStorageFull(error);
      }
    },

    // 判断日期是否是今天
    isToday(date) {
      const today = new Date();
      return date.getDate() === today.getDate() &&
          date.getMonth() === today.getMonth() &&
          date.getFullYear() === today.getFullYear();
    },

    // 处理存储空间不足的情况
    handleStorageFull(error) {
      if (error && error.name === 'QuotaExceededError') {
        console.warn('localStorage存储空间不足，尝试清理...');
        // 尝试清理其他非必要的存储
        this.clearOtherStorage();
        // 尝试再次保存当前数据
        setTimeout(() => {
          try {
            localStorage.setItem(this.storageKey, JSON.stringify({
              messages: this.messages,
              diagnosisResult: this.diagnosisResult,
              lastUpdated: new Date().toISOString(),
              version: '1.0.0'
            }));
            console.log('重新保存成功');
          } catch (e) {
            console.error('存储空间仍然不足，请清除历史记录:', e);
            this.$message.warning('存储空间不足，部分功能可能受影响');
          }
        }, 100);
      }
    },

    // 清理其他非必要的存储
    clearOtherStorage() {
      // 保留必要的key，清理其他可能的临时存储
      const keysToKeep = [this.storageKey];
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (!keysToKeep.includes(key) && key.startsWith('temp_')) {
          localStorage.removeItem(key);
        }
      }
    },

    // 清除本地存储的数据
    clearStoredData() {
      localStorage.removeItem(this.storageKey);
      console.log('本地聊天记录已清除');
    },

    addWelcomeMessage() {
      // 检查是否已有欢迎消息
      const hasWelcomeMessage = this.messages.some(msg =>
          msg.type === 'ai' && msg.content.includes('AI导诊助手')
      );

      if (!hasWelcomeMessage) {
        const welcomeMessage = {
          type: 'ai',
          content: '您好！我是AI导诊助手。请详细描述您的症状，我将为您分析并推荐合适的科室。<br><br>您可以直接输入症状描述，也可以点击下方的常见症状快速选择。',
          time: this.getCurrentTime(),
          timestamp: Date.now()
        };
        this.messages.push(welcomeMessage);
        this.saveChatHistory();
      }
    },

    selectSymptom(symptom) {
      this.userInput = `我出现了${symptom.name}的症状，${symptom.description}`;
      this.sendMessage();
    },

    // 处理回车键事件
    handleEnterKey(event) {
      // 如果按下了 Shift 键，则允许换行（默认行为）
      if (event.shiftKey) {
        return;
      }
      // 否则阻止默认换行行为，并发送消息
      event.preventDefault();
      this.sendMessage();
    },

    async sendMessage() {
      if (!this.userInput.trim() || this.isLoading) return;

      // 添加用户消息
      const userMessage = {
        type: 'user',
        content: this.userInput,
        time: this.getCurrentTime(),
        timestamp: Date.now()
      };
      this.messages.push(userMessage);

      // 立即保存用户消息
      this.saveChatHistory();

      const input = this.userInput;
      this.userInput = '';
      this.isLoading = true;

      try {
        // 调用后端AI导诊接口
        const response = await getAiDiagnosis({
          symptoms: input,
          patientInfo: ''
        });

        // 处理响应
        if (response.data.code === 200) {
          // 生成AI回复消息
          this.generateAiResponse(response.data);

          // 生成诊断结果
          this.generateDiagnosisResult(response.data);
        } else {
          // 处理错误
          this.addErrorMessage(response.data.message || '导诊失败，请稍后重试');
        }
      } catch (error) {
        console.error('AI导诊请求失败:', error);
        this.addErrorMessage('导诊失败，请稍后重试');
      } finally {
        this.isLoading = false;
        this.saveChatHistory(); // 保存完整的聊天记录
        this.scrollToBottom();
      }
    },

    generateAiResponse(data) {
      const aiResponse = {
        type: 'ai',
        content: `
          根据您描述的症状，我为您进行了初步分析：<br><br>
          • 症状识别：已识别关键症状信息<br>
          • 推荐科室：<strong>${data.recommendedDepartment}</strong><br>
          • 严重程度：${data.severityLevel}<br>
          • 是否紧急：${data.needEmergency}<br><br>
          ${data.advice}
        `,
        time: this.getCurrentTime(),
        timestamp: Date.now()
      };
      this.messages.push(aiResponse);
    },

    generateDiagnosisResult(data) {
      this.diagnosisResult = {
        departments: [
          {
            id: 1,
            name: data.recommendedDepartment,
            description: '根据AI智能分析，该科室最适合您的症状',
            matchScore: 85
          }
        ],
        severityLevel: data.severityLevel,
        needEmergency: data.needEmergency,
        suggestions: [
          {
            id: 1,
            title: '及时就医',
            content: data.advice || '建议尽快到医院就诊，进行专业检查和诊断',
            icon: 'el-icon-hospital'
          },
          {
            id: 2,
            title: '注意事项',
            content: data.needEmergency === '是' ? '症状较为严重，建议立即就医或拨打120' : '保持良好的生活习惯，注意观察症状变化',
            icon: data.needEmergency === '是' ? 'el-icon-warning' : 'el-icon-info'
          }
        ],
        timestamp: Date.now()
      };
    },

    addErrorMessage(message) {
      const errorMessage = {
        type: 'ai',
        content: `<span style="color: #f56c6c;">${message}</span>`,
        time: this.getCurrentTime(),
        timestamp: Date.now()
      };
      this.messages.push(errorMessage);
    },

    goToAppointment(department) {
      this.$router.push({
        path: '/orderOperate',
        query: {
          department: department.name
        }
      });
    },

    clearChat() {
      // 清除所有数据
      this.messages = [];
      this.diagnosisResult = null;
      this.userInput = '';

      // 清除本地存储
      this.clearStoredData();

      // 添加新的欢迎消息
      this.addWelcomeMessage();
    },

    // 扩展清除功能，添加确认对话框
    clearChatWithConfirm() {
      this.$confirm('确定要重置对话吗？这将清空所有聊天记录。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'clear-chat-confirm'
      }).then(() => {
        this.clearChat();
        this.$message({
          message: '对话已重置',
          type: 'success',
          duration: 2000
        });
      }).catch(() => {
        // 用户取消操作
        console.log('用户取消重置对话');
      });
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages;
        if (chatMessages) {
          chatMessages.scrollTop = chatMessages.scrollHeight;
        }
      });
    },

    getCurrentTime() {
      return new Date().toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      });
    },

    formatDiagnosisTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      });
    },

    getScoreColor(score) {
      if (score >= 80) return '#67c23a';
      if (score >= 60) return '#e6a23c';
      return '#f56c6c';
    },

    getSeverityType(level) {
      switch (level) {
        case '严重':
          return 'error';
        case '中等':
          return 'warning';
        case '轻微':
          return 'success';
        default:
          return 'info';
      }
    }
  },

  // 监听数据变化，自动保存
  watch: {
    messages: {
      handler(newMessages) {
        // 只有在有实际变化且不是初始加载时保存
        if (newMessages && newMessages.length > 0) {
          this.$nextTick(() => {
            this.saveChatHistory();
          });
        }
      },
      deep: true
    },

    diagnosisResult: {
      handler(newResult) {
        if (newResult) {
          this.$nextTick(() => {
            this.saveChatHistory();
          });
        }
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.ai-diagnosis-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px;
}

.diagnosis-header-section {
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}

.diagnosis-title-content {
  position: relative;
  z-index: 2;
}

.diagnosis-title-icon {
  font-size: 36px;
  color: white;
  margin-bottom: 10px;
}

.diagnosis-page-title {
  font-size: 24px;
  font-weight: bold;
  color: white;
  margin-bottom: 8px;
}

.diagnosis-page-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.diagnosis-decoration-element {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
}

.diagnosis-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.diagnosis-circle-1 {
  width: 80px;
  height: 80px;
  top: -15px;
  right: 15%;
  animation: float 6s ease-in-out infinite;
}

.diagnosis-circle-2 {
  width: 50px;
  height: 50px;
  top: 15px;
  right: 8%;
  animation: float 4s ease-in-out infinite reverse;
}

.diagnosis-circle-3 {
  width: 35px;
  height: 35px;
  bottom: -8px;
  right: 12%;
  animation: float 5s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

.diagnosis-container {
  max-width: 1000px;
  margin: 0 auto;
  width: 100%;
}

.diagnosis-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  align-items: start;
  width: 100%;
  min-width: 0;
}

.chat-section {
  background: white;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 15px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.ai-avatar {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.ai-info h3 {
  margin: 0 0 3px 0;
  font-size: 16px;
}

.ai-info p {
  margin: 0;
  font-size: 12px;
  opacity: 0.8;
}

.ai-status {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.status-dot.online {
  background: #67c23a;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

.status-text {
  font-size: 12px;
}

.chat-messages {
  height: 320px;
  overflow-y: auto;
  padding: 15px;
  background: #f8f9fa;
}

.message-item {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
  gap: 10px;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
}

.message-item.ai .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-item.user .message-avatar {
  background: #e1f3d8;
  color: #67c23a;
}

.message-content {
  max-width: 70%;
}

.message-text {
  background: white;
  padding: 10px 14px;
  border-radius: 15px;
  margin-bottom: 4px;
  line-height: 1.5;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  font-size: 14px;
}

.message-item.user .message-text {
  background: #67c23a;
  color: white;
}

.message-time {
  font-size: 11px;
  color: #999;
  text-align: right;
}

.message-item.user .message-time {
  text-align: left;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 10px 14px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.quick-symptoms {
  padding: 15px;
  background: white;
  border-top: 1px solid #eee;
}

.quick-symptoms h4 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 14px;
}

.symptoms-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.symptom-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.symptom-card:hover {
  border-color: #667eea;
  background: #f8f9ff;
  transform: translateY(-2px);
  box-shadow: 0 3px 10px rgba(102, 126, 234, 0.2);
}

.symptom-icon {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.symptom-info h5 {
  margin: 0 0 4px 0;
  color: #333;
  font-size: 13px;
}

.symptom-info p {
  margin: 0;
  color: #666;
  font-size: 11px;
}

.chat-input-section {
  padding: 15px;
  background: white;
  border-top: 1px solid #eee;
}

.input-container {
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.input-container .el-textarea {
  flex: 1;
}

.input-actions {
  display: flex;
  gap: 8px;
}

.input-tips {
  margin-top: 8px;
  font-size: 11px;
  color: #999;
}

.input-tips i {
  margin-right: 4px;
}

.diagnosis-result {
  background: white;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.result-header {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: white;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-header h3 {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

.result-timestamp {
  font-size: 11px;
  opacity: 0.9;
  background: rgba(255, 255, 255, 0.2);
  padding: 3px 6px;
  border-radius: 4px;
}

.result-content {
  padding: 15px;
}

.result-section {
  margin-bottom: 20px;
}

.result-section h4 {
  margin: 0 0 12px 0;
  color: #333;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.department-recommendations {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.dept-card {
  border: 2px solid #f0f0f0;
  border-radius: 10px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.dept-card:hover {
  border-color: #67c23a;
  box-shadow: 0 3px 10px rgba(103, 194, 58, 0.2);
}

.dept-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.dept-header h5 {
  margin: 0;
  color: #333;
  font-size: 14px;
}

.match-score {
  display: flex;
  align-items: center;
  gap: 8px;
}

.match-score .el-progress {
  width: 60px;
}

.score-text {
  font-weight: bold;
  color: #67c23a;
  font-size: 13px;
}

.dept-desc {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 12px;
}

.dept-actions {
  display: flex;
  gap: 8px;
}

.suggestions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.suggestion-item {
  display: flex;
  gap: 10px;
  padding: 12px;
  background: #f0f9ff;
  border-radius: 6px;
  border-left: 3px solid #409eff;
}

.suggestion-icon {
  width: 30px;
  height: 30px;
  background: #409eff;
  color: white;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.suggestion-content h5 {
  margin: 0 0 4px 0;
  color: #333;
  font-size: 13px;
}

.suggestion-content p {
  margin: 0;
  color: #666;
  font-size: 12px;
  line-height: 1.5;
}

.disclaimer {
  margin-top: 15px;
}

.disclaimer p {
  margin: 4px 0;
  font-size: 11px;
  line-height: 1.4;
}

@media (max-width: 900px) {
  .diagnosis-content {
    grid-template-columns: 1fr;
  }

  .symptoms-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .input-container {
    flex-direction: column;
    align-items: stretch;
  }

  .input-actions {
    justify-content: center;
  }

  .result-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .result-timestamp {
    align-self: flex-end;
  }
}

@media (max-width: 600px) {
  .ai-diagnosis-container {
    padding: 10px;
  }

  .symptoms-grid {
    grid-template-columns: 1fr;
  }

  .diagnosis-page-title {
    font-size: 20px;
  }
}
</style>

<style>
/* 为确认对话框添加自定义样式 */
.clear-chat-confirm {
  min-width: 300px;
}

.clear-chat-confirm .el-message-box__content {
  padding: 20px;
}

.clear-chat-confirm .el-message-box__message {
  color: #606266;
  line-height: 1.6;
}

.clear-chat-confirm .el-message-box__btns {
  padding: 10px 20px 20px;
}
</style>
