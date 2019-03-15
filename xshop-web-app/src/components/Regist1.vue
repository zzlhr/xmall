<template>
  <div>
    <x-header :left-options="{showBack: true}">修改密码</x-header>
    <group>
      <x-input title="验证码" v-model="registForm.code"></x-input>
    </group>

    <div style="margin: 10px; text-align: right;">
      <a @click="sendAuthCode">重新发送({{nextSendTime}})</a>
    </div>
    <group style="margin: 10px">
      <x-button type="primary" action-type="button" @click.native="regist">注册</x-button>
    </group>
    <!--<tabbar>-->
    <!--<tabbar-item selected link="/home">-->
    <!--<img slot="icon" src="../assets/home.png">-->
    <!--<span slot="label">首页</span>-->
    <!--</tabbar-item>-->
    <!--<tabbar-item link="/class">-->
    <!--<img slot="icon" src="../assets/class.png">-->
    <!--<span slot="label">分类</span>-->
    <!--</tabbar-item>-->
    <!--<tabbar-item selected link="/shoppingCart">-->
    <!--<img slot="icon" src="../assets/gwc.png">-->
    <!--<span slot="label">购物车</span>-->
    <!--</tabbar-item>-->
    <!--<tabbar-item link="/my">-->
    <!--<img slot="icon" src="../assets/my.png">-->
    <!--<span slot="label">我的</span>-->
    <!--</tabbar-item>-->
    <!--</tabbar>-->
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
  import BuyCarItem from "./BuyCarItem";
  import {Popover, Group, XButton} from 'vux'

  export default {
    name: "Regist1",
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
        nextSendTime: 60,
        registForm: {
          username: '',
          phone: '',
          password: '',
          code: '',
        }
      }

    },
    mounted() {

      // 获取传入手机号
      const phoneNumber = this.$route.params.phoneNumber
      this.$set(this.$data.registForm, 'phone', phoneNumber)
      this.$set(this.$data.registForm, 'username', phoneNumber)

      setInterval(() => {
        this.nextSendTime--;
      }, 1000)
    },
    methods: {
      next() {
        var reg = /^1[0-9]{10}$/; //验证规则

        var phoneNum = this.$data.registForm.phone;//手机号码

        if (!reg.test(phoneNum)) {
          this.$set(this.$data, 'showPositionValue', true)
          this.$set(this.$data, 'toastMsg', "请输入正确手机号！")
        } else {
          this.$router.push({name: 'Regist1'})
        }
      },
      sendAuthCode() {
        if (this.$data.registForm.phone === "" || this.$data.registForm.phone.length < 11) {
          this.$set(this.$data, 'showPositionValue', true)
          this.$set(this.$data, 'toastMsg', '手机号不规范！');
        } else {
          const that = this;
          http.postForm(this, 'user', 'sendCode', {phoneNumber: this.$data.registForm.phone}, function (resp) {
            const dt = resp.data;
            that.$set(that.$data, 'showPositionValue', true)
            if (dt.code === 0) {
              that.$set(that.$data, 'toastMsg', '发送验证码成功！');
              that.$set(that.$data, 'nextSendTime', 60);
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
            const data = dt.data;
            localStorage.setItem("token", data.token)
            that.$router.push({name: 'my'})
          } else {
            that.$set(that.$data, 'toastMsg', dt.msg);
          }
        })
      },
      createOrder() {
        const that = this;
        http.postForm(this, 'order', 'createOrder',
          {'token': this.$store.state.user.token},
          function (resp) {

            console.table(resp)
            const dt = resp.data;

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
