package com.app.task;

public class Task {
	private long ckey;
	private int completed;
	private int remain;
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getRemain() {
		return remain;
	}
	public void setRemain(int remain) {
		this.remain = remain;
	}
	private String accessionNumber;
	
	public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public long getCkey() {
		return ckey;
	}
	public void setCkey(long ckey) {
		this.ckey = ckey;
	}
	private String seriesUID;
	private String studyInstanceUID;
	private String modality;
	private String patientid;
	public String getPatientid() {
		return patientid;
	}
	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}
	private int id;
	private String status;
	private String qrcode;
	private String report_id;
	private String report_path;
	public String getReport_path() {
		return report_path;
	}
	public void setReport_path(String report_path) {
		this.report_path = report_path;
	}
	public String getReport_id() {
		return report_id;
	}
	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getSeriesUID() {
		return seriesUID;
	}
	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
