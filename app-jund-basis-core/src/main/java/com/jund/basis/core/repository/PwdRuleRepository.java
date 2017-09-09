package com.jund.basis.core.repository;

import com.jund.basis.core.entity.PwdRule;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PwdRuleRepository extends BaseRepository<PwdRule, String> {
	
}

