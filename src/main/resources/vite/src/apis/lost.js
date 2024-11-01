import http from '@/utils/http'

//删除帖子
export const deletePost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/deleteSingleid?id='+id,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })  
}

// 全部帖子
export const queryAllPost = () => {
    return http({
        url: 'http://localhost:8090/missing_notices/list',
        method: 'GET',
    })
}

// 帖子
export const queryPost = () => {
    return http({
        url: 'http://localhost:8090/missing_notices/legal_list',
        method: 'GET',
        header: {
            'Content-Type': 'application/json'
        },
    })
}

export const addLostPost = (data) => {
    return http({
        url: 'http://localhost:8090/missing_notices',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data: data
    })
}

export const passPost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/approve_by_id?id='+id,
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
    })  
}

export const backPost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/reject_by_id?id='+id,
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getPostById = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/getbyid?id='+id,
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

//发评论
export const postComment = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendMissingParentComments',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const postChildComment = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendMissingChildComments',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

//获取评论
export const getComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getMissingParentComments?postId='+id,
        method: 'GET',
    })  
}

export const getChildComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getMissingChildComments?parentId='+id,
        method: 'GET',
    })
}

//查询
export const search = (data) => {
    return http({
        url: 'http://localhost:8090/missing_notices/query?content=' + data,
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


//交还
export const claim = (data) => {
    return http({
        url: 'http://localhost:8090/user/sendFindTips',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

//个人帖子
export const getPersonalPassPost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/getLegalPostByUserId?userId='+id,
        method: 'GET',
    })
}

export const getPersonalWaitPost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/getWaitByUserId?userId='+id,
        method: 'GET',
    })
}

export const getPersonalBackPost = (id) => {
    return http({
        url: 'http://localhost:8090/missing_notices/getIllegalByUserId?userId='+id,
        method: 'GET',
    })
}

// 修改帖子
export const updatePost = (data) => {
    return http({
        url: 'http://localhost:8090/missing_notices/update',
        method: 'PUT',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

//确认找到失主
export const confirmOwner = (id, post) => {
    return http({
        url: `http://localhost:8090/user/confirmFindTips?user_id=${encodeURIComponent(id)}&post_id=${encodeURIComponent(post)}`,
        method: 'PUT',
        data: post,
    })
}