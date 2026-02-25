package com.chandana.framework.utils;
import  com.chandana.framework.config.ConfigReader;
 
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
 
public class ScreenshotUtil {
 
    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            String dir = ConfigReader.getScreenshotPath();
            Files.createDirectories(Paths.get(dir));
            String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String path = dir + name + "_" + ts + ".png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(path));
            return path;
        } catch (Exception e) {
            System.out.println("Screenshot error: " + e.getMessage());
            return null;
        }
    }
}
 