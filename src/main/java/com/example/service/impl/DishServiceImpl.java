package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Dish;
import com.example.mapper.DishMapper;
import com.example.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author: YangQin
 * @className: DishServiceImpl
 * @description: DishServiceImpl
 * @date: 2022/10/21 10:20
 * @other:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
