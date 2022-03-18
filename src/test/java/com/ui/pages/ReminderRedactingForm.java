package com.ui.pages;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$x;


public class ReminderRedactingForm {
    private SelenideElement formHeader = $x("//h2[text()='Reminder']");
    private SelenideElement saveButton = $x(""); //data-tl-test-id="custom-rem.button-save"
    private SelenideElement cancelButton = $x(""); //data-tl-test-id="custom-rem.button-cancel"
    private SelenideElement creatorInfo = $x(""); //data-tl-test-id="custom-rem.creator"
}
