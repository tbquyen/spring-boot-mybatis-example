package com.github.tbquyen.datatables.params;

import java.util.Map;

public class DataTablesOrder {
	private int column;
	private String name;
	private String dir;

	/**
	 * @return sql order as: name + " " + dir
	 */
	public String toSQL() {
		return name + " " + dir;
	}

	/**
	 * sql order as: Map<column>.name + " " + dir
	 * @param columns
	 * @return
	 */
	public String toSQL(Map<Integer, String> columns) {
		if(columns.containsKey(column)) {
			return columns.get(column) + " " + dir;
		}

		return "1 asc";
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		return "DataTablesOrder [column=" + column + ", name=" + name + ", dir=" + dir + "]";
	}
}
