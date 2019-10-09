package com.app.main.util;

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
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;

@Component
public class RptService {
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
		initDownloadFtp();
		// ftp://user:pass@ip:port/path/file.pdf
		String[] args;
		String localFile = "";
		try {
			args = parsePath(reportPath);
		} catch (Exception e) {
			e.printStackTrace();
			return localFile;
		}

		String user = args[0];
		if (user.equals("")) {
			user = rpt_user;
		}
		String pass = args[1];
		if (pass.equals("")) {
			pass = rpt_password;
		}
		String host = args[2];
		String port = args[3];
		String filepath = args[4];
		log.info("user:{},pass:{},host:{},port:{},filepath:{}", user, pass, host, port, filepath);
		try {
			ftp.setRemoteHost(host);
			ftp.setRemotePort(Integer.parseInt(port));
			ftp.setUserName(user);
			ftp.setPassword(pass);
		} catch (FTPException e) {
			e.printStackTrace();
			return localFile;
		}
		try {
			localFile = downloadFile(filepath, filepath);
		} catch (Exception e) {
			e.printStackTrace();
			return localFile;
		} finally {
			try {
				ftp.disconnect();
			} catch (FTPException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return localFile;
	}

	private String[] parsePath(String reportPath) throws Exception {
		String[] args = new String[5];
		int userPassEnd = -1;
		if (reportPath.contains("@")) {
			int userPassStart = reportPath.indexOf("://") + 3;
			userPassEnd = reportPath.indexOf("@");
			args[0] = reportPath.substring(userPassStart, userPassEnd).split("[:]")[0];
			args[1] = reportPath.substring(userPassStart, userPassEnd).split("[:]")[1];
		} else {
			args[0] = "";
			args[1] = "";
			userPassEnd = reportPath.indexOf("://") + 3;
		}

		String ipPortFile = reportPath.substring(userPassEnd);
		if (ipPortFile.contains(":")) {
			args[2] = ipPortFile.substring(0, ipPortFile.indexOf(":"));
			args[3] = ipPortFile.substring(ipPortFile.indexOf(":") + 1, ipPortFile.indexOf("/"));
		} else {
			args[2] = ipPortFile.substring(0, ipPortFile.indexOf("/"));
			args[3] = "21";

		}
		args[4] = ipPortFile.substring(ipPortFile.indexOf("/"));
		return args;
	}

	public String downloadFile(String fileName, String remoteFile) throws FTPException, IOException {

		ftp.connect();
		String absPathLocal = mkdirLocal(localpath + fileName.substring(0, fileName.lastIndexOf('/')));
		String[] dirs = remoteFile.split("[/]");
		for (int i = 0; i < dirs.length - 1; i++) {
			if (!StringUtils.isEmpty(dirs[i])) {
				ftp.changeDirectory(dirs[i]);
			}
		}
		ftp.downloadFile(absPathLocal, remoteFile.substring(remoteFile.lastIndexOf("/") + 1));

		return localpath + fileName;
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
		RptService service = new RptService();
		service.transferReport("aasdffff", "ftp://ftprazor:razorftp@192.168.1.193:21/report/20171003/16.pdf");
		service.transferReport("aasdffff", "ftp://ftprazor:razorftp@192.168.1.193/report/20171102/8.pdf");

	}
}
