package com.summer.common.mapper;

import com.summer.common.model.andi.AndiUser;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:12
 */
public interface AndiUserMapper {
    int createUser(AndiUser user);

    AndiUser selectUserByUsername(String username);
}
