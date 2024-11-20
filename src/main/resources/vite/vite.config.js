import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// 引入resolve
import { resolve } from 'path'

// element-plus
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
 
// 文档: https://vitejs.dev/config/ 
export default defineConfig({

  plugins: [
    vue(),

    // element-plus
    AutoImport({
      resolvers: [ElementPlusResolver()],
  }),
  Components({
      // 1.配置elementPlus采用sass
      resolvers: [ElementPlusResolver({importStyle: "sass"})],
  }),
],

  // 配置路径别名
  resolve: {
    alias: {
      '@': resolve(__dirname, './src'),
    },
  },

  // 配置代理
  server: {
    cors: true,
    proxy: {
      '/api': {
        target: "http://localhost:8090",  //之后再用evn文件来配置
        changeOrigin: true,
        ws: true,
        rewrite: (path) => path.replace(/^\/api/, '') // 重写请求
      }
    }
  },
})