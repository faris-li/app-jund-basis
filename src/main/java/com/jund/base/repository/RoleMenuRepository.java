package com.jund.base.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends BaseRepository<RoleMenu, Long> {

    void deleteByRoleId(Long roleId);

    List<RoleMenu> findByRoleId(Long roleId);

}
