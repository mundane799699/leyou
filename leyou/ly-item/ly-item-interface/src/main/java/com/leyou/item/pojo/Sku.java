package com.leyou.item.pojo;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;
    private String indexes;
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;
    @Transient
    private Integer stock;
}
