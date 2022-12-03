<template>
    <span>{{ rate | fmtFloat }}</span>
</template>

<script>
// 引入Vue
import Vue from "vue";
// 引入filters
import filters from "@/utils/filter";

export default {
    name: "Rate",
    data() {
        return {
            rate: 0
        }
    },
    // 在组件创建完成时，发送ajax请求获取数据
    created() {
        // 使用Vue-axios发送ajax请求，获取利率
        Vue.axios.get("/product/rate").then(json => this.rate = json.data);
    },
    filters
}
</script>

<style scoped>

</style>
