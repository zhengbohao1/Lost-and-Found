import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/store/useAuthStore/useAuthStore';
import { routes } from "./routes";

const router = createRouter({
  history: createWebHistory(),  //取消hash模式，换为history模式，防止'#'和'/'自动添加
  routes
});

//路由守卫,定义重写route的成员函数，在main中注册route，route运作时自动调用
	//to表示目标路由 from从什么地方来的路由、对象
	//next 进行下一个路由 需要放行
  router.beforeEach((to, from, next) => { 
    const useAuth = useAuthStore();
    if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
      if (useAuth.$state.token) { // 通过vuex state获取当前的token是否存在    注意token有时效性
        next();//存在就说明登录了，放行
      }
      else {
        next({  //不存在，回退到一个页面，例如登录页面
          path: '/',
          query: {redirect: to.fullPath} // 将跳转的路由path作为参数，登录成功后跳转到该路由
        })
      }
    }
    else {
      next();
    }
  })

export default router
