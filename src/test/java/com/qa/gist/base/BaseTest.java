package com.qa.gist.base;

import com.qa.gist.pages.LoginPage;
import com.qa.gist.utils.ConfigReader;
import com.qa.gist.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected WebDriver driver;
    protected String username;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.initDriver();
        username = ConfigReader.getWithEnv("github.username", "GITHUB_USERNAME");
        String password = ConfigReader.getWithEnv("github.password", "GITHUB_PASSWORD");


        if (!username.isEmpty() && !password.isEmpty()) {
            new LoginPage(driver).open().loginAs(username, password);
            Assert.assertFalse(driver.getCurrentUrl().contains("/login"),
                    "Login gagal / butuh 2FA. Periksa kredensial atau gunakan chrome.user.data.dir");
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
