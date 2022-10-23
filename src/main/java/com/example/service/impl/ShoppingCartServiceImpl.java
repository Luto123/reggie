package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.ShoppingCart;
import com.example.mapper.ShoppingCartMapper;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author: YangQin
 * @className: ShoppingCartServiceImpl
 * @description: ShoppingCartServiceImpl
 * @date: 2022/10/23 15:54
 * @other:
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    /**
     * 清空用户id下的购物车
     *
     * @param currentId id
     */
    @Override
    public void cleanCart(Long currentId) {
        log.info("用户 {}请求清空他的购物车", currentId);
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentId);
        this.remove(queryWrapper);
    }

    /**
     * 给用户添加购物车里的东西
     *
     * @param id           用户id
     * @param shoppingCart 购物车
     */
    @Override
    public ShoppingCart add(Long id, ShoppingCart shoppingCart) {
        //先从数据库中获得一条数据
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        if (shoppingCart.getDishId() != null) {
            //是菜品
            queryWrapper.eq("dish_id", shoppingCart.getDishId());
        } else {
            //是套餐
            queryWrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }
        ShoppingCart one = this.getOne(queryWrapper);
        //判断数据库中之前是否有这一条数据
        //有就再原基础上+1，没有就新增
        if (one == null) {
            //原来没有
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            one = shoppingCart;
        } else {
            //原来有
            one.setNumber(one.getNumber() + 1);
            this.updateById(one);
        }
        return one;
    }
}
