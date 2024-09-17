package com.summer.common.dao;

import com.summer.common.config.datasource.SwitchDataSource;
import com.summer.common.mapper.AndiInterfaceLogMapper;
import com.summer.common.model.andi.AndiInterfaceLog;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;


/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 17:02
 */
@Repository
@SwitchDataSource
@RequiredArgsConstructor
public class CommonAndiDAO {

    private final SqlSessionTemplate template;

    public void insertInterfaceLog(AndiInterfaceLog log) {
        final AndiInterfaceLogMapper mapper = template.getMapper(AndiInterfaceLogMapper.class);
        mapper.insertInterfaceLog(log);
    }
}
