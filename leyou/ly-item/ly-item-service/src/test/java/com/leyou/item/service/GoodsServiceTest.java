package com.leyou.item.service;

import com.leyou.common.dto.CartDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @org.junit.Test
    public void decreaseStock() {
        List<CartDTO> list = Arrays.asList(new CartDTO(2600242L, 2), new CartDTO(2600248L, 2));
        goodsService.decreaseStock(list);
    }
}