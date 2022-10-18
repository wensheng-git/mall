package com.wensheng.service.impl;

import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallProduct;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class MallProductServiceImplTest {
    @Autowired
    MallProductService mallProductService;
    @Test
    void queryByCategoryList() {
        ResponseVo<PageInfo> pageInfoResponseVo = mallProductService.queryList(100001, 1, 5);
        log.info("{}", pageInfoResponseVo);
    }
    @Test
    void queryByProductId(){
        ResponseVo<MallProduct> mallProductResponseVo = mallProductService.queryById(26);
        ResponseVo<MallProduct> mallProductResponseVo1 = mallProductService.queryById(30);
        log.info("{}",mallProductResponseVo);
        log.info("{}",mallProductResponseVo1);

    }
}