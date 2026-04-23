<template>
  <div class="login-container">
    <div class="main-content">
      <!-- 左侧蓝色区域保持不变 -->
      <div class="left-panel">
        <div class="left-content">
          <h1 class="hospital-name">智慧医疗</h1>
          <h2 class="system-title">医院智能挂号管理系统</h2>
          <p class="system-slogan">专业·高效·安全·便捷</p>

          <div class="features">
            <div class="feature-item">
              <i class="el-icon-s-order feature-icon"></i>
              <div class="feature-text">
                <h3>在线预约挂号</h3>
                <p>支持7×24小时在线预约</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-s-claim feature-icon"></i>
              <div class="feature-text">
                <h3>智能分诊导医</h3>
                <p>智能分诊推荐</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-notebook-2 feature-icon"></i>
              <div class="feature-text">
                <h3>电子病历管理</h3>
                <p>个人健康档案云端存储</p>
              </div>
            </div>
            <div class="feature-item">
              <i class="el-icon-time feature-icon"></i>
              <div class="feature-text">
                <h3>实时排队叫号</h3>
                <p>实时查询排队进度</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录区域 -->
      <div class="right-panel">
        <div class="login-card">
          <div class="login-header">
            <h2>欢迎登录</h2>
            <p>请选择登录方式</p>
          </div>

          <!-- 登录方式切换标签 -->
          <div class="login-type-tabs">
            <div
                class="type-tab"
                :class="{ 'active': loginType === 'password' }"
                @click="switchLoginType('password')"
            >
              <i class="el-icon-user"></i>
              <span>账号密码登录</span>
            </div>
            <div
                class="type-tab"
                :class="{ 'active': loginType === 'emailCode' }"
                @click="switchLoginType('emailCode')"
            >
              <i class="el-icon-message"></i>
              <span>邮箱验证码登录</span>
            </div>
          </div>

          <!-- 账号密码登录表单 -->
          <el-form
              v-if="loginType === 'password'"
              :model="loginForm"
              style="width: 100%"
              :rules="loginRules"
              ref="passwordForm"
              class="login-form"
          >
            <el-form-item prop="id">
              <el-input v-model="loginForm.id" placeholder="请输入账号" size="medium">
                <i slot="prefix" class="el-input__icon el-icon-user"></i>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                  v-model="loginForm.password"
                  size="medium"
                  placeholder="请输入密码"
                  clearable
                  show-password
              >
                <i slot="prefix" class="el-input__icon el-icon-lock"></i>
              </el-input>
            </el-form-item>

            <div class="role-section">
              <div class="role-label">登录身份</div>
              <div class="role-cards">
                <div
                    v-for="(item, index) in roleOptions"
                    :key="item.value"
                    class="role-card"
                    :class="{ 'active': role === item.value }"
                    @click="selectRole(item.value)"
                    :style="{ animationDelay: index * 0.1 + 's' }"
                >
                  <div class="role-icon-wrapper">
                    <i :class="item.icon"></i>
                  </div>
                  <span class="role-name">{{ item.label }}</span>
                  <div class="role-card-bg"></div>
                </div>
              </div>
              <div class="role-hint" :class="{ 'show': role }">
                <i class="el-icon-check-circle"></i>
                <span>您已选择：{{ role }}身份登录</span>
              </div>
            </div>

            <el-button
                type="primary"
                style="width: 100%; margin-top: 20px; height: 40px;"
                @click="submitPasswordLogin('passwordForm')"
                :loading="passwordLoading"
            >
              {{ passwordLoading ? '登录中...' : '登录系统' }}
            </el-button>
          </el-form>

          <!-- 邮箱验证码登录表单 -->
          <el-form
              v-else
              :model="emailLoginForm"
              style="width: 100%"
              :rules="emailLoginRules"
              ref="emailForm"
              class="login-form"
          >
            <el-form-item prop="phone">
              <el-input
                  v-model="emailLoginForm.phone"
                  placeholder="请输入手机号"
                  size="medium"
                  @blur="checkPhoneBound"
              >
                <i slot="prefix" class="el-input__icon el-icon-phone"></i>
              </el-input>
            </el-form-item>

            <el-form-item prop="code" class="code-form-item">
              <el-input
                  v-model="emailLoginForm.code"
                  size="medium"
                  placeholder="请输入验证码"
                  :disabled="!canSendCode"
              >
                <i slot="prefix" class="el-input__icon el-icon-key"></i>
              </el-input>
              <el-button
                  type="primary"
                  plain
                  size="medium"
                  style="margin-left: 10px; min-width: 100px;"
                  @click="sendEmailCode"
                  :disabled="!canSendCode || codeSending"
                  :loading="codeSending"
              >
                {{ codeButtonText }}
              </el-button>
            </el-form-item>

            <div v-if="boundEmail" class="email-hint">
              <i class="el-icon-message"></i>
              <span>验证码将发送到：{{ boundEmail }}</span>
            </div>

            <div v-else-if="emailLoginForm.phone && !checkingPhone" class="register-hint">
              <i class="el-icon-info"></i>
              <span>该手机号未绑定邮箱，请先注册或使用账号密码登录</span>
            </div>

            <div class="role-section">
              <div class="role-label">登录身份</div>
              <div class="role-cards">
                <div
                    v-for="(item, index) in roleOptions"
                    :key="item.value"
                    class="role-card"
                    :class="{ 'active': emailLoginRole === item.value }"
                    @click="selectEmailRole(item.value)"
                    :style="{ animationDelay: index * 0.1 + 's' }"
                >
                  <div class="role-icon-wrapper">
                    <i :class="item.icon"></i>
                  </div>
                  <span class="role-name">{{ item.label }}</span>
                  <div class="role-card-bg"></div>
                </div>
              </div>
              <div class="role-hint" :class="{ 'show': emailLoginRole }">
                <i class="el-icon-check-circle"></i>
                <span>您已选择：{{ emailLoginRole }}身份登录</span>
              </div>
            </div>

            <el-button
                type="primary"
                style="width: 100%; margin-top: 20px; height: 40px;"
                @click="submitEmailLogin('emailForm')"
                :disabled="!boundEmail"
                :loading="emailLoading"
            >
              {{ emailLoading ? '登录中...' : '登录系统' }}
            </el-button>
          </el-form>

          <!-- 公共底部区域 -->
          <div class="login-footer">
            <div style="display: flex; justify-content: space-between; margin-top: 20px; font-size: 12px;">
              <div>
                还没有账号？请
                <span
                    style="color: #1890ff; cursor: pointer"
                    @click="registerFormVisible = true"
                >立即注册</span
                >
              </div>
              <div v-if="loginType === 'password'">
                <span
                    style="color: #1890ff; cursor: pointer"
                    @click="findFormVisible = true"
                >忘记密码？</span
                >
              </div>
            </div>

            <div style="margin-top: 30px; text-align: center;">

              <div style="display: flex; justify-content: center; gap: 15px;">

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 原有的注册、找回密码弹窗保持不变 -->
    <el-dialog title="患者注册" :visible.sync="registerFormVisible">
      <el-form
          class="findPassword"
          :model="registerForm"
          :rules="registerRules"
          ref="registerForm"
      >
        <el-form-item label="账号" label-width="80px" prop="pId">
          <el-input v-model.number="registerForm.pId"></el-input>
        </el-form-item>
        <el-form-item label="性别" label-width="80px">
          <el-radio v-model="registerForm.pGender" label="男">男</el-radio>
          <el-radio v-model="registerForm.pGender" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="密码" label-width="80px" prop="pPassword">
          <el-input v-model="registerForm.pPassword"></el-input>
        </el-form-item>
        <el-form-item label="姓名" label-width="80px" prop="pName">
          <el-input v-model="registerForm.pName"></el-input>
        </el-form-item>
        <el-form-item label="出生日期" label-width="80px" prop="pBirthday">
          <el-date-picker
              v-model="registerForm.pBirthday"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="手机号" label-width="80px" prop="pPhone">
          <el-input v-model="registerForm.pPhone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱号" label-width="80px" prop="pEmail">
          <el-input v-model="registerForm.pEmail"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" label-width="80px" prop="pCard">
          <el-input v-model="registerForm.pCard"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="registerFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="registerClick('registerForm')"
        >确 定</el-button
        >
      </div>
    </el-dialog>

    <el-dialog
        title="找回密码"
        :visible.sync="findFormVisible"
        width="400px"
        :close-on-click-modal="false"
    >
      <el-form
          :model="findForm"
          :rules="findRules"
          ref="findForm"
          label-width="80px"
      >
        <el-form-item label="邮箱" prop="pEmail">
          <el-input v-model="findForm.pEmail" placeholder="请输入注册邮箱" />
        </el-form-item>

        <el-form-item label="验证码" prop="code" class="code-item">
          <el-input v-model="findForm.code" placeholder="请输入验证码" style="width: 200px" />
          <el-button
              type="primary"
              :disabled="!canClick"
              @click="sendEmail"
              style="margin-left: 10px"
          >
            {{ content }}
          </el-button>
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="findForm.newPassword"
              type="password"
              placeholder="请输入新密码"
              show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="checkPassword">
          <el-input
              v-model="findForm.checkPassword"
              type="password"
              placeholder="请再次输入密码"
              show-password
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="findFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="findPassword('findForm')">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";

export default {
  name: "Login",
  data() {
    // 原有的验证规则保持不变
    var validateMoblie = (rule, value, callback) => {
      if (value === undefined || value === '') {
        callback(new Error("请输入手机号"));
      } else {
        let reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
        if (!reg.test(value)) {
          callback(new Error("请输入合法的手机号"));
        }
        callback();
      }
    };
    var validateCard = (rule, value, callback) => {
      if (value === undefined) {
        callback(new Error("请输入身份证号"));
      } else {
        let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if (!reg.test(value)) {
          callback(new Error("请输入合法的身份证号码"));
        }
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.findForm.checkPassword !== "") {
          this.$refs.findForm.validateField("checkPassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.findForm.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };

    return {
      // 登录方式：password-账号密码，emailCode-邮箱验证码
      loginType: "password",
      // 账号密码登录相关
      loginForm: {
        id: "",
        password: ""
      },
      loginRules: {
        id: [
          { required: true, message: "请输入账号编号", trigger: "blur" },
          { min: 3, max: 50, message: "长度在 3到 50 个字符", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },
      role: "患者",
      passwordLoading: false,

      // 邮箱验证码登录相关
      emailLoginForm: {
        phone: "",
        code: ""
      },
      emailLoginRules: {
        phone: [{ validator: validateMoblie, trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { len: 6, message: "验证码为6位数字", trigger: "blur" }
        ]
      },
      emailLoginRole: "患者",
      boundEmail: "", // 绑定的邮箱
      checkingPhone: false, // 检查手机号状态

      // 验证码发送相关
      codeTotalTime: 60,
      codeCountdown: 0,
      codeSending: false,
      canSendCode: false,

      // 原有数据保持不变
      roleOptions: [
        { value: "患者", label: "患者", icon: "el-icon-user", color: "#1890ff" },
        { value: "医生", label: "医生", icon: "el-icon-s-custom", color: "#52c41a" },
        { value: "管理员", label: "管理员", icon: "el-icon-s-tools", color: "#fa8c16" }
      ],
      findRole: "患者",
      findFormVisible: false,
      findForm: {
        code: "",
        newPassword: "",
        checkPassword: "",
        pEmail: ""
      },
      findRules: {
        pEmail: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
        newPassword: [{ validator: validatePass, trigger: "blur" }],
        checkPassword: [{ validator: validatePass2, trigger: "blur" }]
      },
      totalTime: 60,
      content: "发送验证码",
      canClick: true,
      registerFormVisible: false,
      registerForm: {
        pGender: "男"
      },
      registerRules: {
        pId: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { type: "number", message: "账号必须数字类型", trigger: "blur" }
        ],
        pPassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 4, max: 50, message: "长度在 4到 50 个字符", trigger: "blur" }
        ],
        pName: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          { min: 2, max: 8, message: "长度在 2到 8 个字符", trigger: "blur" }
        ],
        pEmail: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        pPhone: [{ validator: validateMoblie }],
        pCard: [{ validator: validateCard }],
        pBirthday: [
          { required: true, message: "选择出生日期", trigger: "blur" }
        ]
      },
      emailLoading: false
    };
  },
  computed: {
    // 验证码按钮文本
    codeButtonText() {
      if (this.codeSending) return '发送中...';
      if (this.codeCountdown > 0) return `${this.codeCountdown}s后重发`;
      return '获取验证码';
    }
  },
  watch: {
    // 监听手机号输入，验证格式
    'emailLoginForm.phone'(newVal) {
      const phoneReg = /^1[3-9]\d{9}$/;
      this.canSendCode = phoneReg.test(newVal);

      // 清除之前的定时器
      if (this.checkPhoneTimer) {
        clearTimeout(this.checkPhoneTimer);
      }

      // 防抖检查手机号绑定状态
      if (phoneReg.test(newVal)) {
        this.checkPhoneTimer = setTimeout(() => {
          this.checkPhoneBound();
        }, 800);
      } else {
        this.boundEmail = '';
      }
    }
  },
  methods: {
    // 切换登录方式
    switchLoginType(type) {
      this.loginType = type;
      if (type === 'emailCode') {
        // 切换到邮箱登录时，重置表单
        this.$nextTick(() => {
          if (this.$refs.emailForm) {
            this.$refs.emailForm.clearValidate();
          }
        });
      } else {
        this.$nextTick(() => {
          if (this.$refs.passwordForm) {
            this.$refs.passwordForm.clearValidate();
          }
        });
      }
    },

    // 选择角色（账号密码登录）
    selectRole(roleValue) {
      this.role = roleValue;
      this.addRoleAnimation();
    },

    // 选择角色（邮箱验证码登录）
    selectEmailRole(roleValue) {
      this.emailLoginRole = roleValue;
      this.addRoleAnimation();
    },

    // 添加角色选择动画
    addRoleAnimation() {
      this.$nextTick(() => {
        const cards = document.querySelectorAll('.role-card');
        cards.forEach(card => {
          if (card.classList.contains('active')) {
            card.style.transform = 'scale(0.95)';
            setTimeout(() => {
              card.style.transform = '';
            }, 150);
          }
        });
      });
    },

    // 检查手机号是否绑定邮箱
    async checkPhoneBound() {
      console.log('=== 前端检查绑定状态 ===');
      console.log('输入手机号:', this.emailLoginForm.phone);

      if (!this.emailLoginForm.phone || !/^1[3-9]\d{9}$/.test(this.emailLoginForm.phone)) {
        console.log('手机号格式无效');
        this.boundEmail = '';
        return;
      }

      this.checkingPhone = true;
      try {
        const response = await request.get('/api/email-code/check-bound', {
          params: { phone: this.emailLoginForm.phone }
        });

        console.log('接口响应:', response.data);
        console.log('isBound字段:', response.data.isBound);
        console.log('boundEmail字段:', response.data.boundEmail);

        if (response.data.code === 200) {
          if (response.data.isBound === true) {
            this.boundEmail = response.data.boundEmail || '';
            console.log('设置为已绑定:', this.boundEmail);
          } else {
            this.boundEmail = '';
            console.log('设置为未绑定');
          }
        }
      } catch (error) {
        console.error('检查失败:', error);
        this.boundEmail = '';
      } finally {
        this.check = false;
      }
    },
    // 发送邮箱验证码
    async sendEmailCode() {
      if (!this.canSendCode || this.codeCountdown > 0 || this.codeSending) {
        return;
      }

      this.codeSending = true;
      try {
        const response = await request.post('/api/auth/phone/send-login-code', {
          phone: this.emailLoginForm.phone
        });

        if (response.data.code === 200) {
          this.$message.success('验证码已发送到您的邮箱');

          // 开始倒计时
          this.codeCountdown = this.codeTotalTime;
          const timer = setInterval(() => {
            this.codeCountdown--;
            if (this.codeCountdown <= 0) {
              clearInterval(timer);
            }
          }, 1000);

        } else if (response.data.code === 404) {
          this.$message.warning('手机号未绑定邮箱，请先注册');
          this.boundEmail = '';
        } else {
          this.$message.error(response.data.message || '验证码发送失败');
        }
      } catch (error) {
        console.error('发送验证码失败:', error);
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.codeSending = false;
      }
    },

    // 账号密码登录
    submitPasswordLogin(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.passwordLoading = true;

          const apiMap = {
            "患者": { url: "patient/login", params: { pId: this.loginForm.id, pPassword: this.loginForm.password } },
            "医生": { url: "doctor/login", params: { dId: this.loginForm.id, dPassword: this.loginForm.password } },
            "管理员": { url: "admin/login", params: { aId: this.loginForm.id, aPassword: this.loginForm.password } }
          };
          const routeMap = {
            "患者": "/patientLayout",
            "医生": "/doctorLayout",
            "管理员": "/adminLayout"
          };

          const params = new URLSearchParams();
          Object.entries(apiMap[this.role].params).forEach(([key, value]) => {
            params.append(key, value);
          });

          request.post(apiMap[this.role].url, params)
              .then(res => {
                this.passwordLoading = false;
                if (res.data.status != 200) {
                  this.$message.error("用户名或密码错误");
                  return;
                }
                setToken(res.data.data.token);
                this.$router.push(routeMap[this.role]);
                this.$message.success("登录成功");
              })
              .catch(() => {
                this.passwordLoading = false;
                this.$message.error("用户名或密码错误");
              });
        }
      });
    },

    // 邮箱验证码登录
    submitEmailLogin(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.emailLoading = true;

          request.post('/api/auth/phone/login', {
            phone: this.emailLoginForm.phone,
            code: this.emailLoginForm.code,
            userType: this.emailLoginRole
          })
              .then(res => {
                this.emailLoading = false;
                if (res.data.code !== 200) {
                  this.$message.error(res.data.message || '登录失败');
                  return;
                }

                // 设置token（模拟，您需要根据实际返回修改）
                if (res.data.token) {
                  setToken(res.data.token);
                }

                // 跳转到对应页面
                const routeMap = {
                  "患者": "/patientLayout",
                  "医生": "/doctorLayout",
                  "管理员": "/adminLayout"
                };

                this.$router.push(routeMap[this.emailLoginRole]);
                this.$message.success("登录成功");
              })
              .catch(error => {
                this.emailLoading = false;
                console.error('登录失败:', error);
                this.$message.error("登录失败，请重试");
              });
        }
      });
    },

    // 原有方法保持不变
    registerClick(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          request
              .get("patient/addPatient", {
                params: {
                  pId: this.registerForm.pId,
                  pName: this.registerForm.pName,
                  pPassword: this.registerForm.pPassword,
                  pGender: this.registerForm.pGender,
                  pEmail: this.registerForm.pEmail,
                  pPhone: this.registerForm.pPhone,
                  pCard: this.registerForm.pCard,
                  pBirthday: this.registerForm.pBirthday
                }
              })
              .then(res => {
                if (res.data.status !== 200)
                  return this.$message.error("账号或邮箱已被占用！");
                this.registerFormVisible = false;
                this.$message.success("注册成功！");
              });
        }
      });
    },

    findPassword(findForm) {
      this.$refs[findForm].validate(valid => {
        if (valid) {
          const apiMap = {
            "患者": "patient/findPassword",
            "医生": "doctor/findPassword",
            "管理员": "admin/findPassword"
          };
          const paramsMap = {
            "患者": { pEmail: this.findForm.pEmail, pPassword: this.findForm.newPassword, code: this.findForm.code },
            "医生": { dEmail: this.findForm.pEmail, dPassword: this.findForm.newPassword, code: this.findForm.code },
            "管理员": { aEmail: this.findForm.pEmail, aPassword: this.findForm.newPassword, code: this.findForm.code }
          };

          request.get(apiMap[this.findRole], { params: paramsMap[this.findRole] })
              .then(res => {
                if (res.data.status !== 200) return this.$message.error("验证码错误或已过期！");
                this.$message.success("密码修改成功！");
                this.findFormVisible = false;
              });
        }
      });
    },

    sendEmail() {
      if (!this.canClick) return;

      // 邮箱格式校验
      const emailReg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailReg.test(this.findForm.pEmail)) {
        this.$message.error("邮箱格式不正确");
        return;
      }

      this.canClick = false;
      this.content = this.totalTime + "s后重新发送";
      let clock = window.setInterval(() => {
        this.totalTime--;
        this.content = this.totalTime + "s后重新发送";
        if (this.totalTime < 0) {
          window.clearInterval(clock);
          this.content = "重新发送验证码";
          this.totalTime = 10;
          this.canClick = true;
        }
      }, 1000);

      // 调用新的发送验证码接口
      request.post("/api/email-code/send-by-email", {
        email: this.findForm.pEmail
      }).then(res => {
        if (!res.data.success) {
          this.$message.error(res.data.message || "发送验证码失败");
          // 发送失败，重置倒计时
          window.clearInterval(clock);
          this.content = "重新发送验证码";
          this.totalTime = 10;
          this.canClick = true;
          return;
        }
        this.$message.success("验证码发送成功！");
      }).catch(error => {
        console.error("发送验证码失败：", error);
        this.$message.error("发送验证码失败，请稍后重试");
        // 发送失败，重置倒计时
        window.clearInterval(clock);
        this.content = "重新发送验证码";
        this.totalTime = 10;
        this.canClick = true;
      });
    }
  },
  beforeDestroy() {
    // 清理定时器
    if (this.checkPhoneTimer) {
      clearTimeout(this.checkPhoneTimer);
    }
  }
};
</script>

<style scoped>
/* 新增的登录方式切换样式 */
.login-type-tabs {
  display: flex;
  background: #f5f7fa;
  border-radius: 8px;
  padding: 4px;
  margin-bottom: 25px;
  border: 1px solid #e4e7ed;
}

.type-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #606266;
  gap: 8px;
}

.type-tab:hover {
  background: #ecf5ff;
  color: #1890ff;
}

.type-tab.active {
  background: white;
  color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.1);
  font-weight: 500;
}

.type-tab i {
  font-size: 16px;
}

/* 邮箱验证码表单特定样式 */
.code-form-item {
  margin-bottom: 10px;
}

.code-form-item ::v-deep .el-form-item__content {
  display: flex;
  align-items: center;
}

.email-hint {
  margin: 10px 0 20px 0;
  padding: 10px 12px;
  background: #f6ffed;
  border: 1px solid #d9f7be;
  border-radius: 6px;
  font-size: 12px;
  color: #52c41a;
  display: flex;
  align-items: center;
  gap: 6px;
  animation: fadeIn 0.3s ease;
}

.email-hint i {
  font-size: 14px;
  color: #52c41a;
}

.register-hint {
  margin: 10px 0 20px 0;
  padding: 10px 12px;
  background: #fff7e6;
  border: 1px solid #ffd591;
  border-radius: 6px;
  font-size: 12px;
  color: #fa8c16;
  display: flex;
  align-items: center;
  gap: 6px;
  animation: fadeIn 0.3s ease;
}

.register-hint i {
  font-size: 14px;
  color: #fa8c16;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 原有样式保持不变，只添加新样式 */
.role-section {
  margin: 25px 0;
}

.role-label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.role-label::before {
  content: '';
  width: 3px;
  height: 16px;
  background: #1890ff;
  border-radius: 2px;
}

.role-cards {
  display: flex;
  gap: 12px;
  margin-top: 12px;
  position: relative;
}

.role-card {
  flex: 1;
  background: #fafafa;
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  padding: 20px 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
  animation: slideInUp 0.6s ease-out forwards;
  opacity: 0;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.role-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  margin-bottom: 10px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.role-card:nth-child(1) .role-icon-wrapper {
  background: linear-gradient(135deg, #1890ff, #40a9ff);
}

.role-card:nth-child(2) .role-icon-wrapper {
  background: linear-gradient(135deg, #52c41a, #73d13d);
}

.role-card:nth-child(3) .role-icon-wrapper {
  background: linear-gradient(135deg, #fa8c16, #ffa940);
}

.role-card i {
  font-size: 20px;
  color: white;
  transition: all 0.3s ease;
}

.role-name {
  font-size: 13px;
  color: #666;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.role-card-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #1890ff, #40a9ff);
  opacity: 0;
  transition: all 0.4s ease;
  z-index: 1;
  border-radius: 10px;
}

.role-card:nth-child(2) .role-card-bg {
  background: linear-gradient(135deg, #52c41a, #73d13d);
}

.role-card:nth-child(3) .role-card-bg {
  background: linear-gradient(135deg, #fa8c16, #ffa940);
}

.role-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(24, 144, 255, 0.15);
  border-color: #1890ff;
}

.role-card:hover .role-icon-wrapper {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.role-card:hover .role-name {
  color: white;
}

.role-card:hover .role-card-bg {
  opacity: 1;
}

.role-card.active {
  border-color: #1890ff;
  background: white;
  transform: scale(1.02);
  box-shadow: 0 4px 16px rgba(24, 144, 255, 0.2);
}

.role-card.active .role-icon-wrapper {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.4);
}

.role-card.active .role-name {
  color: #1890ff;
  font-weight: 600;
}

.role-card.active .role-card-bg {
  opacity: 0.08;
}

.role-hint {
  margin-top: 12px;
  padding: 8px 12px;
  background: #f6ffed;
  border: 1px solid #d9f7be;
  border-radius: 6px;
  font-size: 12px;
  color: #52c41a;
  display: flex;
  align-items: center;
  gap: 6px;
  opacity: 0;
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

.role-hint.show {
  opacity: 1;
  transform: translateY(0);
}

.role-hint i {
  font-size: 14px;
  color: #52c41a;
}

/* 保留原有其他样式 */
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  position: relative;
}

.main-content {
  flex: 1;
  display: flex;
  min-height: 100vh;
}

.left-panel {
  flex: 1;
  background: linear-gradient(135deg, #1890ff 0%, #36c4f0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.left-panel::before {
  content: '';
  position: absolute;
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  top: -100px;
  right: -100px;
}

.left-panel::after {
  content: '';
  position: absolute;
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  bottom: -50px;
  left: -50px;
}

.left-content {
  max-width: 500px;
  width: 100%;
  color: white;
  z-index: 1;
}

.hospital-name {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 10px;
  letter-spacing: 1px;
}

.system-title {
  font-size: 20px;
  font-weight: 500;
  margin-bottom: 8px;
  opacity: 0.95;
}

.system-slogan {
  font-size: 14px;
  opacity: 0.8;
  margin-bottom: 50px;
  letter-spacing: 1px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(5px);
}

.feature-icon {
  font-size: 24px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
}

.feature-text h3 {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 4px 0;
}

.feature-text p {
  font-size: 12px;
  opacity: 0.9;
  margin: 0;
  line-height: 1.4;
}

.right-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: white;
}

.login-card {
  max-width: 380px;
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 0;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  font-size: 20px;
  color: #333;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.login-header p {
  font-size: 13px;
  color: #666;
  margin: 0;
}

.login-footer {
  margin-top: 20px;
}

::v-deep .el-input__inner {
  height: 40px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  font-size: 14px;
}

::v-deep .el-input__inner:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
}

::v-deep .el-input__prefix {
  left: 8px;
  color: #999;
}

::v-deep .el-input__prefix i {
  font-size: 16px;
}

::v-deep .el-button--primary {
  background: #1890ff;
  border-color: #1890ff;
  height: 40px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
}

::v-deep .el-button--primary:hover {
  background: #40a9ff;
  border-color: #40a9ff;
}

a, .link-text {
  color: #1890ff;
  cursor: pointer;
  text-decoration: none;
  transition: color 0.2s;
}

a:hover, .link-text:hover {
  color: #40a9ff;
  text-decoration: underline;
}

::v-deep .el-button--default {
  border: 1px solid #e0e0e0;
  color: #666;
}

::v-deep .el-button--default:hover {
  border-color: #1890ff;
  color: #1890ff;
}

@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }

  .left-panel {
    padding: 30px 20px;
  }

  .right-panel {
    padding: 30px 20px;
  }
}
</style>