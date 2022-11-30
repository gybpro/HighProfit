<template>
    <li>
        <h3 v-if="product.productType === 1" class="preferred-select-title">
            <span>{{ product.productName }}</span>
            <img :src="backgroundImg" alt="">
        </h3>

        <h3 v-else class="preferred-select-title1">{{ product.productName }}
            <span>散标</span>
        </h3>

        <div class="preferred-select-number">
            <p><b>{{ product.rate }}</b>%</p>
            <span>历史年化收益率</span>
        </div>
        <div class="preferred-select-date">
            <div>
                <span>投资周期</span>
                <p><b>{{ product.cycle }}</b>个月</p>
            </div>
            <div>
                <span>余利可投资金额</span>
                <p><b>{{ product.leftProductMoney }}</b>元</p>
            </div>
        </div>
        <p class="preferred-select-txt">
            {{ product.productDesc }}
        </p>
        <a href="javascript:;" target="_blank" class="preferred-select-btn">立即投资</a>
    </li>
</template>

<script>
export default {
    name: "ProductItem",
    props: ['product'], // 接收父组件传递的数据
    computed: {
        backgroundImg() {
            if (this.product.cycle === 1)
                return '/image/1-bg1.jpg';
            if (this.product.cycle === 3)
                return '/image/1-bg2.jpg';
            if (this.product.cycle === 6)
                return '/image/1-bg3.jpg';
            return '/image/1-bg4.jpg';
        }
    }
}
</script>

<style scoped>

</style>
