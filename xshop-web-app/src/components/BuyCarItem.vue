<template>
  <div class="shop_buy_car_item" v-show="buyCar.number > 0">
    <div class="shop_buy_car_content">
      <div class="shop_bc_img">
        <img
          :src="buyCar.goods.cover"/>
      </div>
      <div class="shop_bc_content">
        <p>
          <small>{{buyCar.goods.title}}</small>
        </p>
        <p>
          <small>{{buyCar.goods.describe}}</small>
        </p>
        <p class="shop_bc_price">
          <small>￥{{buyCar.goods.saleStatus === 1 ? buyCar.goods.salePrice : buyCar.goods.originalPrice}}</small>
        </p>
      </div>
      <div class="shop_bc_content_end">
        <group>
          <x-number :value="buyCar.number" v-model="number" class="shop_bc_num" width="30px" :min="0"></x-number>
        </group>
        <x-button class="shop_bc_del" @click.native="deleteBuyCar" mini plain type="primary">删除商品</x-button>
      </div>
    </div>
  </div>
</template>

<script>
  import http from '../util/HttpUtil'
  import {Group} from 'vux'

  export default {
    name: "BuyCarItem",
    components: {Group},
    props: ['buyCar'],
    data() {
      return {
        number: this.buyCar.number
      }
    },
    mounted() {

    },
    methods: {
      deleteBuyCar() {

        const that = this
        http.postForm(this, 'buyCar', 'deleteBuyCar', {buyCarId: this.buyCar.id}, function (resp) {
          that.$emit('loadData')
        })
      }
    },
    watch: {
      number: function (newNumber, oldNumber) {
        this.buyCar.number = newNumber;
        const that = this;
        if (newNumber > oldNumber) {
          http.postForm(this, 'buyCar', 'add',
            {
              'token': this.$store.state.user.token,
              'goodsId': this.buyCar.goods.goodsId,
              'number': 1
            },
            function (resp) {
              const dt = resp.data;
              if (dt.code === 0) {

                that.$emit('emitPrice', (that.buyCar.goods.salePrice === 1 ? that.buyCar.goods.salePrice : that.buyCar.goods.originalPrice + that.buyCar.goods.despatchMoney))
              } else {

              }

            }
          )
        }
        if (newNumber < oldNumber) {
          http.postForm(this, 'buyCar', 'minusBuyCar',
            {
              'token': this.$store.state.user.token,
              'goodsId': this.buyCar.goods.goodsId,
              'number': 1
            },
            function (resp) {
              const dt = resp.data;
              if (dt.code === 0) {

                that.$emit('emitPrice', -(that.buyCar.goods.salePrice === 1 ? that.buyCar.goods.salePrice : that.buyCar.goods.originalPrice + that.buyCar.goods.despatchMoney))

              } else {

              }

            }
          )
        }
      }
    }
  }
</script>

<style scoped>
  .vux-number-selector {
    width: 14px;
  }

  .vux-number-input {
    width: 30px !important;
    font-size: 13px;
  }

  .shop_buy_car_content {
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
  }

  .shop_bc_img > img {
    width: 70px;
    height: 70px;
  }

  .shop_bc_img {
    width: 72px;
  }

  .shop_bc_content {
    /*width: 100%;*/
    width: 170px;
    /*padding-left: 10px;*/
  }

  .shop_bc_price {
    color: red;
  }

  .shop_bc_num {
    flex-shrink: 3;
    margin: 0;
    padding: 0;
  }

  .shop_bc_del {
    width: 100%;
  }

  .shop_bc_content_end {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex-wrap: nowrap;
    padding: 2px;
  }

  .shop_buy_car_item {
    margin: 5px;
    padding: 10px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 1px 3px rgba(26, 26, 26, .1);
    box-sizing: border-box;
  }

  .shop_buy_car_content {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    flex-wrap: nowrap;
  }
</style>
