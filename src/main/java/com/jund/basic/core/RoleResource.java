package com.jund.basic.core;

import com.jund.framework.core.Constants;
import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.core.util.BeanUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseController;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.framework.security.authentication.AuthenticationHolder;
import com.jund.platformwork.security.model.Role;
import com.jund.platformwork.security.model.User;
import com.jund.platformwork.security.model.dto.RoleForm;
import com.jund.security.service.RoleService;
import com.jund.security.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api("角色管理API文档")
@RestController
@RequestMapping(value = "/api/v1/role")
public class RoleResource extends BaseController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "获取角色基本信息", notes = "")
	@ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Long")
	@GetMapping(value = "/{id}")
	public ResponseInfo detail(@PathVariable Long id) {
		return new ResponseInfo(roleService.findOne(id));
	}
	
	@ApiOperation(value = "新增或修改角色", notes = "")
	@ApiImplicitParam(name = "roleform", value = "RoleForm", dataType = "RoleForm")
	@PostMapping
    public ResponseInfo save( @RequestBody RoleForm roleform) {
		Long[] appIds = roleform.getAppIds();
		Long[] menuIds = roleform.getMenuIds();
		Long[] subIds = roleform.getSubIds();
		Long supId = roleform.getSupId();
		
		Role role = new Role();
		try {
			BeanUtil.copyProperties(role, roleform);
        } catch (Exception e) {
        	throw new VpRuntimeException(RestConst.ReturnCode.ERROR, ""); 
        }
		
		if(role.getId() == null){
			role.setAdmFlag(Constants.INTEGER_VALUE_FALSE);
		}
		
		role.setPrivFlag(ArrayUtils.isEmpty(menuIds) ? Constants.INTEGER_VALUE_FALSE : Constants.INTEGER_VALUE_TRUE);
		
		roleService.save(role, appIds, menuIds, subIds, supId);
		return new ResponseInfo();
    }
	
    @ApiOperation(value = "查询指定用户直接拥有的角色", notes = "")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "roleName", value = "角色名称", dataType = "String"),
		@ApiImplicitParam(name = "roleCode", value = "角色号",  dataType = "String"),
		@ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Integer")
	})
   	@GetMapping(value = "/page")
   	public ResponseInfo findRoleAndSubByUser(RoleForm roleform, @PageableDefault(sort="roleCode", direction=Sort.Direction.DESC) Pageable pageable) {
    	String currUserName = AuthenticationHolder.getUsername();
       	return new ResponseInfo(roleService.findRoleAndSubByCurrUser(currUserName, roleform, pageable));
   	}
    
    @ApiOperation(value = "判断角色是否可以删除", notes = "")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Long")
   	@GetMapping(value = "/judgement/{ids}")
   	public ResponseInfo judgement(@PathVariable("ids") Long[] ids) {
    	for (Long roleId : ids) {
    		List<User> users = userService.findUserByRole(roleId);
    		if(! CollectionUtils.isEmpty(users)) {
    			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色下已经存在用户，不能删除！");
    		}
		}
    	return new ResponseInfo();
   	}
    
    @Logging(title = "删除角色", data = "删除角色{0}")
    @ApiOperation(value = "删除角色", notes = "根据角色id删除角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{ids}")
    public ResponseInfo delete(@PathVariable Long[] ids) {
        roleService.delete(ids);
        return new ResponseInfo();
    }
    
    @ApiOperation(value = "角色编号唯一性校验", notes = "")
	@ApiImplicitParam(name = "roleCode", value = "角色号", required = true, dataType = "String")
	@GetMapping(value = "/check/{roleCode}")
	public ResponseInfo checkRoleCodeUnique(@PathVariable("roleCode") String roleCode) {
    	Role role = new Role();
    	role.setRoleCode(roleCode);
    	Example<Role> example = Example.of(role);
    	if(roleService.exists(example))
    		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色号已经存在，无法使用！");
    	return new ResponseInfo();
	}
    
    @ApiOperation(value = "角色名称唯一性校验", notes = "")
   	@ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
   	@GetMapping(value = "/check")
   	public ResponseInfo checkRoleNameUnique(@PathParam("roleCode") String roleCode,@PathParam("roleName") String roleName) {
    	Role checkRole = new Role();
		checkRole.setRoleName(roleName);
		Example<Role> example = Example.of(checkRole);
    	if(StringUtils.isNotEmpty(roleCode)){
       		Role role = roleService.findByRoleCode(roleCode);
       		if(!role.getRoleName().equals(roleName)){
       			if(roleService.exists(example))
       	       		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色名称已经存在，无法使用！");
       		}
       	}else{
   			if(roleService.exists(example))
   	       		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该角色名称已经存在，无法使用！");
       	}
       	return new ResponseInfo();
   	}

    @ApiOperation(value = "查询指定用户直接拥有的角色", notes = "")
   	@GetMapping(value = "/user/{userId}")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long")
   	public ResponseInfo findRoleByUser(@PathVariable("userId") Long userId) {
    	User user = userService.findOne(userId);
       	return new ResponseInfo(roleService.findRoleByUser(user.getUserName()));
   	}
    
    @ApiOperation(value = "查询当前用户直接拥有的角色", notes = "")
	@GetMapping(value = "/curruser/own")
	public ResponseInfo findRoleByCurrentUser() {
    	String currUserName = AuthenticationHolder.getUsername();
    	return new ResponseInfo(roleService.findRoleByUser(currUserName));
	}
    
    @ApiOperation(value = "查询当前用户管理的角色", notes = "")
    @ApiImplicitParam(name = "id", value = "角色id", dataType = "Long")
   	@GetMapping(value = "/curruser/sub")
   	public ResponseInfo findRoleAndSubRoleByCurrentUser() {
       	String currUserName = AuthenticationHolder.getUsername();
       	return new ResponseInfo(roleService.findUserMgrRole(currUserName));
   	}
    
    @ApiOperation(value = "查询当前用户管理的角色", notes = "")
    @ApiImplicitParam(name = "id", value = "角色id", dataType = "Long")
   	@GetMapping(value = "/sub/{id}")
   	public ResponseInfo findSubRoleByRole(@PathVariable("id") Long id) {
       	return new ResponseInfo(roleService.findSubRoleByRole(id));
   	}

}
