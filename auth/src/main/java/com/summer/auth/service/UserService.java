package com.summer.auth.service;

import com.summer.auth.dao.AndiDAO;
import com.summer.common.exception.ConflictException;
import com.summer.common.model.andi.AndiUserDO;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.model.vo.CreateUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 16:00
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final AndiDAO andiDAO;
    public AndiResponse<?> getUser(final String username) {
        final AndiUserDO andiUserDO = andiDAO.getUserByUsername(username);
        if (andiUserDO == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return AndiResponse.success(andiUserDO);
    }

    public AndiResponse<?> insertUser(CreateUserVO userInfo) {
        if (this.checkUserExist(userInfo)) {
            throw new ConflictException("该用户已存在");
        }
        final AndiUserDO andiUserDO = this.initUser(userInfo);
        final int result = andiDAO.insertUser(andiUserDO);
        return AndiResponse.success(result);
    }

    private AndiUserDO initUser(CreateUserVO userInfo) {
        return new AndiUserDO();
        //user.setUserId(OtherUtils.getSnowflakeId());
    }

    private boolean checkUserExist(CreateUserVO userInfo) {
        return andiDAO.getUserByUsername(userInfo.getUsername()) != null;
    }
}
