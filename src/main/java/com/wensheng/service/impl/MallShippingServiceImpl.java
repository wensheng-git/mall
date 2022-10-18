package com.wensheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wensheng.entity.MallShipping;
import com.wensheng.entity.formEntity.ShippingForm;
import com.wensheng.mapper.MallShippingMapper;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallShippingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wensheng.enums.ResponseEnum.*;

/**
* @author 86159
* @description 针对表【mall_shipping】的数据库操作Service实现
* @createDate 2022-10-02 09:57:26
*/
@Service
public class MallShippingServiceImpl extends ServiceImpl<MallShippingMapper, MallShipping>
    implements MallShippingService{
    @Autowired
    MallShippingMapper shippingMapper;

    public ResponseVo<Map<String,Integer>> addShipping(Integer uid,ShippingForm shippingForm){
        MallShipping mallShipping = new MallShipping();
        BeanUtils.copyProperties(shippingForm,mallShipping);
        mallShipping.setUserId(uid);
        int row = shippingMapper.insertByPrimaryKey(mallShipping);
        if (row<=0) {
            new ResponseVo<>(ERROR.getStatus(),ERROR.getMsg());
        }

        HashMap<String,Integer> map = new HashMap();
        map.put("shippingId",mallShipping.getId());
       return new ResponseVo(SUCCESS.getStatus(),map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row == 0) {
            return new ResponseVo(DELETE_SHIPPING_FAIL.getStatus(),DELETE_SHIPPING_FAIL.getMsg());
        }
        return new ResponseVo(SUCCESS.getStatus(),SUCCESS.getMsg());
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
        MallShipping shipping = new MallShipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0) {
            return new ResponseVo(ERROR.getStatus(),ERROR.getMsg());
        }
        return new ResponseVo(SUCCESS.getStatus(),SUCCESS.getMsg());
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<MallShipping> mallShippings = shippingMapper.selectByUserId(uid);
        PageInfo pageInfo = new PageInfo(mallShippings);
        pageInfo.setList(mallShippings);
        return new ResponseVo(SUCCESS.getStatus(),pageInfo);
    }

}




