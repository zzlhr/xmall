<template>
    <div>
        <x-header :left-options="{showBack: false}">购物车</x-header>
        <div class="shop_buy_car">
            <!--<BuyCarItem v-for="(buyCar,index) in buyCars" :key="index" @loadData="loadData" @emitPrice="emitPrice"-->
            <!--:buyCar="buyCar"></BuyCarItem>-->
            <div class="shop_buy_car_item" v-for="(buyCar,index) in buyCars" :key="index">
                <check-icon class="shop_buy_car_check" :value.sync="buyCar.selected"></check-icon>
                <img class="shop_buy_car_img" :src="buyCar.goods.cover" alt="cover">
                <div class="shop_buy_car_right">
                    <div class="shop_buy_car_title">
                        <span>{{buyCar.goods.title}}</span>
                    </div>
                    <div>
                        <span class="shop_buy_car_price">￥{{buyCar.goods.saleStatus === 1 ? buyCar.goods.salePrice
                            : buyCar.goods.originalPrice}}</span>
                        <div class="shop_buy_car_num_tool">
                            <div class="shop_buy_car_num_tool_sub" @click="minus(buyCar)">
                                <font-awesome-icon icon="minus"></font-awesome-icon>
                            </div>
                            <div class="shop_buy_car_num_tool_num">{{buyCar.number}}</div>
                            <div class="shop_buy_car_num_tool_add" @click="add(buyCar)">
                                <font-awesome-icon icon="plus"></font-awesome-icon>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="shop_bc_box" v-show="totalNumber > 0 && aggregate > 0">
            <x-hr class="shop_bc_hr"></x-hr>
            <p class="shop_bc_total_prices">合计：￥ {{aggregate}}(包含配送费)</p>
            <x-button class="shop_send_order" type="primary" @click.native="createOrder">下单</x-button>
        </div>
        <popover placement="top" style="margin: 20px;" v-show="totalNumber <= 0 || aggregate <= 0">
            <p style="text-align:center;">
                <span style="vertical-align:middle;display:inline-block;font-size:14px;">空空如也！</span>
            </p>
        </popover>


        <shop-tabbar selected="shoppingCart"></shop-tabbar>

        <toast v-transfer-dom v-model="showPositionValue" type="text" :time="800" is-show-mask :text="toastMsg"
               :position="position"></toast>
        <div v-transfer-dom>
            <x-dialog v-model="selectAddressShow" class="dialog-demo" hide-on-blur>
                <div>
                    <h2>请选择收货地址</h2>
                    <group title="请选择收货地址">
                        <radio :options="addressOption" v-model="addressSelect"></radio>
                    </group>
                    <div style="padding: 5px">
                        <p style="color: red" v-show="addressOption.length > 0">注意:县城地区因距离偏远，运费将加倍计算（当前价格没有加倍），请见谅！</p>
                        <x-button type="primary" @click.native="sendOrder" v-show="addressOption.length > 0">确定
                        </x-button>
                        <p style="color: red" v-show="addressOption.length === 0">您还没有添加收货地址请先添加！</p>
                        <x-button type="primary" @click.native="$router.push({name:'Address'})"
                                  v-show="addressOption.length === 0">
                            去添加
                        </x-button>

                    </div>
                </div>
            </x-dialog>
        </div>
    </div>
</template>

<script>
    import {InlineXNumber} from 'vux'
    import XHr from "vux/src/components/x-hr/index";
    import http from '../util/HttpUtil'
    import BuyCarItem from "./BuyCarItem";
    import {Popover} from 'vux'
    import {XDialog} from 'vux'
    import {Radio, Card, Group} from 'vux'
    import ShopTabbar from './ShopTabbar'
    import {CheckIcon} from 'vux'

    export default {
        name: "ShoppingCart",
        components: {
            BuyCarItem,
            XHr,
            InlineXNumber,
            Popover,
            XDialog,
            Radio,
            Card,
            Group,
            ShopTabbar,
            CheckIcon
        },
        data() {
            return {
                addressOption: [],
                addressSelect: '',
                selectAddressShow: false,
                buyCars: [],
                aggregate: 0,
                totalNumber: 0,
                position: 'bottom',
                showPositionValue: false,
                toastMsg: '',
                address: []
            }
        },
        watch: {
            buyCars: {
                handler: function (vals, oldVals) {
                    let aggregate = 0;
                    let totalNumber = 0;
                    for (let i = 0; i < vals.length; i++) {
                        console.log(vals[i])
                        totalNumber += vals[i].number;
                        aggregate += (vals[i].goods.saleStatus === 1 ? vals[i].goods.salePrice : vals[i].goods.originalPrice) * vals[i].number
                        // 加快递费
                        aggregate += vals[i].goods.despatchMoney * vals[i].number
                    }
                    this.$set(this.$data, 'totalNumber', totalNumber)

                    this.$set(this.$data, 'aggregate', aggregate)
                },
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
                            that.$router.push({path: 'my', params: {login: true}})
                            return;
                        }
                        const user = dt.data;
                        that.$set(that.$data, 'user', user);
                        that.$store.commit('loginSuccess', user);
                        that.loadData();
                    })
                } else {
                    console.log("登录")
                    this.$router.push({path: 'my', params: {login: true}})
                }
            } else {
                this.loadData();

            }

        },
        methods: {
            emitPrice(price) {
                console.log(price)
                this.$set(this.$data, 'aggregate', this.$data.aggregate + price)
                // 接组件传来的加减价格通知
            },
            sendOrder() {
                const that = this;
                http.postForm(this, 'order', 'createOrder',
                    {'token': this.$store.state.user.token, addressId: this.$data.addressSelect},
                    function (resp) {
                        console.log("创建订单")
                        console.table(resp)
                        const dt = resp.data;
                        console.log(dt.msg)
                        that.$set(that.$data, 'position', 'bottom');
                        that.$set(that.$data, 'showPositionValue', true);
                        if (dt.code === 0) {
                            that.$set(that.$data, 'toastMsg', '下单成功！');
                            that.$set(that.$data, 'selectAddressShow', false);
                            that.loadData()
                        } else {
                            that.$set(that.$data, 'toastMsg', dt.msg);
                        }

                    }
                )
            },
            getAddress() {
                const that = this;
                http.postForm(this, 'address', 'getAddress', {token: localStorage.getItem("token")}, function (resp) {
                    const dt = resp.data;
                    const d = dt.data
                    console.log(d)
                    that.$set(that.$data, 'address', d);
                    const addressOption = []
                    console.log(d.length)
                    if (d.length <= 0) {
                        this.$set(this.$data, 'showPositionValue', true);
                        this.$set(this.$data, 'toastMsg', '您还有没添加收货地址！');
                        return;
                    }
                    for (let i = 0; i < d.length; i++) {
                        const obj = {}
                        obj.key = d[i].id
                        obj.value = d[i].addr
                        obj.select = d[i].defaultStatus === 1
                        addressOption.push(obj)
                        if (d[i].defaultStatus === 1) {
                            that.$set(that.$data, 'addressSelect', d[i].id)
                        }
                    }
                    that.$set(that.$data, 'addressOption', addressOption)
                })
            },
            loadData() {
                const that = this;
                http.postForm(this, 'buyCar', 'getBuyCar',
                    {'token': this.$store.state.user.token},
                    function (resp) {
                        const dt = resp.data;
                        if (dt.code === 0) {
                            console.log(dt.data)
                            that.$set(that.$data, 'buyCars', dt.data);
                        } else {
                            console.log('获取购物车数据失败！')
                        }

                    }
                )
            },
            createOrder() {
                // 最低购买时间商品
                const buyCars = this.$data.buyCars;
                let buyCarNumber = 0; // 购物车商品总数
                for (let i = 0; i < buyCars.length; i++) {
                    buyCarNumber += buyCars[i].number;
                }
                if (buyCarNumber < 10) {
                    this.$set(this.$data, 'showPositionValue', true);
                    this.$set(this.$data, 'toastMsg', '最少结算十件商品！');
                    return;
                }
                this.getAddress()
                this.$set(this.$data, 'selectAddressShow', true)


            },
            add(buyCar) {
                // 添加购物车商品数量
                console.log(buyCar)
                http.postForm(this, 'buyCar', 'add',
                    {
                        'token': localStorage.getItem("token"),
                        'goodsId': buyCar.goods.goodsId,
                        'number': 1
                    },
                    function (resp) {
                        const dt = resp.data;
                        if (dt.code === 0) {
                            buyCar.number++;
                        } else {
                            console.log('添加购物车商品数量失败！')
                        }
                    }
                )
            },
            minus(buyCar) {
                http.postForm(this, 'buyCar', 'minusBuyCar',
                    {
                        'token': localStorage.getItem("token"),
                        'goodsId': buyCar.goods.goodsId,
                        'number': 1
                    },
                    function (resp) {
                        const dt = resp.data;
                        if (dt.code === 0) {
                            buyCar.number--;
                        } else {
                            console.log('添加购物车商品数量失败！')
                        }

                    }
                )
            }
        }
    }
</script>

<style scoped>
    .shop_buy_car {
        padding: 5px;
    }

    .shop_buy_car_item {
        padding: 5px;
        background-color: #fff;
        border-radius: 2px;
        box-shadow: 0 1px 3px rgba(26, 26, 26, .1);
        box-sizing: border-box;
        height: 100px;
        display: flex;
        flex-flow: row nowrap;
    }

    .shop_buy_car_right {
        width: 100%;
    }

    .shop_buy_car_title {
        position: relative;
        left: 5px;
        font-size: 14px;
        color: #666;
        height: 70px;
    }

    .shop_buy_car_price {
        color: #ff4121;
        float: bottom;
    }

    .shop_buy_car_num_tool {
        display: flex;
        flex-flow: row nowrap;
        float: right;
        color: #999;
    }

    .shop_buy_car_num_tool_add {
        width: 30px;
        text-align: center;
        font-size: 16px;
        position: relative;
    }

    .shop_buy_car_num_tool_num {
        width: 30px;
        text-align: center;
        font-size: 16px;
        color: #000;

    }

    .shop_buy_car_num_tool_sub {
        width: 30px;
        text-align: center;
        font-size: 16px;
        position: relative;
    }

    .shop_buy_car_img {
        width: 90px;
        height: 90px;
        float: left;
        margin-right: 5px;
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

    .shop_buy_car_check {
        line-height: 90px;
        margin-right: 5px;
        width: 30px;
    }

</style>
