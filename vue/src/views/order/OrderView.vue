<script setup>

import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../../stores/cart'
import { useAuthStore } from '../../stores/auth'
import axiosInstance from '../../api/axiosInstance'

const router = useRouter() // 取得目前網址的路由資訊
const cartStore = useCartStore() // 取得購物車 Store
const authStore = useAuthStore() // 取得會員登入 Store
const paymentMethod = ref('COD') // 付款方式
const address = ref('') // 地址
const cardNumber = ref('') // 信用卡號碼
const orderId = ref('') // 訂單 ID
const isLoading = ref(false) // 狀態
const errorMessage = ref('') // 狀態
const successMessage = ref('') // 狀態

// 購物車商品
const cartItems = computed(() => {
  return cartStore.items || []
})

// 商品總數量
const totalQuantity = computed(() => {
  return cartStore.totalQuantity
})

// 訂單總金額
const totalPrice = computed(() => {
  return cartStore.totalPrice
})


// 載入購物車
onMounted(async () => {

  await authStore.checkLogin()

  if (!authStore.isLoggedIn) {
    router.push('/login')
    return
  }

  // RULER 不可以建立訂單
  if (authStore.role === 'RULER') {
    router.push('/products')
    return
  }

  await cartStore.fetchCart()

  if (cartStore.items.length === 0) {
    errorMessage.value =
      '購物車目前沒有商品'
  }
})

// 回到購物車
function goToCart() {
  router.push('/cart')
}

// 產生訂單 ID
function generateOrderId() {

  const now = new Date()

  const year =
    now.getFullYear()

  const month =
    String(now.getMonth() + 1)
      .padStart(2, '0')

  const day =
    String(now.getDate())
      .padStart(2, '0')

  const hours =
    String(now.getHours())
      .padStart(2, '0')

  const minutes =
    String(now.getMinutes())
      .padStart(2, '0')

  const seconds =
    String(now.getSeconds())
      .padStart(2, '0')

  return `ORD${year}${month}${day}${hours}${minutes}${seconds}`
}

// 確認訂單
async function confirmOrder() {

  errorMessage.value = ''
  successMessage.value = ''

  // 檢查登入
  if (!authStore.isLoggedIn) {

    errorMessage.value =
      '請先登入'

    return
  }

  // RULER 不可以建立訂單
  if (authStore.role === 'RULER') {

    errorMessage.value =
      '管理者不能建立訂單'

    return
  }

  // 檢查購物車
  if (cartItems.value.length === 0) {

    errorMessage.value =
      '購物車目前沒有商品'

    return
  }

  // 貨到付款 → 檢查地址
  if (paymentMethod.value === 'COD') {

    if (address.value.trim() === '') {

      errorMessage.value =
        '請填寫收貨地址'

      return
    }
  }

  // 線上刷卡 → 檢查卡號
  if (paymentMethod.value === 'CARD') {

    if (cardNumber.value.trim() === '') {

      errorMessage.value =
        '請填寫信用卡號碼'

      return
    }

    // 只允許數字
    if (!/^\d{16}$/.test(cardNumber.value)) {

      errorMessage.value =
        '信用卡號碼必須為16位數字'

      return
    }
  }


  try {

    isLoading.value = true

    // 產生訂單 ID
    orderId.value =
      generateOrderId()

    // 建立訂單明細
    const orderDetails =
      cartItems.value.map(item => ({

        productId:
          item.productId,

        quantity:
          item.quantity

      }))


    // 建立訂單
    const response =
      await axiosInstance.post(
        '/order',
        {
          orderId:
            orderId.value,

          // 目前先使用：
          // 0 = 未付款
          payStatus: 0,

          orderDetails:
            orderDetails
        }
      )


    console.log(
      '建立訂單結果：',
      response
    )


    if (response.success) {

      successMessage.value =
        response.message

      // 清空購物車
      cartStore.clearCart()

      // 使用後端回傳的訂單 ID
      orderId.value =
        response.orderId

    } else {

      errorMessage.value =
        response.message
    }

  } catch (error) {

    console.error(
      '建立訂單失敗：',
      error
    )

    console.error(
      'HTTP 狀態：',
      error.response?.status
    )

    console.error(
      '後端回應：',
      error.response?.data
    )

    if (
      error.response?.data?.message
    ) {

      errorMessage.value =
        error.response.data.message

    } else {

      errorMessage.value =
        '訂單建立失敗，請稍後再試'
    }

  } finally {

    isLoading.value = false
  }
}

</script>


<template>

  <div class="order-page">

    <!-- 頁面標題 -->
    <h1>
      訂單確認
    </h1>


    <!-- 錯誤訊息 -->
    <p
      v-if="errorMessage"
      class="error-message"
    >
      {{ errorMessage }}
    </p>


    <!-- 成功訊息 -->
    <p
      v-if="successMessage"
      class="success-message"
    >
      {{ successMessage }}
    </p>


    <!-- 會員資訊 -->
    <section class="order-section">

      <h2>
        會員資訊
      </h2>

      <p>
        會員名稱：
        {{ authStore.username }}
      </p>

    </section>


    <!-- 商品資訊 -->
    <section class="order-section">

      <h2>
        商品資訊
      </h2>

      <div
        v-for="item in cartItems"
        :key="item.productId"
        class="order-item"
      >

        <div class="product-info">

          <h3>
            {{ item.productName }}
          </h3>

          <p>
            單價：
            ${{ item.price }}
          </p>

          <p>
            購買數量：
            {{ item.quantity }}
          </p>

          <p class="subtotal">

            小計：
            ${{ item.price * item.quantity }}

          </p>
        </div>
      </div>
    </section>


    <!-- 訂單資訊 -->
    <section class="order-section">

      <h2>
        訂單資訊
      </h2>

      <p>
        商品總數：
        {{ totalQuantity }}
        件
      </p>

      <p class="total-price">

        訂單總金額：
        ${{ totalPrice }}

      </p>

    </section>


    <!-- 付款方式 -->
    <section class="order-section">

      <h2>
        付款方式
      </h2>

      <div class="payment-option">

        <!-- 貨到付款 -->
        <label>

          <input
            type="radio"
            value="COD"
            v-model="paymentMethod"
          />

          貨到付款

        </label>


        <!-- 線上刷卡 -->
        <label>

          <input
            type="radio"
            value="CARD"
            v-model="paymentMethod"
          />

          線上刷卡

        </label>
      </div>


      <!-- 貨到付款地址 -->
      <div
        v-if="paymentMethod === 'COD'"
        class="payment-input"
      >

        <label for="address">
          收貨地址
        </label>

        <input
          id="address"
          type="text"
          v-model="address"
          placeholder="請填寫收貨地址"
        />

      </div>


      <!-- 線上刷卡卡號 -->
      <div
        v-if="paymentMethod === 'CARD'"
        class="payment-input"
      >

        <label for="cardNumber">
          信用卡號碼
        </label>

        <input
          id="cardNumber"
          type="text"
          v-model="cardNumber"
          placeholder="請輸入16位數信用卡號碼"
          maxlength="16"
          inputmode="numeric"
        />

      </div>
    </section>


    <!-- 訂單 ID -->
    <section class="order-section">

      <h2>
        訂單 ID
      </h2>

      <p>
        {{ orderId || '尚未建立' }}
      </p>

    </section>


    <!-- 按鈕 -->
    <div class="order-actions">

      <button
        class="back-button"
        @click="goToCart"
      >
        回購物車
      </button>


      <button
        class="confirm-button"
        @click="confirmOrder"
        :disabled="
          isLoading ||
          cartItems.length === 0 ||
          orderId !== ''
        "
      >

        {{
          isLoading
            ? '建立訂單中...'
            : orderId
              ? '訂單已建立'
              : '確認訂單'
        }}

      </button>
    </div>
  </div>
</template>


<style scoped>

.order-page {
  max-width: 1000px;
  margin: 40px auto;
  padding: 20px;
}

.order-page h1 {
  margin-bottom: 30px;
  font-size: 32px;
  font-weight: bold;
  text-align: center;
}


/* 訂單區塊 */
.order-section {
  margin-bottom: 25px;
  padding: 25px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background-color: rgb(40, 37, 37);
}

.order-section h2 {
  margin-bottom: 20px;
  font-size: 22px;
}


/* 商品 */
.order-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.product-info {
  flex: 1;
}

.product-info h3 {
  margin-top: 0;
}


/* 小計 */
.subtotal {
  font-weight: bold;
}


/* 總金額 */
.total-price {
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: bold;
}


/* 付款方式 */
.payment-option {
  display: flex;
  justify-content: center;
  gap: 30px;
}

.payment-option label {
  cursor: pointer;
}


/* 地址 / 信用卡輸入 */
.payment-input {
  margin-top: 25px;
}

.payment-input label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

.payment-input input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}


/* 錯誤 / 成功訊息 */
.error-message {
  margin-bottom: 20px;
  text-align: center;
}

.success-message {
  margin-bottom: 20px;
  text-align: center;
}


/* 按鈕 */
.order-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.order-actions button {
  padding: 12px 25px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.order-actions button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

</style>

