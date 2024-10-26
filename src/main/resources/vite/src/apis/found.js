import http from "@/utils/http";

//删除帖子
export const deletePost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/deleteSingleid?id='+id,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}

// 全部帖子
export const queryAllPost = () => {
    return http({
        url: 'http://localhost:8090/lost_found/list',
        method: 'GET',
    })
}

export const queryPost = () => {
    return http({
        url: 'http://localhost:8090/lost_found/legal_list',
        method: 'GET',
    })
}

export const addFoundPost = (data) => {
    return http({
        url: 'http://localhost:8090/lost_found',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: data
    })
}

export const passPost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/approve_by_id?id='+id,
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
    })  
}

export const backPost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/reject_by_id?id='+id,
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getPostById = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/getbyid?id='+id,
        method: 'GET',
    })
}

export const deletePosts = (ids) => {
    return http({
        url: 'http://localhost:8090/lost_found/deleteIds?ids='+ids,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}

//获取评论
export const getComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getLostParentComments?postId='+id,
        method: 'GET',
    })  
}

export const getChildComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getLostChildComments?parentId='+id,
        method: 'GET',
    })
}

//发评论
export const postComment = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendLostParentComments',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const postChildComment = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendLostChildComments',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

//认领
export const claim = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendClaimInfo',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 个人帖子获得
export const getPersonalPassPost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/getLegalPostByUserId?userId='+id,
        method: 'GET',
    })
}

export const getPersonalWaitPost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/getWaitByUserId?userId='+id,
        method: 'GET',
    })
}

export const getPersonalBackPost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/getIllegalByUserId?userId='+id,
        method: 'GET',
    })
}