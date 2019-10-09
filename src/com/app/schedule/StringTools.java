package com.app.schedule;

import java.util.Vector;



/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class StringTools {

	final private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(StringTools.class);

	final public static String ISO_8859_1 = "ISO-8859-1";

	final public static String UTF8 = "UTF-8";

	final public static String SPACE = " ";

	final public static String ZERO_LENGTH_STRING = "";

	final public static String NULL_STRING = "NULL";
	
	final public static String ZERO = "0";
	
	final public static String ONE = "1";

	public StringTools() {
	}

	public static String arrayToString(String[] array, String dim) {
		String string = "";
		if (array == null) {
			return null;
		}

		for (int i = 0; i < array.length; i++) {
			string = string + array[i] + dim;
		}

		string = string.substring(0, string.length() - 1);

		return string;
	}

	public static String[] vectorToStrings(Vector Vector) {
		String[] strs = new String[Vector.size()];
		for (int i = 0; i < Vector.size(); i++) {
			strs[i] = Vector.get(i).toString().trim();
		}
		return strs;
	}

	public static int getIndex(Object[] array, Object o) {
		if (array == null) {
			return -1;
		}
		if (o == null) {
			return -1;
		}

		int index = -1;

		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			if (object.equals(o)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static boolean matches(String string, String substring) {
		boolean isMatch = false;
		String[] strings = string.split(";");
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals(substring)) {
				isMatch = true;
				break;
			}
		}
		return isMatch;
	}

	public static String getMixedString(String s1, String s2) {
		String[] str1 = s1.split(";");
		String[] str2 = s2.split(";");
		String matchStr = "";
		for (int i = 0; i < str1.length; i++) {
			for (int j = 0; j < str2.length; j++) {
				if (str1[i].equals(str2[j])) {
					matchStr += str1[i] + ";";
					break;
				}
			}
		}
		if (matchStr.endsWith(";")) {
			matchStr = matchStr.substring(0, matchStr.length() - 1);
		}
		return matchStr;
	}
	
	public static String int2String(int intValue){
		return ZERO_LENGTH_STRING + intValue;
	}

	public static int String2Int(String strValue) {
		int intValue = 0;
		try {
			intValue = Integer.parseInt(strValue);
		} catch (Exception e) {
			log.error(e);
		}
		return intValue;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String nullToEmptyString(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * @param str
	 * @return true if str is null or is empty string
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param unicode
	 * @return
	 */
	public static String convertUnicode8859(String unicode) {

		return convertEncoding(unicode, UTF8, ISO_8859_1);

	}

	/**
	 * @param iso8859
	 * @return
	 */
	public static String convert8859Unicode(String iso8859) {

		return convertEncoding(iso8859, ISO_8859_1, UTF8);
	}

	/**
	 * @param string
	 * @param fromEncoding
	 * @param toEncoding
	 * @return
	 */
	private static String convertEncoding(String string, String fromEncoding,
			String toEncoding) {
		try {
			byte[] bytes = string.getBytes(fromEncoding);
			return (new String(bytes, toEncoding));
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * @param keyWord
	 * @return
	 */
	public static String concatWithSpace(String string) {
		return concatWithMark(string,StringTools.SPACE);
	}


	
	private static String concatWithMark(String string,String mark){
		StringBuffer buffer = new StringBuffer();
		buffer.append(mark);
		buffer.append(string);
		buffer.append(mark);
		return buffer.toString();
	}
	/**
	 * If all string element is empty string, return true; otherwise, return
	 * false
	 * 
	 * @param results
	 * @return
	 */
	public static boolean isEmptyStringArray(String[] results) {
		boolean isEmpty = true;
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				if (!results[i].trim().equals("")) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}
	
	public static String[] intArr2StringArr(int[] arrays){
		String[] stringArrs = new String[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			stringArrs[i] = ""+arrays[i];
		}
		return stringArrs;
		
	}

}
/*******************************************************************************
 * <B>Revision History</B><BR>
 * [type 'revision' and press Ctrl + Shft + Space to insert revision block]<BR>
 * Add the latest revision history at top of revision history.<BR>
 * [Revision on 2006-11-16 11:27:48 by zhaoqy]<BR> * Add int[]2String[] method.
 * 
 * [Revision on 2006-9-19 14:39:10 by xiaomeng.chen]<BR> *  Add int2String method.
 *  
 * [Revision on 2006-9-18 11:23:03 by xiaomeng.chen]<BR> *  Add concatWithMark method.
 * 
 * [Revision on 2006-9-12 16:37:14 by xiaomeng.chen]<BR> * Add method concatWithSingleQuotation.<BR>
 * 
 * [Revision on 2006-9-11 10:08:36 by Zenggh]<BR>
 * Add method isEmptyStringArray().
 * 
 * [Revision on 2006-9-11 10:05:38 by xiaomeng.chen]<BR>
 * Add concatWithSpace,concatWithParentheses and concatWithDot<BR>
 * 
 * [Revision on 2006-9-5 18:18:09 by xiaomeng.chen]<BR>
 * Add convert8859Unicode and convertUnicode8859 methods<BR>
 * 
 * 
 * [Revision on 2006-8-11 10:40:45 by liujl]<BR>
 * Add isEmpty(String)<BR>
 * 
 * [Revision on 2006-7-20 15:34:38 by Andy]<BR>
 * Add method nullToEmptyString(String).
 * 
 * [Revision on 2006-7-4 18:51:04 by xiaomeng.chen]<BR>
 * Add String2Int util method
 * 
 * [Revision on 2006-6-21 11:07:38 by xiaomeng.chen]<BR>
 * Add matches method to search substring in splitted ; string
 * 
 * Copyright 2006 GE Healthcare <BR>
 * All rights reserved.
 ******************************************************************************/
