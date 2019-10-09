package com.app.main;

import java.util.ArrayList;
import java.util.List;

public class CADLabel {
	private List<CADInfo> roi = new ArrayList<CADInfo>();
	private String label;
	public List<CADInfo> getRoi() {
		return roi;
	}
	public void setRoi(List<CADInfo> roi) {
		this.roi = roi;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
