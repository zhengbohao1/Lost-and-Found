import { App }  from '../App.vue'

export const routes = [
    
      {
        path:'/',
        name:'App',
        component: App,
        meta:{
          requireAuth: false, // 添加该字段，表示进入这个路由是需要登录的
        },
        children:[
          {
            path:'',
            name:'home',
            component: () => import('@/views/HomePage.vue')
          },
          {
            path:'login',
            name:'login',
            meta:{
            requireAuth: false, // 添加该字段，表示进入这个路由是需要登录的
          },
            component: () => import('@/views/LoginPage.vue')
          },
          {
            path:'sign',
            name:'sign',
            component: () => import('@/views/SignPage.vue')
          },
        ]
      },
      {
        path:'/test',
        name:'test',
        component: () => import('@/views/LayoutHeader.vue')
      },
      {
        path:'/test2',
        name:'test2',
        component: () => import('@/components/MyTest.vue')
      }
];