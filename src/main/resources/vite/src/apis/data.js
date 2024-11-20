import http from "@/utils/http";

//用户总数
export const userCount = () => {
    return http({
        url: "http://localhost:8090/user/getUserAmount",
        method: "GET"
    })
}

//失物招领
//今日完成
export const foundFinish = () => {
    return http({
        url: "http://localhost:8090/admin/get_todayLost_trade",
        method: "GET"
    })
} 

//全部完成
export const foundAll = () =>{
    return http({
        url: "http://localhost:8090/lost_found/getfoundcount",
        method: "GET"
    })
}

//全部
export const foundAllPost = () =>{
    return http({
        url: "http://localhost:8090/lost_found/getallcount",
        method: "GET"
    })
}

//今日新增
export const foundAdd = () =>{
    return http({
        url: "http://localhost:8090/admin/get_todayLost_post",
        method: "GET"
    })
}

//寻物启事
//今日完成
export const lostFinish = () => {
    return http({
        url: "http://localhost:8090/admin/get_todayMissing_trade",
        method: "GET"
    })
}

//全部完成
export const lostAll = () =>{
    return http({
        url: "http://localhost:8090/missing_notices/getfoundcount",
        method: "GET"
    })
}

//全部
export const lostAllPost = () =>{
    return http({
        url: "http://localhost:8090/missing_notices/getallcount",
        method: "GET"
    })
}

//今日新增
export const lostAdd = () =>{
    return http({
        url: "http://localhost:8090/admin/get_todayMissing_post",
        method: "GET"
    })
}

//完成
export const foundCompetedByDay = () =>{
    return http({
        url: "http://localhost:8090/admin/getLostTradeVolumn",
        method: "GET"
    })
}

export const lostCompetedByDay = () =>{
    return http({
        url: "http://localhost:8090/admin/getMissingTradeVolumn",
        method: "GET"
    })
}