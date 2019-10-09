/******************************************************************************
 * @project:	Broker_Core
 * @package:	com.ge.hcit.dragon.broker.poe
 * @file:		BrokerUtil.java 
 * @author:		fanyh
 * @Copyright GE Healthcare Integrated IT Solution
 * @purpose:	
 * 
 * @version: 	1.0 
 * 
 * Revision History at the end of file.
 * 
 ******************************************************************************/
package com.app.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class BrokerUtil {
	private static Logger log = Logger.getLogger(BrokerUtil.class);
	
	public static String getStringValue(String str) {
	    if(str==null || str.equals("")){
	        return "";
	    }
	    log.debug("str = "+str);
		//return str.substring(1, str.length() - 1);
	    return str;
	}
	
	public static String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			String sDate = df.format(new Date());
			return sDate;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String removeChar(String source, char c){
		String s = "";
		for(int i = 0; i<source.length(); i++){
			char cc = source.charAt(i);
			if(cc != c){
				s = s+source.charAt(i);
			}
		}
		
		return s;
	}
}


/******************************************************************************
 * Revision History 
 * [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * 
 ******************************************************************************/
