package com.apps.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class SSLConfig {
    @Autowired
    private Environment env;

    @PostConstruct
    private void configureSSL() throws IOException {
        //set to TLSv1.1 or TLSv1.2
        System.setProperty("https.protocols", "TLSv1.2");

        //if it is not comming from command line load the 'javax.net.ssl.trustStore' and
        //'javax.net.ssl.trustStorePassword' from application.yml
        if(env.getProperty("javax.net.ssl.trustStore") == null){
            System.setProperty("javax.net.ssl.trustStore", new PathMatchingResourcePatternResolver()
                    .getResource(env.getProperty("server.ssl.trust-store")).getURI().getRawSchemeSpecificPart());
            System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
        }
    }
}
