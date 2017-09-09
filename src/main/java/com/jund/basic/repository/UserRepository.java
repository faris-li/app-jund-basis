package com.jund.basic.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
	
	@Query("select count(r.id) from User as u, UserRole as ur, Role as r where u.userName = :username and r.admFlag = :admFlag and u.id = ur.userId and r.id = ur.roleId")
	long countByUserNameAndAdmFlag(@Param("username") String username, @Param("admFlag") int admFlag);

	@Query(" from User where org.id = (:orgId) and status = 1")
    List<User> findUserEnableByOrgId(@Param("orgId") Long orgId);
    
	User findUserByUserName(String userName);
	
	@Query("select u from  User  u , UserRole  ur  where u.id = ur.userId and ur.roleId = (:roleId)")
	List<User> findUserByRole(@Param("roleId") Long roleId);

}

