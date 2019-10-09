package com.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.app.task.FtpTaskManager;
import com.app.task.MoveTaskManager;
@Component
@Order(value=3)
public class FtpTaskStart implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${startFTP}")
	String startFTP;
	@Autowired
	FtpTaskManager ftpmgr;
	public void run(String... arg0) throws Exception {
		if(startFTP!=null&&startFTP.equalsIgnoreCase("false")){
			log.info("do not need start ftp.");
			return;
		}
		log.info("this is FTP starter.........");
		Thread ftpmgrThread = new Thread(ftpmgr);
		ftpmgrThread.start();
		
	}

}
