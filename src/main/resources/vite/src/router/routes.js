const Home = () => import('@/views/Home/index.vue');
const Login = () => import('@/views/Login/index.vue');
const Control = () => import('@/views/Control/index.vue');
const AdminHome = () => import('@/views/Control/Home/index.vue');
const PassPost = () => import('@/views/Control/Manage/post/pass/index.vue');
const DeletePost = () => import('@/views/Control/Manage/post/delete/index.vue');
const ManageUsers = () => import('@/views/Control/Manage/users/index.vue');
const ManageAdvise = () => import('@/views/Control/Manage/advise/index.vue');
const User = () => import('@/views/User/index.vue')
const UserWaterFall = () => import('@/views/User/WaterFall/index.vue');
const UserUpLoad = () => import('@/views/User/UpLoad/index.vue');
const NotFound = () => import('@/views/NotFound/index.vue');
const Test = () => import('@/components/Test2.vue');


    export const routes= [
        {
            path: '/',
            component: User,
            meta: {
                title: '欢迎来到失物招领平台',
                requireAuth: false,
            },
        },
        {
            path: '/control',
            component: Control,
            meta: {
                title: '后台管理',
                admin: false,
            },
            children:[
                {
                    path: 'function/found/deletepost',
                    component: DeletePost,
                    meta: {
                        title: '帖子管理',
                    },
                },
                {
                    path: 'function/found/passpost',
                    component: PassPost,
                    meta: {
                        title: '发帖审核',
                    },
                },
                {
                    path: '/manageusers',
                    component: ManageUsers,
                    meta: {
                        title: '用户管理',
                    },
                },
                {
                    path: '/manageadvises',
                    component: ManageAdvise,
                    meta:{
                        title: '意见反馈'
                    }
                },
                {
                    path: '/adminhome',
                    component: AdminHome,
                    meta: {
                        title: '首页',
                    },
                }
            ]
        },
        {
            path: '/user',
            component: User,
            meta: {
                title: '欢迎来到失物招领平台',
                requireAuth: false,
            },
            children:[
                {
                    path: '/home',
                    component: UserWaterFall,
                    meta: {
                        title: '首页',
                    }
                },
                {
                    path: '/upload',
                    component: UserUpLoad,
                    meta: {
                        title: '发帖',
                    }
                }
            ]
        },
        {
            path: '/login',
            component: Login,
            meta: {
                title: '欢迎登录Dlock分享你的生活',
            },
        },
        {
            path: '/:catchAll(.*)',
            component: NotFound,
            meta: {
                title: '你想找什么呢？',
            },
        },
        {
            path: '/test',
            component: Test,
            meta: {
                title: '测试',
            }
        }
    ];