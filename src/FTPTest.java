import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPTest {
	
	public static void main(String[] args) throws Exception{
//		FileTransferClient ftp = new FileTransferClient();
		FTPClient ftp = new FTPClient();
		ftp.connect("www.qed-cloud.com",21);
		ftp.
		ftp.setAutodetectUTF8(true);
		boolean f =ftp.login("ris", "Password123");
		System.out.println(f);
//		FTPFile[] file=ftp.listFiles("");
//		System.out.println(file);
		ftp.retrieveFile("test.pdf", new FileOutputStream(new File("test.pdf")));
		
		ftp.disconnect();
	}
	
	
}
