package com.ui.pages.maintenance;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class MyRemindersPage {
    SelenideElement myRemindersIFrame = $x(".//iframe[contains(@id, 'ru.teamlead.jira.plugins.reminder-for-jira.test__reminder-my-page')]");
    private static String locatorContent;

    private final SelenideElement MAIN_SECTION_HEADER = $$x("//h1").findBy(Condition.text("My Reminders"));
    private final SelenideElement MAIN_BLOCK =$("#my-reminders-init");


    private final SelenideElement CREATED_FOR_ME_TAB_BUTTON = MAIN_BLOCK.$x(""); //data-tl-test-id="my-queue.tab-for-me"
    private final SelenideElement CREATED_BY_ME_TAB_BUTTON =  MAIN_BLOCK.$x(""); //data-tl-test-id="my-queue.tab-by-me
    private final SelenideElement ARCHIVE_TAB_BUTTON =  MAIN_BLOCK.$x(""); //data-tl-test-id="my-queue.tab-archive"
    private final SelenideElement TIMEZONE_BLOCK =  MAIN_BLOCK.$x(""); //@class='reminder-timezone', span
    private final SelenideElement TIMEZONE_VALUE = TIMEZONE_BLOCK.$x(""); //data-tl-test-id="my-queue.timezone" , a
    private final SelenideElement RETENTION_PERIOD_INFO = MAIN_BLOCK.$x(""); //span contain text Retention period = 3 month

    private final SelenideElement TABLE_BLOCK = MAIN_BLOCK.$x(""); //data-testid="my-queue--table"
    private final SelenideElement TABLE_HEAD = TABLE_BLOCK.$x(""); // data-testid="my-queue--head" , thead
    private final SelenideElement TABLE_HEAD_CELL = TABLE_HEAD.$x(""); //th, data-testid="my-queue--head--cell"

    private final SelenideElement TABLE_BODY = TABLE_BLOCK.$x(""); // data-testid="my-queue--body", tbody
    private final SelenideElement TABLE_ROW = TABLE_BODY.$x(""); // contains data-testid | my-queue--row-row-0-2345, tr


    private final SelenideElement ROW_KEY = TABLE_ROW.$x(""); //data-tl-test-id="my-queue.key" /a
    private final SelenideElement ROW_SUMMARY = TABLE_ROW.$x("");
    private final SelenideElement ROW_ADDRESSEES =  TABLE_ROW.$x("");
    private final SelenideElement ROW_MESSAGE = TABLE_ROW.$x("");
    private final SelenideElement ROW_CREATOR = TABLE_ROW.$x("");
    private final SelenideElement ROW_NEXT_SEND = TABLE_ROW.$x("");
    private final SelenideElement ROW_RECURRENCE = TABLE_ROW.$x("");
    private final SelenideElement ROW_DELETE_BUTTON = TABLE_ROW.$x("");


    private final SelenideElement PAGINATION_BLOCK = MAIN_BLOCK.$x(""); //data-testid="my-queue.page" , nav
    private final SelenideElement PREV_PAGE_BUTTON = PAGINATION_BLOCK.$x(""); //data-testid="my-queue.page--left-navigator" button
    private final SelenideElement PAGE_BUTTON = PAGINATION_BLOCK.$x(""); //
    private final SelenideElement NEXT_PAGE_BUTTON = PAGINATION_BLOCK.$x(""); //data-testid="my-queue.page--right-navigator"

    public MyRemindersPage(){
        MAIN_SECTION_HEADER.shouldBe(Condition.visible);
    }
}
