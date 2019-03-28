<template>
    <div class="shop_class">
        <x-header :left-options="{showBack: false}">产品分类</x-header>
        <div class="shop_class_content">
            <div class="shop_class_list">
                <div class="shop_class_item" v-for="(classify, index) in classList">
                    <span>{{classify}}</span>
                </div>
            </div>
            <div class="shop_class_info"></div>
        </div>
        <shop-tabbar selected="class"></shop-tabbar>
    </div>
</template>

<script>
    import XHr from "vux/src/components/x-hr/index";
    import XButton from 'vux/src/components/x-button/index'
    import http from '../util/HttpUtil'
    import ShopTabbar from '../components/ShopTabbar'

    export default {
        name: "Classify",
        components: {XHr, XButton, ShopTabbar},
        data() {
            return {
                classify: [],
                select: {},
                classList: [
                    "男装", "女装", "美妆洗护", "食品", "手机数码", "人群偏爱", "美妆洗护",
                    "鞋靴", "百货", "百货", "百货", "百货", "百货", "百货", "美妆洗护", "美妆洗护"],
            }
        },
        methods: {}
    }
</script>

<style scoped>
    .shop_class {
        background-color: #F2F2F2;
        position: fixed;
        width: 100%;
        height: 100%;
    }

    .shop_class::-webkit-scrollbar {
        width: 0
    }

    .shop_class_content {
        margin-bottom: 55px;
        width: 100%;
    }

    .shop_class_list {
        position: fixed;
        left: 0;
        top: 46px;
        width: 80px;
        height: calc(100% - 59px);
        background-color: #fff;
        overflow-y: scroll;
    }

    .shop_class_item {
        padding: 10px 5px;
        font-size: 16px;
        text-align: center;
        border-bottom: 1px solid #ccc;
    }

    .selected {
        background-color: #F2F2F2;

    }

    .shop_class_info {
    }
</style>
