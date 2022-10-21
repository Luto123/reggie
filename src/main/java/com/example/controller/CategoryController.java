package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.entity.Category;

import java.util.List;

import com.example.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: YangQin
 * @className: CategoryController
 * @description: CategoryController  菜品分类controller
 * @date: 2022/10/21 9:41
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 提示信息
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        log.info("新增分类: " + category);
        return R.success("新增分类成功");
    }


    /**
     * 获取分页展示数据
     *
     * @param page     当前页数
     * @param pageSize 页面大小
     * @return page
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        log.info("发起分页请求page={},pageSize={}", page, pageSize);
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据ID删除分类
     *
     * @param ids 要删除的ids
     * @return 提示信息
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("发起删除菜品分类请求ID：" + ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 修改分类信息
     *
     * @param category 所修改的信息
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("需要修改菜品分类: " + category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据参数查询数据
     *
     * @param category 参数类
     * @return list
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        log.info("请求根据条件获取数据：{}", category);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
