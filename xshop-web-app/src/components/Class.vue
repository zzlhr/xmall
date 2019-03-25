<template>
    <div>
        <x-header :left-options="{showBack: false}">产品分类</x-header>
        <div style="margin-bottom: 55px">
            <div class="shop_class" v-for="(cl,index) in classify" :key="index">
                <!--<div class="shop_class_list">-->
                <!--<div class="shop_class_item selected">-->
                <!--<p class="shop_class_item_title ">{{cl.clName}}类</p>-->
                <!--</div>-->
                <!--</div>-->
                <div class="shop_class_content">
                    <div class="shop_class_group">
                        <div class="shop_class_group_item">
                            <div class="shop_class_group_title" v-if="cl.select !== true"
                                 @click="cl.select = !cl.select">{{cl.clName}}类 <img class="el-icon-caret-bottom"
                                                                                     style="float: right;vertical-align: -3px; height: 1.5rem; width: 1.5rem; color: #333; z-index: 999"
                                                                                     src="../assets/rightjiantou.png"/>
                            </div>
                            <div class="shop_class_group_title selected" v-if="cl.select"
                                 @click="cl.select = !cl.select">{{cl.clName}}类 <img class="el-icon-caret-bottom"
                                                                                     style="float: right;vertical-align: -3px; height: 1.5rem; width: 1.5rem; color: #333; z-index: 999"
                                                                                     src="../assets/pulldown.png"/>
                            </div>
                            <!--<x-hr></x-hr>-->
                            <transition name="fade">
                                <div class="class_box" v-show="cl.select">
                                    <div class="class_content" v-for="(c, i) in cl.children" :key="i" @click="go(c)">
                                        <!--<img src="https://ws1.sinaimg.cn/large/005tjobOly1ftdzjqthsfj306404lt8n.jpg"/>-->
                                        <div>{{c.clName}}</div>
                                    </div>
                                </div>
                            </transition>
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
    import ShopTabbar from './ShopTabbar'

    export default {
        name: "Class",
        components: {XHr, XButton, ShopTabbar},
        data() {
            return {
                classify: [],
                select: {},
            }
        },
        methods: {
            selectClassify(index) {
                const select = this.$data.select;
                select[index] = !(select[index] === undefined ? false : select[index])

                this.$set(this.$data, 'select', select)
            },
            go(c) {

                this.$router.push({
                    name: 'ClassCommodity',
                    params: {
                        cid: c.clId
                    }
                })
            },
            loadData() {
                const that = this;
                http.postForm(this, 'goods', 'classifyTree', {}, function (resp) {
                    const data = resp.data.data;

                    for (let i = 0; i < data.length; i++) {
                        data[i].select = false;
                    }
                    that.$set(that.$data, 'classify', data);
                })
            }
        },
        mounted() {
            this.loadData();
            if (this.$store.state.user.token === undefined ||
                this.$store.state.user.token === null ||
                this.$store.state.user.token === "") {
                const token = localStorage.getItem('token');
                if (token !== undefined) {
                    const sendData = {
                        'token': token
                    }
                    const that = this;
                    http.postForm(this, 'user', 'tokenUse', sendData, function (data) {
                        const dt = data.data;
                        if (dt.code !== 0) {

                            // that.$router.push({path: 'my', params: {login: true}})
                            return;
                        }
                        const user = dt.data;
                        that.$set(that.$data, 'user', user);
                        that.$store.commit('loginSuccess', user);
                    })
                } else {
                    // this.$router.push({path: 'my', params: {login: true}})
                }
            } else {
            }
        }
    }
</script>

<style scoped>
    .fade-enter-active, .fade-leave-active {
        transition: opacity .3s;
    }

    .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
    {
        opacity: 0;
    }

    .shop_class {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        height: 100%;
    }

    .shop_class_list {
        height: 100%;
        width: 30px;
        background-color: #fff;
    }

    .v-transfer-dom {
        height: 100%;
    }

    /*.shop_class_item {*/
    /*padding: 10px;*/
    /*}*/

    .selected {
        background-color: #EEEEEE;
    }

    .shop_class_item_title {
        text-align: center;

    }

    .shop_class_content {
        width: 100%;
        -ms-overflow-style: none;
        overflow: -moz-scrollbars-none;
        overflow: hidden;
        /*padding-left: 10px;*/
        /*margin-bottom: 55px;*/
    }

    .shop_class_group {
        width: 95%;
        padding: 5px;
    }

    .shop_class_group_item {
        background-color: #fff;
        border: lightblue;
        border-radius: 2px;
        box-shadow: 0 1px 3px rgba(26, 26, 26, .1);
        box-sizing: border-box;
        margin-bottom: 10px;
        padding: 5px 5px;
    }

    .shop_class_group_title {
        /*background-color: #eeeeee;*/
        color: #333;
        font-size: 1.5rem;
        text-align: center;
    }

    .shop_class_group_title > img {
        margin-top: 7px;
    }

    .class_box {
        display: flex;
        flex-flow: row wrap;
        text-align: center;

    }

    .class_content > img {
        width: 100%;
        height: 50px;
    }

    .class_content > p {
        color: #666;
        font-size: 1rem;
        text-align: center;
    }

    .class_content {
        width: 45%;
        padding: 3px;

    }

</style>
