package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.ShoppingCart;

/**
 * @author: YangQin
 * @ClassName: ShoppingCartService
 * @Description: ShoppingCartService
 * @date: 2022/10/23 15:53
 * @Other:
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 清空用户id下的购物车
     * @param currentId id
     */
    void cleanCart(Long currentId);

    /**
     * 给用户添加购物车里的东西
     * @param id 用户id
     * @param shoppingCart 购物车
     */
    ShoppingCart add(Long id, ShoppingCart shoppingCart);
}
