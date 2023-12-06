package com.github.tbquyen.datatables.params;

public class DataTablesColumn {
	private String data;
	private String name;
	private boolean searchable;
	private boolean orderable;
	private DataTablesSearch search;

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
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
	 * @return the searchable
	 */
	public boolean isSearchable() {
		return searchable;
	}

	/**
	 * @param searchable the searchable to set
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * @return the orderable
	 */
	public boolean isOrderable() {
		return orderable;
	}

	/**
	 * @param orderable the orderable to set
	 */
	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
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

	@Override
	public String toString() {
		return "DataTablesColumn [data=" + data + ", name=" + name + ", searchable=" + searchable + ", orderable="
				+ orderable + ", search=" + search + "]";
	}
}
