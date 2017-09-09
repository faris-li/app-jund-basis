package com.jund.basic.core.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.RoleGrp;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleGrpRepository extends BaseRepository<RoleGrp, Long> {

    void deleteBySupRoleId(Long supRoleId);

    void deleteBySubRoleId(Long subRoleId);

}
