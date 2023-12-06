/**
 * 
 */
package com.github.tbquyen.datatables;

import org.json.JSONObject;
import org.springframework.util.StringUtils;

/**
 * @author QUYENTB
 *
 */
public class DataTablesResponse extends JSONObject {
	private int draw;
	private long recordsTotal;
	private long recordsFiltered;
	private Object data;
	private String error;

	/**
	 * @return the draw
	 */
	public int getDraw() {
		return draw;
	}

	/**
	 * @param draw the draw to set
	 */
	public void setDraw(int draw) {
		this.draw = draw;
	}

	/**
	 * @return the recordsTotal
	 */
	public long getRecordsTotal() {
		return recordsTotal;
	}

	/**
	 * @param recordsTotal the recordsTotal to set
	 */
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	/**
	 * @return the recordsFiltered
	 */
	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	/**
	 * @param recordsFiltered the recordsFiltered to set
	 */
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		this.put("draw", this.draw);
		this.put("recordsTotal", this.recordsTotal);
		this.put("recordsFiltered", this.recordsFiltered);
		this.put("data", this.data);
		if (!StringUtils.hasLength(this.error)) {
			this.put("error", this.error);
		}

		return super.toString();
	}
}
