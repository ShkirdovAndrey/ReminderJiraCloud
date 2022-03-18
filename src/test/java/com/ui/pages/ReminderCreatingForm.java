package com.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.core.entities.Reminder;
import com.core.entities.ReminderFormEntity;
import com.core.entities.User;
import com.core.enums.CornerNotifications;
import com.core.enums.ReminderRepeatType;
import com.ui.pages.maintenance.PageBase;
import com.utils.DateGenerator;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.core.enums.ReminderRepeatType.DO_NOT_REPEAT;

public class ReminderCreatingForm extends PageBase {
    @Getter
    @Setter
    ReminderFormEntity remForm;

    private final SelenideElement formIFrame = $x("//iframe[contains(@id, 'ru.teamlead.jira.plugins.reminder-for-jira.test__dialog-create-reminder')]");

    private final SelenideElement formHeader = $x("//h2[text()='Reminder']");
    private final SelenideElement addresseeHeader = $x("//label[text()='To']");
    private final SelenideElement endsHeaderLocator = $x("//span[text()='Ends']");
    private final SelenideElement endsRadioBlock = $x("//div[@data-tl-test-id='custom-rem.radio-block']");
    private final SelenideElement messageHeader = $x("//label[text()='Message']");

    private final SelenideElement createButton = $x("//button[@data-tl-test-id='custom-rem.button-create']");
    private final SelenideElement closeButton = $x("//button[@data-tl-test-id='custom-rem.button-cancel']");

    private final SelenideElement addressesField = $x("//div[contains(@class, 'custom-rem-addressees')]/child::div");
    private final SelenideElement addressesFieldClearButtonInactive =  $x("//div[contains(@class, 'select__clear-indicator')]//span[@aria-label='clear']");

    private final SelenideElement repeatSelector = $x("//div[contains(@class, 'custom-rem.repeat-selector')]");
    private String textInLocatorByRepeatType(ReminderRepeatType reminderRepeatType){
        return switch (reminderRepeatType){
            case DO_NOT_REPEAT -> "Repeat";
            case DAILY -> "Daily";
            case WEEKLY -> "Weekly";
            case MONTHLY -> "Monthly";
            case YEARLY -> "Yearly";
        };
    }
    private SelenideElement selectedRepeatLocator(ReminderRepeatType reminderRepeatType){
        return $x("//div[contains(@class, 'custom-rem.repeat-selector')]//div[contains(text(),'"+textInLocatorByRepeatType(reminderRepeatType)+"')]");
    }

    private SelenideElement repeatOptionLocator(ReminderRepeatType reminderRepeatType){
        return $x("//*[contains(@data-tl-test-id, 'custom-rem.repeat-selector-option') and contains(text(), '"+textInLocatorByRepeatType(reminderRepeatType)+"')]");
    }
    private final SelenideElement repeatPeriodField = $x("//input[@data-testid='custom-rem.period']");

    private final SelenideElement radioNeverOption = $x("//input[@data-testid='custom-rem.radio-never--radio-input']");
    private final SelenideElement radioOnOption = $x("//input[@data-testid='custom-rem.radio-on--radio-input']");
    private final SelenideElement endsDateField = $x("//div[@data-testid='custom-rem.end-date--container']");
    private final SelenideElement clearEndsFieldButton = $x("//div[@data-testid='custom-rem.end-date--container']//span[@aria-label='clear']");

    private final SelenideElement nextSendDateField = $x("//div[@data-testid='custom-rem.date--container']");
    private final SelenideElement nextSendHourSelector = $x("//div[contains(@class,'custom-rem.hours')]");
    private SelenideElement hoursOptionSelector(String hoursInHHFormat){
        return $x("//div[contains(@data-tl-test-id, 'custom-rem.hours-option') and contains(text(),"+hoursInHHFormat+")]");
    }
    private SelenideElement selectedHoursLocator(String hoursInHHFormat){
        return $x("//div[contains(@class,'custom-rem.hours')]//div[contains(text(), "+hoursInHHFormat+")]");
    }
    private final SelenideElement nextSendMinutesSelector = $x("//div[contains(@class, 'custom-rem.minutes')]");
    private SelenideElement minutesOptionSelector(String minutesInmmFormat){
        return $x("//div[contains(@data-tl-test-id, 'custom-rem.minutes-option') and contains(text(),"+minutesInmmFormat+")]");
    }
    private SelenideElement selectedMinutesLocator(String minutesInmmFormat){
        return $x("//div[contains(@class,'custom-rem.minutes')]//div[contains(text(), "+minutesInmmFormat+")]");
    }

    private final SelenideElement messageField = $x("//textarea[@data-testid='custom-rem.message']");


    public ReminderCreatingForm(ReminderRepeatType repeatType, User user){
        actions().pause(1600).perform();     //TODO убрать паузу когда Юра поправит форму
        switchToFrame(formIFrame.shouldBe(Condition.exist));
        remForm = new ReminderFormEntity();

        addresseeHeader.shouldBe(Condition.visible);
                if (repeatType == DO_NOT_REPEAT) {
            selectedRepeatLocator(DO_NOT_REPEAT).shouldBe(Condition.visible);
        } else {
            specifyRepeatabilityAndPeriod(repeatType, 1);
            endsHeaderLocator.shouldBe(Condition.visible);
            endsRadioBlock.shouldBe(Condition.visible);
        }
        nextSendDateField.shouldBe(Condition.visible);
        nextSendHourSelector.shouldBe(Condition.visible);
        nextSendMinutesSelector.shouldBe(Condition.visible);
        messageHeader.shouldBe(Condition.visible);


        ArrayList<String> addresseesList = new ArrayList<>();
        addresseesList.add(user.getUserName());

        remForm.setCreatorName(user.getUserName());
        remForm.setAddresseesList(addresseesList);
        remForm.setRepeatType(repeatType.getValue());
        remForm.setEnds(false);
        remForm.setEndDate("");
        remForm.setDateTime(DateGenerator.getTomorrowDate() + " " + DateGenerator.getCurrentHour());
        remForm.setMessage("");
    }

    @Step("Remove addressee {0} from field To")
    public ReminderCreatingForm clearAddressee(String addressee){
        SelenideElement addresseeLocator= $x("//div[contains(text(), '"+addressee+"')]/parent::div//span[@aria-label='Clear']");
        addresseeLocator.shouldBe(Condition.visible).click();
        remForm.getAddresseesList().remove(addressee);
        return this;
    }

    @Step("Remove all addressees from field To")
    public ReminderCreatingForm clearAddresseeField(){
        addressesFieldClearButtonInactive.click();
        remForm.getAddresseesList().clear();
        return this;
    }

    //TODO Лучше бы избавиться от паузы и научить код видеть выпадающий список адресатов и нужного адресата в нём
    @Step("Add addressee {0} in field To")
    public ReminderCreatingForm addAddresseeInField(String addressee) {
        Selenide.actions().moveToElement(addressesField.shouldBe(Condition.visible)).click()
                .sendKeys(addressee).pause(2000)
                .sendKeys(Keys.ENTER)
                .release()
                .sendKeys(Keys.TAB) //сброс фокуса с поля
                .perform();
        remForm.getAddresseesList().add(addressee);
        return this;
    }
    //==============================================REPEATING SETTINGS METHODS ======================

    @Step("Set type of repeat: {0}")
    public ReminderCreatingForm chooseRepeatability(ReminderRepeatType remainderRepeatType){
        repeatSelector.shouldBe(Condition.visible).click();
        repeatOptionLocator(remainderRepeatType).shouldBe(Condition.visible).click();
        selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
        repeatPeriodField.shouldBe(Condition.visible);
        remForm.setRepeatType(remainderRepeatType.getValue());
        return this;
    }

    @Step("Clear Every-field")
    public void clearEveryField(){
        repeatPeriodField.sendKeys(Keys.BACK_SPACE);

    }

    @Step("Fill Every-field with number {0}")
    public void fillEveryPeriod(int repetitionRate){
        String rate = Integer.toString(repetitionRate);
        clearEveryField();
        repeatPeriodField.sendKeys(rate);
        remForm.setRepetitionRate(repetitionRate);
    }

    @Step("Specify the Repeat as {0} and repetition rate as {1} ")
    public ReminderCreatingForm specifyRepeatabilityAndPeriod(ReminderRepeatType remainderRepeatType, int repetitionRate ){
        repeatSelector.shouldBe(Condition.visible).click();
        repeatOptionLocator(remainderRepeatType).shouldBe(Condition.visible).click();
        remForm.setRepeatType(remainderRepeatType.getValue());

        if (remainderRepeatType == DO_NOT_REPEAT) {
            selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
        } else {
            selectedRepeatLocator(remainderRepeatType).shouldBe(Condition.visible);
            repeatPeriodField.shouldBe(Condition.visible);
            fillEveryPeriod(repetitionRate);
        }
        return this;
    }
    //==============================================NEXT SEND DATE METHODS ======================

    @Step("Fill the next send date field")
    public ReminderCreatingForm fillNextDateInForm(String dateDDMMYYYY) { //дата в формате, зависящем от настроек окружения; на тестовом установлен формат DD/MM/YYYY
        Selenide.actions().moveToElement(nextSendDateField).click(nextSendDateField).sendKeys(dateDDMMYYYY).sendKeys(Keys.ENTER).release().perform();
        return this;
    }
    @Step("Choose Dispatch time: hours")
    public ReminderCreatingForm chooseHoursInForm(String hoursInHHFormat){
        nextSendHourSelector.click();
        hoursOptionSelector(hoursInHHFormat).shouldBe(Condition.visible).click();
        selectedHoursLocator(hoursInHHFormat).shouldBe(Condition.visible);
        return this;
    }

    @Step("Choose Dispatch time: minutes")
    public ReminderCreatingForm chooseMinutesInForm(String minutesInmmFormat){ //принимает только минуты с пятиминутным интервалом! начиная с 00 и до 55.
        nextSendMinutesSelector.click();
        minutesOptionSelector(minutesInmmFormat).shouldBe(Condition.visible).click();
        selectedMinutesLocator(minutesInmmFormat).shouldBe(Condition.visible);
        return this;
    }


    public ReminderCreatingForm specifyNextSendDateTime(String dateDDMMYYYY, String hoursInHHFormat, String minutesInmmFormat){
        fillNextDateInForm(dateDDMMYYYY);
        chooseHoursInForm(hoursInHHFormat);
        chooseMinutesInForm(minutesInmmFormat);
        setDateTime(dateDDMMYYYY,hoursInHHFormat,minutesInmmFormat);
        return this;
    }

    public void setDateTime(String dateDDMMYYYY, String hoursInHHFormat, String minutesInmmFormat) {
        remForm.setDateTime(dateDDMMYYYY+" "+hoursInHHFormat+":"+minutesInmmFormat);
    }

    //==============================================REPEAT END SETTINGS METHODS ======================
    @Step("Choose Ends radio-option: Never")
    public void chooseNeverEnds(){
        radioNeverOption.click();
    }

    @Step("Choose Ends radio-option: On")
    private void chooseEndsOn(){
        radioOnOption.click();
    }

    @Step("Choose Ends radio-option: On, without End Date specify")
    public ReminderCreatingForm clickEndsOnWithoutEndDateSpecify(){
        chooseEndsOn();
        return this;
    }
    @Step("Clear End Date Field")
    private void clearEndDateField(){
        clearEndsFieldButton.click();
    }

    @Step("Specify End Repeating Date")
    public ReminderCreatingForm specifyEndRepeatingDate(String dateDDMMYYYY){
        chooseEndsOn();
        Selenide.actions().moveToElement(endsDateField).click(endsDateField).sendKeys(dateDDMMYYYY).sendKeys(Keys.ENTER).release().perform();
        remForm.setEnds(true);
        remForm.setEndDate(dateDDMMYYYY);
        return this;
    }
    public ReminderCreatingForm setNeverEndDate(){
        chooseNeverEnds();
        remForm.setEnds(false);
        remForm.setEndDate("");
        return this;
    }

    @Step("Write the message")
    public ReminderCreatingForm writeMessage(String message){
        messageField.shouldBe(Condition.visible).sendKeys(message);
        remForm.setMessage(message);
        return this;
    }
    @Step("Check that there is corner notification following type: {0}")
    public void assertFormErrorShown(CornerNotifications cornerNotifications){
        assertNotificationShown(cornerNotifications, formIFrame);
    }

    @Step("Close the form")
    public void closeForm(){
        closeButton.shouldBe(Condition.visible).click();
        new IssuePage(); //это нужно для перехода в нужный фрейм
    }

    @Step("Create the Reminder-object")
    public Reminder createCustomReminder(){
        boolean repeat;
        repeat = !remForm.getRepeatType().equals("Don't repeat");
        boolean haveEndDate;
        haveEndDate = !remForm.getRepeatType().equals("Don't repeat") || !remForm.isEnds();

        return new Reminder(remForm.getCreatorName(), remForm.getAddresseesList(), repeat ,remForm.getDateTime(), remForm.getRepeatType(), remForm.getRepetitionRate(), haveEndDate,remForm.getEndDate(), remForm.getMessage());
    }

    @Step("Confirm creation in form")
    public Reminder confirmCreation(){
        createButton.shouldBe(Condition.visible).click();
        actions().pause(300).perform(); //TODO Убрать
        formHeader.shouldNotBe(Condition.visible);
        switchToDefaultContent();
        new IssuePage(); //это нужно для перехода в нужный фрейм
        return createCustomReminder();
    }

    @Step("Attempt to create reminder")
    public ReminderCreatingForm tryConfirmCreation(){
        createButton.shouldBe(Condition.visible).click();
        return this;
    }

    @Step("Assertions content default reminder creation form")
    public ReminderCreatingForm assertDefaultFormContent(){
        SelenideElement addresseeValue = $x("//div[contains(@class, 'custom-rem-addressees')]//div[contains(@class, 'select__multi-value__label')]");
        SelenideElement dateValue = $x("//div[@data-testid='custom-rem.date--container']//div[contains(@class,'singleValue')]");
        SelenideElement hourValue = $x("//div[contains(@class, 'custom-rem.hours')]//div[contains(@class,'singleValue')]");
        SelenideElement minutesValue = $x("//div[contains(@class, 'custom-rem.minutes')]//div[contains(@class,'singleValue')]");
        SelenideElement repeatHeader = $x("//label[text()='Repeat']");
        SelenideElement whenHeader = $x("//label[text()='When']");

        Assertions.assertAll(
                () -> Assertions.assertTrue(formHeader.isDisplayed(), "Не выводится заголовок формы"),
                () -> Assertions.assertTrue(addresseeHeader.isDisplayed(), "Не выводится заголовок поля адресатов или самое поле"),
                () -> Assertions.assertEquals(remForm.getCreatorName(),addresseeValue.getText(),"Не выводится адресат-создатель или выводится неверный адресат"),
                () -> Assertions.assertTrue(repeatHeader.isDisplayed(), "Не выводится заголовок поля повторяемости"),
                () -> Assertions.assertTrue(selectedRepeatLocator(DO_NOT_REPEAT).isDisplayed(),"Не выставлен дефолтный вариант повторяемости напоминания"),
                () -> Assertions.assertTrue(whenHeader.isDisplayed(),"Не выводится заголовок поля даты отправки"),
                () -> Assertions.assertEquals( DateGenerator.getTomorrowDate(), dateValue.getText(), "Выводится неверная дата в поле даты"),
                () -> Assertions.assertEquals( DateGenerator.getCurrentOnlyHour(), hourValue.getText(), "Выводится неверный час в поле часа"),
                () -> Assertions.assertEquals("00",minutesValue.getText(), "Выводится неверные минуты в поле минут"),
                () -> Assertions.assertTrue(messageHeader.isDisplayed(), "Не выводится заголовок поля сообщения"),
                () -> Assertions.assertTrue(createButton.isDisplayed(), "Не выводится кнопка создания"),
                () -> Assertions.assertTrue(closeButton.isDisplayed(), "Не выводится кнопка отмены"),
                () -> Assertions.assertEquals("A short reminder message (optional, max. 255 symbols)", messageField.getAttribute("placeholder"), "Не выводится плейсхолдер/содержание плейсхолдера некорректно")
        );
        return this;
    }

    @Step("Assertions content repeating reminder creation form")
    public ReminderCreatingForm assertRepeatingFormContent(){
        SelenideElement everyHeader = $x("//span[text()='Every']");
        String text =
                switch (remForm.getRepeatType()){
                    case "Daily" ->  "day(s)";
                    case "Weekly" ->  "week(s)";
                    case "Monthly" ->  "month(s)";
                    case "Yearly" ->  "year(s)";
                    default -> ""; // TODO: 24.02.2022 Можно оставить так, можно unexpected исключение кинуть.
                };
        SelenideElement periodLine = $x("//span[text()='"+text+"']");
        SelenideElement startsOnHeader = $x("//label[text()='Starts On']");

        Assertions.assertAll(
                () -> Assertions.assertTrue(everyHeader.isDisplayed(), "Не выводится надпись Every между полями настройки повторяемости и периодичности"),
                () -> Assertions.assertTrue(periodLine.isDisplayed(), "Не выводится надпись " + text + " после поля указания периодичности"),
                () -> Assertions.assertTrue(startsOnHeader.isDisplayed(), "Не выводится заголовок Starts On или он не поменялся с When"),
                () -> Assertions.assertTrue(endsHeaderLocator.isDisplayed(), "Не выводится заголовок настройки окончания повторения Ends"),
                () -> Assertions.assertTrue(endsRadioBlock.isDisplayed(), "Не выводится блок радиокнопок настройки окончания повторения")
        );
        return this;
    }
}
