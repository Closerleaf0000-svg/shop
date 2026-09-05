<script setup>

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'


const router = useRouter() // 取得路由
const authStore = useAuthStore() // 取得登入狀態 Store
const username = ref('') // 帳號
const password = ref('') // 密碼
const errorMessage = ref('') // 登入狀態
const isLoading = ref(false) // 登入狀態

// 登入
async function login() {

  // 清除之前的錯誤訊息
  errorMessage.value = ''

  // 檢查帳號
  if (username.value.trim() === '') {
    errorMessage.value = '請輸入帳號'
    return
  }

  // 檢查密碼
  if (password.value === '') {
    errorMessage.value = '請輸入密碼'
    return
  }

  try {

    // 顯示登入中
    isLoading.value = true

    // 呼叫 Pinia 登入
    const success = await authStore.login(
      username.value,
      password.value
    )

    // 判斷登入結果
    if (success) {

      console.log('登入成功')

      // 登入成功後回到商品列表
      router.push('/products')

    } else {

      // 登入失敗
      errorMessage.value = '帳號或密碼錯誤'
    }

  } catch (error) {

    console.error('登入錯誤：', error)

    errorMessage.value = '無法連線到伺服器'

  } finally {

    // 登入結束
    isLoading.value = false
  }
}

</script>

<template>

  <div class="login-page">

    <div class="login-box">

      <!-- 登入標題 -->
      <h1>會員登入</h1>

      <!-- 登入表單 -->
      <form @submit.prevent="login">

        <!-- 帳號 -->
        <div class="form-group">

          <label for="username">
            帳號
          </label>

          <input
            id="username"
            type="text"
            v-model="username"
            placeholder="請輸入帳號"
          />

        </div>

        <!-- 密碼 -->
        <div class="form-group">

          <label for="password">
            密碼
          </label>

          <input
            id="password"
            type="password"
            v-model="password"
            placeholder="請輸入密碼"
          />

        </div>

        <!-- 登入按鈕 -->
        <button
          type="submit"
          class="login-button"
          :disabled="isLoading"
        >
          {{ isLoading ? '登入中...' : '登入' }}
        </button>

        <!-- 登入錯誤訊息 -->
        <p
          v-if="errorMessage"
          class="error-message"
        >
          {{ errorMessage }}
        </p>

      </form>

      <!-- 註冊 -->
      <p class="register-text">

        尚未註冊？

        <router-link to="/register">
          立即註冊
        </router-link>

      </p>
    </div>
  </div>
</template>


<style scoped>

/* 登入頁面 */
.login-page {
  min-height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 登入表單外框 */
.login-box {
  width: 350px;
  padding: 35px;
  border: 1px solid #161616;
  border-radius: 10px;
  background-color: rgb(55, 54, 54);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 登入標題 */
.login-box h1 {
  text-align: center;
  margin-bottom: 30px;
}

/* 帳號、密碼區域 */
.form-group {
  margin-bottom: 20px;
}

/* 帳號、密碼文字 */
.form-group label {
  display: block;
  margin-bottom: 8px;
}

/* 帳號、密碼輸入框 */
.form-group input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 15px;
}

/* 登入按鈕 */
.login-button {
  width: 100%;
  padding: 11px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

/* 登入失敗訊息 */
.error-message {
  text-align: center;
  margin-top: 15px;
}

/* 註冊區域 */
.register-text {
  text-align: center;
  margin-top: 25px;
}

/* 立即註冊 */
.register-text a {
  text-decoration: none;
}

</style>