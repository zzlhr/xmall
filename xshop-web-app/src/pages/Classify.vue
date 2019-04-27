<template>
    <div class="shop_class">
        <x-header :left-options="{showBack: false}">产品分类</x-header>
        <div class="shop_class_content">
            <div class="shop_class_list">
                <div :class="{'shop_class_item' : true, selected: classify===selected}"
                     v-for="(classify, index) in classList" @click="selectClassify(classify)">
                    <span>{{classify.clName}}</span>
                </div>
            </div>
            <div  v-infinite-scroll  infinite-scroll-disabled="busy" infinite-scroll-distance="10" class="shop_class_info">
                <div class="shop_class_info_content">
                    <div v-for="classify in selected.children">
                        <span>{{classify.clName}}</span>
                        <div class="shop_class_info_children" v-for="cc in classify.children">
                            <img :src="picturePath + cc.picture">
                            <span>{{cc.clName}}</span>
                        </div>
                    </div>
                </div>
            </div>
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
                selected: {},
                classList: [],
                busy: false,
                picturePath: http.baseurl('goods') + "classifyImg/"
            }
        },
        mounted() {
            this.loadClassifyTree();
        },
        methods: {
            selectClassify(classify) {
                this.selected = classify;
            },
            loadClassifyTree() {
                const that = this;
                http.postForm(this, "goods", "classifyTree", {}, function (resp) {
                    that.classList = resp.data.data;
                    if (that.classList.length > 0) {
                        that.selected = that.classList[0];
                    }
                });
            }
        }
    }
</script>

<style scoped lang="scss">
    .shop_class_info_children {
        img {
            width: 60px;
            height: 60px;
        }
    }

    .shop_class {
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
        background-color: rgba(237, 237, 237, 0.28);
        overflow-y: scroll;
    }

    .shop_class_item {
        padding: 10px 5px;
        font-size: 14px;
        text-align: center;
    }

    .selected {
        background-color: #ffffff;
        color: #d60004;
    }

    .shop_class_info {
        padding-left: 85px;
        padding-right: 5px;
        padding-top: 5px;
        margin-bottom: 56px;
        height: calc(100% - 59px);
    }

    .shop_class_info_content {
        padding: 10px;
    }
</style>
