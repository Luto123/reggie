package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.entity.Dish;
import com.example.entity.ShoppingCart;
import com.example.service.DishService;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: YangQin
 * @className: ShoppingCartController
 * @description: ShoppingCartController  购物车信息
 * @date: 2022/10/23 15:54
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private DishService dishService;

    /**
     * 获取list,当前登录用户的购物车列表信息
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("获取购物车参数");
        Long currentId = BaseContext.getCurrentId();
        //从表中获取数据
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentId);
        queryWrapper.orderByDesc("create_time");
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 向购物车中添加信息
     *
     * @param shoppingCart 添加的菜品信息
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户ID
        Long currentId = BaseContext.getCurrentId();
        log.info("用户 {}希望添加一条购物车信息{}", currentId, shoppingCart);
        shoppingCart.setUserId(currentId);
        ShoppingCart one = shoppingCartService.add(currentId, shoppingCart);
        return R.success(one);
    }

    /**
     * 从购物车中减少一种菜品
     *
     * @param shoppingCart 购物车信息
     * @return 响应
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        log.info("用户 {}希望减少一条购物车信息{}", currentId, shoppingCart);
        //先从数据库中获得一条数据
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        if (shoppingCart.getDishId() != null) {
            //是菜品
            queryWrapper.eq("dish_id", shoppingCart.getDishId());
        } else {
            //是套餐
            queryWrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one.getNumber() > 1) {
            //只需要减一
            one.setNumber(one.getNumber() - 1);
            shoppingCartService.updateById(one);
        } else if (one.getNumber() == 1) {
            //只有一条，就直接删除
            shoppingCartService.removeById(one);
            one.setNumber(0);
        }
        return R.success(one);
    }

    @DeleteMapping("clean")
    public R<String> clean() {
        Long currentId = BaseContext.getCurrentId();
        shoppingCartService.cleanCart(currentId);
        return R.success("成功");
    }

}
