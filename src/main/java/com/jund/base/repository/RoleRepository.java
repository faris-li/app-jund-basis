package com.jund.base.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    List<Role> findByAdmFlag(int admFlag);

    @Query("select r from Role r, UserRole ur, User u where ur.roleId = r.id and ur.userId = u.id and u.userName = :userName ")
    List<Role> findRoleByUser(@Param("userName") String userName);

    @Query("select r from Role r, RoleGrp rg where rg.subRoleId = r.id and rg.supRoleId = :roleId ")
    List<Role> findSubRoleByRole(@Param("roleId") Long roleId);

    Role findRoleByRoleCode(String roleCode);
}
