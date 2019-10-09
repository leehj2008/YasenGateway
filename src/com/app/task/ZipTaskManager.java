package com.app.task;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.main.util.TaskStatus;
import com.app.main.util.ZipService;
import com.app.main.util.ZipTest;
import com.enterprisedt.net.ftp.FTPException;

@Component
public class ZipTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(ZipTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Value("${storescp.StoreDir}")
	String storeDir;
	public String getStoreDir() {
		return storeDir;
	}
	public void setStoreDir(String storeDir) {
		this.storeDir = storeDir;
	}
	@Autowired
	ZipService zipService;
	@Override
	public void run() {
		while(true){
			List<Task> tasks = taskdao.getZipTask();
			for(Task task : tasks){
				log.info("zip task ckey={}, suid={}",task.getCkey(),task.getPatientid()+task.getModality());
				taskdao.updateTask(TaskStatus.ZIPING, task.getCkey());
				
				String qrcode=task.getQrcode();
				try {
//					String absPath=new File(task.getAccessionNumber()).getAbsolutePath();
					String zipParent="zip/"+task.getPatientid()+task.getModality()+"/";
					File zipFolder = new File(zipParent);
					if(!zipFolder.exists()) {
						zipFolder.mkdirs();
					}
					//zipService.zip(zipParent+qrcode+".zip", storeDir+"/"+task.getAccessionNumber());
					ZipTest.zip(zipParent+qrcode+".zip", storeDir+"/"+task.getPatientid()+task.getModality());
					taskdao.updateTask(TaskStatus.ZIP_SUCCESS, task.getCkey());
				} catch (Exception e) {
					e.printStackTrace();
					taskdao.updateTask(TaskStatus.ZIP_FAILED, task.getCkey());
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
