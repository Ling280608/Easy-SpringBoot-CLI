package com.ling.cli.service.sys;

import com.ling.cli.models.entity.sys.SysParameterEntity;
import com.ling.cli.models.global.SysResult;

/**
 * @author ling
 * @description: 参数业务接口
 */
public interface SysParameterService {
    /**
     * @description: 新增系统参数
     * @param sysParameter 参数数据
     */
    SysResult insert(SysParameterEntity sysParameter);

    SysResult getValue(String key);
}
