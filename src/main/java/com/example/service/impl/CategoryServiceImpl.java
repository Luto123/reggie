package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.CustomException;
import com.example.entity.Category;
import com.example.entity.Dish;
import com.example.entity.Setmeal;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import com.example.service.DishService;
import com.example.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: YangQin
 * @className: CategoryServiceImpl
 * @description: CategoryServiceImpl 菜品分类实现类
 * @date: 2022/10/21 9:39
 * @other:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类，确定分类是否关联菜品
     *
     * @param id 删除id
     */
    @Override
    public void remove(Long id) {
        //是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        if (dishService.count(dishLambdaQueryWrapper) > 0) {
            //有关联菜品,抛出业务异常
            throw new CustomException("当前分类关联了菜品");
        }
        //是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmealLambdaQueryWrapper) > 0) {
            //有关联套餐,抛出业务异常
            throw new CustomException("当前分类关联了套餐");
        }
        //正常删除
        super.removeById(id);
    }
}
