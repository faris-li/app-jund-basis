package com.jund.basis.core.repository;

import com.jund.basis.core.entity.UserRole;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole,Long> {

	void deleteByUserId(Long userId);

	void deleteByRoleId(Long roleId);
}
