package com.wensheng.service.impl;

import com.google.gson.Gson;
import com.wensheng.entity.Cart;
import com.wensheng.entity.MallProduct;
import com.wensheng.entity.formEntity.CartAddForm;
import com.wensheng.entity.formEntity.CartUpdateForm;
import com.wensheng.mapper.MallProductMapper;
import com.wensheng.resposeVo.CartProductVo;
import com.wensheng.resposeVo.CartVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wensheng.enums.ProductEnum.ON_SALE;
import static com.wensheng.enums.ResponseEnum.*;
@Service
public class MallCartSeviceImpl implements MallCartService {
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";
    @Autowired
    MallProductMapper mallProductMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    Gson gson=new Gson();


    public ResponseVo<CartVo> add(Integer uid, CartAddForm form){
        // redis---uid:pid:cart
        // 先查是否存在,再添加
        MallProduct product = mallProductMapper.selectById(form.getProductId());
        if (product==null) {
            return new ResponseVo<>(PRODUCT_NOT_EXIST.getStatus(),PRODUCT_NOT_EXIST.getMsg());
        }
        if (!product.getStatus().equals(ON_SALE.getStatus())) {
            return new ResponseVo<>(PRODUCT_OFF_SALE_OR_DELETE.getStatus(),PRODUCT_OFF_SALE_OR_DELETE.getMsg());
        }
        if (product.getStock() <= 0) {
            return new ResponseVo<>(PRODUCT_STOCK_ERROR.getStatus(),PRODUCT_STOCK_ERROR.getMsg());
        }

        // 添加先判断购物车有无商品
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String value = ops.get(redisKey, String.valueOf(form.getProductId()));

        Integer quantity=1;
        Cart cart;
        if (StringUtils.isEmpty(value)) {
            cart = new Cart(product.getId(), quantity, form.getSelected());
        }else{
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity()+quantity);
        }

        ops.put(redisKey,String.valueOf(form.getProductId()),gson.toJson(cart));

        //返回的是list列表
        return list(uid);

    }



    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        // redis_hash:(uid:productId:cart)

        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, String> entries = ops.entries(redisKey); // uid的所有pid:cart的键值对

        CartVo cartVo = new CartVo();
        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        List<CartProductVo> cartProductVoList = new ArrayList<>();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            // 一个键值对就是一种商品
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //TODO 需要优化，使用mysql里的in
            MallProduct product = mallProductMapper.selectById(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);
                // 全选字段
                if (!cart.getProductSelected()) selectAll = false;
                //计算总价(只计算选中的)
                if (cart.getProductSelected())  cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
            }
                 cartTotalQuantity+=cart.getQuantity();
        }
        //字段重新设回
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return new ResponseVo<>(SUCCESS.getStatus(),cartVo);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品, 报错
            return new ResponseVo(CART_PRODUCT_NOT_EXIST.getStatus(),CART_PRODUCT_NOT_EXIST.getMsg());
        }

        opsForHash.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }

     public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form){
         HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
         String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

         String value = opsForHash.get(redisKey, String.valueOf(productId));
         if (StringUtils.isEmpty(value)) {
             //没有该商品, 报错
             return new ResponseVo<>(CART_PRODUCT_NOT_EXIST.getStatus(),CART_PRODUCT_NOT_EXIST.getMsg());
         }

         //已经有了，修改内容
         Cart cart = gson.fromJson(value, Cart.class);
         if (form.getQuantity() != null
                 && form.getQuantity() >= 0) {
             cart.setQuantity(form.getQuantity());
         }
         if (form.getSelected() != null) {
             cart.setProductSelected(form.getSelected());
         }

         opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));
         return list(uid);
    }


    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        return new ResponseVo<>(SUCCESS.getStatus(),sum);
    }

    public List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return cartList;
    }
}
