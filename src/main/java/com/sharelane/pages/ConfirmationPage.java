package com.sharelane.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage {

    private final WebDriver driver;

    private final By confirmationText = By.xpath("//*[contains(text(), 'Click') and contains(text(), 'here')]");
    private final By noteToTester = By.xpath("//*[contains(text(), 'NOTE TO TESTER')]");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDisplayed() {
        return !driver.findElements(confirmationText).isEmpty()
                || !driver.findElements(noteToTester).isEmpty();
    }
}