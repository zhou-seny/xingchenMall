package com.changgou.order.service;

import java.util.Map;

public interface CartService {

    void addCart(String skuId, Integer num, String username);

    Map cartList(String username);
}
