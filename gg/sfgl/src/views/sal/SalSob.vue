<template>
  <div>
    <div
        style="display: flex; justify-content: space-between; margin-top: 10px"
    >
      <el-button type="primary" @click="showAddSalaryView"
      >添加工资账套</el-button
      >
      <el-button
          type="success"
          icon="el-icon-refresh"
          @click="initSalaries"
      ></el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table :data="salaries">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column
            prop="name"
            label="账套名称"
            width="120"
        ></el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资"
            width="80"
        ></el-table-column>
        <el-table-column prop="bonus" label="奖金" width="70"></el-table-column>
        <el-table-column
            prop="lunchSalary"
            label="午餐补助"
            width="80"
        ></el-table-column>
        <el-table-column
            prop="trafficSalary"
            label="交通补助"
            width="80"
        ></el-table-column>
        <el-table-column
            prop="createDate"
            label="时间"
            width="120"
        ></el-table-column>
        <el-table-column label="养老金" align="center">
          <el-table-column
              prop="pensionBase"
              label="基数"
              width="70"
          ></el-table-column>
          <el-table-column
              prop="pensionPer"
              label="比率"
              width="70"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="医疗保险" align="center">
          <el-table-column
              prop="medicalBase"
              label="基数"
              width="70"
          ></el-table-column>
          <el-table-column
              prop="medicalPer"
              label="比率"
              width="70"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="公积金" align="center">
          <el-table-column
              prop="accumulationFundBase"
              label="基数"
              width="70"
          ></el-table-column>
          <el-table-column
              prop="accumulationFundPer"
              label="比率"
              width="70"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template scope="scope">
            <el-button type="primary" @click="showEditSalaryView(scope.row)"
            >编辑</el-button
            >
            <el-button type="danger" @click="deleteSalaryData(scope.row)"
            >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <div
          style="
          display: flex;
          justify-content: space-around;
          align-items: center;
        "
      >
        <el-steps direction="vertical" :active="activeItemIndex">
          <el-step
              :title="item"
              v-for="(item, index) in salaryItemName"
              :key="index"
          ></el-step>
        </el-steps>
        <el-input
            style="width: 200px"
            v-model="salary[title]"
            :placeholder="'请输入' + salaryItemName[index] + '...'"
            v-for="(value, title, index) in salary"
            :key="index"
            v-show="activeItemIndex == index"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="preStep">{{
            activeItemIndex == 10 ? "取消" : "上一步"
          }}</el-button>
        <el-button type="primary" @click="nextStep">{{
            activeItemIndex == 10 ? "完成" : "下一步"
          }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "SalSob",
  data() {
    return {
      dialogTitle: "添加工资账套",
      salaries: [],
      activeItemIndex: 0,
      dialogVisible: false,
      salaryItemName: [
        "账套名称",
        "基本工资",
        "奖金",
        "午餐补助",
        "交通补助",
        "养老金基数",
        "养老金比率",
        "医疗保险基数",
        "医疗保险比率",
        "公积金基数",
        "公积金比率",
      ],
      salary: {
        name: "",
        basicSalary: 0,
        bonus: 0,
        lunchSalary: 0,
        trafficSalary: 0,
        pensionBase: 0,
        pensionPer: 0,
        medicalBase: 0,
        medicalPer: 0,
        accumulationFundBase: 0,
        accumulationFundPer: 0,
      },
    };
  },
  methods: {
    showEditSalaryView(data) {
      this.dialogTitle = "编辑工资账套";
      this.activeItemIndex = 0;
      this.salary.id = data.id;
      this.salary.name = data.name;
      this.salary.basicSalary = data.basicSalary;
      this.salary.bonus = data.bonus;
      this.salary.lunchSalary = data.lunchSalary;
      this.salary.trafficSalary = data.trafficSalary;
      this.salary.pensionBase = data.pensionBase;
      this.salary.pensionPer = data.pensionPer;
      this.salary.medicalBase = data.medicalBase;
      this.salary.medicalPer = data.medicalPer;
      this.salary.accumulationFundBase = data.accumulationFundBase;
      this.salary.accumulationFundPer = data.accumulationFundPer;
      this.dialogVisible = true;
    },
    deleteSalaryData(data) {
      this.$confirm("此操作将永久删除" + data.name + ", 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.deleteRequest("/salary/sob/deleteSalary/" + data.id).then((resp) => {
              if (resp) {
                this.initSalaries();
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
    preStep() {
      if (this.activeItemIndex == 0) {
        return;
      } else if (this.activeItemIndex == 10) {
        this.dialogVisible = false;
        return;
      }
      this.activeItemIndex--;
    },
    nextStep() {
      if (this.activeItemIndex == 10) {
        if (this.salary.id) {
          this.putRequest("/salary/sob/updateSalary", this.salary).then((resp) => {
            if (resp) {
              this.initSalaries();
              this.dialogVisible = false;
            }
          });
        } else {
          this.postRequest("/salary/sob/addSalary", this.salary).then((resp) => {
            if (resp) {
              this.initSalaries();
              this.dialogVisible = false;
            }
          });
        }
        return;
      }
      this.activeItemIndex++;
    },
    showAddSalaryView() {
      this.dialogTitle = "添加工资账套";
      this.salary = {
        name: "",
        basicSalary: 0,
        bonus: 0,
        lunchSalary: 0,
        trafficSalary: 0,
        pensionBase: 0,
        pensionPer: 0,
        medicalBase: 0,
        medicalPer: 0,
        accumulationFundBase: 0,
        accumulationFundPer: 0,
      };
      this.activeItemIndex = 0;
      this.dialogVisible = true;
    },
    //初始化数据
    initSalaries() {
      this.getRequest("/salary/sob/").then((resp) => {
        if (resp) {
          this.salaries = resp;
        }
      });
    },
  },
  mounted() {
    this.initSalaries();
  }
}
</script>

<style scoped>

</style>