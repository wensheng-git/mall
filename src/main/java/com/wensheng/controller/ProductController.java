package com.wensheng.controller;

import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallProduct;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    @Autowired
    MallProductService mallProductService;

    @GetMapping()
    public ResponseVo<PageInfo> getProducts(
            @RequestParam(value = "categoryId",required = false) Integer id,
            @RequestParam(value = "pageNum" ,required = false ,defaultValue ="1") Integer pageNum,
            @RequestParam(value = "pageSize" ,required = false,defaultValue = "10") Integer pageSize
    ){
      return mallProductService.queryList(id,pageNum,pageSize);
    }

    @GetMapping("{productId}")
    public ResponseVo<MallProduct> getProductDetail(
            @PathVariable(value = "productId") Integer id
    ){
        return mallProductService.queryById(id);
    }

}
