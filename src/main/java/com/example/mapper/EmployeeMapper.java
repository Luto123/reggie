package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: EmployeeMapper
 * @Description: EmployeeMapper 员工信息Mapper
 * @date: 2022/10/20 9:56
 * @Other:
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
