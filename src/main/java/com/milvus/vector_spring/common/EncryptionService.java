package com.milvus.vector_spring.common;

import com.milvus.vector_spring.config.EncryptionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${iv.size}")
    private Integer ivSize;

    private final SecretKey secretKey;

    public EncryptionService(EncryptionConfig encryptionConfig) throws Exception {
        this.secretKey = encryptionConfig.getSecretKey();
    }

    public String encryptData(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[ivSize];
        new java.security.SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

        byte[] encryptedWithIv = new byte[ivSize + encryptedData.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, ivSize);
        System.arraycopy(encryptedData, 0, encryptedWithIv, ivSize, encryptedData.length);

        return Base64.getEncoder().encodeToString(encryptedWithIv);
    }

    public String decryptData(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[ivSize];
        System.arraycopy(decodedData, 0, iv, 0, ivSize);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedContent = new byte[decodedData.length - ivSize];
        System.arraycopy(decodedData, ivSize, encryptedContent, 0, encryptedContent.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decryptedData = cipher.doFinal(encryptedContent);

        return new String(decryptedData, "UTF-8");
    }
}
