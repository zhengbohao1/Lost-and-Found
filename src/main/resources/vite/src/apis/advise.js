import http from "@/utils/http";

export const queryAdvises = () => { 
    return http({
        url: 'http://localhost:8090/advises/list',
        method: 'GET',
        data: {}
    })
}

export const deleteAdvise = (id) => {
    return http({
        url: 'http://localhost:8090/advises/deleteSingleid?id=' + id,
        method: 'DELETE',
    })
}

export const deleteAdvises = (advises) => {
    console.log(advises)
    return http({
        url: 'http://localhost:8090/advises/deleteIds?ids=' + advises,
        method: 'Delete',
    })
}

export const addAdvise = (data) => {
    return http({
        url: 'http://localhost:8090/advises/insert',
        method: 'POST',
        data: data
    })
}