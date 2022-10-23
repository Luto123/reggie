package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.OrderDetail;

import java.util.List;

/**
 * @author: YangQin
 * @ClassName: OrderDetailService
 * @Description: OrderDetailService
 * @date: 2022/10/23 18:47
 * @Other:
 */
public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 根据一个订单号返回所有对应的OrderDetail
     * @param orderId id
     * @return 结果
     */
    public List<OrderDetail> getListById(String orderId);
}
