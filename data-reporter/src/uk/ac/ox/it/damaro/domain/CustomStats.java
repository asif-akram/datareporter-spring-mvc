package uk.ac.ox.it.damaro.domain;

import java.util.Date;

public class CustomStats {
	
	private String statsType;
	
	private String inputValue;
	
	private Date from;
	
	private Date to;
	
	private String reportType;
	
	public CustomStats(){}

	public String getStatsType() {
		return statsType;
	}

	public void setStatsType(String statsType) {
		this.statsType = statsType;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}	
}
