import axiosInstance from './axiosInstance'

// 會員登入
export function loginMember(username, password) {
  return axiosInstance.post('/login', {
    username: username,
    password: password
  })
}

// 會員註冊
export function registerMember(
  username,
  password,
  email,
  phone,
  confirmPassword
) {

  return axiosInstance.post('/register', {
    username: username,
    password: password,
    confirmPassword: confirmPassword,
    email: email,
    phone: phone

  })

}