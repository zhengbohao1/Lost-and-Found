import http from "@/utils/http";

export const queryAdvises = () => { 
    return http({
        url: '/advises/list',
        method: 'GET',
        data: {}
    })
}