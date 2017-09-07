package com.jund.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.Organ;
import com.jund.platformwork.security.model.dto.UserForm;
import com.jund.security.service.UserService;
import com.jund.testcase.ITestCase;

public class UserControllerTestCase extends SpringBootSuite implements ITestCase{

	@Autowired
	private UserService userService;
	
    @Before
    public void initalize() {
    	this.domainUrl = "/api/v1/user";
    }

    @Test
    public void testJudgementOK() throws Exception {
    	String uri = domainUrl + "/judgement/1";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testJudgementOK:" + mvcResult);
    }
    
    @Test
    public void testJudgementError() throws Exception {
    	String uri = domainUrl + "/judgement/2";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testJudgementError:" + mvcResult);
    }

    @Test
    public void testCancelUsing() throws Exception {
    	Long userId = userService.findUserByUserName("User").getId();
    	String uri = domainUrl + "/"+userId;
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testCancelUsing:" + mvcResult);
    }

    @Test
    public void testCheckUserNameError() throws Exception {
    	String uri = domainUrl + "/check/sysadmin";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.VALID_ERROR))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testCheckUserNameError:" + mvcResult);
    }

    @Test
    public void testCheckUserNameOk() throws Exception {
    	String uri = domainUrl + "/check/ake";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testCheckUserNameOk:" + mvcResult);
    }

    @Test
    public void testSave() throws Exception {
        UserForm user = new UserForm();
        Organ organ = new Organ();
        user.setUserName("yase122");
        user.setRealName("yase122");
        user.setStatus(1);
        organ.setId(1l);
        user.setOrgan(organ);
//        Long[] roleIds = {1l, 2l};
//        user.setRoleIds(roleIds);
        String requestJson = this.toJsonString(user);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        UserForm user = new UserForm();
        Organ organ = new Organ();
        Long userId = userService.findUserByUserName("yase12").getId();
        user.setId(userId);
        user.setUserName("yase12");
        user.setRealName("yase10");
        user.setStatus(1);
        organ.setId(1l);
        user.setOrgan(organ);
//		Long [] roleIds = {1l,4l,5l};
//		user.setRoleIds(roleIds);
        String requestJson = this.toJsonString(user);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

	public void testDelete() throws Exception {
		
	}

	@Test
	public void testFindOne() throws Exception {
		Long userId = userService.findUserByUserName("UserCeshi").getId();
    	String uri = domainUrl + "/"+userId;
        String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.data.appCode").value("UserCeshi"))
        		.andReturn().getResponse().getContentAsString();
        System.out.println("testFindOne:" + mvcResult);
		
	}

	@Test
	public void testPage() throws Exception {
		String uri = domainUrl + "/page";
    	String mvcResult = mockMvc.perform(get(uri)
        		.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindAll:" + mvcResult);
	}
	
	@Test
	public void testExp() throws Exception {
		String uri = domainUrl + "/exp";
		byte[] mvcResult = mockMvc.perform(get(uri)
    			.contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsByteArray();
		File file = new File("D:\\vp\\user\\downUser.xls");
        OutputStream fos = new FileOutputStream(file);
        fos.write(mvcResult);
        Assert.assertNotNull(mvcResult);
	}
	
	 @Test
    public void testImp() throws Exception {
        FileInputStream is = new FileInputStream("D:\\vp\\user\\downUser.xls");
        MockMultipartFile file = new MockMultipartFile("excelFile", is);
        String mvcResult = mockMvc.perform(fileUpload(domainUrl + "/imp").file(file))
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testUpload:" + mvcResult);
        
    }
}
