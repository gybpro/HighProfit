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
                                <input class="yzm-send" @click="sendCode" type="text" value="获取验证码" id="yzmBtn"
                                       readonly="readonly">
                            </div>
                            <p class="prompt_yan">{{ codeErr }}</p>
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
                        已有账号？ <a href="" target="_blank">登录</a>
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
            realCode: ""
        }
    },
    methods: {
        sendCode() {
            Vue.axios.get("sms/register/" + this.phone).then(json => {
                console.log(json.data);
                this.realCode = json.data + "";
            });
        },
        register() {
            if (this.phone === "") {
                alert("手机号码不能为空");
                return;
            }
            if (this.password === "") {
                alert("密码不能为空");
                return;
            }
            if (this.code === "") {
                alert("验证码不能为空");
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
                if (this.phoneErr !== "") {
                    alert(this.phoneErr);
                    return;
                }
                if (this.passwordErr !== "") {
                    alert(this.passwordErr);
                    return;
                }
                if (this.code === "") {
                    alert("验证码不能为空");
                    return;
                }
                if (this.code !== this.realCode) {
                    alert("验证码错误");
                    return;
                }
                if (json.data.code === "1") {
                    alert("跳转到登录页面");
                }
            });
        },
        checkPhone() {
            if (this.phone) {
                if (!regExp.phone.test(this.phone)) {
                    this.phoneErr = "手机号码格式不正确";
                } else {
                    Vue.axios.get("/user/check/" + this.phone)
                            .then(json => {
                                if (!json.data) {
                                    this.phoneErr = "手机号码已被注册";
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
