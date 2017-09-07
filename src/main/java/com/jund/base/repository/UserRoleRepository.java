package com.jund.base.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole,Long> {

	void deleteByUserId(Long userId);

	void deleteByRoleId(Long roleId);
}
