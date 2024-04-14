package com.summer.auth.service;

import com.summer.auth.dao.AndiDAO;
import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.request.UserRequest;
import com.summer.common.model.response.AndiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 16:00
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private AndiDAO andiDAO;
    public AndiResponse<?> queryUser(UserRequest request) {
//        //原密码
//        String plain_password = "187965";
//        //gensalt()方法接受一个可选参数(默认10.范围4-31)，它确定哈希的计算复杂性，指数级增长，两倍增加哈希的复杂性，会增加哈希计算的时间。
//        String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());
//        //验证加密之后的密码与原密码是否匹配
//        if (BCrypt.checkpw(plain_password, pw_hash)) {
//            System.out.println("It matches");
//        } else {
//            System.out.println("It does not match");
//        }
        final AndiUser andiUser = andiDAO.queryUserByUsername(request.getUsername());
        return AndiResponse.success(andiUser);
    }
}
