package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.dto.OrdersDto;
import com.example.entity.*;
import com.example.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

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
     *
     * @return 响应
     */
    @PutMapping
    public R<String> status(@RequestBody Orders orders) {
        log.info("请求更改订单 {}状态为 {}", orders.getId(), orders.getStatus());
        orderService.updateById(orders);
        return R.success("成功");
    }

    /**
     * 分页获取用户的订单
     *
     * @param page     当前页
     * @param pageSize 页面大小
     * @return 数据
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize) {

        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> pageDto = new Page<>(page, pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        //这里是直接把当前用户分页的结果查询出来，要添加用户id作为查询条件，否则会出现用户可以查询到其他用户的订单情况
        //添加排序条件，根据更新时间排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrdersDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> OrdersDtoList = records.stream().map((item) -> {
            OrdersDto OrdersDto = new OrdersDto();
            //此时的OrdersDto对象里面orderDetails属性还是空，下面准备为他赋值
            Long orderId = item.getId();//获取订单Id
            List<OrderDetail> orderDetailList = this.getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item, OrdersDto);
            //对OrdersDto进行orderDetails属性赋值
            OrdersDto.setOrderDetails(orderDetailList);
            return OrdersDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        pageDto.setRecords(OrdersDtoList);
        return R.success(pageDto);
    }

    /**
     * 再来一单
     *
     * @param order 存储订单id
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders order) {
        log.info("用户发起请求再来一单:" + order);
        order = orderService.getById(order.getId());
        //获取用户ID
        Long currentId = BaseContext.getCurrentId();
        Long orderId = order.getId();
        //再来一单的业务逻辑，给用户清空其购物车，再添加相同的dish
        //先获取orderDetail
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderDetail> detailList = orderDetailService.list(queryWrapper);
        log.info("detailList={}", detailList);
        //清空购物车
        shoppingCartService.cleanCart(currentId);
        //重新装入购物车
        for (OrderDetail orderDetail : detailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            if (orderDetail.getDishId() != null) {
                //是菜品
                shoppingCart.setDishId(orderDetail.getDishId());
            } else if (orderDetail.getSetmealId() != null) {
                //是套餐
                shoppingCart.setSetmealId(orderDetail.getSetmealId());
            }
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setUserId(currentId);
            shoppingCart.setAmount(order.getAmount());
            shoppingCartService.add(currentId, shoppingCart);
        }

        return R.success("成功");
    }


    //通过订单id查询订单明细，得到一个订单明细的集合
    //这为了避免在stream中遍历的时候直接使用构造条件来查询导致eq叠加，从而导致后面查询的数据都是null
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        return orderDetailService.list(queryWrapper);
    }

    /**
     * 用户提交订单
     *
     * @param map 信息表
     * @return 响应2
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Map<String, String> map) {
        //{remark=5555, payMethod=1, addressBookId=1584087145274417153}
        log.info("用户提交了订单:" + map.toString());
        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        //获取用户购物车信息
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(new QueryWrapper<ShoppingCart>().eq("user_id", currentId));
        //删除购物车信息
        shoppingCartService.remove(new QueryWrapper<ShoppingCart>().eq("user_id", currentId));
        for (ShoppingCart shoppingCart : shoppingCartList) {
            System.out.println(shoppingCart);
        }
        //根据地址本查询用户提交地址
        AddressBook addressBook = addressBookService.getOne(new QueryWrapper<AddressBook>().eq("id", map.get("addressBookId")));
        User user = userService.getOne(new QueryWrapper<User>().eq("id", currentId));
        Orders orders = new Orders();
        //初始化order
        orders.setStatus(2);
        orders.setUserId(currentId);
        orders.setAddressBookId(Long.valueOf(map.get("addressBookId")));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(new Random().nextInt() % 2 + 1);
        orders.setRemark(map.get("remark"));
        orders.setPhone(user.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserName(user.getName());
        double countAmount = 0;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            countAmount += shoppingCart.getAmount().doubleValue()*shoppingCart.getNumber();
        }
        orders.setAmount(BigDecimal.valueOf(countAmount));
        //初始化orderDetail
        orderService.save(orders);
        int num = Math.abs(new Random().nextInt()%100000);
        orders.setNumber(String.valueOf(num));
        orderService.updateById(orders);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setOrderId(orders.getId());
            if (shoppingCart.getDishId() != null) {
                orderDetail.setDishId(shoppingCart.getDishId());
            } else if (shoppingCart.getSetmealId() != null) {
                orderDetail.setSetmealId(shoppingCart.getSetmealId());
            }
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetailService.save(orderDetail);
        }
        return R.success("成功");
    }
}
