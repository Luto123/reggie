package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: CategoryMapper
 * @Description: CategoryMapper  菜品分类
 * @date: 2022/10/21 9:37
 * @Other:
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
