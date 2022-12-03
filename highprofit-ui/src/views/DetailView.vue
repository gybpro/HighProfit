<template>
    <div>
        <Header/>

        <div class="content clearfix">
            <div class="detail-left">
                <div class="detail-left-title">新手宝（20210819期）</div>
                <ul class="detail-left-number">
                    <li>
                        <span>历史年化收益率</span>
                        <p><b>{{ product.rate | fmtFloat }}</b>%</p>
                        <span>历史年化收益率</span>
                    </li>
                    <li>
                        <span>募集金额（元）</span>
                        <p><b>{{ product.productMoney | fmtMoney }}</b>元</p>
                        <span>募集中&nbsp;&nbsp;剩余募集金额{{ product.leftProductMoney | fmtMoney }}元</span>
                    </li>
                    <li>
                        <span>投资周期</span>
                        <p><b>{{ product.cycle }}</b>个月</p>
                    </li>
                </ul>
                <div class="detail-left-way">
                    <span>收益获取方式</span>
                    <span>收益返还：<i>到期还本付息</i></span>
                </div>
                <!--投资记录-->
                <div class="datail-record">
                    <h2 class="datail-record-title">投资记录</h2>
                    <div class="datail-record-list">
                        <table align="center" width="880" border="0" cellspacing="0" cellpadding="0">
                            <colgroup>
                                <col style="width: 72px"/>
                                <col style="width: 203px"/>
                                <col style="width: 251px"/>
                                <col style="width: 354px"/>
                            </colgroup>
                            <thead class="datail_thead">
                            <tr>
                                <th>序号</th>
                                <th>投资人</th>
                                <th>投资金额（元）</th>
                                <th>投资时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(bidInfo, i) in bidInfos" :key="bidInfo.id">
                                <td>{{ i + 1 }}</td>
                                <td class="datail-record-phone">{{ bidInfo.user.phone }}</td>
                                <td>{{ bidInfo.bidMoney | fmtMoney }}</td>
                                <td>{{ bidInfo.bidTime }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!--右侧-->
            <div class="detail-right">
                <div class="detail-right-title">立即投资</div>
                <div class="detail-right-mode">
                    <h3 class="detail-right-mode-title">收益方式</h3>
                    <p class="detail-right-mode-p"><span>到期还本付息</span></p>
                    <h3 class="detail-right-mode-title">我的账户可用</h3>
                    <div class="detail-right-mode-rmb">
                        <p>资金（元）：{{ accountBalance | fmtMoney }}</p>
                        <router-link v-if="!isLogin" to="/login">请登录</router-link>
                    </div>
                    <h3 class="detail-right-mode-title">预计本息收入{{ profit | fmtMoney }}（元）</h3>
                    <form id="number_submit">
                        <p>请在下方输入投资金额</p>
                        <input v-model="bidMoney" @focus="bidMoneyErr = ''" @blur="checkMoney" type="text"
                               placeholder="请输入日投资金额，应为100元整倍数" name="" class="number-money">
                        <p>{{ bidMoneyErr }} </p>
                        <input type="button" @click="invest" value="立即投资" class="submit-btn">
                    </form>
                </div>
            </div>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Vue from "vue";
import Footer from "@/components/Footer";
import Header from "@/components/Header";
import Rate from "@/components/Rate";
import filters from "@/utils/filter";

export default {
    name: "DetailView",
    components: {Rate, Header, Footer},
    data() {
        return {
            product: "",
            bidInfos: [],
            accountBalance: "",
            bidMoney: "",
            bidMoneyErr: "",
            isLogin: sessionStorage.getItem("token"),
        }
    },
    filters,
    computed: {
        // 预计收益
        profit() {
            if (!this.bidMoney) return 0;
            return this.product.rate / 12 * this.product.cycle * this.bidMoney;
        }
    },
    created() {
        this.load();
    },
    methods: {
        // 加载页面
        async load() {
            this.bidMoney = "";

            this.accountBalance = (await Vue.axios.get("/account/balance")).data;

            let id = this.$route.query.id;
            this.product = (await Vue.axios.get(`/product/info/${id}`)).data;

            this.bidInfos = (await Vue.axios.get(`/bidInfo/record/${id}`)).data;
        },
        // 投资
        async invest() {
            // 登录验证
            if (!this.isLogin || !(await Vue.axios.get("/user/checkLogin")).data) {
                await this.$router.push(`/login?redirectUrl=${this.$route.fullPath}`);
                return;
            }

            // 表单验证
            this.checkMoney();
            if (this.bidMoney === "" || this.bidMoney === 0) {
                this.bidMoneyErr = "投资金额不能为空";
            }
            if (!this.bidMoneyErr) {
                alert(this.bidMoneyErr);
                return;
            }

            // 发送请求
            Vue.axios.post(`/bidInfo/invest/${this.product.id}/${this.bidMoney}`).then(response => {
                if (response.data.code === "1") {
                    this.load();
                }
            });
        },
        checkMoney() {
            if (isNaN(this.bidMoney)) {
                this.bidMoneyErr = "金额不合法";
                return;
            }
            if (this.bidMoney % 100 !== 0) {
                this.bidMoneyErr = "投资金额必须是100的整数倍";
                return;
            }
            if (this.bidMoney < this.product.bidMinLimit) {
                this.bidMoneyErr = "投资金额不能小于最低投资金额";
                return;
            }
            if (this.bidMoney > this.product.bidMaxLimit) {
                this.bidMoneyErr = "投资金额不能大于最高投资金额";
                return;
            }
            if (this.bidMoney > this.product.leftProductMoney) {
                this.bidMoneyErr = "投资金额不能大于产品剩余可投资金额";
                return;
            }
            if (this.bidMoney > this.accountBalance) {
                this.bidMoneyErr = "投资金额不能大于余额";
            }
        }
    }
}
</script>

<style scoped>

</style>
