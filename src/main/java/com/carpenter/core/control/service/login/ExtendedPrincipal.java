package com.carpenter.core.control.service.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
public class ExtendedPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 5702083108506963239L;

    private String firstName;
    private String lastName;
    private String email;

    public ExtendedPrincipal() {
    }

    public ExtendedPrincipal(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean implies(Subject subject) {
        Set<? extends ExtendedPrincipal> principals = subject.getPrincipals(getClass());
        return principals.contains(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedPrincipal)) {
            return false;
        }
        ExtendedPrincipal that = (ExtendedPrincipal) o;
        return Objects.equals(getFirstName(), that.getFirstName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName());
    }
}
