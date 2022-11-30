<template>
    <span>{{ totalMoney }}</span>
</template>

<script>
import Vue from "vue";

export default {
    name: "TotalMoney",
    data() {
        return {
            totalMoney: 0
        }
    },
    created() {
        Vue.axios.get("bidInfo/totalMoney").then(json => {
           this.totalMoney = 1000000 + json.data;
        });
    }
}
</script>

<style scoped>

</style>
