package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.CustomException;
import com.example.dto.SetmealDto;
import com.example.entity.Dish;
import com.example.entity.Setmeal;
import com.example.entity.SetmealDish;
import com.example.mapper.SetmealMapper;
import com.example.service.SetmealDishService;
import com.example.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: YangQin
 * @className: SetmealServiceImpl
 * @description: SetmealServiceImpl
 * @date: 2022/10/21 10:21
 * @other:
 */
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐同时保存套餐和菜品信息
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐信息
        this.save(setmealDto);
        //保存关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 修改套餐信息
     * @param setmealDto 套餐信息
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //要保存新的数据，想法是删除旧的数据，再保存新的数据
        this.removeById(setmealDto.getId());
        //删除旧的关联信息
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        this.saveWithDish(setmealDto);
    }

}
