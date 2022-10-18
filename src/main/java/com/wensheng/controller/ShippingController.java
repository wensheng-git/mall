package com.wensheng.controller;

import com.wensheng.consts.MallConst;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.ShippingForm;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class ShippingController {
    @Autowired
    MallShippingService shippingService;


    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingForm form,
                          HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.addShipping(user.getId(), form);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm form,
                             HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.update(user.getId(), shippingId, form);
    }

    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
