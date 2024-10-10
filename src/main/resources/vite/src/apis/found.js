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
    console.log("queryAdvises")
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

export const deletePosts = (ids) => {
    return http({
        url: 'http://localhost:8090/lost_found/deleteIds?ids='+ids,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}