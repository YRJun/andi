package com.summer.common.mapper;

import com.summer.common.model.andi.AndiUserDO;

import java.util.Optional;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:12
 */
public interface AndiUserMapper {
    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建结果
     */
    int insertUser(AndiUserDO user);
    /**
     * 根据用户名称查询用户对象
     * @param username 用户名称
     * @return 用户对象
     */
    AndiUserDO getUserByUsername(String username);
    /**
     * 根据用户名称查询用户对象
     * @param username 用户名称
     * @return 用户对象
     */
    Optional<AndiUserDO> getUserByUsernameOptional(String username);

}
