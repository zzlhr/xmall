<template>

  <div>
    <x-header :left-options="{showBack: true}">商品分类</x-header>
    <group-title>商品列表</group-title>
    <scroller class="scroller_main" use-pullup
              lock-x height="-118"
              @on-pullup-loading="loadGoodsList"
              ref="scrollerBottom">
      <div>
        <flexbox :gutter="0" wrap="wrap">
          <flexbox-item :span="1/2" v-for="(item, index) in shopList" :key="index">
            <div class="flex-demo">
              <div class="shop_item" @click="goCommodity(item.goodsId)">
                <img :src="item.cover"/>
                <div class="shop_text">
                  <p class="shop_title">{{item.title}}</p>
                  <p class="shop_price">￥{{item.saleStatus === 1 ? item.salePrice : item.originalPrice}}</p>
                  <p class="shop_describe">{{item.describe}}</p>
                </div>
              </div>
            </div>
          </flexbox-item>
        </flexbox>
      </div>
    </scroller>

    <tabbar>
      <tabbar-item link="/home">
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
      <tabbar-item link="/my">
        <img slot="icon" src="../assets/my.png">
        <span slot="label">我的</span>
      </tabbar-item>
    </tabbar>
  </div>
</template>

<script>
  import {Swiper, SwiperItem, Grid, GridItem, GroupTitle, Flexbox, FlexboxItem,} from 'vux'
  import http from '../util/HttpUtil'
  import { Scroller } from 'vux'

  export default {
    name: "ClassCommodity",
    components: {
      Swiper,
      SwiperItem,
      Grid,
      GridItem,
      Flexbox,
      FlexboxItem,
      GroupTitle,
      Scroller
    },
    methods: {

      loadGoodsList() {
        const that = this;
        http.postForm(this, 'goods', 'list', this.goodsParams, function (resp) {
          const data = resp.data.data
          that.$set(that.$data, 'shopList', that.shopList.concat(data.arr))
          that.$set(that.$data.goodsParams, 'page', that.$data.goodsParams.page + 1)
        })
      },

      goCommodity(id) {
        this.$router.push({
          name: 'Commodity',
          params: {
            id: id
          }
        })
      }
    },
    mounted() {
      this.loadGoodsList();
      if (this.$store.state.user.token=== undefined ||this.$store.state.user.token === null || this.$store.state.user.token === ""){
        const token = localStorage.getItem('token');
        if (token !== undefined){
          const sendData = {
            'token': token
          }
          const that = this;
          http.postForm(this, 'user', 'tokenUse', sendData, function(data){
            const dt = data.data;
            if(dt.code !== 0){
              return;
            }
            const user = dt.data;
            that.$set(that.$data, 'user', user);
            that.$store.commit('loginSuccess', user);
          })
        }
      } else{

      }
    },
    data() {
      return {
        shopList: [],
        swiperItemIndex: 1,
        goodsParams: {
          cid: this.$route.params.cid,
          page: 1,
        }
      }
    }
  }
</script>

<style>
  .home_swiper > .vux-swiper {
    height: 190px !important;
  }
  .scroller_main{
    flex: 1;
  }

  .shop_item {
    padding: 1px;
    margin-bottom: 10px;
  }

  .shop_item > img {
    width: 100%;
    height: 12rem;
  }

  .shop_title {
    color: #0084ff;
  }

  .shop_text {
    padding-left: 5px;
  }

  .shop_price {
    color: #FF4825;
  }

  .shop_describe {
    color: #B4B4B4;
    font-size: 13px;
  }
</style>
