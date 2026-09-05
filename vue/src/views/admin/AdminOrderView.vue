<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axiosInstance from '../../api/axiosInstance'
import { useAuthStore } from '../../stores/auth'

const router = useRouter() // 取得路由
const authStore = useAuthStore() // 取得登入狀態
const orders = ref([]) // 儲存後端回傳的所有訂單
const isLoading = ref(true) // 控制訂單是否正在載入
const errorMessage = ref('') // 儲存錯誤訊息

// 查詢所有訂單
async function fetchOrders() {
  try {
    isLoading.value = true
    errorMessage.value = ''

    const response = await axiosInstance.get('/order/all')

    if (!response.success) {
      errorMessage.value =
        response.message || '無法取得訂單'
      return
    }

    orders.value = response.orders || []

  } catch (error) {

    console.error(
      '取得所有訂單失敗：',
      error
    )

    errorMessage.value =
      '取得訂單失敗'

  } finally {
    isLoading.value = false
  }
}

// 返回商品列表
function goToProducts() {
  router.push('/products')
}

// Vue 元件載入完成後執行
onMounted(async () => {
  
  // 向後端確認目前是否有登入
  await authStore.checkLogin()

  // 沒登入
  if (!authStore.isLoggedIn) {
    router.push('/login')
    return
  }

  // 不是管理者
  if (authStore.role !== 'RULER') {
    router.push('/products')
    return
  }

  await fetchOrders()
})
</script>


<template>

  <!-- 管理者訂單頁面的主要容器 -->
  <div class="admin-orders-page">

    <!-- 頁面上方區域 -->
    <div class="top-bar">

      <h1>所有訂單資訊</h1>

      <button
        class="back-button"
        @click="goToProducts"
      >
        返回商品頁
      </button>

    </div>


    <div v-if="isLoading" class="message">
      訂單載入中...
    </div>

    <!-- 如果發生錯誤 顯示錯誤訊息 -->
    <div
      v-else-if="errorMessage"
      class="error-message"
    >
      {{ errorMessage }}
    </div>

    <!-- 如果沒有任何訂單 顯示「目前沒有訂單」 -->
    <div
      v-else-if="orders.length === 0"
      class="message"
    >
      目前沒有訂單
    </div>

    <!-- 有訂單時 顯示訂單表格 -->
    <div
      v-else
      class="order-table-container"
    >

      <table class="order-table">

        <thead>

          <tr>
            <th>訂單編號</th>
            <th>會員 ID</th>
            <th>訂單金額</th>
            <th>付款狀態</th>
          </tr>

        </thead>


        <tbody>

          <tr
            v-for="order in orders"
            :key="order.orderId"
          >

            <td>
              {{ order.orderId }}
            </td>

            <td>
              {{ order.memberId }}
            </td>

            <td>
              ${{ order.price.toLocaleString() }}
            </td>

            <td>

              <span
                v-if="order.payStatus === 1"
                class="paid"
              >
                已付款
              </span>

              <span
                v-else
                class="unpaid"
              >
                未付款
              </span>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>

</template>


<style scoped>

.admin-orders-page {
  min-height: 100vh;
  background-color: #1e1e1e;
  color: white;
  padding: 30px;
  box-sizing: border-box;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.top-bar h1 {
  margin: 0;
}

.back-button {
  padding: 10px 18px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.order-table-container {
  width: 100%;
  overflow-x: auto;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #2a2a2a;
}

.order-table th,
.order-table td {
  padding: 15px;
  border-bottom: 1px solid #444;
  text-align: left;
}

.order-table th {
  background-color: #333;
}

.order-table tr:hover {
  background-color: #383838;
}

.paid {
  color: #6ee7b7;
}

.unpaid {
  color: #fbbf24;
}

.message {
  text-align: center;
  padding: 50px;
}

.error-message {
  color: #ff6b6b;
  text-align: center;
  padding: 50px;
}

</style>