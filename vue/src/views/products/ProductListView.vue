<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProducts } from '../../api/productApi'
import { useAuthStore } from '../../stores/auth'
import axiosInstance from '../../api/axiosInstance'

const router = useRouter() // 取得目前網址的路由資訊
const products = ref([]) // 儲存商品列表資料
const isLoading = ref(true) // 商品資料是否正在載入
const errorMessage = ref('') // 儲存商品資料載入時的錯誤訊息
const showLogoutConfirm = ref(false) // 控制「確定登出嗎？」確認視窗是否顯示
const authStore = useAuthStore() // 取得會員登入狀態的 Pinia Store


// 新增商品視窗
const showAddProduct = ref(false)

// 新增商品資料
const newProduct = ref({
  productId: '',
  productName: '',
  price: '',
  quantity: ''
})

// 新增商品錯誤訊息
const addProductError = ref('')

// 新增商品中
const isAddingProduct = ref(false)


// 取得商品
async function fetchProducts() {

  try {

    isLoading.value = true

    errorMessage.value = ''

    const response =
      await getProducts()

    products.value = response

  } catch (error) {

    console.error(
      '取得商品失敗：',
      error
    )

    errorMessage.value =
      '商品取得失敗'

  } finally {

    isLoading.value = false

  }

}


// 登出
async function logout() {

  const success =
    await authStore.logout()

  if (success) {

    showLogoutConfirm.value = false

    router.push('/products')

  }

}


// 查看商品
function goToDetail(productId) {

  router.push(
    `/products/${productId}`
  )

}


// 開啟新增商品視窗
function openAddProduct() {

  newProduct.value = {
    productId: '',
    productName: '',
    price: '',
    quantity: ''
  }

  addProductError.value = ''

  showAddProduct.value = true
}


// 關閉新增商品視窗
function closeAddProduct() {

  showAddProduct.value = false

}


// 新增商品
async function addProduct() {

  if (!newProduct.value.productId ||
      !newProduct.value.productName ||
      newProduct.value.price === '' ||
      newProduct.value.quantity === '') {

    addProductError.value = '請填寫完整的商品資訊'

    return
  }


  if (Number(newProduct.value.price) < 0) {

    addProductError.value = '商品價格不可小於 0'

    return
  }


  if (Number(newProduct.value.quantity) < 0) {

    addProductError.value = '商品庫存不可小於 0'

    return
  }


  try {

    isAddingProduct.value = true

    addProductError.value = ''


    const response =
      await axiosInstance.post(
        '/product',
        {
          productId: newProduct.value.productId,
          productName: newProduct.value.productName,
          price: Number(newProduct.value.price),
          quantity: Number(newProduct.value.quantity)
        }
      )


    if (response === '商品新增成功') {

      alert('商品新增成功')

      showAddProduct.value = false

      await fetchProducts()

    } else {

      addProductError.value =
        response || '商品新增失敗'

    }

  } catch (error) {

    console.error(
      '新增商品失敗：',
      error
    )

    addProductError.value =
      '商品新增失敗，請確認商品編號是否重複'

  } finally {

    isAddingProduct.value = false

  }

}


// 頁面載入
onMounted(() => {

  fetchProducts()

  authStore.checkLogin()

})

</script>


<template>
  
  <!-- 上方會員區 -->
  <div class="top-bar">

    <div class="member-area">

      <!-- 未登入 -->

      <template
        v-if="!authStore.isLoggedIn"
      >

        <span>
          未登入
        </span>

        <router-link to="/login">
          立即登入
        </router-link>

      </template>


      <!-- 已登入 -->
      <template v-else>

        <span>
          Hi~{{ authStore.username }}
        </span>

        <button
          class="logout-button"
          @click="showLogoutConfirm = true"
        >
          登出
        </button>

      </template>

    </div>


    <!-- 購物車 -->
    <router-link
      v-if="authStore.role !== 'RULER'"
      to="/cart"
      class="cart-link"
    >
    我的購物車
  </router-link>

  <router-link
    v-else
    to="/admin/orders"
    class="cart-link"
  >
    查看所有訂單資訊
  </router-link>

  </div>


  <!-- 登出確認視窗-->
  <div
    v-if="showLogoutConfirm"
    class="logout-overlay"
  >

    <div class="logout-dialog">

      <h3>
        確定登出嗎？
      </h3>

      <div class="logout-actions">

        <button
          @click="logout"
        >
          是
        </button>

        <button
          @click="
            showLogoutConfirm = false
          "
        >
          否
        </button>

      </div>

    </div>

  </div>


  <!-- 商品標題 -->
  <div class="product-title-area">

  <h1>
    商品列表
  </h1>

  <!-- 只有管理員看到 -->
  <button
    v-if="authStore.role === 'RULER'"
    class="add-product-button"
    @click="openAddProduct"
  >
    ＋新增商品
  </button>

</div>

  <!-- 新增商品視窗 -->
  <div
    v-if="showAddProduct"
    class="add-product-overlay"
  >

    <div class="add-product-dialog">

      <h2>
        新增商品
      </h2>


      <div class="form-group">

        <label>
          商品編號
        </label>

        <input
          v-model="newProduct.productId"
          type="text"
          placeholder="請輸入商品編號"
        />

      </div>


      <div class="form-group">

        <label>
          商品名稱
        </label>

        <input
          v-model="newProduct.productName"
          type="text"
          placeholder="請輸入商品名稱"
        />

      </div>


      <div class="form-group">

        <label>
          價格
        </label>

        <input
          v-model="newProduct.price"
          type="number"
          min="0"
          placeholder="請輸入價格"
        />

      </div>


      <div class="form-group">

        <label>
          庫存
        </label>

        <input
          v-model="newProduct.quantity"
          type="number"
          min="0"
          placeholder="請輸入庫存"
        />

      </div>


      <p
        v-if="addProductError"
        class="add-product-error"
      >
        {{ addProductError }}
      </p>


      <div class="add-product-actions">

        <button
          @click="closeAddProduct"
          :disabled="isAddingProduct"
        >
          取消
        </button>


        <button
          @click="addProduct"
          :disabled="isAddingProduct"
        >
          {{
            isAddingProduct
              ? '新增中...'
              : '新增商品'
          }}
        </button>
      </div>
    </div>
  </div>


  <!-- 載入中-->
  <div
    v-if="isLoading"
    class="status"
  >
    商品載入中...
  </div>

  <!-- 錯誤-->
  <div
    v-else-if="errorMessage"
    class="status error"
  >

    <p>
      {{ errorMessage }}
    </p>

    <button
      @click="fetchProducts"
    >
      重新載入
    </button>

  </div>


  <!-- 商品列表 -->
  <div
    v-else
    class="product-list"
  >

    <!-- 標題列 -->
    <div class="product-header">

      <div class="product-name">
        商品名稱
      </div>

      <div class="product-price">
        價格
      </div>

      <div class="product-action">
          
      </div>

    </div>


    <!-- 商品 -->
    <div
      v-for="product in products"
      :key="product.productId"
      class="product-row"
    >

      <!-- 商品名稱 -->
      <div class="product-name">

        {{ product.productName }}

      </div>


      <!-- 商品價格 -->
      <div class="product-price">

        ${{ product.price }}

      </div>


      <!-- 查看商品 -->
      <div class="product-action">

        <button
          @click="
            goToDetail(product.productId)
          "
        >
          查看商品
        </button>
      </div>
    </div>
  </div>
</template>


<style scoped>

:global(body) {
  margin: 0;
  background-color: #1e1e1e;
  color: #ffffff;
}


/* 整個商品頁面 */
.product-list-page {
  min-height: 100vh;
  background-color: #1e1e1e;
  color: #ffffff;
  padding: 20px;
  box-sizing: border-box;
}

/* 上方會員區 */
.top-bar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.member-area {
  display: flex;
  align-items: center;
  gap: 10px;
}

.member-area a {
  color: #8ab4f8;
  text-decoration: none;
}

.member-area a:hover {
  text-decoration: underline;
}

.logout-button {
  border: none;
  background: none;
  color: #ffffff;
  cursor: pointer;
  font-size: 14px;
}

.logout-button:hover {
  color: #bbbbbb;
}


/* 購物車 */
.cart-link {
  padding: 8px 15px;
  border: 1px solid #555555;
  border-radius: 5px;
  color: #ffffff;
  text-decoration: none;
  background-color: #2b2b2b;
}

.cart-link:hover {
  background-color: #3a3a3a;
}


/* 商品標題 */
h1 {
  margin-bottom: 30px;
  color: #ffffff;
  font-size: 32px;
  font-weight: bold;
  text-align: center;
}


/* 商品列表 */
.product-list {
  width: 90%;
  max-width: 1000px;
  margin: 0 auto;
  border: 1px solid #444444;
  border-radius: 10px;
  overflow: hidden;
  background-color: #252525;
}


/* 表頭 */
.product-header {
  display: grid;
  grid-template-columns:1fr 200px 150px;
  align-items: center;
  min-height: 55px;
  padding: 10px 20px;
  box-sizing: border-box;
  border-bottom: 1px solid #555555;
  background-color: #303030;
  color: #cccccc;
  font-weight: bold;
}


/* 商品列 */
.product-row {
  display: grid;
  grid-template-columns:1fr 200px 150px;
  align-items: center;
  min-height: 70px;
  padding: 10px 20px;
  box-sizing: border-box;
  border-bottom: 1px solid #3d3d3d;
  background-color: #252525;
}


/* 滑鼠移過商品 */
.product-row:hover {
  background-color: #303030;
}


/* 最後一筆不要底線 */
.product-row:last-child {
  border-bottom: none;
}


/* 商品名稱 */
.product-name {
  color: #ffffff;
  font-size: 18px;
}


/* 商品價格*/
.product-price {
  color: #eeeeee;
  font-size: 18px;
  font-weight: bold;
}


/* 查看商品按鈕 */
.product-action button {
  padding: 8px 18px;
  border: 1px solid #555555;
  border-radius: 5px;
  background-color: #333333;
  color: #ffffff;
  cursor: pointer;
}

.product-action button:hover {
  background-color: #444444;
}


/* 載入狀態 */
.status {
  padding: 50px;
  color: #dddddd;
  text-align: center;
}


/* 錯誤 */
.error {
  color: #ff6b6b;
}


/* 登出視窗 */
.logout-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.65);
  z-index: 1000;
}


.logout-dialog {
  width: 300px;
  padding: 30px;
  border: 1px solid #444444;
  border-radius: 10px;
  background-color: #292929;
  color: #ffffff;
  text-align: center;
  box-shadow:
    0 5px 20px
    rgba(0, 0, 0, 0.5);
}


.logout-dialog h3 {
  margin-bottom: 25px;
}


.logout-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}


.logout-actions button {
  padding: 8px 25px;
  border: 1px solid #555555;
  border-radius: 5px;
  background-color: #333333;
  color: #ffffff;
  cursor: pointer;
}


.logout-actions button:hover {
  background-color: #444444;
}


/* 商品標題區 */
.product-title-area {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}


.product-title-area h1 {
  margin: 0;
}


/* 新增商品按鈕固定在右邊 */
.add-product-button {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  padding: 10px 18px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 15px;
}


/* 新增商品視窗背景 */
.add-product-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}


/* 新增商品視窗 */
.add-product-dialog {
  width: 420px;
  max-width: 90%;
  padding: 30px;
  background: rgb(48, 48, 48);
  border-radius: 10px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
}


/* 表單 */
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}


.form-group label {
  margin-bottom: 6px;
  font-weight: bold;
}


.form-group input {
  padding: 9px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 15px;
}


/* 錯誤訊息 */
.add-product-error {
  margin: 10px 0;
}


/* 按鈕 */
.add-product-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}


.add-product-actions button {
  padding: 9px 18px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

</style>
