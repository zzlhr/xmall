import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import My from '@/components/My'
import Class from '@/components/Class'
import ShoppingCart from '@/components/ShoppingCart'
import Commodity from '@/components/Commodity'
import OrderList from '@/components/OrderList'
import OrderInfo from '@/components/OrderInfo'
import Address from '@/components/Address'
import ClassCommodity from '@/components/ClassCommodity'
import UpPwd from '@/components/UpPwd'
import Regist from '@/components/Regist'
import Regist1 from '@/components/Regist1'
import About from '@/components/About'
import SaleList from '@/components/SaleList'
import Login from "@/components/Login";
import EditAddress from "../components/EditAddress";
import ConfirmOrder from "../components/ConfirmOrder";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        }, {
            path: '/home',
            name: 'home',
            component: Home
        }, {
            path: '/my',
            name: 'My',
            component: My
        }, {
            path: '/class',
            name: 'class',
            component: Class
        }, {
            path: '/shoppingCart',
            name: 'ShoppingCart',
            component: ShoppingCart
        },
        {
            path: '/commodity/:id',
            name: 'Commodity',
            component: Commodity
        },
        {
            path: '/orders',
            name: 'OrderList',
            component: OrderList
        },
        {
            path: '/order/:id',
            name: 'OrderInfo',
            component: OrderInfo
        },
        {
            path: '/address',
            name: 'Address',
            component: Address
        },
        {
            path: '/class1c/:cid',
            name: 'ClassCommodity',
            component: ClassCommodity
        },
        {
            path: '/uppwd',
            name: 'UpPwd',
            component: UpPwd
        },
        {
            path: '/regist',
            name: 'Regist',
            component: Regist
        },
        {
            path: '/about',
            name: 'About',
            component: About
        }, {
            path: '/sale',
            name: 'SaleList',
            component: SaleList
        }, {
            path: '/regist1',
            name: 'Regist1',
            component: Regist1,
        }, {
            path: '/login',
            name: 'Login',
            component: Login
        }, {
            path: '/editAddress',
            name: 'EditAddress',
            component: EditAddress
        }, {
            path: "/confirmOrder",
            name: "ConfirmOrder",
            component: ConfirmOrder
        }
    ]
})
