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
