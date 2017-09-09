package com.jund.basis.core.service.impl;

import com.jund.basis.core.entity.*;
import com.jund.basis.core.entity.dto.MenuDTO;
import com.jund.basis.core.repository.MenuRepository;
import com.jund.basis.core.repository.RoleMenuRepository;
import com.jund.basis.core.service.AppService;
import com.jund.basis.core.service.MenuService;
import com.jund.framework.core.Const;
import com.jund.framework.core.exception.JundRuntimeException;
import com.jund.framework.jpa.JpaConst;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    @Autowired
    private AppService appService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Override
    protected BaseRepository<Menu, Long> getRepository() {
        return menuRepository;
    }

    public List<Map<String, Object>> findRootMenu() {
        Map<String, Object> treeObj = new HashMap<String, Object>();
        treeObj.put("menuCode", "root");
        treeObj.put("url", "root");
        treeObj.put("menuType", Const.DICT.INTEGER_FALSE);
        treeObj.put("expandFlag", Const.DICT.INTEGER_TRUE);
        treeObj.put("id", 0l);
        treeObj.put("pid", "");
        treeObj.put("menuName", "根节点");
        treeObj.put("menuIcon", "root");
        treeObj.put("isParent", "true");
        treeObj.put("open", "true");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(treeObj);
        return list;
    }

    public List<Map<String, Object>> findAllMenu() {
        Map<String, Object> treeObj = new HashMap<String, Object>();
        List<App> allApps = appService.findAll();
        treeObj.put("menuCode", "root");
        treeObj.put("url", "root");
        treeObj.put("menuType", Const.DICT.INTEGER_FALSE);
        treeObj.put("expandFlag", Const.DICT.INTEGER_TRUE);
        treeObj.put("id", 0);
        treeObj.put("pid", "");
        treeObj.put("menuName", "根节点");
        treeObj.put("menuIcon", "root");
        treeObj.put("isParent", "true");
        treeObj.put("open", "true");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(treeObj);

        for (App app : allApps) {
            Map<String, Object> appObj = new HashMap<String, Object>();
            appObj.put("menuCode", app.getAppCode());
            appObj.put("menuType", Const.DICT.INTEGER_FALSE);
            appObj.put("expandFlag", Const.DICT.INTEGER_TRUE);
            appObj.put("id", app.getId());
            appObj.put("appId", app.getId());
            if (StringUtils.isNotEmpty(app.getAppIcon())) {
                appObj.put("menuIcon", app.getAppIcon());
            }
            appObj.put("pid", 0);
            appObj.put("menuName", app.getAppName());
            appObj.put("isParent", true);
            appObj.put("open", true);

            list.add(appObj);
        }

        Order order = new Order(Direction.ASC, "sortNo");
        Sort sort = new Sort(order);
        List<Menu> allMenus = menuRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(allMenus)) {
            for (Menu menu : allMenus) {
                App app = new App();

                Map<String, Object> menuObj = new HashMap<String, Object>();
                menuObj.put("menuCode", menu.getMenuCode());
                menuObj.put("url", menu.getMenuUrl());
                menuObj.put("menuType", menu.getMenuType());
                menuObj.put("expandFlag", menu.getExpandFlag());
                menuObj.put("id", menu.getId());
                menuObj.put("appId", menu.getApp().getId());
                menuObj.put("menuName", menu.getMenuName());
                if (Const.DICT.INTEGER_FALSE == (menu.getMenuType())) {
                    menuObj.put("isParent", true);
                }
                if (Const.DICT.INTEGER_TRUE == menu.getExpandFlag()) {
                    menuObj.put("open", true);
                } else {
                    menuObj.put("open", false);
                }
                if (menu.getParentMenu() != null) {
                    menuObj.put("pid", menu.getParentMenu().getId());
                } else {
                    menuObj.put("pid", -menu.getApp().getId());
                    app = appService.findOne(menu.getApp().getId());
                }
                if (StringUtils.isNotEmpty(menu.getMenuIcon())) {
                    menuObj.put("menuIcon", menu.getMenuIcon());
                }
                if (app != null) {

                }
                list.add(menuObj);
            }
        }
        return list;
    }

    public List<Menu> findExistMenuByAppId(Long appId) {
        return menuRepository.findByAppId(appId);
    }

    public List<Map<String, Object>> findMenuByAppId(Long appId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        App app = appService.findOne(appId);
        Map<String, Object> appObj = new HashMap<String, Object>();
        appObj.put("menuCode", app.getAppCode());
        appObj.put("id", -app.getId());
        appObj.put("appId", app.getId());
        if (StringUtils.isNotEmpty(app.getAppIcon())) {
            appObj.put("menuIcon", app.getAppIcon());
        }
        appObj.put("menuName", app.getAppName());
        appObj.put("menuType", Const.DICT.INTEGER_FALSE);
        appObj.put("expandFlag", Const.DICT.INTEGER_TRUE);
        appObj.put("pid", 0);
        appObj.put("isParent", true);
        appObj.put("open", true);
        list.add(appObj);

        List<Menu> menus = menuRepository.findByAppId(appId);
        if (!CollectionUtils.isEmpty(menus)) {
            for (Menu menu : menus) {
                Map<String, Object> menuObj = new HashMap<String, Object>();
                menuObj.put("menuCode", menu.getMenuCode());
                menuObj.put("url", menu.getMenuUrl());
                menuObj.put("menuType", menu.getMenuType());
                menuObj.put("expandFlag", menu.getExpandFlag());
                menuObj.put("id", menu.getId());
                menuObj.put("appId", menu.getApp().getId());
                menuObj.put("menuName", menu.getMenuName());
                if (Const.DICT.INTEGER_FALSE == (menu.getMenuType())) {
                    menuObj.put("isParent", true);
                }
                if (Const.DICT.INTEGER_TRUE==(menu.getExpandFlag())) {
                    menuObj.put("open", true);
                } else {
                    menuObj.put("open", false);
                }
                if (menu.getParentMenu() != null) {
                    menuObj.put("pid", menu.getParentMenu().getId());
                } else {
                    menuObj.put("pid", -app.getId());
                }
                if (StringUtils.isNotEmpty(menu.getMenuIcon())) {
                    menuObj.put("menuIcon", menu.getMenuIcon());
                }
                list.add(menuObj);
            }
        }

        return list;
    }

    public List<Long> findMenuIdByRoleId(Long roleId) {
        List<Long> menuIds = new ArrayList<Long>();

        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        if (!CollectionUtils.isEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                menuIds.add(roleMenu.getMenuId());
            }
        }

        return menuIds;
    }

    public Menu findMenuByMenuCode(String menuCode) {
        return menuRepository.findMenuByMenuCode(menuCode);
    }

    @Transactional
    public void saveMenu(Menu menu) {
        /**
         * 对菜单url中特殊字符（单引号 双引号）处理
         */
        String url = menu.getMenuUrl();
        if (StringUtils.isNotEmpty(url)) {
            url = url.replaceAll("[\"\']", "");
        }
        menu.setMenuUrl(url);
        Long menuId = menu.getId();
        // 判断menuId是否为空，为空，则为新增菜单，不为空，则为修改菜单
        if (menuId == null) {
            if (null != findMenuByMenuCode(menu.getMenuCode())) {
                throw new JundRuntimeException(JpaConst.ErrorCode.CHECK_CODE_UNIQUE, "菜单编码已存在！");
            }

            menu.setId(null);
            menu.setStatus(Const.DICT.INTEGER_TRUE);

            Long pid = null;
            Menu parentMenu = null;
            if (menu.getParentMenu().getId() != null) {
                pid = menu.getParentMenu().getId();
                parentMenu = menuRepository.findOne(pid);
            }
            menu.setParentMenu(parentMenu);
            // 设置显示顺序
            Integer NodeMaxOrder = findMaxOrder(pid);
            if (NodeMaxOrder != null) {
                NodeMaxOrder += 1;
            } else {
                // 添加父节点没有子菜单的时候 初始为1
                NodeMaxOrder = 1;
            }
            menu.setSortNo(NodeMaxOrder);
        } else {
            Menu menuOld = this.menuRepository.findOne(menu.getId());
            // 添加页面上没有的属性
            menu.setStatus(menuOld.getStatus());
            menu.setParentMenu(menuOld.getParentMenu());
            menu.setSortNo(menuOld.getSortNo());
            // 添加页面上disable的属性
            menu.setMenuCode(menuOld.getMenuCode());
            menu.setMenuType(menuOld.getMenuType());
        }

        menuRepository.save(menu);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Integer findMaxOrder(Long pid) {
        List paramsList = new ArrayList();
        StringBuilder hql = new StringBuilder();
        hql.append(" select m from Menu m where m.parentMenu.id ");
        if (pid == null) {
            hql.append(" is null ");
        } else {
            hql.append(" = ? ");
            paramsList.add(pid);
        }
        hql.append(" order by m.sortNo desc ");

        List<Menu> list = menuRepository.findAllByHql(hql.toString(), paramsList.toArray());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0).getSortNo();
        }
    }

    public List<MenuDTO> findMenuByUserName(String username) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new " + MenuDTO.class.getName());
        hql.append("(T5.id as id ,T5.menuName as menuName,T5.menuUrl as menuUrl,T5.parentMenu.id as parentId,");
        hql.append(" T5.expandFlag as expandFlag,T5.menuIcon as menuIcon,T5.sortNo as sortNo)");
        hql.append(" FROM  " + User.class.getName() + " T1,");
        hql.append(UserRole.class.getName() + " T2, ");
        hql.append(Role.class.getName() + " T3, ");
        hql.append(RoleMenu.class.getName() + " T4, ");
        hql.append(Menu.class.getName() + " T5 WHERE ");
        hql.append(" T1.userName= ? ");
        hql.append("  AND T5.status= ? AND T5.parentMenu.id IS NOT NULL ");
        hql.append(" AND T1.id=T2.userId ");
        hql.append(" AND T2.roleId=T3.id ");
        hql.append(" AND T3.id=T4.roleId ");
        hql.append(" AND T4.menuId=T5.id ");
        hql.append(" ORDER BY T5.sortNo ASC ");

        List<Object> parameters = new ArrayList<Object>();
        parameters.add(username);
        parameters.add(Const.DICT.INTEGER_TRUE);

        return menuRepository.findAllByHql(hql.toString(), MenuDTO.class, parameters.toArray());
    }
}
