package com.leyou.sms.mq;

import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private SmsProperties prop;

    /**
     * 发送短信验证码
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "sms.verify.code.queue", durable = "true"), exchange = @Exchange(name = "ly.sms.exchange", type = ExchangeTypes.TOPIC), key = "sms.verify.code"))
    public void listenSendSms(Map<String, String> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        smsUtil.sendSms(phone, prop.getSignName(), prop.getVerifyCodeTemplate(),
                JsonUtils.toString(msg));
    }
}
