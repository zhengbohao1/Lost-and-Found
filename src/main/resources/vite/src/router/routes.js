const Home = () => import('@/views/Home/index.vue');
const Login = () => import('@/views/Login/index.vue');
const Control = () => import('@/views/Control/index.vue');
const AdminHome = () => import('@/views/Control/Home/index.vue');
const PassFound = () => import('@/views/Control/Manage/found/pass/index.vue');
const DeleteFound = () => import('@/views/Control/Manage/found/delete/index.vue');
const PassLost = () => import('@/views/Control/Manage/lost/pass/index.vue');
const DeleteLost = () => import('@/views/Control/Manage/lost/delete/index.vue')
const ManageUsers = () => import('@/views/Control/Manage/users/index.vue');
const ManageAdvise = () => import('@/views/Control/Manage/advise/index.vue');
const User = () => import('@/views/User/index.vue');
const Space = () => import('@/views/User/Space/index.vue');
const Message = () => import('@/views/User/Message/index.vue');
const FoundWaterFall = () => import('@/views/User/WaterFall/found/index.vue');
const LostWaterFall = () => import('@/views/User/WaterFall/lost/index.vue');
const UserUpLoad = () => import('@/views/User/UpLoad/index.vue');
const FeedBack = () => import('@/views/User/FeedBack/index.vue');
const NotFound = () => import('@/views/NotFound/index.vue');
const Test = () => import('@/components/Test2.vue');


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
                requireAuth: true,
                admin: true,
            },
            children:[
                {
                    path: 'function/found/deletepost',
                    component: DeleteFound,
                    meta: {
                        title: '帖子管理',
                    },
                },
                {
                    path: 'function/found/passpost',
                    component: PassFound,
                    meta: {
                        title: '发帖审核',
                    },
                },
                {
                    path: 'function/lost/deletepost',
                    component: DeleteLost,
                    meta: {
                        title: '帖子管理',
                    },
                },
                {
                    path: 'function/lost/passpost',
                    component: PassLost,
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
                isNotAdmin: true,
            },
            children:[
                {
                    path: 'found',
                    component: FoundWaterFall,
                    meta: {
                        title: '失物招领',
                    },
                },
                {
                    path: 'lost',
                    component: LostWaterFall,
                    meta: {
                        title: '寻物启事',
                    }
                },
                {
                    path: 'space',
                    component: Space,
                    meta: {
                        title: '个人空间',
                        requireAuth: true,
                        isUser: true,
                        isAdmin: false,
                    }
                },
                {
                    path: 'upload',
                    component: UserUpLoad,
                    meta: {
                        title: '发帖',
                        requireAuth: false,
                        isUser: true,
                    }
                },
                {
                    path: 'message',
                    component: Message,
                    meta:{
                        title: '我的通知'
                    }
                },
                {
                    path: 'feedback',
                    component: FeedBack,
                    meta: {
                        title: '意见反馈',
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