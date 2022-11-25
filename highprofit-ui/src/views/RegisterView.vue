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
              <input v-model="phone" type="text" class="form-border user-num" name="phone"
                     placeholder="请输入11位手机号">
              <p class="prompt_num"></p>
              <input v-model="password" type="password" placeholder="请输入6-20位英文和数字混合密码"
                     class="form-border user-pass"
                     autocomplete name="password">
              <p class="prompt_pass"></p>
              <div class="form-yzm form-border">
                <input v-model="code" class="yzm-write" type="text" name="code" placeholder="输入短信验证码">
                <input class="yzm-send" @click="sendCode" type="text" value="获取验证码" id="yzmBtn"
                       readonly="readonly">
              </div>
              <p class="prompt_yan"></p>
            </div>
            <div class="alert-input-agree">
              <input type="checkbox" v-model:checked="agree" class="fa fa-square-o"/>
              我已阅读并同意<a href="javascript:;" target="_blank">《动力金融网注册服务协议》</a>
            </div>
            <div class="alert-input-btn">
              <input type="button" @click="register" :disabled="!agree" class="login-submit" value="注册">
            </div>
          </form>
          <div class="login-skip">
            已有账号？ <a href="login.html" target="_blank">登录</a>
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

export default {
  name: "RegisterView",
  components: {Header, Footer, Rate},
  data() {
    return {
      phone: "",
      password: "",
      code: "",
      agree: false
    }
  },
  methods: {
    sendCode() {
      Vue.axios.get("sms/register/" + this.phone).then(json => console.log(json.data));
    },
    register() {
      let data = {
        phone: this.phone,
        password: this.password,
        code: this.code
      };
      data = qs.stringify(data);
      Vue.axios.post("user/register", data).then(json => {
        if (json.data.code) {
          alert("跳转到登录页面");
        }
      });
    }
  }
}
</script>

<style scoped>

</style>
