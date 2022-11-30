<template>
    <span>{{ count }}</span>
</template>

<script>
import Vue from "vue";

export default {
    name: "UserCount",
    data() {
        return {
            count: 0
        }
    },
    created() {
        Vue.axios.get("/user/userCount").then(json => {
            this.count = json.data + 500;
        });
    }
}
</script>

<style scoped>

</style>
