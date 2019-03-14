package com.lhrsite.xshop.service.impl;


import com.lhrsite.xshop.core.exception.ErrEumn;
import com.lhrsite.xshop.core.exception.XShopException;
import com.lhrsite.xshop.po.*;
import com.lhrsite.xshop.repository.GoodsRepository;
import com.lhrsite.xshop.repository.MessageRepositroy;
import com.lhrsite.xshop.repository.OrderDetailsRepository;
import com.lhrsite.xshop.repository.OrderRepository;
import com.lhrsite.xshop.service.AddressService;
import com.lhrsite.xshop.service.BuyCarService;
import com.lhrsite.xshop.service.OrderService;
import com.lhrsite.xshop.service.UserService;
import com.lhrsite.xshop.vo.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {

    private JPAQueryFactory queryFactory;

    private final OrderRepository orderRepository;
    private final UserService userService;

    private final OrderDetailsRepository orderDetailsRepository;
    private final BuyCarService buyCarService;
    private final AddressService addressService;
    private final MessageRepositroy messageRepositroy;
    private final GoodsRepository goodsRepository;
    private final JdbcTemplate jdbcTemplate;

    public OrderServiceImpl(EntityManager entityManager, OrderRepository orderRepository, UserService userService, OrderDetailsRepository orderDetailsRepository, BuyCarService buyCarService, AddressService addressService, MessageRepositroy messageRepositroy, GoodsRepository goodsRepository, JdbcTemplate jdbcTemplate) {
        super(entityManager);
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderDetailsRepository = orderDetailsRepository;
        this.buyCarService = buyCarService;
        this.addressService = addressService;
        this.messageRepositroy = messageRepositroy;
        this.goodsRepository = goodsRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.queryFactory = getQueryFactory();
    }

    /**
     * 生成订单id
     *
     * @return 订单id
     */
    private String createOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = sdf.format(new Date(System.currentTimeMillis()));
        return dateString + randomOrderIdEnd();
    }

    @Override
    public OrderVO createOrder(String token, Integer addressId) throws XShopException {

        // 获取用户购物车
        List<BuyCarVO> buyCarVOS = buyCarService.getBuyCar(token);
        List<String> buyCarIds = new ArrayList<>();

        if (buyCarVOS.size() == 0) {
            // 购物车为空
            throw new XShopException(ErrEumn.BUY_CAR_IS_NULL);
        }
        Order order = new Order();

        order.setOrderId(createOrderId());

        List<OrderDetails> orderDetails = new ArrayList<>();
        BigDecimal orderAmount = new BigDecimal(0);
        BigDecimal despatchMoney = new BigDecimal(0);
        // 优惠
        BigDecimal offer = new BigDecimal(0);
        for (int i = 0; i < buyCarVOS.size(); i++) {

            buyCarIds.add(buyCarVOS.get(i).getId());

            orderDetails.add(buyCarVOSToOrderDetails(buyCarVOS.get(i), order.getOrderId(), i));
            // 算单个商品价格（价格*数量）

            Map<String, BigDecimal> result = settleAccountsOneGoods(buyCarVOS, orderAmount, offer, i, despatchMoney);
            orderAmount = result.get("orderAmount");
            despatchMoney = result.get("despatchMoney");
            offer = result.get("offer");
        }

        User user = userService.tokenGetUser(token);
        Address address = addressService.getAddressById(addressId);
        System.out.println(address.getAddr().indexOf('县'));
        if (address.getAddr().indexOf('县') > 0) {
            // 县里双倍运费
            order.setDespatchMoney(despatchMoney.add(despatchMoney));
        } else {
            order.setDespatchMoney(despatchMoney);
        }
        order.setUserId(user.getUid());
        order.setOrderAmount(orderAmount);
        order.setOffer(offer);
        order.setStatus(0);
        order.setAddrId(addressId);
        Order order1 = orderRepository.save(order);
        System.out.println(order1);


        OrderVO orderVO = new OrderVO();
        orderVO.setOrder(order1);
        orderVO.setOrderDetails(orderDetailsRepository.saveAll(orderDetails));
        // 成功下单删除购物车商品
        for (String buyCarId : buyCarIds) {
            buyCarService.deleteBuyCar(buyCarId);
        }
        //下单成功减少库存
        List<Goods> goodses = new ArrayList<>();
        buyCarVOS.forEach(buyCarVO -> {
            Goods goods = buyCarVO.getGoods();
            goods.setStock(goods.getStock() - buyCarVO.getNumber());
            // 销量累加
            goods.setSalesVolume(goods.getSalesVolume() + buyCarVO.getNumber());
            goodses.add(goods);
        });
        goodsRepository.saveAll(goodses);

        // 通知管理员处理订单
        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMessageStatus(0);
        // todo: 该处应当设置通过查询获取管理员，但暂时只是默认通知了id为1的用户
        message.setInceptUser(1);
        message.setSendUser(0);
        message.setMessageValue(
                ("您有一个订单待处理: 用户 {{userName}}({{userPhone}}) " +
                        "下了一个订单，订单号:{{orderId}},请及时处理!")
                        .replace("{{userName}}", user.getUsername().toString())
                        .replace("{{userPhone}}", user.getUsername())
                        .replace("{{orderId}}", order.getOrderId())
        );
        message.setMessageType(1);
        System.out.println(message);
        messageRepositroy.save(message);
        return orderVO;
    }

    public Map<String, BigDecimal> settleAccounts(String token) throws XShopException {
        // 获取用户购物车
        List<BuyCarVO> buyCarVOS = buyCarService.getBuyCar(token);
        if (buyCarVOS.size() == 0) {
            // 购物车为空
            throw new XShopException(ErrEumn.BUY_CAR_IS_NULL);
        }

        BigDecimal orderAmount = new BigDecimal(0);
        BigDecimal despatchMoney = new BigDecimal(0);
        // 优惠
        BigDecimal offer = new BigDecimal(0);
        for (int i = 0; i < buyCarVOS.size(); i++) {
            Map<String, BigDecimal> result = settleAccountsOneGoods(buyCarVOS, orderAmount, offer, i, despatchMoney);
            orderAmount = result.get("orderAmount");
            despatchMoney = result.get("despatchMoney");
            offer = result.get("offer");
        }

        Map<String, BigDecimal> zhang = new HashMap<>();
        zhang.put("orderAmount", orderAmount);
        zhang.put("offer", offer);
        zhang.put("despatchMoney", despatchMoney);
        return zhang;

    }

    private Map<String, BigDecimal> settleAccountsOneGoods(List<BuyCarVO> buyCarVOS, BigDecimal orderAmount, BigDecimal offer, int i, BigDecimal despatchMoney) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("orderAmount", new BigDecimal(0));
        result.put("offer", new BigDecimal(0));
        result.put("despatchMoney", new BigDecimal(0));

        if (buyCarVOS.get(i).getGoods().getSaleStatus() == 1) {
            BigDecimal goodsSalePrice = buyCarVOS.get(i).getGoods()
                    .getSalePrice();
            BigDecimal number = new BigDecimal(buyCarVOS.get(i).getNumber());
            BigDecimal addUp = goodsSalePrice.multiply(number);
            orderAmount = orderAmount.add(addUp);
            result.put("orderAmount", orderAmount);
            offer = offer.add(
                    (
                            buyCarVOS.get(i).getGoods().getOriginalPrice()
                                    .subtract(
                                            buyCarVOS.get(i).getGoods().getSalePrice()
                                    )
                    ).multiply(number)
            );
            result.put("offer", offer);
        } else {
            BigDecimal goodsOriginalPrice = buyCarVOS.get(i).getGoods()
                    .getOriginalPrice();
            BigDecimal number = new BigDecimal(buyCarVOS.get(i).getNumber());
            BigDecimal addUp = goodsOriginalPrice.multiply(number);
            orderAmount = orderAmount.add(addUp);
            result.put("orderAmount", orderAmount);


        }
        despatchMoney = despatchMoney.add(
                buyCarVOS.get(i).getGoods().getDespatchMoney().multiply(
                        new BigDecimal(buyCarVOS.get(i).getNumber())
                )
        );
        result.put("despatchMoney", despatchMoney);
        return result;


    }

    private String randomOrderIdEnd() {
        StringBuilder orderIdEnd = new StringBuilder();
        Random random = new Random();
        int[] array = random.ints(3, 0, 10).toArray();
        for (int anArray : array) {
            orderIdEnd.append(anArray);
        }
        return orderIdEnd.toString();
    }

    private OrderDetails buyCarVOSToOrderDetails(BuyCarVO buyCarVO, String orderId, int time) {
        OrderDetails orderDetails = new OrderDetails();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < (4 - String.valueOf(time).length()); i++) {
            str.append("0");
        }
        str.append(time);

        orderDetails.setOdId(orderId + str.toString());
        orderDetails.setGoodsId(buyCarVO.getGoods().getGoodsId());
        orderDetails.setNumber(buyCarVO.getNumber());
        orderDetails.setTransactionPrice(
                buyCarVO.getGoods().getSaleStatus() == 1 ?
                        buyCarVO.getGoods().getSalePrice() :
                        buyCarVO.getGoods().getOriginalPrice());
        orderDetails.setOrderId(orderId);

        return orderDetails;

    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<OrderStatisticsVO> orderStatistics(int type) {
//        select a.times,b.num
//        from (
//                SELECT curdate() as times
//                union all
//                SELECT date_sub(curdate(), interval 1 day) as times
//                union all
//                SELECT date_sub(curdate(), interval 2 day) as times
//                union all
//                SELECT date_sub(curdate(), interval 3 day) as times
//                union all
//                SELECT date_sub(curdate(), interval 4 day) as times
//                union all
//                SELECT date_sub(curdate(), interval 5 day) as times
//                union all
//                SELECT date_sub(curdate(), interval 6 day) as times
//        ) a left join (
//                select date(create_time) as create_time, count(*) as num
//        from order_master
//        group by date(create_time)
//) b on a.times = b.create_time;
        // TODO: mybatis 实现
        String statisticsDaySql = "select a.times,b.num\n" +
                "from (\n" +
                "    SELECT date_sub(curdate(), interval 6 day) as times\n" +
                "    union all\n" +
                "    SELECT date_sub(curdate(), interval 5 day) as times\n" +
                "    union all\n" +
                "    SELECT date_sub(curdate(), interval 4 day) as times\n" +
                "    union all\n" +
                "    SELECT date_sub(curdate(), interval 3 day) as times\n" +
                "    union all\n" +
                "    SELECT date_sub(curdate(), interval 2 day) as times\n" +
                "    union all\n" +
                "    SELECT date_sub(curdate(), interval 1 day) as times\n" +
                "\t\tunion all\n" +
                "\t\tSELECT curdate() as times\n" +
                ") a left join (\n" +
                "  select date(create_time) as create_time, count(*) as num\n" +
                "  from order_master\n" +
                "  group by date(create_time)\n" +
                ") b on a.times = b.create_time;\n";
        String statisticsMonthSql = "select count(order_id) as order_num, DATE_FORMAT(create_time,'%Y-%m') as times " +
                "from `order_master` group by times order by times desc limit 0,11";
        String sql = "";
        switch (type) {
            case 0:
                sql = statisticsDaySql;
                break;
            case 1:
                sql = statisticsMonthSql;
                break;
            default:
                return null;
        }
        log.info("【订单统计】sql={}", sql);
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
            orderStatisticsVO.setTime(resultSet.getString("times"));
            orderStatisticsVO.setOrderNumber(resultSet.getInt("num"));
            return orderStatisticsVO;
        });
    }

    @Override
    public List<GoodsStatisticsVO> goodsStatistics(int type) {
        // TODO: mybatis 实现
        String sql = "select  g.goods_id, g.title, sum(od.number) num, TO_DAYS(NOW())-TO_DAYS(od.create_time) t from order_details od " +
                "left join order_master as om" +
                "on om.order_id=od.order_id" +
                "left join goods as g on od.goods_id=g.goods_id" +
                "where g.goods_id is not null " +
                "and TO_DAYS(NOW())-TO_DAYS(od.create_time) <= 7" +
                "and om.`status`>=0" +
                "group by g.goods_id ";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            GoodsStatisticsVO orderStatisticsVO = new GoodsStatisticsVO();
            orderStatisticsVO.setGoodsId(resultSet.getString("goods_id"));
            orderStatisticsVO.setGoodsName(resultSet.getString("title"));
            orderStatisticsVO.setSaleNumber(resultSet.getInt("num"));

            return orderStatisticsVO;
        });

    }


    @Override
    public List<OrderListVO> orderListByUser(String token, String orderId, Integer orderStatus, long page, long pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        builder.and(qOrder.userId.eq(user.getUid()));
        if (!orderId.equals("")) {
            builder.and(qOrder.orderId.like("%" + orderId + "%"));
        }
        // TODO: 审查逻辑
        if (orderStatus != -1 && orderStatus == 0) {
            // 已发货和未发货都属于未签收
            builder.and(qOrder.status.in(Arrays.asList(0, 1)));
        }
        if (orderStatus != -1 && orderStatus == 1) {
            // 2是已签收的订单
            builder.and(qOrder.status.eq(2));
        }
        QUser qUser = QUser.user;

        // 查询订单列表
//        List<Order> orders = queryFactory.selectFrom(qOrder)
//                .where(builder)
//                .orderBy(qOrder.createTime.desc())
//                .offset((page - 1) * pageSize)
//                .limit(pageSize)
//                .fetch();
        QAddress qAddress = QAddress.address;
        List<OrderListVO> orders = queryFactory.select(
                Projections.bean(
                        OrderListVO.class,
                        qOrder.orderId,
                        qOrder.userId,
                        qOrder.orderAmount,
                        qOrder.despatchMoney,
                        qOrder.offer,
                        qOrder.status,
                        qOrder.createTime,
                        qUser.username,
                        qUser.phone,
                        qUser.email,
                        qAddress.addr
                )
        ).from(qOrder)
                .leftJoin(qUser).on(qOrder.userId.eq(qUser.uid))
                .leftJoin(qAddress).on(qUser.uid.eq(qAddress.uid)
                        .and(qAddress.defaultStatus.eq(1)))
                .where(builder)
                .orderBy(qOrder.createTime.desc())
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .fetch();

        List<String> orderListId = new ArrayList<>();
        orders.forEach(orderListVO -> {
            orderListId.add(orderListVO.getOrderId());
            // 设置订单总价 总价 = 快递费+商品费用
            orderListVO.setOrderMoney(new BigDecimal(0)
                    .add(orderListVO.getDespatchMoney()
                            .add(orderListVO.getOrderAmount())));
            orderListVO.setOrderInfoVOS(new ArrayList<>());
        });
//        List<OrderListVO> orderListVOS = new ArrayList<>();
//        orders.forEach(order -> {
//            orderListId.add(order.getOrderId());
//            OrderListVO orderListVO = new OrderListVO();
//            BeanUtils.copyProperties(order, orderListVO);
//            orderListVO.setOrderInfoVOS(new ArrayList<>());
//            orderListVOS.add(orderListVO);
//
//        });


        // 查询订单详情
        QOrderDetails qOrderDetails = QOrderDetails.orderDetails;
        QGoods qGoods = QGoods.goods;
        List<OrderInfoVO> orderInfoVOS = queryFactory.select(Projections.bean(
                OrderInfoVO.class,
                qOrderDetails.odId,
                qOrderDetails.orderId,
                qOrderDetails.number,
                QGoods.goods,
                qOrderDetails.transactionPrice
        ))
                .from(qOrderDetails)
                .join(qGoods)
                .on(qGoods.goodsId.eq(qOrderDetails.goodsId))
                .where(qOrderDetails.orderId.in(orderListId))
                .orderBy(qOrderDetails.odId.desc())
                .fetch();
        orders.forEach(orderListVO -> {
            orderInfoVOS.forEach(orderInfoVO -> {
                if (orderInfoVO.getOrderId().equals(orderListVO.getOrderId())) {

                    orderInfoVO.setGoods(
                            getGoodsList(
                                    orderInfoVO.getGoods()));

                    orderListVO.getOrderInfoVOS().add(orderInfoVO);
                }
            });
        });


        return orders;
    }

    @Override
    public List<OrderListVO> orderListByUserAdmin(String token, String orderId, long page, long pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);
        if (user == null) {
            throw new XShopException(ErrEumn.USER_NO_EXIST);
        }
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (!orderId.equals("")) {
            builder.and(qOrder.orderId.like("%" + orderId + "%"));
        }
        QUser qUser = QUser.user;

        // 查询订单列表
//        List<Order> orders = queryFactory.selectFrom(qOrder)
//                .where(builder)
//                .orderBy(qOrder.createTime.desc())
//                .offset((page - 1) * pageSize)
//                .limit(pageSize)
//                .fetch();
        QAddress qAddress = QAddress.address;
        List<OrderListVO> orders = queryFactory.select(
                Projections.bean(
                        OrderListVO.class,
                        qOrder.orderId,
                        qOrder.userId,
                        qOrder.orderAmount,
                        qOrder.despatchMoney,
                        qOrder.offer,
                        qOrder.status,
                        qOrder.createTime,
                        qUser.username,
                        qUser.phone,
                        qUser.email,
                        qAddress.addr
                )
        ).from(qOrder)
                .leftJoin(qUser).on(qOrder.userId.eq(qUser.uid))
                .leftJoin(qAddress).on(qUser.uid.eq(qAddress.uid)
                        .and(qAddress.defaultStatus.eq(1)))
                .where(builder)
                .orderBy(qOrder.createTime.desc())
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .fetch();

        List<String> orderListId = new ArrayList<>();
        orders.forEach(orderListVO -> {
            orderListId.add(orderListVO.getOrderId());
            // 设置订单总价 总价 = 快递费+商品费用
            orderListVO.setOrderMoney(new BigDecimal(0)
                    .add(orderListVO.getDespatchMoney()
                            .add(orderListVO.getOrderAmount())));
            orderListVO.setOrderInfoVOS(new ArrayList<>());
        });
//        List<OrderListVO> orderListVOS = new ArrayList<>();
//        orders.forEach(order -> {
//            orderListId.add(order.getOrderId());
//            OrderListVO orderListVO = new OrderListVO();
//            BeanUtils.copyProperties(order, orderListVO);
//            orderListVO.setOrderInfoVOS(new ArrayList<>());
//            orderListVOS.add(orderListVO);
//
//        });


        // 查询订单详情
        QOrderDetails qOrderDetails = QOrderDetails.orderDetails;
        QGoods qGoods = QGoods.goods;
        List<OrderInfoVO> orderInfoVOS = queryFactory.select(Projections.bean(
                OrderInfoVO.class,
                qOrderDetails.odId,
                qOrderDetails.orderId,
                qOrderDetails.number,
                QGoods.goods,
                qOrderDetails.transactionPrice
        ))
                .from(qOrderDetails)
                .join(qGoods)
                .on(qGoods.goodsId.eq(qOrderDetails.goodsId))
                .where(qOrderDetails.orderId.in(orderListId))
                .orderBy(qOrderDetails.odId.desc())
                .fetch();
        orders.forEach(orderListVO -> {
            orderInfoVOS.forEach(orderInfoVO -> {
                if (orderInfoVO.getOrderId().equals(orderListVO.getOrderId())) {
                    orderInfoVO.setGoods(
                            getGoodsList(
                                    orderInfoVO.getGoods()));
                    orderListVO.getOrderInfoVOS().add(orderInfoVO);
                }
            });
        });


        return orders;
    }

    @Override
    public PageVO<OrderListVO> list(String token, String orderId, long page, long pageSize) throws XShopException {
        User user = userService.tokenGetUser(token);

        QOrder qOrder = QOrder.order;
        JPAQuery<Order> orderJPAQuery = queryFactory.selectFrom(qOrder)
                .offset((page - 1) * pageSize)
                .limit(pageSize);
        // 查询订单列表

        PageVO<OrderListVO> pageVO = new PageVO<>();
        pageVO.init(orderJPAQuery.fetchCount(), page, orderListByUserAdmin(token, orderId, page, pageSize));
        return pageVO;
    }

    @Override
    public OrderListVO order(String orderId) {


        QOrder qOrder = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOrder.orderId.eq(orderId));

        QUser qUser = QUser.user;

        QAddress qAddress = QAddress.address;
        List<OrderListVO> orders = queryFactory.select(
                Projections.bean(
                        OrderListVO.class,
                        qOrder.orderId,
                        qOrder.userId,
                        qOrder.orderAmount,
                        qOrder.despatchMoney,
                        qOrder.offer,
                        qOrder.status,
                        qOrder.createTime,
                        qUser.username,
                        qUser.phone,
                        qUser.email,
                        qAddress.addr
                )
        ).from(qOrder)
                .leftJoin(qUser).on(qOrder.userId.eq(qUser.uid))
                .leftJoin(qAddress).on(qUser.uid.eq(qAddress.uid)
                        .and(qAddress.defaultStatus.eq(1)))
                .where(builder)
                .orderBy(qOrder.createTime.desc())
                .fetch();
        System.out.println(orders);

        List<String> orderListId = new ArrayList<>();
        orders.forEach(orderListVO -> {
            orderListId.add(orderListVO.getOrderId());
            // 设置订单总价 总价 = 快递费+商品费用
            orderListVO.setOrderMoney(new BigDecimal(0)
                    .add(orderListVO.getDespatchMoney()
                            .add(orderListVO.getOrderAmount())));
            orderListVO.setOrderInfoVOS(new ArrayList<>());
        });


        // 查询订单详情
        QOrderDetails qOrderDetails = QOrderDetails.orderDetails;
        QGoods qGoods = QGoods.goods;
        List<OrderInfoVO> orderInfoVOS = queryFactory.select(Projections.bean(
                OrderInfoVO.class,
                qOrderDetails.odId,
                qOrderDetails.orderId,
                qOrderDetails.number,
                QGoods.goods,
                qOrderDetails.transactionPrice
        ))
                .from(qOrderDetails)
                .join(qGoods)
                .on(qGoods.goodsId.eq(qOrderDetails.goodsId))
                .where(qOrderDetails.orderId.in(orderListId))
                .orderBy(qOrderDetails.odId.desc())
                .fetch();
        orders.forEach(orderListVO -> {
            orderInfoVOS.forEach(orderInfoVO -> {
                if (orderInfoVO.getOrderId().equals(orderListVO.getOrderId())) {

                    orderInfoVO.setGoods(
                            getGoodsList(
                                    orderInfoVO.getGoods()));

                    orderListVO.getOrderInfoVOS().add(orderInfoVO);
                }
            });
        });


        return orders.get(0);
    }

    @Override
    public Order receipt(String token, String orderId) {
        // todo 未验证用户
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(2);
            return orderRepository.save(order);
        }
        return null;
    }

//    @Override
//    public void consignment(String orderId, String token, Integer deliveryId) throws XShopException {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
//        if (!orderOptional.isPresent()) {
//            throw new XShopException(ErrEumn.ORDER_NOT_EXIST);
//        }
//        Order order = orderOptional.get();
//        order.setStatus(1);
//        order.setDeliveryId(deliveryId);
//        orderRepository.save(order);
//    }

    @Override
    public OrderVO createOrderUseShoppingCar(List<String> shoppingCarIds,
                                             String token, Integer addressId) throws XShopException {
        User user = userService.tokenGetUser(token);
        List<BuyCarVO> buyCars = buyCarService.getBuyCarByIds(shoppingCarIds);

        String orderId = createOrderId();
        // 验证是否为本人购物车商品
        int i = 0;
        for (BuyCarVO buyCar : buyCars) {
            i++;
            if (!buyCar.getUserId().equals(user.getUid())) {
                throw new XShopException(ErrEumn.BUY_CAR_IS_NOT_YOUR);
            }

        }


        // 结算
        OrderSettleAccounts orderSettleAccounts = settleAccounts(buyCars, orderId);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(user.getUid());
        order.setAddrId(addressId);
        order.setOrderAmount(orderSettleAccounts.getGoodsNumber().add(orderSettleAccounts.getDespatchNumber()));
        order.setDespatchMoney(orderSettleAccounts.getDespatchNumber());
        order.setOffer(new BigDecimal(0));
        orderRepository.save(order);


        return null;
    }

    /**
     * 创建订单详情id
     *
     * @return 订单详情id
     */
    private String createOdId(int index) {
        int ORDER_DETAIL_ID_LENGTH = 4;
        StringBuilder odId = new StringBuilder();
        for (int i = 0; i < ORDER_DETAIL_ID_LENGTH - String.valueOf(index).length(); i++) {
            odId.append("0");
        }
        odId.append(index);
        return odId.toString();
    }

    private OrderSettleAccounts settleAccounts(List<BuyCarVO> buyCars, String orderId) {

        // 商品总价
        BigDecimal goodsNumber = new BigDecimal(0);
        // 运费总价
        BigDecimal despatchNumber = new BigDecimal(0);
        int i = 0;
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (BuyCarVO buyCar : buyCars) {
            i++;
            Goods goods = buyCar.getGoods();
            // 按照促销价或者原价计算
            BigDecimal price = goods.getSaleStatus() == 0 ? goods.getOriginalPrice() : goods.getSalePrice();
            // 计算该商品总价格 = 商品当前售价 * 商品数量
            BigDecimal partTotal = price.multiply(new BigDecimal(buyCar.getNumber()));
            goodsNumber = goodsNumber.add(partTotal);

            // 计算运费

            // 判断是否执行免邮数量
            if (goods.getExecFreePostageNum() && goods.getFreePostageNum() <= buyCar.getNumber()) {
                break;
            }

            // 商品累加并 且累加价格大于0 且购买商品大于1
            if (goods.getDespatchIsPlus()
                    && goods.getDespatchPlusMoney().compareTo(new BigDecimal(0)) > 0
                    && buyCar.getNumber() > 1) {
                // 累加运费 总运费 = 运费价格 + (购买数量 - 1) * 累加价格
                despatchNumber = despatchNumber
                        .add(goods.getDespatchMoney()
                                .add(goods.getDespatchPlusMoney()
                                        .multiply(new BigDecimal(buyCar.getNumber() - 1))));
                break;
            }

            despatchNumber = despatchNumber.add(goods.getDespatchMoney());
            // 生产订单详情
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setGoodsId(buyCar.getGoods().getGoodsId());
            orderDetails.setNumber(buyCar.getNumber());
            orderDetails.setOdId(orderId + createOdId(i));
            orderDetails.setOrderId(orderId);
            //TODO: 将改代码块逻辑放在计算价格中，将计算价格部分返回Map改为返回一个po
            orderDetails.setTransactionPrice(goodsNumber);
            orderDetailsList.add(orderDetails);
        }


        OrderSettleAccounts orderSettleAccounts = new OrderSettleAccounts();
        orderSettleAccounts.setDespatchNumber(despatchNumber);
        orderSettleAccounts.setGoodsNumber(goodsNumber);
        orderSettleAccounts.setOrderDetailsList(orderDetailsList);
        return orderSettleAccounts;

    }


    private Goods getGoodsList(Goods goods) {
        goods.setCreateTime(null);
        goods.setContent(null);
        goods.setDescribe(null);
        goods.setPictures(null);
        goods.setUpdateTime(null);
        goods.setUpdateUser(null);
        return goods;
    }
}
