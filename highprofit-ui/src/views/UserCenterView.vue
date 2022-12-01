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
                    <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>投资产品</th>
                            <th>投资金额</th>
                            <th>投资时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>6</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p class="user-record-no">还没有投资记录，请投资：<a href="user_center.html" target="_blank">投资</a>
                    </p>
                </div>
                <div class="user-record user-record-2">
                    <h3 class="user-record-title">最近充值</h3>
                    <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>充值描述</th>
                            <th>充值金额</th>
                            <th>充值时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        <tr>
                            <td>6</td>
                            <td>新手宝</td>
                            <td>1500.0</td>
                            <td>2021-08-19</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p class="user-record-no">还没有充值记录，请充值：<a href="user_pay.html" target="_blank">充值</a></p>
                </div>
                <div class="user-record user-record-3">
                    <h3 class="user-record-title ">最近收益</h3>
                    <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                        <thead class="datail_thead">
                        <tr>
                            <th>序号</th>
                            <th>项目名称</th>
                            <th>投资日期</th>
                            <th>收益金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        <tr>
                            <td>6</td>
                            <td>新手宝</td>
                            <td>2021-08-19</td>
                            <td>0.46</td>
                        </tr>
                        </tbody>
                    </table>
                    <!--无记录-->
                    <p class="user-record-no">还没有收益记录</p>
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
        }
    },
    filters,
    created() {
        Vue.axios.get("/account/balance").then(json => {
            this.accountBalance = json.data;
        });
    },
    methods: {
        upload() {
            if (userPic.files.length) {
                // 文件上传必须用postForm()方法
                Vue.axios.postForm("/user/upload", {userPic: userPic.files[0]}).then(json => {
                    this.user.headerImage = json.data;
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
