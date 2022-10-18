package com.wensheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallProduct;
import com.wensheng.enums.ProductEnum;
import com.wensheng.mapper.MallProductMapper;
import com.wensheng.resposeVo.ProductVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCategoryService;
import com.wensheng.service.MallProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.wensheng.enums.ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE;
import static com.wensheng.enums.ResponseEnum.SUCCESS;

/**
* @author 86159
* @description 针对表【mall_product】的数据库操作Service实现
* @createDate 2022-10-02 09:57:26
*/
@Service
public class MallProductServiceImpl extends ServiceImpl<MallProductMapper, MallProduct>
    implements MallProductService{
    @Autowired
    MallProductMapper mallProductMapper;
    @Autowired
    MallCategoryService mallCategoryService;

    public ResponseVo<PageInfo> queryList(Integer id, Integer pageNum, Integer pageSize){
        // 1:先查id下的所有子id
        HashSet<Integer> result = new HashSet<>();
        if(id!=null) { // id为非必传,不传默认所有
            mallCategoryService.queryIdSet(id, result);
        }

        // 2:查询前先分页
        PageHelper.startPage(pageNum,pageSize);

        // 3:result去查对应的商品
        List<MallProduct>  productList= mallProductMapper.selectByCategoryIdSet(result);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        // 4:设置pageInfo
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return new ResponseVo<>(SUCCESS.getStatus(), pageInfo);
    }

    public ResponseVo<MallProduct> queryById(Integer id){
        // 商品上线状态才可以返回
        MallProduct mallProduct = mallProductMapper.selectById(id);
        if (
                mallProduct.getStatus().equals(ProductEnum.DELETE.getStatus())
                ||
                mallProduct.getStatus().equals(ProductEnum.OFF_SALE.getStatus())
        ) {
            return new ResponseVo<>(PRODUCT_OFF_SALE_OR_DELETE.getStatus(),PRODUCT_OFF_SALE_OR_DELETE.getMsg());
        }

        return new ResponseVo<>(SUCCESS.getStatus(),mallProduct);
    }

}




