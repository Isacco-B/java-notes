package com.isaccobertoli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();

        try (InputStream input = PropertiesUtil.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("File " + fileName + " not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading file " + fileName, e);
        }

        return properties;
    }
}
