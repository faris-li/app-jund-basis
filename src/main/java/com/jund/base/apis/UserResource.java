package com.jund.base.apis;

import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.core.util.BeanUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseController;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.framework.security.authentication.AuthenticationHolder;
import com.jund.platformwork.security.model.User;
import com.jund.platformwork.security.model.dto.UserForm;
import com.jund.security.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Api("用户管理API文档")
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserResource extends BaseController {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "获取用户详细信息", notes = "根据用户id获取用户详细信息")
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
	@GetMapping(value = "/{userId}")
	public ResponseInfo findUser(@PathVariable("userId") Long userId) {
		return new ResponseInfo(userService.findOne(userId));
	}

	@ApiOperation(value = "分页查询数据", notes = "")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "realName", value = "用户姓名", dataType = "String"),
		@ApiImplicitParam(name = "userName", value = "用户账号",  dataType = "String"),
		@ApiImplicitParam(name = "userFlg", value = "用户启用/锁定状态", dataType = "Integer"),
		@ApiImplicitParam(name = "orgId", value = "机构", dataType = "Long"),
		@ApiImplicitParam(name = "roleId", value = "角色", dataType = "Long"),
		@ApiImplicitParam(name = "status", value = "生效标识", dataType = "Integer"),
		@ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Integer")
	})
	@GetMapping(value = "/page")
	public ResponseInfo findAll(UserForm user, @PageableDefault(sort="userName", direction=Sort.Direction.DESC) Pageable pageable) {
		String currUserName = AuthenticationHolder.getUsername();
		return new ResponseInfo(userService.findAllUser(currUserName, user, pageable));
	}

	@ApiOperation(value = "是否可以注销", notes = "")
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
	@GetMapping(value = "/judgement/{userIds}")
	public ResponseInfo judgement(@PathVariable("userIds") Long[] userIds) {
		if (!userService.isCancelUsing(userIds)) {
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "该用户拥有管理员角色，不可注销！");
		}
		
		return new ResponseInfo();
	}

	@Logging(title = "注销用户",data = "注销用户{0}")
	@ApiOperation(value = "注销用户", notes = "")
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
	@DeleteMapping(value="/{userIds}")
	public ResponseInfo cancelUsing(@PathVariable("userIds") Long[] userIds) {
		this.userService.cancelUsing(userIds);
		return new ResponseInfo();
	}

	@ApiOperation(value = "帐号唯一性校验", notes = "")
	@ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String")
	@GetMapping(value = "/check/{userName}")
	public ResponseInfo checkUserName(@PathVariable("userName") String userName) {
		User user = this.userService.findUserByUserName(userName);
		if (user != null) {
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "用户名已存在，无法使用！");
		}
		return new ResponseInfo();
	}

	@ApiOperation(value = "创建或修改用户", notes = "")
	@ApiImplicitParam(name = "userForm", value = "UserForm", required = true, dataType = "UserForm")
	@PostMapping
	public ResponseInfo save(@RequestBody UserForm userForm) {
		User user = new User();
		try {
			BeanUtil.copyProperties(user, userForm);
		} catch (Exception e) {
			throw new VpRuntimeException(RestConst.ReturnCode.ERROR, ""); 
		} 
		userService.save(user,userForm.getOrgan().getId(),userForm.getRoleIds());
		return new ResponseInfo();
	}

	@GetMapping("/exp")
	public void exp(HttpServletResponse response) throws Exception {
		String fileName = "应用数据.xls";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Type", "application/VND.MS-EXCEL");
		response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"UTF-8"));
		response.setCharacterEncoding("UTF-8");
		
		String currUserName = AuthenticationHolder.getUsername();
		Workbook workbook = userService.exp(currUserName);
		workbook.write(response.getOutputStream());
	}
	
	/**
	 * 导入用户权限 数据
	 */
	@PostMapping(value = "/imp")
	public ResponseInfo imp(@RequestBody MultipartFile excelFile) throws Exception {
		List<Map<String, Object>> dataResult = impExcelForMap(excelFile);
		String currUserName = AuthenticationHolder.getUsername();
		userService.batchsave(dataResult, currUserName);
		return new ResponseInfo();
	}
}
