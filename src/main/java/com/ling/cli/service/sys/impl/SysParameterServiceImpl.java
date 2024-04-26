package com.ling.cli.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ling.cli.mapper.sys.SysParameterMapper;
import com.ling.cli.models.entity.sys.SysParameterEntity;
import com.ling.cli.models.global.SysResult;
import com.ling.cli.service.sys.SysParameterService;
import org.springframework.stereotype.Service;

/**
 * @author ling
 * @description: 参数业务实现
 */
@Service
public class SysParameterServiceImpl extends ServiceImpl<SysParameterMapper, SysParameterEntity> implements SysParameterService {
    @Override
    public SysResult insert(SysParameterEntity sysParameter) {
        this.save(sysParameter);
        return SysResult.success();
    }

    @Override
    public SysResult getValue(String key) {
        SysParameterEntity one = this.getOne(new LambdaQueryWrapper<SysParameterEntity>().eq(SysParameterEntity::getParameterKey, key));
        return SysResult.success(one);
    }
}
