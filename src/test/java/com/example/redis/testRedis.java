package com.example.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: YangQin
 * @className: testRedis
 * @description: testRedis redis测试
 * @date: 2022/10/25 16:57
 * @other:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testRedis {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 测试redis接口是否能够正常工作
     */
    @Test
    public void test(){
        ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("name","杨钦");
        String name = (String) opsForValue.get("name");
        System.out.println(name);
        redisTemplate.delete("name");
        System.out.println((String)opsForValue.get("name"));
    }
}
