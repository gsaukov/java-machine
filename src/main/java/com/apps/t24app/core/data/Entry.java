package com.apps.t24app.core.data;

import java.util.UUID;

public class Entry {

    private UUID id;
    private String name;
    private String email;
    private Integer age;
    private String status;

    private Entry(){}

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final class Builder {
        private String name;
        private String email;
        private Integer age;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withAge(Integer age) {
            this.age = age;
            return this;
        }

        public Entry build() {
            Entry newEntry = new Entry();
            newEntry.setName(name);
            newEntry.setEmail(email);
            newEntry.setAge(age);
            newEntry.setId(UUID.nameUUIDFromBytes(email.getBytes()));
            return newEntry;
        }
    }
}
