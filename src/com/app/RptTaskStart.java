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
import com.app.task.RptTaskManager;
import com.app.zhongshan.ZSRptTaskManager;
@Component
@Order(value=5)
public class RptTaskStart implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${startRPT}")
	String startRPT;
	@Autowired
	ZSRptTaskManager rtpmgr;
	public void run(String... arg0) throws Exception {
		if(startRPT!=null&&startRPT.equalsIgnoreCase("false")){
			log.info("do not need start RPT.");
			return;
		}
		log.info("this is RPT starter.........");
		Thread ftpmgrThread = new Thread(rtpmgr);
		ftpmgrThread.start();
		
	}

}
