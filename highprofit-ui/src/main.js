import Vue from 'vue'
import App from './App.vue'
import router from './router'

// Vue整合axios
import axios from "axios";
import VueAxios from "vue-axios";
// 整合ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 导入css文件
import '../public/css/details.css'
import '../public/css/font-awesome.min.css'
import '../public/css/index.css'
import '../public/css/list.css'
import '../public/css/login.css'
import '../public/css/public-head.css'
import '../public/css/reset.css'
import '../public/css/swiper.css'
import '../public/css/user_center.css'
import '../public/css/user_pay.css'

// Global axios defaults：全局默认设置，axios的默认根路径
axios.defaults.baseURL = 'http://127.0.0.1:8081';

// 添加请求拦截器，每次发送请求携带token令牌
// use的第一个函数参数是请求前配置，第二个函数参数是处理请求错误
axios.interceptors.request.use(function (config) {
    // 请求前配置
    // 添加token令牌
    let token = sessionStorage.getItem("token");
    if (token) {
        config.headers.token = token;
    }
    return config;
}, function (error) {
    // 处理请求错误
    return Promise.reject(error);
});

// 添加响应拦截器，每次响应统一拦截处理
axios.interceptors.response.use(function (response) {
    if (response.data.code === "0") {
        alert(response.data.message);
        if (response.data.message === "用户未登录，请前往登录" || response.data.message === "登录超时，请重新登录") {
            router.push(`/login?redirectUrl=${router.currentRoute.fullPath}`);
        }
    }
    return response;
}, function (error) {
    // 处理响应错误
    return Promise.reject(error);
});

Vue.use(ElementUI);

Vue.use(VueAxios, axios);

Vue.config.productionTip = false

let app = new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
