package com.jund.base.service;

import com.jund.framework.jpa.base.service.BaseService;
import com.jund.platformwork.security.model.Menu;
import com.jund.platformwork.security.model.dto.MenuDTO;
import com.jund.platformwork.security.model.dto.MenuForm;

import java.util.List;
import java.util.Map;

public interface MenuService extends BaseService<Menu, Long> {
	
	/**
	 * 查询角色拥有的菜单id列表
	 * @param roleId
	 * @return
	 */
	List<Long> findMenuIdByRoleId(Long roleId);
	
	/**
	 * 根据菜单号查询菜单
	 * @param menuCode
	 * @return
	 */
	Menu findMenuByMenuCode(String menuCode);
	
	/**
	 * 菜单保存
	 * @param menu
	 */
	void saveMenu(Menu menu);
	
	/**
	 * 查询用户拥有的菜单
	 * @param userName
	 * @return
	 */
	List<MenuDTO> findMenuByUserName(String userName);
	
	/**
	 * 根据排序号查所有菜单,处理是否打开/是否有子级等属性。该方法用于菜单管理的菜单树展示
	 * @return
	 */
	List<Map<String,Object>> findAllMenu();
	
	/**
	 * 菜单拖动保存
	 * @param menuForm
	 */
	 void saveMoveMenu(MenuForm menuForm);
	 
	 /**
	  * 查询指定应用的菜单
	  * @param appId
	  * @return
	  */
	 List<Map<String, Object>> findMenuByAppId(Long appId);
	 
	 /**
	  * 加载菜单树根节点
	  * @return
	  */
	 List<Map<String, Object>> findRootMenu();
	 
	 /**
	  * 判断指定应用下是否存在菜单
	  * @param appId
	  * @return
	  */
	 List<Menu> findExistMenuByAppId(Long appId);
}
