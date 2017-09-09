package com.jund.basic.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long> {

	Menu findMenuByMenuCode(String menuCode);
	
	@Query("select m from Menu m where m.app.id = :appId order by m.sortNo asc ")
	List<Menu> findByAppId(@Param("appId") Long appId);
}
