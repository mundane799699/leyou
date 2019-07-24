package com.leyou.order.dto;

import com.leyou.common.dto.CartDTO;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private Long addressId;
    @NotNull
    private Integer paymentType;
    @NotNull
    private List<CartDTO> carts;
}
