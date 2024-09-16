//可以存在多个store，每个store对应一个数据组，有一个唯一标识
import { createPinia } from "pinia" //引入pinia
import useUserStore from './useUserStore/useUserStore' //引入userStore
import useAuthStore from './useAuthStore/useAuthStore' //引入authStore
 
const pinia = createPinia() //创建pinia实例

useUserStore(pinia) //注册userStore
useAuthStore(pinia) //注册authStore
 
export default pinia //导出pinia用于main.js注册 