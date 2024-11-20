import http from '@/utils/http'
import { useUserStore } from '@/stores/user'

// 显示所用用户信息
export const getUserList = () => {
    const userStore = useUserStore();
    return http({
        url: '/admin/userInfo/all',
        method: 'GET',
        headers: {
            'Authorization': userStore.userToken,
        },
        data: {}
    })
}

//禁用用户
export const updateUser = (id) => {
    return http({
        url: '/admin/disable?userId=' + id,
        method: 'GET',
        data: {}
    })
}