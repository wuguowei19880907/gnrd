<template>
  <div class="data-table">
    <h2>用户权限列表</h2>

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

      <el-button type="success" @click="add" size="small">添加用户权限</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="rawData" style="width: 100%" border>
      <el-table-column prop="name" label="用户权限名称" width="180" align="center"></el-table-column>
      <el-table-column prop="code" label="用户权限编码" width="180" align="center"></el-table-column>
      <el-table-column prop="state" label="用户权限状态" align="center" width="120">
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
          <el-button @click="config(scope.row)" size="default" type="success">配置url地址</el-button>
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

    <!-- 添加权限的对话框 -->
    <el-dialog title="添加用户权限" v-model="addDialogVisible" width="400px">
      <el-form :model="newPermission" ref="formRef" label-width="100px" label-position="left">
        <el-form-item label="用户权限名称" required>
          <el-input v-model="newPermission.name" placeholder="请输入用户权限名称" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="用户权限编码" required>
          <el-input v-model="newPermission.code" placeholder="请输入用户权限编码" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="设置请求映射">
          <el-select
              v-model="selectRequestMappings"
              multiple
              placeholder="请设置请求映射"
              style="width: 240px"
          >
            <el-option
                v-for="requestMapping in requestMappings"
                :key="requestMapping.id"
                :label="requestMapping.name"
                :value="requestMapping.id"
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

    <!-- 编辑用户权限的对话框 -->
    <el-dialog title="编辑用户权限" v-model="editDialogVisible" width="400px">
      <el-form :model="editPermission" ref="editFormRef" label-width="100px" label-position="left">
        <el-form-item label="用户权限名称" required>
          <el-input v-model="editPermission.name" placeholder="请输入用户权限名称" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="用户权限编码" required>
          <el-input v-model="editPermission.code" placeholder="请输入用户权限编码" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取 消</el-button>
        <el-button type="primary" :disabled="!isUpdateFormValid" @click="updateUser">编 辑</el-button>
      </span>
    </el-dialog>

    <!-- 设置url地址的对话框 -->
    <el-dialog title="设置请求映射" v-model="configDialogVisible" width="400px">
      <el-form ref="configFormRef" label-width="100px" label-position="left">
        <el-form-item label="设置请求映射">
          <el-select
              v-model="selectRequestMappings"
              multiple
              placeholder="请设置请求映射"
              style="width: 240px"
          >
            <el-option
                v-for="requestMapping in requestMappings"
                :key="requestMapping.id"
                :label="requestMapping.name"
                :value="requestMapping.id"
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


export default {
  data() {
    return {
      query1: '',
      selectedState: null,
      addDialogVisible: false,
      editDialogVisible: false,
      configDialogVisible: false,
      newPermission: {
        name: '',
        code: ''
      },
      editPermission: {
        name: '',
        id: '',
        code: ''
      },
      reConfigData: {
        id: ''
      },
      total: 0,
      rawData: [], // 原始数据
      requestMappings: [], // 原始数据
      selectRequestMappings: [], // 原始数据
      pageSize: 10, // 每页显示的数据数量
      currentPage: 1 // 当前页码
    };
  },
  computed: {
    isAddFormValid() {
      // 检查每个输入是否都有值
      return this.newPermission.name && this.newPermission.code;
    },
    isUpdateFormValid() {
      // 检查每个输入是否都有值
      return this.editPermission.name && this.editPermission.id && this.editPermission.code;
    }
  },
  methods: {
    fetchData() {
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
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
      axios.get('/api/df-admin/permissions', {headers, params: queryData})
          .then(response => {
            this.rawData = response.data.result.content; // 将返回的数据赋值给 menuItems
            this.total = parseInt(response.data.result.totalElements);
          })
          .catch(error => {
            ElMessage({
              message: '获取用户权限列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    getRequestMappings() {
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      axios.get('/api/df-admin/permissions/request-mappings', {headers})
          .then(response => {
            this.requestMappings = response.data.result; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取request mappings列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    add() {
      this.addDialogVisible = true;
      // 清空输入框
      this.newPermission = {
        name: '',
        code: ''
      };
      this.selectRequestMappings = []
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
    handleStatusChange(permission) {
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      // 提交数据到后台
      const userData = {
        state: permission.state
      };
      axios.put(`/api/df-admin/permissions/${permission.id}/change-status`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
      }).catch(error => {
        ElMessage({
          message: '修改用户权限状态失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    editItem(item) {
      this.editDialogVisible = true;
      // 清空输入框
      this.editPermission = {
        name: item.name,
        id: item.id,
        code: item.code
      };
    },
    config(item) {
      this.configDialogVisible = true;
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      this.reConfigData.id = item.id;
      axios.get(`/api/df-admin/permissions/${item.id}/request-mappings`, { headers })
          .then(response => {
            this.selectRequestMappings = response.data.result.requestIds; // 将返回的数据赋值
          })
          .catch(error => {
            ElMessage({
              message: '获取用户权限失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    deleteItem(item) {
      this.$confirm('确定要删除该用户权限吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const headers = {
          'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
        };
        axios.delete(`/api/df-admin/permissions/${item.id}`, { headers })
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
        name: this.newPermission.name,
        code: this.newPermission.code
      };
      if (this.selectRequestMappings.length > 0) {
        userData.requestIds = toRaw(this.selectRequestMappings);
      }
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      axios.post('/api/df-admin/permissions', userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.addDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '添加用户权限失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    updateUser() {
      // 提交数据到后台
      const userData = {
        name: this.editPermission.name,
        code: this.editPermission.code
      };
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      axios.put(`/api/df-admin/permissions/${this.editPermission.id}`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.editDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '编辑用户权限失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    reConfig() {
      // 提交数据到后台
      const userData = {
        requestIds: this.selectRequestMappings
      };
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      axios.put(`/api/df-admin/permissions/${this.reConfigData.id}/request-mappings`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.configDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '配置url地址失败：' + error.response.data.message,
          type: 'error'
        });
      });
    }
  },
  mounted() {
    this.fetchData(); // 组件挂载后获取数据
    this.getRequestMappings();
  }
};
</script>

<style>
.data-table {
  padding: 20px;
}
</style>
