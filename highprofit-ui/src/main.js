import Vue from 'vue'
import App from './App.vue'
import router from './router'

// Vue整合axios
import axios from "axios";
import VueAxios from "vue-axios";

// Global axios defaults：全局默认设置，axios的默认根路径
axios.defaults.baseURL = 'http://127.0.0.1:8081';

Vue.use(VueAxios, axios);

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

Vue.config.productionTip = false

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
