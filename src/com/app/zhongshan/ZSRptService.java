package com.app.zhongshan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.main.util.FTLString;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;

@Component
public class ZSRptService {
	private FileTransferClient ftp = new FileTransferClient();
	Logger log = LoggerFactory.getLogger(this.getClass());
	boolean initiated = false;
	@Value("${rpt.user}")
	private String rpt_user;
	@Value("${rpt.password}")
	private String rpt_password;
	@Value("${rpt.localpath}")
	private String localpath = "report";
	@Value("${rpt.mode}")
	private String mode = "Passive";
	@Value("${rpt.url}")
	private String url;

	@Autowired
	FTLString ftlString;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void initDownloadFtp() {
		try {
			if (mode.equalsIgnoreCase("ACTIVE")) {
				ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.ACTIVE);
			}
			ftp.getAdvancedSettings().setControlEncoding("GBK");
			initiated = true;
		} catch (FTPException e) {
			e.printStackTrace();
		}
	}

	public String transferReport(String reportId, String reportPath) {
		String[] args;
		String localFile = "";
		//192.1.1.116\/img_1/101/2/2019/08/13/10120190813910636/20190813162657printreport.jpg
		try {
			args = parsePath(reportPath);
		} catch (Exception e) {
			e.printStackTrace();
			return localFile;
		}

		
		try {
			localFile = downloadFile(args[0],args[1],args[2]);
		} catch (Exception e) {
			e.printStackTrace();
			return localFile;
		} finally {
			
		}

		return localFile;
	}

	private String[] parsePath(String reportPath) throws Exception {
		//192.1.1.116\/img_1/101/2/2019/08/13/10120190813910636/20190813162657printreport.jpg
		String[] args = new String[3];
		int hostEnd = 0;
		if (reportPath.contains("\\")) {
			hostEnd = reportPath.indexOf("\\");
			args[0] = reportPath.substring(0, hostEnd);
		} else {
			args[0] = "";
		}
		String pathAndFile= reportPath.substring(hostEnd+1);
		int pathEnd = pathAndFile.lastIndexOf("/");
		args[1] = pathAndFile.substring(0,pathEnd+1);
		if(args[1].startsWith("/")){
			args[1] = args[1].substring(1);
		}if(args[1].endsWith("/")){
			args[1] = args[1].substring(0,args[1].length()-1);
		}
		args[2] = pathAndFile.substring(pathEnd+1);
		log.info("host:{},path:{},file:{}",args[0],args[1],args[2]);
		return args;
	}

	public String downloadFile(String host,String path, String fileName) throws FTPException, IOException {

		String absPathLocal = mkdirLocal(localpath+"/"+path);
		String newUrl = url.replaceAll("[@]", "\\$");
		Map data = new HashMap<String,String>();
		data.put("host", host);
		data.put("path", path);
		data.put("file", fileName);
		log.info("newUrl:{},host={},path={} ,file={},data={}",newUrl,host,path,fileName,data.toString());
		String processedUrl = ftlString.compileString(newUrl, data);
		log.info("processedUrl={}",processedUrl);
		URL u = null;
		try {
			u = new URL(processedUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(new File(absPathLocal+"/"+fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		byte[] b = new byte[1024];
		int size = 0;
		try {
			is = u.openStream();
			while ((size = is.read(b)) != -1) {
				os.write(b, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			os.flush();
			if (os != null)
				os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return absPathLocal+"/"+fileName;
	}


	private String mkdirLocal(String localPath) {
		File localFile = new File(localPath);
		if (!localFile.exists() && !localFile.isDirectory()) {
			log.info("{} not exists.create it", localPath);
			boolean r = localFile.mkdirs();
			log.info("is successfully create dir? {}", r);
		} else {
			log.info("{} already exists.", localPath);
		}
		return localFile.getAbsolutePath();
	}

	private boolean deleteFile(String fileName) {
		try {
			String cmd = "del " + fileName;
			System.out.println(cmd);
			Runtime.getRuntime().exec("cmd /c " + cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public FileTransferClient getFtp() {
		return ftp;
	}

	public void setFtp(FileTransferClient ftp) {
		this.ftp = ftp;
	}

	public static void main(String[] args) {
		ZSRptService service = new ZSRptService();
		//service.transferReport("aasdffff", "ftp://ftprazor:razorftp@192.168.1.193:21/report/20171003/16.pdf");
		//service.transferReport("aasdffff", "ftp://ftprazor:razorftp@192.168.1.193/report/20171102/8.pdf");
		String reportPath = "192.1.1.116\\/img_1/101/2/2019/08/13/10120190813910636/20190813162657printreport.jpg";
		try {
			service.parsePath(reportPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
