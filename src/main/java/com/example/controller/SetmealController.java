package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.DishDto;
import com.example.dto.SetmealDto;
import com.example.entity.*;
import com.example.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: YangQin
 * @className: SetmealController
 * @description: SetmealController  套餐管理
 * @date: 2022/10/22 13:04
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增套餐
     *
     * @param setmealDto 套餐信息
     * @return 响应
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//清除所有套餐数据
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("请求保存新的套餐信息:" + setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> list(int page, int pageSize, String name) {
        log.info("发起分页请求page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器和条件构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Setmeal::getName, name).eq(Setmeal::getIsDeleted, 0);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            //根据分类id查询
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids 套餐id
     * @return 响应
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//清除所有套餐数据
    public R<String> delete(Long[] ids) {
        log.info("请求尝试删除套餐：{}", Arrays.toString(ids));
        UpdateWrapper<Setmeal> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_deleted", 1).in("id", ids);
        setmealService.update(updateWrapper);
        UpdateWrapper<SetmealDish> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.set("is_deleted", 1).in("setmeal_id", ids);
        setmealDishService.update(updateWrapper1);
        return R.success("删除成功");
    }

    /**
     * 通过id获取套餐信息
     *
     * @param id id
     * @return 套餐信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getOne(@PathVariable Long id) {
        log.info("请求获取单个套餐信息:" + id);
        SetmealDto setmealDto = new SetmealDto();
        //获取套餐dish信息
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        //获取套餐信息
        Setmeal byId = setmealService.getById(id);
        setmealDto.setCategoryName(byId.getName());
        BeanUtils.copyProperties(byId, setmealDto);
        System.out.println();
        System.out.println();
        System.out.println(setmealDto);
        System.out.println();
        System.out.println();
        return R.success(setmealDto);
    }

    /**
     * 修改信息
     *
     * @param setmealDto 修改后的信息
     * @return 响应
     */
    @PutMapping
    public R<String> saveUpdate(@RequestBody SetmealDto setmealDto) {
        log.info("请求保存经修改的套餐信息:" + setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功");
    }

    /**
     * 批量改变套餐信息
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids) {
        log.info("请求将id为{} 的套餐状态更改为{}", ids, status);
        UpdateWrapper<Setmeal> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", status).in("id", ids);
        setmealService.update(updateWrapper);
        return R.success("更改状态成功");
    }

    /**
     * 获取列表信息
     *
     * @param categoryId 分类ID
     * @param status     是套餐还是菜品
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#categoryId+'_'+#status")//载入缓存
    public R<List<Setmeal>> list(Long categoryId, int status) {
        log.info("请求分类为{}的{}", categoryId, status);
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId).eq("status", status).eq("is_deleted",0);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 通过点击图片返回图片信息
     *
     * @param id 菜品ID
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable String id) {
        List<DishDto> list = new ArrayList<>();
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        Setmeal setmeal = setmealService.getById(id);
        dishDto.setCopies(1);
        if (dish != null) {
            BeanUtils.copyProperties(dish, dishDto);
        } else {
            BeanUtils.copyProperties(setmeal,dishDto);
        }

        log.info("请求获取一条菜品{}", dish);
        log.info("请求获取一条套餐{}", setmeal);
        list.add(dishDto);
        return R.success(list);
    }

}
