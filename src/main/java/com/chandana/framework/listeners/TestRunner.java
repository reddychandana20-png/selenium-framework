package com.chandana.framework.listeners;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.testng.TestNG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chandana.framework.reporting.ExtentManager;


public class TestRunner {

    public static void main(String[] args) {
        int port = 8080;
        boolean serverStarted = false;

        for (int i = 0; i < 5; i++) {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);

                // ✅ Register pages
                server.createContext("/", new TestHandler());
                server.createContext("/extent", new ExtentReportHandler()); // ✅ IMPORTANT

                server.setExecutor(null);
                server.start();

                System.out.println("✓ Server started successfully!");
                System.out.println("✓ Access URL: http://localhost:" + port);
                System.out.println("✓ Open your browser and visit: http://localhost:" + port);

                serverStarted = true;
                System.out.println("✓ Server is running. Press Ctrl+C to stop.");
                Thread.currentThread().join();
                break;

            } catch (java.net.BindException e) {
                System.out.println("⚠ Port " + port + " is in use, trying port " + (port + 1) + "...");
                port++;
            } catch (Exception e) {
                System.err.println("✗ Error starting server: " + e.getMessage());
                e.printStackTrace();
                port++;
            }
        }

        if (!serverStarted) {
            System.err.println("✗ Failed to start server on any port between 8080-8084");
        }
    }

    static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("GET".equalsIgnoreCase(method)) {
                writeHtml(exchange, 200, getHtmlPage());
                return;
            }

            if ("POST".equalsIgnoreCase(method)) {
                String formData = readBody(exchange);
                System.out.println("Form Data Received: " + formData);

                Map<String, String> params = parseFormData(formData);

                String browser = params.getOrDefault("browser", "chrome");
                String headless = params.getOrDefault("headless", "false");

                System.setProperty("browser", browser);
                System.setProperty("headless", headless);

                System.out.println("Execution Config -> Browser: " + browser + ", Headless: " + headless);

                List<String> selectedTests = new ArrayList<>();

                if ("on".equalsIgnoreCase(params.get("loginTest"))) {
                    selectedTests.add("com.chandana.tests.LoginTest");
                }
                if ("on".equalsIgnoreCase(params.get("ixlTest"))) {
                    selectedTests.add("com.chandana.tests.IXLTest");
                }

                String testList = selectedTests.isEmpty() ? "No tests selected" : String.join(", ", selectedTests);

                String response = "<html><head><title>Test Results</title></head><body>"
                        + "<h1>Tests Requested</h1>"
                        + "<p><b>Browser:</b> " + escape(browser) + "</p>"
                        + "<p><b>Headless:</b> " + escape(headless) + "</p>"
                        + "<p><b>Selected Tests:</b> " + escape(testList) + "</p>"
                        + "<p><a href='/extent' target='_blank'>Open Extent Report</a> (refresh if not ready)</p>"
                        + "<a href='/'>Run Another Test</a>"
                        + "</body></html>";

                writeHtml(exchange, 200, response);

                if (!selectedTests.isEmpty()) {
                    new Thread(() -> runTests(selectedTests)).start();
                } else {
                    System.out.println("No tests selected!");
                }
                return;
            }

            writeHtml(exchange, 405, "<html><body><h1>405 - Method Not Allowed</h1></body></html>");
        }

        private String getHtmlPage() {
            return "<!DOCTYPE html>"
                    + "<html><head><title>Test Runner</title>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; margin: 40px; background: #f9f9f9; }"
                    + "h1 { color: #333; }"
                    + "form { background: white; padding: 20px; border-radius: 6px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }"
                    + "label { display: block; margin: 10px 0; font-size: 16px; }"
                    + "select { margin: 6px 0 12px 0; padding: 6px; font-size: 14px; }"
                    + "input[type='checkbox'] { margin-right: 10px; cursor: pointer; }"
                    + "input[type='submit'] { margin-top: 16px; background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }"
                    + "input[type='submit']:hover { background: #218838; }"
                    + ".info { color: #666; font-size: 14px; margin: 6px 0 14px 28px; }"
                    + "</style>"
                    + "</head><body>"
                    + "<h1>Selenium Test Runner</h1>"
                    + "<form method='POST' action='/'>"
                    + "<h2>Available Tests:</h2>"
                    + "<label><input type='checkbox' name='loginTest' value='on' /> Login Test</label>"
                    + "<div class='info'>- Tests the login functionality</div>"
                    + "<label><input type='checkbox' name='ixlTest' value='on' /> IXL Test</label>"
                    + "<div class='info'>- Runs the IXL scenario validations</div>"
                    + "<h2>Execution Settings:</h2>"
                    + "<label>Browser:</label>"
                    + "<select name='browser' required>"
                    + "  <option value='chrome' selected>Chrome</option>"
                    + "  <option value='edge'>Edge</option>"
                    + "  <option value='firefox'>Firefox</option>"
                    + "</select>"
                    + "<label>Execution Mode:</label>"
                    + "<select name='headless' required>"
                    + "  <option value='false' selected>Headed (Show Browser)</option>"
                    + "  <option value='true'>Headless</option>"
                    + "</select>"
                    + "<input type='submit' value='Run Selected Tests' />"
                    + "<p class='info'>Tests will run in the background. Then open Extent Report link.</p>"
                    + "</form>"
                    + "</body></html>";
        }

        private String readBody(HttpExchange exchange) throws IOException {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        }

        private Map<String, String> parseFormData(String formData) {
            Map<String, String> params = new HashMap<>();
            if (formData == null || formData.isEmpty()) return params;

            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = keyValue.length > 1
                        ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                        : "";
                params.put(key, value);
            }
            return params;
        }

        private void writeHtml(HttpExchange exchange, int status, String html) throws IOException {
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String escape(String s) {
            if (s == null) return "";
            return s.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
        }
    }

    private static void runTests(List<String> testClasses) {
        try {
            TestNG testng = new TestNG();

            Class<?>[] classes = testClasses.stream()
                    .map(className -> {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(c -> c != null)
                    .toArray(Class[]::new);

            testng.setTestClasses(classes);

            // ✅ Attach your Extent listener
            testng.addListener(new ExtentTestNGListener());

            testng.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ExtentReportHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = ExtentManager.getLatestReportPath();

            if (path == null) {
                write(exchange, 200, "<html><body><h2>No Extent report yet.</h2><a href='/'>Back</a></body></html>");
                return;
            }

            Path reportPath = Paths.get(path);

            if (!Files.exists(reportPath)) {
                write(exchange, 200,
                        "<html><body><h2>Report not ready yet.</h2>"
                                + "<p>Wait for test to finish, then refresh.</p>"
                                + "<a href='/extent'>Refresh</a> | <a href='/'>Back</a>"
                                + "</body></html>");
                return;
            }

            byte[] bytes = Files.readAllBytes(reportPath);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void write(HttpExchange exchange, int status, String html) throws IOException {
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}