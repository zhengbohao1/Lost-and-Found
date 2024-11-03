import http from "@/utils/http";

export const queryAdvises = () => { 
    return http({
        url: 'http://localhost:8090/advises/list',
        method: 'GET',
        data: {}
    })
}