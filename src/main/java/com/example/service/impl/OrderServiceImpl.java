package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Orders;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author: YangQin
 * @className: OrderServiceImpl
 * @description: OrderServiceImpl
 * @date: 2022/10/23 10:47
 * @other:
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

}
