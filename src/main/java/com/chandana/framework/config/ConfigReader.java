package com.chandana.framework.config;
 
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
 
public class ConfigReader {
 
    private static Properties properties = new Properties();
 
 
    static {
        String path = System.getProperty("user.dir") + "/src/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Could not load config.properties from " + path + " -> " + e.getMessage());
        }
    }
 
    public static String get(String key, String defaultValue) {
        String val = System.getProperty(key);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return properties.getProperty(key, defaultValue);
    }
 
    public static String getBaseUrl() {
        return get("baseUrl", "");
    }
 
    public static String getBrowser() {
        return get("browser", "chrome");
    }
 
    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }
 
    public static int getImplicitWait() {
        try {
            return Integer.parseInt(get("implicitWait", "5"));
        } catch (NumberFormatException e) {
            return 5;
        }
    }
 
    public static String getScreenshotPath() {
        return get("screenshotPath", "./screenshots/");
    }
 
    public static String getReportPath() {
        return get("reportPath", "./reports/");
    }
 
    public static int getExplicitWait() {
        try {
            return Integer.parseInt(get("explicitWait", "10"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }
 
    public static String getUsername() {
        return get("username", "");
    }

    public static String getPassword() {
        return get("password", "");
    }

    public static String getApiKey() {
        return get("api_key", "");
    }
}