package org.dcm4che3.tool.storescu;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.Priority;
import org.dcm4che3.tool.common.CLIUtils;
import org.dcm4che3.util.AttributesFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.main.UIDHandler;
@Component
public class StoreClient extends StoreSCU {
	

	@Value("${storescu.LocalAE}")
	String localAE;
	@Value("${storescu.RemoteAE}")
	String remoteAE;
	@Value("${storescu.RemoteHost}")
	String remoteHost;
	@Value("${storescu.RemotePort}")
	String remotePort;
	@Value("${storescu.PatientIdPrefix}")
	String patientIdPrefix;
	
	
	public StoreClient() throws IOException {
		super(null);

	}
	

	
	public  void sendFile(String file) throws Exception {
		long t1, t2;
		try {
			Device device = new Device("storescu");
			Connection conn = new Connection();
			device.addConnection(conn);
			ApplicationEntity ae = new ApplicationEntity(localAE);
			device.addApplicationEntity(ae);
			ae.addConnection(conn);
			StoreSCU main = new StoreSCU(ae);
			main.setTmpFilePrefix("storescu-");
			main.setTmpFileSuffix(".tmp");
			main.rq.setCalledAET(remoteAE);
			main.remote.setHostname(remoteHost);
			main.remote.setPort(Integer.parseInt(remotePort));
			main.remote.setTlsProtocols(conn.getTlsProtocols());
			main.remote.setTlsCipherSuites(conn.getTlsCipherSuites());
			Attributes attr = new Attributes();
			attr.setString(Tag.PatientID, VR.LO, patientIdPrefix);
			main.setAttributes(attr);
			main.patientIdPrefix=patientIdPrefix;
			
			main.setPriority(Priority.NORMAL);
			t1 = System.currentTimeMillis();
			List<String> files = new ArrayList<String>();
			files.add(file);
			main.scanFiles(files);
			t2 = System.currentTimeMillis();
			int n = main.filesScanned;
			System.out.println();
			if (n == 0)
				return;
			System.out.println(MessageFormat.format(rb.getString("scanned"), n,
					(t2 - t1) / 1000F, (t2 - t1) / n));
			ExecutorService executorService = Executors
					.newSingleThreadExecutor();
			ScheduledExecutorService scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			device.setExecutor(executorService);
			device.setScheduledExecutor(scheduledExecutorService);
			try {
				main.open();
				main.sendFiles();
			}catch (Exception e) {
				throw e;
			} 
			finally {
				main.close();
				executorService.shutdown();
				scheduledExecutorService.shutdown();
			}
			if (main.filesScanned > 0) {
				float s = (t2 - t1) / 1000F;
				float mb = main.totalSize / 1048576F;
				System.out.println(MessageFormat.format(rb.getString("sent"),
						main.filesSent, mb, s, mb / s));
			}
		} catch (Exception e) {
			System.err.println("storescu: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	

	
	public static void main(String[] args) {
		try {
			StoreClient sc = new StoreClient();
			 String euid = args[0];
			sc.sendFile(euid);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
