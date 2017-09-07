package com.jund.base.service.impl;

import com.jund.base.service.AppService;
import com.jund.base.service.MenuService;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.*;
import com.jund.platformwork.security.model.dto.MenuDTO;
import com.jund.platformwork.security.model.dto.MenuForm;
import com.jund.security.Constants;
import com.jund.security.repository.MenuRepository;
import com.jund.security.repository.RoleMenuRepository;
import org.apache.commons.lang.StringUtils;
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
		treeObj.put("menuType", Constants.INTEGER_VALUE_FALSE);
		treeObj.put("expandFlag", Constants.INTEGER_VALUE_TRUE);
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
		treeObj.put("menuType", Constants.INTEGER_VALUE_FALSE);
		treeObj.put("expandFlag", Constants.INTEGER_VALUE_TRUE);
		treeObj.put("id", 0);
		treeObj.put("pid", "");
		treeObj.put("menuName", "根节点");
		treeObj.put("menuIcon", "root");
		treeObj.put("isParent", "true");
		treeObj.put("open", "true");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(treeObj);

		for(App app : allApps){
			Map<String, Object> appObj = new HashMap<String, Object>();
			appObj.put("menuCode", app.getAppCode());
			appObj.put("menuType", Constants.INTEGER_VALUE_FALSE);
			appObj.put("expandFlag", Constants.INTEGER_VALUE_TRUE);
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
		if(!CollectionUtils.isEmpty(allMenus)){
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
				if (Constants.INTEGER_VALUE_FALSE.equals(menu.getMenuType())) {
					menuObj.put("isParent", true);
				}
				if (Constants.INTEGER_VALUE_TRUE.equals(menu.getExpandFlag())) {
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

	public List<Menu> findExistMenuByAppId(Long appId){
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
		appObj.put("menuType", Constants.INTEGER_VALUE_FALSE);
		appObj.put("expandFlag", Constants.INTEGER_VALUE_TRUE);
		appObj.put("pid", 0);
		appObj.put("isParent", true);
		appObj.put("open", true);
		list.add(appObj);

		List<Menu> menus = menuRepository.findByAppId(appId);
		if(!CollectionUtils.isEmpty(menus)){
			for (Menu menu : menus) {
				Map<String, Object> menuObj = new HashMap<String, Object>();
				menuObj.put("menuCode", menu.getMenuCode());
				menuObj.put("url", menu.getMenuUrl());
				menuObj.put("menuType", menu.getMenuType());
				menuObj.put("expandFlag", menu.getExpandFlag());
				menuObj.put("id", menu.getId());
				menuObj.put("appId", menu.getApp().getId());
				menuObj.put("menuName", menu.getMenuName());
				if (Constants.INTEGER_VALUE_FALSE.equals(menu.getMenuType())) {
					menuObj.put("isParent", true);
				}
				if (Constants.INTEGER_VALUE_TRUE.equals(menu.getExpandFlag())) {
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
		// 对菜单url中特殊字符（单引号 双引号）处理
		String url = menu.getMenuUrl();
		if (StringUtils.isNotEmpty(url)) {
			url = url.replaceAll("[\"\']", "");
		}
		menu.setMenuUrl(url);
		Long menuId = menu.getId();
		// 判断menuId是否为空，为空，则为新增菜单，不为空，则为修改菜单
		if (menuId == null) {
			if(null != findMenuByMenuCode(menu.getMenuCode())){
				throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "菜单号已存在，无法使用！！");
			}
			
			menu.setId(null);
			menu.setStatus(Constants.INTEGER_VALUE_TRUE);
			
			Long pid = null;
			Menu parentMenu = null;
			if(menu.getParentMenu().getId() != null){
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Integer findMaxOrder(Long pid){
		List paramsList = new ArrayList();
		StringBuilder hql  = new StringBuilder();
		hql.append(" select m from Menu m where m.parentMenu.id ");
		if(pid == null){
			hql.append(" is null ");
		}else{
			hql.append(" = ? ");
			paramsList.add(pid);
		}
		hql.append(" order by m.sortNo desc ");
		
		List<Menu> list = menuRepository.findAllByHql(hql.toString(), paramsList.toArray());
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else{
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
		logger.debug("查询用户拥有的菜单:", hql.toString());
		
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(username);
		parameters.add(Constants.INTEGER_VALUE_TRUE);
		
		return menuRepository.findAllByHql(hql.toString(), MenuDTO.class, parameters.toArray());
	}

	@Transactional
	public void saveMoveMenu(MenuForm menuForm) {
		Long currId = menuForm.getCurrId();
		if (currId == null) {
			logger.error("当前节点为空！");
			return;
		}
		
		Long toId = menuForm.getToId();
		if (toId == null) {
			logger.error("目标节点为空！");
			return;
		}
		
		Integer type = menuForm.getType();
		if (type == null) {
			logger.error("操作类型为空！");
			return;
		}
		
		Menu currMenu = menuRepository.findOne(currId);
		Menu toMenu = menuRepository.findOne(toId);
		//如果是应用下的第一层菜单，没有parentMenu
//		if (currMenu.getParentMenu() == null) {
//			return;
//		}
		if (Constants.type_inner.equals(type)) {
			Integer toMax = 0;
			if (toMenu.getParentMenu() == null) {
				toMax = findMaxOrder(null);
			} else {
				toMax = findMaxOrder(toMenu.getId());
			}
			// 修改拖拽节点的parentId为目标节点ID ，同时修改menuOrder（放在最后）
			currMenu.setParentMenu(toMenu);
			//如果是A应用的菜单拖到B应用，菜单中应用改为B应用
			currMenu.setApp(toMenu.getApp());
			if (toMax != null && toMax > 0) {
				currMenu.setSortNo(toMax + 1);
			} else {
				currMenu.setSortNo(1);
			}
		} else {
			Integer currMenuOrder = currMenu.getSortNo();
			Integer toMenuOrder = toMenu.getSortNo();
			if ((currMenu.getParentMenu() != null && toMenu.getParentMenu() != null && currMenu.getParentMenu().equals(toMenu.getParentMenu())) 
					|| (currMenu.getParentMenu() == null && toMenu.getParentMenu() == null)) {
				// 是在同一个父节点下，同时修改两菜单及其中间的菜单的menuOrder即可
				if (currMenuOrder > toMenuOrder) {
					// 当前要移动的节点的序号小于要移动到的目标序号,则除当前节点外，相关菜单下移
					if(currMenu.getParentMenu() == null){
						downNode(null, toMenuOrder, currMenuOrder, type);
					}else{
						downNode(currMenu.getParentMenu().getId(), toMenuOrder, currMenuOrder, type);
					}
				} else if (currMenuOrder < toMenuOrder) {
					// 当要移动的节点的序号小于要移动到的目标序号，则除当前节点外，相关菜单上移
					if(currMenu.getParentMenu() == null){
						upNode(null, currMenuOrder, toMenuOrder, type);
					}else{
						upNode(currMenu.getParentMenu().getId(), currMenuOrder, toMenuOrder, type);
					}
				}
				currMenu.setSortNo(toMenuOrder);
				currMenu.setApp(toMenu.getApp());
			} else {
				/**
				 * 在不同的节点下，则当前拖拽节点后的菜单上移，目标节点及其下的节点菜单下移。并且把当前节点的父级节点设置为目标节点的父级
				 */
				if(currMenu.getParentMenu() == null && toMenu.getParentMenu() != null){
					upNode(null, currMenuOrder, null, type);
					downNode(toMenu.getParentMenu().getId(), toMenuOrder, null, type);
				}else{
					upNode(currMenu.getParentMenu().getId(), currMenuOrder, null, type);
					downNode(null, toMenuOrder, null, type);
				}
				currMenu.setParentMenu(toMenu.getParentMenu());
				currMenu.setSortNo(toMenuOrder);
			}
		}
		this.menuRepository.save(currMenu);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void upNode(Long pid, Integer startOrder, Integer endOrder, Integer type) {
		List paramsList = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("update plt_sec_menu t set t.sort_No = t.sort_No-1 where t.pid ");
		
		if(pid == null){
			hql.append(" is null ");
		}else{
			hql.append(" = ? ");
			paramsList.add(pid);
		}
		
		if (startOrder != null && startOrder > 0) {
			hql.append(" and t.sort_No > ? ");
			paramsList.add(startOrder);
		}

		if (endOrder != null && endOrder > 0) {
			if (Constants.type_prev.equals(type)) {
				hql.append(" and t.sort_No < ? ");
				paramsList.add(endOrder);
			}
			if (Constants.type_next.equals(type)) {
				hql.append(" and t.sort_No <= ? ");
				paramsList.add(endOrder);
			}
		}
		logger.debug("upNode:", hql.toString());
		menuRepository.excuteSql(hql.toString(), paramsList.toArray());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void downNode(Long pid, Integer startOrder, Integer endOrder, Integer type) {
		List paramsList = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("update plt_sec_menu t set t.sort_No = t.sort_No+1 where t.pid ");
		
		if(pid == null){
			hql.append(" is null ");
		}else{
			hql.append(" = ? ");
			paramsList.add(pid);
		}
		
		if (endOrder != null && endOrder > 0) {
			hql.append(" and t.sort_No < ? ");
			paramsList.add(endOrder);
		}
		
		if (startOrder != null && startOrder > 0) {
			if (Constants.type_prev.equals(type)) {
				hql.append(" and t.sort_No >= ? ");
				paramsList.add(startOrder);
			}
			
			if (Constants.type_next.equals(type)) {
				hql.append(" and t.sort_No > ? ");
				paramsList.add(startOrder);
			}
		}
		logger.debug("downNode:", hql.toString());
		menuRepository.excuteSql(hql.toString(), paramsList.toArray());
	}

}
