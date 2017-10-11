package com.jund.basis.apis;


import com.jund.basis.apis.form.AppForm;
import com.jund.basis.core.entity.App;
import com.jund.basis.core.service.AppService;
import com.jund.framework.core.Const;
import com.jund.framework.core.annotation.Logger;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseResource;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.framework.mvc.response.RestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Api("应用管理API文档")
@RestController
@RequestMapping(value = "/api/v1/app")
public class AppResource extends BaseResource {

	@Autowired
	private AppService appService;

	@ApiOperation(value = "查询应用列表", notes = "")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "appCode", value = "应用编号", dataType = "String"),
		@ApiImplicitParam(name = "appName", value = "应用名称", dataType = "String"),
		@ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Integer")
	})
	@GetMapping(value = "/page")
	public ResponseInfo findAll(App app, @PageableDefault(sort="appCode", direction=Sort.Direction.DESC) Pageable pageable){
		return new ResponseInfo(appService.findAll(app, pageable));
	}

	@ApiOperation(value = "查询应用详情", notes = "")
	@ApiImplicitParam(name = "id", value = "应用id", required = true, dataType = "Long")
	@GetMapping(value = "/{id}")
	public ResponseInfo findApp(@PathVariable("id") Long id) {
		return new ResponseInfo(appService.findOne(id));
	}
	
	@ApiOperation(value = "判断是否能够删除", notes = "")
    @ApiImplicitParam(name = "id", value = "应用id", required = true, dataType = "Long")
    @GetMapping(value = "/judgement/{ids}")
    public ResponseInfo judgement(@PathVariable Long[] ids) {
		appService.judgement(ids);
        return new ResponseInfo();
    }
	
	@Logger(title = "删除应用",data = "删除应用{0}")
    @ApiOperation(value = "删除应用", notes = "根据应用id删除应用")
    @ApiImplicitParam(name = "id", value = "应用id", required = true, dataType = "Long")
    @DeleteMapping(value = "/{ids}")
    public ResponseInfo delete(@PathVariable Long[] ids) {
        appService.delete(ids);
        return new ResponseInfo();
    }

    @ApiOperation(value = "启用/撤销应用", notes = "")
	@ApiImplicitParams({
		 @ApiImplicitParam(name = "id", value = "应用id", required = true, dataType = "Long"),
		 @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer")
	})
    @PutMapping(value = "/status")
    public ResponseInfo status(@RequestBody AppForm appForm) {
		Long[] ids = appForm.getId();
		Integer status = appForm.getStatus();
        appService.updateStatus(ids,status);
        return new ResponseInfo();
    }
	
	@Logger(title = "新增或修改应用",data = "新增或修改应用{0}")
	@ApiOperation(value = "新增或修改应用", notes = "")
	@ApiImplicitParam(name = "app", value = "App", required = true, dataType = "App")
	@PostMapping
	public ResponseInfo save(@RequestBody App app) {
		if(app == null ){
			throw new RestException(Const.ReturnCode.ERROR, "应用为空!");
		}
		appService.save(app);
		return new ResponseInfo();
	}

	@ApiOperation(value = "查询所有应用", notes = "")
	@GetMapping
	public ResponseInfo findAll() {
		return new ResponseInfo(appService.findAll());
	}
	
	@ApiOperation(value = "应用编号唯一性检验", notes = "")
	@ApiImplicitParam(name = "appCode", value = "应用编码", required = true, dataType = "String")
	@GetMapping(value = "/check/{appCode}")
	public ResponseInfo checkCode(@PathVariable("appCode") String appCode) {
		App app = new App();
		app.setAppCode(appCode);
		Example<App> example = Example.of(app);
		if(appService.exists(example)){
			throw new RestException(RestConst.ReturnCode.VALID_ERROR, "该应用编码已经存在，无法使用！");
		}
		return new ResponseInfo();
	}
	
	@ApiOperation(value = "查询指定角色拥有的应用列表", notes = "")
	@ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Long")
	@GetMapping(value = "/role/{roleId}")
	public ResponseInfo findAppIdByRoleId(@PathVariable("roleId") Long roleId) {
		return new ResponseInfo(appService.findAppIdByRoleId(roleId));
	}
}
