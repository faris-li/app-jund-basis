package com.jund.base.service;

import com.jund.base.entity.MenuEntity;
import com.jund.base.entity.dto.MenuDTO;
import com.jund.framework.jpa.base.service.JpaBaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by zhijund on 2017/9/2.
 */
public interface MenuService extends JpaBaseService<MenuEntity,Long>{

    List<Long> findMenuIdsByRoleId(Long roleId);

    MenuEntity findMenuByMenuCode(String menuCode);

    void saveMenu(MenuEntity menuEntity);

    List<MenuDTO> findMenusByUserName(String userName);

    List<Map<String,Object>> findAllMenu();

    boolean checkMenuCode(String menuCode);
}
