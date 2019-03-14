<template>
    <div class="confirm_order">
        <x-header>确认订单</x-header>

        <div class="confirm_order_address">
            <div class="confirm_order_address_logo">
                <font-awesome-icon icon="address-card"></font-awesome-icon>
            </div>
            <div class="confirm_order_address_content">
                <p class="confirm_order_address_consignee">
                    <span>收货人: 刘先生</span>
                    <span>19937150218</span>
                </p>
                <span class="confirm_order_address_info">收货地址: 河南省郑州市中原区梧桐街道高新区升龙又一城D区1号楼1单元xxx</span>
            </div>
            <div class="confirm_order_address_logo">
                <font-awesome-icon icon="chevron-right"></font-awesome-icon>
            </div>
        </div>
        <div class="confirm_order_shop">
            <div class="confirm_order_shop_item">
                <div class="confirm_order_shop_item_img">
                    <img src="../assets/d1.jpg" alt="shop_img">
                </div>
                <div class="confirm_order_shop_item_content">
                    <span class="confirm_order_shop_item_title">这是一个好看的一个好看的衣服这是一个好看的衣服这是一个好看的衣服</span>
                    <div class="confirm_order_shop_item_bottom">
                        <span class="confirm_order_address_content_price">￥200.0</span>
                        <span class="confirm_order_address_content_num">x10</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="confirm_order_bottom">
            <div class="confirm_order_bottom_addup">合计金额: ￥56.50</div>
            <div class="confirm_order_bottom_btn_group">
                <button>结&nbsp;&nbsp;算</button>
            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: "ConfirmOrder"
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