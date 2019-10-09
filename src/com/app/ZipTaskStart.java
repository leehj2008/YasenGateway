package com.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.app.task.ZipTaskManager;
@Component
@Order(value=4)
public class ZipTaskStart implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${startZIP}")
	String startZIP;
	@Autowired
	ZipTaskManager zipmgr;
	public void run(String... arg0) throws Exception {
		if(startZIP!=null&&startZIP.equalsIgnoreCase("false")){
			log.info("do not need start zip.");
			return;
		}
		log.info("this is ZIP starter.........");
		Thread ftpmgrThread = new Thread(zipmgr);
		ftpmgrThread.start();
		
	}

}
