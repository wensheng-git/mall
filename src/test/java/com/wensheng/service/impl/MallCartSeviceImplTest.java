package com.wensheng.service.impl;

import com.wensheng.entity.formEntity.CartAddForm;
import com.wensheng.resposeVo.CartVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
@Slf4j
class MallCartSeviceImplTest {
    @Autowired
    MallCartService mallCartService;
    
    @Test
    void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(28);
        ResponseVo<CartVo> add = mallCartService.add(1, cartAddForm);
        log.info("{}",add);
    }

    @Test
    void list() {
        ResponseVo<CartVo> list = mallCartService.list(1);
        log.info("{}",list);
    }

    @Test
    void delete() {
        ResponseVo<CartVo> delete = mallCartService.delete(1, 26);
        log.info("{}",delete);
    }

    @Test
    void selectAll() {
        ResponseVo<CartVo> cartVoResponseVo = mallCartService.selectAll(1);
        log.info("{}",cartVoResponseVo);
    }

    @Test
    void unSelectAll() {
        ResponseVo<CartVo> cartVoResponseVo = mallCartService.unSelectAll(1);
        log.info("{}",cartVoResponseVo);
    }

    @Test
    void sum() {
        ResponseVo<Integer> sum = mallCartService.sum(1);
        log.info("{}",sum);
    }
}