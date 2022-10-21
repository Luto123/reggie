package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author: YangQin
 * @className: EmployeeController
 * @description: EmployeeController
 * @date: 2022/10/20 9:59
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request  HttpRequest
     * @param employee 登录信息
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1. 获取前端发送的数据
        String password = employee.getPassword();
        //md5处理密码
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2. 根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 3. 没有查询到就返回null
        if (emp == null) {
            return R.error("登陆失败");
        }
        // 4. 查询到了就比对密码，不一致则登陆失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败(密码错误)");
        }
        // 5. 确认员工状态，
        if (emp.getStatus() == 0) {
            return R.error("登陆失败(账号已经被禁用)");
        }
        // 6. 登陆成功，id存入session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出登录功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 保存新员工
     *
     * @param employee 新增员工信息
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());
        //初始化
        //md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     *
     * @param page     当前页数
     * @param pageSize 页面大小
     * @param name     筛选name条件
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("发起分页请求page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器和条件构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        if (name != null)
            queryWrapper.like(Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("收到需要修改的员工信息: "+employee.toString());
//        long id = Thread.currentThread().getId();
//        log.info("当前线程ID："+id);
        //修改更新时间
//        employee.setUpdateTime(LocalDateTime.now());
        //修改更新人
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改员工信息成功");
    }

    /**
     * 根据ID查询员工信息
     * @param id 查询员工id
     * @return 成功
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
