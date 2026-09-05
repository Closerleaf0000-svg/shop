import { defineStore } from 'pinia'
import axiosInstance from '../api/axiosInstance'

export const useAuthStore = defineStore('auth', {

  state: () => ({
    // 是否登入
    isLoggedIn: false,
    // 會員 ID
    userId: null,
    // 會員帳號
    username: '',
    // 會員角色
    role: ''
  }),

  actions: {

    // 檢查登入狀態
    async checkLogin() {
      try {
        const response = await axiosInstance.get('/member/me')

        this.isLoggedIn = response.loggedIn === true

        if (this.isLoggedIn) {

          this.userId = response.memberId
          this.username = response.username
          this.role = response.role

        } else {

          this.userId = null
          this.username = ''
          this.role = ''
        }

      } catch (error) {

        console.error(
          '檢查登入狀態失敗：',
          error
        )

        this.isLoggedIn = false
        this.userId = null
        this.username = ''
        this.role = ''
      }
    },


    // 登入
    async login(username, password) {

      try {

        const response =
          await axiosInstance.post('/login', {
            username: username,
            password: password
          })

        if (response.success) {

          // 更新登入狀態
          this.isLoggedIn = true
          // 儲存會員 ID
          this.userId = response.memberId
          // 儲存會員帳號
          this.username = response.username
          // 儲存會員角色
          this.role = response.role

          return true
        }

        return false

      } catch (error) {

        console.error(
          '登入失敗：',
          error
        )

        return false
      }
    },


    // 登出
    async logout() {

      try {

        const response =
          await axiosInstance.post('/logout')

        if (response.success) {

          // 清除前端登入狀態
          this.isLoggedIn = false
          this.userId = null
          this.username = ''
          this.role = ''

          return true
        }

        return false

      } catch (error) {

        console.error(
          '登出失敗：',
          error
        )

        return false
      }
    }
  }
})