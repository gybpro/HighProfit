<template>
    <div>
        <Header/>

        <!--banner-->
        <div class="banner-content">
            <div class="swiper-container banner-lb">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <a href="javascript:" >
                            <img src="/image/banner.jpg" alt="">
                        </a>
                    </div>
                    <div class="swiper-slide">
                        <a href="javascript:" >
                            <img src="/image/banner.jpg" alt="">
                        </a>
                    </div>
                </div>
            </div>
            <div class="banner-abs">
                <div class="banner-abs-box">
                    <div class="banner-abs-title">动力金融网历史年化收益率</div>
                    <b><Rate/>%</b>
                    <p>平台用户数</p>
                    <span><UserCount/> <i>位</i></span>
                    <p class="banner-abs-border">累计成交金额</p>
                    <span><TotalMoney/> <i>元</i></span>
                </div>
            </div>
        </div>
        <div class="banner-list">
            <ul>
                <li>
                    <img src="/image/banner-tag1.png" alt="">
                    <p>
                        <b>实力雄厚</b>
                        <span>亿级注册资本 ,千万技术投入</span>
                    </p>
                </li>
                <li>
                    <img src="/image/banner-tag2.png" alt="">
                    <p>
                        <b>风控严苛</b>
                        <span>30道风控工序，60项信用审核</span>
                    </p>
                </li>
                <li>
                    <img src="/image/banner-tag3.png" alt="">
                    <p>
                        <b>投资省心</b>
                        <span>资金安全风控，银行安全托管</span>
                    </p>
                </li>
            </ul>
        </div>

        <!--产品-->
        <div class="content">
            <h2 class="public-title"><span>{{ product0.productName }}</span></h2>
            <div class="new-user">
                <div class="new-user-sm">
                    <span>{{ product0.bidMinLimit }}元起投</span>
                    <span>投资最高限额{{ product0.bidMaxLimit }}元</span>
                    <span>当日计息</span>
                </div>
                <div class="new-user-number">
                    <ul>
                        <li>
                            <p><b>{{ product0.rate }}</b>%</p>
                            <span>历史年化收益率</span>
                        </li>
                        <li>
                            <p><b>{{ product0.cycle }}</b>个月</p>
                            <span>投资周期</span>
                        </li>
                        <li>
                            <p><b>{{ product0.leftProductMoney }}</b>元</p>
                            <span>余利可投资金额</span>
                        </li>
                    </ul>
                    <router-link :to="'/detail?id=' + product0.id" class="new-user-btn">立即投资</router-link>
                </div>
                <span class="new-tag">新用户专享</span>
            </div>

            <h2 class="public-title">
                <span>优选产品</span>
                <router-link to="/list/1" class="public-title-more">查看更多产品>></router-link>
            </h2>
            <ul class="preferred-select clearfix">
                <ProductItem :product="product1" />
                <ProductItem :product="product3" />
                <ProductItem :product="product6" />
            </ul>

            <h2 class="public-title">
                <span>散标产品</span>
                <router-link to="/list/2" class="public-title-more">查看更多产品>></router-link>
            </h2>
            <ul class="preferred-select clearfix">
                <ProductItem v-for="pro in products" :product="pro" />
            </ul>
        </div>

        <!--说明-->
        <div class="information-box">
            <ul>
                <li>
                    <img src="/image/2-icon1.png" alt="">
                    <p>优质借款</p>
                    <span>严苛风控，多重审核</span>

                </li>
                <li>
                    <img src="/image/2-icon2.png" alt="">
                    <p>次日计息</p>
                    <span>闪电成交，谁比我快</span>
                </li>
                <li>
                    <img src="/image/2-icon3.png" alt="">
                    <p>全年无休</p>
                    <span>百万用户，一路同行</span>
                </li>
                <li>
                    <img src="/image/2-icon4.png" alt="">
                    <p>知心托付</p>
                    <span>百万用户，一路同行</span>
                </li>
                <li>
                    <img src="/image/2-icon5.png" alt="">
                    <p>技术保障</p>
                    <span>千万投入，专注研发</span>
                </li>
            </ul>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import UserCount from "@/components/UserCount";
import TotalMoney from "@/components/TotalMoney";
import Rate from "@/components/Rate";
import Vue from "vue";
import ProductItem from "@/components/ProductItem";
export default {
    name: "IndexView",
    components: {ProductItem, Rate, TotalMoney, UserCount, Header, Footer},
    data() {
        return {
            product0: {}, // 新手宝
            product1: {}, // 优选--满月宝
            product3: {}, // 优选--季度宝
            product6: {}, // 优选--双季宝
            products: [], // 散标
        }
    },
    created() {
        Vue.axios.get("/product/newUserPro").then(response => this.product0 = response.data);
        Vue.axios.get("/product/prePro/1").then(response => this.product1 = response.data);
        Vue.axios.get("/product/prePro/3").then(response => this.product3 = response.data);
        Vue.axios.get("/product/prePro/6").then(response => this.product6 = response.data);
        Vue.axios.get("/product/scatter").then(response => this.products = response.data);
    }
}
</script>

<style scoped>

</style>
