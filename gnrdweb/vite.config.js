import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [AutoImport({
    resolvers: [ElementPlusResolver()],
  }),
    Components({
      resolvers: [ElementPlusResolver()],
    }), vue()],
  server: {
    proxy: {
      '/': {
        target: 'http://localhost:8081', // 替换为你的后端 API 地址
        changeOrigin: true, // 是否改变请求源
        // rewrite: (path) => path.replace(/^\/api/, ''), // 可选，重写路径
      },
    },
  },
})
