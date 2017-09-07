package com.jund.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.Role;
import com.jund.security.service.RoleService;
import com.jund.testcase.ITestCase;

public class RoleControllerTestCase extends SpringBootSuite implements ITestCase{

    @Autowired
    private RoleService roleService;
    
    @Before
    public void initalize() {
    	this.domainUrl = "/api/v1/role";
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testUpdate() throws Exception {
    	Long roleId = findByRoleCode("ROLE_Ceshi").getId();
        Map map = new HashMap();
        map.put("id", roleId);
        map.put("roleCode", "aaabbb");
        map.put("roleName", "aaabbb");
//        map.put("appIds", new Long[]{1L, 3L});
//        map.put("menuIds", new Long[]{1L, 2L});
//        map.put("subIds", new Long[]{854L});
        String requestJson = this.toJsonString(map);
        mockMvc.perform(post(domainUrl).content(requestJson).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK));
    }

    @Test
    public void testjudgementOk() throws Exception {
    	Long appId = findByRoleCode("ROLE_Ceshi1").getId();
    	String uri = domainUrl + "/judgement/"+appId;
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testjudgementOk:" + result);
    }

    @Test
    public void testjudgementError() throws Exception {
    	Long roleId = findByRoleCode("ROLE_Ceshi5").getId();
    	String uri = domainUrl + "/judgement/"+roleId;
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testjudgementError:" + result);
    }

    @Test
    public void testDelete() throws Exception {
    	Long roleId = findByRoleCode("ROLE_Ceshi2").getId();
    	String uri = domainUrl + "/"+roleId;
    	String result = mockMvc.perform(delete(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testDelete:" + result);
    }

    @Test
    public void testPage() throws Exception {
         String uri = domainUrl+"/page?sort=roleCode,asc";
         String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.data").isNotEmpty())
 				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testPage:" + result);
    }

    @Test
    public void testCheckOrgCodeUniqueOk() throws Exception {
    	String uri = domainUrl + "/check/111";
    	String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testCheckOrgCodeUniqueOk:" + result);
    }

    @Test
    public void testCheckOrgCodeUniqueError() throws Exception {
    	String uri = domainUrl + "/check/001";
    	String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testCheckOrgCodeUniqueError:" + result);
    }
    
    @Test
    public void testCheckRoleNameUniqueOk() throws Exception {
    	String uri = domainUrl + "/check?roleName=sysadmin";
    	String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testCheckOrgCodeUniqueError:" + result);
    }
    
    @Test
    public void testCheckRoleNameUniqueOk1() throws Exception {
    	String uri = domainUrl + "/check?roleCode=ROLE_sysadmin&roleName=sysadmin";
    	String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testCheckOrgCodeUniqueError:" + result);
    }

    @Test
    public void testFindRoleByCurrUser() throws Exception {
    	String uri = domainUrl + "/curruser/own";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn().getResponse().getContentAsString();
         System.out.println("testFindRoleByCurrUser:" + result);
    }

    @Test
    public void testFindRoleAndSubRoleByCurrUser() throws Exception {
    	String uri = domainUrl + "/curruser/sub/1";
    	String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn().getResponse().getContentAsString();
        System.out.println("testFindRoleAndSubRoleByCurrUser:" + result);
    }

    @Test
    public void testFindRoleByUser() throws Exception {
    	String uri = domainUrl + "/user/1";
    	String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn().getResponse().getContentAsString();
        System.out.println("testFindRoleByUser:" + result);
    }

    @Test
    public void testFindSubRoleByRole() throws Exception {
    	String uri = domainUrl + "/sub/1";
    	String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn().getResponse().getContentAsString();
        System.out.println("testFindSubRoleByRole:" + result);
    }

    @Test
	public void testFindOne() throws Exception {
		Long roleId = findByRoleCode("Role_Ceshe3").getId();
    	String uri = domainUrl + "/"+roleId;
    	String result = mockMvc.perform(get(uri)
 				.contentType(MediaType.APPLICATION_JSON))
 				.andDo(print())
 				.andExpect(status().isOk())
 				.andExpect(jsonPath("$.data.roleCode").value("Role_Ceshe3"))
 				.andReturn().getResponse().getContentAsString();
 		System.out.println("testFindOne:" + result);
		
	}

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
	public void testSave() throws Exception {
    	Map map = new HashMap();
	    	map.put("roleCode", "ROLE_Ceshi");
	        map.put("roleName", "发");
//	        map.put("appIds", new Long[]{1L, 3L});
//	        map.put("menuIds", new Long[]{1L, 2L});
//	        map.put("subIds", new Long[]{46L});
//	        map.put("supId", 1L);
	        String requestJson = this.toJsonString(map);
	        mockMvc.perform(post(domainUrl).content(requestJson)
	        		.contentType(MediaType.APPLICATION_JSON)).andDo(print())
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK));
	}

	private Role findByRoleCode(String roleCode){
	    return roleService.findByRoleCode(roleCode);
	}
}
