package com.app.schedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.apache.log4j.Logger;


public class CPacsDataModule{
	
	public static String SQLRIFNEWORDER = "RIFNewOrder";
	public static String SQLRIFPATIENTUPDATE = "RIFPatientUpdate";
	public static String SQLRIFUPDATEORDER = "RIFUpdateOrder";
	public static String SQLRIFCANCELORDER = "RIFCancelOrder";
	public static String SQLRIFEXAMSTATUSUPDATE = "RIFExamStatusUpdate";
	public static String SQLRIFREPORTAPPROVE = "RIFReportApproved";
	
	public static final int  DEADLOCK = 1205;
	public static final int  UNKNOWNWARNING=6248;
	public static final int  RETRYABLE1 = 701;
	public static final int  RETRYABLE2 = 4964;
	public static final int  RETRYABLE3 = 4834;
	public static final int  RETRYABLE4 = 7794;
	
	private static CPacsDataModule instance = null;

	private static Logger log = Logger.getLogger(CPacsDataModule.class);

	private Hashtable hashSqlClass = new Hashtable();
	
	private Hashtable hashModality = new Hashtable();

	public static CPacsDataModule getInstance(Connection conn) {
		if (instance == null) {
			try{
				instance = new CPacsDataModule(conn);
			} catch(SQLException se){
				log.error(se.getMessage(), se);
				instance = null;
			}
		}
		return instance;
	}

	private CPacsDataModule(Connection conn) throws SQLException{
		try {
			jbInit(conn);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	private void jbInit(Connection conn) throws SQLException {
		Statement sm = null;
//		Statement smModality = null;
		try {
			sm = conn.createStatement();
			ResultSet rs = sm.executeQuery("exec get_sql_class 'RIF_COMMON'");
			
			while (rs.next()) {
				hashSqlClass.put(rs.getString("sql_req_name"), rs
						.getString("sql_req_text"));
			}
			
//			smModality = conn.createStatement();
//			ResultSet rsModality = smModality.executeQuery("select modality_code,procedure_code from exam_procedure");
//			while(rsModality.next()){
//				hashModality.put(rsModality.getString("procedure_code"),rsModality.getString("modality_code"));
//			}
		} catch(SQLException se){
			throw se;
		} finally{
			if(sm != null){
				sm.close();
//				smModality.close();
			}
			if(conn !=null ){
				conn.close();
			}
		}
	}
	
	

	public String getSqlText(String sql_req_name) {
		Object temp = hashSqlClass.get(sql_req_name);
		
		if(temp == null){
			return null;
		}
		
		return temp.toString();
	}
	
	public String getModalityCode(String procedureCode) {
		Object temp = hashModality.get(procedureCode);
		
		if(temp == null){
			return null;
		}
		
		return temp.toString();
	}

}