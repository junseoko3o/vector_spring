package com.milvus.vector_spring.config;

import com.milvus.vector_spring.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class EncryptionConfig {

    @Value("${secret.key}")
    private String secretKey;

    public SecretKey getSecretKey() throws CustomException {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = new byte[32];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
        return new SecretKeySpec(paddedKey, "AES");
    }
}
