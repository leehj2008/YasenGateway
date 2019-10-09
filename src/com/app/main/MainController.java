package com.app.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.task.Task;
import com.app.task.TaskDao;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@EnableAutoConfiguration
public class MainController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TaskDao taskdao;
	ObjectMapper objmap = new ObjectMapper();
	@Value("${qr_base_url}")
	String qr_base_url;
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request,Model model) {
		return "index";
	}
	
	@RequestMapping(value="/api/getQR")
	public String getAPIQR(HttpServletRequest request,HttpServletResponse response,@RequestParam("qr_code_value") String qr_code_value) {
		return getQR(request,response,qr_code_value);
	}
	
	@RequestMapping(value="/getQR")
	public String getQR(HttpServletRequest request,HttpServletResponse response,@RequestParam("qr_code_value") String qr_code_value) {
		log.info("getQRImage/qrcode="+qr_code_value);
		response.setContentType("image/png");
		String url=qr_base_url+qr_code_value;
//		String url = "http://www.qed-tec.com:8081/bootdemo/api/getQR?qr_code_value=BB9BDE5700B3D4346015C1A607C398D5";
//		String url="https://img0.bdstatic.com/static/searchresult/img/logo-2X_b99594a.png";
		log.info(url);
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		FileOutputStream os=null;
		try {
			os = new FileOutputStream(new File("temp.html"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		byte[] b=new byte[1024];
		int size=0;
		try {
			is = u.openStream();
			while ((size=is.read(b))!=-1) {
				response.getOutputStream().write(b,0,size);
				os.write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			response.getOutputStream().flush();
			if(os!=null)os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("getQRImage END");
		return null;
	}
	
}