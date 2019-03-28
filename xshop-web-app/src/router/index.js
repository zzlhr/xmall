import Vue from 'vue'
import Router from 'vue-router'
import Home from '../pages/Home'
import My from '../pages/My'
import Class from '../pages/Class'
import ShoppingCart from '../pages/ShoppingCart'
import Commodity from '../pages/Commodity'
import OrderList from '../pages/OrderList'
import OrderInfo from '../pages/OrderInfo'
import Address from '../pages/Address'
import ClassCommodity from '../pages/ClassCommodity'
import UpPwd from '../pages/UpPwd'
import Regist from '../pages/Regist'
import Regist1 from '../pages/Regist1'
import About from '../pages/About'
import SaleList from '../pages/SaleList'
import Login from "../pages/Login";
import EditAddress from "../pages/EditAddress";
import ConfirmOrder from "../pages/ConfirmOrder";
import SelectAddress from "../pages/SelectAddress";
import Classify from "../pages/Classify";

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
        }, {
            path: '/selectAddress',
            name: 'SelectAddress',
            component: SelectAddress
        }, {
            path: '/classify',
            name: 'Classify',
            component: Classify
        }
    ]
})
