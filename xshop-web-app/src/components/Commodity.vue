<template>
  <div>
    <div class="shop_info">
      <x-header :left-options="{preventGoBack: true}" @on-click-back="goBack()">{{commodiy.title}}</x-header>
      <swiper class="commodity_swiper" :list="pictures" v-model="picturesIndex"></swiper>
      <div class="shop_model">
        <p>{{commodiy.title}}</p>
      </div>
      <div class="shop_model shop_price">
        <!--<x-button class="shop_add_bc" mini plain type="primary" @click.native="showDialog=true">加入购物车</x-button>-->
        <p v-if="commodiy.saleStatus === 1">￥{{commodiy.salePrice}}</p>
        <p v-else>￥{{commodiy.originalPrice}}</p>
      </div>
      <div class="shop_model shop_model_other">
        <span>快递：￥{{commodiy.despatchMoney}}</span>
        <!--<span>月销量：{{commodiy.salesVolume}}</span>-->
        <span>{{commodiy.deliveryPlace}}</span>
      </div>
      <divider>商品详情</divider>
      <div class="shop_price_info" v-html="commodiy.content">
      </div>
    </div>
    <toast v-transfer-dom v-model="showPositionValue" type="text" :time="800" is-show-mask :text="toastMsg"
           :position="position"></toast>
    <div v-transfer-dom>
      <x-dialog v-model="showDialog" :v-show="showDialog" class="dialog-demo" hide-on-blur>
        <div style="padding:15px;">
          <group>
            <x-number title="选择数量" v-model="buyNumber" :min="1"></x-number>
          </group>
          <x-button class="loginBtn" mini plain type="primary" @click.native="addBuyShop()">添加</x-button>
        </div>
      </x-dialog>
    </div>
    <div class="shop_bottom">
      <div class="shop_bottom_group">
        <div class="shop_bottom_item">
          <img src="../assets/logo.png" alt="store-up">
          <span>收藏</span>
        </div>
      </div>
      <div class="shop_buy_group">
        <div class="shop_buy shop_buy_group_item">立即购买</div>
        <div class="shop_add_car shop_buy_group_item" @click="addShoppingCart">加入购物车</div>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    Swiper,
    SwiperItem,
    XNumber,
    XDialog,
    XInput,
    Group
  } from 'vux'
  import http from '../util/HttpUtil'

  export default {
    name: "Commodity",
    data() {
      return {
        picturesIndex: 0,
        toastMsg: '请先登录！',
        showPositionValue: false,
        position: 'bottom',
        pictures: [],
        demo01_index: 0,
        commodiy: {},
        buyNumber: 1,
        showDialog: false,
      }
    },
    methods: {
      goBack() {
        this.$router.push({name: 'home'})
      },
      addShoppingCart: function () {
        // 加入购物车
        console.log("addBuyShop")
        this.$set(this.$data, 'position', 'bottom');

        this.$set(this.$data, 'showPositionValue', true);
        console.log(this.$store.state.user.token)
        if (this.$data.buyNumber < 1) {
          this.$set(this.$data, 'toastMsg', '数量不能少于0');
        }
        if (localStorage.getItem("token") === undefined || localStorage.getItem("token") === '') {
          this.$set(this.$data, 'toastMsg', '请先登录!');
        } else {
          const that = this;
          http.postForm(this, 'buyCar', 'add',
            {'token': this.$store.state.user.token, 'goodsId': this.$route.params.id, 'number': this.$data.buyNumber},
            function (resp) {
              const dt = resp.data;
              if (dt.code === 0) {
                that.$set(that.$data, 'toastMsg', '添加成功！');
                that.$set(that.$data, 'showDialog', false)
              }
            }
          )
        }
      },
      loadGoodsList() {
        const that = this;
        const sendData = 'goodsId=' + this.$route.params.id
        http.post(this, 'goods', 'info', sendData, function (resp) {
          const data = resp.data.data
          console.log(data)

          that.$set(that.$data, 'commodiy', data)

          const pictures = JSON.parse(data.pictures);
          const ps = []
          for (var i = 0; i < pictures.length; i++) {
            var p = {
              img: '',
              url: '',
              title: '',
              fallbackImg: '',
            }
            p['img'] = pictures[i].url
            p['url'] = 'javascript:'
            p['title'] = ''
            p['fallbackImg'] = 'https://ws3.sinaimg.cn/large/006tNbRwly1fw5rgy9nh5j30b40b4gli.jpg'
            ps.push(p)
          }
          console.log(ps)
          that.$set(that.$data, 'pictures', ps)
        })
      },
    },
    mounted() {
      this.loadGoodsList()

      if (this.$store.state.user.token === undefined ||
        this.$store.state.user.token === null ||
        this.$store.state.user.token === "") {
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

    },
    components: {
      XNumber,
      Swiper,
      SwiperItem,
      XDialog,
      XInput,
      Group
    }
  }
</script>

<style>
  body {
    background-color: #fff !important;
  }

  .shop_bottom_item {
    display: flex;
    flex-flow: column nowrap;
    font-size: 12px;
    text-align: center;
    width: 30px;
  }

  .shop_bottom_group {
    width: 200px;
    height: 100%;
    margin-left: 10px;
  }

  .shop_bottom_item > img {
    width: 25px;
    height: 25px;
    margin-bottom: 3px;
  }

  .shop_buy {
    border: 1px solid #FA9500;
    border-top-left-radius: 25px;
    border-bottom-left-radius: 25px;
    background-color: #FA9500;
  }

  .shop_add_car {
    border: 1px solid #FA2235;
    border-top-right-radius: 25px;
    border-bottom-right-radius: 25px;
    background-color: #FA2235;
  }

  .shop_buy_group_item {
    flex: 1;
    text-align: center;
    color: #fff;
    font-size: 12px;
  }

  .shop_bottom {
    padding: 10px 0;
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 40px;
    background: #fff;
    box-shadow: -1px -1px 1px #ccc;
  }

  .shop_info {
    margin-bottom: 50px;
    background-color: #fff;
  }

  .shop_buy_group {
    display: flex;
    flex-flow: row nowrap;
    width: 200px;
    height: 30px;
    line-height: 30px;
    position: absolute;
    right: 10px;
    bottom: 15px;
  }

  .shop_price_info > p > img {
    width: 100% !important;
  }

  .commodity_swiper {
    height: 400px
  }

  .commodity_swiper > .vux-swiper {
    height: 400px !important;
  }

  .shop_model {
    padding: 5px;
    font-size: .90rem;
    background-color: #fff;
  }

  .shop_price {
    color: #FF0036;
    font-size: 1.3rem;
  }

  .shop_add_bc {
    float: right;
  }

  .shop_price_info > img {
    width: 100%;
  }

  .shop_price_info {
    padding: 10px;
  }

  .shop_model_other {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    font-size: .75rem;
    color: #888888;
  }

  .shop_price > .weui-cell {
    margin: 0;
    padding: 0;
    padding-bottom: 5px;
  }
</style>
<style scoped>

</style>
