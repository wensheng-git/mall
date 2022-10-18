package com.wensheng.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductEnum {
    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3);

    Integer status;
}
