package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.entity.Orders;
import com.example.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: YangQin
 * @className: OrderController
 * @description: OrderController
 * @date: 2022/10/23 10:50
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页获取订单信息
     *
     * @param page      第几页
     * @param pageSize  页面大小
     * @param number    订单ID
     * @param beginTime 范围订单开始时间
     * @param endTime   范围顶顶那结束时间
     * @return 响应
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, String number, String beginTime, String endTime) {
        log.info("请求发起分页请求订单信息:page={},pageSize={},number={},beginTime={},endTime={}", page, pageSize, number, beginTime, endTime);
        //构造分页
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        //订单号相似
        queryWrapper.like(number != null, "number", number);
        //确认时间范围
        queryWrapper.ge(beginTime != null, "order_time", beginTime);
        queryWrapper.le(endTime != null, "order_time", endTime);
        Page<Orders> res = orderService.page(pageInfo, queryWrapper);
        log.info("订单page信息：" + res.getRecords());
        return R.success(res);
    }

    /**
     * 更改订单状态
     * @return 响应
     */
    @PutMapping
    public R<String> status(@RequestBody Orders orders) {
        log.info("请求更改订单 {}状态为 {}", orders.getId(), orders.getStatus());
        orderService.updateById(orders);
        return R.success("成功");
    }
}
