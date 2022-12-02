<template>
    <div>
        <!--头部-->
        <Header/>

        <div class="login-content">
            <div class="login-flex">
                <div class="login-left">
                    <p>万民用户知心托付&nbsp;&nbsp;&nbsp;&nbsp;<Rate/>
                        历史年化收益
                    </p>
                    <p>千万级技术研发投入&nbsp;&nbsp;&nbsp;&nbsp;亿级注册资本平台 </p>
                </div>
                <div class="login-box">
                    <h3 class="login-title">用户注册</h3>
                    <form action="" id="register_Submit">
                        <div class="alert-input">
                            <input v-model="phone" type="text" @blur="checkPhone" @focus="phoneErr = ''"
                                   class="form-border user-num" name="phone" placeholder="请输入11位手机号">
                            <p class="prompt_num">{{ phoneErr }}</p>
                            <input v-model="password" type="password" @blur="checkPwd" @focus="passwordErr = ''"
                                   placeholder="请输入6-20位英文和数字混合密码" class="form-border user-pass"
                                   autocomplete name="password">
                            <p class="prompt_pass">{{ passwordErr }}</p>
                            <div class="form-yzm form-border">
                                <input v-model="code" class="yzm-write" type="text" name="code"
                                       placeholder="输入短信验证码">
                                <input class="yzm-send" @click="sendCode" type="text" :value="sendText" id="yzmBtn"
                                       :disabled="cdFlag" :style="{color: cdFlag ? '#c0c0c0' : ''}" readonly="readonly">
                            </div>
                            <p class="prompt_yan" @focus="codeErr = ''">{{ codeErr }}</p>
                        </div>
                        <div class="alert-input-agree">
                            <input type="checkbox" v-model:checked="agree" class="fa fa-square-o"/>
                            我已阅读并同意<a href="javascript:" target="_blank">《动力金融网注册服务协议》</a>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="register" :disabled="!agree"
                                   :style="agree ? '' : {'background':'rgba(254, 46, 85, 0.5)'}"
                                   class="login-submit" value="注册">
                        </div>
                    </form>
                    <div class="login-skip">
                        已有账号？
                        <router-link to="/login">登录</router-link>
                    </div>
                </div>
            </div>
        </div>

        <!--公共底部-->
        <Footer/>
    </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import Rate from "@/components/Rate";
import Vue from "vue";
import qs from "qs";
import regExp from "@/utils/regExp"

export default {
    name: "RegisterView",
    components: {Header, Footer, Rate},
    data() {
        return {
            phone: "",
            password: "",
            code: "",
            agree: false,
            phoneErr: "",
            passwordErr: "",
            codeErr: "",
            realCode: "",
            registered: false, // 手机号是否已注册
            sendText: "获取验证码",
            sendInterval: 60, // 短信发送间隔
            cdFlag: false // 发送冷却标志
        }
    },
    methods: {
        sendCode() {
            if (this.phone === "") {
                this.phoneErr = "手机号码不能为空";
                alert(this.phoneErr);
                return;
            }
            if (!regExp.phone.test(this.phone)) {
                this.phoneErr = "手机号码格式不正确";
                alert(this.phoneErr);
                return;
            }
            if (this.registered) {
                this.phoneErr = "手机号码已被注册";
                alert(this.phoneErr);
                return;
            }
            this.cdFlag = true;
            Vue.axios.get("sms/register/" + this.phone).then(json => {
                console.log(json.data);
                this.realCode = json.data + "";
                /* 倒计时: 参数1表示定时任务，参数2表示任务多久执行一次
                setTimeout: 只执行一次
                setInterval: 多次执行
                 */
                let interval = this.sendInterval;
                let flag = setInterval(() => {
                    this.sendText = --interval + "s后重试";
                    if (interval === 0) {
                        clearInterval(flag);
                        this.sendText = "获取验证码";
                        this.cdFlag = false;
                    }
                }, 1000);
            });
        },
        register() {
            if (!this.checkPhone()) {
                alert(this.phoneErr);
                return;
            }
            if (!this.checkPwd()) {
                alert(this.passwordErr);
                return;
            }
            if (this.phone === "") {
                this.phoneErr = "手机号码不能为空";
                alert(this.phoneErr);
                return;
            }
            if (this.password === "") {
                this.passwordErr = "密码不能为空";
                alert(this.passwordErr);
                return;
            }
            if (this.code === "") {
                this.codeErr = "验证码不能为空";
                alert(this.codeErr);
                return;
            }
            if (this.code !== this.realCode) {
                this.codeErr = "验证码错误";
                alert(this.codeErr);
                return;
            }
            let data = {
                phone: this.phone,
                password: this.password,
                code: this.code
            };
            this.checkPhone();
            this.checkPwd();
            data = qs.stringify(data);
            Vue.axios.post("user/register", data).then(json => {
                if (json.data.code === "1") {
                    alert("注册成功，请前往登录");
                    this.$router.push("/login");
                }
            });
        },
        async checkPhone() {
            if (this.phone) {
                if (!regExp.phone.test(this.phone)) {
                    this.phoneErr = "手机号码格式不正确";
                    return false;
                } else {
                    let response = await Vue.axios.get("/user/check/" + this.phone);
                    if (!response.data) {
                        this.phoneErr = "手机号码已被注册";
                        this.registered = true;
                        return false;
                    }
                    return true;
                }
            }
            return false;
        },
        checkPwd() {
            if (this.password && !regExp.pwd.test(this.password)) {
                this.passwordErr = "密码格式不正确";
                return false;
            }
            return true;
        }
    }
}
</script>

<style scoped>

</style>
