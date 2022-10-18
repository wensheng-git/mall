package com.wensheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wensheng.entity.MallCategory;
import com.wensheng.mapper.MallCategoryMapper;
import com.wensheng.resposeVo.CategoryVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wensheng.consts.MallConst.ROOT_PARENT_ID;
import static com.wensheng.enums.ResponseEnum.SUCCESS;

/**
* @author 86159
* @description 针对表【mall_category】的数据库操作Service实现
* @createDate 2022-10-02 09:57:26
*/
@Service
public class MallCategoryServiceImpl extends ServiceImpl<MallCategoryMapper, MallCategory>
    implements MallCategoryService{
    @Autowired
    MallCategoryMapper mallCategoryMapper;

    public ResponseVo<List<CategoryVo>> selectAll(){
        List<MallCategory> mallCategories = mallCategoryMapper.selectAll();
        //先查询根目录再处理子目录
        List<CategoryVo> categoryVoList = mallCategories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID)) //lambda:参数->实现
                .map(this::categoryToCategoryVo)             //lambda:方法引用....直接引用实现[参数省略]
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());
        findSubCategoryVo(categoryVoList,mallCategories);

        return new ResponseVo(SUCCESS.getStatus(),categoryVoList);
    }

    public CategoryVo categoryToCategoryVo(MallCategory category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    public void findSubCategoryVo(List<CategoryVo> categoryVoList,List<MallCategory> categories){
       //深度优先设置子目录
        for (CategoryVo categoryVo : categoryVoList) {
            //根元素的子目录
             List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (MallCategory category : categories) {
                if (categoryVo.getId().equals(category.getParentId())) {
                    subCategoryVoList.add(categoryToCategoryVo(category));
                }
            }
        // 一个元素的子目录找到后降序排序===>并深度优先
        subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        categoryVo.setSubCategoryVoList(subCategoryVoList);
        findSubCategoryVo(subCategoryVoList,categories);
        }
    }


    public void queryIdSet(Integer id,Set<Integer> result){
        result.add(id);
        List<MallCategory> mallCategories = mallCategoryMapper.selectAll();
        findSubCategoryId(id,result,mallCategories);
    }
    public void findSubCategoryId(Integer id,Set<Integer> result,List<MallCategory> mallCategories){
        // 深度优先查找子id
            for (MallCategory mallCategory : mallCategories) {
                if (mallCategory.getParentId().equals(id)) {
                    //result只收集
                    result.add(mallCategory.getId());
                    findSubCategoryId(mallCategory.getId(),result,mallCategories);
                }
            }
    }



}




