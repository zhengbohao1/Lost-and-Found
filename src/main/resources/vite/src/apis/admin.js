import http from '@/utils/http'

export const adminLogin = ({email, password, checkCode}) => {
    return http({
        url: '/user/admin/login',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            email,
            password,
            checkCode
        }
    })
}