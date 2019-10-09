package org.dcm4che2.tool.dcmrcv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class StoreSCPService2 {
	
	public static final Logger log = LoggerFactory.getLogger(StoreSCPService2.class);
	@Autowired
	DcmRcv dcmrcv;
	@Value("${storescp.StoreDir}")
	String storeDir;
	@Value("${storescp.StoreFile}")
	String storeFile;
	@Value("${storescp.LocalAE}")
	String localAE;
	@Value("${storescp.LocalPort}")
	String localPort;
	public void startScp() {
		log.info("Starting StoreSCP");
		String args [] = {localAE+":"+localPort,"-dest",storeDir,"-filepath",storeFile};
		dcmrcv.main(args);
	}
}
