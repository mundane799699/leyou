package com.leyou.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsProperties;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtil {
    @Autowired
    private SmsProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "sms:phone";
    private static final long SMS_MIN_INTERVAL_IN_MILLIS = 60000L;
    //初始化ascClient需要的几个参数
    final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

    public SendSmsResponse sendSms(String phoneNumber, String signName, String templateCode,
            String templateParam) {
        String key = KEY_PREFIX + phoneNumber;
        // 读取时间
        String lastTime = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(lastTime)) {
            Long last = Long.valueOf(lastTime);
            if (System.currentTimeMillis() - last < SMS_MIN_INTERVAL_IN_MILLIS) {
                log.info("[短信服务] 发送短信频率过高, 被拦截, 手机号码: {}", phoneNumber);
                return null;
            }
        }
        try {
            // 设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", prop.getAccessKeyId(),
                    prop.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            // 组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            // 使用post提交
            request.setMethod(MethodType.POST);
            // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phoneNumber);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            // 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(templateParam);
            //请求失败这里会抛ClientException异常
            SendSmsResponse resp = acsClient.getAcsResponse(request);
            if (!"OK".equals(resp.getCode())) {
                log.error("[短信服务] 发送短信失败, phoneNumber: {}, 原因: {}", phoneNumber, resp.getMessage());
            }
            log.info("[短信服务], 发送短信验证码, 手机号:{}", phoneNumber);
            // 发送短信成功后, 写入redis, 指定生存时间为1分钟
            //  写入发送该条短信的时间, 因为一分钟之内只能发一条短信
            redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 1, TimeUnit.MINUTES);
            return resp;
        } catch (ClientException e) {
            log.error("[短信服务] 发送短信异常, 手机号码:{}", phoneNumber, e);
        }
        return null;
    }
}
