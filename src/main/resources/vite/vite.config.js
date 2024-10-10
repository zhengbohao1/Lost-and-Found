import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// element-plus
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'



// https://vitejs.dev/config/
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
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
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
