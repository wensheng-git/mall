package com.wensheng.service.impl;

import com.github.pagehelper.PageInfo;
import com.wensheng.entity.formEntity.ShippingForm;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallShippingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Slf4j
class MallShippingServiceImplTest {
    @Autowired
    MallShippingService mallShippingService;
    @Test
    void addShipping() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("文生");
        shippingForm.setReceiverAddress("广东省广州市");
        ResponseVo<Map<String, Integer>> mapResponseVo = mallShippingService.addShipping(1, shippingForm);
        log.info("{}",mapResponseVo);
    }
    @Test
    void deleteShipping(){
        ResponseVo delete = mallShippingService.delete(1, 5);
        log.info("{}",delete);
    }
    @Test
    void update(){
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("小明");
        mallShippingService.update(1,6,shippingForm);
    }
    @Test
    void list(){
        ResponseVo<PageInfo> list = mallShippingService.list(1, 1, 10);
        log.info("{}",list);
    }
}