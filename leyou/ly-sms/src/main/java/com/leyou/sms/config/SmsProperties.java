package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    String accessKeyId;
    String accessKeySecret;
    String signName;
    String verifyCodeTemplate;

}
