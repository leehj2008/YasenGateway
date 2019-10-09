package com.app.zhongshan;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.app.main.util.FtpService;
import com.app.main.util.PdfUtilImg;
import com.app.main.util.RptService;
import com.app.main.util.TaskStatus;
import com.app.task.Task;
import com.app.task.TaskDao;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.WriteMode;

@Component
public class ZSRptTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(ZSRptTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Autowired
	ZSRptService rptService;
	@Autowired
	FtpService ftpService;
	@Autowired
	PdfUtilImg pdfUtilImg;
	@Override
	public void run() {
		ftpService.initFtp();
		while(true){
			List<Task> tasks = taskdao.getRptTask();
			for(Task task : tasks){
				String reportPath=task.getReport_path();
				log.info("RPT task ckey={}, reportid={},reportpath={}",task.getCkey(),task.getReport_id(),reportPath);
				taskdao.updateTask(TaskStatus.REPORTING, task.getCkey());
				
				try {
					String rptFile=rptService.transferReport(task.getReport_id(), reportPath);
					log.info("Dowload Result localFile:{}",rptFile);//jpg file
					
					if (StringUtils.isEmpty(rptFile)||!new File(rptFile).exists()) {
						throw new Exception("File not exist file="+rptFile);
					}
					if(rptFile.endsWith(".jpg")){
						rptFile = pdfUtilImg.imgOfPdf(rptFile);
						log.info("convert jpg to PDF file:{}",rptFile);
					}
					String ur= ftpService.uploadFile(rptFile, task.getReport_id()+".pdf");//reportPath.substring(reportPath.lastIndexOf("/")+1));
					log.info("Upload Result remoteFile:{}",ur);
					taskdao.updateTask(TaskStatus.REPORT_SUCCESS, task.getCkey());
				} catch (Exception e) {
					e.printStackTrace();
					taskdao.updateTask(TaskStatus.REPORT_FAILED, task.getCkey());
				} 
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
