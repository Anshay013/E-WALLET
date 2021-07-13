package com.wallet.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
/* we want discard security auto-configuration and add want to add our own configuration, for that we need to exclude
 securityAutoConfiguration class OR instead of this exclusion by @SpringBootApplication annotation we can add
" spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration " in application.properties.  */
public class GWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(GWalletApplication.class, args);
	}

}
