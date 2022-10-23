package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: OrderMapper
 * @Description: OrderMapper
 * @date: 2022/10/23 10:45
 * @Other:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
