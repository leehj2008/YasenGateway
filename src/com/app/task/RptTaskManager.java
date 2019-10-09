package com.app.task;

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
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.WriteMode;

@Component
public class RptTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(RptTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Autowired
	RptService rptService;
	@Autowired
	FtpService ftpService;
	
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
					String r=rptService.transferReport(task.getReport_id(), reportPath);
					log.info("Dowload Result localFile:{}",r);
					if (StringUtils.isEmpty(r)||!new File(r).exists()) {
						throw new Exception("File not exist file="+r);
					}
					String ur= ftpService.uploadFile(r, task.getReport_id()+".pdf");//reportPath.substring(reportPath.lastIndexOf("/")+1));
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
