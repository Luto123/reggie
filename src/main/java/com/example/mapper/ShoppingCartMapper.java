package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @className: ShoppingCartMapper
 * @description: ShoppingCartMapper
 * @date: 2022/10/23 15:53
 * @other:
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
