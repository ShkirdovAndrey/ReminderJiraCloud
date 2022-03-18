package com.ui.test;

import com.codeborne.selenide.Configuration;
import com.core.entities.*;
import com.core.enums.CornerNotifications;
import com.core.enums.DefaultRemType;

import com.core.enums.ReminderRepeatType;
import com.ui.pages.IssuePage;
import com.ui.pages.ReminderCreatingForm;
import com.ui.pages.maintenance.*;
import com.utils.DateGenerator;
import io.qameta.allure.TmsLink;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class IssuePageTests  {
    public static User user;
    public static IssuePage issuePage;
    public static LoginPage loginPage;
    private final GroupAndUserNames g = new GroupAndUserNames();


    @BeforeAll
    public static void setUp() {
        Configuration.startMaximized = true;
        readPropertiesFile();
        user = new User(getProperty("userName"),getProperty("userEmail"),getProperty("userPassword"));
        loginPage = new LoginPage();
        issuePage = loginPage
                .loginAndMoveMainPageAsUser(user)
                .getIssue();
    }

    @Test
    @TmsLink("DEV-T1173")
    @DisplayName("Проверка интерфейса")
    public void uiUxSectionTests(){
        issuePage.assertIsElementsDisplayed()
                 .assertNoRemindersInList();
    }


    private static Stream<Arguments> anyDefaultType(){
        return Stream.of(
                arguments(DefaultRemType.TONIGHT),
                arguments(DefaultRemType.TOMORROW),
                arguments(DefaultRemType.IN_A_WEEK),
                arguments(DefaultRemType.IN_A_MONTH)
        );
    }

    @ParameterizedTest
    @TmsLink("DEV-T1175")
    @DisplayName("Создание каждого типа дефолтного напоминания")
    @MethodSource("anyDefaultType")
    public void creatingDefault(DefaultRemType remType){
        Reminder rem = issuePage.createDefaultReminder(remType, user);
        CreatedReminderInIssueList remLine = new CreatedReminderInIssueList(rem);
        issuePage.assertNotificationRemWasAdded()
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved()
                .assertNoRemindersInList();
    }

    @Test //DEV-T1177
    @TmsLink("DEV-T1179")
    @DisplayName("Проверка содержания формы создания напоминания")
    public void creatingFormContentIsDisplay(){
        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);
        form
                .assertDefaultFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.DAILY,1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.MONTHLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.YEARLY, 1).assertRepeatingFormContent()
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.DO_NOT_REPEAT, 0).assertDefaultFormContent()
                .closeForm();
    }

    @Test
    @TmsLink("DEV-T1183")
    @DisplayName("Сценарий: создания одноразового напоминания")
    public void scenarioNonRecurrentReminderCreating(){
        String futureDate = DateGenerator.getFutureDate(2); //дата через пару дней
        String futureHour = DateGenerator.getFutureHour(2); //час через пару часов
        String changedMinutes = "20";
        String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ut eros libero. Vestibulum non semper odio. Cras non lacus quis neque vestibulum molestie. Praesent eget consequat purus, vel sodales sed.";

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);
        Reminder rem = form
                .specifyNextSendDateTime(futureDate, futureHour, changedMinutes)
                .writeMessage(longMessage)
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved()
                .assertNoRemindersInList();
    }

    @Test
    @TmsLink("DEV-T1184")
    @DisplayName("Сценарий: создание повторяемого напоминания, без окончания повторения")
    public void scenarioRepeatWithoutEndingReminderCreating(){
        String date = DateGenerator.getTomorrowDate();

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.YEARLY, user);
        Reminder rem = form
                .clearAddressee(user.getUserName()).addAddresseeInField(g.SITE_ADMIN_NAME)
                .chooseRepeatability(ReminderRepeatType.DAILY)
                .specifyNextSendDateTime(date,"08","55")
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved()
                .assertNoRemindersInList();
    }

    @Test
    @TmsLink("DEV-T1185")
    @DisplayName("Сценарий: создание повторяемого напоминания, с окончанием повторения")
    public void scenarioRepeatWithEndingReminderCreating(){
        String endDate = DateGenerator.getFutureDate(30);
        String message = "Сообщение напоминания";

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DAILY, user);

        Reminder rem = form
                .clearAddresseeField().addAddresseeInField(g.SITE_ADMIN_GROUP)
                .specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY,2)
                .specifyEndRepeatingDate(endDate)
                .writeMessage(message)
                .confirmCreation();

        CreatedReminderInIssueList remLine = issuePage.createReminderLineInIssue(rem);
        issuePage
                .assertRemWasCreated(rem)
                .deleteReminder(remLine)
                .assertNotificationWasRemoved()
                .assertNoRemindersInList();

    }

    @Test
    @TmsLink("DEV-T1189")
    @DisplayName("Ограничения при создании напоминания")
    public void creatingFormFieldsValidationTests(){
        String pastDate = DateGenerator.getPastDate();
        String pastHour = DateGenerator.getPastHour();
        String futureDate = DateGenerator.getFutureDate(2);
        String futureHour = DateGenerator.getFutureHour(1);

        ReminderCreatingForm form = issuePage.callNewReminderCreatingRForm(ReminderRepeatType.DO_NOT_REPEAT, user);

        form
                .clearAddresseeField()
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.EMPTY_ADDRESSEE_ERROR);

        form.addAddresseeInField(g.SITE_ADMIN_GROUP).chooseRepeatability(ReminderRepeatType.DAILY).clearEveryField();
        form
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.EMPTY_EVERY_FIELD_ERROR);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.WEEKLY,1)
                .clickEndsOnWithoutEndDateSpecify()
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.EMPTY_END_DATE_ERROR);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.DO_NOT_REPEAT,0)
                .specifyNextSendDateTime(pastDate, pastHour,"00")
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.INCORRECT_WHEN_DATE_ERROR);

        form.specifyRepeatabilityAndPeriod(ReminderRepeatType.DAILY,1)
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.INCORRECT_STARTS_ON_DATE_ERROR);

        form.specifyNextSendDateTime(futureDate,futureHour, "00")
                .specifyEndRepeatingDate(pastDate)
                .tryConfirmCreation()
                .assertFormErrorShown(CornerNotifications.INCORRECT_ENDS_ON_DATE_ERROR);

        form.closeForm();
    }

}
