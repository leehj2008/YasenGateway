package org.dcm4che3.tool.storescu;

import java.io.File;
import java.io.IOException;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.tool.xml2dcm.Xml2Dcm;

public class Test {
	public static void main(String[] args) {
		String file ="tmpdcm/20171221/80323908/1.2.840.113619.2.416.134484686572639972145609614328954399419/1.2.840.113619.2.416.79514276122408311270147303177764312996/1.2.840.113619.2.416.3357431576032430635155099606315000323.1.dcm";
		File f =new File(file);
		try {
			DicomInputStream i = new DicomInputStream(f);
			Attributes a = i.readDataset(-1, -1);
			System.out.println(a);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
				
	}
	
	public static void main1(String[] args) {
		/*String[] a = new String[]{"-c", "DCM4CHEE@192.168.1.170:11112","-L",
				"SERIES", "-m", "SeriesInstanceUID=1.2.528.1.1001.100.3.441.437.1.20170905045819542",
				"--dest","KPServer"};
		MoveSCU.main(a);*/
		Xml2Dcm xml = new Xml2Dcm();
		StoreClient sc = null;
		try {
			sc = new StoreClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			xml.mergeXML("tmpdcm/pr1.xml");
			File prtemp = new File("tmpdcm/pr.temp.dcm");
			if(prtemp.exists()){
				prtemp.delete();
			}
			DicomOutputStream out = new DicomOutputStream(prtemp);
			out.writeDataset(xml.fmi, xml.dataset);
			out.finish();
			out.flush();
			sc.sendFile("tmpdcm/pr.temp.dcm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
