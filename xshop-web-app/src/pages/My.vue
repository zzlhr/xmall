<template>
    <div>
        <x-header :left-options="{showBack: false}">我的</x-header>
        <div class="shop_my" v-show="this.$store.state.user.username === undefined">
            <div class="shop_my_header">
                <img src="../assets/header.png"/>
            </div>
            <p>未登录</p>
            <group style="margin: 10px">
                <x-button type="primary" @click.native="goLogin">登录</x-button>
            </group>
            <!--<group style="margin: 10px">-->
            <!--<x-button type="primary" @click.native="wecahtLogin">微信登录</x-button>-->
            <!--</group>-->
            <group style="margin: 10px">
                <x-button type="primary" link="/regist">注册</x-button>
            </group>
        </div>
        <div class="shop_my" v-show="this.$store.state.user.username !== undefined">
            <div class="shop_my_header">
                <img
                        :src="this.$store.state.user.header === '' ? 'data:image/png;base64,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' : this.$store.state.user.header"/>
            </div>
            <p>{{this.$store.state.user.username}}</p>
        </div>

        <div class="cell-class" v-show="this.$store.state.user.token !== undefined">
            <cell title="我的订单" link="/orders"></cell>
            <cell title="绑定微信(微信内)" @click.native="bindWeChat"></cell>
            <cell title="绑定微信(app内)" link="/orders"></cell>
            <cell title="修改密码" link="/uppwd"></cell>
            <cell title="收货地址" link="/address"></cell>
            <cell title="关于我们" link="/about"></cell>
            <cell title="退出登录" @click.native="outLogin()"></cell>
        </div>


        <shop-tabbar selected="my"></shop-tabbar>

        <div v-transfer-dom>
            <x-dialog v-model="showDialog" :v-show="showDialog" class="dialog-demo" hide-on-blur>
                <div style="padding:15px;">
                    <group>
                        <x-input title='手机号' v-model="loginForm.phone"></x-input>
                    </group>
                    <group>
                        <x-input title='密 码' type="password" v-if="!smsCodeLogin"
                                 v-model="loginForm.password"></x-input>
                        <x-input title='验证码' type="text" v-if="smsCodeLogin" v-model="loginForm.smsCode"></x-input>
                    </group>
                    <div class="op_line">
                        <x-button class="op_login_r" mini plain type="primary" v-if="!smsCodeLogin"
                                  @click.native="smsCodeLogin = true">快速登录
                        </x-button>
                        <x-button class="op_login_l" mini plain type="primary" v-if="smsCodeLogin"
                                  @click.native="sendLoginCode">{{
                            nextSendTime < 0 ? '发送验证码' :
                            nextSendTime}}
                        </x-button>
                        <x-button class="op_login_r" mini plain type="primary" v-if="smsCodeLogin"
                                  @click.native="smsCodeLogin = false">密码登录
                        </x-button>
                    </div>

                    <x-button class="loginBtn" mini plain type="primary" @click.native="loginDo">登录</x-button>
                </div>
            </x-dialog>
        </div>
        <toast v-model="showPositionValue" type="text" :time="800" is-show-mask :text="toastValue"
               position="bottom"></toast>
    </div>
</template>

<script>
    import {Clocker, Cell, GroupTitle, XHr, XDialog, XInput, Group} from "vux";
    import http from "../util/HttpUtil";
    import ShopTabbar from '../components/ShopTabbar'

    export default {
        name: "My",
        components: {
            Clocker,
            Cell,
            GroupTitle,
            XHr,
            XDialog,
            XInput,
            Group,
            ShopTabbar
        },
        data() {
            return {
                showDialog: false,
                showPositionValue: false,
                smsCodeLogin: true,
                toastValue: "",
                loginForm: {
                    username: "",
                    password: "",
                    smsCode: ""
                },
                order: {},
                orderNumber: 0,
                nextSendTime: 0
            };
        },
        mounted() {
            // 接收参数 设置token
            if (this.$route.query.token !== undefined) {
                localStorage.setItem('token', this.$route.query.token)
            }
            if (this.$route.params.login) {
                this.$set(this.$data, 'showDialog', true)
            }
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
                    const that = this;
                    http.postForm(this, "user", "tokenUse", sendData, function (data) {
                        const dt = data.data;
                        if (dt.code !== 0) {
                            return;
                        }
                        const user = dt.data;
                        that.$set(that.$data, "user", user);
                        that.$store.commit("loginSuccess", user);
                    });
                }
            }
        },
        watch: {
            order: function (newOrder, oldOrder) {

                let number = 0;
                for (var i = 0; i < newOrder.orderInfoVOS.length; i++) {
                    number += newOrder.orderInfoVOS[0].number;
                }
                this.$set(this.$data, "orderNumber", number);
            }
        },
        methods: {
            goLogin() {
                this.$router.push({name: 'Login', params: {from: "My"}})
            },
            bindWeChat() {
                const regExp = new RegExp("/", "g")
                location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0e10c5ebcf43787&redirect_uri=" +
                    (http.baseurl() + "user/bindWeChat").replace(":", "%3A").replace(regExp, "%2F") + "&response_type=code&scope=snsapi_base&state=" + localStorage.getItem("token") + "#wechat_redirect"
            },
            wecahtLogin() {
                const regExp = new RegExp("/", "g")
                location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0e10c5ebcf43787&redirect_uri=" +
                    (http.baseurl() + "user/getWeChatCode").replace(":", "%3A").replace(regExp, "%2F") + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
            },
            sendLoginCode() {
                if (this.$data.nextSendTime > 0) {
                    return;
                }
                if (
                    this.$data.loginForm.phone === "" ||
                    this.$data.loginForm.phone === undefined
                ) {
                    this.$set(this.$data, "showPositionValue", true);
                    this.$set(this.$data, "toastValue", "请输入手机号");
                    return;
                }
                const that = this;
                http.postForm(
                    this,
                    "user",
                    "sendLoginCode",
                    {phoneNumber: this.$data.loginForm.phone},
                    function (resp) {
                        if (resp.data.code === 0) {
                            that.$set(that.$data, "showPositionValue", true);
                            that.$set(that.$data, "toastValue", "发送验证码成功！");
                            that.$set(that.$data, "nextSendTime", 60);
                        } else {
                            that.$set(that.$data, "showPositionValue", true);
                            that.$set(that.$data, "toastValue", resp.data.msg);
                        }
                    }
                );
            },
            loadOrderList(userToken) {
                const that = this;
                http.postForm(
                    this,
                    "order",
                    "orderList",
                    {token: userToken, page: 1, pageSize: 1},
                    function (resp) {
                        if (resp.data.code === 0) {
                            const od = resp.data.data[0];


                            that.$set(that.$data, "order", od);
                        }
                    }
                );
            },
            outLogin() {

                this.$store.commit("loginSuccess", {});
                localStorage.setItem("token", {});
            },
            loginDo() {
                const that = this;
                if (this.$data.smsCodeLogin) {
                    this.$set(this.$data.loginForm, 'password', '')
                } else {
                    this.$set(this.$data.loginForm, 'smsCode', '')
                }
                http.postForm(this, "user", "login", this.loginForm, function (resp) {
                    const dt = resp.data;
                    // TODO: the user must be prohibited from sending the next request when the request is a response
                    if (dt.code === 0) {
                        const user = dt.data;
                        that.$store.commit("loginSuccess", user);
                        that.$set(that.$data, "user", user);
                        that.$set(that.$data, "showPositionValue", true);
                        that.$set(that.$data, "showDialog", false);
                        that.$set(that.$data, "toastValue", "登录成功！");
                        localStorage.setItem("token", user.token);
                        that.loadOrderList(user.token);
                    } else {
                        that.$set(that.$data, "showPositionValue", true);
                        that.$set(that.$data, "toastValue", dt.msg);
                    }
                });
            }
        }
    };
</script>

<style scoped>
    .shop_my {
        background-color: #fff;
        text-align: center;
    }

    .loginBtn {
        width: 100%;
    }

    .shop_my_order {
        background-color: #fff;
        padding: 5px 10px;
        margin-top: 10px;
    }

    .op_line {
        height: 50px;
        width: 100%;
    }

    .op_login_r {
        right: 20px;
        top: 157px;
        position: absolute;
        margin-top: 0 !important;
    }

    .op_login_l {
        left: 20px;
        top: 157px;
        position: absolute;
    }

    .no_order {
        text-align: center;
    }

    .shop_my_header {
        display: flex;
        justify-content: center;
    }

    .shop_my_header > img {
        width: 100px;
        height: 100px;
    }

    .shop_my_order_item {
        height: 80px;
        background-color: #eeeeee;
        display: flex;
        flex-direction: row;
        margin: 10px;
    }

    .shop_my_order_img > img {
        width: 100px;
        height: 70px;
    }

    .shop_my_order_img {
        padding: 5px;
    }

    .shop_my_order_content {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-between;
        width: 80%;
        color: #999999;
    }

    .shop_my_order_id {
        color: #999999;
        font-size: 0.75rem;
    }

    .shop_my_order_content_price {
        display: flex;
        flex-direction: column;
        padding-right: 5px;
    }

    .shop_my_order_content_title {
        color: #222222;
    }

    .shop_my_order_content_price {
        text-align: right;
    }

    .shop_my_order_content_price_original {
        font-size: 0.9rem;
    }

    .shop_my_order_content_price_buy {
        color: red;
    }

    .shop_my_order_content_num {
        font-size: 0.75rem;
    }

    .order_content_price_count {
        font-size: 0.75rem;
        text-align: right;
    }

    .weui-cell {
        background-color: #fff;
    }

    .cell-class {
        margin-top: 20px;
    }

    .v-transfer-dom {
        margin-bottom: 50px;
    }

    .shop_my_order_hr {
        padding-top: 1px;
    }
</style>
