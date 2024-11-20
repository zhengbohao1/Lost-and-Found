import http from "@/utils/http";
import { useUserStore } from "@/stores/user";

// 登录
export const login = ({email, password, checkCode}) => {
    return http({
        url: '/user/login',
        method: 'POST',
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

// 注册
export const Register = ({email, nickName, password, emailCode, checkCode}) => {
    return http({
        url: 'http://localhost:8090/user/register',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            email,
            nickName,
            password,
            emailCode,
            checkCode
        }
    })
}

//发送验证码
export const sendCode = async ({email, type}) => {
    return http({
        url: 'http://localhost:8090/user/sendEmailCode',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            email,
            type
        }
    })
}

// 获取用户详细信息
export const queryUserInfo = () => {
    const userStore = useUserStore();
    return http({
        url: 'http://localhost:8090/user/userInfo',
        method: 'GET',
        headers: {
            'Authorization': userStore.userToken,
        },
        data: {}
    })
}

