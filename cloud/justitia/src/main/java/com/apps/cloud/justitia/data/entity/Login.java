package com.apps.cloud.justitia.data.entity;

import com.apps.cloud.common.data.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "LOGIN")
public class Login extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

    @ElementCollection
    @CollectionTable(name = "LOGIN_PARAMS", joinColumns = {@JoinColumn(name = "LOGIN_ID")})
    @MapKeyColumn(name = "PARAM_KEY")
    @Column(name = "PARAM_VALUE")
    private Map<String, String> params;

    private Login() {
        // JPA
    }

    private Login(Builder builder) {
        reference = builder.reference;
        clientId = builder.clientId;
        username = builder.username;
        expiresAt = builder.expiresAt;
        params = builder.params;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Login user = (Login) o;

        return Objects.equals(reference, user.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    public static final class Builder {

        private String reference;
        private String clientId;
        private String username;
        private LocalDateTime expiresAt;
        private Map<String, String> params = new HashMap<>();

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder withParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder withParams(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Login build() {
            return new Login(this);
        }

    }

}

