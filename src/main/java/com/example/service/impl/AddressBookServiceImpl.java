package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.AddressBook;
import com.example.mapper.AddressBookMapper;
import com.example.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author: YangQin
 * @className: AddressBookServiceImpl
 * @description: AddressBookServiceImpl
 * @date: 2022/10/23 15:31
 * @other:
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
