package com.wensheng.resposeVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseVo<T> {
    private T data;
    private int status;
    private String msg;

    public ResponseVo(T data, int status){
       this.data=data;
       this.status=status;
    }
    public ResponseVo(int status, String msg){
        this.status=status;
        this.msg=msg;
    }
    public ResponseVo(int status, T data){
        this.status=status;
        this.data=data;
    }
}
