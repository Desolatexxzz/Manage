<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin-top: 20px"
      >
        <div>
          <el-input
              style="width: 300px; margin-right: 10px"
              prefix-icon="el-icon-search"
              placeholder="请输入员工名"
              v-model.trim="empName"
              @keydown.enter.native="initEmps"
              @clear="initEmps"
              clearable
          ></el-input>
          <el-button type="primary" @click="initEmps">搜索</el-button>
          <el-button
              type="primary"
              @click="showAdvanceSearchVisible = !showAdvanceSearchVisible"
          >
            <i :class="showAdvanceSearchVisible? 'fa fa-angle-double-up': 'fa fa-angle-double-down'" aria-hidden="true"></i>
            高级搜索
          </el-button
          >
        </div>
        <div>
          <el-upload
              style="display: inline-flex; margin-right: 10px"
              :headers="headers"
              action="/employee/basic/importEmployee"
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
          >
            <el-button type="success" :icon="importDataBtnIcon" :disabled="importDataDisabled">
              {{ importDataBtnText }}
            </el-button
            >
          </el-upload>
          <el-button
              @click="exportData"
              type="success"
              icon="el-icon-folder-remove"
              :disabled="importDataDisabled"
          >
            导出数据
          </el-button>
          <el-button type="primary" @click="showAddEmpView">添加员工</el-button>
        </div>
      </div>
    </div>
    <transition name="slide-fade">
      <div
          v-show="showAdvanceSearchVisible"
          style="
          border: 1px solid #409eff;
          border-radius: 5px;
          box-sizing: border-box;
          padding: 5px;
          margin: 10px 0px;
        "
      >
        <el-row>
          <el-col :span="5">
            政治面貌:
            <el-select
                v-model="searchVaule.politicId"
                size="mini"
                placeholder="政治面貌"
                style="width: 120px"
            >
              <el-option
                  v-for="item in politicsstatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            民族:
            <el-select
                v-model="searchVaule.nationId"
                size="mini"
                placeholder="民族"
                style="width: 120px"
            >
              <el-option
                  v-for="item in nations"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职位:
            <el-select
                v-model="searchVaule.posId"
                size="mini"
                placeholder="职位"
                style="width: 120px"
            >
              <el-option
                  v-for="item in positions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职称:
            <el-select
                v-model="searchVaule.jobLevelId"
                size="mini"
                placeholder="职称"
                style="width: 120px"
            >
              <el-option
                  v-for="item in joblevels"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="7" style="height: 28px">
            聘用形式:
            <el-radio-group v-model="searchVaule.engageForm">
              <el-radio label="劳动合同">劳动合同</el-radio>
              <el-radio label="劳务合同">劳务合同</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="5" style="height: 28px; line-height: 28px">
            所属部门:
            <el-popover
                placement="bottom"
                title="所属部门"
                width="200"
                trigger="manual"
                v-model="visible2"
            >
              <el-tree
                  default-expand-all
                  :data="allDeps"
                  :props="defaultProps"
                  @node-click="searchHandleNodeClick"
              ></el-tree>
              <div
                  slot="reference"
                  style="
                  width: 120px;
                  height: 28px;
                  display: inline-flex;
                  border: 1px solid #dedede;
                  border-radius: 5px;
                  cursor: pointer;

                  align-items: center;
                  font-size: 12px;
                  padding-left: 10px;
                  box-sizing: border-box;
                "
                  @click="showDepView2"
              >
                {{ inputDepName }}
              </div>
            </el-popover>
          </el-col>
          <el-col :span="10">
            入职日期:
            <el-date-picker
                v-model="searchVaule.beginDateScope"
                type="daterange"
                size="mini"
                value-format="yyyy-MM-dd"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 300px"
            >
            </el-date-picker>
          </el-col>
          <el-col :span="5" :offset="4">
            <el-button size="mini">取消</el-button>
            <el-button type="primary" size="mini" @click="initEmps('advanced')"
            >搜索
            </el-button
            >
          </el-col>
        </el-row>
      </div>
    </transition>
    <div style="margin-top: 20px">
      <el-table :data="emps" style="width: 100%" v-loading="loading">
        <el-table-column type="selection" width="45"></el-table-column>
        <el-table-column prop="name" fixed="left" label="姓名" width="70">
        </el-table-column>
        <el-table-column prop="workID" label="工号" width="100">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="50">
        </el-table-column>
        <el-table-column prop="birthday" label="出生年月" width="100">
        </el-table-column>
        <el-table-column prop="idCard" label="身份证" width="180">
        </el-table-column>
        <el-table-column prop="wedlock" label="婚姻" width="55">
        </el-table-column>
        <el-table-column prop="nation.name" label="民族" width="55">
        </el-table-column>
        <el-table-column prop="nativePlace" label="籍贯" width="65">
        </el-table-column>
        <el-table-column
            prop="politicsStatus.name"
            label="政治面貌"
            width="110"
        >
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="180">
        </el-table-column>
        <el-table-column prop="phone" label="手机" width="115">
        </el-table-column>
        <el-table-column prop="address" label="地址" width="300">
        </el-table-column>
        <el-table-column prop="department.name" label="所属部门" width="95">
        </el-table-column>
        <el-table-column prop="joblevel.name" label="职称" width="95">
        </el-table-column>
        <el-table-column prop="position.name" label="职位" width="95">
        </el-table-column>
        <el-table-column prop="engageForm" label="合同形式" width="85">
        </el-table-column>
        <el-table-column prop="tiptopDegree" label="学历" width="65">
        </el-table-column>
        <el-table-column prop="specialty" label="专业" width="150">
        </el-table-column>
        <el-table-column prop="school" label="毕业院校" width="150">
        </el-table-column>
        <el-table-column prop="workState" label="在职状态" width="80">
        </el-table-column>
        <el-table-column prop="beginDate" label="入职日期" width="100">
        </el-table-column>
        <el-table-column prop="conversionTime" label="转正日期" width="100">
        </el-table-column>
        <el-table-column prop="beginContract" label="合同起始日期" width="110">
        </el-table-column>
        <el-table-column prop="endContract" label="合同结束日期" width="110">
        </el-table-column>
        <el-table-column label="合同期限" width="85">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.contractTerm }}年</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="280">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditEmpView(scope.row)"
            >编辑
            </el-button
            >
            <el-button size="mini" type="primary">查看高级资料</el-button>
            <el-button size="mini" type="danger" @click="deleteEmp(scope.row)"
            >删除
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: flex-end">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total"
            :total="total"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="80%" center>
      <div>
        <el-form ref="empForm" :model="emp" :rules="rules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名:" prop="name">
                <el-input
                    v-model="emp.name"
                    placeholder="请输入员工姓名"
                    prefix-icon="el-icon-edit"
                    size="mini"
                    style="width: 150px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="emp.gender">
                  <el-radio label="男" style="margin-top: 10px">男</el-radio>
                  <el-radio label="女" style="margin-top: 10px">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期" prop="birthday">
                <el-date-picker
                    v-model="emp.birthday"
                    size="mini"
                    style="width: 150px"
                    align="right"
                    type="date"
                    placeholder="选择日期"
                    value-format="yyyy-MM-dd"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="政治面貌" prop="politicId">
                <el-select
                    v-model="emp.politicId"
                    size="mini"
                    placeholder="政治面貌"
                    style="width: 200px"
                >
                  <el-option
                      v-for="item in politicsstatus"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族:" prop="nationId">
                <el-select
                    v-model="emp.nationId"
                    size="mini"
                    placeholder="民族"
                    style="width: 150px"
                >
                  <el-option
                      v-for="item in nations"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="籍贯" prop="nativePlace">
                <el-input
                    v-model="emp.nativePlace"
                    size="mini"
                    placeholder="请输入籍贯"
                    prefix-icon="el-icon-edit"
                    style="width: 120px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱: " prop="email">
                <el-input
                    size="mini"
                    prefix-icon="el-icon-message"
                    v-model="emp.email"
                    placeholder="请输入邮箱"
                    style="width: 150px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="联系地址" prop="address">
                <el-input
                    size="mini"
                    prefix-icon="el-icon-edit"
                    v-model="emp.address"
                    placeholder="请输入地址"
                    style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位" prop="posId">
                <el-select
                    v-model="emp.posId"
                    size="mini"
                    placeholder="职位"
                    style="width: 150px"
                >
                  <el-option
                      v-for="item in positions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="职称:" prop="jobLevelId">
                <el-select
                    v-model="emp.jobLevelId"
                    size="mini"
                    placeholder="职称"
                    style="width: 120px"
                >
                  <el-option
                      v-for="item in joblevels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门:" prop="departmentId">
                <el-popover
                    placement="bottom"
                    title="所属部门"
                    width="200"
                    trigger="manual"
                    v-model="visible"
                >
                  <el-tree
                      default-expand-all
                      :data="allDeps"
                      :props="defaultProps"
                      @node-click="handleNodeClick"
                  ></el-tree>
                  <div
                      slot="reference"
                      style="
                      width: 150px;
                      height: 26px;
                      display: inline-flex;
                      border: 1px solid #dedede;
                      border-radius: 5px;
                      cursor: pointer;
                      margin-top: 8px;
                      align-items: center;
                      font-size: 12px;
                      padding-left: 10px;
                      box-sizing: border-box;
                    "
                      @click="showDepView"
                  >
                    {{ inputDepName }}
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="电话号码: " prop="phone">
                <el-input
                    prefix-icon="el-icon-phone"
                    v-model="emp.phone"
                    size="mini"
                    style="width: 200px"
                    placeholder="请输入电话号码"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号: " prop="workID">
                <el-input
                    prefix-icon="el-icon-edit"
                    size="mini"
                    v-model="emp.workID"
                    style="width: 150px"
                    placeholder="请输入工号"
                    disabled
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="学历:" prop="tiptopDegree">
                <el-select
                    v-model="emp.tiptopDegree"
                    size="mini"
                    placeholder="最高学历"
                    style="width: 120px"
                >
                  <el-option
                      v-for="item in tiptopDegrees"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校: " prop="school">
                <el-input
                    size="mini"
                    prefix-icon="el-icon-edit"
                    v-model="emp.school"
                    style="width: 150px"
                    placeholder="请输入学校"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="专业名称" prop="specialty">
                <el-input
                    size="mini"
                    prefix-icon="el-icon-edit"
                    v-model="emp.specialty"
                    style="width: 200px"
                    placeholder="请输入专业"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    size="mini"
                    type="date"
                    placeholder="入职日期"
                    style="width: 150px"
                    value-format="yyyy-MM-dd"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="转正日期: " prop="conversionTime">
                <el-date-picker
                    v-model="emp.conversionTime"
                    size="mini"
                    type="date"
                    placeholder="转正日期"
                    value-format="yyyy-MM-dd"
                    style="width: 120px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期:" prop="beginContract">
                <el-date-picker
                    v-model="emp.beginContract"
                    size="mini"
                    type="date"
                    placeholder="合同起始日期"
                    style="width: 150px"
                    value-format="yyyy-MM-dd"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="合同截止日期:" prop="endContract">
                <el-date-picker
                    v-model="emp.endContract"
                    size="mini"
                    type="date"
                    placeholder="合同截止日期"
                    value-format="yyyy-MM-dd"
                    style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="身份证号" prop="idCard">
                <el-input
                    prefix-icon="el-icon-edit"
                    size="mini"
                    placeholder="身份证号码"
                    v-model="emp.idCard"
                    style="width: 180px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="聘用形式" prop="engageForm">
                <el-radio-group v-model="emp.engageForm">
                  <el-radio label="劳动合同" style="margin-top: 10px"
                  >劳动合同
                  </el-radio
                  >
                  <el-radio label="劳务合同" style="margin-top: 10px"
                  >劳务合同
                  </el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="婚姻状况" prop="wedlock">
                <el-radio-group v-model="emp.wedlock">
                  <el-radio label="已婚" style="margin-top: 10px"
                  >已婚
                  </el-radio
                  >
                  <el-radio label="未婚" style="margin-top: 10px"
                  >未婚
                  </el-radio
                  >
                  <el-radio label="离异" style="margin-top: 10px"
                  >离异
                  </el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      searchVaule: {
        politicId: null,
        nationId: null,
        posId: null,
        jobLevelId: null,
        engageForm: "",
        departmentId: null,
        beginDateScope: null,
      },
      showAdvanceSearchVisible: false,
      headers: {
        Authorization: window.sessionStorage.getItem("tokenStr"),
      },
      importDataDisabled: false,
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-folder-add",
      title: "",
      defaultProps: {
        children: "children",
        label: "name",
      },
      inputDepName: "",
      allDeps: [],
      visible: false,
      visible2: false,
      emps: [],
      loading: false,
      total: 0, //总数据
      currentPage: 1, //页数
      size: 10, //条数
      empName: "",
      dialogVisible: false,
      nations: [],
      joblevels: [],
      politicsstatus: [],
      positions: [],
      tiptopDegrees: [
        "博士",
        "硕士",
        "本科",
        "大专",
        "高中",
        "初中",
        "小学",
        "其他",
      ],
      emp: {
        id: null,
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: null,
        nativePlace: "",
        politicId: null,
        email: "",
        phone: "",
        address: "",
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "在职",
        workID: "",
        contractTerm: null,
        conversionTime: "",
        notWorkDate: null,
        beginContract: "",
        endContract: "",
        workAge: null,
        salaryId: null,
      },
      rules: {
        name: [{required: true, message: "请输入员工姓名", trigger: "blur"}],
        gender: [
          {required: true, message: "请输入员工性别", trigger: "blur"},
        ],
        birthday: [
          {required: true, message: "请输入出生日期", trigger: "blur"},
        ],
        idCard: [
          {required: true, message: "请输入身份证号", trigger: "blur"},
          {
            pattern:
                /^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "身份证号格式不正确",
            trigger: "blur",
          },
        ],
        wedlock: [
          {required: true, message: "请输入婚姻状况", trigger: "blur"},
        ],
        nationId: [{required: true, message: "请输入民族", trigger: "blur"}],
        nativePlace: [
          {required: true, message: "请输入籍贯", trigger: "blur"},
        ],
        politicId: [
          {required: true, message: "请输入政治面貌", trigger: "blur"},
        ],
        email: [
          {required: true, message: "请输入邮箱地址", trigger: "blur"},
          {type: "email", message: "邮箱地址格式不正确", trigger: "blur"},
        ],
        phone: [{required: true, message: "请输入电话号码", trigger: "blur"}],
        address: [{required: true, message: "请输入地址", trigger: "blur"}],
        departmentId: [
          {required: true, message: "请输入部门名称", trigger: "blur"},
        ],
        jobLevelId: [
          {required: true, message: "请输入职称", trigger: "blur"},
        ],
        posId: [{required: true, message: "请输入职位", trigger: "blur"}],
        engageForm: [
          {required: true, message: "请输入聘用方式", trigger: "blur"},
        ],
        tiptopDegree: [
          {required: true, message: "请输入学历", trigger: "blur"},
        ],
        specialty: [{required: true, message: "请输入专业", trigger: "blur"}],
        school: [
          {required: true, message: "请输入毕业院校", trigger: "blur"},
        ],
        beginDate: [
          {required: true, message: "请输入入职日期", trigger: "blur"},
        ],
        workState: [
          {required: true, message: "请输入工作状态", trigger: "blur"},
        ],
        workID: [{required: true, message: "请输入工号", trigger: "blur"}],
        contractTerm: [
          {required: true, message: "请输入合同期限", trigger: "blur"},
        ],
        conversionTime: [
          {required: true, message: "请输入转正日期", trigger: "blur"},
        ],
        notWorkDate: [
          {required: true, message: "请输入离职日期", trigger: "blur"},
        ],
        beginContract: [
          {required: true, message: "请输入合同起始日期", trigger: "blur"},
        ],
        endContract: [
          {required: true, message: "请输入合同结束日期", trigger: "blur"},
        ],
        workAge: [{required: true, message: "请输入工龄", trigger: "blur"}],
      },
    };
  },
  methods: {
    onSuccess() {
      this.importDataBtnIcon = "el-icon-folder-add";
      this.importDataBtnText = "导入数据";
      this.importDataDisabled = false;
      this.initEmps();
    },
    onError() {
      this.importDataBtnIcon = "el-icon-folder-add";
      this.importDataBtnText = "导入数据";
      this.importDataDisabled = false;
    },
    beforeUpload() {
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataBtnText = "正在导入";
      this.importDataDisabled = true;
    },
    exportData() {
      this.downloadRequest("/employee/basic/export");
    },
    //显示编辑员工信息
    showEditEmpView(data) {
      this.title = "编辑员工信息";
      this.emp = data;
      this.inputDepName = data.department.name;
      this.initPositions();
      this.dialogVisible = true;
    },
    deleteEmp(data) {
      this.$confirm("此操作将永久删除" + data.name + ", 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.deleteRequest("/employee/basic/deleteEmployee/" + data.id).then((resp) => {
              if (resp) {
                this.initEmps();
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    // 添加员工
    doAddEmp() {
      if (this.emp.id) {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            //更新员工
            this.putRequest("/employee/basic/updateEmployee", this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        });
      } else {
        //添加员工
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.postRequest("/employee/basic/addEmployee", this.emp).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        });
      }
    },
    searchHandleNodeClick(data) {
      this.inputDepName = data.name;
      this.searchVaule.departmentId = data.id;
      this.visible2 = !this.visible2;
    },
    //部门树形控件的显示
    handleNodeClick(data) {
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.visible = !this.visible;
    },
    showDepView2() {
      this.visible2 = !this.visible2;
    },
    showDepView() {
      this.visible = !this.visible;
    },
    getMaxWorkID() {
      this.getRequest("/employee/basic/maxWorkId").then((resp) => {
        if (resp) {
          this.emp.workID = resp.object;
        }
      });
    },
    //初始化职位
    initPositions() {
      this.getRequest("/employee/basic/positions").then((resp) => {
        if (resp) {
          this.positions = resp;
        }
      });
    },
    // 初始化数据 政治面貌 职称 民族
    initData() {
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest("/employee/basic/nations").then((resp) => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem("nations", JSON.stringify(resp));
          }
        });
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      if (!window.sessionStorage.getItem("joblevels")) {
        this.getRequest("/employee/basic/joblevels").then((resp) => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
          }
        });
      } else {
        this.joblevels = JSON.parse(window.sessionStorage.getItem("joblevels"));
      }
      if (!window.sessionStorage.getItem("politicsstatus")) {
        this.getRequest("/employee/basic/politicsstatus").then((resp) => {
          if (resp) {
            this.politicsstatus = resp;
            window.sessionStorage.setItem(
                "politicsstatus",
                JSON.stringify(resp)
            );
          }
        });
      } else {
        this.politicsstatus = JSON.parse(
            window.sessionStorage.getItem("politicsstatus")
        );
      }
      if (!window.sessionStorage.getItem("allDeps")) {
        this.getRequest("/employee/basic/deps").then((resp) => {
          if (resp) {
            this.allDeps = resp;
            window.sessionStorage.setItem("allDeps", JSON.stringify(resp));
          }
        });
      } else {
        this.allDeps = JSON.parse(window.sessionStorage.getItem("allDeps"));
      }
    },
    // 显示添加对话框
    showAddEmpView() {
      this.title = "添加员工";
      this.emp = {
        id: null,
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: null,
        nativePlace: "",
        politicId: null,
        email: "",
        phone: "",
        address: "",
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "在职",
        workID: "",
        contractTerm: null,
        conversionTime: "",
        notWorkDate: null,
        beginContract: "",
        endContract: "",
        workAge: null,
        salaryId: null,
      };
      this.inputDepName = "";
      this.initPositions();
      this.getMaxWorkID();
      this.dialogVisible = true;
    },
    sizeChange(size) {
      this.size = size;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.initEmps();
    },
    //初始化数据
    initEmps(type) {
      this.loading = true;
      let url =
          "/employee/basic/?currentPage=" +
          this.currentPage +
          "&size=" +
          this.size;
      if (type && type == "advanced") {
        if (this.searchVaule.politicId) {
          url += "&politicId=" + this.searchVaule.politicId;
        }
        if (this.searchVaule.nationId) {
          url += "&nationId=" + this.searchVaule.nationId;
        }
        if (this.searchVaule.posId) {
          url += "&posId=" + this.searchVaule.posId;
        }
        if (this.searchVaule.jobLevelId) {
          url += "&jobLevelId=" + this.searchVaule.jobLevelId;
        }
        if (this.searchVaule.engageForm) {
          url += "&engageForm=" + this.searchVaule.engageForm;
        }
        if (this.searchVaule.departmentId) {
          url += "&departmentId=" + this.searchVaule.departmentId;
        }
        if (this.searchVaule.beginDateScope) {
          url += "&beginDateScope=" + this.searchVaule.beginDateScope;
        }
      } else {
        url += "&name=" + this.empName;
      }
      this.getRequest(url).then((resp) => {
        if (resp) {
          this.loading = false;
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
  },
  mounted() {
    this.initEmps();
    this.initData();
    this.initPositions();
  }
}
</script>

<style>
body {
  margin: 0;
}

.slide-fade-enter-active {
  transition: all 0.3s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}
</style>