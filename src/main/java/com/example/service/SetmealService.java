package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.SetmealDto;
import com.example.entity.Setmeal;
import java.util.List;
/**
 * @author: YangQin
 * @className: SetmealService
 * @description: SetmealService
 * @date: 2022/10/21 10:21
 * @other:
 */

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐同时保存套餐和菜品信息
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 保存修改后的套餐信息
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);

}
