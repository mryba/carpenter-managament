package com.carpenter.core.control.service.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Pagination<T> {

    private final List<T> items = new LinkedList<>();

    private int totalRows;
    private int firstRow;
    private int rowsPerPage;
    private int totalPages;
    private int pageRange;
    private Integer[] pages;
    private int currentPage;
    private boolean backToFirstPage = false;


    public Pagination(int rowsPerPage, int pageRange) {
        this.rowsPerPage = rowsPerPage;
        this.pageRange = pageRange;
    }

    public void defaultPagination(int totalRows) {
        setTotalRows(totalRows);
        setCurrentPage((this.totalRows / this.rowsPerPage) - ((this.totalRows - firstRow) / rowsPerPage) + 1);

        setTotalPages((this.totalRows / this.rowsPerPage) + ((this.totalRows % this.rowsPerPage != 0) ? 1 : 0));

        int pagesLength = Math.min(this.rowsPerPage, this.totalPages);

        setPages(new Integer[pagesLength]);

        int firstPage = Math.min(Math.max(0, this.currentPage - (this.pageRange / 2)), this.totalPages - pagesLength);
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
    }
}