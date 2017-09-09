package com.jund.basis.core.repository;

import com.jund.basis.core.entity.RoleGrp;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleGrpRepository extends BaseRepository<RoleGrp, Long> {

    void deleteBySupRoleId(Long supRoleId);

    void deleteBySubRoleId(Long subRoleId);

}
