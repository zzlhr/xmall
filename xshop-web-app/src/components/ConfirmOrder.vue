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
                <img src="../assets/shop_img.webp">
            </div>
        </div>
        <div class="confirm_order_bottom">

        </div>
    </div>

</template>

<script>
    export default {
        name: "ConfirmOrder"
    }
</script>

<style scoped>
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
    }
</style>