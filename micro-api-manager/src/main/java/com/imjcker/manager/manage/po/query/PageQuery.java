package com.imjcker.manager.manage.po.query;

import java.util.List;

public class PageQuery<E> {

    private int count;
    private int pageCount;
    private int pageNum = 1;
    private int startIndex;
    private int endIndex;
    private int pageSize = 10;

    private List<E> elements;

    public void pagination(int count) {
        this.count = count;

        this.pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if (pageNum > pageCount) {
        	pageNum = pageCount;
        }

        if (count == 0) {
        	startIndex = 0;
        	endIndex = 0;
        	return ;
        }

        this.startIndex = pageSize * (pageNum - 1);
        this.endIndex = pageSize * pageNum - 1;
        if (endIndex > count - 1) {
        	endIndex = count - 1;
        }
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
    	pagination(count);
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public List<E> getElements() {
        return elements;
    }
    public void setElements(List<E> elements) {
        this.elements = elements;
    }


    @Override
    public String toString() {
        return "PageQuery{" +
                "count=" + count +
                ", pageCount=" + pageCount +
                ", pageNum=" + pageNum +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", pageSize=" + pageSize +
                ", elements=" + elements +
                '}';
    }
}
