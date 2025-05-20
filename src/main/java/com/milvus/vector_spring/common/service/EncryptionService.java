package com.milvus.vector_spring.common.service;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.EncryptionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${iv.size}")
    private Integer ivSize;

    private final SecretKey secretKey;

    public EncryptionService(EncryptionConfig encryptionConfig) throws CustomException {
        this.secretKey = encryptionConfig.getSecretKey();
    }

    public String encryptData(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[ivSize];
            new java.security.SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] encryptedWithIv = new byte[ivSize + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, ivSize);
            System.arraycopy(encryptedData, 0, encryptedWithIv, ivSize, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.ENCRYPTION_ERROR);
        }
    }

    public String decryptData(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            byte[] iv = new byte[ivSize];
            System.arraycopy(decodedData, 0, iv, 0, ivSize);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            byte[] encryptedContent = new byte[decodedData.length - ivSize];
            System.arraycopy(decodedData, ivSize, encryptedContent, 0, encryptedContent.length);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedData = cipher.doFinal(encryptedContent);

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.DECRYPTION_ERROR);
        }
    }
}
