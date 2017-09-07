package com.jund.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.Organ;
import com.jund.platformwork.security.model.dto.OrganForm;
import com.jund.security.service.OrganService;
import com.jund.testcase.ITestCase;

@FixMethodOrder(MethodSorters.DEFAULT)
public class OrganControllerTestCase extends SpringBootSuite implements ITestCase{

    @Autowired
    private OrganService organService;
    
    @Before
    public void initalize() {
    	this.domainUrl = "/api/v1/organ";
    }
   
    @Test
    public void initData() throws Exception {
    	for(int i = 1; i < 3; i ++){
    		OrganForm organ = new OrganForm();
            Organ pOrgan = new Organ();
            pOrgan.setId(1l);
            organ.setOrgCode("8000042"+i);
            organ.setOrgName("测试机构"+i);
            organ.setParentOrgan(pOrgan);
//            organ.setStatus(i <2 ?0: 1);
            organ.setStatus(1);
            String requestJson = this.toJsonString(organ);
            mockMvc.perform(post(domainUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk());
    	}
    }

    @Test
    public void testGetOrgansByOrgLevel() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000421").getId();
        String uri = domainUrl + "/level/2";
        String result = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].orgId").value(orgId))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testGetOrgansByOrgLevel:"+result);
    }

    @Test
    public void testOrganMgrTree() throws Exception {
    	String uri = domainUrl + "/mgrtree";
    	String result = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testOrganMgrTree:"+result);
    }

    @Test
    public void testOrganTree() throws Exception {
    	String uri = domainUrl + "/tree";
    	String result = mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testOrganTree:"+result);
    }

    @Test
    public void testOgranTreeAsync() throws Exception {
        String uri = domainUrl + "/tree/async/1";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testOgranTreeAsync:"+result);
    }

    @Test
    public void testCheckOrgCodeUniqueError() throws Exception {
    	String uri = domainUrl + "/check/code/80000420";
    	String result = mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckOrgCodeUniqueError:"+result);
    }

    @Test
    public void testCheckOrgCodeUniqueOk() throws Exception {
    	String uri = domainUrl + "/check/code/80000420";
    	String result =  mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckOrgCodeUniqueOk:"+result);
    }

    @Test
    public void testCheckOrgNameUniqueError() throws Exception {
    	String uri = domainUrl + "/check/name/cccc";
    	String result = mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckOrgNameUniqueError:"+result);
    }
    
    @Test
    public void testCheckOrgNameUniqueOk() throws Exception {
    	String uri = domainUrl + "/check/name/测试机构0";
    	String result = mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckOrgNameUniqueOk:"+result);
    }

    @Test
    public void testCheckParentIdError() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000422").getId();
    	String uri = domainUrl + "/check/parent/1/"+orgId;
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckParentIdError:"+result);
    }

    @Test
    public void testCheckParentIdOk() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000422").getId();
    	String uri = domainUrl + "/check/parent/"+orgId+"/1";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testCheckParentIdOk:"+result);
    }

    @Test
    public void testIsStartStatusError() throws Exception {
    	String uri = domainUrl + "/judgement/1/1";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testIsStartStatusError:"+result);
    }
    
    @Test
    public void testIsStartStatusOk() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000421").getId();
    	String uri = domainUrl + "/judgement/"+orgId+"/1";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testIsStartStatusOk:"+result);
    }

    @Test
    public void testIsCancelStatusError() throws Exception {
    	String uri = domainUrl + "/judgement/1/0";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testIsCancelStatusError:"+result);
    }
    
    @Test
    public void testIsCancelStatusOk() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000422").getId();
    	String uri = domainUrl + "/judgement/"+orgId+"/0";
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testIsCancelStatusOk:"+result);
    }

    @Test
    public void testCancelStatus() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000422").getId();
    	String uri = domainUrl + "/status";
        OrganForm organForm = new OrganForm();
        Long ids[] = {orgId};
        organForm.setIds(ids);
        organForm.setStatus(0);
        String requestJson = this.toJsonString(organForm);
        String result = mockMvc.perform(put(uri)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn()
        		.getResponse()
        		.getContentAsString();
        System.out.println("testCancelStatus:"+result);

    }

    @Test
    public void testStartStatus() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000421").getId();
    	String uri = domainUrl + "/status";
        OrganForm organForm = new OrganForm();
        Long ids[] = {orgId};
        organForm.setIds(ids);
        organForm.setStatus(1);
        String requestJson = this.toJsonString(organForm);
        String result = mockMvc.perform(put(uri)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn()
        		.getResponse()
        		.getContentAsString();
        System.out.println("testStartStatus:"+result);


    }

    @Test
    public void testSave() throws Exception {
    	String uri = domainUrl;
        OrganForm organ = new OrganForm();
        Organ pOrgan = new Organ();
        pOrgan.setId(1l);
        organ.setOrgCode("80000410");
        organ.setOrgName("测试新增");
        organ.setParentOrgan(pOrgan);
        organ.setStatus(1);
        String requestJson = this.toJsonString(organ);
        String result = mockMvc.perform(post(uri)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn()
        		.getResponse()
        		.getContentAsString();
        System.out.println("testSave:"+result);
    }

    @Test
    public void testUpdate() throws Exception {
    	String uri = domainUrl;
    	Long orgId = organService.findOrganByOrgCode("80000410").getId();
        OrganForm organ = new OrganForm();
        Organ pOrgan = new Organ();
        pOrgan.setId(1l);
        organ.setId(orgId);
        organ.setOrgCode("80000410");
        organ.setOrgName("测试编辑");
        organ.setParentOrgan(pOrgan);
        organ.setOldParentId(1l);
        organ.setStatus(1);
        organ.setOrgSeq("000001");
        String requestJson = this.toJsonString(organ);
        String result = mockMvc.perform(post(uri)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
        		.andReturn()
        		.getResponse()
        		.getContentAsString();
        System.out.println("testSave:"+result);
    }

	public void testDelete() throws Exception {
		
	}

    @Test
	public void testFindOne() throws Exception {
    	Long orgId = organService.findOrganByOrgCode("80000422").getId();
    	String uri = domainUrl + "/"+orgId;
        String result = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orgCode").value(orgId))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("testGetOrganDetail:"+result);
		
	}

    @Test
	public void testPage() throws Exception {
    	String uri = domainUrl + "/page?sort=orgName,desc";
		String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testAsyFindOrgans:" + result);
		
	}

}
