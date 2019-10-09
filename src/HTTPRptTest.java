import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPRptTest {
	
	static String url = "http://192.1.1.116:9090/GDFileServer/Download?username=pacs3iftp&password=pacs3iftpUserView&path=101/5/2019/07/31/10120190731849806&file=20190801101938781printreport.pdf";
	 
	 public static void main(String[] args) {
		 URL u = null;
			try {
				u = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStream is = null;
			FileOutputStream os=null;
			try {
				os = new FileOutputStream(new File("temp.pdf"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			byte[] b=new byte[1024];
			int size=0;
			try {
				is = u.openStream();
				while ((size=is.read(b))!=-1) {
					os.write(b,0,size);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(is!=null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				os.flush();
				if(os!=null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }

}
