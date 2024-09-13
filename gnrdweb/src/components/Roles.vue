<template>
  <div class="data-table">
    <h2>用户角色列表</h2>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <div style="display: flex; align-items: center;">
        <el-input
            v-model="query1"
            placeholder="请输入查询内容"
            size="small"
            style="width: 200px; margin-right: 10px;"
        ></el-input>
        <!-- 添加状态下拉框 -->
        <el-select v-model="selectedState" placeholder="选择状态" clearable size="small" style="width: 100px; margin-right: 10px;" :value-on-clear="null">
          <el-option label="启用" :value="1"></el-option>
          <el-option label="禁用" :value="0"></el-option>
        </el-select>
        <el-button type="primary" @click="fetchData" size="small">搜索</el-button>
      </div>

      <el-button type="success" @click="add" size="small">添加用户角色</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="rawData" style="width: 100%" border>
      <el-table-column prop="name" label="用户角色名称" width="180" align="center"></el-table-column>
      <el-table-column prop="code" label="用户角色编码" width="180" align="center"></el-table-column>
      <el-table-column prop="state" label="用户角色状态" align="center" width="120">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.state"
              @change="handleStatusChange(scope.row)"
              active-text="启用"
              inactive-text="禁用"
              class="ml-2"
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              :active-value="1"
              :inactive-value="0">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150">
        <template v-slot="scope">
          <el-button @click="editItem(scope.row)" size="default" type="primary">编 辑</el-button>
          <el-button @click="config(scope.row)" size="default" type="success">配置用户权限</el-button>
          <el-button @click="deleteItem(scope.row)" type="danger" size="default">删 除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 分页组件 -->
    <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[1, 2, 5, 10]"
        :total="total"
        :page-size="pageSize"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
    ></el-pagination>

    <!-- 添加角色的对话框 -->
    <el-dialog title="添加用户角色" v-model="addDialogVisible" width="400px">
      <el-form :model="newRole" ref="formRef" label-width="100px" label-position="left">
        <el-form-item label="用户角色名称" required>
          <el-input v-model="newRole.name" placeholder="请输入用户角色名称" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="用户角色编码" required>
          <el-input v-model="newRole.code" placeholder="请输入用户角色编码" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="设置用户权限">
          <el-select
              v-model="selectPermissions"
              multiple
              placeholder="请设置用户权限"
              style="width: 240px"
          >
            <el-option
                v-for="permission in permissions"
                :key="permission.id"
                :label="permission.name"
                :value="permission.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAddDialog">取 消</el-button>
        <el-button
            type="primary"
            :disabled="!isAddFormValid"
            @click="submitAdd">添 加</el-button>
      </span>
    </el-dialog>

    <!-- 编辑用户角色的对话框 -->
    <el-dialog title="编辑用户角色" v-model="editDialogVisible" width="400px">
      <el-form :model="editRole" ref="editFormRef" label-width="100px" label-position="left">
        <el-form-item label="用户角色名称" required>
          <el-input v-model="editRole.name" placeholder="请输入用户角色名称" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="用户角色编码" required>
          <el-input v-model="editRole.code" placeholder="请输入用户角色编码" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取 消</el-button>
        <el-button type="primary" :disabled="!isUpdateFormValid" @click="updateUser">编 辑</el-button>
      </span>
    </el-dialog>

    <!-- 设置角色权限的对话框 -->
    <el-dialog title="设置用户权限" v-model="configDialogVisible" width="400px">
      <el-form ref="configFormRef" label-width="100px" label-position="left">
        <el-form-item label="设置用户权限">
          <el-select
              v-model="selectPermissions"
              multiple
              placeholder="请设置用户权限"
              style="width: 240px"
          >
            <el-option
                v-for="permission in permissions"
                :key="permission.id"
                :label="permission.name"
                :value="permission.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeConfigDialog">取 消</el-button>
        <el-button type="primary" @click="reConfig">编 辑</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import {toRaw} from "@vue/reactivity";
import {TOKEN_HEADER} from "../constants.js";


export default {
  data() {
    return {
      query1: '',
      selectedState: null,
      addDialogVisible: false,
      editDialogVisible: false,
      configDialogVisible: false,
      newRole: {
        name: '',
        code: ''
      },
      editRole: {
        name: '',
        id: '',
        code: ''
      },
      reConfigData: {
        id: ''
      },
      total: 0,
      rawData: [], // 原始数据
      permissions: [], // 原始数据
      selectPermissions: [], // 原始数据
      pageSize: 10, // 每页显示的数据数量
      currentPage: 1 // 当前页码
    };
  },
  computed: {
    isAddFormValid() {
      // 检查每个输入是否都有值
      return this.newRole.name && this.newRole.code;
    },
    isUpdateFormValid() {
      // 检查每个输入是否都有值
      return this.editRole.name && this.editRole.id && this.editRole.code;
    }
  },
  methods: {
    fetchData() {
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      const queryData = {
        number: this.currentPage,
        size: this.pageSize
      };
      if (this.query1 !== '') {
        queryData.nameQuery = this.query1;
      }
      if (this.selectedState !== null && this.selectedState !== '') {
        queryData.state = parseInt(this.selectedState);
      }
      axios.get('/df-admin/roles', {headers, params: queryData})
          .then(response => {
            this.rawData = response.data.result.content; // 将返回的数据赋值给 rawData
            this.total = parseInt(response.data.result.totalElements);
          })
          .catch(error => {
            ElMessage({
              message: '获取用户角色列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    getPermissions() {
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.get('/df-admin/roles/permissions', {headers})
          .then(response => {
            this.permissions = response.data.result; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取权限列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    add() {
      this.addDialogVisible = true;
      // 清空输入框
      this.newRole = {
        name: '',
        code: ''
      };
      this.selectPermissions = []
    },
    closeAddDialog() {
      this.dialogVisible = false;
    },
    closeEditDialog() {
      this.editDialogVisible = false;
    },
    closeConfigDialog() {
      this.configDialogVisible = false;
    },
    handleSizeChange(newSize) {
      this.currentPage = 1;
      this.pageSize = newSize;
      this.fetchData();
    },
    handleCurrentChange(currentPageNew) {
      this.currentPage = currentPageNew;
      this.fetchData();
    },
    handleStatusChange(role) {
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      // 提交数据到后台
      const userData = {
        state: role.state
      };
      axios.put(`/df-admin/roles/${role.id}/change-status`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
      }).catch(error => {
        ElMessage({
          message: '修改用户角色状态失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    editItem(item) {
      this.editDialogVisible = true;
      // 清空输入框
      this.editRole = {
        name: item.name,
        id: item.id,
        code: item.code
      };
    },
    config(item) {
      this.configDialogVisible = true;
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      this.reConfigData.id = item.id;
      axios.get(`/df-admin/roles/${item.id}/permissions`, { headers })
          .then(response => {
            this.selectPermissions = response.data.result.permissionIds; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取用户角色已配置权限失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    deleteItem(item) {
      this.$confirm('确定要删除该用户角色吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const headers = {
          'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
        };
        axios.delete(`/df-admin/roles/${item.id}`, { headers })
            .then(response => {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.fetchData(); // 刷新列表
            })
            .catch(error => {
              ElMessage({
                message: '删除失败, 请重试：' + error.response.data.message,
                type: 'error'
              });
            });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    submitAdd() {
      // 提交数据到后台
      const userData = {
        name: this.newRole.name,
        code: this.newRole.code
      };
      if (this.selectPermissions.length > 0) {
        userData.permissionIds = toRaw(this.selectPermissions);
      }
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.post('/df-admin/roles', userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.addDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '添加用户角色失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    updateUser() {
      // 提交数据到后台
      const userData = {
        name: this.editRole.name,
        code: this.editRole.code
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/df-admin/roles/${this.editRole.id}`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.editDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '编辑用户角色失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    reConfig() {
      // 提交数据到后台
      const userData = {
        permissionIds: this.selectPermissions
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/df-admin/roles/${this.reConfigData.id}/permissions`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.configDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '配置用户权限失败：' + error.response.data.message,
          type: 'error'
        });
      });
    }
  },
  mounted() {
    this.fetchData(); // 组件挂载后获取数据
    this.getPermissions();
  }
};
</script>

<style>
.data-table {
  padding: 20px;
}
</style>
