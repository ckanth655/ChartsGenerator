package com.otsi.rti.bean;

public class ElasticData {

	private String[] indices;
	private String[] fields;
	private String[] chartType;

	public String[] getIndices() {
		return indices;
	}

	public void setIndices(String[] indices) {
		this.indices = indices;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getChartType() {
		return chartType;
	}

	public void setChartType(String[] chartType) {
		this.chartType = chartType;
	}

}
