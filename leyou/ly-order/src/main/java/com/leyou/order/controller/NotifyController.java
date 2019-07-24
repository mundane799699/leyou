package com.leyou.order.controller;

import com.leyou.order.service.OrderService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotifyController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/wxpay/notify", produces = "application/xml")
    public Map<String, String> notify(@RequestBody Map<String, String> result) {
        orderService.handleNotify(result);

        log.info("[支付回调] 接收微信支付回调, 结果:{}", result);
        Map<String, String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        return msg;
    }

}
