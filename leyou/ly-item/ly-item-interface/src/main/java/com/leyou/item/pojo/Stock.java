package com.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock; // 秒杀可用库存
    private Integer seckillTotal; // 已秒杀数量
    private Integer stock; // 正常库存
}
