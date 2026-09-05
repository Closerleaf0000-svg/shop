export const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/home/HomeView.vue'),
    meta: {
      title: '首頁'
    }
  },

  {
    path: '/products',
    name: 'products',
    component: () => import('@/views/products/ProductListView.vue'),
    meta: {
      title: '商品列表'
    }
  },

  {
    path: '/products/:id',
    name: 'product-detail',
    component: () => import('@/views/products/ProductDetailView.vue'),
    meta: {
      title: '商品詳情'
    }
  },

  {
    path: '/admin/orders',
    name: 'admin-orders',
    component: () => import('@/views/admin/AdminOrderView.vue'),
    meta: {
    title: '所有訂單資訊'
    }
  }
]