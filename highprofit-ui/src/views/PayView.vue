<template>
    <div>
        <Header/>

        <!--充值-->
        <div class="user-pay-box clearfix">
            <div class="user-pay-left fl">
                <p class="user-pay-title1">我的账户</p>
                <div class="user-pay-list">
                    <a class="user-pay-list-on" href="javascript:;">充值</a>
                    <a href="javascript:;" target="_blank">我的信息</a>
                    <a href="javascript:;" target="_blank">投资记录</a>
                    <a href="javascript:;" target="_blank">收益记录</a>
                </div>
            </div>
            <div class="user-pay-right fl">
                <p class="user-pay-title2">第三方支付平台</p>
                <div class="user-pay-form">
                    <img alt="" src="/image/pay-1.jpg">
                    <form id="money_submit" action="http://127.0.0.1:8081/pay">
                        <p class="user-pay-form-ts">请输入金额</p>
                        <input class="number-money" name="money" placeholder="元" type="text">
                        <input :value="token" name="token" type="hidden">
                        <input class="submit-btn" type="submit" value="支付">
                    </form>
                </div>
                <div class="user-pay-sm">
                    <h3>温馨提示</h3>
                    <p>1.为了您的使用安全，充值操作建议不要使用公共电脑。</p>
                    <p>2.银行卡充值限额，由各银行限制。</p>
                    <p>3.品台禁止用卡套现、虚拟交易、洗钱等行为，一经发现并确认，将终止该账户的使用。</p>
                    <p>4.如果充值中出现问题，请联系客服400-890-0000。</p>
                </div>
            </div>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Footer from "@/components/Footer";
import Header from "@/components/Header";

export default {
    name: "PayView",
    components: {Header, Footer},
    data() {
        return {
            token: sessionStorage.getItem("token"),
        }
    }
}
</script>

<style scoped>

</style>
