import http from "@/utils/http";

export const getPrizeList = () =>{
    return http({
        url: 'http://localhost:8090/prize/list',
        method: 'GET'
    })
}

// 用户之于奖品
export  const exchangePrize = (id) => {
    return http({
        url: 'http://localhost:8090/prize/exchange?id='+id,
        method: 'PUT'
    })
}

export const getCoin = () => {
    return http({
        url: 'http://localhost:8090/user/getGoldCoin',
        method: 'GET'
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

export const updatePrize = (id, num) => {
    console.log(id, num)
    return http({
        url: 'http://localhost:8090/prize/updateNum?id=' + id + '&num=' + num,
        method: 'PUT',
    })
}

export const deletePrize = (id) => {
    return http({
        url: 'http://localhost:8090/prize/delete?id='+id,
        method: 'DELETE',
        data: id
    })
}

export const allExchange = () => {
    return http({
        url: 'http://localhost:8090/prize/userPrizeList',
        method: 'GET'
    })
}

//发送奖励
export const sendCoin = (data) => {
    return http({
        url: 'http://localhost:8090/user/tipping',
        method: 'POST',
        params: data
    })
}

//获取奖励记录
export const getCoinRecord = (id) => {
    return http({
        url: 'http://localhost:8090/notice/getGoldMessages?userId=' + id,
        method: 'GET'
    })
}