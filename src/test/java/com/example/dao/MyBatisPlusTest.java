package com.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ReggieApplication;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: YangQin
 * @className: MyBatisPlusTest
 * @description: MyBatisPlusTest  测试MyBatisPlus中的几个条件构造器
 * @date: 2022/10/22 11:53
 * @other:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisPlusTest {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 测试条件构造器的按条件查询
     */
    @Test
    public void testSelectByCondition() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        //设置条件sex=0,且名字中有'店员'
        queryWrapper.eq("sex", 0).like("username", "店员");
        List<Employee> list = employeeService.list(queryWrapper);
    }

    /**
     * 通过一个数组查询数据
     */
    @Test
    public void testSelectByArray() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        String[] ids = {"1582989630466813954", "1582989932809023489", "1582990152754130945"};
        queryWrapper.in("id", ids);
        List<Employee> list = employeeService.list(queryWrapper);
    }
}