package com.app.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
public class FtpService  {
	private FileTransferClient ftp = new FileTransferClient();
	Logger log = LoggerFactory.getLogger(this.getClass());
	boolean initiated=false;
	@Value("${ftp.host}")
	private String host;
	@Value("${ftp.port}")
	private String port;
	@Value("${ftp.user}")
	private String user;
	@Value("${ftp.password}")
	private String password;
	@Value("${ftp.remotepath}")
	private String remotepath;
	@Value("${ftp.localpath}")
	private String localpath;
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Value("${ftp.mode}")
	private String mode;
	
	
	
	public void initFtp(){
		if (initiated) return;
		try {
			if(mode.equalsIgnoreCase("ACTIVE")) {
				ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.ACTIVE);
			}
			ftp.getAdvancedSettings().setControlEncoding("GBK");
			ftp.setRemoteHost(host);
			ftp.setUserName(user);
			ftp.setPassword(password);
			ftp.setRemotePort(Integer.parseInt(port));
			initiated=true;
		} catch (FTPException e) {
			e.printStackTrace();
		}
	}

	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}




	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRemotepath() {
		return remotepath;
	}

	public void setRemotepath(String remotepath) {
		this.remotepath = remotepath;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String sendFile(String fileName,String pathMsg) {
		String pathResult = sendByFtp(fileName,pathMsg);
		if(pathResult==null||"".equals(pathResult)){
			return null;
		}
		if(!deleteFile(fileName)){
			return null;
		}
		return pathResult;
	}
	
	private void expandFolders(FTPFile[] folders) throws FTPException, IOException, ParseException{
		boolean isconn = ftp.isConnected();
		log.info("connect:{}",isconn);
		for(FTPFile file : folders){
			if(file.isDir()){
				ftp.changeDirectory(file.getPath()+"/"+file.getName());
				expandFolders(ftp.directoryList());
			}else{
				downloadFile(file.getPath()+"/"+file.getName(),file.getName());
			}
		}
	}
	
	public String downloadFolder(String folderName) throws FTPException,IOException,ParseException{
		try{
			ftp.connect();
			ftp.changeDirectory(remotepath);
			ftp.changeDirectory(folderName);
			FTPFile[] folders = ftp.directoryList();
			expandFolders(folders);
		}catch (FTPException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(ftp!=null&&ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (FTPException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return "";
	}
	
	public String downloadFile(String fileName,String remoteFile) {
		
		try {
			//ftp.connect();
			String absPathLocal = mkdirLocal(localpath+fileName.substring(0, fileName.lastIndexOf('/')));
			ftp.downloadFile(absPathLocal, remoteFile);
			//log.info("{}: {}",localPath,dirExists);
		}catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}


	private boolean deleteFile(String fileName){
		try {
			String cmd = "del "+fileName;
			System.out.println(cmd);
			Runtime.getRuntime().exec("cmd /c "+cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public String uploadFile(String localFile,String fileName) {
		String realFullPath = "";
		
		try {
			ftp.connect();
			realFullPath=ftp.uploadFile(localFile, fileName,WriteMode.OVERWRITE);
			ftp.disconnect();
		} catch (FTPException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			if(ftp!=null&&ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (FTPException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return realFullPath;
	}
	private String sendByFtp(String fileName,String pathMsg) {
		String realFullPath = "";
		
		try {
			ftp.connect();
			String jpgFileName = mkdir(pathMsg);
			String remoteFileName = new File(fileName).getName();
			ftp.uploadFile(fileName, jpgFileName);
			ftp.disconnect();
			
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(ftp!=null&&ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (FTPException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return realFullPath;
	}
	
	/**
	 * 
	 * @param pathStr
	 * @return JPG File Name
	 */
	private String mkdir(String pathStr){
		String s[] = pathStr.split("[/]");
		for (int i = 0; i < s.length-1; i++) {
//			pathSB.append(s[i]);
			try{
				ftp.changeDirectory(s[i]);
				
			}catch(Exception e){
				try {
					ftp.createDirectory(s[i]);
					ftp.changeDirectory(s[i]);
				} catch (FTPException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return s[s.length-1];
	}
	
	private String mkdirLocal(String localPath){
		File localFile = new File(localPath);
		if  (!localFile.exists() && !localFile.isDirectory())      
		{       
		    log.info("{} not exists.create it",localPath);  
		    boolean r=localFile.mkdirs();
		    log.info("is successfully create dir? {}",r);
		} else   
		{  
			 log.info("{} already exists.",localPath);  
		}  
		return localFile.getAbsolutePath();
	}
	
	public FileTransferClient getFtp() {
		return ftp;
	}
	public void setFtp(FileTransferClient ftp) {
		this.ftp = ftp;
	}
	
	public static void main(String[] args) {
		FtpService sender = new FtpService( );
		FileTransferClient ftp = sender.getFtp();
		
		try {
			
			ftp.connect();
			ftp.uploadFile("doc/云胶片集成方案.docx", "云胶片集成方案.docx", WriteMode.OVERWRITE);
			ftp.disconnect();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
