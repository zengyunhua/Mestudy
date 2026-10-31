package com.tecsun.sisp.npm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPERTIES = loadProperties();

    private AppConfig() {
    }

    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = PROPERTIES.getProperty(key);
        }
        if (isBlank(value)) {
            value = System.getenv(toEnvironmentKey(key));
        }
        return isBlank(value) ? defaultValue : value.trim();
    }

    public static String required(String key) {
        String value = get(key, null);
        if (isBlank(value)) {
            throw new IllegalStateException("Missing required configuration: " + key);
        }
        return value;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        String externalPath = System.getProperty("npm.config.file");
        InputStream input = null;
        try {
            if (!isBlank(externalPath)) {
                input = new FileInputStream(new File(externalPath));
            } else {
                input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties");
            }
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load NetPortalManagement configuration", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                    // Best effort close for Java 8 compatibility.
                }
            }
        }
        return properties;
    }

    private static String toEnvironmentKey(String key) {
        return "NPM_" + key.replace('.', '_').replace('-', '_').toUpperCase();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
