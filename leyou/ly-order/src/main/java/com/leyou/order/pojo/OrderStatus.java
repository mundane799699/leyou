package com.leyou.order.pojo;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "tb_order_status")
public class OrderStatus {
    @Id
    private Long orderId;

    private Integer status;
    private Date createTime;
    private Date paymentTime;
    private Date consignTime;
    private Date endTime;
    private Date closeTime;
    private Date commentTime;
}
