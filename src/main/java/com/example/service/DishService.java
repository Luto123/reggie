package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.DishDto;
import com.example.entity.Dish;

/**
 * @author: YangQin
 * @className: DishService
 * @description: DishService 菜品
 * @date: 2022/10/21 10:19
 * @other:
 */

public interface DishService extends IService<Dish> {
    /**
     * 新增菜品，同时操作两张表dish,dish_flavor
     *
     */
    public void saveWithFlavor(DishDto dishDto);


    /**
     * 根据ID查询对应菜品信息以及口味信息
     */
    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
