package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.OrderDetail;
import com.example.mapper.OrderDetailMapper;
import com.example.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: YangQin
 * @className: OrderDetailServiceImpl
 * @description: OrderDetailServiceImpl
 * @date: 2022/10/23 18:47
 * @other:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {

    /**
     * 根据一个订单号返回所有对应的OrderDetail
     * @param orderId id
     * @return 结果
     */
    public List<OrderDetail> getListById(String orderId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return this.list(queryWrapper);
    }
}
