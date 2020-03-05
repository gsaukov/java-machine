package com.apps.cloud.justitia.data.entity;

import com.apps.cloud.common.data.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "USER_RIGHTS")
public class UserRights extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "RIGHT_NAME")
    private String rightName;

    @Column(name = "RIGHT_VALUE")
    private String rightValue;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private UserRights() {
        // JPA
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
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
        UserRights that = (UserRights) o;
        return Objects.equals(rightName, that.rightName) &&
                Objects.equals(rightValue, that.rightValue) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rightName, rightValue, user);
    }
}

