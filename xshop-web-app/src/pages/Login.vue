<template>
  <div>
    <x-header :left-options="{showBack: true}">用户登录</x-header>
    <div class="login-logo">
      <img src="../assets/logo.png" alt="logo">
    </div>
    <div>
      <group>
        <x-input title="用户名" v-model="login.phone"></x-input>
        <x-input title="密 码" type="password" v-model="login.password"></x-input>
      </group>
      <div style="padding: 10px;">
        <x-button type="primary" @click.native="loginDo">登录</x-button>
      </div>
    </div>
    <div class="login-bottom">

    </div>
  </div>
</template>

<script>
  import {XInput, Group} from 'vux'
  import http from "../util/HttpUtil";

  export default {
    name: "Login",
    components: {XInput, Group},
    data() {
      return {
        login: {
          phone: '',
          password: '',
          loginMethod: 'password',
          smsCode: ''
        },
        user: {}
      }
    },
    methods: {
      loginDo() {
        const that = this;
        if (this.login.loginMethod === "password"
          && (this.login.phone === "" || this.login.password === "")) {
          that.$vux.toast.show({
            type: "warn",
            text: '用户名或密码不能为空'
          });
          return;
        }
        if (this.login.loginMethod === "smsCode"
          && (this.login.phone === "" || this.login.smsCode === "")) {
          that.$vux.toast.show({
            type: "warn",
            text: '用户名或验证码不能为空'
          });
          return;
        }

        if (this.loginMethod === "password") {
          that.login.password = "";
        }
        if (this.loginMethod === "smsCode") {
          that.login.smsCode = "";
        }

        http.postForm(this, "user", "login", this.login, function (resp) {

          const dt = resp.data;
          if (dt.code === 0) {
            const user = dt.data;
            that.$store.commit("loginSuccess", user);
            that.user = user;
            that.$vux.toast.show({
              type: "success",
              text: '登录成功'
            });
            localStorage.setItem("token", user.token);

            that.$router.push({name: that.$route.params.from || "home"})
          } else {
            that.$vux.toast.show({
              type: "warn",
              text: dt.msg,
            })
          }
        });
      },

      /**
       * 发送短信验证码
       */
      sendLoginCode() {
        if (this.$data.nextSendTime > 0) {
          return;
        }
        if (
          this.$data.login.phone === "" ||
          this.$data.login.phone === undefined
        ) {
          this.$set(this.$data, "showPositionValue", true);
          this.$set(this.$data, "toastValue", "请输入手机号");
          return;
        }
        const that = this;
        http.postForm(
          this,
          "user",
          "sendLoginCode",
          {phoneNumber: this.$data.login.phone},
          function (resp) {
            if (resp.data.code === 0) {
              that.$set(that.$data, "showPositionValue", true);
              that.$set(that.$data, "toastValue", "发送验证码成功！");
              that.$set(that.$data, "nextSendTime", 60);
            } else {
              that.$set(that.$data, "showPositionValue", true);
              that.$set(that.$data, "toastValue", resp.data.msg);
            }
          }
        );
      },
    }
  }
</script>

<style scoped>
  .login-logo > img {
    width: 120px;
    height: 120px;
  }

  .login-logo {
    text-align: center;
    padding-top: 2rem;
    background-color: #fff;
    padding-bottom: 20px;
  }
</style>
