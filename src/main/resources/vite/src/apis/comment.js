import http from '@/utils/http'

export const getFirstComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getParentComments'
    })
}

export const getSecondComment = (id) => {
    return http({
        url: 'http://localhost:8090/user/getChildComments?'
    })
}