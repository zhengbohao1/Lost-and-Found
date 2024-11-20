import http from '@/utils/http'
import { useUserStore } from '@/stores/user'

// 显示所用用户信息
export const getUserList = () => {
    const userStore = useUserStore();
    return http({
        url: 'http://localhost:8090/admin/userInfo/all',
        method: 'GET',
        data: {}
    })
}

//禁用用户
export const updateUser = (id) => {
    return http({
        url: 'http://localhost:8090/admin/disable?userId=' + id,
        method: 'GET',
        data: {}
    })
}

//获得个人头像
export const getUserAvatar = () => {
    return http({
        url: 'http://localhost:8090/user/getAvatar',
        method: 'GET',
    })
}

//获得用户名
export const getUserName = (id) => {
    return http({
        url: 'http://localhost:8090/user/getNickNameByid?userId='+id,
        method: 'GET',
    })
}

//重置密码
export const resetPsw = ({email, password, emailCode}) => {
    return http({
        url: 'http://localhost:8090/user/resetPwd',
        method: 'POST',
        data: {
            email,
            password,
            emailCode
        }
    })
}

// 修改密码
export const changePsw = ({oldPassword, newPassword}) => {
    return http({
        url: 'http://localhost:8090/user/updatePwd',
        method: 'POST',
        data: {
            oldPassword,
            newPassword,
            newPassword
        }
    })
}

//换头像
export const changeAvatar = (formData) => {
  return http({
    url: 'http://localhost:8090/user/updateAvatar',
    method: 'POST',
    data: formData, // 使用 FormData 作为请求体
    headers: {
      'Content-Type': 'multipart/form-data' // 设置请求头
    }
  });
}

//修改密码
export const updatePassword = (data) => {
    return http({
        url: 'http://localhost:8090/user/updatePwd',
        method: 'POST',
        data: data
    })
}

//消息
export const countMessage = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getUnreadCount?userId='+id,
        method: 'GET',
    })
}

export const replyToMe = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getAllReplyMessages?userId='+id,
        method: 'GET',
    })
}

export const claimToMe = () => {
    return http({
        url: 'http://localhost:8090/notice/getClaimMessage',
        method: 'GET',
    })
}

export const foundMine = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getFindTips?ownerId='+id,
        method: 'GET',
    })
}

export const postPass = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getApprovedMessages?userId='+id,
        method: 'GET',
    })
}

export const postBack = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getRejectedMessages?userId='+id,
        method: 'GET',
    })
}