package com.summer.gateway.service;

import com.summer.common.model.andi.AndiUserDO;
import com.summer.common.model.response.AndiResponse;
import com.summer.gateway.dao.AndiDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommonService {
    private final AndiDAO andiDao;

    public AndiResponse<?> test(AndiUserDO user) {
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
