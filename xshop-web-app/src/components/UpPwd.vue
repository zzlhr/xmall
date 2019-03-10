<template>
  <div>
    <x-header :left-options="{showBack: true}">修改密码</x-header>

    <group>
      <x-input title="验证码" v-model="upPwdForm.smsCode"></x-input>
    </group>
    <group style="margin: 10px">
      <x-button type="primary" v-show="nextSendTime<=0" @click.native="sendUpPwdSMS()">{{nextSendTime <= 0 ? '发送验证码' : nextSendTime}}</x-button>
    </group>
    <group>
      <x-input type="password" title="新密码" v-model="upPwdForm.newPassword"></x-input>
    </group>
    <group style="margin: 10px">
      <x-button type="primary" action-type="button" @click.native="upPwd">修改密码</x-button>
    </group>
    <tabbar>
      <tabbar-item selected link="/home">
        <img slot="icon" src="../assets/home.png">
        <span slot="label">首页</span>
      </tabbar-item>
      <tabbar-item link="/class">
        <img slot="icon" src="../assets/class.png">
        <span slot="label">分类</span>
      </tabbar-item>
      <tabbar-item link="/shoppingCart">
        <img slot="icon" src="../assets/gwc.png">
        <span slot="label">购物车</span>
      </tabbar-item>
      <tabbar-item selected link="/my">
        <img slot="icon" src="../assets/my.png">
        <span slot="label">我的</span>
      </tabbar-item>
    </tabbar>
    <toast v-transfer-dom v-model="showPositionValue" type="text" :time="1000" is-show-mask :text="toastMsg"
           :position="position"></toast>
  </div>
</template>

<script>
  import {InlineXNumber} from 'vux'
  import {XInput} from 'vux'
  import XHr from "vux/src/components/x-hr/index";
  import http from '../util/HttpUtil'
  import BuyCarItem from "./BuyCarItem";
  import {Popover, Group, XButton} from 'vux'

  export default {
    name: "UpPwd",
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
        upPwdForm: {
          token: this.$store.state.user.token,
          smsCode: '',
          newPassword: '',
        },
        nextSendTime:0,

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
        this.nextSendTime --;
      },1000)
    },
    methods: {
      sendUpPwdSMS() {
        const that = this;
        http.postForm(this, 'user', 'sendUpPwdCode', {token: localStorage.getItem("token")}, function (resp) {
          const dt = resp.data;
          console.log(dt)
          that.$set(that.$data, 'showPositionValue', true)
          if (dt.code === 0){
            that.$set(that.$data, 'toastMsg', '发送成功！');
          }else {
            that.$set(that.$data, 'toastMsg', dt.msg);
          }
        })
      },
      upPwd() {
        const that = this;
        http.postForm(this, 'user', 'upPassword', this.$data.upPwdForm, function (resp) {
          const dt = resp.data;
          console.log(dt)
          that.$set(that.$data, 'showPositionValue', true)
          if (dt.code === 0) {
            that.$set(that.$data, 'toastMsg', '修改密码成功！');
          } else {
            that.$set(that.$data, 'toastMsg', dt.msg);
          }
        })
      },
      createOrder() {
        // if (buyCars.size() <= 0){
        //
        // }
        const that = this;
        http.postForm(this, 'order', 'createOrder',
          {'token': this.$store.state.user.token},
          function (resp) {
            console.log("创建订单")
            console.table(resp)
            const dt = resp.data;
            console.log(dt.msg)
            that.$set(that.$data, 'position', 'bottom');
            that.$set(that.$data, 'showPositionValue', true);
            if (dt.code === 0) {
              that.$set(that.$data, 'toastMsg', '下单成功！');
              that.loadData()
            } else {
              that.$set(that.$data, 'toastMsg', dt.msg);
            }

          }
        )

      },
      addBuyShop: function (goodsId) {
        // if (this.$store.state.user == null){
        //   this.$set(this.$data, 'showPositionValue', true);
        //   this.$set(this.$data, 'toastMsg', '请先登录!');
        //
        // }
        this.$set(this.$data, 'position', 'bottom');

        this.$set(this.$data, 'showPositionValue', true);
        console.log(this.$store.state.user.token)
        if (this.$store.state.user.token === undefined || this.$store.state.user.token === '') {
          this.$set(this.$data, 'toastMsg', '请先登录!');
        } else {
          const that = this;
          http.postForm(this, 'buyCar', 'add',
            {'token': this.$store.state.user.token, 'goodsId': goodsId, 'number': 1},
            function (resp) {
              const dt = resp.data;
              if (dt.code === 0) {
                that.$set(that.$data, 'toastMsg', '添加成功！');

              }

            }
          )
        }
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
