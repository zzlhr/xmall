<template>
    <div>
        <s-search></s-search>
        <div class="scrollerWrap">
            <main>
                <swiper class="home_swiper" :auto="true" :list="demo01_list" v-model="demo01_index"></swiper>
                <card :header="cardTitle">
                    <div slot="header" class="weui-panel__hd">{{cardTitle.title}}</div>
                    <grid :show-lr-borders="false">
                        <grid-item label="活动商品" @click.native="goSale(1)">
                            <img slot="icon" src="../assets/home_active.png" alt="活动商品">
                        </grid-item>
                        <grid-item label="精选商品" @click.native="goSale(2)">
                            <img slot="icon" src="../assets/home_boutique.png" alt="精选商品">
                        </grid-item>
                        <grid-item label="包邮商品" @click.native="goSale(3)">
                            <img slot="icon" src="../assets/home_freepostage.png" alt="包邮商品">
                        </grid-item>
                        <grid-item label="新店必备" @click.native="goSale(4)">
                            <img slot="icon" src="../assets/home_new.png" alt="新店必备">
                        </grid-item>
                    </grid>
                </card>
                <group style="margin-bottom: 10px">
                    <cell title='通知' value-align="left">
                        <slot>
                            <marquee>
                                <marquee-item v-for="message in messageList.arr" :key="message.msgId"
                                              @click.native="clickMessage(message)">
                                    {{message.messageValue.split("|")[0]}}
                                </marquee-item>
                            </marquee>
                        </slot>
                    </cell>
                </group>
                <tab bar-active-color="#668599" :line-width="1">
                    <tab-item v-for="item in fClassify" :key="item.clId" :selected="selectedClassify===item.clId"
                              @click.native="selectedClassify=item.clId">{{item.clName}}
                    </tab-item>
                </tab>
                <div style="margin-bottom: 60px">
                    <group>
                        <cell v-for="(item, index) in priceRange" :key="index" :title="item.clName"
                              :link="'class1c/'+item.clId"
                              :inline-desc='"￥"+item.min+"-"+item.max'></cell>
                    </group>
                </div>
            </main>
        </div>

        <shop-tabbar selected="home"></shop-tabbar>

    </div>
</template>

<script>
    import {
        Swiper,
        SwiperItem,
        Grid,
        GridItem,
        GroupTitle,
        Flexbox,
        FlexboxItem,
        XDialog,
        XInput,
        Marquee, MarqueeItem,
        Group,
        Cell,
        Tab,
        TabItem,
    } from "vux";
    import SSearch from './search'
    import {Search} from "vux";
    import http from "../util/HttpUtil";

    import ShopTabbar from './ShopTabbar'

    export default {
        name: "Home",
        components: {
            SSearch,
            Swiper,
            SwiperItem,
            Grid,
            GridItem,
            Flexbox,
            FlexboxItem,
            GroupTitle,
            Search,
            XDialog,
            XInput,
            Marquee,
            MarqueeItem,
            Group,
            Cell,
            Tab,
            TabItem,
            ShopTabbar
        },
        data() {
            return {
                shopList: [],
                cardTitle: {title: "推荐频道"},
                demo01_list: [],
                swiperItemIndex: 1,
                demo01_index: 0,
                goodsParams: {
                    page: 1,
                    title: ""
                },
                fClassify: [],
                selectedClassify: 0,
                messageList: [],
                priceRange: []
            };
        },
        watch: {
            selectedClassify(val) {
                console.log("ssssss" + val);
            }
        },
        methods: {
            clickMessage(message) {
                const jumpUrl = message.messageValue.split("|")[1] || "";
                if (jumpUrl !== "") {
                    location.href = jumpUrl;
                }
            },
            getMessage() {
                const that = this;
                const MESSAGE_TYPE = 3;
                http.postForm(this, 'message', 'getMessageList',
                    {token: localStorage.getItem('token'), type: MESSAGE_TYPE, page: 1, pageSize: 5}, function (resp) {
                        const data = resp.data.data;
                        that.$set(that, 'messageList', data);

                    })
            },
            goSale(saleType) {

                this.$router.push({name: 'SaleList', params: {cid: saleType}})
            },
            loadGoodsListSearch() {
                this.$set(this.$data.goodsParams, "page", 1);
                this.$set(this.$data, "shopList", []);
                this.loadGoodsList();
            },
            loadGoodsList(done) {

                const that = this;
                http.postForm(this, "goods", "list", this.goodsParams, function (resp) {
                    const data = resp.data.data;
                    that.$set(that.$data, "shopList", that.shopList.concat(data.arr));
                    that.$set(
                        that.$data.goodsParams,
                        "page",
                        that.$data.goodsParams.page + 1
                    );
                    // that.scroll.finishPullUp()
                    done();
                });
            },

            getFClassify(fid) {
                const that = this;
                http.postForm(this, "goods", "fClassify", {fid: 1}, function (resp) {
                    that.fClassify = resp.data.data
                    that.$set(that.$data, 'selectedClassify', that.fClassify[0].clId);
                })
            },

            loadClassifyPriceRange(fid) {
                const that = this;
                http.postForm(this, 'goods', 'getClassifyPriceRange', {fid: fid}, function (resp) {
                    that.priceRange = resp.data.data

                })
            },
            goCommodity(id) {
                this.$router.push({
                    name: "Commodity",
                    params: {
                        id: id
                    }
                });
            },
            loadData() {
            },
            loadSwiper() {
                const that = this;
                http.get(this, "app", "picture", function (data) {
                    const dt = data.data;
                    const picture = JSON.parse(dt.data);
                    for (let i = 0; i < picture.length; i++) {
                        picture[i].img = picture[i].picture;
                        picture[i].url = "/commodity/" + picture[i].url;
                    }
                    sessionStorage.setItem("index_p", picture);
                    that.$set(that.$data, "demo01_list", picture);
                });
            }
        },
        mounted() {
            const query = this.$route.query
            const co = query.co
            if (co !== undefined && co !== "") {
                this.$router.push({name: 'Commodity', params: {id: co}})
            }
            const that = this;
            this.loadSwiper();
            if (
                this.$store.state.user.token === undefined ||
                this.$store.state.user.token === null ||
                this.$store.state.user.token === ""
            ) {
                const token = localStorage.getItem("token");
                if (token !== undefined) {
                    const sendData = {
                        token: token
                    };

                    http.postForm(this, "user", "tokenUse", sendData, function (data) {
                        const dt = data.data;
                        if (dt.code !== 0) {

                            // that.$r5outer.push({path: 'my', params: {login: true}})
                            return;
                        }
                        const user = dt.data;
                        that.$set(that.$data, "user", user);
                        that.$store.commit("loginSuccess", user);
                        that.getMessage()
                    });
                } else {
                    // 如果cookie中没有token跳转到
                    // this.$router.push({path: 'my', params: {login: true}})
                }
            } else {
                this.getMessage()
            }
            this.getFClassify(1);
        },
        watch: {
            selectedClassify(val) {
                this.loadClassifyPriceRange(val);
            }
        }
    };
</script>

<style>
    .scrollerWrap {
        position: absolute;
        top: 46px;
        width: 100%;
        height: 90%;
        bottom: 53px;
    }

    .vux-marquee {
        padding-left: 30px;
    }

    .vux-marquee-box {
        list-style-type: none;
    }

    main {
        height: 100%;
    }

    .home_swiper > .vux-swiper {
        height: 190px !important;
    }

    .shop_item {
        padding: 1px;
        margin-bottom: 10px;
    }

    .shop_item > img {
        width: 100%;
        height: 12rem;
    }

    .shop_title {
        color: #0084ff;
    }

    .shop_text {
        padding-left: 5px;
    }

    .shop_price {
        color: #ff4825;
    }

    .shop_describe {
        color: #b4b4b4;
        font-size: 13px;
    }

    .weui-panel__hd {
        padding: 14px 15px 10px;
        color: #999999;
        font-size: 13px;
        position: relative;
    }

    .weui-grid:before {
        border-right: 0px;
    }
</style>
