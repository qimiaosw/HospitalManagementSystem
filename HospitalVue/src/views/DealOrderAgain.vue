<template>
  <div class="treatment-management">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-content">
        <i class="el-icon-first-aid-kit title-icon"></i>
        <h1 class="page-title">诊疗处理</h1>
        <p class="page-subtitle">患者诊疗信息处理与药品检查项目管理</p>
      </div>
      <div class="decoration-element">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <el-card class="management-card" shadow="hover">
      <!-- 患者基本信息 -->
      <div class="patient-info-section">
        <div class="section-header">
          <i class="el-icon-user patient-icon"></i>
          <h3 class="section-title">患者基本信息</h3>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">挂号单号：</label>
              <el-input disabled v-model="oId" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-tickets"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">患者账号：</label>
              <el-input disabled v-model="pId" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">患者姓名：</label>
              <el-input disabled v-model="pName" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-user-solid"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="action-item">
              <el-button 
                type="success" 
                @click="submitClick"
                class="submit-btn"
                :loading="submitting"
              >
                <i class="el-icon-check"></i>
                提交诊疗
              </el-button>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">患者性别：</label>
              <el-input disabled v-model="pGender" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-male"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">联系方式：</label>
              <el-input disabled v-model="pPhone" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-phone"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label class="info-label">主治医生：</label>
              <el-input disabled v-model="dName" class="info-input">
                <template slot="prepend">
                  <i class="el-icon-user-solid"></i>
                </template>
              </el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="action-item">
              <el-button 
                type="primary" 
                @click="openAdvice"
                class="advice-btn"
              >
                <i class="el-icon-edit"></i>
                诊断/意见
              </el-button>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 药品管理 -->
      <div class="drug-management-section">
        <div class="section-header">
          <i class="el-icon-medal drug-icon"></i>
          <h3 class="section-title">药品管理</h3>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 药品列表 -->
            <el-card class="sub-card" shadow="hover">
              <div class="card-header">
                <div class="search-box">
                  <el-input
                    v-model="queryDrug"
                    placeholder="请输入药品名称查询"
                    class="search-input"
                  >
                    <el-button
                      slot="append"
                      icon="el-icon-search"
                      @click="requestDrug"
                      class="search-btn"
                    >搜索</el-button>
                  </el-input>
                </div>
              </div>
              
              <div class="table-container">
                <el-table 
                  :data="drugData" 
                  stripe 
                  border
                  class="data-table"
                  v-loading="loading"
                  height="400"
                >
                  <el-table-column 
                    label="药品编号" 
                    prop="drId"
                    width="100"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="药品名称" 
                    prop="drName"
                    min-width="120"
                  ></el-table-column>
                  <el-table-column 
                    label="库存数量" 
                    prop="drNumber"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-tag 
                        :type="scope.row.drNumber > 0 ? 'success' : 'danger'"
                        class="stock-tag"
                      >
                        {{ scope.row.drNumber }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="单位" 
                    prop="drUnit"
                    width="80"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="单价" 
                    prop="drPrice"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <span class="price-text">¥{{ scope.row.drPrice }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="操作" 
                    width="100" 
                    fixed="right"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-button
                        type="success"
                        size="mini"
                        @click="addDrug(scope.row.drId)"
                        class="add-btn"
                        :disabled="scope.row.drNumber <= 0"
                      >
                        <i class="el-icon-circle-plus"></i>
                        添加
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              
              <div class="pagination-section">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  background
                  layout="total, sizes, prev, pager, next"
                  :total="total"
                  :page-size="size"
                  :page-sizes="[4, 8, 16, 32]"
                  class="custom-pagination"
                ></el-pagination>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <!-- 已选药品 -->
            <el-card class="sub-card selected-card" shadow="hover">
              <div class="card-header">
                <h4 class="card-title">已选药品</h4>
                <div class="total-price">
                  <span class="total-label">药品总计：</span>
                  <span class="price-value">¥{{ drugTotalPrice }}</span>
                </div>
              </div>
              
              <div class="table-container">
                <el-table 
                  stripe 
                  border 
                  :data="drugBuyData" 
                  class="data-table"
                  height="400"
                  empty-text="暂无已选药品"
                >
                  <el-table-column 
                    label="药品编号" 
                    prop="drId"
                    width="100"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="药品名称" 
                    prop="drName"
                    min-width="120"
                  ></el-table-column>
                  <el-table-column 
                    label="单价" 
                    prop="drPrice"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <span class="price-text">¥{{ scope.row.drPrice }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="数量" 
                    prop="drNum"
                    width="80"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="小计" 
                    prop="drSum"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <span class="price-text">¥{{ scope.row.drSum }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="操作" 
                    width="100" 
                    fixed="right"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        size="mini"
                        @click="deleteDrug(scope.row.drId)"
                        class="remove-btn"
                      >
                        <i class="el-icon-delete"></i>
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 检查项目管理 -->
      <div class="check-management-section">
        <div class="section-header">
          <i class="el-icon-suitcase check-icon"></i>
          <h3 class="section-title">检查项目管理</h3>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <!-- 检查项目列表 -->
            <el-card class="sub-card" shadow="hover">
              <div class="card-header">
                <div class="search-box">
                  <el-input
                    v-model="queryCheck"
                    placeholder="请输入检查项目名称查询"
                    class="search-input"
                  >
                    <el-button
                      slot="append"
                      icon="el-icon-search"
                      @click="requestCheck"
                      class="search-btn"
                    >搜索</el-button>
                  </el-input>
                </div>
              </div>
              
              <div class="table-container">
                <el-table 
                  stripe 
                  border 
                  :data="checkData" 
                  class="data-table"
                  height="300"
                >
                  <el-table-column 
                    label="项目编号" 
                    prop="chId"
                    width="100"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="项目名称" 
                    prop="chName"
                    min-width="150"
                  ></el-table-column>
                  <el-table-column 
                    label="价格" 
                    prop="chPrice"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <span class="price-text">¥{{ scope.row.chPrice }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="操作" 
                    width="100" 
                    fixed="right"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-button
                        type="success"
                        size="mini"
                        @click="addCheck(scope.row.chId)"
                        class="add-btn"
                      >
                        <i class="el-icon-circle-plus"></i>
                        添加
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              
              <div class="pagination-section">
                <el-pagination
                  @size-change="checkSizeChange"
                  @current-change="checkCurrentChange"
                  background
                  layout="total, prev, pager, next"
                  :total="checkTotal"
                  :page-size="checkSize"
                  class="custom-pagination"
                ></el-pagination>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <!-- 已选检查项目 -->
            <el-card class="sub-card selected-card" shadow="hover">
              <div class="card-header">
                <h4 class="card-title">已选检查项目</h4>
                <div class="total-price">
                  <span class="total-label">检查总计：</span>
                  <span class="price-value">¥{{ checkTotalPrice }}</span>
                </div>
              </div>
              
              <div class="table-container">
                <el-table 
                  stripe 
                  border 
                  :data="checkBuyData" 
                  class="data-table"
                  height="300"
                  empty-text="暂无已选检查项目"
                >
                  <el-table-column 
                    label="项目编号" 
                    prop="chId"
                    width="100"
                    align="center"
                  ></el-table-column>
                  <el-table-column 
                    label="项目名称" 
                    prop="chName"
                    min-width="150"
                  ></el-table-column>
                  <el-table-column 
                    label="价格" 
                    prop="chPrice"
                    width="100"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <span class="price-text">¥{{ scope.row.chPrice }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column 
                    label="操作" 
                    width="100" 
                    fixed="right"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        size="mini"
                        @click="deleteCheck(scope.row.chId)"
                        class="remove-btn"
                      >
                        <i class="el-icon-delete"></i>
                        移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 费用汇总 -->
        <div class="total-section">
          <el-card class="total-card" shadow="hover">
            <div class="total-content">
              <div class="total-item">
                <span class="total-label">药品费用：</span>
                <span class="price-value drug-price">¥{{ drugTotalPrice }}</span>
              </div>
              <div class="total-item">
                <span class="total-label">检查费用：</span>
                <span class="price-value check-price">¥{{ checkTotalPrice }}</span>
              </div>
              <div class="total-item">
                <span class="total-label">挂号费用：</span>  <!-- ✅ 新增 -->
                <span class="price-value register-price">¥{{ dPrice || 0 }}</span>
              </div>
              <div class="total-item grand-total">
                <span class="total-label">总计费用：</span>
                <span class="price-value grand-price">¥{{ drugTotalPrice + checkTotalPrice + (dPrice || 0) }}</span>
              </div>
            </div>
          </el-card>
        </div>

      </div>
    </el-card>

    <!-- 诊断建议编写对话框 -->
    <el-dialog 
      title="诊断及医生建议编写" 
      :visible.sync="adviceFormVisible" 
      width="600px"
      class="custom-dialog"
    >
      <div class="dialog-header">
        <i class="el-icon-edit dialog-icon"></i>
        <h3>诊断及医生建议</h3>
      </div>
      
      <el-input
        type="textarea"
        :rows="8"
        placeholder="请输入诊断结果及医生建议..."
        v-model="advice"
        class="advice-textarea"
        resize="none"
      ></el-input>
      
      <div slot="footer" class="dialog-footer">
        <el-button 
          @click="adviceFormVisible = false" 
          class="cancel-btn"
        >
          <i class="el-icon-close"></i>
          取消
        </el-button>
        <el-button 
          type="primary" 
          @click="holdAdvice"
          class="confirm-btn"
        >
          <i class="el-icon-check"></i>
          保存建议
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
  name: "dealOrderAgain",
  data() {
    return {
      loading: false,
      submitting: false,
      oId: 1,
      pId: 1,
      dId: 0,
      pName: "aa",
      pGender: "as",
      pPhone: "asd",
      dName: "",
      drugData: [],
      size: 4,
      pageNumber: 1,
      total: 3,
      queryDrug: "",
      drugBuyData: [],
      drugTotalPrice: 0,
      //诊断建议
      advice: "",
      //项目表格数据
      checkData: [],
      queryCheck: "",
      checkTotal: 3,
      checkSize: 4,
      checkPageNumber: 1,
      checkTotalPrice: 0,
      checkBuyData: [],
      // 诊断建议对话框显示参数
      adviceFormVisible: false,
      dPrice:0,
    };
  },
  methods: {
    //获取医生信息
    requestDoctor() {
      request
          .get("doctor/findDoctorById", {
            params: {
              dId: this.dId,
            },
          })
          .then((res) => {
            if (res.data.status != 200) {
              this.$message.error("获取医生信息失败");
              return;
            }
            this.dPrice = res.data.data.dPrice || 0;
          });
    },
    //根据id减少药物数量
    reduceDrugNumber(drId, usedNumber) {
      request
        .get("drug/reduceDrugNumber", {
          params: {
            drId: drId,
            usedNumber: usedNumber,
          },
        })
        .then((res) => {
          if (res.data.status !== 200) {
            this.$message.error("药物数量不足！！");
          }
        });
    },
    
    //点击提交按钮
    submitClick() {
      this.submitting = true;
      for (let i = 0; i < this.drugBuyData.length; i++) {
        this.reduceDrugNumber(this.drugBuyData[i].drId, this.drugBuyData[i].drNum);
      }
      let data = {
        oId: this.dataPackage().oId,
        oAdvice: this.dataPackage().oAdvice,
        oDrug: this.dataPackage().oDrug,
        oCheck: this.dataPackage().oCheck,
        oTotalPrice: this.dataPackage().oTotalPrice,
      };
      request
        .post("order/updateOrderByAdd", data)
        .then((res) => {
          console.log(res);
          if (res.data.status !== 200) {
            this.$message.error("请求信息错误");
            return;
          }

          // 查询总价确认
          request
            .get("order/findTotalPrice", {
              params: {
                oId: data.oId,
              },
            })
            .then((res) => {
              if (res.data.status === 200) {
                this.$message.success("提交成功！请登录系统自助缴费！");
                this.$router.push("/orderToday");
              }
            });
        })
        .catch((err) => {
          this.submitting = false;
          console.error(err);
          this.$message.error("提交失败，请重试");
        });
    },
    
    //封装数据
//封装数据
    dataPackage() {
      let oDrug = " // ";
      let oCheck = " // ";
      let oTotalPrice = 0;
      let oId = this.oId;
      let oAdvice = this.advice;

      // 药品费
      for (let i = 0; i < this.drugBuyData.length; i++) {
        oDrug +=
            this.drugBuyData[i].drName +
            "*" +
            this.drugBuyData[i].drPrice +
            "(元)*" +
            this.drugBuyData[i].drNum +
            " ";
      }

      // 检查费
      for (let i = 0; i < this.checkBuyData.length; i++) {
        oCheck +=
            this.checkBuyData[i].chName +
            "*" +
            this.checkBuyData[i].chPrice +
            "(元) ";
      }

      oCheck += "项目总价" + this.checkTotalPrice + "元";
      oDrug += "药物总价" + this.drugTotalPrice + "元";

      // ✅ 修改：总费用 = 药品费 + 检查费 + 挂号费
      oTotalPrice = this.checkTotalPrice + this.drugTotalPrice + (this.dPrice || 0);

      return { oId, oAdvice, oDrug, oCheck, oTotalPrice };
    },

    //点击诊断建议保存按钮
    holdAdvice() {
      this.adviceFormVisible = false;
      this.$message.success("诊断建议保存成功！");
    },
    
    //打开诊断建议编写对话框
    openAdvice() {
      this.adviceFormVisible = true;
    },
    
    //检查列表点击移除按钮
    deleteCheck(chId) {
      for (let i = 0; i < this.checkBuyData.length; i++) {
        if (this.checkBuyData[i].chId === chId) {
          this.checkTotalPrice -= this.checkBuyData[i].chPrice;
          this.checkBuyData.splice(i, 1);
          this.$message.success("检查项目移除成功");
          break;
        }
      }
    },
    
    //检查列表点击增加按钮
    addCheck(chId) {
      request
        .get("check/findCheck", {
          params: {
            chId: chId,
          },
        })
        .then((res) => {
          if (res.data.status != 200) {
            this.$message.error("信息请求失败");
            return;
          }
          this.checkBuyData.push({
            chId: res.data.data.chId,
            chPrice: res.data.data.chPrice,
            chName: res.data.data.chName,
          });
          this.checkTotalPrice += res.data.data.chPrice;
          this.$message.success("检查项目添加成功");
        });
    },
    
    //检查页面大小切换时触发
    checkSizeChange(size) {
      this.checkSize = size;
      this.requestCheck();
    },
    
    //检查页数切换时触发
    checkCurrentChange(num) {
      this.checkPageNumber = num;
      this.requestCheck();
    },
    
    //请求检查项目
    requestCheck() {
      this.loading = true;
      request
        .get("check/findAllChecks", {
          params: {
            size: this.checkSize,
            pageNumber: this.checkPageNumber,
            query: this.queryCheck,
          },
        })
        .then((res) => {
          this.loading = false;
          if (res.data.status != 200) {
            this.$message.error("获取信息失败");
            return;
          }
          this.checkData = res.data.data.checks;
          this.checkTotal = res.data.data.total;
        })
        .catch(() => {
          this.loading = false;
          this.$message.error("数据加载失败");
        });
    },
    
    //药物列表点击移除按钮
    deleteDrug(drId) {
      for (let i = 0; i < this.drugBuyData.length; i++) {
        if (this.drugBuyData[i].drId === drId) {
          for (let j = 0; j < this.drugData.length; j++) {
            if (this.drugData[j].drId === drId) this.drugData[j].drNumber += 1;
          }
          this.drugBuyData[i].drNum -= 1;
          this.drugBuyData[i].drSum =
            this.drugBuyData[i].drPrice * this.drugBuyData[i].drNum;
          this.drugTotalPrice -= this.drugBuyData[i].drPrice;
          if (this.drugBuyData[i].drNum === 0) {
            this.drugBuyData.splice(i, 1);
            this.$message.success("药品移除成功");
          }
          break;
        }
      }
    },
    
    //药物列表点击增加按钮
    addDrug(drId) {
      request
        .get("drug/findDrug", {
          params: {
            drId: drId,
          },
        })
        .then((res) => {
          if (res.data.status != 200) {
            this.$message.error("信息请求失败");
            return;
          }

          // 检查是否已存在
          for (let i = 0; i < this.drugBuyData.length; i++) {
            if (this.drugBuyData[i].drId === res.data.data.drId) {
              for (let j = 0; j < this.drugData.length; j++) {
                if (
                  this.drugData[j].drId === res.data.data.drId &&
                  this.drugData[j].drNumber > 0
                ) {
                  this.drugData[j].drNumber -= 1;
                  this.drugBuyData[i].drNum += 1;
                  this.drugBuyData[i].drSum =
                    this.drugBuyData[i].drPrice * this.drugBuyData[i].drNum;
                  this.drugTotalPrice += this.drugBuyData[i].drPrice;
                  this.$message.success("药品数量增加成功");
                  return;
                }
              }
              return;
            }
          }

          // 检查库存
          for (let j = 0; j < this.drugData.length; j++) {
            if (
              this.drugData[j].drId === res.data.data.drId &&
              this.drugData[j].drNumber <= 0
            ) {
              this.$message.warning("该药品库存不足");
              return;
            }
          }

          // 添加新药品
          this.drugBuyData.push({
            drId: res.data.data.drId,
            drPrice: res.data.data.drPrice,
            drName: res.data.data.drName,
            drNum: 1,
            drSum: res.data.data.drPrice,
          });

          // 更新库存
          for (let j = 0; j < this.drugData.length; j++) {
            if (this.drugData[j].drId === res.data.data.drId)
              this.drugData[j].drNumber -= 1;
          }

          this.drugTotalPrice += res.data.data.drPrice;
          this.$message.success("药品添加成功");
        });
    },
    
    //药物页面大小切换时触发
    handleSizeChange(size) {
      this.size = size;
      this.requestDrug();
    },
    
    //药物页数切换时触发
    handleCurrentChange(num) {
      this.pageNumber = num;
      this.requestDrug();
    },

    //获取药物列表
    requestDrug() {
      this.loading = true;
      request
        .get("drug/findAllDrugs", {
          params: {
            size: this.size,
            pageNumber: this.pageNumber,
            query: this.queryDrug,
          },
        })
        .then((res) => {
          this.loading = false;
          if (res.data.status != 200) {
            this.$message.error("获取信息失败");
            return;
          }
          this.drugData = res.data.data.drugs;
          this.total = res.data.data.total;
        })
        .catch(() => {
          this.loading = false;
          this.$message.error("数据加载失败");
        });
    },
    
    //获取患者信息
    requestPatient() {
      request
        .get("doctor/findPatientById", {
          params: {
            pId: this.pId,
          },
        })
        .then((res) => {
          if (res.data.status != 200) {
            this.$message.error("获取信息失败");
            return;
          }
          this.pName = res.data.data.pName;
          this.pGender = res.data.data.pGender;
          this.pPhone = res.data.data.pPhone;
        });
    },
    
    //token解码
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
    },
  },
  created() {
    this.dName = this.tokenDecode(getToken()).dName;
    this.dId = this.tokenDecode(getToken()).dId;
    this.oId = this.$route.query.oId;
    this.pId = this.$route.query.pId;
    this.requestPatient();
    this.requestDrug();
    this.requestCheck();
    this.requestDoctor();
  },
};
</script>

<style lang="scss" scoped>
.treatment-management {
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
  color: #67c23a;
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

/* 患者信息区域 */
.patient-info-section {
  padding: 20px;
  background: #f8fafc;
  border-bottom: 1px solid #e6e8eb;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e6e8eb;
}

.patient-icon, .drug-icon, .check-icon {
  font-size: 20px;
  color: #67c23a;
  margin-right: 10px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.info-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.info-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  margin-bottom: 5px;
}

.info-input {
  .el-input__inner {
    background: white;
    border-radius: 8px;
    border: 1px solid #e4e7ed;
  }
}

.action-item {
  display: flex;
  align-items: flex-end;
  height: 100%;
}

.submit-btn, .advice-btn {
  width: 100%;
  border-radius: 8px;
  padding: 12px 0;
  font-weight: 500;
  font-size: 14px;
}

.submit-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
  color: white;
}

.advice-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
  color: white;
}

/* 药品和检查管理区域 */
.drug-management-section, .check-management-section {
  padding: 20px;
  border-bottom: 1px solid #e6e8eb;
}

.check-management-section {
  border-bottom: none;
}

.sub-card {
  border-radius: 12px;
  border: none;
  height: 100%;
  margin-bottom: 0;
}

.sub-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.selected-card {
  border-left: 4px solid #67c23a;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f2f5;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2d3d;
  margin: 0;
}

.search-box {
  width: 300px;
}

.search-input {
  border-radius: 8px;
  overflow: hidden;
}

.search-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: none;
  color: white;
  font-weight: 500;
}

.total-price {
  display: flex;
  align-items: center;
  gap: 8px;
}

.total-label {
  font-size: 14px;
  color: #606266;
}

.price-value {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

/* 表格容器 */
.table-container {
  margin-bottom: 15px;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
}

.stock-tag {
  border-radius: 12px;
  font-weight: 500;
}

.price-text {
  font-weight: 600;
  color: #f56c6c;
}

.add-btn, .remove-btn {
  border-radius: 6px;
  font-weight: 500;
  padding: 6px 10px;
  font-size: 12px;
}

.add-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
  color: white;
}

.remove-btn {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
  border: none;
  color: white;
}

/* 分页样式 */
.pagination-section {
  display: flex;
  justify-content: center;
  padding: 10px 0;
}

.custom-pagination {
  .el-pagination.is-background .el-pager li:not(.disabled).active {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    color: white;
  }
}

/* 费用汇总 */
.total-section {
  padding: 20px 0 0;
}

.total-card {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f3ff 100%);
}

.total-content {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px;
}

.total-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.total-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.drug-price {
  color: #e6a23c;
}

.check-price {
  color: #409eff;
}

.grand-total {
  .total-label {
    font-size: 16px;
    font-weight: 600;
  }
}

.grand-price {
  font-size: 24px;
  color: #f56c6c;
}

/* 对话框样式 */
.custom-dialog {
  .el-dialog {
    border-radius: 16px;
    overflow: hidden;
  }
  
  .el-dialog__header {
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    padding: 20px;
  }
  
  .el-dialog__title {
    color: white;
    font-size: 18px;
    font-weight: 600;
  }
  
  .el-dialog__body {
    padding: 30px;
  }
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
  color: #67c23a;
  margin-right: 10px;
}

.dialog-header h3 {
  margin: 0;
  color: #1f2d3d;
  font-size: 18px;
  font-weight: 600;
}

.advice-textarea {
  .el-textarea__inner {
    border-radius: 8px;
    border: 1px solid #e4e7ed;
    padding: 15px;
    font-size: 14px;
    line-height: 1.5;
  }
}

.dialog-footer {
  text-align: right;
  padding: 20px 0 0;
}

.cancel-btn, .confirm-btn {
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
}

.confirm-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .treatment-management {
    padding: 15px;
  }
  
  .total-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .total-item {
    flex-direction: row;
    justify-content: space-between;
    width: 100%;
  }
}

@media (max-width: 768px) {
  .treatment-management {
    padding: 10px;
  }
  
  .header-section {
    flex-direction: column;
    text-align: center;
  }
  
  .patient-info-section, .drug-management-section, .check-management-section {
    padding: 15px;
  }
  
  .info-item {
    margin-bottom: 10px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 10px;
  }
  
  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .search-box {
    width: 100%;
  }
  
  .data-table {
    font-size: 12px;
  }
  
  .add-btn, .remove-btn {
    padding: 4px 8px;
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .management-card {
    margin: 0 -10px;
  }
  
  .sub-card {
    margin-bottom: 15px;
  }
  
  .table-container {
    overflow-x: auto;
  }
  
  .data-table {
    min-width: 600px;
  }
}
</style>