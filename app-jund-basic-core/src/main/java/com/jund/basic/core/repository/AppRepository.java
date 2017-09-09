package com.jund.basic.core.repository;

import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.platformwork.security.model.App;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends BaseRepository<App, Long> {

    App findByAppCode(String appCode);
}

