package com.apps.cloud.common.data.util;

import static java.util.UUID.randomUUID;

public class IdUtils {

    public static String uuid() {
        return randomUUID().toString().replace("-", "").toUpperCase();
    }

}
