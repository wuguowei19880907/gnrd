<template>
  <div class="common-layout">
    <el-container style="height: 100vh;">
      <!-- Header -->
      <el-header class="header">
        <div class="header-left">xx管理系统</div>
        <div class="header-right">
          当前用户：张三
          <el-button @click="toggleMenu" size="small" type="primary" icon="el-icon-menu">
            {{ menuVisible ? '隐藏菜单' : '显示菜单' }}
          </el-button>
        </div>
      </el-header>

      <el-container>
        <!-- Aside Menu -->
        <el-aside v-if="menuVisible" width="200px" class="aside-menu">
          <el-menu :router="true" default-active="1" class="menu" text-color="white" background-color="#333" active-text-color="#ffd04b">
            <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path" @click="go(item.path)">
<!--              <router-link :to="item.path"></router-link>-->
              {{ item.name }}
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-container>
          <!-- Main Content -->
          <el-main>
            <router-view></router-view>
          </el-main>
          <!-- Footer -->
          <el-footer class="footer">
            <span>当前版本: 1.0.0</span>
            <span>&copy; 2023 xx公司. 保留所有权利.</span>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "App",
  data() {
    return {
      menuVisible: true, // 控制菜单的显示与隐藏
      menuItems: [], // 用于存储菜单数据
    };
  },
  methods: {
    toggleMenu() {
      this.menuVisible = !this.menuVisible; // 切换菜单的可见状态
    },
    fetchMenuData() {
      const headers = {
        'X-Auth-Token': sessionStorage.getItem('X-Auth-Token')
      };
      // 假设后端接口为 /api/menu
      axios.get('/api/current-user', {headers})
          .then(response => {
            this.menuItems = response.data.result; // 将返回的数据赋值给 menuItems
          })
          .catch(error => {
            console.error("获取菜单数据失败:", error);
          });
    },
    go(path) {
      this.$router.push(path)
    }
  },
  mounted() {
    this.fetchMenuData(); // 组件挂载后获取菜单数据
  },
};
</script>

<style>
/* 样式保留不变 */
.el-header {
  background-color: #409eff;
  color: white;
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 左右两侧对齐 */
  align-items: center; /* 垂直居中对齐 */
  padding: 0 20px; /* 左右内边距 */
}

.header-left {
  font-size: 18px; /* 左侧文字大小 */
}

.header-right {
  font-size: 16px; /* 右侧文字大小 */
}

.aside-menu {
  background-color: #333; /* 深色背景 */
}

.menu {
  background-color: #333; /* 将菜单背景设置为深色，与 aside-menu 一致 */
}

.common-layout {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0;
}

h1 {
  margin: 0;
  padding: 20px;
}

h2 {
  padding: 20px;
}

.footer {
  background-color: #f1f1f1; /* Footer 背景颜色 */
  padding: 10px; /* Footer 内边距 */
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  font-size: 14px; /* Footer 字体大小 */
}
</style>
