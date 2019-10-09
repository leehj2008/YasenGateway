package com.app.schedule;

import java.io.IOException;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class CamConfiguaration {
	private static CamConfiguaration instance;
	 XPathFactory xpfac = null;
	 Document doc = null;
	public static CamConfiguaration getInstance(String configFile){
		if(instance ==null){
			instance = new CamConfiguaration(configFile);
		}
		return instance;
	}
	private CamConfiguaration(String configFile){
		  xpfac = XPathFactory.instance();
		readConfig(configFile);
	}
	
	public String getConfigValue(String xpath){
		String value="";
		if(doc!=null){
			Element  element = null;
			 XPathExpression xp = xpfac.compile(xpath, Filters.element());
			 element = (Element)xp.evaluateFirst(doc);
			 value= element.getText();
		}
		return value;
	}
	
	private void readConfig(String configFile) {
		SAXBuilder sb=new SAXBuilder();
		  try {
			 doc=sb.build(configFile);//"conf/cam_config.xml");
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
		 
			
	}

public static void main(String[] args) {
	CamConfiguaration.getInstance("conf/cam_config.xml");
}
	
}
