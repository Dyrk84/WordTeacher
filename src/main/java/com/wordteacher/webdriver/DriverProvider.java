package com.wordteacher.webdriver;

import com.wordteacher.pageobjects.GoogleConsent;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class DriverProvider {

    @Getter
    private static String browserVersion;
    public WebDriver driver;
    private static final short PAGE_LOAD_TIMEOUT = 60;
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.google.com/";

    private static WebDriver getInstance() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions capabilityChrome = new ChromeOptions();
        capabilityChrome.setAcceptInsecureCerts(true);
        WebDriver chromeDriver = new ChromeDriver(capabilityChrome);
        browserVersion = ((ChromeDriver) chromeDriver).getCapabilities().getVersion();
        return chromeDriver;
    }

    public void setupBrowser() {
        if (driver == null) {
            initializeDriver();
            driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
            driver.get(GOOGLE_TRANSLATE_URL);
            driver.manage().window().maximize();
            GoogleConsent googleConsent = new GoogleConsent(driver);
            googleConsent.clickAcceptConsent();
        }
    }

    private void initializeDriver() {
        try {
            driver = getInstance();
            if (driver == null) {
                System.out.println("driver object is null !!!");
                throw new AssertionError("driver object is null");
            }
        } catch (WebDriverException exception) {
            System.out.println("Unknown error at driver initialization" + exception);
            throw new AssertionError("Exception Caught during initializeDriver");
        }
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }
}