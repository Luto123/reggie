package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.DishDto;
import com.example.entity.Category;
import com.example.entity.Dish;
import com.example.entity.Employee;
import com.example.service.CategoryService;
import com.example.service.DishFlavorService;
import com.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: YangQin
 * @className: DishController
 * @description: DishController 菜品管理
 * @date: 2022/10/21 15:41
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    public CategoryService categoryService;
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDto 数据传输对象
     * @return 响应
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("请求新增了一个菜品：" + dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }


    /**
     * @param page     当前页数
     * @param pageSize 页面大小
     * @return page
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        log.info("发起分页请求page={},pageSize={}", page, pageSize);
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name).eq(Dish::getIsDeleted, 0);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据ID返回菜品信息，以及口味信息
     * 实现数据回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 新增菜品
     *
     * @param dishDto 数据传输对象
     * @return 响应
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("请求修改了一个菜品：" + dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 更改菜品停售起售状态
     *
     * @param status 目标状态
     * @param ids    菜品ID
     * @return 响应
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, Long[] ids) {
        log.info("发起请求将菜品:{} 更改状态为:{}", ids, status);
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", status).in("id", ids);
        dishService.update(updateWrapper);
        return R.success("更改成功");
    }

    /**
     * 删除菜品
     *
     * @param ids 菜品ID
     * @return 响应
     */
    @DeleteMapping
    public R<String> delete(Long[] ids) {
        log.info("发起请求将菜品:{} 删除", Arrays.toString(ids));
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_deleted", 1).in("id", ids);
        dishService.update(updateWrapper);
        return R.success("更改成功");
    }


    /**
     * 获取所有菜品
     *
     * @param dish 包含条件的dish
     * @return 响应
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        log.info("发起查询菜品："+dish);
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime).eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }
}
