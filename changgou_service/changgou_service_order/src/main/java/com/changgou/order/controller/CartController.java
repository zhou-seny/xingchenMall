package com.changgou.order.controller;


import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    @RequestMapping("/addCart")
    public Result addCart(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num){


        // String username = "zhangsan";
        String username = tokenDecode.getUserInfo().get("username");
        cartService.addCart(skuId, num, username);
        return new Result(true, StatusCode.OK, "添加购物车成功");
    }

    @RequestMapping("/cartList")
    public Map cartList(){
        String username = tokenDecode.getUserInfo().get("username");
        // String username = "zhangsan";
        return cartService.cartList(username);
    }
}
