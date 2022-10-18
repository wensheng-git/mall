package com.wensheng.controller;

import com.wensheng.consts.MallConst;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.CartAddForm;
import com.wensheng.entity.formEntity.CartUpdateForm;
import com.wensheng.resposeVo.CartVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class CartConroller {
    @Autowired
    MallCartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(), productId, form);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(), productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
