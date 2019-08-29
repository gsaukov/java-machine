package com.apps.cloud.justitia.service.challenge;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class Challenge {

    private Type type;
    private Optional<String> value;

    public Challenge(Type type) {
        this.type = type;
        this.value = empty();
    }

    public Challenge(Type type, String value) {
        this.type = type;
        this.value = of(value);
    }

    public String getKey() {
        return type.getValue();
    }

    public String getValue() {
        return value.orElse("true");
    }

    public enum Type {

        CREDENTIALS("credentials"),
        ONE_TIME_PASSWORD("oneTimePassword");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

}
