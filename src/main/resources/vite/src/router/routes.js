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
const MyFoundPost = () => import('@/views/User/Space/found/index.vue');
const MyLostPost = () => import('@/views/User/Space/lost/index.vue');
const waitPostFound = () => import('@/views/User/Space/wait/found/index.vue')
const waitPostLost = () => import('@/views/User/Space/wait/lost/index.vue')
const backPostFound = () => import('@/views/User/Space/back/found/index.vue')
const backPostLost = () => import('@/views/User/Space/back/lost/index.vue')
const Prize = () => import('@/views/User/Space/prize/index.vue');
const Message = () => import('@/views/User/Message/index.vue');
const Reply = () => import('@/views/User/Message/reply/index.vue');
const Claim = () => import('@/views/User/Message/claim/index.vue');
const Find = () => import('@/views/User/Message/find/index.vue');
const Owner = () => import('@/views/User/Message/owner/index.vue');
const Pass = () => import('@/views/User/Message/pass/index.vue');
const Back = () => import('@/views/User/Message/back/index.vue');
const Pay = () => import('@/views/User/Message/pay/index.vue');
const FoundWaterFall = () => import('@/views/User/WaterFall/found/index.vue');
const LostWaterFall = () => import('@/views/User/WaterFall/lost/index.vue');
const UserUpLoad = () => import('@/views/User/UpLoad/index.vue');
const FeedBack = () => import('@/views/User/FeedBack/index.vue');
const FoundDetail = () => import('@/components/user/FoundDetailForRoute.vue');
const LostDetail = () => import('@/components/user/LostDetailForRoute.vue');
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
                    children:[
                    ]
                },
                {
                    path: 'lost',
                    component: LostWaterFall,
                    meta: {
                        title: '寻物启事',
                    },
                },
                {
                    path: 'space',
                    component: Space,
                    meta: {
                        title: '个人空间',
                        requireAuth: true,
                        isUser: true,
                        isAdmin: false,
                    },
                    children:[
                        {
                            path: 'found',
                            component: MyFoundPost,
                            meta: {
                                title: '我的失物',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            }
                        },
                        {
                            path: 'lost',
                            component: MyLostPost,
                            meta: {
                                title: '我的寻物',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            }
                        },
                        {
                            path: 'waitfound',
                            component: waitPostFound,
                            meta: {
                                title: '待审核',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            },
                        },
                        {
                            path: 'waitlost',
                            component: waitPostLost,
                            meta: {
                                title: '待审核',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            },
                        },
                        {
                            path: 'backfound',
                            component: backPostFound,
                            meta: {
                                title: '帖子退回',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            },
                        },
                        {
                            path: 'backlost',
                            component: backPostLost,
                            meta: {
                                title: '帖子退回',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false, 
                            }
                        },
                        {
                            path: 'prize',
                            component: Prize,
                            meta:{
                                title: '奖品兑换',
                                requireAuth: true,
                                isUser: true,
                                isAdmin: false,
                            }
                        }
                    ]
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
                        title: '我的通知',
                        requireAuth: false,
                        isUser: true,
                    },
                    children:[
                        {
                            path: 'reply',
                            component: Reply,
                            meta: {
                                title: '回复我的',
                                requireAuth: false,
                                isUser: true,
                            }
                        },
                        {
                            path: 'claim',
                            component: Claim,
                            meta: {
                                title: '向我认领',
                                requireAuth: false,
                                isUser: true,
                            }
                        },
                        {
                            path: 'find',
                            component: Find,
                            meta: {
                                title: '我的失物',
                                requireAuth: false,
                                isUser: true,
                            },
                        },
                        {
                            path: 'owner',
                            component: Owner,
                            meta: {
                                title: '请求确认',
                                requireAuth: false,
                                isUser: true,
                            }
                        },
                        {
                            path: 'pass',
                            component: Pass,
                            meta: {
                                title: '审核通过',
                                requireAuth: false,
                                isUser: true,
                            }
                        },
                        {
                            path: 'back',
                            component: Back,
                            meta: {
                                title: '审核失败',
                                requireAuth: false,
                                isUser: true,
                            }
                        },
                        {
                            path: 'pay',
                            component: Pay,
                            meta: {
                                title: '打赏详情',
                                requireAuth: false,
                                isUser: true,
                            }
                        }
                    ]
                },
                {
                    path: 'feedback',
                    component: FeedBack,
                    meta: {
                        title: '意见反馈',
                        requireAuth: false,
                        isUser: true,
                    }
                },
                {
                    path: '/user/found/:id',
                    component: FoundDetail,
                    meta: {
                        title: '详情',
                    },
                },
                {
                    path: '/user/lost/:id',
                    component: LostDetail,
                    meta: {
                        title: '详情',
                    },
                },
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
        },
        { 
            path: '/test2',
            component: FoundWaterFall,
            meta: {
                title: '测试',
            }
        }
    ];