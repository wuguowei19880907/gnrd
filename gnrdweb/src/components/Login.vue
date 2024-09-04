<template>
  <div class="login-container">
    <el-card class="login-card" shadow="always">
      <h2>登录</h2>
      <el-form :model="form" ref="formRef" label-width="70px" @submit.native.prevent>
        <el-form-item label="用户名" prop="username" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :disabled="isButtonDisabled">登录</el-button>
        </el-form-item>
      </el-form>
      <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';
import JSEncrypt from 'jsencrypt';

const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4xg8XEY89wr4P9DQGRdeRldLk7PsnHt6zgvm+J6CNteN733llu8OpVTJAXLhVXg4Ntj2e+KEghHd+CyCOry8u0iLhnH4j/Zrdc1+xYRkBvt2Zcxb20O7XLWJoltbNZ1uHo/TcOhDOgX1MY3UvKoxvDAp0Wj5Eq8GK0NbHXRZENmrQ1O1OuDH5uJ5MEB7ceF8Mc0PwV5skk0EfrPL6S5dGUulc7QhopynBLX/iKGOJ/LXyXPp5XpaVuh7dNu/rAAegXufRK4q8zmfUrmTrrOddSUMmNBAFOZmrbmiLmoE950HgI5LVg9eLCBRVp4gwQPaEQYt+AT323urNJ4xEkFpewIDAQAB';

export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
      errorMessage: '',
    };
  },
  computed: {
    isButtonDisabled() {
      // 检查用户名和密码是否为空
      return !this.form.username || !this.form.password;
    }
  },
  methods: {
    handleLogin() {
      const encrypt = new JSEncrypt();
      encrypt.setPublicKey(publicKey); // 设置公钥
      const encryptedUsername = encrypt.encrypt(this.form.username); // 加密用户名
      const encryptedPassword = encrypt.encrypt(this.form.password); // 加密密码
      // 模拟登录请求
      axios.post('/api/common/login', {
        username: encryptedUsername,
        password: encryptedPassword,
      }).then(response => {
        if ('000000' === response.data.code) {
          if (response.data.result.flag) {
            sessionStorage.setItem('X-Auth-Token', response.data.result.content);
            this.$router.push({name: 'CenterContent'});
          } else {
            window.location.href = response.data.result.content;
          }
        } else {
          this.errorMessage = response.data.message;
        }
      }).catch(error => {
        console.error('登录失败:', error.response.data);
        this.errorMessage = error.response.data.message;
      });
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #4e54c8, #8f94fb); /* 渐变背景 */
}

.login-card {
  width: 400px;
  padding: 20px;
  text-align: center;
  border-radius: 10px; /* 圆角边框 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  background-color: white; /* 白色背景用于卡片 */
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
