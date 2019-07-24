package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:08
 * @Description:
 */
public interface SpecificationApi {

    @GetMapping("spec/params")
    List<SpecParam> queryParamList(@RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic);

    /**
     * 查询规格参数组，及组内参数
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    List<SpecGroup> querySpecsByCid(@RequestParam("cid") Long cid);




}
