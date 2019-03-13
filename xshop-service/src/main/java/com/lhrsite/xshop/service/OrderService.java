package com.lhrsite.xshop.service;

import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.Order;
import com.lhrsite.xshop.vo.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {


    @Transactional
    OrderVO createOrder(String token, Integer addressId) throws XShopException;

    Map<String, BigDecimal> settleAccounts(String token) throws XShopException;

    Order updateOrder(Order order);


    /**
     * 统计订单
     *
     * @param type 类型 0为统计，1为月统计
     * @return 订单统计视图
     */
    List<OrderStatisticsVO> orderStatistics(int type);

    /**
     * 统计商品销量
     *
     * @param type
     * @return
     */
    List<GoodsStatisticsVO> goodsStatistics(int type);

    List<OrderListVO> orderListByUser(String token, String orderId, Integer orderStatus, long page, long pageSize) throws XShopException;

    List<OrderListVO> orderListByUserAdmin(String token, String orderId, long page, long pageSize) throws XShopException;


    /**
     * 后台调用的获取订单列表方法
     *
     * @param token    管理员token
     * @param orderId  订单id
     * @param page     页数
     * @param pageSize 每页数量
     * @return 返回订单列表
     * @throws XShopException 抛出定义异常
     */
    PageVO<OrderListVO> list(String token, String orderId, long page, long pageSize) throws XShopException;


    OrderListVO order(String orderId);


    /**
     * 签收
     *
     * @param token   用户token
     * @param orderId 签收订单代号
     * @return 订单对象
     */
    Order receipt(String token, String orderId);


//    void consignment(String orderId, String token, Integer deliveryId) throws XShopException;


    /**
     * 通过购物车下单
     *
     * @param shoppingCarIds 购物车内容编号
     * @param token          用户token
     * @return 订单视图
     */

    OrderVO createOrderUseShoppingCar(List<String> shoppingCarIds,
                                      String token, Integer addressId) throws XShopException;
}
