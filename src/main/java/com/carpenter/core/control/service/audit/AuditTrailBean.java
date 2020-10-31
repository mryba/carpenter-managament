package com.carpenter.core.control.service.audit;

import com.carpenter.core.control.dto.EmployeeDto;
import com.carpenter.core.control.service.employee.EmployeeService;
import com.carpenter.core.control.service.login.PrincipalBean;
import com.carpenter.core.control.service.pagination.Pagination;
import com.carpenter.core.control.service.pagination.PaginationService;
import com.carpenter.core.entity.AuditTrail;
import com.carpenter.core.entity.employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ViewScoped
@Named("audiTrailBean")
public class AuditTrailBean implements PaginationService<AuditTrail>, Serializable {

    private static final long serialVersionUID = -6518823109555890992L;

    @Inject
    private AuditTrailService auditTrailService;

    @Inject
    private PrincipalBean principalBean;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private AuditTrailFilters filters;

    private Pagination<AuditTrail> pagination = new Pagination<>(10, 10);
    private List<Employee> employees;

    @PostConstruct
    public void init() {
        employees = employeeService.getEmployersList(principalBean);
        refresh(filters);
    }

    public void refresh(@Observes(notifyObserver = Reception.IF_EXISTS) AuditTrailFilters filters) {
        this.filters = filters;
        pagination.getItems().clear();
        performPagination();

        pagination.getItems().addAll(auditTrailService.getAllAuditsByFilter(filters, pagination.getRowsPerPage(), pagination.getCurrentPage()));
    }

    @Override
    public List<AuditTrail> items() {
        return pagination.getItems();
    }

    @Override
    public void performPagination() {
        pagination.defaultPagination(Math.toIntExact(auditTrailService.getAuditTrailsCount(filters)));
    }

    @Override
    public void firstPage() {
        page(0);
    }

    @Override
    public void nextPage() {
        page(pagination.getFirstRow() + pagination.getRowsPerPage());
    }

    @Override
    public void previousPage() {
        page(pagination.getFirstRow() - pagination.getRowsPerPage());
    }

    @Override
    public void lastPage() {
        page(pagination.getTotalRows() - ((pagination.getTotalRows() % pagination.getRowsPerPage() != 0) ? pagination.getTotalRows() % pagination.getRowsPerPage() : pagination.getRowsPerPage()));

    }

    @Override
    public void page(int firstRow) {
        this.pagination.setFirstRow(firstRow);
        init();
    }

    @Override
    public void page(ActionEvent event) {
        page(((Integer) ((UICommand) event.getComponent()).getValue() - 1) * pagination.getRowsPerPage());

    }

    @Override
    public List<String> getIterationList() {
        List<String> results = new ArrayList<>();
        for (int i = 5; i < 35; i += 5) {
            results.add(String.valueOf(i));
        }
        return results;
    }
}
