package com.qa.gist.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties tidak ditemukan di src/test/resources");
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Gagal membaca config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key, "").trim();
    }


    public static String getWithEnv(String key, String envName) {
        String env = System.getenv(envName);
        return (env != null && !env.isBlank()) ? env : get(key);
    }

    public static int getInt(String key, int def) {
        try { return Integer.parseInt(get(key)); } catch (Exception e) { return def; }
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
