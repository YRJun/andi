package com.summer.gateway.dao;

import com.summer.common.config.datasource.SwitchDataSource;
import com.summer.common.mapper.AndiInterfaceLogMapper;
import com.summer.common.mapper.AndiUserMapper;
import com.summer.common.model.andi.AndiInterfaceLog;
import com.summer.common.model.andi.AndiUserDO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/06 12:11
 */
@Repository
@SwitchDataSource
@RequiredArgsConstructor
public class AndiDAO {
    private final SqlSessionTemplate template;

    public void createUser(AndiUserDO user) {
        final AndiUserMapper mapper = template.getMapper(AndiUserMapper.class);
        mapper.insertUser(user);
    }

    public void createInterfaceLog(AndiInterfaceLog log) {
        final AndiInterfaceLogMapper mapper = template.getMapper(AndiInterfaceLogMapper.class);
        mapper.insertInterfaceLog(log);
    }
}
