package test;

import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeeting = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeeting = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeeting);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(25));
        $("[data-test-id=success-notification]").shouldHave(Condition.text("Успешно!\n" +
                "Встреча успешно запланирована на " + firstMeeting)).shouldBe(Condition.visible);

        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").doubleClick().sendKeys(secondMeeting);
        $(".button").click();
        $("[data-test-id=replan-notification]")
                .shouldHave(visible, Condition.text("Необходимо подтверждение"))
                .shouldBe(visible);
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(25));
        $("[data-test-id=success-notification]").shouldHave(Condition.text("Успешно!\n" +
                "Встреча успешно запланирована на " + secondMeeting)).shouldBe(Condition.visible);
    }
}
