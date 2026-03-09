package com.chandana.framework.reporting;

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
    private static String latestReportPath; // stores latest report full path

    // Jenkins-safe: always create report inside workspace/project folder
    private static final String REPORT_DIR = "reports";

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            try {
                // Create workspace/reports folder
                Path dirPath = Paths.get(REPORT_DIR);
                Files.createDirectories(dirPath);

                // Timestamped report file name
                String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                Path reportFilePath = dirPath.resolve("ExtentReport_" + ts + ".html");

                latestReportPath = reportFilePath.toString();

                ExtentSparkReporter spark = new ExtentSparkReporter(latestReportPath);

                extent = new ExtentReports();
                extent.attachReporter(spark);

                // Optional metadata
                // extent.setSystemInfo("Framework", "Selenium + TestNG");

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
            extent = null; // reset so next run creates a new report file
        }
    }

    public static String getLatestReportPath() {
        return latestReportPath;
    }
}