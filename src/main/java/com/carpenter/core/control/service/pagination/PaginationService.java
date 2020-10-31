package com.carpenter.core.control.service.pagination;

import javax.faces.event.ActionEvent;
import java.util.List;

public interface PaginationService<T> {

    List<T> items();

    void performPagination();

    void firstPage();

    void nextPage();

    void previousPage();

    void lastPage();

    void page(ActionEvent event);

    void page(int firstRow);

    List<String> getIterationList();
}
