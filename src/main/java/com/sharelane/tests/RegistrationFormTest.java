package com.sharelane.tests;

import com.sharelane.pages.ConfirmationPage;
import com.sharelane.pages.RegistrationPage;
import com.sharelane.pages.ZipCodePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationFormTest extends BaseTest {

    private RegistrationPage regPage;

    @BeforeEach
    void goToRegistrationPage() {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode("94118");
        zipPage.clickContinue();
        regPage = new RegistrationPage(driver);
    }

    private String uniqueEmail() {
        return "qa.test." + System.currentTimeMillis() + "@example.com";
    }

    @Test
    @DisplayName("Валидные данные -> успешная регистрация")
    void validRegistration_showsConfirmationPage() {
        regPage.fillForm("Ivan", "Petrov", uniqueEmail(), "pass1234", "pass1234");
        regPage.clickRegister();

        ConfirmationPage confirmationPage = new ConfirmationPage(driver);
        assertTrue(confirmationPage.isDisplayed(), "После валидной регистрации должна открыться страница подтверждения");
    }

    @Test
    @DisplayName("Пустое First Name -> ошибка")
    void emptyFirstName_showsError() {
        regPage.fillForm("", "Petrov", uniqueEmail(), "pass1234", "pass1234");
        regPage.clickRegister();

        assertTrue(regPage.isErrorDisplayed(), "Пустое обязательное поле First Name должно давать ошибку");
    }

    @Test
    @DisplayName("Email без символа @ -> ошибка")
    void emailWithoutAtSymbol_showsError() {
        regPage.fillForm("Ivan", "Petrov", "ivan.petrov.example.com", "pass1234", "pass1234");
        regPage.clickRegister();

        assertTrue(regPage.isErrorDisplayed(), "Email без '@' должен давать ошибку");
    }

    @Test
    @DisplayName("Пароль короче 4 символов -> ошибка")
    void tooShortPassword_showsError() {
        regPage.fillForm("Ivan", "Petrov", uniqueEmail(), "123", "123");
        regPage.clickRegister();

        assertTrue(regPage.isErrorDisplayed(), "Пароль короче 4 символов должен давать ошибку");
    }

    // BUG #5: скрипт не сравнивает password1 и password2 между собой.
    // Тест написан "как должно быть" — и должен упасть, подтверждая баг.
    @Test
    @DisplayName("BUG #5: несовпадающие пароли должны давать ошибку, но регистрация проходит")
    void mismatchedPasswords_shouldShowErrorPerSpec() {
        regPage.fillForm("Ivan", "Petrov", uniqueEmail(), "pass1234", "otherPass9999");
        regPage.clickRegister();

        assertTrue(regPage.isErrorDisplayed(),
                "Password и Confirm Password не совпадают — должна быть ошибка (известный баг сайта)");
    }
}