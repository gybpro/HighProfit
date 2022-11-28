<template>
    <div>
        <!--公共头部-->
        <Header/>

        <div class="login-content">
            <div class="login-flex">
                <div class="login-left">
                    <h3>加入动力金融网</h3>
                    <p>坐享
                        <Rate/>
                        历史年化收益
                    </p>
                    <p>平台用户<span>100000+</span>位 </p>
                    <p>累计成交金额<span>15000+</span>万元</p>
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
                                   placeholder="请输入登录密码" class="form-border user-pass"
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
                                <input v-model="code" class="yzm-write" type="text" name="code"
                                       placeholder="输入短信验证码">
                                <input class="yzm-send" @click="sendCode" type="text" :value="sendText" id="yzmBtn"
                                       :disabled="cdFlag" :style="{color: cdFlag ? '#c0c0c0' : ''}" readonly="readonly">
                            </div>
                            <p class="prompt_yan" @focus="codeErr = ''">{{ codeErr }}</p>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="codeLogin" class="login-submit" value="登录">
                        </div>
                    </div>
                    <div class="login-skip">
                        没有账号？ <router-link to="/register">注册</router-link>
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

export default {
    name: "LoginView",
    components: {Rate, Header, Footer},
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
            Vue.axios.get("sms/login/" + this.phone).then(json => {
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
        pwdLogin() {
            if (this.phone && this.password) {
                Vue.axios.post("/user/pwdLogin", `phone=${this.phone}&password=${this.password}`).then(json => {
                    if (json.data.code === "1") {
                        // 保存客户端标识
                        // 登录成功，如果用户已经实名认证，跳转到首页，否则跳转到实名认证页面
                        let user = json.data.result;
                        if (user.name && user.idCard) {
                            this.$router.push("/index");
                        } else {
                            this.$router.push("/auth");
                        }
                    } else {
                        alert(json.data.message);
                    }
                });
            }
        },
        codeLogin() {

        },
        checkPhone() {
            if (this.phone) {
                if (!regExp.phone.test(this.phone)) {
                    this.phoneErr = "手机号码格式不正确";
                } else {
                    Vue.axios.get("/user/check/" + this.phone)
                            .then(json => {
                                if (json.data) {
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
