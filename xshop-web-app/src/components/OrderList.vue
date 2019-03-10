<template>
  <div>
    <div class="scrollerWrap">
      <x-header :left-options="{showBack: true}" v-transfer-dom>我的订单</x-header>

      <tab v-transfer-dom badge-background="#0084ff" bar-active-color="#0084ff">
        <tab-item selected @on-item-click="onItemClick">全部订单</tab-item>
        <tab-item @on-item-click="onItemClick">未收货</tab-item>
        <tab-item @on-item-click="onItemClick">已收货</tab-item>
      </tab>
      <scroller noDataText="----到底了----"
                :on-refresh="refresh"
                ref="scroll" style="margin-top: 44px;"
                :on-infinite="loadData"
      >
        <main class="scroller_main">
          <div class="shop_my_order_list" v-for="(order, index) in orders" :key="index"
               v-if="order.orderId !== undefined">
            <div class="shop_my_order">
              <p class="shop_my_order_id">订单编号：{{order.orderId}}</p>
              <x-hr class="shop_my_order_hr"></x-hr>
              <div class="shop_my_order_item" v-for="(item, i) in (order.orderInfoVOS)" :key="i">
                <div class="shop_my_order_img">
                  <img :src="item.goods.cover"/>
                </div>
                <div class="shop_my_order_content">
                  <div class="shop_my_order_content_title">
                    <span>{{item.goods.title}}</span>
                  </div>
                  <div class="shop_my_order_content_price">
                    <span class="shop_my_order_content_price_buy">￥{{item.transactionPrice}}</span>
                    <span class="shop_my_order_content_price_original">￥<s>{{item.goods.originalPrice}}</s></span>
                    <span class="shop_my_order_content_num">x{{item.number}}</span>
                  </div>
                </div>
              </div>

              <x-hr/>
              <p class="order_content_price_count"><span :class="order.status === 0 ? 'red':'success'">{{order.status === 2 ? '已签收':'未签收'}}</span>
                <span style="margin-right: 10px;">共计{{order.orderNumber}}件商品 合计￥ {{order.orderAmount + order.despatchMoney}}(含运费 ￥ {{order.despatchMoney}})</span>
                <x-button mini style="background-color: #0084ff; color: #fff;" v-if="order.status !== 2" @click.native="receipt(order.orderId)">签收</x-button></p>

            </div>
          </div>
          <toast v-model="toastShow" :text="toastText" position="bottom" v-transfer-dom></toast>

        </main>
      </scroller>
    </div>
  </div>

</template>

<script>
  import http from "../util/HttpUtil";
  import {
    XHr,
    XButton,
    Tab,
    TabItem,
    Toast
  } from 'vux'
  import { XDialog } from 'vux'
  export default {
    name: "OrderList",
    components: {XHr, XButton, Tab, TabItem,Toast,XDialog},
    data() {
      return {
        toastShow: false,
        toastText: '',
        sendForm: {
          orderStatus: -1,
          page: 1,
          pageSize: 10,
        },
        orders: []
      }
    },
    created() {
      if (this.$store.state.user.token === undefined || this.$store.state.user === null || this.$store.state.user === "") {
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
      }
    },
    methods: {
      receipt(orderId){
        console.log(orderId);
        const that = this;
        http.postForm(this, 'order', 'receipt', {token: this.$store.state.user.token, orderId: orderId}, function (data) {
          const dt = data.data;
          that.$set(that.$data, 'toastShow', true)
          console.log(dt)
          if (dt.code !== 0) {
            that.$set(that.$data, 'toastText', '签收失败！')
          }else {
            that.$set(that.$data, 'toastText', '签收成功！')
            that.refresh()
          }
        })
      },
      onItemClick(index){
        this.$set(this.$data.sendForm, 'orderStatus', index-1)
        this.$set(this.$data.sendForm, 'page', 1)
        this.$set(this.$data, 'orders', [])
        this.loadData()
        console.log(index)
      },
      refresh() {
        this.$set(this.$data.sendForm, 'page', 1);
        this.$set(this.$data, 'orders', [])
        this.loadData()
      },
      loadData() {
        console.log("loadData")
        const that = this
        const send = this.sendForm;
        const page = send.page
        const token = localStorage.getItem('token');

        send.token = token;

        http.postForm(this, 'order', 'orderList', send, function (resp) {
          console.log(resp)
          if (page === 1){
            that.$set(that.$data, 'orders', [])
          }
          if (resp.data.code === 0) {
            const dt = resp.data.data
            console.log(that.orders.concat(dt))

            for (let i = 0; i < dt.length; i++) {
              dt[i].orderNumber = dt[i].orderInfoVOS.length;
              that.orders.push(dt[i])
            }

            that.$set(that.$data.sendForm, 'page', that.$data.sendForm.page + 1);
            that.$refs.scroll.finishPullToRefresh()
            const hideSpinner = dt.length < 10;
            that.$refs.scroll.finishInfinite(hideSpinner)
            if (page === 1){
              that.$set(that.$data.sendForm, 'page', 2)
            }
          }


        })

      }
    }
  }
</script>

<style scoped>
  .red {
    color: red;
  }
  .vux-tab .vux-tab-item.vux-tab-selected {
    color: #0084ff;
    border-bottom: 3px solid #0084ff;
  }
  .vux-tab-ink-bar {
    background-color: #0084ff !important;
  }
  .success {
    color: #0bb20c;
  }

  .shop_my_order {
    background-color: #fff;
    padding: 5px 10px;
    margin-top: 10px;
  }

  .no_order {
    text-align: center;
  }

  .shop_my_header {
    display: flex;
    justify-content: center;
  }

  .shop_my_header > img {
    width: 100px;
    height: 100px;
  }

  .shop_my_order_item {
    height: 80px;
    background-color: #EEEEEE;
    display: flex;
    flex-direction: row;
    margin: 10px;
  }

  .shop_my_order_img > img {
    width: 100px;
    height: 70px;
  }

  .shop_my_order_img {
    padding: 5px;
  }

  .shop_my_order_content {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    width: 80%;
    color: #999999;
  }

  .shop_my_order_id {
    color: #999999;
    font-size: .75rem;
  }

  .shop_my_order_content_price {
    display: flex;
    flex-direction: column;
    padding-right: 5px;
  }

  .shop_my_order_content_title {
    color: #222222;
  }

  .shop_my_order_content_price {
    text-align: right;

  }

  .shop_my_order_content_price_original {
    font-size: .90rem;
  }

  .shop_my_order_content_price_buy {
    color: red;
  }

  .shop_my_order_content_num {
    font-size: .75rem;
  }

  .order_content_price_count {
    font-size: .75rem;
    text-align: right;
  }

  .cell-class {
    margin-top: 20px;
  }

  .shop_my_order_hr {
    padding-top: 1px;
  }

</style>
