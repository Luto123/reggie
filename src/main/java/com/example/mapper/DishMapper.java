package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: DishMapper
 * @Description: DishMapper 菜品
 * @date: 2022/10/21 10:17
 * @Other:
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
