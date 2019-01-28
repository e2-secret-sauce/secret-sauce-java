package com.jpmorgan.e2.processors;

public class DataElement {
    private boolean isKey = false;
    private boolean isPI = false;
    private String fieldName;
    private int columnCount = 0;

    public DataElement () {
	this.setKey(false);
	this.setPI(false);
    }

    public boolean isKey() {
	return isKey;
    }

    public void setKey(boolean isKey) {
	this.isKey = isKey;
    }

    public boolean isPI() {
	return isPI;
    }

    public void setPI(boolean isPI) {
	this.isPI = isPI;
    }

    pubic String getFieldName() {
	return fieldName;
    }

    public void setField_name(String field_name) {
	this.fieldName = fieldName;
    }

    public void set Column(int columnCount) {
	this.columnCount = columnCount;
    }

    public int getColumnCount() {
	return columnCount;
    }

    public String toString() {
	return "DataElements:->" + this.getColumnCount() + " FieldName->" + " isKey->" + this.isKey() + " isPI->" + this.isPI;
    }

}
