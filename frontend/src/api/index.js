import axios from 'axios'

// ==================== 配置区：修改这里的后端地址 ====================
const BASE_URL = 'http://localhost:8080/api'

// 创建 Axios 实例
const request = axios.create({
  baseURL: BASE_URL,
  timeout: 60000 // 请求超时时间30秒
})

// 请求拦截器（可以在这里加 Token）
request.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器（统一处理返回结果）
request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('请求失败', error)
    return Promise.reject(error)
  }
)

// ==================== 定义后端接口 ====================
// 1. 上传文档
export const uploadDoc = (formData) => {
  return request({
    url: '/document/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 2. AI 问答
export const askAI = (data) => {
  return request({
    url: '/api/ai/ask',
    method: 'post',
    data: data
  })
}

// 3. 获取文档列表（后续你补全后端接口后用）
export const getDocList = () => {
  return request({
    url: '/document/list',
    method: 'get'
  })
}
// 清空所有文档记录
export const clearAllDocuments = () => {
  return request({
    url: '/document/clear',
    method: 'delete'
  })
}