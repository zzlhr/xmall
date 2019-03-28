<template>
  <div>
    <x-header :left-options="{showBack: true}">修改密码</x-header>
    <group>
      <x-input title="手机号" v-model="registForm.phone"></x-input>
    </group>
    <group style="margin: 10px">
      <x-button type="primary" action-type="button" @click.native="next">下一步</x-button>
    </group>
    <toast v-transfer-dom v-model="showPositionValue" width="10em" type="text" :time="1000" is-show-mask
           :text="toastMsg"
           :position="position"></toast>
  </div>
</template>

<script>
  import {InlineXNumber} from 'vux'
  import {XInput} from 'vux'
  import XHr from "vux/src/components/x-hr/index";
  import http from '../util/HttpUtil'
  import BuyCarItem from "../components/BuyCarItem";
  import {Popover, Group, XButton} from 'vux'

  export default {
    name: "Regist",
    components: {
      BuyCarItem,
      XHr,
      InlineXNumber,
      Popover,
      XInput,
      Group,
      XButton
    },
    data() {
      return {
        buyCars: [],
        aggregate: 0,
        totalNumber: 0,
        position: 'bottom',
        showPositionValue: false,
        toastMsg: '',
        nextSendTime: 0,
        registForm: {
          username: '',
          phone: '',
          password: '',
          code: '',
        }
      }

    },
    mounted() {

      if (this.$store.state.user.token === undefined || this.$store.state.user.token === null || this.$store.state.user.token === "") {
        const token = localStorage.getItem('token');
        if (token !== undefined) {
          const sendData = {
            'token': token
          }
          const that = this;
          http.postForm(this, 'user', 'tokenUse', sendData, function (data) {
            const dt = data.data;
            if (dt.code !== 0) {
              return;
            }
            const user = dt.data;
            that.$set(that.$data, 'user', user);
            that.$store.commit('loginSuccess', user);
          })
        }
      } else {

      }
      setInterval(() => {
        this.nextSendTime--;
      }, 1000)
    },
    methods: {
      next() {
        const reg = /^1[0-9]{10}$/; //验证规则

        const phoneNum = this.$data.registForm.phone;//手机号码
        const that = this
        if (!reg.test(phoneNum)) {
          this.$set(this.$data, 'showPositionValue', true)
          this.$set(this.$data, 'toastMsg', "请输入正确手机号！")
        } else {
          // 验证手机号是否注册
          http.postForm(this, 'user', 'phoneIsExist', {phone: phoneNum}, function (resp) {
            const dt = resp.data
            //
            if (dt.code !== 0) {
              that.$set(that.$data, 'showPositionValue', true)
              that.$set(that.$data, 'toastMsg', dt.msg)
              return;
            }
            that.sendAuthCode();
          })


        }
      },
      sendAuthCode() {
        const reg = /^1[0-9]{10}$/; //验证规则
        const phoneNum = this.$data.registForm.phone;//手机号码
        if (!reg.test(phoneNum)) {
          this.$set(this.$data, 'showPositionValue', true)
          this.$set(this.$data, 'toastMsg', "请输入正确手机号！")
        } else {
          const that = this;
          http.postForm(this, 'user', 'sendCode', {phoneNumber: this.$data.registForm.phone}, function (resp) {
            const dt = resp.data;
            that.$set(that.$data, 'showPositionValue', true)
            if (dt.code === 0) {
              that.$set(that.$data, 'toastMsg', '发送验证码成功，60秒后可重新发送！');
              that.$router.push({name: 'Regist1', params:{phoneNumber: phoneNum}})
            } else {
              that.$set(that.$data, 'toastMsg', dt.msg);
            }
          })
        }
      },
      regist() {
        const that = this;
        http.postForm(this, 'user', 'addUser', this.$data.registForm, function (resp) {
          const dt = resp.data;

          that.$set(that.$data, 'showPositionValue', true)
          if (dt.code === 0) {
            that.$set(that.$data, 'toastMsg', '注册成功！');
          } else {
            that.$set(that.$data, 'toastMsg', dt.msg);
          }
        })
      },
    }
  }
</script>

<style scoped>
  .shop_buy_car {
    width: 100%;
  }

  .shop_bc_total_prices {
    font-size: .75rem;
    color: red;
    text-align: right;
    margin-bottom: 10px;
  }

  .shop_bc_hr {
    margin-bottom: 1px;
  }

  .shop_bc_box {
    margin: 5px;
  }
</style>
