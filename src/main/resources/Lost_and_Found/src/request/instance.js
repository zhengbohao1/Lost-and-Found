import axios from 'axios';
import {Message} from 'element-plus'
import router from '../router';
import { useAuthStore } from '@/store/useAuthStore/useAuthStore';

  const request = axios.create({
      //创建axios实体
      baseURL: 'api', //基础路径上会携带/api
      timeout: 5000, //超时的时间
    })
  //封装了 Axios 的实例，因此可以直接调用方法

//拦截器的作用在于判断登录并添加token，和具体请求无关
request.interceptors.request.use(config => {
  const authStore = useAuthStore();
  if(config.url !== 'api/user/'){
    if (authStore.getToken) {
      config.headers['token'] = authStore.getToken;
    }
  }
  return config;
  //config包含了即将发出的HTTP请求的所有配置信息
  },
  error =>{
    return Promise.reject(error);
  }
)

request.interceptors.response.use(
  response => {
    if (response.data.code == 200) {  
      // 假设服务器返回的数据中包含需要更新的状态  
      // 调用Pinia store中的方法来更新状态  
    }
    return response;
  },  
  error => {
    if (error.response.status == 504 || error.response.status == 404) {
        Message.error({message: '服务器被吃了( ╯□╰ )'})
    } else if (error.response.status == 403) {
        Message.error({message: '权限不足，请联系管理员'})
    } 
    
    else if (error.response.status == 401) {
        Message.error({message: error.response.data.msg ? error.response.data.msg : '尚未登录，请登录'})
        router.replace('/');
    } else {
        if (error.response.data.msg) {
            Message.error({message: error.response.data.msg})
        } else {
            Message.error({message: '未知错误!'})
        }
    }
    return;
    //return Promise.reject(error); 
  }
)

export default request;