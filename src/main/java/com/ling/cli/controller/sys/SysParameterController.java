package com.ling.cli.controller.sys;

import com.ling.cli.models.entity.sys.SysParameterEntity;
import com.ling.cli.models.global.SysResult;
import com.ling.cli.service.sys.SysParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ling
 * @description: 系统参数控制类
 */
@RestController
@RequestMapping("/sys/parameter")
public class SysParameterController {
    @Autowired
    private SysParameterService sysParameterService;

    /**
     * 新增系统参数
     */
    @PostMapping("/insert")
    public SysResult insert(@RequestBody SysParameterEntity sysParameter) {
       return sysParameterService.insert(sysParameter);
    }

    /**
     * 新增系统参数
     */
    @GetMapping("/getValue")
    public SysResult insert(@RequestParam String parameterKey) {
        return sysParameterService.getValue(parameterKey);
    }
}
