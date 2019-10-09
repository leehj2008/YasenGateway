package com.app.schedule;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl {

	@Value("${storescu.PatientIdPrefix}")
	String patientIdPrefix;
	CamConfiguaration config;
	String startDate;
	String endDate;
	Logger log = Logger.getLogger(this.getClass());
	CPacsDataModule cpacsDM;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");

	public OrderServiceImpl() {
		config = CamConfiguaration.getInstance("config/db_ris.xml");
	}

	public static void main(String[] args) {
		try {
			String r = new OrderServiceImpl().doSendSchedule("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String doSendSchedule(String accessionNumber) throws Exception {
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("startDate", startDate);
		paramap.put("endDate", endDate);
		String getRisExamSql = config.getConfigValue("/config/getRisExamSql");

		Connection pacsconn = PACSConnectionUtil.getConnection("config/db_pacs.xml");

		cpacsDM = CPacsDataModule.getInstance(pacsconn);
		pacsconn = PACSConnectionUtil.getConnection("config/db_pacs.xml");
		Connection risconn = ConnectionUtil.getConnection("config/db_ris.xml");
		Statement stmtpacs = null;
		Statement stmtris = null;
		try {
			stmtris = risconn.createStatement();
			ResultSet rs = stmtris.executeQuery(getRisExamSql+"'"+accessionNumber+"'");
			int riscolumns = rs.getMetaData().getColumnCount();
			while (rs.next()) {// loop exam
				Map ris_exam = new HashMap<String, String>();
				for (int i = 1; i <= riscolumns; i++) {
					String columnName = rs.getMetaData().getColumnLabel(i);
					String columnValue = rs.getString(i);
					ris_exam.put(columnName, columnValue);

				}
				try {
					newOrder(pacsconn, ris_exam);
				} catch (IllegalParametersException e) {
					e.printStackTrace();
					throw e;
				}
			}
			DBUtil.closeResource(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.closeResource(stmtpacs);
			//DBUtil.closeResource(pacsconn);
			DBUtil.closeResource(stmtris);
			DBUtil.closeResource(risconn);
		}
		log.debug("Pacs Order Scheduled Success");
		return "success";
	}

	public boolean newOrder(Connection pacsconn, Map<String, String> exam)
			throws SQLException, IllegalParametersException {

		Object accNo = exam.get("ExamAccessionID");

		if (accNo == null || accNo.equals("")) {
			throw new IllegalParametersException(
					"The Accession Number can not be null.");
		}

		Object suid = exam.get("StudyInstanceUID");

		if (suid == null || suid.equals("")) {
			suid=accNo;
			//throw new IllegalParametersException(
				//	"The StudyInstanceUID can not be null.");
		}

		Object temp = exam.get("PatientID");
		if (temp == null) {
			throw new IllegalParametersException(
					"The PatientID can not be null.");
		}
		String patientID = patientIdPrefix+temp.toString();

		temp = exam.get("ModalityTypeName");
		if (temp == null) {
			throw new IllegalParametersException(
					"The ModalityTypeName  can not be null.");
		}
		String modality = temp.toString();

		temp = exam.get("ProcedureStepCode");
		if (temp == null) {
			throw new IllegalParametersException(
					"The ProcedureStepCode  can not be null.");
		}

		String procode = temp.toString();

		temp = exam.get("ProcedureStepDescriptions");
		if (temp == null) {
			throw new IllegalParametersException("The ProcedureStepDescriptions  can not be null.");
		}
		String prodesc = temp.toString();

		temp = exam.get("PreExamScheduleDate");
		log.debug("PreExamSchedule date =" + temp);
		String schd = "";
		if (temp == null || temp.equals("") || temp.equals("''")) {
			schd = sdf.format(new Date());
		} else {
			schd = temp.toString();
		}
		log.debug("convert schd to :" + schd);
		temp = exam.get("PreExamScheduleTime");
		String scht = "";
		log.debug("PreExamSchedule time =" + temp);
		if (temp == null || temp.equals("") || temp.equals("''")) {
			scht = tdf.format(new Date());
		} else {
			scht = temp.toString();
		}

		try {
			log.debug("schd = " + schd);
			String schdt = schd.substring(0, 4) + schd.substring(5, 7)
					+ schd.substring(8, 10) + scht.substring(0, 2)
					+ scht.substring(3, 5) + "00";
			log.debug("schdt =" + schdt);
			SqlString sqlStr = new SqlString(
					cpacsDM.getSqlText(CPacsDataModule.SQLRIFNEWORDER));

			sqlStr.setColumnValue("U_RIS_PAT_ID",
					BrokerUtil.getStringValue(patientID));
			sqlStr.setColumnValue("RIS_EXAM_ID",
					BrokerUtil.getStringValue(accNo.toString()));
			sqlStr.setColumnValue("PROCEDURE_CODE",
					BrokerUtil.getStringValue(procode));
			sqlStr.setColumnValue("PROCEDURE_DESC",
					BrokerUtil.getStringValue(prodesc));
			sqlStr.setColumnValue("MODALITY_TYPE", modality);
			sqlStr.setColumnValue("SCHEDULED_DTTM", schdt);
			sqlStr.setColumnValue("EXAM_STAT", "S");

			temp = exam.get("PreClinicalSymptom");
			if (temp != null) {
				sqlStr.setColumnValue("CLINICAL_IMPR_TEXT",
						BrokerUtil.getStringValue(temp.toString()));
			}

			temp = exam.get("PreExamClinicDiagnose");
			if (temp != null) {
				sqlStr.setColumnValue("CLINICAL_CMNT_TEXT",
						BrokerUtil.getStringValue(temp.toString()));
			}

			sqlStr.setColumnValue(
					"PAT_NAME",
					BrokerUtil.getStringValue(
							exam.get("PatientNameEnglish").toString()).trim());

			sqlStr.setColumnValue(
					"I_PAT_NAME",
					StringTools.convertUnicode8859(BrokerUtil.getStringValue(
							exam.get("PatientNameChinese").toString()).trim()));

			sqlStr.setColumnValue(
					"P_PAT_NAME",
					BrokerUtil.getStringValue(
							exam.get("PatientNameEnglish").toString()).trim());

			sqlStr.setColumnValue("RIS_PAT_ID",
					"" + BrokerUtil.getStringValue(patientID));

			temp = exam.get("PatientBirthday");
			if (temp != null) {
				sqlStr.setColumnValue("BIRTH_DATE", BrokerUtil.removeChar(
						BrokerUtil.getStringValue(temp.toString()), '-'));
			}

			temp = exam.get("PatientGender");
			if (temp != null) {
				sqlStr.setColumnValue("SEX_CODE",
						BrokerUtil.getStringValue(temp.toString()));
			}

			temp = exam.get("PatientHomeAddress");
			if (temp != null) {
				sqlStr.setColumnValue("ADDRESS_DATA",
						BrokerUtil.getStringValue(temp.toString()));
			}

			temp = exam.get("PatientTel");
			if (temp != null) {
				sqlStr.setColumnValue("HOME_PHONE_NBR",
						BrokerUtil.getStringValue(temp.toString()));
			}

			temp = exam.get("ApplyDoctor");
			if (temp != null) {
				sqlStr.setColumnValue("RQST_PHYN_P_NAME",
						BrokerUtil.getStringValue(temp.toString()));
			}
			sqlStr.setColumnValue("STUDY_INSTANCE_UID",
					BrokerUtil.getStringValue(suid.toString()));
			sqlStr.setColumnValue("RQST_PHYN_NAME", 1 + "");
			sqlStr.setColumnValue("STAFF_ID", 1 + "");
			sqlStr.setColumnValue("DEPT_ID", 1 + "");
			sqlStr.setColumnValue("SITE_QID", "riserver");
			sqlStr.printSql();
			// dataModulePthpd1.getDatabaseIMS().executeStatement(sqlStr.getSql());
			log.debug("begin storeProcedureExcute" + sqlStr.getSql());
			storeProcedureExcute(pacsconn, sqlStr, accNo.toString(), 1);
			log.debug("storeProcedureExcute end");
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			log.error(ex.getMessage(), ex);
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return false;
		}
	}

	private void storeProcedureExcute(Connection conn, SqlString sqlStr,
			String item, int type) throws SQLException {

		Statement statement = null;
		try {
			statement = conn.createStatement();

			statement.execute(sqlStr.getSql());

			conn.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
}
