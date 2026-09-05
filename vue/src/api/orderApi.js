import axiosInstance from './axiosInstance'

// 建立訂單
export function createOrder(orderData) {
  return axiosInstance.post('/order', orderData)
}

// 查詢單筆訂單
export function getOrder(orderId) {
  return axiosInstance.get(`/order/${orderId}`)
}