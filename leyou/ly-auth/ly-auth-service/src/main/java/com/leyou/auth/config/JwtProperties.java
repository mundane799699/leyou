package com.leyou.auth.config;

import com.leyou.common.utils.RsaUtils;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private String cookieName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    //对象一旦实例化后，就应该读取公钥和私钥,这个注解是在构造方法实例化执行
    @PostConstruct
    public void init() throws Exception {
        //公钥私钥如果不存在
        File pubPath = new File(pubKeyPath);
        File priPath = new File(priKeyPath);
        if(!pubPath.exists()||!priPath.exists()){
            //生成公钥和私钥
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        //读取公钥和私钥
         this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
         this.privateKey= RsaUtils.getPrivateKey(priKeyPath);
    }

}
