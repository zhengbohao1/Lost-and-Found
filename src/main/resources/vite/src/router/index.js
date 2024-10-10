import {createRouter, createWebHistory} from 'vue-router';
import {useUserStore} from "@/stores/user";
import { routes } from "./routes";

const router = createRouter({
    history: createWebHistory(),  //取消hash模式，换为history模式，防止'#'和'/'自动添加
    routes,
    strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
  });

  router.beforeEach(async (to, from, next) => {
    // 获取目标路由的相关信息，例如路由元信息 meta
    const { meta } = to;  
  
    // 获取用户信息
    const userStore = useUserStore();
    const userInfo = userStore.userInfo;
  
    // 动态设置网页标题
    if (userInfo.username && meta.title) {
      document.title = `${userInfo.username} - ${meta.title}`;
    }
  
    // 路由守卫
    if (meta.requireAuth) { // 判断该路由是否需要登录权限
      if (userStore.userToken) { // 用户已登录
        if (!checkRoleAccess(meta, userStore)) {
          ElMessage.error('用户权限不足');
          next(from.path);
          return;
        }
        next();
      } else {
        // 用户未登录，重定向到登录页面
        next({
          path: '/login',
          query: { redirect: to.fullPath }
        });
      }
    } else {
      if( meta.isNotAdmin && userStore.isAdmin)
      {
        ElMessage.error('请退出管理员登录');
        next({ path: from.path });
      }
      next();
    }
  });
  
  // 辅助函数检查用户角色
  function checkRoleAccess(meta, userStore) {
    if (meta.admin && !userStore.isAdmin) {
      return false;
    }
    if (meta.isUser && !userStore.isUser) {
      return false;
    }
    return true;
  }

router.afterEach((to, from) => {
    // 根据当前路由信息来设置新的 title
    document.title = to.meta.title || '欢迎来到失物招领系统';
});

export default router
