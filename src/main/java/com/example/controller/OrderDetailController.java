package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.OrderDetail;
import com.example.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: YangQin
 * @className: OrderDetailController
 * @description: OrderDetailController
 * @date: 2022/10/23 18:47
 * @other:
 */
@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

}
