package com.apps.authdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HttpsURLConnection;

@SpringBootApplication
public class AuthDemoApplication {

    public static void main(String[] args) {
        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}
