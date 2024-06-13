package com.summer.auth.dao;

import com.summer.common.config.datasource.SwitchDataSource;
import com.summer.common.mapper.AndiUserMapper;
import com.summer.common.model.andi.AndiUser;
import jakarta.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:11
 */
@Repository
@SwitchDataSource
public class AndiDAO {
    @Resource
    private SqlSessionTemplate template;

    public void createUser(AndiUser user) {
        final AndiUserMapper mapper = template.getMapper(AndiUserMapper.class);
        mapper.createUser(user);
    }
 
    public AndiUser queryUserByUsername(String username) {
        final AndiUserMapper mapper = template.getMapper(AndiUserMapper.class);
        final List<AndiUser> andiUserList = mapper.selectUserByUsername(username);
        if (!CollectionUtils.isEmpty(andiUserList)) {
            return andiUserList.getFirst();
        }
        return null;
    }
}
