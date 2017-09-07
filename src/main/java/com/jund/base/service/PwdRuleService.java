package com.jund.base.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.PwdRule;

public interface PwdRuleService extends BaseService<PwdRule, String> {

    /**
     * 查询密码规则
     *
     * @return
     */
    PwdRule findPwdRule();

    /**
     * 查询默认密码
     *
     * @return
     */
    String findDefaultPwd();

}
