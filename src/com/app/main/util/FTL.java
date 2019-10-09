package com.app.main.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FTL {
	Configuration cfg;

	private String templateDir;
	
	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		try {
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.templateDir = templateDir;
	}

	public FTL(){
		cfg = new Configuration();
		cfg.setClassicCompatible(true);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	public void compileTemplate(Map<String,String> data,String template,String outfile){
		Template tmpl =null;
		try {
			tmpl = cfg.getTemplate(template);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(outfile);
		if(outFile.exists()){
			outFile.delete();
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(outFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		OutputStreamWriter wfile = new OutputStreamWriter(os) ;
		try {
			tmpl.process(data, wfile);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String compileTemplateToString(Map<String,String> data,String template){
		Template tmpl =null;
		String result = "";
		try {
			tmpl = cfg.getTemplate(template);
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		StringWriter stringWriter = new StringWriter();  
		BufferedWriter writer = new BufferedWriter(stringWriter);  
		tmpl.setEncoding("GBK");  
		try {
			tmpl.process(data, writer);
		} catch (TemplateException e) {
			e.printStackTrace();
			result = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage();
		}  
		result = stringWriter.toString();  
		
		try {
			writer.flush();
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
			result = e.getMessage();
		}   
		return result;
	}
	
	public static void main(String[] args) {
		FTL f = new FTL();
		f.setTemplateDir("conf");
		Map<String, String> m = new HashMap<String, String>();
		m.put("institutionid","41");
		m.put("patientid","11289");
		f.compileTemplate(m,"LaunchInit.ftl","LaunchInit");
	}
}
