package com.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.app.task.StoreScuTaskManager;
@Component
@Order(value=4)
public class StoreScuStart implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${storescu.LocalBaseDir}")
	String localBaseDir;
	@Value("${startStoreSCU}")
	String startStoreSCU;
	@Autowired
	StoreScuTaskManager storeScuTaskManage;
	@Override
	public void run(String... arg0) throws Exception {
		if(startStoreSCU!=null&&startStoreSCU.equalsIgnoreCase("false")){
			log.info("do not need start storescp..");
			return;
		}
		log.info("localBaseDir={}",localBaseDir);
		Thread storescuMgrThread = new Thread(storeScuTaskManage);
		storescuMgrThread.start();
		log.info("this is scu starter.........");
	}

}
