package com.sberStart.bank_api.config;

import com.sberStart.bank_api.logger.BankLoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LogConfig {
    @Bean
    public BankLoggingAspect loggingAspect() {
        return new BankLoggingAspect();
    }
}
