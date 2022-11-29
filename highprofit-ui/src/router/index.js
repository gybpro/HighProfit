import Vue from 'vue'
import VueRouter from 'vue-router'
import RegisterView from "@/views/RegisterView";
import LoginView from "@/views/LoginView";
import IndexView from "@/views/IndexView";
import VerifyView from "@/views/VerifyView";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'index',
        component: IndexView
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterView
    },
    {
        path: '/verify',
        name: 'verify',
        component: VerifyView
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
