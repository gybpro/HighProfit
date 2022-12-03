import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

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
    },
    {
        path: '/detail',
        name: 'detail',
        component: () => import("@/views/DetailView")
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

// 登录才能进入的组件
let loginName = ['userCenter', 'verify'];

// 全局导航守卫--验证登录
router.beforeEach((to, from, next) => {
    // 是否为loginName组件
    if (loginName.includes(to.name)) {
        // 是否有登录
        let token = sessionStorage.getItem("token");
        if (!token) {
            next({name: "login"});
        } else {
            // 向后端请求验证登录状态
            Vue.axios.get("/user/checkLogin").then(json => {
                // 有登录状态，则放行，否则跳转登录
                if (json.data.code === "1") {
                    next();
                } else {
                    sessionStorage.removeItem("token");
                    alert(json.data.message);
                    next({name: "login"});
                }
            });
        }
    }
    next();
});

export default router;
