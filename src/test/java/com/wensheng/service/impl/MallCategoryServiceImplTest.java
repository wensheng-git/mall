package com.wensheng.service.impl;

import com.wensheng.service.MallCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Slf4j
class MallCategoryServiceImplTest {
    @Autowired
    MallCategoryService mallCategoryService;

    @Test
    void selectAll() {
        log.info("{}", mallCategoryService.selectAll());
    }
    @Test
    void queryIdSet(){
        Set<Integer> result = new HashSet<>();
        mallCategoryService.queryIdSet(100001, result);
       log.info("{}",result);
    }
}