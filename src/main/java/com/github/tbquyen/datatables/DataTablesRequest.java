/**
 * 
 */
package com.github.tbquyen.datatables;

import java.util.ArrayList;
import java.util.List;

import com.github.tbquyen.datatables.params.DataTablesColumn;
import com.github.tbquyen.datatables.params.DataTablesOrder;
import com.github.tbquyen.datatables.params.DataTablesSearch;

/**
 * @author QUYENTB
 *
 */
public class DataTablesRequest {
	private int draw;
	private int start;
	private int length;
	private DataTablesSearch search;
	private List<DataTablesOrder> order = new ArrayList<>();
	private List<DataTablesColumn> columns = new ArrayList<>();

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
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the search
	 */
	public DataTablesSearch getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(DataTablesSearch search) {
		this.search = search;
	}

	/**
	 * @return the order
	 */
	public List<DataTablesOrder> getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(List<DataTablesOrder> order) {
		this.order = order;
	}

	/**
	 * @return the columns
	 */
	public List<DataTablesColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<DataTablesColumn> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "DataTablesRequest [draw=" + draw + ", start=" + start + ", length=" + length + ", search=" + search
				+ ", order=" + order + ", columns=" + columns + "]";
	}
}
