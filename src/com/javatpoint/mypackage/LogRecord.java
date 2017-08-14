package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.List;

public class LogRecord implements Comparable<LogRecord>{
	private String modifiedBy;
	private String timestamp;
	private String action;
	private List<String> modifiedCols = new ArrayList<String>();
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getModifiedCols() {
		return modifiedCols;
	}
	public void setModifiedCols(List<String> modifiedCols) {
		this.modifiedCols = modifiedCols;
	}
	@Override
	public int compareTo(LogRecord arg0) {
		return this.timestamp.compareTo(arg0.getTimestamp());
	}
	
	
	
}
