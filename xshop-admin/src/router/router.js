/**
 *  加载模块
 */

import IndexHome from '../pages/IndexHome'
import UserList from '../components/user/List'
import UserInfo from '../components/user/Info'
import EnterpriseList from '../components/enterprise/List'
import UserEdit from '../components/user/Edit'
import MenuList from '../components/auth/MenuList'
import AuthGroup from '../components/auth/AuthGroup'
import EditAuthValue from '../components/auth/EditAuthValue'
import GoodsList from '../components/goods/List'
import GoodsEdit from '../components/goods/Edit'
import OrderList from '../components/order/List'
import SystemIndex from '../components/system/Index'
import SupplierList from '../components/supplier/List'
import SupplierEdit from '../components/supplier/Edit'
import UserIndex from '../components/user/UserIndex'
import MessageList from '../components/message/List'
import SaleStatistics from '../components/statistics/SaleStatistics'
import Message from '../components/system/Message'
import Classify from "~/components/goods/Classify";

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

