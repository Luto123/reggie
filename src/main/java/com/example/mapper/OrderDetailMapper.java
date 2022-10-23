package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @className: OrderDetailMapper
 * @description: OrderDetailMapper
 * @date: 2022/10/23 18:46
 * @other:
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
