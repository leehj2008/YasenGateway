package com.app.api.yasen;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



 
@Component
public class YASENAPI {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${endpoint}")
	String endpoint;
	@Value("${contentType}")
	String contentType;
	
	public String newOrder(String order) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "POST");
		log.info("endpoint="+endpoint);
		map.put("url", endpoint+"api/getQRCode");
		map.put("paramObj", order);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String updateOrder(String order) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "POST");
		map.put("url", endpoint+"api/updateStudy");
		map.put("paramObj", order);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String delOrder(String qrcode) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "GET");
		map.put("url", endpoint+"api/deleteStudy");
		Map<String,String> paramap=new HashMap<String, String>();
		paramap.put("qr_code_value", qrcode);
		map.put("paramObj", paramap);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String recallStudy(String qrcode) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "GET");
		map.put("url", endpoint+"api/recallStudy");
		Map<String,String> paramap=new HashMap<String, String>();
		paramap.put("qr_code_value", qrcode);
		map.put("paramObj", paramap);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String getReportID(String qrcode) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "GET");
		map.put("url", endpoint+"api/getReportID");
		Map<String,String> paramap=new HashMap<String, String>();
		paramap.put("qr_code_value", qrcode);
		paramap.put("qrcode", qrcode);
		map.put("paramObj", paramap);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String recallReport(String report_id) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "GET");
		map.put("url", endpoint+"api/recallReport");
		Map<String,String> paramap=new HashMap<String, String>();
		paramap.put("report_id", report_id);
		map.put("paramObj", paramap);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	public String getQR(String qrcode) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerMap = new HashMap<String, String>();
		map.put("header", headerMap);
		map.put("contentType", contentType);
		map.put("method", "GET");
		map.put("url", endpoint+"api/getQR");
		Map<String,String> paramap=new HashMap<String, String>();
		paramap.put("qr_code_value", qrcode);
		map.put("paramObj", paramap);
		result = HttpClientUtil.transmit(map);
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		YASENAPI api = new YASENAPI();
		
}

}
