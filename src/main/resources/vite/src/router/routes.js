const Home = () => import('@/views/Home/index.vue');
const Login = () => import('@/views/Login/index.vue');
const Control = () => import('@/views/Control/index.vue');
const AdminHome = () => import('@/views/Control/Home/index.vue');
const ManagePost = () => import('@/views/Control/Manage/post/index.vue');
const ManageUsers = () => import('@/views/Control/Manage/users/index.vue');
const AdminDetail = () => import('@/components/control/Detail.vue');
const UserHome = () => import('@/views/User/index.vue')
const NotFound = () => import('@/views/NotFound/index.vue');


    export const routes= [
        {
            path: '/',
            component: Home,
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
            },
            children:[
                {
                    path: '/control/managepost',
                    component: ManagePost,
                    meta: {
                        title: '发帖审核',
                    },
                },
                {
                    path: '/control/manageusers',
                    component: ManageUsers,
                    meta: {
                        title: '用户管理',
                        requireAuth: true,
                    },
                },
                {
                    path: '/control/adminhome',
                    component: AdminHome,
                    meta: {
                        title: '首页',
                    },
                }
            ]
        },
        {
            path: '/user',
            component: UserHome,
            meta: {
                title: '欢迎来到失物招领平台',
                requireAuth: true,
            },
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
    ];