package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: YangQin
 * @className: UserController
 * @description: UserController
 * @date: 2022/10/23 10:52
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @param user    给用户手机号码发送短信
     * @param session session
     * @return 响应
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        log.info("用户想通过手机号码{}登录", phone);
        if (phone != null) {
            //生成验证码
            // TODO: 2022/10/23 更改登录时的默认参数
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//                String code = "6666";
            log.info("为用户 {} 生成的验证码是:{}", phone, code);
            //将短信验证码放在session中
            session.setAttribute(phone, code);
            return R.success("短信发送成功");
        }
        return R.success("短信发送失败");
    }


    /**
     * 登录请求
     *
     * @param map     信息表
     * @param session session
     * @return 响应
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        log.info("发起登陆请求：" + map.toString());
        //获取手机号
        String phone = map.get("phone");
        //获取验证码
        String code = map.get("code");
        //与session中的验证码进行比较
        String sessionCode = (String) session.getAttribute(phone);
        log.info("phone={},code={},sessionCode={}", phone, code, sessionCode);
        if (sessionCode != null && sessionCode.equals(code)) {
            //比对成功直接登录
            //判断是否新用户
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(lambdaQueryWrapper);
            if (one == null) {
                //为新用户
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                session.setAttribute("user",userService.getOne(lambdaQueryWrapper).getId());
            }else{
                session.setAttribute("user",one.getId());
            }
            return R.success(one);
        }
        //不成功
        return R.error("登陆失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.removeAttribute("user");
        return R.success("退出成功");
    }
}

