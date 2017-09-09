package com.jund.basis.core.repository;

import com.jund.basis.core.entity.RoleApp;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  RoleAppRepository extends BaseRepository<RoleApp,Long> {

	List<RoleApp> findByRoleId(Long roleId);
	
	void deleteByRoleId(Long roleId);
	
	void deleteByAppId(Long appId);
}
