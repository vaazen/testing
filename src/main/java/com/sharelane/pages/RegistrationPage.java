package com.sharelane.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegistrationPage {

    private final WebDriver driver;

    private final By firstNameInput = By.name("first_name");
    private final By lastNameInput = By.name("last_name");
    private final By emailInput = By.name("email");
    private final By password1Input = By.name("password1");
    private final By password2Input = By.name("password2");
    private final By registerButton = By.cssSelector("input[type='submit']");
    private final By errorMessage = By.xpath("//*[contains(text(), 'Oops, error on page')]");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public RegistrationPage fillForm(String firstName, String lastName, String email,
                                     String password1, String password2) {
        setField(firstNameInput, firstName);
        setField(lastNameInput, lastName);
        setField(emailInput, email);
        setField(password1Input, password1);
        setField(password2Input, password2);
        return this;
    }

    private void setField(By locator, String value) {
        WebElement el = driver.findElement(locator);
        el.clear();
        if (value != null && !value.isEmpty()) {
            el.sendKeys(value);
        }
    }

    public void clickRegister() {
        driver.findElement(registerButton).click();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public boolean isRegistrationFormDisplayed() {
        return !driver.findElements(firstNameInput).isEmpty();
    }
}