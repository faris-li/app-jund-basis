package com.jund.basic.apis;

import com.jund.framework.core.annotation.Logging;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseController;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.platformwork.security.model.Menu;
import com.jund.platformwork.security.model.dto.MenuForm;
import com.jund.security.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api("菜单管理API文档")
@RestController
@RequestMapping(value = "/api/v1/menu")
public class MenuResource extends BaseController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取菜单详细信息", notes = "根据菜单ID")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, dataType = "Long")
    @GetMapping(value = "/{menuId}")
    public ResponseInfo detail(@PathVariable("menuId") Long menuId) {
        return new ResponseInfo(menuService.findOne(menuId));
    }

    @ApiOperation(value = "获取菜单列表", notes = "")
    @GetMapping
    public ResponseInfo findAll() {
		return new ResponseInfo(menuService.findAllMenu());
    }

	@Logging(title = "创建或修改菜单",data = "创建菜单{0}")
    @ApiOperation(value = "创建或修改菜单", notes = "")
	@ApiImplicitParam(name = "menu", value = "Menu", required = true, dataType = "Menu")
    @PostMapping
    public ResponseInfo saveMenu(@RequestBody Menu menu) {
        menuService.saveMenu(menu);
        return new ResponseInfo();
    }

	@Logging(title = "删除菜单",data = "删除菜单{0}")
    @ApiOperation(value = "删除菜单", notes = "根据菜单ID删除")
    @ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{menuId}")
    public ResponseInfo deleteMenu(@PathVariable("menuId") Long menuId) {
        menuService.deleteOne(menuId);
        return new ResponseInfo();
    }

    @ApiOperation(value = "检验菜单号唯一性", notes = "")
    @ApiImplicitParam(name = "menuCode", value = "菜单号", required = true, dataType = "String")
    @GetMapping(value = "/check/{menuCode}")
    public ResponseInfo checkMenuCode(@PathVariable("menuCode") String menuCode){
    	Menu menu = menuService.findMenuByMenuCode(menuCode);
    	if(menu != null){
    		throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "菜单号已存在，无法使用！！");
    	}
    	return new ResponseInfo();
    }
    
    @ApiOperation(value = "获取角色拥有的菜单id列表", notes = "")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long")
    @GetMapping(value = "/role/{roleId}")
    public ResponseInfo findMenuIdByRoleId(@PathVariable("roleId") Long roleId) {
        return new ResponseInfo(menuService.findMenuIdByRoleId(roleId));
    }
    
    @ApiOperation(value = "获取用户拥有的菜单", notes = "")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String")
    @GetMapping(value = "/user/{userName}")
    public ResponseInfo findMenuByUserName(@PathVariable("userName") String userName) {
        return new ResponseInfo(menuService.findMenuByUserName(userName));
    }
    
    @Logging(title = "菜单拖动")
    @ApiOperation(value = "菜单拖动", notes = "")
    @ApiImplicitParams({
    	 @ApiImplicitParam(name = "currId", value = "当前id", required = true, dataType = "Long"),
    	 @ApiImplicitParam(name = "toId", value = "目标节点", required = true, dataType = "Long"),
    	 @ApiImplicitParam(name = "type", value = "移动类型", required = true, dataType = "Integer")
    })
    @PutMapping(value = "/move")
    public ResponseInfo moveMenu(@RequestBody MenuForm menuForm) {
    	menuService.saveMoveMenu(menuForm);
    	return new ResponseInfo();
    }
    
    @ApiOperation(value = "获取指定应用的菜单", notes = "根据应用ID")
    @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "Long")
    @GetMapping(value = "/app/{appId}")
    public ResponseInfo findMenuByAppId(@PathVariable("appId") Long appId) {
        return new ResponseInfo(menuService.findMenuByAppId(appId));
    }
    
    @ApiOperation(value = "获取指定应用的菜单", notes = "无参")
    @GetMapping(value = "/app")
    public ResponseInfo findRootMenu() {
        return new ResponseInfo(menuService.findRootMenu());
    }
}
