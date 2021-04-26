package com.wordteacher.pageobjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page {

    WebDriver driver;

    Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitUntilElementVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementAttributeToBe(WebElement element, String attribute, String value) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public WebElement typeText(WebElement element, String text) {
        WebElement field = waitUntilElementVisible(element);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(field));
        field.sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
        field.sendKeys(text);
        return field;
    }

    public WebElement click(WebElement element) {
        WebElement elementToClick = waitUntilElementVisible(element);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(elementToClick)).click();
        return  elementToClick;
    }
}
