package com.summer.gateway.service;

import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.util.RedisUtils;
import com.summer.gateway.dao.AndiDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:11
 */
@Service
@Slf4j
public class CommonService {
    @Resource
    private AndiDAO andiDao;
    @Resource
    private RedisUtils redisUtils;

    public AndiResponse<?> test(AndiUser user) {
        log.info("CommonService.test");
        AndiResponse<?> response = new AndiResponse<>();
        try {
            andiDao.createUser(user);
            response.withCode(AndiResponse.RESPONSE_SUCCESS);
            response.withMsg(AndiResponse.MSG_SUCCESS);
        } catch (Exception e) {
            log.error("CommonService.test error", e);
            response.withCode(AndiResponse.RESPONSE_FAIL);
            response.withMsg(AndiResponse.MSG_FAIL);
        }
        return response;
    }
}
