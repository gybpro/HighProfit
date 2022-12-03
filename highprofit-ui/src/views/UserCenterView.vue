<template>
    <div>
        <Header/>

        <div class="content clearfix">
            <!--个人信息-->
            <div class="user-head">
                <div class="user-head-left fl">
                    <div class="user-head-img">
                        <label for="userPic" style="cursor: pointer">
                            <img :src="user.headerImage || '/image/user-head.png'" alt="">
                        </label>
                        <input type="file" accept="image/*" @change="upload" id="userPic" style="display: none">
                    </div>
                    <!--<p style="cursor: pointer">上传头像</p>-->
                </div>
                <div class="user-head-right fl">
                    <ul class="user-head-name fl">
                        <li><b>{{ user.name }}</b></li>
                        <li>{{ user.phone }}</li>
                        <li>最近登录：{{ user.lastLoginTime }}</li>
                    </ul>
                    <div class="user-head-money fr">
                        <p>可用余额：<span>￥{{ accountBalance | fmtMoney }}元</span></p>
                        <a href="user_pay.html" target="_blank" class="user-head-a1">充值</a>
                        <a href="details.html" target="_blank" class="user-head-a2">投资</a>
                    </div>
                </div>
            </div>
            <!--记录-->
            <div class="user-record-box clearfix">
                <div class="user-record user-record-1">
                    <h3 class="user-record-title">最近投资</h3>
                    <table v-if="bidRecords.length" align="center" width="388" border="0" cellspacing="0"
                           cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>投资产品</th>
                            <th>投资金额</th>
                            <th>投资时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(r, i) in bidRecords">
                            <td>{{ i + 1 }}</td>
                            <td>{{ r.product.productName }}</td>
                            <td>{{ r.bidMoney | fmtMoney }}</td>
                            <td>{{ r.bidTime | fmtDate }}</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p v-else class="user-record-no">还没有投资记录，请投资：<a href="user_center.html"
                                                                              target="_blank">投资</a>
                    </p>
                </div>
                <div class="user-record user-record-2">
                    <h3 class="user-record-title">最近充值</h3>
                    <table v-if="rechargeRecords.length" align="center" width="388" border="0" cellspacing="0"
                           cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>充值描述</th>
                            <th>充值金额</th>
                            <th>充值时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(r, i) in rechargeRecords">
                            <td>{{ i + 1 }}</td>
                            <td>{{ r.rechargeDesc }}</td>
                            <td>{{ r.rechargeMoney | fmtMoney }}</td>
                            <td>{{ r.rechargeTime | fmtDate }}</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p v-else class="user-record-no">还没有充值记录，请充值：<a href="user_pay.html"
                                                                              target="_blank">充值</a></p>
                </div>
                <div class="user-record user-record-3">
                    <h3 class="user-record-title ">最近收益</h3>
                    <table v-if="incomeRecords.length" align="center" width="388" border="0" cellspacing="0"
                           cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>项目名称</th>
                            <th>投资日期</th>
                            <th>收益金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(r, i) in incomeRecords">
                            <td>{{ i + 1 }}</td>
                            <td>{{ r.product.productName }}</td>
                            <td>{{ r.bidInfo.bidTime | fmtDate }}</td>
                            <td>{{ r.incomeMoney | fmtMoney }}</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p v-else class="user-record-no">还没有收益记录</p>
                </div>
            </div>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Vue from "vue";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import filters from "@/utils/filter";

export default {
    name: "UserCenter",
    components: {Footer, Header},
    data() {
        return {
            user: JSON.parse(sessionStorage.getItem("user")),
            accountBalance: 0,
            bidRecords: "",
            rechargeRecords: "",
            incomeRecords: "",
        }
    },
    filters,
    created() {
        Vue.axios.get("/account/balance").then(response => {
            this.accountBalance = response.data;
        });
        Vue.axios.get("/bidInfo/record").then(response => {
            if (response.data) {
                this.bidRecords = response.data;
            }
        });
        Vue.axios.get("/recharge/record").then(response => {
            if (response.data) {
                this.rechargeRecords = response.data;
            }
        });
        Vue.axios.get("/income/record").then(response => {
            if (response.data) {
                this.incomeRecords = response.data;
            }
        });
    },
    methods: {
        upload() {
            if (userPic.files.length) {
                // 文件上传必须用postForm()方法
                Vue.axios.postForm("/user/upload", {userPic: userPic.files[0]}).then(response => {
                    this.user.headerImage = response.data;
                    sessionStorage.setItem("user", JSON.stringify(this.user));
                    alert(this.user.headerImage);
                });
            }
        }
    }
}
</script>

<style scoped>

</style>
