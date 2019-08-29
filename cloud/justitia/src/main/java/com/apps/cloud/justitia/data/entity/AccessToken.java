package com.apps.cloud.justitia.data.entity;

import com.apps.cloud.common.data.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ACCESS_TOKEN")
public class AccessToken extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "JTI")
    private String jti;

    @Column(name = "ENCODED")
    private String encoded;

    private AccessToken() {
        // JPA
    }

    private AccessToken(Builder builder) {
        jti = builder.jti;
        encoded = builder.encoded;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AccessToken user = (AccessToken) o;

        return Objects.equals(jti, user.jti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jti);
    }

    public static final class Builder {

        private String jti;
        private String encoded;

        public Builder withJti(String jti) {
            this.jti = jti;
            return this;
        }

        public Builder withEncoded(String encoded) {
            this.encoded = encoded;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(this);
        }

    }

}

