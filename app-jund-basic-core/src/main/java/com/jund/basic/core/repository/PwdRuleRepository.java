package com.jund.basic.core.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.PwdRule;
import org.springframework.stereotype.Repository;

@Repository
public interface PwdRuleRepository extends BaseRepository<PwdRule, String> {
	
}

