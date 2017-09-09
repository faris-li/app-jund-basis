package com.jund.basic.service.impl;

import com.jund.basic.service.OrganService;
import com.jund.basic.service.PwdRuleService;
import com.jund.basic.service.RoleService;
import com.jund.basic.service.UserService;
import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.core.util.ExcelUtil;
import com.jund.framework.jpa.base.repository.BaseRepository;
import com.jund.framework.jpa.base.service.impl.BaseServiceImpl;
import com.jund.framework.jpa.util.HqlUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.param.remote.api.ParamRemoteService;
import com.jund.platformwork.security.model.*;
import com.jund.platformwork.security.model.dto.RoleDTO;
import com.jund.platformwork.security.model.dto.UserDTO;
import com.jund.platformwork.security.model.dto.UserForm;
import com.jund.security.Constants;
import com.jund.security.repository.OrganRepository;
import com.jund.security.repository.UserRepository;
import com.jund.security.repository.UserRoleRepository;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

	private static final String selectValue = "√";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrganService organService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PwdRuleService pwdRuleService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private OrganRepository organRepository;

    @Autowired
    private ParamRemoteService paramRemoteService;

    @Override
    protected BaseRepository<User, Long> getRepository() {
        return userRepository;
    }

    public Page<UserDTO> findAllUser(String currUserName, UserForm userform, Pageable pageable) {
    	//处理是否显示admin用户
    	String adminDesplyFlg = "1";
        try {
        	ResponseInfo result = paramRemoteService.findByParamCode(Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
            if (result != null && RestConst.ReturnCode.OK.equals(result.getCode())) {
                adminDesplyFlg = (String) result.getData();
            }
        } catch (Throwable e) {
            logger.debug("参数服务调用异常，参数为:" + Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
        }

        List<Object> parameters = new ArrayList<Object>();
        String hql = prepareHqlForSelect(currUserName, userform, parameters, adminDesplyFlg);
        String orderHql = HqlUtil.appendOrderHql(hql, pageable.getSort());
        Page<User> page = userRepository.findAllByHql(pageable, orderHql, parameters.toArray());
        List<User> userlist = page.getContent();
        if (CollectionUtils.isEmpty(userlist))
            return new PageImpl<UserDTO>(new ArrayList<UserDTO>(), pageable, 0L);

        // roleDTO
        Map<Long, List<RoleDTO>> roleDtoMap = new HashMap<Long, List<RoleDTO>>();
        List<Role> list = new ArrayList<Role>();
        for (User user : page.getContent()) {
            List<RoleDTO> roleDtoList = new ArrayList<RoleDTO>();
            list = roleService.findRoleByUser(user.getUserName());
            
            if (!CollectionUtils.isEmpty(list)) {
                for (Role r : list) {
                    RoleDTO roledto = new RoleDTO();
                    roledto.setRoleName(r.getRoleName());
                    roledto.setRoleCode(r.getRoleCode());
                    roledto.setId(r.getId());
                    roledto.setUserId(user.getId());
                    roledto.setRemark(r.getRemark());
                    roleDtoList.add(roledto);
                }
            }
            roleDtoMap.put(user.getId(), roleDtoList);
        }
        // 封装数据
        List<UserDTO> userDTOlist = new ArrayList<UserDTO>();
        for (User user2 : page.getContent()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setOrgName(user2.getOrg().getOrgName());
            userDTO.setRealName(user2.getRealName());
            userDTO.setUserFlag(user2.getUserLogin().getUserFlag());
            userDTO.setId(user2.getId());
            userDTO.setUserName(user2.getUserName());
            userDTO.setStatus(user2.getStatus());
            userDTO.setPrivFlag(user2.getPrivFlag());
            userDTO.setMobilePhone(user2.getMobilePhone());
            if (!CollectionUtils.isEmpty(roleDtoMap)) {
                if (userDTO.getRoles() == null) {
                    userDTO.setRoles(new ArrayList<RoleDTO>());
                }
                userDTO.setRoles(roleDtoMap.get(user2.getId()));
            }
            userDTOlist.add(userDTO);
        }
        return new PageImpl<UserDTO>(userDTOlist, pageable, page.getTotalElements());
    }

    public Workbook exp(String currUserName){
    	//处理是否显示admin用户
    	String adminDesplyFlg = "1";
        try {
        	ResponseInfo result = paramRemoteService.findByParamCode(Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
            if (result != null && RestConst.ReturnCode.OK.equals(result.getCode())) {
                adminDesplyFlg = (String) result.getData();
            }
        } catch (Throwable e) {
            logger.debug("参数服务调用异常，参数为:" + Constants.PARAM_ROLE_ADMIN_DISPLAY_FLAG);
        }
        
        List<Object> parameters = new ArrayList<Object>();
    	String hql = prepareHqlForSelect(currUserName, new UserForm(), parameters, adminDesplyFlg);
    	List<User> users = userRepository.findAllByHql(hql, parameters.toArray());
    	List<Role> roles = roleService.findUserMgrRole(currUserName);
    	List<ExcelExportEntity> columns = new ArrayList<ExcelExportEntity>();
    	columns.add(new ExcelExportEntity("用户账号", "userName"));
    	columns.add(new ExcelExportEntity("用户姓名", "realName"));
    	columns.add(new ExcelExportEntity("所属机构代码", "orgId"));
    	columns.add(new ExcelExportEntity("所属机构名称", "orgName"));
    	columns.add(new ExcelExportEntity("身份证号", "idcard"));
    	columns.add(new ExcelExportEntity("邮箱", "email"));
    	columns.add(new ExcelExportEntity("办公电话", "officePhone"));
    	columns.add(new ExcelExportEntity("移动电话", "mobilePhone"));
    	columns.add(new ExcelExportEntity("员工编号", "empNo"));
    	columns.add(new ExcelExportEntity("备注", "remark"));
    	for (Role role : roles) {
    		columns.add(new ExcelExportEntity("角色[" + role.getRoleName() + "]", "role" + role.getRoleCode()));
		}
    	
    	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    	for (User user : users) {
    		Map<String, Object> up = new HashMap<String, Object>();
    		up.put("userName", user.getUserName());
    		up.put("realName", user.getRealName());
    		up.put("orgId", user.getOrg().getOrgCode());
    		up.put("orgName", user.getOrg().getOrgName());
    		up.put("idcard", user.getIdcard());
    		up.put("email", user.getEmail());
    		up.put("officePhone", user.getOfficePhone());
    		up.put("mobilePhone", user.getMobilePhone());
    		up.put("empNo", user.getEmpNo());
    		up.put("remark", user.getRemark());
    		List<Role> ownRoles = roleService.findRoleByUser(user.getUserName());
    		if (!CollectionUtils.isEmpty(ownRoles)) {
    			for (Role role : ownRoles) {
    				up.put("role" + role.getRoleCode(), selectValue);
    			}
    		}
    		data.add(up);
        }
    	
    	return ExcelExportUtil.exportExcel(new ExportParams(), columns, data);
    }
    private String prepareHqlForSelect(String userName, UserForm user, List<Object> parameters,
                                       String roleAdminDisplayFlg) {
        String username = user.getUserName();
        Integer userFlag = user.getUserFlag();
        Organ organ = user.getOrgan();
        Long orgId = null;
        if (organ != null) {
            orgId = organ.getId();
        }
        
        Long roleId = null;
        Long[] roleIds = user.getRoleIds();
        if (ArrayUtils.isNotEmpty(roleIds)) {
            roleId = roleIds[0];
        }
        
        String realName = user.getRealName();
        String orgSeq = userRepository.findUserByUserName(userName).getOrg().getOrgSeq();
        
        StringBuilder hql = new StringBuilder();
        hql.append("select distinct t");
        hql.append(" from " + User.class.getName() + " as t");
        if(userFlag != null){
        	hql.append("  join t.userLogin as ul ");
        }
        
        hql.append(" left join t.org as o ");
        if (roleId != null) {
            hql.append(" join t.roles as r ");
        } else if (Constants.STRING_VALUE_FALSE.equals(roleAdminDisplayFlg)) {
            hql.append(" left join t.roles as r ");
        }
        
        hql.append(" where o.orgSeq like ? ");
        parameters.add("%" + orgSeq + "%");
        
        if (StringUtils.isNotBlank(realName)) {
            hql.append(" and t.realName like ?");
            parameters.add("%" + realName + "%");
        }
        
        if (StringUtils.isNotBlank(username)) {
            hql.append(" and upper(t.userName) like ?");
            parameters.add("%" + username.toUpperCase() + "%");
        }
        
        if (userFlag != null) {
            hql.append(" and ul.userFlag = ?");
            parameters.add(userFlag);
        }
        
        if (orgId != null) {
            hql.append(" and o.orgId = ? ");
            parameters.add(orgId);
        }
        
        if (roleId != null) {
            hql.append(" and r.roleId = ? ");
            parameters.add(roleId);
        }

        if (Constants.STRING_VALUE_FALSE.equals(roleAdminDisplayFlg)) {
            hql.append(" and (r.admFlag <> 1 or r.admFlag is null) ");
        }

        hql.append(" and t.status = 1 ");
        logger.debug("用户分页hql：", hql.toString());
        return hql.toString();
    }

    public boolean isAdminUser(String userName) {
        if (StringUtils.isEmpty(userName)) {
            logger.error("用户名为空");
            return false;
        }
        long count = userRepository.countByUserNameAndAdmFlag(userName, 1);
        return count > 0;
    }

    @Logging(title = "保存用户信息", data = "用户信息{0},所属机构{1},授予角色{2}")
    @Transactional
    public void save(User user, Long orgId, Long[] roleIds) {
        if (ArrayUtils.isNotEmpty(roleIds)) {
            user.setPrivFlag(Constants.INTEGER_VALUE_TRUE);
        } else {
            user.setPrivFlag(Constants.INTEGER_VALUE_FALSE);
        }
        
        if (orgId == null) {
            throw new RuntimeException("机构不能为空!");
        } else {
            Organ organ = this.organService.findOne(orgId);
            user.setOrg(organ);
        }
        
        if (user.getId() == null) {
        	User checkName = findUserByUserName(user.getUserName());
    		if (checkName != null) {
    			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "用户名已存在，无法使用！");
    		}
    		
            // 新增时初始密码
            UserLogin userLogin = new UserLogin();
            userLogin.setUser(user);
            userLogin.setPassword(passwordEncoder.encode(pwdRuleService.findDefaultPwd()));
            userLogin.setUserFlag(Constants.INTEGER_VALUE_TRUE);
            userLogin.setIsFirstLogin(Constants.INTEGER_VALUE_TRUE);

            user.setUserLogin(userLogin);
            user.setStatus(Constants.INTEGER_VALUE_TRUE);
            user = userRepository.save(user);

            this.saveUserRoleRelation(user.getId(), roleIds);
        } else {
            userRepository.save(user);
            this.saveUserRoleRelation(user.getId(), roleIds);
        }
    }

    public boolean isCancelUsing(Long[] userIds) {
        if (ArrayUtils.isEmpty(userIds))
            return true;

        for (Long userId : userIds) {
            List<Role> roles = this.roleService.findRoleByUser(userRepository.findOne(userId).getUserName());
            for (Role role : roles) {
                if (Constants.INTEGER_VALUE_FALSE.equals(role.getAdmFlag())) {
                    return false;
                }
            }
        }

        return true;
    }

    @Transactional
    public void cancelUsing(Long[] userIds) {
    	if(!isCancelUsing(userIds)){
    		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该用户拥有管理员角色，不可注销！");
    	}
    	
        for (int i = 0; i < userIds.length; i++) {
            User user = this.userRepository.findOne(userIds[i]);
            user.setStatus(Constants.INTEGER_VALUE_FALSE);
            userRepository.save(user);
            // 删除userRole级联
            this.userRoleRepository.deleteByUserId(user.getId());
        }
    }

    public User findUserByUserName(String username) {
        return this.userRepository.findUserByUserName(username);
    }

    public List<User> findUserByRole(Long roleId) {
        return userRepository.findUserByRole(roleId);
    }

    @Transactional
    public void saveUserRoleRelation(Long userId, Long[] roleIds) {
        if (userId == null)
            throw new VpRuntimeException(RestConst.ReturnCode.ERROR, "用户为空！");
        // 校验用户选择的角色列表中是否存在角色互斥

        // 1.删除以前的用户角色关系 2.保存新的用户角色关系
        this.userRoleRepository.deleteByUserId(userId);

        if (ArrayUtils.isEmpty(roleIds))
            return;

        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            this.userRoleRepository.save(userRole);
        }
    }

    @Transactional
	public void batchsave(List<Map<String, Object>> dataResult, String currUserName) {
		if(CollectionUtils.isEmpty(dataResult))  throw new VpRuntimeException(RestConst.ReturnCode.ERROR, "导入文件没有用户数据！");
		//校验角色是否存在，并对角色转码
		List<Role> roles = roleService.findUserMgrRole(currUserName);
		Map<String, Role> roleMap = new HashMap<String, Role>();
		Set<String> keys = dataResult.get(0).keySet();
		for (Role role : roles) {
			String key = "角色[" + role.getRoleName() + "]";
			if(keys.contains(key)){
				roleMap.put(key, role);
			}
		}
		
		/**
         * 校验行
         */
        int verfiyRow = ExcelUtil.VERIFY_STARTROW;
        for (Map<String, Object> data : dataResult) {
            User user = generateUserByExcelRow(verfiyRow, data);
            Set<String> keyset = data.keySet();
			Iterator<String> iterator = keyset.iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				if(roleMap.containsKey(key)) {
					user.getRoles().add(roleMap.get(key));
				}
			}
            if (CollectionUtils.isEmpty(user.getRoles())) {
                user.setPrivFlag(Constants.INTEGER_VALUE_FALSE);
            } else {
                user.setPrivFlag(Constants.INTEGER_VALUE_TRUE);
            }
            userRepository.save(user);
            verfiyRow++;
        }

	}
	
	public User generateUserByExcelRow(int verfiyRow,
			Map<String, Object> rowResult) {
		/**
		 * 校验用户名是否合法
		 */
		Object userName = rowResult.get("用户账号");
		userName=(userName==null ? "":userName);
		if (!ExcelUtil.verifyName("^[a-zA-Z0-9_]{3,16}", String.valueOf(userName))) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，用户账号只可填写(3-16)位英文字母、数字和下划线！");
		}
		/**
		 * 校验用户名是否重复
		 */
		User checkName = findUserByUserName(String.valueOf(userName));
		if (checkName != null) {
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，用户账号" + userName + "已存在");
		}
		/**
		 * 校验姓名是否合法
		 */
		Object realName = rowResult.get("用户姓名");
		realName=(realName==null ? "":realName);
		if (!ExcelUtil.verifyText(String.valueOf(realName), 1, 50, 3)) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，用户姓名【" + userName + "】只可填写(1-50)位英文字母、数字、下划线和中文！");
		}
		/**
		 * 校验机构代码是否合法
		 */
		Object orgCode = rowResult.get("所属机构代码");
		orgCode=(orgCode==null ? "":orgCode);
		if (!ExcelUtil.verifyCode("[a-zA-Z0-9_]{3,32}", String.valueOf(orgCode))) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，所属机构代码【" + orgCode + "】只可填写(3-32)位英文字母、数字和下划线！");
		}
		/**
		 * 校验机构是否存在
		 */
		Organ org = organRepository.findOrganByOrgCode(String.valueOf(orgCode));
		if (null == org) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，机构代码【" + orgCode + "】关联的机构不存在或没有该机构的操作权限！");
		}
		/**
		 * 校验员工编号是否合法
		 */
		Object empNo = rowResult.get("员工编号");
		empNo=(empNo==null ? "":empNo);
		if (empNo != null && StringUtils.isNotBlank(String.valueOf(empNo)) && !ExcelUtil.verifyNum(String.valueOf(empNo))) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，员工编号【" + empNo + "】只可填写(1-50)位英文字母、数字和下划线！");
		}
		/**
		 * 校验身份证号是否存在
		 */
		Object idcard = rowResult.get("身份证号");
		idcard=(idcard==null ? "":idcard);
		if (idcard != null && StringUtils.isNotBlank(String.valueOf(idcard)) && !ExcelUtil.verifyCode("^[\\d]{6}((19[\\d]{2})|(200[0-8]))((0[1-9])|(1[0-2]))((0[1-9])|([12][\\d])|(3[01]))[\\d]{3}[0-9xX]$", String.valueOf(idcard))) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，身份证【" + idcard + "】格式不正确！");
		}
		/**
		 * 校验邮件是否合法
		 */
		Object email = rowResult.get("邮箱");
		email=(email==null ? "":email);
		if (email != null && StringUtils.isNotBlank(String.valueOf(email))) {
		    if (!ExcelUtil.verifyMail(String.valueOf(email))) {
		        throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，邮箱【" + email + "】格式不正确！");
		    }
		    /**
		     * 校验邮件长度是否合法
		     */
		    if (String.valueOf(email).length() > 50) {
		        throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，邮件【" + email + "】只可填写(0-50)位字符！");
		    }
		}

		/**
		 * 校验办公电话是否合法
		 */
		Object officePhone = rowResult.get("办公电话");
		officePhone=(officePhone==null ? "":officePhone);
		if (officePhone != null && StringUtils.isNotBlank(String.valueOf(officePhone))) {
		    if (!ExcelUtil.verifyPhone(String.valueOf(officePhone))) {
		        throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，办公电话【" + officePhone + "】格式不正确！");
		    }
		}
		/**
		 * 校验移动电话是否合法
		 */
		Object mobilePhone = rowResult.get("移动电话");
		mobilePhone=(mobilePhone==null ? "":mobilePhone);
		if (mobilePhone != null && StringUtils.isNotBlank(String.valueOf(mobilePhone))) {
		    if (!ExcelUtil.verifyPhone("^0?1[358]\\d{9}$", String.valueOf(mobilePhone))) {
		        throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，移动电话【" + mobilePhone + "】格式不正确！");
		    }
		}
		/**
		 * 备注
		 */
		Object remark = rowResult.get("备注");
		remark=(remark==null ? "":remark);
		if (!ExcelUtil.verifyText(String.valueOf(remark), 0, 200, 3)) {
		    throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "第" + verfiyRow + "行，备注只可填写(0-200)位英文字母、数字、下划线和中文！");
		}
		User user = new User();
		user.setUserName(String.valueOf(userName));
		user.setRealName(String.valueOf(realName));
		user.setOrg(org);
		user.setEmpNo(String.valueOf(empNo));
		user.setIdcard(String.valueOf(idcard));
		user.setEmail(String.valueOf(email));
		user.setOfficePhone(String.valueOf(officePhone));
		user.setMobilePhone(String.valueOf(mobilePhone));
		user.setRemark(String.valueOf(remark));
		
		// 新增时初始密码
        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setPassword(passwordEncoder.encode(pwdRuleService.findDefaultPwd()));
        userLogin.setUserFlag(Constants.INTEGER_VALUE_TRUE);
        userLogin.setIsFirstLogin(Constants.INTEGER_VALUE_TRUE);

        user.setUserLogin(userLogin);
        user.setStatus(Constants.INTEGER_VALUE_TRUE);
		
		return user;
	}
}
