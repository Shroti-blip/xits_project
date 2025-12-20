package com.example.ProjectHON.SecurityPackage.MessageEncryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class MessageEncryptor implements AttributeConverter<String, String> {

    @Autowired
    private AESUtil aesUtil;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return aesUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return aesUtil.decrypt(dbData);
    }
}
