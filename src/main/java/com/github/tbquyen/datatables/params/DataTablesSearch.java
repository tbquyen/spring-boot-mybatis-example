package com.github.tbquyen.datatables.params;

public class DataTablesSearch {
	private String value;
	private boolean regex;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the regex
	 */
	public boolean isRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	@Override
	public String toString() {
		return "DataTablesSearch [value=" + value + ", regex=" + regex + "]";
	}
}
