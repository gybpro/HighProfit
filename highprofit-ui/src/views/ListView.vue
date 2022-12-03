<template>
    <div>
        <Header/>

        <div class="content clearfix">
            <!--排行榜-->
            <ul class="rank-list">
                <li v-for="(t,i) in top3">
                    <img :src="'/image/list-rank'+(i+1)+'.png'" alt="">
                    <p class="rank-list-phone">{{ t.phone }}</p>
                    <span>{{ t.money | fmtMoney }}元</span>
                </li>
            </ul>
            <!--产品列表-->
            <ul class="preferred-select clearfix">
                <ProductItem v-for="pro in products" :product="pro"/>
            </ul>

            <!--分页-->
            <div class="page_box">
                <ul class="pagination">
                    <li :class="pageInfo.isFirstPage ? 'disabled' : ''">
                        <span v-if="pageInfo.isFirstPage">首页</span>
                        <a v-else href="javascript:" @click="paging(1, pageInfo.pageSize)">
                            首页
                        </a>
                    </li>
                    <li :class="pageInfo.hasPreviousPage ? '' : 'disabled'">
                        <span v-if="!pageInfo.hasPreviousPage">上一页</span>
                        <a v-else href="javascript:" @click="paging(pageInfo.prePage, pageInfo.pageSize)">
                            上一页
                        </a>
                    </li>
                    <li class="active"><span>{{ pageInfo.pageNum }}</span></li>
                    <li :class="pageInfo.hasNextPage ? '' : 'disabled'">
                        <span v-if="!pageInfo.hasNextPage">下一页</span>
                        <a v-else href="javascript:" @click="paging(pageInfo.nextPage, pageInfo.pageSize)">
                            下一页
                        </a>
                    </li>
                    <li :class="pageInfo.isLastPage ? 'disabled' : ''">
                        <span v-if="pageInfo.isLastPage">尾页</span>
                        <a v-else href="javascript:" @click="paging(pageInfo.pages, pageInfo.pageSize)">
                            尾页
                        </a>
                    </li>
                    <li class="totalPages"><span>共{{ pageInfo.pages }}页</span></li>
                </ul>
            </div>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Vue from "vue";
import ProductItem from "@/components/ProductItem";
import Footer from "@/components/Footer";
import Header from "@/components/Header";
import filters from "@/utils/filter";

export default {
    name: "ListView",
    components: {Header, Footer, ProductItem},
    data() {
        return {
            top3: [],
            products: [],
            pageInfo: ""
        }
    },
    created() {
        Vue.axios.get("/bidInfo/top3").then(response => this.top3 = response.data);

        // 监视路由参数变化，获取动态路由的参数
        this.$watch(
                () => this.$route.params,
                (toParams, previousParams) => {
                    // 对路由变化做出反应...
                    //console.log(toParams);
                    this.paging(1, 9, this.$route.params.type);
                }
        )

        this.paging(1, 9, this.$route.params.type);
    },
    methods: {
        paging(pageNum, pageSize) {
            let proType = this.$route.params.type;
            Vue.axios.get(`/product/pagingQuery/${pageNum}/${pageSize}/${proType}`).then(response => {
                /*PageInfo{pageNum=2, pageSize=2, size=2, startRow=3, endRow=4, total=6, pages=3,
                list=Page{count=true, pageNum=2, pageSize=2, startRow=2, endRow=4, total=6, pages=3,
                reasonable=false, pageSizeZero=false}
                [Car{id=86, carNum='1234', brand='丰田霸道', guidePrice=50.5, produceTime='2020-10-11', carType='燃油车'},
                Car{id=87, carNum='1234', brand='丰田霸道', guidePrice=50.5, produceTime='2020-10-11', carType='燃油车'}],
                prePage=1, nextPage=3, isFirstPage=false, isLastPage=false, hasPreviousPage=true, hasNextPage=true,
                navigatePages=5, navigateFirstPage=1, navigateLastPage=3, navigatepageNums=[1, 2, 3]}*/
                this.pageInfo = response.data;
                this.products = this.pageInfo.list;
            });
        }
    },
    filters
}
</script>

<style scoped>

</style>
