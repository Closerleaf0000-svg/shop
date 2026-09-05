import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import ProductListView from '../views/products/ProductListView.vue'
import ProductDetailView from '../views/products/ProductDetailView.vue'
import LoginView from '../views/member/LoginView.vue'
import RegisterView from '../views/member/RegisterView.vue'
import CartView from '../views/cart/CartView.vue'
import OrderView from '../views/order/OrderView.vue'
import AdminOrderView from '../views/admin/AdminOrderView.vue'

const router = createRouter({
  history: createWebHistory(),

  routes: [

    {
      path: '/',
      component: HomeView
    },

    {
      path: '/products',
      component: ProductListView
    },

    {
      path: '/products/:id',
      component: ProductDetailView
    },

    {
      path: '/login',
      component: LoginView
    
    },

    {
      path: '/register',
      component: RegisterView
    },
    
    {
      path: '/cart',
      component: CartView
    },

    {
      path: '/admin/orders',
      component: AdminOrderView
    },

    {
    path: '/order',
    component: OrderView
    } 
  ],
    // 每次切換頁面，都回到最上方
  scrollBehavior() {
    return {
      top: 0
    }
  }
})

export default router