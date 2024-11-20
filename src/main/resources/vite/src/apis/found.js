import http from "@/utils/http";

//删除帖子
export const deletePost = (id) => {
    return http({
        url: 'http://localhost:8090/lost_found/deleteSingleid?id='+id,
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {}
    })
}

// 全部帖子
export const queryAllPost = () => {
    return http({
        url: 'http://localhost:8090/lost_found/list',
        method: 'GET',
        data: {}
    })
}

export const queryPost = () => {
    console.log("queryAdvises")
    return http({
        url: 'http://localhost:8090/lost_found/legal_list',
        method: 'GET',
        data: {}
    })
}