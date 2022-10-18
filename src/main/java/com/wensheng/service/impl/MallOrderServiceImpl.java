package com.wensheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wensheng.entity.*;
import com.wensheng.enums.OrderStatusEnum;
import com.wensheng.enums.PaymentTypeEnum;
import com.wensheng.enums.ProductEnum;
import com.wensheng.enums.ResponseEnum;
import com.wensheng.mapper.MallOrderItemMapper;
import com.wensheng.mapper.MallOrderMapper;
import com.wensheng.mapper.MallProductMapper;
import com.wensheng.mapper.MallShippingMapper;
import com.wensheng.resposeVo.OrderItemVo;
import com.wensheng.resposeVo.OrderVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCartService;
import com.wensheng.service.MallOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.wensheng.enums.ResponseEnum.*;

/**
 * @author 86159
 * @description 针对表【mall_order】的数据库操作Service实现
 * @createDate 2022-10-02 09:57:26
 */
/*
* order表,orderItem表和Shippingheproduct之间的联系
* */
@Service
public class MallOrderServiceImpl extends ServiceImpl<MallOrderMapper, MallOrder>
        implements MallOrderService {
    @Autowired
    MallOrderMapper mallOrderMapper;
    @Autowired
    MallOrderItemMapper mallOrderItemMapper;
    @Autowired
    MallProductMapper mallProductMapper;
    @Autowired
    MallShippingMapper mallShippingMapper;
    @Autowired
    MallCartService mallCartService;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //1: shipping校验
        MallShipping mallShipping = mallShippingMapper.selectByIdAndUserId(shippingId, uid);
        if (mallShipping == null) new ResponseVo(SHIPPING_NOT_EXIST.getStatus(), SHIPPING_NOT_EXIST.getMsg());

        //2: 购物车校验
        List<Cart> cartList = mallCartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList))
            return new ResponseVo(CART_SELECTED_IS_EMPTY.getStatus(), CART_PRODUCT_NOT_EXIST.getMsg());

        //购物车选择中的productIdSet
        Set<Integer> productIdSet = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());

        //productIdSet去查productId及对应的product信息
        List<MallProduct> productList = mallProductMapper.selectByIdSet(productIdSet);
        Map<Integer, MallProduct> map = productList.stream()
                .collect(Collectors.toMap(MallProduct::getId, MallProduct -> MallProduct));

        //3:cart里面的productId和数据库有的校验
        // 开始封装Vo
        List<MallOrderItem> orderItemList = new ArrayList<>();
        Long orderNo = System.currentTimeMillis() + new Random().nextInt(999);
        for (Cart cart : cartList) {
            MallProduct product = map.get(cart.getProductId());
            // (商品存在,库存,状态)
            if (product == null)
                return new ResponseVo(PRODUCT_NOT_EXIST.getStatus(), PRODUCT_NOT_EXIST.getMsg());
            if (!ProductEnum.ON_SALE.getStatus().equals(product.getStatus()))
                return new ResponseVo<>(PRODUCT_OFF_SALE_OR_DELETE.getStatus(), PRODUCT_OFF_SALE_OR_DELETE.getMsg());
            if (product.getStock() < cart.getQuantity())
                return new ResponseVo(PRODUCT_STOCK_ERROR.getStatus(), PRODUCT_STOCK_ERROR.getMsg());
            // 4:构建OrderItem(一种商品对应一种)
            MallOrderItem mallOrderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(mallOrderItem);
            // 减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = mallProductMapper.updateById(product);
            if (row <= 0) return new ResponseVo<>(ERROR.getStatus(), ERROR.getMsg());
        }

        // 5:构建Order并入库记录
        MallOrder mallOrder = buildOrder(uid, orderNo, shippingId, orderItemList);
        int row = mallOrderMapper.insertSelective(mallOrder);
        if (row <= 0) return new ResponseVo(ERROR.getStatus(), ERROR.getMsg());
        int batchRow = mallOrderItemMapper.batchInsert(orderItemList);
        if (batchRow <= 0) return new ResponseVo(ERROR.getStatus(), ERROR.getMsg());


        // 6:更新购物车
        for (Cart cart : cartList) {
            mallCartService.delete(uid,cart.getProductId());
        }
        // 7返回OrderVo对象
        OrderVo orderVo = buildOrderVo(mallOrder, orderItemList, mallShipping);
        return new ResponseVo<>(SUCCESS.getStatus(),orderVo);
    }

    private OrderVo buildOrderVo(MallOrder order, List<MallOrderItem> orderItemList, MallShipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }
    private MallOrder buildOrder(Integer uid,
                                 Long orderNo,
                                 Integer shippingId,
                                 List<MallOrderItem> orderItemList
    ) {
        BigDecimal payment = orderItemList.stream()
                .map(MallOrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        MallOrder order = new MallOrder();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }

    private MallOrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, MallProduct product) {
        MallOrderItem item = new MallOrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MallOrder> orderList = mallOrderMapper.selectByUserId(uid);
        //orderList的所有orderNo
        Set<Long> orderNoSet = orderList.stream()
                .map(MallOrder::getOrderNo)
                .collect(Collectors.toSet());
        //1:根据orderNo找OrderItem
        List<MallOrderItem> orderItemList = mallOrderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<MallOrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(MallOrderItem::getOrderNo));
        //orderList的所有的ShippingId
        Set<Integer> shippingIdSet = orderList.stream()
                .map(MallOrder::getShippingId)
                .collect(Collectors.toSet());
        //2:根据shippingIdSet找shipping
        List<MallShipping> shippingList = mallShippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, MallShipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(MallShipping::getId, shipping -> shipping));
        //3:orderList和item和shipping绑定
        List<OrderVo> orderVoList = new ArrayList<>();
        for (MallOrder order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);
        return new ResponseVo<>(SUCCESS.getStatus(),pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {

        // 1:先判断ordre存在
        MallOrder order = mallOrderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return new ResponseVo<>(ORDER_NOT_EXIST.getStatus(),ORDER_NOT_EXIST.getMsg());
        }

        // 2:根据orderNo去查Item
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<MallOrderItem> orderItemList = mallOrderItemMapper.selectByOrderNoSet(orderNoSet);
        // 3:根据shippingId去查shipping
        MallShipping shipping = mallShippingMapper.selectById(order.getShippingId());

        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return new ResponseVo<>(SUCCESS.getStatus(),orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        MallOrder order = mallOrderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return new ResponseVo(ORDER_NOT_EXIST.getStatus(),ORDER_NOT_EXIST.getMsg());
        }
        //只有[未付款]订单可以取消，看自己公司业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            return new ResponseVo(ORDER_STATUS_ERROR.getStatus(),ORDER_STATUS_ERROR.getMsg());
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = mallOrderMapper.updateSelective(order);
        if (row <= 0) {
            return new ResponseVo(ERROR.getStatus(),ERROR.getMsg());
        }

        return new ResponseVo(SUCCESS.getStatus(),SUCCESS.getMsg());
    }

    @Override
    public void paid(Long orderNo) {
        MallOrder order = mallOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getMsg() + "订单id:" + orderNo);
        }
        //只有[未付款]订单可以变成[已付款]，看自己公司业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getMsg() + "订单id:" + orderNo);
        }

        //用No找到的order然后该一下状态
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = mallOrderMapper.updateSelective(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id:" + orderNo);
        }
    }
}




