package com.apps.cloud.justitia.data.entity;

import com.apps.cloud.common.data.entity.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "AUTHORITY")
public class Authority extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "AUTHORITY")
    private String authority;

    private Authority() {
        // JPA
    }

    private Authority(Builder builder) {
        user = builder.user;
        authority = builder.authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        return Objects.equals(user, authority.user) && Objects.equals(this.authority, authority.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, authority);
    }

    public static final class Builder {

        private User user;
        private String authority;

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withAuthority(String authority) {
            this.authority = authority;
            return this;
        }

        public Authority build() {
            return new Authority(this);
        }

    }

}

