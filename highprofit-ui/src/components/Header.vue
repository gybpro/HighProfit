<template>
    <div class="public-head">
        <div class="public-head-nav">
            <div class="public-head-left">
                <h1 class="public-head-logo"><a href="javascript:;">
                    <img src="/image/logo.png" alt="">
                </a></h1>
                <ul class="public-head-list">
                    <li><router-link to="/">主页</router-link></li>
                    <li class="public-head-hover">
                        <a href="javascript:void(0);">我要投资</a>
                        <!--二级导航-->
                        <div class="two-title">
                            <a href="javascript:;">优先类产品</a>
                            <a href="javascript:;">散标类产品</a>
                        </div>
                    </li>
                    <li><router-link to="/userCenter">用户中心</router-link></li>
                    <li><a href="javascript:;">信息披露</a></li>
                    <li><a href="javascript:;">安全计划</a></li>
                </ul>
            </div>
            <div v-if="sessionStorage.getItem('token')" class="public-head-right">
                <h3>欢迎登录</h3> &nbsp;
                <h3 style="cursor: pointer; text-decoration: underline black; color: black" @click="logout">安全退出</h3>
                <span/>
            </div>
            <div v-else class="public-head-right">
                <router-link to="/login">登录</router-link>
                <router-link to="/register">注册</router-link>
            </div>
        </div>
    </div>
</template>

<script>
import Vue from "vue";

export default {
    name: "Header",
    data() {
        return {
            sessionStorage
        }
    },
    methods: {
        logout() {
            Vue.axios.get("/user/logout").then(json => {
                if (json.data.code === "1") {
                    sessionStorage.removeItem("token");
                    sessionStorage.removeItem("user");
                    alert("退出成功");
                    this.$router.push("/login");
                } else {
                    alert("系统忙，请稍候重试......");
                }
            });
        }
    }
}
</script>

<style scoped>

</style>
