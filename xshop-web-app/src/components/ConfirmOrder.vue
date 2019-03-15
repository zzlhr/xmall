<template>
    <div class="confirm_order">
        <x-header :left-options="{preventGoBack: true}" @on-click-back="goBack()">确认订单</x-header>
        <div class="confirm_order_address">
            <div class="confirm_order_address_logo">
                <font-awesome-icon icon="address-card"></font-awesome-icon>
            </div>
            <div class="confirm_order_address_content" @click="selectAddress">
                <p class="confirm_order_address_consignee">
                    <span>收货人: {{address.consignee}}</span>
                    <span>{{address.linkTel}}</span>
                </p>
                <span class="confirm_order_address_info">收货地址:
                    {{address.provinceName +
                    address.cityName +
                    (address.countryName || "") +
                    (address.townName || "") +
                    (address.addr || "")}}</span>
            </div>
            <div class="confirm_order_address_logo">
                <font-awesome-icon icon="chevron-right"></font-awesome-icon>
            </div>
        </div>
        <div class="confirm_order_shop">
            <div class="confirm_order_shop_item" v-for="(buyCar, index) in selectedBuyCar" :key="index">
                <div class="confirm_order_shop_item_img">
                    <img :src="buyCar.goods.cover" alt="shop_img">
                </div>
                <div class="confirm_order_shop_item_content">
                    <span class="confirm_order_shop_item_title">{{buyCar.goods.title}}</span>
                    <div class="confirm_order_shop_item_bottom">
                        <span class="confirm_order_address_content_price">￥{{buyCar.goods.originalPrice}}</span>
                        <span class="confirm_order_address_content_num">x{{buyCar.number}}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="confirm_order_bottom">
            <div class="confirm_order_bottom_addup">合计金额: ￥{{totalPrice}}</div>
            <div class="confirm_order_bottom_btn_group">
                <button @click="settleAccounts">结&nbsp;&nbsp;算</button>
            </div>
        </div>
    </div>

</template>

<script>
    import http from "../util/HttpUtil";

    export default {
        name: "ConfirmOrder",
        data() {
            return {
                address: {},
                selectedBuyCar: [],
                totalPrice: 0,
            }
        },
        mounted() {

            this.selectedBuyCar = this.$route.params['selectedBuyCar'];
            if (this.$route.params['addr'] !== undefined) {
                // 如果从选择收货地址页面跳转回来的需要初始化地址
                this.address = this.$route.params['addr']
            } else {
                // 如果不是从选择地址跳转进来的加载默认收货地址。
                this.getAddr();
            }
            this.initPrices();
        },
        methods: {
            initPrices() {
                let buyCarIds = "";
                for (let i = 0; i < this.selectedBuyCar.length; i++) {
                    buyCarIds += this.selectedBuyCar[i].id + ",";
                }
                buyCarIds = buyCarIds.substring(0, buyCarIds.length - 1);
                const that = this;
                http.postForm(this, "order", "getTotalPrice", {buyCarIds: buyCarIds}, function (resp) {
                    console.log(resp.data.data.totalPrice);
                    that.totalPrice = resp.data.data.totalPrice;
                })
            },
            settleAccounts() {
                // 结算、下单
                let buyCarIds = "";
                for (let i = 0; i < this.selectedBuyCar.length; i++) {
                    buyCarIds += this.selectedBuyCar[i].id + ",";
                }
                buyCarIds = buyCarIds.substring(0, buyCarIds.length - 1);
                const that = this;
                http.postForm(this, "order", "createOrder",
                    {
                        token: localStorage.getItem("token"),
                        addressId: this.address.id,
                        buyCarIds: buyCarIds
                    },
                    function (resp) {
                        if (resp.data.code === 0) {
                            that.$vux.toast.show({
                                type: "success",
                                text: "下单成功！",
                            });
                        } else {
                            that.$vux.toast.show({
                                type: "warn",
                                text: "下单失败！",
                            });
                        }
                    }
                )
            },
            goBack() {
                // 退回到购物车
                this.$router.push({name: 'ShoppingCart'});
            },
            selectAddress() {
                this.$router.push({
                    name: 'SelectAddress',
                    params: {from: 'ConfirmOrder', selectedBuyCar: this.selectedBuyCar}
                })
            },
            getAddr() {
                const token = localStorage.getItem("token");
                const that = this;
                http.postForm(this, "address", "getAddress", {token: token}, function (resp) {
                    const addresses = resp.data.data;
                    for (let i = 0; i < addresses.length; i++) {
                        if (addresses[i].defaultStatus === 1) {
                            that.address = addresses[i];
                        }
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .confirm_order_bottom {
        position: fixed;
        bottom: 0;
        height: 50px;
        width: 100%;
        border-top: 1px solid #dedede;
        display: flex;
        flex-flow: row nowrap;
    }

    .confirm_order_bottom_addup {
        flex: 3;
        height: 100%;
        line-height: 50px;
        padding-right: 10px;
        text-align: right;
    }

    .confirm_order_bottom_btn_group {
        flex: 1;
        height: 100%;
    }

    .confirm_order_bottom_btn_group > button {
        height: 100%;
        width: 100%;
        background-color: #fd512e;
        border: none;
        color: #fff;
        font-size: 16px;
    }

    .confirm_order {
        background-color: #fff;
        height: 100%;
    }

    .confirm_order_address {
        padding: 10px;
        display: flex;
        flex-flow: row nowrap;
        height: 80px;
        border-bottom: 1px solid #ccc;
        margin-bottom: 10px;
    }

    .confirm_order_address_logo {
        line-height: 60px;
        padding: 10px;
        color: #999;
    }

    .confirm_order_address_consignee {
        margin-bottom: 10px;
    }

    .confirm_order_address_content {
        display: flex;
        flex-flow: column nowrap;
        flex: 3;
    }

    .confirm_order_address_info {
        font-size: 14px;
        font-weight: bold;
        line-height: 15px;
    }

    .confirm_order_shop_item {
        height: 100px;
        background-color: #EEEEEE;
        padding: 10px;
        display: flex;
    }

    .confirm_order_shop_item_img > img {
        width: 100px;
        height: 100px;
        margin-right: 5px;
    }

    .confirm_order_shop_item_content {
        line-height: 14px;
        font-size: 14px;
        display: flex;
        flex-flow: column nowrap;
        flex: 3;
    }

    .confirm_order_address_content_price {
        color: #c76541;
        flex: 3;
    }

    .confirm_order_shop_item_bottom {
        display: flex;
        flex-flow: row nowrap;
    }

    .confirm_order_shop_item_title {
        flex: 3;
    }


</style>
