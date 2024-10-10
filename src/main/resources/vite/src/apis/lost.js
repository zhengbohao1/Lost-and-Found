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

export const deletePosts = (ids) => {
    return http({
        url: 'http://localhost:8090/lost_found/deleteIds?ids='+ids,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}