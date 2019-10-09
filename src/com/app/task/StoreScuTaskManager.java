package com.app.task;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.dcm4che3.tool.storescu.StoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.main.util.FtpService;
import com.app.main.util.TaskStatus;
import com.enterprisedt.net.ftp.FTPException;

@Component
public class StoreScuTaskManager implements Runnable{
	Logger log = LoggerFactory.getLogger(StoreScuTaskManager.class);
	@Autowired
	TaskDao taskdao;
	@Autowired
	StoreClient storeClient;
	@Value("${storescu.LocalBaseDir}")
	String localBaseDir;
	
	@Override
	public void run() {
		
		while(true){
			log.debug("fetching tasks....");
			List<Task> tasks = taskdao.getTask(TaskStatus.MOVE_SUCCESS);
			log.debug("task count.{}",tasks.size());
			for(Task task : tasks){
				log.info("task ckey={}, suid={}",task.getCkey(),task.getStudyInstanceUID());
				taskdao.updateTask(TaskStatus.DCM_SENDING, task.getCkey());
				try {
					String filePath = localBaseDir+"/"+task.getStudyInstanceUID();
					log.info("sending study. studyinstanceUID={},ckey={},filepath={}",task.getStudyInstanceUID(),task.getCkey(),filePath);
					storeClient.sendFile(filePath);
					taskdao.updateTask(TaskStatus.DCM_SENT_SUCCESS, task.getCkey());
				} catch (Exception e) {
					e.printStackTrace();
					taskdao.updateTask(TaskStatus.DCM_SENT_FAILED, task.getCkey());
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
