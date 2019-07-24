package com.leyou.item.pojo;

import java.util.List;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Table(name = "tb_spec_group")
@Data
public class SpecGroup {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> params;
}
