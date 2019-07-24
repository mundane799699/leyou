package com.leyou.item.service;

import com.leyou.item.pojo.Item;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    public Item saveItem(Item item) {
        // 商品新增
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
