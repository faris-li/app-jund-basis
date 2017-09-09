package com.jund.basis.core.repository;

import com.jund.basis.core.entity.RoleMenu;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends BaseRepository<RoleMenu, Long> {

    void deleteByRoleId(Long roleId);

    List<RoleMenu> findByRoleId(Long roleId);

}
