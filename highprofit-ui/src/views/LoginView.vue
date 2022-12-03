<template>
    <div>
        <!--公共头部-->
        <Header/>

        <div class="login-content">
            <div class="login-flex">
                <div class="login-left">
                    <h3>加入动力金融网</h3>
                    <p>坐享
                        <Rate/>%
                        历史年化收益
                    </p>
                    <p>平台用户<UserCount/>位 </p>
                    <p>累计成交金额<TotalMoney/>元</p>
                </div>
                <!--登录栏-->
                <div class="login-box">
                    <h3 class="login-title">
                        <a href="javascript:" @click="isPwdLogin = true" :style="{color: isPwdLogin ? '#4fa5d9' : ''}">
                            密码登录
                        </a>
                        <span :style="{color: '#c0c0c0'}">&nbsp; | &nbsp;</span>
                        <a href="javascript:" @click="isPwdLogin = false"
                           :style="{color: !isPwdLogin ? '#4fa5d9' : ''}">
                            短信登录
                        </a>
                    </h3>
                    <!--密码登录-->
                    <div v-if="isPwdLogin">
                        <div class="alert-input">
                            <input v-model="phone" type="text" @blur="checkPhone" @focus="phoneErr = ''"
                                   class="form-border user-num" name="phone" placeholder="请输入手机号">
                            <p class="prompt_num">{{ phoneErr }}</p>
                            <input v-model="password" type="password" @blur="checkPwd" @focus="passwordErr = ''"
                                   placeholder="请输入登录密码" class="form-border user-pass" @keydown.enter="pwdLogin"
                                   autocomplete name="password">
                            <p class="prompt_pass">{{ passwordErr }}</p>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="pwdLogin" class="login-submit" value="登录">
                        </div>
                    </div>
                    <!--验证码登录-->
                    <div v-else>
                        <div class="alert-input">
                            <input v-model="phone" type="text" @blur="checkPhone" @focus="phoneErr = ''"
                                   class="form-border user-num" name="phone" placeholder="请输入手机号">
                            <p class="prompt_num">{{ phoneErr }}</p>
                            <div class="form-yzm form-border">
                                <input v-model="code" @focus="codeErr = ''" class="yzm-write" type="text" name="code"
                                       placeholder="输入短信验证码" @keydown.enter="codeLogin">
                                <input class="yzm-send" @click="sendCode" type="text" :value="sendText" id="yzmBtn"
                                       :disabled="cdFlag" :style="{color: cdFlag ? '#c0c0c0' : ''}" readonly="readonly">
                            </div>
                            <p class="prompt_yan">{{ codeErr }}</p>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="codeLogin" class="login-submit" value="登录">
                        </div>
                    </div>
                    <div class="login-skip">
                        没有账号？
                        <router-link to="/register">注册</router-link>
                    </div>
                </div>
            </div>
        </div>

        <!--公共底部-->
        <Footer/>
    </div>
</template>

<script>
import Rate from "@/components/Rate";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import regExp from "@/utils/regExp";
import Vue from "vue";
import qs from "qs";
import UserCount from "@/components/UserCount";
import TotalMoney from "@/components/TotalMoney";

export default {
    name: "LoginView",
    components: {TotalMoney, UserCount, Rate, Header, Footer},
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
            registered: true, // 手机号是否已注册
            sendText: "获取验证码",
            sendInterval: 60, // 短信发送间隔
            cdFlag: false, // 发送冷却标志
            isPwdLogin: true
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
            if (!this.registered) {
                this.phoneErr = "手机号码尚未注册";
                alert(this.phoneErr);
                return;
            }
            this.cdFlag = true;
            Vue.axios.get("sms/login/" + this.phone).then(response => {
                console.log(response.data);
                this.realCode = response.data + "";
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
        pwdLogin() {
            if (this.phone && this.password) {
                Vue.axios.post("/user/pwdLogin", `phone=${this.phone}&password=${this.password}`).then(response => {
                    if (response.data.code === "1") {
                        // 用sessionStorage会话存储器保存客户端标识(token令牌)
                        sessionStorage.setItem("token", response.data.result.token);
                        // sessionStorage仅支持字符串存储，如果需要存储对象，需要将对象先转换为json字符串、
                        let userStr = JSON.stringify(response.data.result.user);
                        sessionStorage.setItem("user", userStr);

                        // 登录成功，如果用户已经实名认证，跳转到首页，否则跳转到实名认证页面
                        let user = response.data.result.user;
                        if (user.name && user.idCard) {
                            this.$router.push(this.$route.query.redirectUrl || "/");
                        } else {
                            alert("您尚未实名认证，正在为您跳转实名认证页面");
                            this.$router.push("/verify");
                        }
                    } else {
                        alert(response.data.message);
                    }
                });
            }
        },
        codeLogin() {
            if (this.phone === "") {
                this.phoneErr = "手机号码不能为空";
                alert(this.phoneErr);
                return;
            }
            if (this.code === "") {
                this.codeErr = "密码不能为空";
                alert(this.codeErr);
                return;
            }
            if (this.phoneErr !== "") {
                alert(this.phoneErr);
                return;
            }
            if (this.passwordErr !== "") {
                alert(this.passwordErr);
                return;
            }
            Vue.axios.post("/user/codeLogin", `phone=${this.phone}&code=${this.code}`).then(response => {
                if (response.data.code === "1") {
                    // 用sessionStorage会话存储器保存客户端标识(token令牌)
                    sessionStorage.setItem("token", response.data.result.token);
                    // sessionStorage仅支持字符串存储，如果需要存储对象，需要将对象先转换为json字符串、
                    let userStr = JSON.stringify(response.data.result.user);
                    sessionStorage.setItem("user", userStr);

                    // 登录成功，如果用户已经实名认证，跳转到首页，否则跳转到实名认证页面
                    let user = response.data.result.user;
                    if (user.name && user.idCard) {
                        this.$router.push("/");
                    } else {
                        alert("您尚未实名认证，正在为您跳转实名认证页面");
                        this.$router.push("/verify");
                    }
                } else {
                    alert(response.data.message);
                }
            });
        },
        checkPhone() {
            if (this.phone) {
                if (!regExp.phone.test(this.phone)) {
                    this.phoneErr = "手机号码格式不正确";
                } else {
                    Vue.axios.get("/user/check/" + this.phone)
                            .then(response => {
                                if (response.data) {
                                    this.phoneErr = "手机号码尚未注册";
                                    this.registered = false;
                                }
                            });
                }
            }
        },
        checkPwd() {
            if (this.password && !regExp.pwd.test(this.password)) {
                this.passwordErr = "密码格式不正确";
            }
        }
    }
}
</script>

<style scoped>

</style>
