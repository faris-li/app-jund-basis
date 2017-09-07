package com.jund.security.test.excel;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.jund.framework.core.exception.VpRuntimeException;
import com.jund.framework.mvc.RestConst;
import com.jund.framework.mvc.base.BaseController;
import com.jund.framework.mvc.response.ResponseInfo;
import com.jund.platformwork.security.model.App;
import com.jund.security.service.AppService;
@Controller
@RequestMapping(value = "/excel")
public class ExcelController extends BaseController {

	@Autowired
	private AppService appService;
	
	@GetMapping("/error")
	public String detail() {
		throw new VpRuntimeException(RestConst.ReturnCode.ERROR, "异常");
	}
	
	/**
	 * 导出用户权限 数据
	 */
	@RequestMapping("/exp")
	public void exp(HttpServletResponse response) throws Exception {
		String fileName = "下载.xls";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"UTF-8"));
		FileInputStream ins = new FileInputStream("C:\\Users\\vp\\Downloads\\testUpload.xls");
		OutputStream os = response.getOutputStream();
		byte[] fileData = new byte[1024];
		int readCount = 0;
		while(readCount != -1){
			readCount = ins.read(fileData, 0, 1024);
			os.write(fileData,0,readCount);
		}
		os.flush();
		os.close();
		ins.close();
	}
	
	@RequestMapping("/exp2")
	public void exp2(HttpServletResponse response) throws Exception {
		String fileName = "应用数据.xls";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Type", "application/VND.MS-EXCEL");
		response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"UTF-8"));
		response.setCharacterEncoding("UTF-8");
		List<App> list = appService.findAll();
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), App.class, list);
		
		workbook.write(response.getOutputStream());
	}
	
	/**
	 * 导入用户权限 数据
	 */
	@PostMapping(value = "/imp")
	public ResponseInfo imp(@RequestBody MultipartFile excelFile) throws Exception {
//		ModelMap modelMap = new ModelMap();
//		HSSFWorkbook workbook = new HSSFWorkbook(excelFile.getInputStream());
//		HSSFSheet hssfSheet = workbook.getSheet("用户授权Sheet");
		List<Map<String, Object>> result = impExcelForMap(excelFile);
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> data = result.get(i);
			Set<String> keyset = data.keySet();
			java.util.Iterator<String> iterator = keyset.iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				System.err.println("第" + i + "行数据：" + key + "=" + data.get(key));
				
			}
		}
		return new ResponseInfo();
	}
    
}