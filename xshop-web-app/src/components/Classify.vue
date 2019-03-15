<template>
    <div class="shop_class">
        <x-header :left-options="{showBack: false}">产品分类</x-header>
        <div class="shop_class_content">
            <div class="shop_class_list">
                <div class="shop_class_item selected">
                    <span>男装</span>
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
    import ShopTabbar from './ShopTabbar'

    export default {
        name: "Classify",
        components: {XHr, XButton, ShopTabbar},
        data() {
            return {
                classify: [],
                select: {},
                classList: [],
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

    .shop_class_content {
        margin-bottom: 55px;
        width: 100%;
    }

    .shop_class_list {
        position: fixed;
        left: 0;
        top: 46px;
        width: 80px;
        height: 100%;
        padding-bottom: 59px;
    }

    .shop_class_item {
        padding: 10px;
        font-size: 16px;
        text-align: center;
    }

    .selected {
        background-color: #fff;
    }

    .shop_class_info {
    }
</style>
