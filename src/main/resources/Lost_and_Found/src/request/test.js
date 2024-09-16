//在这里写对应部分的后端请求和返回数据处理
import { getRequest, postRequest, putRequest, deleteRequest } from "./api";

export const ptest = ( user ) => {
    return postRequest("/user/", user );
}

export const test = () => {
    return getRequest("/user/", {"": ""})
}

export const utest = ( id, user ) => {
    return putRequest("/user/"+id, user );
}

export const dtest = ( id ) => {
    return deleteRequest("/user/"+id, {"":""} );
}

export const gtest = ( id ) => {
    return getRequest("/user/"+id, {"":""} );
}