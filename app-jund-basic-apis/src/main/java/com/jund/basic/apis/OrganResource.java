package com.jund.basic.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.core.util.BeanUtil;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseController;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.platformwork.security.model.Organ;
import com.jund.platformwork.security.model.dto.OrganForm;
import com.jund.security.Constants;
import com.jund.security.service.OrganService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("机构管理API文档")
@RestController
@RequestMapping(value = "/api/v1/organ")
public class OrganResource extends BaseController {

	@Autowired
	private OrganService organService;

	@ApiOperation(value = "查询机构列表", notes = "")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "orgId", value = "机构", dataType = "Long"),
		@ApiImplicitParam(name = "orgSeq", value = "机构序列", dataType = "String"),
		@ApiImplicitParam(name = "orgCode", value = "机构号", dataType = "String"),
		@ApiImplicitParam(name = "status", value = "状态", dataType = "Integer"),
		@ApiImplicitParam(name = "orgLevel", value = "机构层次", dataType = "Integer"),
		@ApiImplicitParam(name = "orgName", value = "机构名", dataType = "String"),
		@ApiImplicitParam(name = "parentOrgan", value = "父机构", dataType = "Organ"),
		@ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Integer")
	})
	@GetMapping(value = "/page")
	public ResponseInfo asyFindOrgans(OrganForm organForm,
									 @PageableDefault(sort = "orgCode", direction = Sort.Direction.DESC) Pageable pageable) {
		return new ResponseInfo(organService.findAll(organForm, pageable));
	}

	@ApiOperation(value = "查询机构详情", notes = "")
	@ApiImplicitParam(name = "id", value = "机构id", required = true, dataType = "Long")
	@GetMapping(value = "/{id}")
	public ResponseInfo getOrganDetail(@PathVariable("id") Long id) {
		return new ResponseInfo(organService.findOne(id));
	}

	@ApiOperation(value = "根据orgLevel查询机构", notes = "返回值为orgId，orgName")
	@ApiImplicitParam(name = "orgLevel", value = "机构层次", required = true, dataType = "Integer")
	@GetMapping(value = "/level/{orgLevel}")
	public ResponseInfo getOrgansByOrgLevel(@PathVariable("orgLevel") Integer orgLevel) {
		if (orgLevel == null) {
			throw new RuntimeException("等级机构获取异常");
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Organ> orgList = organService.findEnableByLevel(orgLevel - 1);
		for (Organ organ : orgList) {
			Map<String, Object> organMap = new HashMap<String, Object>();
			organMap.put("orgId", organ.getId());
			organMap.put("orgName", organ.getOrgName());
			list.add(organMap);
		}
		return new ResponseInfo(list);
	}

	@ApiOperation(value = "根据parentId加载机构树", notes = "")
	@GetMapping(value = "/mgrtree")
	public ResponseInfo organMgrTree() {
		Long currOrgId = 1L; // UserContext.getUserInfo().getOrg().getOrgId()
		return new ResponseInfo(organService.findAllChildByPid(currOrgId));
	}

	@ApiOperation(value = "加载机构树", notes = "状态为启用")
	@GetMapping(value = "/tree")
	public ResponseInfo ogranTree() {
		return new ResponseInfo(organService.findAllChildByPid(1l, Constants.INTEGER_VALUE_TRUE));
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "异步加载机构树", notes = "状态为启用")
	@ApiImplicitParam(name = "parentId", value = "父机构Id", required = true, dataType = "Long")
	@GetMapping(value = "/tree/async/{parentId}")
	public ResponseInfo ogranTreeAsync(@PathVariable("parentId") Long parentId) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Organ> organList = organService.findOrganByPidAndStatus(parentId);
		String json = objectMapper.writeValueAsString(organList);
		json = json.replace("}", ",\"isParent\":\"true\"}");
//		ObjectMapper mapper = new ObjectMapper();
		List<Organ> list = new ArrayList<Organ>();
		try {
			list = objectMapper.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseInfo(list);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "异步加载机构树（无参）", notes = "状态为启用,第一次加载，无参")
	@GetMapping(value = "/tree/async")
	public ResponseInfo ogranTreeAsync() throws JsonProcessingException {
		List<Organ> organList = organService.findOrganByPidAndStatus(null);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(organList);
		json = json.replace("}", ",\"isParent\":\"true\"}");
		ObjectMapper mapper = new ObjectMapper();
		List<Organ> list = new ArrayList<Organ>();
		try {
			list = mapper.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseInfo(list);
	}

	@ApiOperation(value = "机构号唯一性校验", notes = "")
	@ApiImplicitParam(name = "organCode", value = "机构号", required = true, dataType = "String")
	@GetMapping(value = "/check/code/{organCode}")
	public ResponseInfo checkOrgCodeUnique(@PathVariable("organCode") String organCode) {
		Organ organ = organService.findOrganByOrgCode(organCode);
		if (organ != null)
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构号已存在，无法使用！");
		
		return new ResponseInfo();
	}

	@ApiOperation(value = "机构名唯一性校验", notes = "")
	@ApiImplicitParam(name = "organName", value = "机构名", required = true, dataType = "String")
	@GetMapping(value = "/check/name/{organName}")
	public ResponseInfo checkOrgNameUnique(@PathVariable("organName") String organName) {
		Organ organ = organService.findOrganByOrgName(organName);
		if (organ != null)
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构名称已存在，无法使用！");
		
		return new ResponseInfo();
	}

	@ApiOperation(value = "判断上级机构是否合法", notes = "不能将自己或者自己的所有下级设置为父机构")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "id", value = "机构id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "parentId", value = "上级机构", required = true, dataType = "Long") 
	})
	@GetMapping(value = "/check/parent/{id}/{parentId}")
	public ResponseInfo checkParentId(@PathVariable("id") Long id, @PathVariable("parentId") Long parentId) {
		boolean result = organService.checkParentId(id, parentId);
		if (!result) {
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "上级机构不合法，不能将自己的子级设置为父机构！");
		}
		return new ResponseInfo();
	}

	@ApiOperation(value = "是否启用/撤销", notes = "")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "ids", value = "机构id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer") 
	})
	@GetMapping(value = "/judgement/{ids}/{status}")
	public ResponseInfo isStartStatus(@PathVariable("ids") Long[] ids, @PathVariable("status") Integer status) {
		if (status == 1) {
			organService.isStartStatus(ids);
		} else {
		    organService.isCancelStatus(ids);
		}
		return new ResponseInfo();
	}

	@ApiOperation(value = "启用&撤销", notes = "")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "ids", value = "机构id", required = true, dataType = "Long"),
		@ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer") 
	})
	@PutMapping(value = "/status")
	public ResponseInfo status(@RequestBody OrganForm organForm) {
		Long[] ids = organForm.getIds();
        Integer status = organForm.getStatus();
		organService.updateStatus(ids,status);
		return new ResponseInfo();
	}

	@ApiOperation(value = "新增或修改机构", notes = "")
	@ApiImplicitParam(name = "organForm", value = "OrganForm", required = true, dataType = "OrganForm")
	@PostMapping
	public ResponseInfo save(@RequestBody OrganForm organForm) {
		if (StringUtils.isBlank(organForm.getOrgCode()) || StringUtils.isBlank(organForm.getOrgName())) {
            throw new RuntimeException("机构名、机构号都不能空");
        }
		
		Organ checkName = organService.findOrganByOrgName(organForm.getOrgName());
		if (checkName != null)
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构名称已存在，无法使用！");
		
		Organ checkCode = organService.findOrganByOrgCode(organForm.getOrgCode());
		if (checkCode != null)
			throw new VpRuntimeException(RestConst.ReturnCode.VALID_ERROR, "机构号已存在，无法使用！");
        
        Organ organ = new Organ();
        try {
            BeanUtil.copyProperties(organ, organForm);
        } catch (Exception e) {
        	throw new VpRuntimeException(RestConst.ReturnCode.ERROR, ""); 
        }
        
        Long pid = null;
        if(null != organForm.getParentOrgan()){
        	pid = organForm.getParentOrgan().getId();
        }
        Long oldPId = organForm.getOldParentId();
		organService.saveWithParent(organ,pid,oldPId);
		return new ResponseInfo();
	}

}
