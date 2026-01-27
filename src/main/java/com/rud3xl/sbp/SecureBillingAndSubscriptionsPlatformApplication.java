package com.rud3xl.sbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SecureBillingAndSubscriptionsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureBillingAndSubscriptionsPlatformApplication.class, args);
    }

}
