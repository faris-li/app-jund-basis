package com.jund.basic.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.RoleApp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  RoleAppRepository extends BaseRepository<RoleApp,Long> {

	List<RoleApp> findByRoleId(Long roleId);
	
	void deleteByRoleId(Long roleId);
	
	void deleteByAppId(Long appId);
}
