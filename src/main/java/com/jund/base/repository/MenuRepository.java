package com.jund.base.repository;

import com.jund.base.entity.MenuEntity;
import com.jund.framework.jpa.base.repository.BaseRepository;

/**
 * Created by zhijund on 2017/9/2.
 */
public interface MenuRepository extends BaseRepository<MenuEntity, Long> {

    MenuEntity findMenuByMenuCode(String menuCode);

}
