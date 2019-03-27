/**
 *  加载模块
 */

import IndexHome from '../pages/IndexHome'
import UserList from '../pages/user/List'
import UserInfo from '../pages/user/Info'
import EnterpriseList from '../pages/enterprise/List'
import UserEdit from '../pages/user/Edit'
import MenuList from '../pages/auth/MenuList'
import AuthGroup from '../pages/auth/AuthGroup'
import EditAuthValue from '../pages/auth/EditAuthValue'
import GoodsList from '../pages/goods/List'
import GoodsEdit from '../pages/goods/Edit'
import OrderList from '../pages/order/List'
import SystemIndex from '../pages/system/Index'
import SupplierList from '../pages/supplier/List'
import SupplierEdit from '../pages/supplier/Edit'
import UserIndex from '../pages/user/UserIndex'
import MessageList from '../pages/message/List'
import SaleStatistics from '../pages/statistics/SaleStatistics'
import Message from '../pages/system/Message'
import Classify from "~/pages/goods/Classify";

const routes = [
    {
        path: '/',
        component: IndexHome,
        children: [
            {
                path: '/user',
                component: UserIndex,
                children: [
                    {
                        path: 'list',
                        component: UserList
                    },
                    {
                        path: 'getUser/:uid',
                        component: UserInfo
                    }, {
                        path: 'addUser',
                        component: UserEdit
                    }
                ]
            },
            {
                path: '/enterprise/list',
                component: EnterpriseList,
            },
            {
                path: '/auth/menulist',
                component: MenuList
            }, {
                path: '/auth/authgroup',
                component: AuthGroup
            }, {
                path: '/auth/editAuthValue',
                component: EditAuthValue
            },
            {
                path: '/goods/list',
                component: GoodsList
            },
            {
                path: '/goods/add',
                component: GoodsEdit
            },
            {
                path: '/goods/edit',
                name: 'GoodsEdit',
                component: GoodsEdit
            },

            {
                path: '/order/list',
                component: OrderList,
                props: true,
            }, {
                path: '/system/index',
                component: SystemIndex,
            },
            {
                path: '/supplier/list',
                component: SupplierList,
            },
            {
                path: '/supplier/edit',
                component: SupplierEdit
            },
            {
                path: '/message/list',
                component: MessageList,
            },
            {
                path: '/statistics/sale',
                component: SaleStatistics
            },
            {
                path: '/message/message',
                component: Message,
            },
            {
                path: '/goods/classify',
                name: 'Classify',
                component: Classify
            }
        ]
    }
];

export default routes

