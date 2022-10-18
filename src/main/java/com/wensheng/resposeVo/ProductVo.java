package com.wensheng.resposeVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {
    private Integer id;
    private Integer caategoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private Integer status;
    private BigDecimal price;

}
