package com.apps.cloud.justitia.data.entity;

import com.apps.cloud.common.data.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "USER_DOMAIN_ACCESS")
public class UserDomainAccess extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "DOMAIN")
    private String domain;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public UserDomainAccess() {
        //JPA
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDomainAccess that = (UserDomainAccess) o;
        return Objects.equals(domain, that.domain) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), domain, user);
    }
}

