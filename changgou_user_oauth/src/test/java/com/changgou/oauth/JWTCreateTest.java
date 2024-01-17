package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class JWTCreateTest {

    @Test
    public void createJWT(){
        // 1.获取私钥
        // 1.1.读取密钥证书文件
        ClassPathResource classPathResource = new ClassPathResource("changgou.jks");
        String keyPass = "changgou";
        // 1.2.创建私钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keyPass.toCharArray());
        // 1.3.获取私钥
        String alias = "changgou";
        String password = "changgou";
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, password.toCharArray());
        // 1.4.将当前私钥转换为RSA私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 2.生成JWT令牌
        // 2.1.创建jwt内容
        Map<String, String> map = new HashMap<>();
        map.put("company", "zhou");
        map.put("address", "shenzhen");
        Jwt encode = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(rsaPrivateKey));
        String jwtToken = encode.getEncoded();
        System.out.println(jwtToken);
    }
}
