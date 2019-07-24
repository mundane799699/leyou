package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import java.io.InputStream;
import lombok.Data;

@Data
public class PayConfig implements WXPayConfig {
    private String appID; // 公众账号 ID

    private String mchID; // 商户号

    private String key; // 生成签名的密钥

    private int httpConnectTimeoutMs; // 连接超时时间

    private int httpReadTimeoutMs;// 读取超时时间

    private String tradeType; // 交易类型
    private String spbillCreateIp;// 本地 ip
    private String notifyUrl;// 回调地址

    @Override
    public InputStream getCertStream() {
        return null;
    }
}
