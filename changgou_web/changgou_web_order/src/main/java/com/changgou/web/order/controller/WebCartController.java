package com.changgou.web.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/webCart")
public class WebCartController {

    @Autowired
    private CartFeign cartFeign;

    @RequestMapping("/cart")
    public String cart(Model model){
        Map map = cartFeign.cartList();
        model.addAttribute("items", map);
        return "cart";
    }

    @RequestMapping("/add")
    public Result<Map> addCart(String skuId, Integer num){
        cartFeign.addCart(skuId, num);

        Map map = cartFeign.cartList();
        return new Result<>(true, StatusCode.OK, "添加购物车成功", map);
    }
}
