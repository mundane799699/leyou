package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void queryCategoryByIds() {
        List<Long> idList = Arrays.asList(1L, 2L, 3L);
        List<Category> categories = categoryClient.queryCategoryListByIds(idList);
        Assert.assertEquals(3, categories.size());
        for (Category category : categories) {
            System.out.println("category = " + category);
        }
    }
}