package com.jund.base.service.impl;

import com.jund.base.entity.MenuEntity;
import com.jund.base.entity.dto.MenuDTO;
import com.jund.base.repository.MenuRepository;
import com.jund.base.service.MenuService;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.JpaBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhijund on 2017/9/2.
 */
@Service
@Transactional(readOnly = true)
public class MenuServiceImpl extends JpaBaseServiceImpl<MenuEntity, Long> implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Long> findMenuIdsByRoleId(Long roleId) {
        return null;
    }

    @Override
    public MenuEntity findMenuByMenuCode(String menuCode) {
        return null;
    }

    @Override
    @Transactional
    public void saveMenu(MenuEntity menuEntity) {

    }

    @Override
    public List<MenuDTO> findMenusByUserName(String userName) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findAllMenu() {
        return null;
    }

    @Override
    protected BaseRepository<MenuEntity, Long> getRepository() {
        return menuRepository;
    }
}
