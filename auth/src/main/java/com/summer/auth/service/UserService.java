package com.summer.auth.service;

import com.summer.auth.config.MetricsTracker;
import com.summer.auth.dao.AndiDAO;
import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.response.AndiResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

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
    @Resource
    MetricsTracker metricsTracker;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    public AndiResponse<?> queryUser(final String username) {
        final AndiUser andiUser = andiDAO.queryUserByUsername(username);
        return AndiResponse.success(andiUser);
    }
}
