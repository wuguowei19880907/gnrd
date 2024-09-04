<template>
  <div class="data-table">
    <h2>home</h2>

  </div>
</template>

<script>
export default {
  data() {
    return {
      query1: '', // 第一个查询框的绑定值
      query2: '', // 第二个查询框的绑定值
      rawData: [], // 原始数据
      pageSize: 10, // 每页显示的数据数量
      currentPage: 1 // 当前页码
    };
  },
  computed: {
    filteredData() {
      let filtered = this.rawData;

      // 根据两个查询框的值过滤数据
      if (this.query1) {
        filtered = filtered.filter(item => item.name.includes(this.query1));
      }
      if (this.query2) {
        filtered = filtered.filter(item => item.date.includes(this.query2));
      }

      // 返回当前页数据
      const start = (this.currentPage - 1) * this.pageSize;
      return filtered.slice(start, start + this.pageSize);
    }
  },
  methods: {
    fetchData() {
      // 在这里模拟获取数据，实际情况应为从 API 获取
      this.rawData = [
        { name: "张三", date: "2023-01-01", status: "已完成" },
        { name: "李四", date: "2023-01-02", status: "待处理" },
        { name: "王五", date: "2023-01-03", status: "已完成" },
        // 添加更多数据...
      ];
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage; // 更新当前页码
    },
    editItem(item) {
      console.log("编辑项:", item);
      // 实现编辑项的逻辑
    },
    deleteItem(item) {
      console.log("删除项:", item);
      // 实现删除项的逻辑
    }
  },
  mounted() {
    this.fetchData(); // 组件挂载后获取数据
  }
};
</script>

<style>
.data-table {
  padding: 20px;
}
.search-box {
  margin-bottom: 20px;
}
</style>
