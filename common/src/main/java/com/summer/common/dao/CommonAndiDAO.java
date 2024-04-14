package com.summer.common.dao;

import com.summer.common.config.datasource.DatasourceTag;
import com.summer.common.mapper.AndiInterfaceLogMapper;
import com.summer.common.model.andi.AndiInterfaceLog;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 17:02
 */
@Repository
@DatasourceTag
public class CommonAndiDAO {
    @Resource
    private SqlSessionTemplate template;

    public void createInterfaceLog(AndiInterfaceLog log) {
        final AndiInterfaceLogMapper mapper = template.getMapper(AndiInterfaceLogMapper.class);
        mapper.createInterfaceLog(log);
    }
}
