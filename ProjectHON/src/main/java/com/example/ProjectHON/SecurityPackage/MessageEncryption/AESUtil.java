package com.example.ProjectHON.SecurityPackage.MessageEncryption;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtil {

    // 32-char complex AES key (256-bit)
    private static final String SECRET_KEY = "bA7$e9@kL1#pQxZt2vM!rSd4FgHjYw3N";

    // 16-char complex IV (128-bit)
    private static final String INIT_VECTOR = "mE4$gR8@qP1!vS6#";

    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivParameterSpec;

    @PostConstruct
    public void init() {
        secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes());
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting", e);
        }
    }
}
