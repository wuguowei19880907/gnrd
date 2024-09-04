<template>
  <el-container style="height: 100%;">
    <el-aside width="200px">
      <el-menu>
        <el-menu-item index="1">
          <router-link to="/index/users">用户管理</router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <router-link to="/index/permissions">设置</router-link>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <h2>欢迎来到管理系统</h2>
      </el-header>
      <el-main>
        <router-view /> <!-- 这里会根据菜单项的选择更新内容 -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {};
</script>
