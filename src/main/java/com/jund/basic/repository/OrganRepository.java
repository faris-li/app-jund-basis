package com.jund.basic.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.Organ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganRepository extends BaseRepository<Organ, Long> {

    @Query(" from Organ t where t.orgSeq like CONCAT((:pseq),'%') order by t.orgCode")
    List<Organ> findAllChildByPseq(@Param("pseq") String pseq);

    @Query(" from Organ t  where t.orgSeq like CONCAT((:pseq),'%') and t.status = (:status) order by t.orgCode")
    List<Organ> findAllChildByPseq(@Param("pseq") String pseq, @Param("status") Integer status);

    Organ findOrganByOrgCode(String orgCode);

    Organ findOrganByOrgName(String orgName);

    @Query(" from Organ t left join fetch t.parentOrgan where t.id =(:id)")
    Organ findWithParentById(@Param("id") Long id);

}