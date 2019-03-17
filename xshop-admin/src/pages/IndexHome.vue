<template>
    <div id="app">
        <el-container>
            <el-header>
                <el-menu
                        default-active="spterp"
                        class="el-menu-header"
                        mode="horizontal"
                        @select="handleSelect"
                        :background-color="backgroundColor"
                        text-color="#fff"
                        active-text-color="#99CCFF">
                    <el-menu-item index="spterp">选货邦后台管理系统</el-menu-item>
                    <el-menu-item class="right-menu-item" v-if="store.login === 0" index="#" @click="loginWindow=true">
                        登录
                    </el-menu-item>
                    <el-menu-item class="right-menu-item" v-if="store.login === 1" index="3">
                        <el-dropdown style="color: #fff; margin-top: -2px;">
                            <span class="el-dropdown-link">{{user.username}}<i class="el-icon-arrow-down el-icon--right"
                                                                               style="color:#fff;"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item>我的信息</el-dropdown-item>
                                <el-dropdown-item>修改密码</el-dropdown-item>
                                <el-dropdown-item divided @click.native="loginOut">退出</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </el-menu-item>

                    <el-menu-item class="right-menu-item" v-if="store.login === 1" index="4" @click="goMessagePage">
                        <i class="el-icon-bell"></i>消息
                        <el-badge :value="newOrderCount" v-show="newOrderCount !== 0" class="item"></el-badge>
                    </el-menu-item>

                    <!--<el-menu-item class="right-menu-item"  index="1.html">dddd</el-menu-item>-->
                </el-menu>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu
                            @select="selectItem"
                            @open="handleOpen"
                            @close="handleClose"
                            :background-color="backgroundColor"
                            text-color="#fff"
                            active-text-color="#99CCFF">

                        <el-submenu v-for="(menuItem, index) in leftMenu" :disabled="store.login === 0"
                                    :index="menuItem.menuIndex" :key="index">
                            <template slot="title" :tabindex="menuItem.menuIndex">
                                <i class="el-icon-menu"></i>
                                <span slot="title">{{ menuItem.menuName }}</span>
                            </template>
                            <el-menu-item v-for="(item, i) in menuItem.menuItems"
                                          :data-key="item.itemName"
                                          :index="item.itemIndex" :key="item.compName">
                                {{ item.itemName }}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-container>
                    <el-main>
                        <router-view></router-view>
                    </el-main>
                    <el-footer>Copyright © 2018, Content By lhr 保留所有权利.</el-footer>
                </el-container>
            </el-container>
        </el-container>

        <!-- 登录窗口 -->
        <el-dialog
                title="登 录"
                :visible.sync="loginWindow"
                width="30%">
            <div>
                <el-form ref="loginForm" :rules="rules" :model="loginForm" label-width="80px">
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="loginForm.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password" v-model="loginForm.password"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="loginWindow = false">取 消</el-button>
                <el-button type="primary" @click="loginDo('loginForm')">登 录</el-button>
            </span>
        </el-dialog>


    </div>
</template>

<script>
    import httpUtil from '../util/HttpUtil.js';

    import Home from "../components/Home";
    import UserList from '../components/user/List'
    import UserInfo from '../components/user/Info'
    import UserEdit from '../components/user/Edit'
    import EnterpriseList from '../components/enterprise/List'
    import EnterpriseEdit from '../components/enterprise/Edit'
    import MenuList from '../components/auth/MenuList'
    import AuthGroup from '../components/auth/AuthGroup'
    import AuthGroupEdit from '../components/auth/AuthGroupEdit'
    import EditAuthValue from '../components/auth/EditAuthValue'

    import GoodsList from '../components/goods/List'
    import GoodsEdit from '../components/goods/Edit'
    import GoodsAdd from '../components/goods/Edit'
    import OrderList from '../components/order/list'
    import SystemIndex from '../components/system/Index'
    import SupplierList from '../components/supplier/List'
    import SupplierEdit from '../components/supplier/Edit'
    import Menu from '../config/menu'

    export default {
        name: "IndexHome",
        components: {
            Home,
            UserList,
            EnterpriseList,
            EnterpriseEdit,
            UserInfo,
            UserEdit,
            MenuList,
            AuthGroup,
            AuthGroupEdit,
            EditAuthValue,
            GoodsList,
            GoodsAdd,
            GoodsEdit,
            OrderList,
            SystemIndex,
            SupplierList,
            SupplierEdit,
        },
        data() {
            return {
                newOrderCount: 0,
                loginWindow: false, // false不显示登录窗口，true显示
                store: this.$store.state,
                backgroundColor: '#567390',
                loginForm: {
                    // 登录表单
                    username: '',
                    password: '',
                },
                user: {},
                leftMenu: [
                    {
                        menuIndex: 'enterprise',
                        menuName: '企业管理',
                        menuItems: [
                            {
                                itemIndex: '/enterprise/list',
                                itemName: '企业列表',
                                compName: 'enterpriseList'
                            }
                        ]
                    },
                    {
                        menuIndex: 'user',
                        menuName: '用户管理',
                        menuItems: [
                            {
                                itemIndex: '/user/list',
                                itemName: '用户列表',
                                compName: 'userList'
                            }
                        ]
                    },
                    {
                        menuIndex: 'auth',
                        menuName: '权限管理',
                        menuItems: [
                            {
                                itemIndex: '/auth/menulist',
                                itemName: '菜单列表',
                                compName: 'authMenu'
                            },
                            {
                                itemIndex: '/auth/authgroup',
                                itemName: '权限组管理',
                                compName: 'authGroup'
                            }
                        ]
                    },
                    {
                        menuIndex: 'goods',
                        menuName: '商品管理',
                        menuItems: [
                            {
                                itemIndex: '/goods/list',
                                itemName: '商品列表',
                                compName: 'goodsList'
                            },
                        ]
                    },
                    {
                        menuIndex: 'order',
                        menuName: '订单管理',
                        menuItems: [
                            {
                                itemIndex: '/order/list',
                                itemName: '订单列表',
                                compName: 'orderList'
                            },
                        ]
                    }, {
                        menuIndex: 'system',
                        menuName: '系统管理',
                        menuItems: [
                            {
                                itemIndex: '/system/index',
                                itemName: '系统管理',
                                compName: 'systemIndex'
                            },
                            {
                                itemIndex: '/supplier/list',
                                itemName: '供应商列表',
                                compName: 'supplierList'
                            },
                            {
                                itemIndex: '/message/message',
                                itemName: '消息通知',
                                compName: 'message'
                            }
                        ]
                    },
                    {
                        menuIndex: 'statistics',
                        menuName: '统计菜单',
                        menuItems: [
                            {
                                itemIndex: '/statistics/sale',
                                itemName: '销售统计',
                                compName: 'SaleStatistics'
                            }
                        ]
                    }
                ],
                activeIndex: 'Home',
                activeIndex2: 'Home',
                editableTabsValue2: 'Home',
                editableTabs2: [
                    {
                        title: '首页',
                        name: 'Home',
                    },
                ],
                tabIndex: 2,
                //表单验证
                rules: {
                    phone: [
                        {required: true, message: '账号不能为空', trigger: 'change'},
                        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'},
                        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
                    ]
                }
            };
        },
        mounted() {
            const token = this.$cookie.get('token');
            if (token.length > 10) {
                const that = this;
                const sendData = {
                    'token': token
                }
                httpUtil.post(this, 'user', 'tokenUse', sendData, function (data) {
                    const dt = data.body;
                    if (dt.code !== 0) {
                        return;
                    }
                    const user = dt.data;
                    that.$set(that.$data, 'user', user);
                    //console.log(user)
                    that.$cookie.set('token', user.token);
                    that.$set(that.$data, 'user', user);
                    that.$store.commit('loginSuccess', user);
                    // console.log()
                    that.$set(that.$data, 'loginWindow', false);
                    that.$set(that.$data.store, 'login', 1);
                })
            }
            const that = this;
            // that.getNewOrderCount();
            // const timer = setInterval(function () {
            //     that.getNewOrderCount();
            // }, 5000)
        },
        methods: {
            goMessagePage() {
                this.$router.push({path: '/message/list'})
            },
            loginOut() {
                console.log("退出登录")
                this.$cookie.set('token', "");
                location.href = httpUtil.home();
            },
            getNewOrderCount() {
                if (this.$store.getters.user.token === undefined) {
                    return;
                }
                const that = this;
                httpUtil.post(this, 'message', 'getNewOrderCount', {token: this.$store.getters.user.token}, function (resp) {
                    const dt = resp.body;
                    that.$set(that.$data, 'newOrderCount', JSON.parse(dt.data).count)
                })
            },
            //登录方法
            loginDo(formName) {
                const that = this;
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        httpUtil.post(that, 'user', 'loginAdmin', that.loginForm, function (data) {
                            const dt = data.body;
                            //console.log(dt)
                            //console.log(dt.code)
                            if (dt.code !== 0) {
                                return;
                            }
                            that.$message({
                                message: '登录成功',
                                type: 'success'
                            });
                            const user = dt.data;
                            console.log("token = " + user.token)
                            that.$set(that.$data, 'user', user);
                            that.$store.commit('loginSuccess', user);
                            that.$cookie.set('token', user.token);
                            // console.log()
                            that.$set(that.$data, 'loginWindow', false);
                            that.$set(that.$data.store, 'login', 1);
                        });
                    } else {
                        return false;
                    }
                });
            },

            handleOpen(key, keyPath) {
                // this.addTab(key)
                //console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                // console.log(key, keyPath);
            },
            handleSelect(key, keyPath) {
                //console.log(key, keyPath);
            },
            selectItem(key, keyPath) {
                // Vue.router.push({path: key})
                // console.log('APP ==> selectItem ==> key={' + key + '}'+'==> keyPath={' + keyPath + '}')
                // //判断是否存在
                // for (let mapKey in Menu) {
                //     if (mapKey === key) {
                //         // this.addTab(Menu[mapKey].name)
                //     }
                // }
                // console.log("路由跳转："+key)
                console.log(keyPath[1])
                this.$router.push({path: keyPath[1]})
            }
        }
    }
</script>

<style>
</style>
