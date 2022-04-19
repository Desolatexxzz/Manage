<template>
  <div>
    <div>
      <el-input
          size="small"
          v-model="jl.name"
          placeholder="添加职称名称..."
          prefix-icon="el-icon-plus"
          style="width: 300px"
      ></el-input>
      <el-select
          size="small"
          style="margin-left: 8px; margin-right: 8px"
          v-model="jl.titleLevel"
          placeholder="职称等级"
      >
        <el-option
            v-for="item in titleLevels"
            :key="item"
            :label="item"
            :value="item"
        >
        </el-option>
      </el-select>
      <el-button
          size="small"
          type="primary"
          icon="el-icon-plus"
          @click="addJobLevel"
      >
        添加
      </el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          size="small"
          :data="jls"
          stripe
          border
          style="width: 70%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="编号" width="55"> </el-table-column>
        <el-table-column prop="name" label="职称名称" width="150">
        </el-table-column>
        <el-table-column prop="titleLevel" label="职称等级" width="150">
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="150">
        </el-table-column>
        <el-table-column prop="enabled" label="是否启用" width="150">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button size="mini" @click="showEditView(scope.row)"
            >编辑</el-button
            >
            <el-button
                size="mini"
                type="danger"
                @click="deleteJobLevel(scope.row)"
            >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button
        size="small"
        style="margin-top: 8px"
        type="danger"
        :disabled="this.multipleSelection.length == 0"
        @click="deleteMany"
    >批量删除
    </el-button>
    <el-dialog title="编辑职称" :visible.sync="dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>职位名称</el-tag>
          </td>
          <td>
            <el-input
                v-model="updateJl.name"
                size="small"
                style="margin-left: 8px"
            ></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>职称等级</el-tag>
          </td>
          <td>
            <el-select
                size="small"
                style="margin-left: 8px; margin-right: 8px"
                v-model="updateJl.titleLevel"
                placeholder="职称等级"
            >
              <el-option
                  v-for="item in titleLevels"
                  :key="item"
                  :label="item"
                  :value="item"
              >
              </el-option>
            </el-select>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>是否启用</el-tag>
          </td>
          <td>
            <el-switch
                style="margin-left: 8px"
                v-model="updateJl.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="已启用"
                inactive-text="未启用"
            >
            </el-switch>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate"> 确 定 </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "JoblebelMana",
  data() {
    return {
      jl: {
        name: "",
        titleLevel: "",
      },
      updateJl: {
        name: "",
        titleLevel: "",
        enabled: false,
      },
      titleLevels: ["正高级", "副高级", "中级", "初级", "员级"],
      jls: [],
      dialogVisible: false,
      multipleSelection: [],
    };
  },
  mounted() {
    this.initJls();
  },
  methods: {
    deleteMany() {
      this.$confirm(
          "此操作将永久删除[" +
          this.multipleSelection.length +
          "]个职称, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      )
          .then(() => {
            let ids = "?";
            this.multipleSelection.forEach((item) => {
              ids += "ids=" + item.id + "&";
            });
            this.deleteRequest("/system/basic/joblevel/deleteJoblevels" + ids).then(
                (res) => {
                  if (res) {
                    this.initJls();
                  }
                }
            );
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    doUpdate() {
      console.log(this.updateJl);
      this.putRequest("/system/basic/joblevel/updateJoblevel", this.updateJl).then(
          (res) => {
            if (res) {
              this.initJls();
              this.dialogVisible = false;
            }
          }
      );
    },
    showEditView(data) {
      Object.assign(this.updateJl, data);
      this.updateJl.createDate = "";
      this.dialogVisible = true;
    },
    deleteJobLevel(data) {
      this.$confirm(
          "此操作将永久删除[" + data.name + "]职称, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      )
          .then(() => {
            this.deleteRequest(
                "/system/basic/joblevel/deleteJoblevel/" + data.id
            ).then((res) => {
              if (res) {
                this.initJls();
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
    addJobLevel() {
      if (this.jl.name && this.jl.titleLevel) {
        this.postRequest("/system/basic/joblevel/addJoblevel", this.jl).then((res) => {
          if (res) {
            this.initJls();
          }
        });
      } else {
        this.$message.error("字段不能为空!");
      }
    },
    initJls() {
      this.getRequest("/system/basic/joblevel/").then((res) => {
        if (res) {
          this.jls = res;
          this.jl.name = "";
          this.jl.titleLevel = "";
        }
      });
    }
  }
}
</script>

<style scoped>

</style>