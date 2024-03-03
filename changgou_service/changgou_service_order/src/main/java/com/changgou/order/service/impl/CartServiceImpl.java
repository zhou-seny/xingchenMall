package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private final static String CART = "cart_";


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SkuFeign skuFeign;

    @Resource
    private SpuFeign spuFeign;

    /**
     * 添加购物车
     * @param skuId
     * @param num
     * @param username
     */
    @Override
    public void addCart(String skuId, Integer num, String username) {
        // 判断数据库（redis）中有没有该数据
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        if (orderItem != null){
            // 购物车中有该数据，更新购物车中信息
            orderItem.setNum(orderItem.getNum() + num);
            if (orderItem.getNum() + num <= 0){
                redisTemplate.boundHashOps(CART + username).delete(skuId);
                return;
            }
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());
        } else {
            // 添加购物车,封装OrderItem
            Sku sku = (Sku) skuFeign.findById(skuId).getData();
            Spu spu = spuFeign.findSpuById(sku.getSpuId()).getData();
            orderItem = this.ObtainOrderItem(sku, spu, num);
        }

        // 添加到red is中
        redisTemplate.boundHashOps(CART + username).put(skuId, orderItem);
    }

    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    @Override
    public Map cartList(String username) {
        Map map = new HashMap();

        List<OrderItem> cartLists = redisTemplate.boundHashOps(CART + username).values();

        // 总数量
        int totalNum = cartLists.stream().mapToInt(OrderItem::getNum).sum();

        // 总价钱
        double totalMoney = cartLists.stream().mapToDouble(OrderItem::getMoney).sum();

        map.put("totalNum", totalNum);
        map.put("totalMoney", totalMoney);
        map.put("cartLists", cartLists);
        return map;
    }

    private OrderItem ObtainOrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setNum(num);
        orderItem.setPrice(sku.getPrice());
        orderItem.setMoney(orderItem.getPrice() * num);
        orderItem.setPayMoney(orderItem.getPrice() * num);
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight() * num);

        // 分类信息
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
