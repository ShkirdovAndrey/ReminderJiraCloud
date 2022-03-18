package com.ui.pages.maintenance;

import com.codeborne.selenide.AuthenticationType;
import com.ui.pages.IssuePage;
import io.qameta.allure.Step;

import javax.naming.AuthenticationNotSupportedException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class MainPage extends PageBase{

    public MainPage(){
        $$x(".//h1").findBy(text("Your work")).shouldBe(visible, Duration.ofSeconds(8));
        openMainPage();
    }

    private MainPage openMainPage(){
        readPropertiesFile();
        open(getProperty("mainUrl"));
        return this;
    }



    @Step("Переход на страницу задачи с плагином")
    public IssuePage getIssue(){
        readPropertiesFile();
        open(getProperty("issueUrl"));
        return new IssuePage();
    }

    private MainPage openApiMainPage(){
        open(
                "https://teamlead-instance2.atlassian.net/jira/your-work",
                AuthenticationType.BASIC,
                "automation.teamlead.bot@gmail.com",
                "iaAsCf5sibHuDoz8cMFQ1294"
        );
        return this;
    }
}
