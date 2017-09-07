package com.jund.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.jund.platformwork.security.model.App;
import com.jund.platformwork.security.model.dto.AppForm;
import com.jund.security.service.AppService;
import com.jund.testcase.ITestCase;

@FixMethodOrder(MethodSorters.DEFAULT)
public class AppControllerTestCase extends SpringBootSuite implements ITestCase{
	
    @Autowired
    private AppService appService;
    
    private App findAppByAppCode(String appCode){
    	return appService.findByAppCode(appCode);
    }
    
    @Before
    public void initalize() {
    	this.domainUrl = "/api/v1/app";
    }
    
    @Test
    public void initData() throws Exception {
    	for(int i = 1; i < 4; i ++){
    		App app = new App();
            app.setAppAddr("http://localhost:8081/api/v1/app");
            app.setAppCode("newapp"+i);
            app.setAppName("newapp"+i);
            app.setStatus((i<2)?0 :1);
            String requestJson = this.toJsonString(app);
            mockMvc.perform(post(domainUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk());
    	}
    }
    
    @Test
    public void testFindAppIdByRoleId() throws Exception {
    	String uri = domainUrl + "/role/1";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindAppIdByRoleId:" + mvcResult);
    }

    @Test
    public void testStatus() throws Exception {
    	Long appId = findAppByAppCode("newapp1").getId();
    	AppForm app = new AppForm();
    	app.setId(new Long[]{appId});
    	app.setStatus(1);
    	String requestJson = this.toJsonString(app);
    	String uri = domainUrl+"/status";
    	String mvcResult = mockMvc.perform(put(uri)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testStatus:" + mvcResult);
    }
    
    @Test
    public void testDelete() throws Exception {
    	Long appId = findAppByAppCode("newapp3").getId();
    	String uri = domainUrl+"/"+appId;
    	String mvcResult;
		mvcResult = mockMvc.perform(delete(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testDelete:" + mvcResult);
    }
    
    @Test
    public void testJudgement() throws Exception {
    	Long appId = findAppByAppCode("newapp2").getId();
    	String uri = domainUrl+"/judgement/"+appId;
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testJudgement:" + mvcResult);
    }
    
    @Test
    public void testSave() throws Exception {
        App app = new App();
        app.setAppAddr("http://localhost:8081/api/v1/app");
        app.setAppCode("newapp");
        app.setAppName("newapp");
        app.setStatus(1);
        String requestJson = this.toJsonString(app);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    public void testUpdate() throws Exception {
        App app = findAppByAppCode("newapp");
        app.setAppAddr("http://localhost:8081/api/v1/app");
        app.setAppCode("newapp");
        app.setAppName("xiugai");
        app.setStatus(1);
        String requestJson = this.toJsonString(app);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
	public void testFindOne() throws Exception {
//		Long appId = findAppByAppCode("newapp1").getId();
    	String uri = domainUrl + "/"+1;
        String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.appCode").value("newapp1"))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindApp:" + mvcResult);
	}

    @Test
	public void testPage() throws Exception {
		String uri = domainUrl + "/page?sort=appCode,desc";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindAll:" + mvcResult);
	}
	 
}