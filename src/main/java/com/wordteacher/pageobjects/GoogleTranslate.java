package com.wordteacher.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleTranslate extends Page {

    @FindBy(xpath = "//textarea")
    private WebElement textBox;

    @FindBy(xpath = "(//span[normalize-space()='volume_up']/ancestor::button)[1]")
    private WebElement listenButton;

    @FindBy(xpath = "(//span[normalize-space()='volume_up']/ancestor::button[@aria-pressed='true'])[1]")
    private WebElement listenButtonPressed;

    public GoogleTranslate(WebDriver driver) {
        super(driver);
    }

    public void enterText(String text) {
        typeText(textBox, text);
    }

    public void clickListen() {
        click(listenButton);
        waitForElementAttributeToBe(listenButton, "aria-pressed", "true");
        waitForElementAttributeToBe(listenButton, "aria-pressed", "false");
    }
}
