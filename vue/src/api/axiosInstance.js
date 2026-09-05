import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 統一處理 API 回應
axiosInstance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('[API Error]:', error)
    return Promise.reject(error)
  }
)

export default axiosInstance