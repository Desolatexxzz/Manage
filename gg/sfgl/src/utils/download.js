import axios from 'axios'

const service = axios.create({
    responseType: 'arraybuffer'
})
//请求拦截器
service.interceptors.request.use(config => {
    config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr')
    return config
},error=>{
    console.log(error);
})

//响应拦截器

service.interceptors.response.use(resp => {
    const headers = resp.headers
    let reg = RegExp(/application\/json/)
    if (headers['content-type'].match(reg)) {
        resp.data = unitToString(resp.data)
    } else {
        let fileDownLoad = require('js-file-download')
        let name = headers['content-disposition'].split(';')[1]
        let fileName = name.split('filename=')[1]
        let contentType = headers['content-type']
        fileName = decodeURIComponent(fileName)
        fileDownLoad(resp.data, fileName, contentType)
    }
}, error => {
    console.log(error);
})


function unitToString(unitArray) {
    //编码
    let encodedString = String.fromCharCode.apply(null, new Uint8Array(unitArray))
    //解码
    let decodedString = decodeURIComponent(escape(encodedString))
    return JSON.parse(decodedString)
}


let base = ''
export const downloadRequest = (url, params) => {
    return service({
        method: 'GET',
        url: `${base}${url}`,
        data: params
    })
}

export default service;