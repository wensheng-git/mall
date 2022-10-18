package com.wensheng.controller;

import com.wensheng.resposeVo.CategoryVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CategoryController {
    @Autowired
    MallCategoryService mallCategoryService;

    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> getAll(){
        return mallCategoryService.selectAll();
    }

}
