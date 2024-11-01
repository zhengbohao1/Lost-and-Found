import axios from "axios";
import {useUserStore} from "@/stores/user";
import { showErrorState } from "@/stores/showErrorState";
import router from '@/router';
import { ElMessage } from 'element-plus';

const http = axios.create({
    baseURL: 'api',
    timeout: 5000,
    retry: 6, //设置全局重试请求次数（最多重试几次请求）
    retryDelay: 2000, //设置全局请求间隔
});

// axios请求拦截器
http.interceptors.request.use(config => {
    const shoeError = showErrorState()
    shoeError.showError.value = false
    const userStore = useUserStore();
    if (userStore.userToken) {
        config.headers.Authorization = `${userStore.userToken}`
    }
    return config
}, e => Promise.reject(e))


// axios响应式拦截器
http.interceptors.response.use(
    (res) => {
      return Promise.resolve(res.data);
    },
    (error) => {
      var config = error.config;

      if (error.response && error.response.status === 401) {
        // 处理401状态码
        const userStore = useUserStore();
        
        if(userStore.userToken){
          ElMessage.error('会话已过期，请重新登录！');
          userStore.loginOut();
        }
        
        return Promise.reject(error);
    }
  
      if (!config || !config.retry) return Promise.reject(error);
  
      config.__retryCount = config.__retryCount || 0;
  
      if (config.__retryCount >= config.retry) {
        // 显示错误信息
        const shoeError = showErrorState()
        shoeError.showError.value = true
        ElMessage.error('请求失败，请稍后再试！');
        return Promise.reject(error);
      }
  
      config.__retryCount += 1;
  
      var backoff = new Promise((resolve) => {
        setTimeout(() => {
          resolve();
        }, config.retryDelay || 1000); // 假设默认延迟为1秒
      });
  
      return backoff.then(() => {
        return http(config);
      });
    }
  );

export default http;