import {createRouter, createWebHistory} from 'vue-router';
import {useUserStore} from "@/stores/user";
import { routes } from "./routes";

const router = createRouter({
    history: createWebHistory(),  //取消hash模式，换为history模式，防止'#'和'/'自动添加
    routes
  });

router.beforeEach((to, from, next) => {
    // 获取目标路由的相关信息，例如路由元信息 meta
    const {meta} = to;

    // 获取用户信息
    const userStore = useUserStore();
    const userInfo = userStore.userInfo;

    // 根据用户信息动态设置网页标题
    if (userInfo.username && meta.title) {
        meta.title = `${userInfo.username} - ${meta.title}`;
    }
    // 路由守卫
    if (meta.requireAuth) { // 判断该路由是否需要登录权限
        if (userStore.userToken) { 
          next();//存在就说明登录了，放行
        }
        else {
          next({  //不存在，回退到一个页面，例如登录页面
            path: '/login',
            //query: {redirect: to.fullPath} // 将跳转的路由path作为参数，登录成功后跳转到该路由
          })
        }
      }
      else {
        next();
      }
});

router.afterEach((to, from) => {
    // 根据当前路由信息来设置新的 title
    document.title = to.meta.title || '欢迎来到失物招领系统';
});

export default router
