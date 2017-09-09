package com.jund.basic.core.service.impl;

import com.jund.basic.core.service.RoleService;
import com.jund.basic.core.service.UserService;
import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.framework.jpa.util.HqlUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.param.remote.api.ParamRemoteService;
import com.jund.platformwork.security.model.*;
import com.jund.platformwork.security.model.dto.RoleDTO;
import com.jund.platformwork.security.model.dto.RoleForm;
import com.jund.security.Constants;
import com.jund.security.repository.*;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
   
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleAppRepository roleAppRepository;
    
    @Autowired
    private RoleGrpRepository roleGrpRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private ParamRemoteService paramRemoteService;

    @Override
    protected BaseRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    @Logging(title = "保存角色信息", data = "角色信息为{0}，授予应用{1}，授予菜单{2}，授予下级角色{3}，该角色的上级角色为{4}")
    @Transactional
    public void save(Role role, Long[] appIds, Long[] menuIds, Long[] subIds, Long supId) {
    	Role checkCode = new Role();
    	checkCode.setRoleCode(role.getRoleCode());
    	Example<Role> example = Example.of(checkCode);
    	if(exists(example))
    		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色号已经存在，无法使用！");
    	
        role = roleRepository.save(role);

        Long id = role.getId();
        //处理角色应用关系
        if (id != null) {
            roleAppRepository.deleteByRoleId(id);
        }
        
        addRoleApp(role.getId(), appIds);
        //处理角色菜单关系
        if (id != null) {
            roleMenuRepository.deleteByRoleId(id);
        }
        
        addRoleMenu(role.getId(), menuIds);
        //处理可管理角色
        if (id != null) {
            roleGrpRepository.deleteBySupRoleId(id);
        }
        
        if (!ArrayUtils.isEmpty(subIds)) {
            for (Long subId : subIds) {
                addRoleGrp(role.getId(), subId);
            }
        }

        if (id == null) {
            //新增时，将该角色授予超级管理员所管理
            List<Role> admRoles = roleRepository.findByAdmFlag(Constants.INTEGER_VALUE_TRUE);
            Set<Long> admIds = new HashSet<Long>();
            if (!CollectionUtils.isEmpty(admRoles)) {
                for (Role admRole : admRoles) {
                    addRoleGrp(admRole.getId(), role.getId());
                    admIds.add(admRole.getId());
                }
            }
            //新增时，处理选中的上级角色
            if (!admIds.contains(supId)) {
                addRoleGrp(supId, role.getId());
            }
        }
    }

    @Transactional
    private void addRoleMenu(Long roleId, Long[] menuIds) {
        if (roleId == null || ArrayUtils.isEmpty(menuIds))
            return;
        
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
           
            this.roleMenuRepository.save(roleMenu);
        }
    }

    @Transactional
    private void addRoleApp(Long roleId, Long[] appIds) {
        if (roleId == null || ArrayUtils.isEmpty(appIds))
            return;
       
        for (Long appId : appIds) {
            RoleApp roleApp = new RoleApp();
            roleApp.setRoleId(roleId);
            roleApp.setAppId(appId);
           
            this.roleAppRepository.save(roleApp);
        }
    }
    
    @Transactional
    private void addRoleGrp(Long roleId, Long subRoleId) {
        if (roleId == null || subRoleId == null)
            return;
       
        RoleGrp roleGrp = new RoleGrp();
        roleGrp.setSupRoleId(roleId);
        roleGrp.setSubRoleId(subRoleId);
       
        this.roleGrpRepository.save(roleGrp);
    }

    @Transactional
    @Override
    public void delete(Long... ids) {
        //删除角色用户关系/下级角色关系/上级角色关系/角色菜单关系/角色资源关系/角色互斥关系
        for (Long id : ids) {
        	List<User> users = userService.findUserByRole(id);
        	if(! CollectionUtils.isEmpty(users)) {
    			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色下已经存在用户，不能删除！");
    		}
        	
            userRoleRepository.deleteByRoleId(id);
            roleGrpRepository.deleteBySupRoleId(id);
            roleGrpRepository.deleteBySubRoleId(id);
            roleAppRepository.deleteByRoleId(id);
            roleMenuRepository.deleteByRoleId(id);
//			roleMutexRepository.deleteByRoleIdOrSubRoleId(id);
        }
        //删除角色本身
        super.delete(ids);

        //更新资源权限缓存
    }

    public List<Role> findRoleByUser(String userName) {
        return roleRepository.findRoleByUser(userName);
    }

    public List<Role> findUserMgrRole(String currUserName) {
        if (StringUtils.isEmpty(currUserName)) {
            return Collections.emptyList();
        }

        List<Object> parameters = new ArrayList<Object>();
        String hql = prepareHqlForSelect(currUserName, null, parameters);
        
        return roleRepository.findAllByHql(hql.toString(), parameters.toArray());
    }

    public boolean isAdminUser(String userName) {
        if (StringUtils.isEmpty(userName))
            return false;
        
        long count = userRepository.countByUserNameAndAdmFlag(userName, Constants.INTEGER_VALUE_TRUE);
        return count > 0;
    }

    public List<Role> findSubRoleByRole(Long roleId) {
        return roleRepository.findSubRoleByRole(roleId);
    }

    public Page<RoleDTO> findRoleAndSubByCurrUser(String currUserName, RoleForm roleForm, Pageable pageable) {
        List<Object> parameters = new ArrayList<Object>();
        String hql = prepareHqlForSelect(currUserName, roleForm, parameters);
        String orderhql = HqlUtil.appendOrderHql(hql,pageable.getSort());
        Page<Role> page = roleRepository.findAllByHql(pageable, orderhql, parameters.toArray());
        
        List<Role> roleList = page.getContent();
        if (CollectionUtils.isEmpty(roleList))
            return new PageImpl<RoleDTO>(new ArrayList<RoleDTO>(), pageable, 0L);

        // 封装数据
        List<RoleDTO> roleDTOlist = new ArrayList<RoleDTO>();
        for (Role role : roleList) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleCode(role.getRoleCode());
            roleDTO.setRemark(role.getRemark());
            roleDTO.setId(role.getId());
            roleDTO.setRoleName(role.getRoleName());

            //查询可管理角色
            List<Role> subRoles = findSubRoleByRole(role.getId());
            if (!CollectionUtils.isEmpty(subRoles)) {
                for (Role subRole : subRoles) {
                    RoleDTO subRoleDTO = new RoleDTO();
                    subRoleDTO.setId(subRole.getId());
                    subRoleDTO.setRoleCode(subRole.getRoleCode());
                    subRoleDTO.setRoleName(subRole.getRoleName());
                    roleDTO.getSubRoles().add(subRoleDTO);
                }
            }
            roleDTOlist.add(roleDTO);
        }
        Page<RoleDTO> result = new PageImpl<RoleDTO>(roleDTOlist, pageable, page.getTotalElements());
        return result;
    }

    private String prepareHqlForSelect(String userName, RoleForm roleForm, List<Object> parameters) {
        //处理是否显示
    	String adminDesplyFlg = "1";
		try {
			ResponseInfo result = paramRemoteService.findByParamCode(Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
	    	if(result != null && RestConst.ReturnCode.OK.equals(result.getCode())){
	    		adminDesplyFlg = (String) result.getData();
	    		
	    	}
		} catch (Throwable e) {
			logger.debug("参数服务调用异常，参数为:" + Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
		}
        StringBuilder hql = new StringBuilder();
        if (isAdminUser(userName)) {
            hql.append("select distinct t");
            hql.append(" from Role as t");
            hql.append(" where 1=1");
            if (!Constants.ADMIN_FLG_TRUE.equals(adminDesplyFlg)) {
                hql.append(" and t.admFlag <> 1 ");
            }
        } else {
            hql.append("select distinct t");
            hql.append(" from User as u");
            hql.append(", UserRole as ur");
            hql.append(", RoleGrp as rg");
            hql.append(", Role as t");
            hql.append(" where u.userName = ?");
            hql.append(" and ur.userId = u.id");
            hql.append(" and rg.supRoleId = ur.roleId");
            hql.append(" and rg.subRoleId = t.id");
            
            parameters.add(userName);
        }
        if(roleForm != null) {
        	if (!StringUtils.isEmpty(roleForm.getRoleName())) {
        		hql.append(" and upper(t.roleName) like ? ");
        		parameters.add("%" + roleForm.getRoleName().toUpperCase() + "%");
        	}
        	
        	if (!StringUtils.isEmpty(roleForm.getRoleCode())) {
        		hql.append(" and upper(t.roleCode) like ? ");
        		parameters.add("%" + roleForm.getRoleCode() + "%");
        	}
        	
        	if (roleForm.getId() != null) {
        		hql.append(" and t.id <> ?");
        		parameters.add(roleForm.getId());
        	}
        }
        logger.debug("角色分页hql：", hql.toString());
        return hql.toString();
    }
    
    public Role findByRoleCode(String roleCode){
    	return roleRepository.findRoleByRoleCode(roleCode);
    }
}
