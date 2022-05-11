package dmitrishin.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import dmitrishin.config.CredentialsConfig;
import dmitrishin.data.GenerateData;
import dmitrishin.helpers.Attach;
import dmitrishin.pages.RegistrationFormPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static java.lang.String.format;

public class TestBase {
    RegistrationFormPage registrationFormPage = new RegistrationFormPage();
    GenerateData generate = new GenerateData();
    static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);

    //Input parameters
    String firstName = generate.firstname;
    String lastName = generate.lastname;
    String email = generate.email;
    String gender = generate.setRandomGenderType();
    String phone = generate.phoneNumber;
    String dayOfBirth = generate.dayOfBirth;
    String monthOfBirth = generate.monthOfBirth;
    String yearOfBirth = generate.yearOfBirth;
    String subject = generate.setRandomSubject();
    String hobby = generate.setRandomHobbies();
    String imagePath = "img/Avatar.jpg";
    String address = generate.address;
    String state = "NCR";
    String city = "Delhi";
    String resultTitle = "Thanks for submitting the form";

    String fullName = format("%s %s", firstName, lastName);
    String dateOfBirth = format("%s %s,%s", dayOfBirth, monthOfBirth, yearOfBirth);
    String fileName = imagePath.substring(4);
    String stateAndCity = format("%s %s", state, city);

    static String urlParameterForJenkins = System.getProperty("baseDemoUrl");
    static String browserSizeParameterForJenkins = System.getProperty("browserSize");
    static String selenoidUrlParameterForJenkins = System.getProperty("selenoidServer");

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.baseUrl = urlParameterForJenkins;
        Configuration.browserSize = browserSizeParameterForJenkins;
        Configuration.remote = "https://" + config.login() + ":" + config.password() + "@" + selenoidUrlParameterForJenkins;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}
