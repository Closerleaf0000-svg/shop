import { defineStore } from 'pinia'
import axiosInstance from '../api/axiosInstance'

export const useCartStore = defineStore('cart', {

  // 購物車資料
  state: () => ({
  items: []
}),

  getters: {

    // 購物車商品總數量
    totalQuantity: (state) => {
      return state.items.reduce(
        (total, item) =>
          total + item.quantity,
        0
      )
    },

    // 購物車總金額
    totalPrice: (state) => {
      return state.items.reduce(
        (total, item) =>
          total + item.price * item.quantity,
        0
      )
    }
  },

  actions: {

    // 查詢目前登入會員的購物車
    async fetchCart() {

      try {

      const response =
        await axiosInstance.get('/cart')

      console.table(
          response.map(item => ({
            cartId: item.cartId,
            productId: item.productId,
            productName: item.productName,
            price: item.price,
            quantity: item.quantity,
            stock: item.stock
          }))
        )

      this.items = response

    } catch (error) {

      console.error(
        '取得購物車失敗：',
        error
      )

      this.items = []
    }
  },

    // 加入購物車
    async addToCart(product, quantity) {

      try {

        await axiosInstance.post('/cart', {

          productId:product.productId,
          quantity:quantity

        })

        // 加入成功後重新取得購物車
        await this.fetchCart()

      } catch (error) {

        console.error(
          '加入購物車失敗：',
          error
        )
      }
    },

    // 修改商品數量
    async updateQuantity(productId,quantity) {

      try {

        await axiosInstance.put(
          '/cart',
          null,
          {
            params: { productId:productId,
                quantity:quantity}
          }
        )

        // 更新成功後重新取得購物車
        await this.fetchCart()

      } catch (error) {

        console.error(
          '修改購物車數量失敗：',
          error
        )
      }
    },

    // 增加商品數量
    async increaseQuantity(productId) {

      const item =
        this.items.find(
          item =>
            item.productId === productId
        )

      if (!item) {
        return
      }
      
      // 不可超過商品庫存
      if (
        item.quantity >= item.stock
      ) {
        return
      }

      await this.updateQuantity(
        productId,
        item.quantity + 1
      )
    },

    // 減少商品數量
    async decreaseQuantity(productId) {

      const item =
        this.items.find(
          item =>
            item.productId === productId
        )

      if (!item) {
        return
      }

      // 最少只能有 1 個
      if (item.quantity <= 1) {
        return
      }

      await this.updateQuantity(
        productId,
        item.quantity - 1
      )
    },

    // 移除購物車商品
    async removeItem(productId) {

      try {

        await axiosInstance.delete(
          `/cart/${productId}`
        )

        // 移除成功後重新取得購物車
        await this.fetchCart()

      } catch (error) {

        console.error(
          '移除購物車商品失敗：',
          error
        )
      }
    },

    // 建立訂單
    async createOrder() {

      try {

        // 沒有商品不能建立訂單
        if (this.items.length === 0) {

          return {
            success: false,
            message: '購物車是空的'
          }
        }

        // 建立訂單編號
        const orderId = 'ORD' + Date.now()

        // 組成後端 OrderRequest
        const orderData = {

          orderId: orderId,

          // 0 = 未付款
          payStatus: 0,

          orderDetails:
            this.items.map(item => ({

              productId: item.productId,
              quantity: item.quantity
            }))

        }

        console.log(
          '送出的訂單資料：',
          orderData
        )

        // 呼叫後端 API
        const response =
          await axiosInstance.post(
            '/order',
            orderData
          )

        console.log(
          '建立訂單結果：',
          response
        )

        // 建立成功
        if (response.success) {

          // 清空前端購物車
          this.items = []

          return response
        }

        return response

      } catch (error) {

        console.error(
          '建立訂單失敗：',
          error
        )

        return {
          success: false,
          message: '建立訂單失敗'
        }
      }
    },

    // 清空購物車
    clearCart() {
      this.items = []
    }
  }
})