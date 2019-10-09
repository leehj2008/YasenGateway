package com.app;

import org.dcm4che2.tool.dcmrcv.StoreSCPService2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Component
@Order(value=1)
public class StoreScpStart implements CommandLineRunner {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	StoreSCPService2 storeScpService ;
	@Value("${startStoreSCP}")
	String startStoreSCP;
	@Override
	public void run(String... arg0) throws Exception {
		if(startStoreSCP!=null&&startStoreSCP.equalsIgnoreCase("false")){
			log.info("do not need start storescp..");
			return;
		}
		log.info("this is scp starter.........");
		storeScpService.startScp();
	}

}
