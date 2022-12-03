<template>
    <div>
        <Header/>

        <div class="login-content">
            <div class="login-flex">
                <div class="login-left"></div>
                <!--实名认证-->
                <div class="login-box">
                    <h3 class="login-title">实名认证</h3>
                    <form action="" id="renZ_Submit">
                        <div class="alert-input">
                            <input type="text" v-model="name" @blur="checkName" @focus="nameErr = ''"
                                   class="form-border user-name" name="name" placeholder="请输入您的真实姓名">
                            <p class="prompt_name">{{ nameErr }}</p>
                            <input type="text" v-model="idCard" @blur="checkIdCard" @focus="idCardErr = ''"
                                   class="form-border user-sfz" name="sfz" placeholder="请输入15位或18位身份证号">
                            <p class="prompt_sfz">{{ idCardErr }}</p>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="sendVerify" class="login-submit" value="认证">
                        </div>
                    </form>
                    <div class="login-skip">
                        暂不认证？
                        <router-link to="/">跳过</router-link>
                    </div>
                </div>
            </div>
        </div>

        <Footer/>
    </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import regExp from "@/utils/regExp";
import Vue from "vue";

export default {
    name: "VerifyView",
    components: {Header, Footer},
    data() {
        return {
            name: "",
            nameErr: "",
            idCard: "",
            idCardErr: ""
        }
    },
    methods: {
        sendVerify() {
            if (!this.name) {
                this.nameErr = "姓名不能为空";
                alert(this.nameErr);
                return;
            }
            if (!this.idCard) {
                this.idCardErr = "身份证号码不能为空";
                alert(this.idCardErr);
                return;
            }
            if (this.idCardErr !== "") {
                this.idCardErr = "身份证格式不正确";
                alert(this.idCardErr);
                return;
            }
            Vue.axios.post("/user/verify", `name=${this.name}&idCard=${this.idCard}`).then(response => {
            // Vue.axios.post("/user/verify", {name: this.name, idCard: this.idCard}).then(response => {
                if (response.data.code === "1") {
                    alert(response.data.message);
                    sessionStorage.setItem("user", JSON.stringify(response.data.result.user));
                    this.$router.push("/");
                } else if (response.data.code === "2") {
                    alert(response.data.message);
                    this.$router.push("/login")
                } else {
                    alert(response.data.message);
                }
            });
        },
        checkName() {
            if (!this.name) {
                this.nameErr = "姓名不能为空";
            }
        },
        checkIdCard() {
            if (!this.idCard) {
                this.idCardErr = "身份证号码不能为空";
                return;
            }
            if (!regExp.idCard.test(this.idCard)) {
                this.idCardErr = "身份证格式不正确";
            }
        }
    }
}
</script>

<style scoped>

</style>
