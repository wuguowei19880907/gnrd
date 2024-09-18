<template>
  <div class="data-table">
    <h2>学生信息</h2>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <el-button type="success" @click="add" size="small">添加学生信息</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="rawData" style="width: 100%" border>
      <el-table-column prop="name" label="用户权限名称" width="180" align="center"></el-table-column>
      <el-table-column prop="addr" label="用户权限编码" width="180" align="center"></el-table-column>
      <el-table-column label="操作" align="center" min-width="150">
        <template v-slot="scope">
          <el-button @click="editItem(scope.row)" size="default" type="primary">编 辑</el-button>
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

    <!-- 添加学生的对话框 -->
    <el-dialog title="添加学生" v-model="addDialogVisible" width="400px">
      <el-form :model="newStu" ref="formRef" label-width="100px" label-position="left">
        <el-form-item label="学生姓名" required>
          <el-input v-model="newStu.name" placeholder="请输入学生姓名" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="学生地址" required>
          <el-input v-model="newStu.addr" placeholder="请输入学生地址" style="width: 100%; text-align: right;"></el-input>
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

    <!-- 编辑学生的对话框 -->
    <el-dialog title="编辑学生" v-model="editDialogVisible" width="400px">
      <el-form :model="editStu" ref="editFormRef" label-width="100px" label-position="left">
        <el-form-item label="学生姓名" required>
          <el-input v-model="editStu.name" placeholder="请输入学生姓名" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
        <el-form-item label="学生地址" required>
          <el-input v-model="editStu.addr" placeholder="请输入学生地址" style="width: 100%; text-align: right;"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取 消</el-button>
        <el-button type="primary" :disabled="!isUpdateFormValid" @click="updateStu">编 辑</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import axios from "axios";
import {ElMessage} from "element-plus";
import {BASE_URL, TOKEN_HEADER} from "../constants.js";
export default {
  data() {
    return {
      addDialogVisible: false,
      editDialogVisible: false,
      newStu: {
        name: '',
        addr: ''
      },
      editStu: {
        name: '',
        id: '',
        addr: ''
      },
      total: 0,
      rawData: [], // 原始数据
      pageSize: 10, // 每页显示的数据数量
      currentPage: 1 // 当前页码
    };
  },
  computed: {
    isAddFormValid() {
      // 检查每个输入是否都有值
      return this.newStu.name && this.newStu.addr;
    },
    isUpdateFormValid() {
      // 检查每个输入是否都有值
      return this.editStu.name && this.editStu.id && this.editStu.addr;
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
      axios.get(BASE_URL + '/students', {headers, params: queryData})
          .then(response => {
            this.rawData = response.data.result.content; // 将返回的数据赋值给 menuItems
            this.total = parseInt(response.data.result.totalElements);
          })
          .catch(error => {
            ElMessage({
              message: '获取学生列表失败：' + error.response.data.message,
              type: 'error'
            });
          });
    },
    add() {
      this.addDialogVisible = true;
      // 清空输入框
      this.newStu = {
        name: '',
        addr: ''
      };
    },
    closeAddDialog() {
      this.addDialogVisible = false;
    },
    closeEditDialog() {
      this.editDialogVisible = false;
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
    editItem(item) {
      this.editDialogVisible = true;
      // 清空输入框
      this.editStu = {
        name: item.name,
        id: item.id,
        addr: item.addr
      };
    },
    deleteItem(item) {
      this.$confirm('确定要删除该学生信息吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const headers = {
          'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
        };
        axios.delete(`/students/${item.id}`, { headers })
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
        name: this.newStu.name,
        addr: this.newStu.addr
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.post('/students', userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.addDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '添加学生失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
    updateUser() {
      // 提交数据到后台
      const userData = {
        name: this.editStu.name,
        addr: this.editStu.addr
      };
      const headers = {
        'df-auth-token': sessionStorage.getItem(TOKEN_HEADER)
      };
      axios.put(`/students/${this.editPermission.id}`, userData, {headers}).then(response => {
        this.fetchData(); // 刷新列表
        this.editDialogVisible = false; // 关闭对话框
      }).catch(error => {
        ElMessage({
          message: '编辑学生失败：' + error.response.data.message,
          type: 'error'
        });
      });
    },
  },
  mounted() {
    this.fetchData(); // 组件挂载后获取数据
  }
};
</script>
