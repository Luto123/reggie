package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Category;

/**
 * @author: YangQin
 * @ClassName: CategoryService
 * @Description: CategoryService
 * @date: 2022/10/21 9:39
 * @Other:
 */
public interface CategoryService extends IService<Category> {
    /**
     * 自定义根据ID实现删除
     * @param id 删除id
     */
    void remove(Long id);
}
