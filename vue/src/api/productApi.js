import axiosInstance from './axiosInstance'

// 取得全部商品
export function getProducts() {
  return axiosInstance.get('/product/list')
}

// 商品名稱搜尋
export function searchProducts(keyword) {
  return axiosInstance.get('/product/search', {
    params: {
      keyword: keyword
    }
  })
}

// 取得單一商品
export function getProductById(id) {
  return axiosInstance.get(`/product/${id}`)
}