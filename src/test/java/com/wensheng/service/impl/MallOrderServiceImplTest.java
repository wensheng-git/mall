package com.wensheng.service.impl;

import com.github.pagehelper.PageInfo;
import com.wensheng.resposeVo.OrderVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
@Slf4j
class MallOrderServiceImplTest {
    @Autowired
    MallOrderService mallOrderService;
    @Test
    void create() {
        ResponseVo<OrderVo> orderVoResponseVo = mallOrderService.create(1, 6);
        log.info("{}",orderVoResponseVo);
    }

    @Test
    void list() {
        ResponseVo<PageInfo> list = mallOrderService.list(1, 1, 10);
        log.info("{}",list);
    }

    @Test
    void detail() {
        ResponseVo<OrderVo> detail = mallOrderService.detail(1, 1664789362314L);
        log.info("{}",detail);
    }

    @Test
    void cancel() {
        ResponseVo cancel = mallOrderService.cancel(1, 1664789362314L);
        log.info("{}",cancel);
    }

    @Test
    void paid() {
        mallOrderService.paid(1664792109453L);
    }
}