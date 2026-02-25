package com.chandana.framework.reporting;
import  com.chandana.framework.config.ConfigReader;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static String latestReportPath;   // ✅ store latest report full path

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            try {
                String reportDir = ConfigReader.getReportPath(); // e.g. "target/reports/"
                Path dirPath = Paths.get(reportDir);
                Files.createDirectories(dirPath);

                String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                Path reportFilePath = dirPath.resolve("ExtentReport_" + ts + ".html"); // ✅ safe join
                latestReportPath = reportFilePath.toString();

                ExtentSparkReporter spark = new ExtentSparkReporter(latestReportPath);
                extent = new ExtentReports();
                extent.attachReporter(spark);

            } catch (Exception e) {
                throw new RuntimeException("Failed to init ExtentReports", e);
            }
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String name) {
        return getInstance().createTest(name);
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            extent = null; // ✅ reset so next run creates a new report file
        }
    }

    public static String getLatestReportPath() {
        return latestReportPath;
    }
    
}