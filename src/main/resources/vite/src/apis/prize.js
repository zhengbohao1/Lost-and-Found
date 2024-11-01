import http from "@/utils/http";

// 用户之于奖品
export const getPrizeList = () =>{
    return http({
        url: 'http://localhost:8090/prize/list',
        method: 'GET'
    })
}

export  const exchangePrize = (id) => {
    return http({
        url: 'http://localhost:8090/prize/exchange?id='+id,
        method: 'PUT'
    })
}

//管理员操作
export const addPrize = (data) => {
    return http({
        url: 'http://localhost:8090/prize/add',
        method: 'POST',
        data: data
    })
}

export const updatePrize = (data) => {
    return http({
        url: 'http://localhost:8090/prize/updateNum',
        method: 'PUT',
        data: data
    })
}

export const deletePrize = (id) => {
    return http({
        url: 'http://localhost:8090/prize/delete?id='+id,
        method: 'DELETE',
        data: id
    })
}

export const getCoin = () => {
    return http({
        url: 'http://localhost:8090/user/getGoldCoin',
        method: 'GET'
    })
}

//发送奖励
export const sendCoin = (data) => {
    return http({
        url: 'http://localhost:8090/user/tipping',
        method: 'POST',
        data: data
    })
}