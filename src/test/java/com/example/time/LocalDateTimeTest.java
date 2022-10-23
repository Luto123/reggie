package com.example.time;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: YangQin
 * @className: LocalDateTime
 * @description: LocalDateTime
 * @date: 2022/10/23 11:10
 * @other:
 */
@SpringBootTest
public class LocalDateTimeTest {
    @Test
    public void test1(){
        String beginTime = "2022-10-23 12:01:24";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime begin = LocalDateTime.parse(beginTime, df);
        System.out.println(begin);
        System.out.println(LocalDateTime.now().format(df));
    }
}
