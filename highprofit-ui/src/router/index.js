import Vue from 'vue'
import VueRouter from 'vue-router'
import RegisterView from "@/views/RegisterView";
import LoginView from "@/views/LoginView";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'login',
        component: LoginView
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterView
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
