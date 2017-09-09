package com.jund.basic.core.service.impl;

import com.jund.basic.core.service.PwdRuleService;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.platformwork.security.model.PwdRule;
import com.jund.security.repository.PwdRuleRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PwdRuleServiceImpl extends BaseServiceImpl<PwdRule, String> implements PwdRuleService {

	@Autowired
	private PwdRuleRepository pwdRuleRepository;
	
	@Override
	protected BaseRepository<PwdRule, String> getRepository() {
		return pwdRuleRepository;
	}

	public PwdRule findPwdRule() {
		List<PwdRule> list=pwdRuleRepository.findAll();
		PwdRule pwdRule = new PwdRule();
		if(!CollectionUtils.isEmpty(list)){
			pwdRule = list.get(0);
		}
		return pwdRule;
	}
	
	public String findDefaultPwd() {
		PwdRule passwordRules = findPwdRule();
		return passwordRules.getDefaultPwd();
	}
	
}
