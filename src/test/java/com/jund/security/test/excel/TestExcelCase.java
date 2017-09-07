package com.jund.security.test.excel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.security.Application;
import com.jund.framework.mvc.RestConst;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TestExcelCase {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/excel/error")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ResponseInfo message = mapper.readValue(content, ResponseInfo.class);
        Assert.assertEquals(RestConst.ReturnCode.ERROR, message.getCode());
    }
    
    @Test
    public void testUpload() throws Exception {
        FileInputStream is = new FileInputStream("C:\\Users\\vp\\Downloads\\testUpload.xls");
        MockMultipartFile file = new MockMultipartFile("excelFile", is);
        mockMvc.perform(fileUpload("/excel/imp").file(file))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
        
    }
    
    @SuppressWarnings("resource")
	@Test
    public void testDownload() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/excel/exp")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        OutputStream fos = new FileOutputStream("C:\\Users\\vp\\Downloads\\writerExcel.xls");
        fos.write(content);
        Assert.assertNotNull(content);
    }
    
	@SuppressWarnings("resource")
    @Test
    public void testDownload2() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/excel/exp2")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        OutputStream fos = new FileOutputStream("C:\\Users\\vp\\Downloads\\appExcel.xls");
        fos.write(content);
        Assert.assertNotNull(content);
    }
}
