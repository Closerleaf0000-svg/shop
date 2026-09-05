<script setup>

import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../../stores/cart'

const router = useRouter() // 取得路由
const cartStore = useCartStore() // 取得購物車

onMounted(() => {
  cartStore.fetchCart()
})

// 購物車商品
const cartItems = computed(() => {
  return cartStore.items || []
})

// 購物車總金額
const totalPrice = computed(() => {
  return cartStore.totalPrice
})

// 增加商品數量
function increaseQuantity(productId) {
  cartStore.increaseQuantity(productId)
}

// 減少商品數量
function decreaseQuantity(productId) {
  cartStore.decreaseQuantity(productId)
}

// 移除商品
function removeItem(productId) {
  cartStore.removeItem(productId)
}

// 回到商品列表
function goToProducts() {
  router.push('/products')
}

// 前往訂單確認頁
function goToOrder() {
  router.push('/order')
}

</script>


<template>

  <div class="cart-page">

    <!-- 購物車標題 -->
    <h1>
      我的購物車
    </h1>

    <!-- 購物車是空的 -->
    <div
      v-if="cartItems.length === 0"
      class="empty-cart"
    >

      <p>
        目前購物車沒有商品
      </p>

      <button
        @click="goToProducts"
      >
        前往商品頁
      </button>

    </div>


    <!-- 購物車有商品 -->
    <div
      v-else
      class="cart-content"
    >

      <!-- 商品列表 -->
      <div class="cart-list">

        <div
          v-for="item in cartItems"
          :key="item.productId"
          class="cart-item"
        >

          <!-- 商品資訊 -->
          <div class="product-info">

            <!-- 商品名稱 -->
            <h2>
              {{ item.productName }}
            </h2>

            <!-- 商品單價 -->
            <p>
              單價：
              ${{ item.price }}
            </p>

            <!-- 商品庫存 -->
            <p>
              庫存：
              {{ item.stock  }}
            </p>

            <!-- 數量控制 -->
            <div class="quantity-area">

              <span>
                數量：
              </span>

              <button
                @click="decreaseQuantity(item.productId)"
                :disabled="item.quantity <= 1"
              >
                -
              </button>

              <span class="quantity">
                {{ item.quantity }}
              </span>

              <button
                @click="increaseQuantity(item.productId)"
              >
                +
              </button>

            </div>

            <!-- 商品小計 -->
            <p class="subtotal">
              小計：
              ${{ item.price * item.quantity }}
            </p>

            <!-- 移除商品 -->
            <button
              class="remove-button"
              @click="removeItem(item.productId)"
            >
              移除所有此商品
            </button>

          </div>

        </div>

      </div>


      <!-- 購物車總結 -->
      <div class="cart-summary">

        <h2>
          購物車總計
        </h2>

        <!-- 商品總數 -->
        <p>
          商品總數：
          {{ cartStore.totalQuantity }}
          件
        </p>

        <!-- 總金額 -->
        <p class="total-price">
          總金額：
          ${{ totalPrice }}
        </p>

        <!-- 繼續購物 -->
        <button
          class="continue-button"
          @click="goToProducts"
        >
          繼續購物
        </button>

        <!-- 結帳 -->
        <button
          class="checkout-button"
          @click="goToOrder"
        >
          前往結帳
        </button>

      </div>

    </div>

  </div>

</template>


<style scoped>

/* 購物車頁面 */
.cart-page {
  max-width: 1000px;
  margin: 40px auto;
  padding: 20px;
}

/* 購物車頁面的主標題 */
.cart-page h1 {
  font-size: 48px;
  margin-bottom: 36px;
  font-weight: bold;
}

/* 購物車是空的 */
.empty-cart {
  text-align: center;
  padding: 60px 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
}

.empty-cart p {
  margin-bottom: 20px;
}

/* 商品列表 */
.cart-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 單一商品 */
.cart-item {
  display: flex;
  gap: 25px;
  padding: 20px;
  border: 1px solid #f8f8f8;
  border-radius: 10px;
  background-color: rgb(40, 37, 37);
}

/* 商品資訊 */
.product-info {
  flex: 1;
}

.product-info h2 {
  margin-top: 0;
}

/* 數量 */
.quantity-area {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 15px 0;
}

.quantity-area button {
  width: 30px;
  height: 30px;
}

.quantity {
  min-width: 30px;
  text-align: center;
}

/* 小計 */
.subtotal {
  font-size: 18px;
  font-weight: bold;
}

/* 移除 */
.remove-button {
  margin-top: 10px;
}

/* 購物車總計 */
.cart-summary {
  margin-top: 30px;
  padding: 25px;
  border: 1px solid #ddd;
  border-radius: 10px;
  text-align: center;
}

.cart-summary h2 {
  font-weight: bold;
  font-size: 28px;
}

.total-price {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 18px;
}

.cart-summary button {
  margin-left: 10px;
  padding: 10px 20px;
  cursor: pointer;
}

</style>