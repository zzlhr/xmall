export default [
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
            {
                itemIndex: '/goods/classify',
                itemName: '分类管理',
                compName: 'Classify'
            }
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
]
