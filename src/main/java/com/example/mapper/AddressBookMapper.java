package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: YangQin
 * @ClassName: AddressBookMapper
 * @Description: AddressBookMapper
 * @date: 2022/10/23 15:31
 * @Other:
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
