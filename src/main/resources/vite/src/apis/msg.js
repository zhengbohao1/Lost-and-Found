import http from '@/utils/http'

export const markClaimAsRead = ( id ) => {
    return http({
        url: 'http://localhost:8090/notice/markClaimAsRead?id=' + id,
        method: 'PUT',
    })
}

export const markCommentAsRead = ( id ) => {
    return http({
        url: 'http://localhost:8090/notice/markMessageAsRead?id=' + id,
        method: 'PUT',
    })
}

export const markFindTipsAsRead = ( id ) => {
    return http({
        url: 'http://localhost:8090/notice/markFindTipsAsRead?id='+ id,
        method: 'PUT',
    })
}