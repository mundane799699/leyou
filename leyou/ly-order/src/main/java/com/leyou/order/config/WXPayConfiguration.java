package com.leyou.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WXPayConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ly.pay")
    public PayConfig payConfig() {
        return new PayConfig();
    }

    @Bean
    public WXPay wxPay(PayConfig payConfig) {
        return new WXPay(payConfig, SignType.HMACSHA256);
    }
}
