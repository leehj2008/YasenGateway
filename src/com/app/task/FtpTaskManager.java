package com.app.task;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.main.util.FtpService;
import com.app.main.util.TaskStatus;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.WriteMode;

@Component
public class FtpTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(FtpTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Autowired
	FtpService ftpService;
	@Override
	public void run() {
		ftpService.initFtp();
		while(true){
			List<Task> tasks = taskdao.getFtpTask();
			for(Task task : tasks){
				log.info("FTP task ckey={}, qrcode={}",task.getCkey(),task.getQrcode());
				String localFile="zip/"+task.getPatientid()+task.getModality()+"/"+task.getQrcode()+".zip";
				taskdao.updateTask(TaskStatus.FILE_MOVING, task.getCkey());
				
				try {
					String r=ftpService.uploadFile(localFile, task.getQrcode()+".zip");
					log.info("Upload Result:{}",r);
					taskdao.updateTask(TaskStatus.FILE_MOVE_SUCCESS, task.getCkey());
				} catch (Exception e) {
					e.printStackTrace();
					taskdao.updateTask(TaskStatus.FILE_MOVE_FAILED, task.getCkey());
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
