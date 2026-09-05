<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { getProductById } from '../../api/productApi'
import { useCartStore } from '../../stores/cart'


// 基本設定

const route = useRoute() // 取得目前網址的路由資訊
const product = ref(null) // 商品資料
const isLoading = ref(true) // 商品是否正在載入
const errorMessage = ref('') // 錯誤訊息
const showCartDialog = ref(false) // 控制加入購物車視窗
const quantity = ref(1) // 加入購物車數量
const showLoginDialog = ref(false) // 控制未登入提示視窗
const authStore = useAuthStore() // 會員 Store
const cartStore = useCartStore() // 購物車 Store


// 取得商品資料
async function fetchProduct() {

  try {

    isLoading.value = true
    errorMessage.value = ''

    const response =
      await getProductById(route.params.id)

    product.value = response

  } catch (error) {

    console.error(
      '取得商品失敗：',
      error
    )

    errorMessage.value =
      '商品資料取得失敗'

  } finally {

    isLoading.value = false

  }

}



// 頁面載入
onMounted(() => {

  fetchProduct()

  authStore.checkLogin()

})


// 開啟加入購物車視窗
function openCartDialog() {

  // 每次開啟都從 1 開始
  quantity.value = 1

  showCartDialog.value = true

}


// 關閉加入購物車視窗
function closeCartDialog() {

  showCartDialog.value = false

}


// 關閉未登入視窗
function closeLoginDialog() {

  showLoginDialog.value = false

}

// 減少商品數量
function decreaseQuantity() {

  if (quantity.value > 1) {

    quantity.value--

  }

}

// 增加商品數量
function increaseQuantity() {

  if (
    product.value &&
    quantity.value < product.value.quantity
  ) {

    quantity.value++

  }

}


// 點擊「加入購物車」
function handleAddToCart() {

  // 沒有登入
  if (!authStore.isLoggedIn) {

    showLoginDialog.value = true

    return

  }

  openCartDialog()

}

// 實際加入購物車
async function addToCart() {

  await cartStore.addToCart(
    product.value,
    quantity.value
  )

  // 加入完成後關閉視窗
  showCartDialog.value = false

}

// 計算總價格
const totalPrice = computed(() => {

  if (!product.value) {

    return 0

  }

  const price =
    Number(product.value.price)

  return price * quantity.value

})

</script>


<template>

  <div class="product-detail">


    <!-- 載入中 -->
    <div
      v-if="isLoading"
      class="status"
    >

      商品資料載入中...

    </div>


    <!-- 發生錯誤 -->
    <div
      v-else-if="errorMessage"
      class="status error"
    >

      {{ errorMessage }}

    </div>


    <!-- 商品內容 -->
    <div
      v-else-if="product"
      class="product-container"
    >


      <!-- 商品資訊 -->

      <div class="product-info">

        <!-- 商品名稱 -->

        <h1>
          {{ product.productName }}
        </h1>


        <!-- 商品價格 -->

        <p class="price">

          ${{ product.price }}

        </p>


        <!-- 商品庫存 -->

        <p class="stock">

          庫存：
          {{ product.quantity }}

        </p>


        <!-- 加入購物車 -->

        <button
          v-if="authStore.role !== 'RULER'"
          class="cart-button"
          @click="handleAddToCart"
          :disabled="product.quantity <= 0"
        >   

          加入購物車

        </button>

      </div>

    </div>


    <!-- 未登入視窗 -->
    <div
      v-if="showLoginDialog"
      class="cart-overlay"
    >

      <div class="cart-dialog">

        <h2>
          未登入
        </h2>

        <p>
          請先登入會員才能加入購物車
        </p>


        <div class="dialog-actions">

          <!-- 前往登入 -->

          <router-link
            to="/login"
            class="login-link"
          >

            立即登入

          </router-link>


          <!-- 取消 -->

          <button
            class="cancel-button"
            @click="closeLoginDialog"
          >

            取消

          </button>
        </div>
      </div>
    </div>


    <!-- 加入購物車視窗 -->
    <div
      v-if="showCartDialog"
      class="cart-overlay"
    >

      <div class="cart-dialog">

        <h2>
          加入購物車
        </h2>


        <!-- 商品名稱 -->

        <p>

          商品名稱：
          {{ product.productName }}

        </p>


        <!-- 商品價格 -->

        <p>

          單價：
          ${{ product.price }}

        </p>


        <!-- 商品庫存 -->

        <p>

          庫存：
          {{ product.quantity }}

        </p>


        <!-- 加入數量 -->

        <div class="quantity-area">

          <span>
            加入數量：
          </span>


          <!-- 減少 -->

          <button
            @click="decreaseQuantity"
            :disabled="quantity <= 1"
          >

            -

          </button>


          <!-- 目前數量 -->

          <span class="quantity">

            {{ quantity }}

          </span>


          <!-- 增加 -->

          <button
            @click="increaseQuantity"
            :disabled="quantity >= product.quantity"
          >

            +

          </button>

        </div>


        <!-- 總價格 -->

        <p class="total-price">

          總共價格：

          ${{ totalPrice }}

        </p>


        <!-- 按鈕 -->

        <div class="dialog-actions">

          <!-- 取消 -->

          <button
            class="cancel-button"
            @click="closeCartDialog"
          >

            取消

          </button>


          <!-- 確定加入 -->

          <button
            class="confirm-cart-button"
            @click="addToCart"
          >

            加入購物車

          </button>
        </div>
      </div>
    </div>
  </div>
</template>



<style scoped>

/* 商品詳細頁面 */
.product-detail {
  width: 100%;
  padding: 40px;
  box-sizing: border-box;
}


/* 商品主要區域 */
.product-container {
  max-width: 1000px;
  margin: 0 auto;

  display: flex;

  gap: 60px;

  padding: 30px;

  border: 1px solid #ddd;
  border-radius: 10px;

  box-sizing: border-box;
}


/* 商品資訊 */
.product-info {
  flex: 1;
}


/* 商品名稱 */
.product-info h1 {
  margin-top: 0;
  margin-bottom: 20px;
}


/* 商品價格 */
.price {
  font-size: 30px;
  font-weight: bold;

  margin-bottom: 25px;
}


/* 庫存 */
.stock {
  margin-bottom: 25px;
}


/* 加入購物車 */
.cart-button {
  width: 200px;

  padding: 12px;

  border: none;
  border-radius: 6px;

  cursor: pointer;

  font-size: 16px;
}


/* 商品庫存為 0 時，加入購物車按鈕不能使用 */
.cart-button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}


/* 載入與錯誤 */
.status {
  text-align: center;

  padding: 50px;
}


.error {
  color: red;
}


/* 購物車視窗背景遮罩 */
.cart-overlay {
  position: fixed;

  top: 0;
  left: 0;

  width: 100%;
  height: 100%;

  background-color: rgba(0, 0, 0, 0.5);

  display: flex;

  justify-content: center;
  align-items: center;

  z-index: 1000;
}


/* 購物車視窗 */
.cart-dialog {
  width: 400px;

  padding: 30px;

  background-color: rgb(20, 21, 34);

  border-radius: 10px;

  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);

  box-sizing: border-box;
}


/* 視窗標題 */
.cart-dialog h2 {
  text-align: center;

  margin-bottom: 25px;
}


/* 數量區域 */
.quantity-area {
  display: flex;

  align-items: center;
  justify-content: center;

  gap: 15px;

  margin: 25px 0;
}


/* 數量按鈕 */
.quantity-area button {
  width: 35px;
  height: 35px;

  font-size: 20px;

  cursor: pointer;
}


/* 數量 */
.quantity {
  min-width: 30px;

  text-align: center;

  font-size: 18px;
}


/* 總價格 */
.total-price {
  font-size: 20px;

  font-weight: bold;

  text-align: right;

  margin-top: 20px;
}


/* 視窗底部按鈕 */
.dialog-actions {
  display: flex;

  justify-content: flex-end;

  gap: 10px;

  margin-top: 25px;
}


/* 取消按鈕 */
.cancel-button {
  padding: 10px 20px;

  border: 1px solid #333333;
  border-radius: 5px;

  background-color: #333;

  cursor: pointer;
}


/* 加入購物車確認按鈕 */
.confirm-cart-button {
  padding: 10px 20px;

  border: none;
  border-radius: 5px;

  background-color: #333;

  color: white;

  cursor: pointer;
}


/* 登入連結 */
.login-link {
  padding: 10px 20px;

  border-radius: 5px;

  background-color: #333;

  color: white;

  text-decoration: none;
}

</style>