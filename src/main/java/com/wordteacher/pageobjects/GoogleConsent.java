package com.wordteacher.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleConsent extends Page {

    @FindBy(xpath = "//span[normalize-space()='Elfogadom']/ancestor::button")
    private WebElement acceptButton;

    public GoogleConsent(WebDriver driver) {
        super(driver);
    }

    public void clickAcceptConsent() {
        waitUntilElementVisible(acceptButton).click();
    }
}
