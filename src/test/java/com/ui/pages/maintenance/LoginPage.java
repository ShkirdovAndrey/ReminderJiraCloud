package com.ui.pages.maintenance;

import com.codeborne.selenide.SelenideElement;
import com.core.entities.User;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class LoginPage {
    @Getter
    private final SelenideElement FORM = $$("h5").find(text("Войдите в свой аккаунт"));
    private final SelenideElement loginField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement submitButton = $("#login-submit");

    public LoginPage() {
        openLoginPage();
        FORM.shouldBe(visible);
    }

    private LoginPage openLoginPage(){
        readPropertiesFile();
        String url = getProperty("loginUrl");
        open(url);
        return this;
    }

    @Step("Заполнить поля данными юзера: {0}")
    private LoginPage fillFieldsAsUser(User user){
        loginField.sendKeys(user.getEmail());
        submitButton.click();
        passwordField.sendKeys(user.getPassword());
        return this;
    }

    @Step("Залогиниться и попасть на главную страницу")
    public MainPage loginAndMoveMainPageAsUser(User user){
        fillFieldsAsUser(user);
        submitButton.click();
        return new MainPage();
    }
}
