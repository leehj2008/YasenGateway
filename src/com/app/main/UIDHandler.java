package com.app.main;

public class UIDHandler {
	
	public static String getNewUID(String oldUid){
		int firstNumber = Integer.valueOf(oldUid.substring(0,1));
		int newFirstNumber = firstNumber+1;
		String newUID = newFirstNumber+ oldUid.substring(1);
		return newUID;
	}
	public static String getNewUID_PR(String oldUid){
		int firstNumber = Integer.valueOf(oldUid.substring(0,1));
		int newFirstNumber = firstNumber+2;
		String newUID = newFirstNumber+ oldUid.substring(1);
		return newUID;
	}
	public static String getNewUID_PR(String oldUid,String label){
		int firstNumber = Integer.valueOf(oldUid.substring(0,1));
		int newFirstNumber = firstNumber+2;
		String newUID = newFirstNumber+ oldUid.substring(1);
		
		StringBuilder labelstr = new StringBuilder();
		for(char c:label.toCharArray()){
			labelstr.append((int)c);
		}
		String newlable =  labelstr.toString();
		if(labelstr.length()>8){
			newlable = newlable.substring(0,8);
		}else if(labelstr.length()<8){
			double b = Math.pow(10,7-labelstr.length());
			newlable=Math.round(b)+newlable;
		}
		newUID=newUID.substring(0,4)+newlable+newUID.substring(12,newUID.length());
		return newUID;
	}
	public static void main(String[] args) {
		String uid = "1.2.840.1123.121113123124125.131.12414556.118.1";
		String uid1="1.2.528.1.1001.100.4.3833.5881.1.20180119122807741";
		String uid2="1.2.528.1.1001.100.4.3833.5881.1.20180119123405948";
		
		String s = getNewUID_PR(uid1,"AI_60%");
		String s2 = getNewUID_PR(uid2,"AI_60%");
		System.out.println(s );
		System.out.println(s2);
		System.out.println(s2.length());
		System.out.println(uid1.length());
		
	}

}
