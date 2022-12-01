import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import("@/views/IndexView")
    },
    {
        path: '/login',
        name: 'login',
        component: () => import("@/views/LoginView")
    },
    {
        path: '/register',
        name: 'register',
        component: () => import("@/views/RegisterView")
    },
    {
        path: '/verify',
        name: 'verify',
        component: () => import("@/views/VerifyView")
    },
    {
        path: '/list/:type', // 动态路由，:type表示一个变量
        name: 'list',
        component: () => import("@/views/ListView")
    },
    {
        path: '/userCenter',
        name: 'userCenter',
        component: () => import("@/views/UserCenterView")
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
