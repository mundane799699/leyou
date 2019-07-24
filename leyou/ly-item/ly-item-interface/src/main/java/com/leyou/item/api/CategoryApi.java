package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:03
 * @Description:
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据id查询商品分类
     * @param ids
     */
    @GetMapping("list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);

}
