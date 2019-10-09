package com.app.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.api.yasen.YASENAPI;
import com.app.main.util.FtpService;
import com.app.main.util.TaskStatus;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@EnableAutoConfiguration
public class APIController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	APIDao dao;
	@Autowired
	YASENAPI api;
	@Autowired
	FtpService ftpservice;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/newOrder",method = RequestMethod.POST)
	@ResponseBody
	public String newOrder(HttpServletRequest request,@RequestBody()Map order) throws Exception {
		
		String accno=(String)order.get("accession_number");
		List l = dao.getOrders(accno);
		if(l.size()>0){
			return accno+" already exists.";
		}
		dao.newOrder(order);
		if(StringUtils.isEmpty(accno)){
			return "{\"code\":\"-1\",\"value\":\"ERROR:AccessionNumber is Empty!\"}";
		}
		String json=mapper.writeValueAsString(order);
		log.info("map to json:[{}]",json);
		String qrcode=api.newOrder(json);
		log.info("API returned qrcode={}",qrcode);//DEB41D2AA4EEA3658E105DCDFE8DB8F1
		dao.saveQRCode(accno, qrcode);
		if(qrcode!=null&&qrcode.length()>30){
			return "{\"code\":\"0\",\"value\":\""+qrcode+"\"}";
		}else{
			return "{\"code\":\"-2\",\"value\":\"API Error:"+qrcode+"\"}";
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/updateOrder",method = RequestMethod.POST)
	@ResponseBody
	public String updateOrder(HttpServletRequest request,@RequestBody()Map order) throws Exception {
		String qrcode=(String)order.get("qr_code_value");
		if(StringUtils.isEmpty(qrcode)){
			return "ERROR: qrcode is Empty!";
		}
		dao.updateOrder(order);
		log.info("DB update order,qrcode={}",qrcode);
		String json=mapper.writeValueAsString(order);
		log.info("map to json:[{}]",json);
		String ret=api.updateOrder(json);
		log.info("API returned ret={}",ret);
		return ret;
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/deleteOrder",method = RequestMethod.GET)
	@ResponseBody
	public String deleteOrder(HttpServletRequest request,@RequestParam("qr_code_value") String qrcode ) throws Exception {
		dao.updateExamStatus(qrcode, ExamStatus.ORDER_DELETE,TaskStatus.INIT,"");
		log.info("GTW updateExamStatus qrcode={},exam status={}",qrcode,ExamStatus.ORDER_DELETE);
		String ret=api.delOrder(qrcode);
		log.info("API delete order qrcode={} , ret={}",qrcode,ret);
		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/examed",method = RequestMethod.GET)
	@ResponseBody
	public String examed(HttpServletRequest request,@RequestParam("qr_code_value") String qrcode,
			@RequestParam("accession_number") String accession_number,
			@RequestParam("study_instance_uid") String study_instance_uid ) throws Exception {
		dao.updateExamStatus(qrcode, ExamStatus.EXAMED,TaskStatus.INIT,accession_number);
		log.info("GTW updateExamStatus qrcode={},exam status={},exam accession_number={}",qrcode,ExamStatus.EXAMED,accession_number);
		return "0";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/examedOld",method = RequestMethod.GET)
	@ResponseBody
	public String examedOld(HttpServletRequest request,@RequestParam("qr_code_value") String qrcode ) throws Exception {
		//dao.updateExamStatus(qrcode, ExamStatus.EXAMED,TaskStatus.INIT);
		log.info("GTW updateExamStatus qrcode={},exam status={}",qrcode,ExamStatus.EXAMED);
		return "0";
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/examedCA",method = RequestMethod.GET)
	@ResponseBody
	public String examedCA(HttpServletRequest request,@RequestParam("qr_code_value") String qrcode ) throws Exception {
		dao.updateExamStatus(qrcode, ExamStatus.EXAMED_CA,TaskStatus.INIT,"");
		log.info("GTW updateExamStatus qrcode={},exam status={}",qrcode,ExamStatus.EXAMED_CA);
		String ret=api.recallStudy(qrcode);
		log.info("API recallStudy qrcode={} , ret={}",qrcode,ret);
		
		return "0";
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/releaseReport",method = RequestMethod.GET)
	@ResponseBody
	public String releaseReport(HttpServletRequest request,
			@RequestParam("qr_code_value") String qrcode,
			@RequestParam("file_address") String file_address) throws Exception {
		if(StringUtils.isEmpty(qrcode)){
			return "{\"code\":\"-1\",\"value\":\"ERROR: qrcode is Empty!\"}";
		}
		
		String report_id=api.getReportID(qrcode);
		if(StringUtils.isEmpty(report_id)){
			return "{\"code\":\"-1\",\"value\":\"ERROR: report_id create failed!\"}";
		}
		log.info("API getReportID returned ret={}",report_id);

		dao.newReport(qrcode,file_address,report_id);
		log.info("DAO newReport new report_id={}",report_id);
		if(report_id!=null&&report_id.length()>30){
			return "{\"code\":\"0\",\"value\":\""+report_id+"\"}";
		}else{
			return "{\"code\":\"-2\",\"value\":\"API Error:"+report_id+"\"}";
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/recallReport",method = RequestMethod.GET)
	@ResponseBody
	public String recallReport(HttpServletRequest request,
			@RequestParam("report_id") String report_id) throws Exception {
		if(StringUtils.isEmpty(report_id)){
			return "ERROR: qrcode is Empty!";
		}
		dao.reportCA(report_id);
		log.info("DAO reportCA report_id={}",report_id);
		
		String ret=api.recallReport(report_id);
		log.info("API recallReport ret={}",ret);
		return ret;
	}
	

	@SuppressWarnings("rawtypes")
	//@RequestMapping(value="/api/getQR",method = RequestMethod.GET)
	public ResponseEntity<?> getQR(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("qr_code_value") String qrcode) throws Exception {
		if(StringUtils.isEmpty(qrcode)){
			return null;
		}
		//ResponseEntity ret=api.getQR(qrcode);
		response.setContentType("image/x-png");
		String url="http://www.qed-cloud.com:8080/api/getQR?qr_code_value=24192450A93565188A56C834BE589E0A";
		URL u=new URL(url);
		InputStream is=u.openStream();
		ResponseEntity ret = ResponseEntity.ok(is);
		return  ret;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/api/notifyUpload",method = RequestMethod.GET)
	@ResponseBody
	public String notifyUpload(HttpServletRequest request,
			@RequestParam("qrcode") String qrcode,
			@RequestParam("pay_status") String pay_status) throws Exception {
		if(StringUtils.isEmpty(qrcode)){
			return "ERROR: qrcode is Empty!";
		}
		dao.updatePayStatus(qrcode,pay_status);
		log.info("DAO updatePayStatus qrcode={},paystatus={}",qrcode,pay_status);
		
		return "0";
	}
	
}
