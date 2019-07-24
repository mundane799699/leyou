package com.leyou.order.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "tb_order_detail")
public class OrderDetail {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long orderId;
    private Long skuId;
    private Integer num;
    private String title;
    private Long price;
    private String ownSpec;
    private String image;
}
