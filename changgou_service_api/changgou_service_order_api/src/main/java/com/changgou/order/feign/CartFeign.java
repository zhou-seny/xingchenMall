package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "order")
public interface CartFeign {
    @RequestMapping("/cart/addCart")
    public Result addCart(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);

    @RequestMapping("/cart/cartList")
    public Map cartList();
}
