<template>
    <div class="_shop-group">
        <div class="_shop-item">
            <img class="_shop-picture" src="../assets/d1.jpg">
            <p class="_shop-title">美美 的衣服测试商品我要跳样式css最棒</p>
            <span class="_shop-price">￥ 2888.0</span>
            <span class="_shop-sale">588888人已付款</span>
        </div>
        <div class="_shop-item">
            <img class="_shop-picture" src="../assets/d3.jpg">
            <p class="_shop-title">美美 的衣服测试商品我要跳样式css最棒</p>
            <span class="_shop-price">￥ 2888.0</span>
            <span class="_shop-sale">588888人已付款</span>
        </div>
        <div class="_shop-item">
            <img class="_shop-picture" src="../assets/d4.jpg">
            <p class="_shop-title">美美 的衣服测试商品我要跳样式css最棒</p>
            <span class="_shop-price">￥ 2888.0</span>
            <span class="_shop-sale">588888人已付款</span>
        </div>
    </div>
</template>

<script>
    export default {
        name: "ShopList"
    }
</script>

<style lang="scss" scoped>
    ._shop-group {
        padding: 5px;
        display: flex;
        flex-flow: row wrap;
    }

    ._shop-item {
        margin-left: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
        width: calc(50% - 30px);
        padding: 10px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3), 0 0 20px rgba(0, 0, 0, 0.1) inset;
        height: 250px;
        img {
            height: 180px;
        }
        ._shop-title{
            font-size: 14px;
        }
        ._shop-price{
            color: #d60004;
            font-size: 16px;
            font-weight: bold;
        }
        ._shop-sale{
            color: #999999;
            font-size: 12px;
        }
    }

    ._shop-picture {
        width: 100%;
    }

</style>