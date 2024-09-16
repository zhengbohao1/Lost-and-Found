import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ELIcons from '@element-plus/icons-vue'
import router from './router'
import Cookies from 'vue-cookies'

const app = createApp(App)
const pinia = createPinia()
 app.use(pinia)
 app.use(ElementPlus)
 app.use(router) //注册路由 
 app.config.globalProperties.$cookies = Cookies;
 Object.keys(ELIcons).forEach((key) => {
    app.component(key, ELIcons[key])
  })
 app.mount('#app')