package com.bigdata.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bigdata.api.model.JwtKeys;

import java.security.*;

@Configuration
public class JwtConfiguration {

    @Bean
    public JwtKeys getKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        return new JwtKeys(publicKey, privateKey);
    }
}
