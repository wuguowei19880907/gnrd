<template>
  <div class="data-table">
    <h2>后台用户列表</h2>

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

      <el-button type="success" @click="addUser" size="small">添加用户</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="rawData" style="width: 100%" border>
      <el-table-column prop="name" label="用户名" width="180" align="center"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="180" align="center"></el-table-column>
      <el-table-column prop="state" label="状态" align="center" width="120">
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
      <el-table-column prop="roles" label="角色" align="center">
        <template v-slot="scope">
          <el-tag
              v-for="role in scope.row.roles"
              :key="role.id"
              type="warning"
              effect="dark"
              round
              style="margin-right: 8px;"
          >
            {{ role.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150">
        <template v-slot="scope">
          <el-button @click="editItem(scope.row)" size="default" type="primary">编 辑</el-button>
          <el-button @click="configRole(scope.row)" size="default" type="primary">设置用户角色</el-button>
          <el-button @click="resetPassword(scope.row)" size="default" type="success">重置密码</el-button>
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

    <!-- 添加用户的对话框 -->
    <el-dialog title="添加用户" v-model="dialogVisible" width="400px">
      <el-form :model="newUser" ref="formRef" label-width="100px" label-position="left">
        <el-form-item label="用户名" required>
          <el-input v-model="newUser.name" placeholder="请输入用户名" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input type="password" v-model="newUser.password" placeholder="请输入密码" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="newUser.phone" placeholder="请输入手机号" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="设置用户角色">
          <el-select
              v-model="selectRoles"
              multiple
              placeholder="请设置用户角色"
              style="width: 240px"
          >
            <el-option
                v-for="role in roles"
                :key="role.id"
                :label="role.name"
                :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button
            type="primary"
            :disabled="!isAddFormValid"
            @click="submitUser">添 加</el-button>
      </span>
    </el-dialog>

    <!-- 编辑用户的对话框 -->
    <el-dialog title="编辑用户" v-model="editDialogVisible" width="400px">
      <el-form :model="editedUser" ref="editFormRef" label-width="100px" label-position="left">
        <el-form-item label="用户名" required>
          <el-input v-model="editedUser.name" placeholder="请输入用户名" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="editedUser.phone" placeholder="请输入手机号" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取 消</el-button>
        <el-button type="primary" :disabled="!isUpdateFormValid" @click="updateUser">编 辑</el-button>
      </span>
    </el-dialog>

    <!-- 重置用户登录密码的对话框 -->
    <el-dialog title="重置密码" v-model="resetDialogVisible" width="400px">
      <el-form :model="resetUserPwd" ref="editFormRef" label-width="100px" label-position="left">
        <el-form-item label="请输入密码" required>
          <el-input v-model="resetUserPwd.newPassword" placeholder="请输入用户名" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" required>
          <el-input v-model="resetUserPwd.confirmPassword" placeholder="请输入手机号" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeResetDialog">取 消</el-button>
        <el-button type="primary" :disabled="!isResetFormValid" @click="resetUser">编 辑</el-button>
      </span>
    </el-dialog>

    <!-- 设置用户角色的对话框 -->
    <el-dialog title="设置用户角色" v-model="configDialogVisible" width="400px">
      <el-form ref="configFormRef" label-width="100px" label-position="left">
        <el-form-item label="设置用户角色">
          <el-select
              v-model="selectRoles"
              multiple
              placeholder="请设置用户角色"
              style="width: 240px"
          >
            <el-option
                v-for="role in roles"
                :key="role.id"
                :label="role.name"
                :value="role.id"
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
      dialogVisible: false,
      editDialogVisible: false,
      resetDialogVisible: false,
      configDialogVisible: false,
      newUser: {
        name: '',
        password: '',
        phone: ''
      },
      editedUser: {
        name: '',
        id: '',
        phone: ''
      },
      resetUserPwd: {
        id: '',
        newPassword: '',
        confirmPassword: ''
      },
      reConfigData: {
        id: ''
      },
      total: 0,
      rawData: [], // 原始数据
      roles: [], // 原始数据
      selectRoles: [], // 原始数据
      pageSize: 10, // 每页显示的数据数量
      currentPage: 1 // 当前页码
    };
  },
  computed: {
    isAddFormValid() {
      // 检查每个输入是否都有值
      return this.newUser.name && this.newUser.password && this.newUser.phone;
    },
    isUpdateFormValid() {
      // 检查每个输入是否都有值
      return this.editedUser.name && this.editedUser.id && this.editedUser.phone;
    },
    isResetFormValid() {
      // 检查每个输入是否都有值
      return this.resetUserPwd.id && this.resetUserPwd.newPassword && this.resetUserPwd.confirmPassword;
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
      axios.get('/df-admin/users', {headers, params: queryData})
          .then(response => {
            this.rawData = response.data.result.content; // 将返回的数据赋值给 menuItems
            this.total = parseInt(response.data.result.totalElements);
          })
          .catch(error => {
            ElMessage({
              message: '获取后台用户列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    addUser() {
      this.dialogVisible = true;
      // 清空输入框
      this.newUser = {
        name: '',
        password: '',
        phone: ''
      };
      this.selectRoles = [];
    },
    getRoles() {
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.get('/df-admin/users/roles', {headers})
          .then(response => {
            this.roles = response.data.result; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取权限列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    closeEditDialog() {
      this.editDialogVisible = false;
    },
    closeResetDialog() {
      this.resetDialogVisible = false;
    },
    closeConfigDialog() {
      this.configDialogVisible = false;
      this.reConfigData.id = '';
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
    handleStatusChange(user) {
      if (user.state === 0) {
        const headers = {
          'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
        };
        axios.put(`/df-admin/users/${user.id}/disable`, {headers}).then(response => {
          this.fetchData(); // 刷新列表
        }).catch(error => {
          ElMessage({
            message: '修改用户状态失败：' + error.response.data.message,
            type: 'error'
          });
        });
      } else {
        const headers = {
          'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
        };
        axios.put(`/df-admin/users/${user.id}/enable`, {headers}).then(response => {
          this.fetchData(); // 刷新列表
        }).catch(error => {
          ElMessage({
            message: '修改用户状态失败：' + error.response.data.message,
            type: 'error'
          });
        });
      }
    },
    configRole(item) {
      this.configDialogVisible = true;
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      this.reConfigData.id = item.id;
      axios.get(`/df-admin/users/${item.id}/roles`, { headers })
          .then(response => {
            this.selectRoles = response.data.result.roleIds; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取用户已配置角色失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    editItem(item) {
      this.editDialogVisible = true;
      // 清空输入框
      this.editedUser = {
        name: '',
        id: item.id,
        phone: ''
      };
    },
    resetPassword(item) {
      this.resetDialogVisible = true;
      // 清空输入框
      this.resetUserPwd = {
        id: item.id,
        password: ''
      };
    },
    deleteItem(item) {
      this.$confirm('确定要删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const headers = {
          'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
        };
        // 假设删除接口为 /df-admin/users/{id}
        axios.delete(`/df-admin/users/${item.id}`, { headers })
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
    submitUser() {
      // 提交数据到后台
      const userData = {
        name: this.newUser.name,
        password: this.newUser.password,
        phone: this.newUser.phone
      };
      if (this.selectRoles.length > 0) {
        userData.roleIds = toRaw(this.selectRoles);
      }
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.post('/df-admin/users', userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.dialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '添加后台用户失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    updateUser() {
      // 提交数据到后台
      const userData = {
        name: this.editedUser.name,
        phone: this.editedUser.phone
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/df-admin/users/${this.editedUser.id}`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.dialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '编辑后台用户失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    resetUser() {
      if (this.resetUserPwd.newPassword !== this.resetUserPwd.confirmPassword) {
        ElMessage({
          message: '两次密码不相同',
          type: 'error'
        });
        return;
      }
      // 提交数据到后台
      const userData = {
        password: this.resetUserPwd.newPassword
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/df-admin/users/${this.resetUserPwd.id}/reset-password`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.dialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '重置密码失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    reConfig() {
      // 提交数据到后台
      const userData = {
        roleIds: this.selectRoles
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/df-admin/users/${this.reConfigData.id}/roles`, userData, {headers}).then(response => {
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
    this.getRoles();
  }
};
</script>

<style>
.data-table {
  padding: 20px;
}
</style>
