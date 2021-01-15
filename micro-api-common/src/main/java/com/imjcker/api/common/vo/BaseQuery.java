package com.imjcker.api.common.vo;

public class BaseQuery {
	/** 页码 */
	private Integer pageNum = 1;
	/** 每页显示数量 */
	private Integer pageSize =10;
	/** 排序名称 */
	private String sort;
	/** 排序顺序 */
	private String order;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
