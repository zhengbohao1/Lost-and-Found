//为几个基础的请求创建一个api文件,方便后续使用
import http from "@/utils/http";

//param一般是对象，类似于一个包含多条数据的json
export const getRequest = (url, params) => {
  return http.get(url, { params });
}

export const postRequest = (url, data) => {
  return http.post(url, data, {  
      headers: {  
        'Content-Type': 'application/json'
      }  
  });
}

export const putRequest = (url, params) => {
  return http.put(url, params,
    {  
      headers: {  
        'Content-Type': 'application/json'
      }  
  });
}

export const deleteRequest = (url, params) => {
  return http.delete(url, { params });
}