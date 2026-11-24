package com.qa.gist.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications", "--remote-allow-origins=*");

        if (ConfigReader.getBool("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        String userDataDir = ConfigReader.get("chrome.user.data.dir");
        if (!userDataDir.isEmpty()) {
            options.addArguments("--user-data-dir=" + userDataDir);
            options.addArguments("--profile-directory=" + ConfigReader.get("chrome.profile.dir"));
        }


        driver = new ChromeDriver(options);
        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
