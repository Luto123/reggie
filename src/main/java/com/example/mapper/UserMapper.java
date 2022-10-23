package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: UserMapper
 * @Description: UserMapper
 * @date: 2022/10/23 10:51
 * @Other:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
