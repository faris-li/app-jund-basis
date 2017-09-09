package com.jund.basis.core.repository;

import com.jund.basis.core.entity.App;
import com.jund.framework.jpa.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends BaseRepository<App, Long> {

    App findByAppCode(String appCode);
}

