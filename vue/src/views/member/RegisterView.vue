<script setup>

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerMember } from '../../api/memberApi'

// 取得目前網址的路由資訊
const router = useRouter()

// 表單資料
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')
const phone = ref('')

// 狀態
const errorMessage = ref('')
const successMessage = ref('')
const isLoading = ref(false)

// 註冊
async function register() {

  // 清除之前的訊息
  errorMessage.value = ''
  successMessage.value = ''

  // 前端基本檢查
  if (
    username.value.trim() === '' ||
    password.value === '' ||
    confirmPassword.value === '' ||
    email.value.trim() === '' ||
    phone.value.trim() === ''
  ) {
    errorMessage.value = '請填寫所有欄位'
    return
  }

  // 密碼格式
  // 6~20碼，至少包含一個英文字母與一個數字
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(password.value)) {

    errorMessage.value =
      '密碼需為6~20碼,且至少包含一個英文字母與一個數字'

    return
  }

  // 確認密碼
  if (password.value !== confirmPassword.value) {

    errorMessage.value =
      '兩次輸入的密碼不一致'

    return
  }

  // 手機號碼格式
  // 必須是09開頭，總共10位，而且全部都是數字
  if (!/^09\d{8}$/.test(phone.value)) {

    errorMessage.value =
      '手機號碼必須是09開頭的10位數字'

    return
  }

  try {

    isLoading.value = true

    // 呼叫後端註冊 API
    const response = await registerMember(
      username.value,
      password.value,
      email.value,
      phone.value,
      confirmPassword.value
    )

    console.log('註冊結果：', response)

    // 註冊成功
    if (response.success) {

      successMessage.value =
        response.message

      // 稍微等待後回登入頁
      setTimeout(() => {
        router.push('/login')
      }, 1000)

    } else {

      // 註冊失敗
      errorMessage.value =
        response.message
    }

  } catch (error) {

    console.error('註冊錯誤：', error)
    console.error(
      'HTTP 狀態：',
      error.response?.status
    )
    console.error(
      '後端回應：',
      error.response?.data
    )

    if (error.response?.data?.message) {

      errorMessage.value =
        error.response.data.message

    } else {

      errorMessage.value =
        '註冊失敗，請查看 VS Code 後端 Console'
    }

  } finally {

    isLoading.value = false
  }
}

</script>


<template>

  <div class="register-page">

    <div class="register-box">

      <!-- 標題 -->
      <h1>
        會員註冊
      </h1>

      <!-- 註冊表單 -->
      <form @submit.prevent="register">

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
            placeholder="6~20 碼,至少包含英文字母與數字"
          />

        </div>

        <!-- 確認密碼 -->
        <div class="form-group">

          <label for="confirmPassword">
            確認密碼
          </label>

          <input
            id="confirmPassword"
            type="password"
            v-model="confirmPassword"
            placeholder="請再次輸入密碼"
          />

        </div>

        <!-- Email -->
        <div class="form-group">

          <label for="email">
            Email
          </label>

          <input
            id="email"
            type="email"
            v-model="email"
            placeholder="請輸入 Email"
          />

        </div>

        <!-- 手機 -->
        <div class="form-group">

          <label for="phone">
            手機號碼
          </label>

          <input
            id="phone"
            type="text"
            v-model="phone"
            placeholder="請輸入 09 開頭的手機號碼"
            maxlength="10"
            inputmode="numeric"
          />

        </div>

        <!-- 註冊按鈕 -->
        <button
          type="submit"
          class="register-button"
          :disabled="isLoading"
        >

          {{ isLoading ? '註冊中...' : '註冊' }}

        </button>

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

      </form>

      <!-- 回登入 -->
      <p class="login-text">

        已經有帳號？

        <router-link to="/login">
          立即登入
        </router-link>

      </p>
    </div>
  </div>
</template>

<style scoped>

/* 註冊頁面 */
.register-page {
  min-height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 註冊表單 */
.register-box {
  width: 350px;
  padding: 35px;
  border: 1px solid #161616;
  border-radius: 10px;
  background-color: rgb(55, 54, 54);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 標題 */
.register-box h1 {
  text-align: center;
  margin-bottom: 30px;
}

/* 表單欄位 */
.form-group {
  margin-bottom: 20px;
}

/* 欄位名稱 */
.form-group label {
  display: block;
  margin-bottom: 8px;
}

/* 輸入框 */
.form-group input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 15px;
}

/* 註冊按鈕 */
.register-button {
  width: 100%;
  padding: 11px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

/* 錯誤訊息 */
.error-message {
  text-align: center;
  margin-top: 15px;
}

/* 成功訊息 */
.success-message {
  text-align: center;
  margin-top: 15px;
}

/* 回登入 */
.login-text {
  text-align: center;
  margin-top: 25px;
}

/* 登入連結 */
.login-text a {
  text-decoration: none;
}

</style>