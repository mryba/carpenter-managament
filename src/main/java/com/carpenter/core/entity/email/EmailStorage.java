package com.carpenter.core.entity.email;

import com.carpenter.core.entity.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "EMAIL_STORAGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailStorage extends DomainObject {

    private static final long serialVersionUID = 3110912881201732295L;

    @Column(name = "RECIPIENTS")
    private String recipients;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "SENT")
    private Boolean sent;

    @Column(name = "COMMIT_DATE")
    private Date commitDate;

    @Column(name = "STATUS")
    @Enumerated(value = EnumType.STRING)
    private EmailStatus status;

    @Column(name = "ATTEMPTS")
    private Integer attempts;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailStorage)) return false;
        if (!super.equals(o)) return false;
        EmailStorage that = (EmailStorage) o;
        return Objects.equals(recipients, that.recipients) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(content, that.content) &&
                Objects.equals(sent, that.sent) &&
                Objects.equals(commitDate, that.commitDate) &&
                status == that.status &&
                Objects.equals(attempts, that.attempts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), recipients, subject, content, sent, commitDate, status, attempts);
    }
}
