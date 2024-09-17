package com.summer.auth.dao;

import com.summer.common.config.datasource.SwitchDataSource;
import com.summer.common.mapper.AndiUserMapper;
import com.summer.common.model.andi.AndiUserDO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:11
 */
@Repository
@SwitchDataSource
@RequiredArgsConstructor
public class AndiDAO {

    private final SqlSessionTemplate sqlSessionTemplate;

    public int insertUser(AndiUserDO user) {
        final AndiUserMapper mapper = sqlSessionTemplate.getMapper(AndiUserMapper.class);
        return mapper.insertUser(user);
    }
 
    public AndiUserDO getUserByUsername(String username) {
        final AndiUserMapper mapper = sqlSessionTemplate.getMapper(AndiUserMapper.class);
        return mapper.getUserByUsername(username);
    }

    public AndiUserDO getOptionalUserByUsername(String username) {
        final AndiUserMapper mapper = sqlSessionTemplate.getMapper(AndiUserMapper.class);
        final Optional<AndiUserDO> andiUserOptional = mapper.getUserByUsernameOptional(username);
        return andiUserOptional.orElse(null);
    }
}
