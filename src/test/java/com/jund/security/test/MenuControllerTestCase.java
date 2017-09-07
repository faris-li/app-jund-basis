package com.jund.security.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.jund.framework.mvc.RestConst;
import com.jund.platformwork.security.model.App;
import com.jund.platformwork.security.model.Menu;
import com.jund.security.service.MenuService;
import com.jund.testcase.ITestCase;

public class MenuControllerTestCase extends SpringBootSuite implements ITestCase{

    @Autowired
    private MenuService menuService;

    @Before
    public void initalize() throws Exception {
    	this.domainUrl = "/api/v1/menu";
    }

    @Test
    public void initData() throws Exception {
    	for(int i = 1; i < 3; i ++){
            App app = new App();
            app.setId(1l);
            Menu menu = new Menu();
            Menu pMenu = new Menu();
            menu.setMenuName("测试"+i);
            menu.setMenuUrl("/api/v1/ceshi");
            menu.setMenuIcon("user.gif");
            menu.setExpandFlag(1);
            menu.setApp(app);
            menu.setMenuCode("Ceshi"+i);
            pMenu.setId(1l);
            String requestContent = this.toJsonString(menu);
            mockMvc.perform(post(domainUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestContent))
                    .andDo(print())
                    .andExpect(status().isOk());
    	}
    }
    
    @Test
    public void testFindMenuIdByRoleId() throws Exception {
    	String uri = domainUrl + "/role/2";
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindMenuIdByRoleId:" + result);
    }

    @Test
    public void testFindAll() throws Exception {
    	String uri = domainUrl;
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindAll:" + result);
    }

    @Test
    public void testFindRootMenu() throws Exception {
    	String uri = domainUrl+"/app";
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindAll:" + result);
    }

    @Test
    public void testFindMenuByUserName() throws Exception {
    	String uri = domainUrl + "/user/sysadmin";
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindMenuByUserName:" + result);
    }
    
    @Test
    public void testFindMenuByAppId() throws Exception {
    	String uri = domainUrl + "/app/16";
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testFindMenuByAppId:" + result);
    }
    
    private Menu findByMenuCode(String menuCode){
    	return menuService.findMenuByMenuCode(menuCode);
    }

	@Test
	public void testFindOne() throws Exception {
		Long menuId = findByMenuCode("Ceshi1").getId();
    	String uri = domainUrl + "/"+menuId;
    	String result = mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.menuCode").value("Ceshi1"))
				.andReturn().getResponse().getContentAsString();
		System.out.println("testDetail:" + result);
		
	}
	
	@Test
	public void testSave() throws Exception {
		App app = new App();
        app.setId(1l);
        Menu menu = new Menu();
        menu.setMenuName("测试");
        menu.setMenuUrl("/api/v1/ceshi");
        menu.setMenuIcon("user.gif");
        menu.setApp(app);
        menu.setExpandFlag(1);
        menu.setMenuCode("Ceshi");
        Menu pMenu = new Menu();
        pMenu.setId(1l);
//        menu.setParentMenu(null);
        String requestContent = this.toJsonString(menu);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK));
	}
	
	@Test
    public void testUpdateMenu() throws Exception {
        Menu menu = findByMenuCode("Ceshi");
        menu.setMenuName("用户");
        menu.setMenuUrl("/api/v1/user1");
        menu.setMenuIcon("user.gif");
        menu.setRemark("ceshi");
        App app = new App();
        app.setId(1l);
        menu.setApp(app);
        String requestContent = this.toJsonString(menu);
        mockMvc.perform(post(domainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(RestConst.ReturnCode.OK));
        
    }

	public void testPage() throws Exception {
		
	}

	public void testDelete() throws Exception {
		
	}
	
}
