package SberInsuarance;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class InsuaranceTest {
    WebDriver driver;
    String baseUrl;

    @Before
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        baseUrl = "https://www.sberbank.ru/ru/person";
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @Test
    public void testInsuarance() throws InterruptedException {

        //Переходим в меню "Страхование"
        driver.findElement(By.xpath("//a[@aria-label = 'Меню  Страхование']")).click();

        //Нажимаем "Перейти в каталог"
        driver.findElement(By.xpath("//a[(text() = 'Перейти в каталог')]")).click();

        //Устанавливаем ожидание, устанавливаемое для случая, когда элемент появляется на странице не сразу
        Wait<WebDriver> wait = new WebDriverWait(driver, 10, 1000);

        //Проверяем наличие на странице заголовка "Страхование путешественников"
        try {
            ((JavascriptExecutor) driver).executeScript("return arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//h3[text() = 'Страхование для путешественников']")));
            WebElement title = driver.findElement(By.xpath("//h3[text() = 'Страхование для путешественников']"));
            wait.until(ExpectedConditions.visibilityOf(title));
            Assert.assertEquals("Страхование для путешественников", title.getText());
        } catch (StaleElementReferenceException ex) {
            ((JavascriptExecutor) driver).executeScript("return arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//h3[text() = 'Страхование для путешественников']")));
            WebElement title = driver.findElement(By.xpath("//h3[text() = 'Страхование для путешественников']"));
            wait.until(ExpectedConditions.visibilityOf(title));
            Assert.assertEquals("Страхование для путешественников", title.getText());
        }

        //Нажимаем на кнопку "Оформить онлайн" в разделе "Страхование для путешественников"
        try {
            WebElement checkOnlineBtn = driver.findElement(By.xpath("//a[(contains(@href, 'viewCalc'))]"));
            wait.until(ExpectedConditions.elementToBeClickable(checkOnlineBtn));
            checkOnlineBtn.click();
        } catch (StaleElementReferenceException ex) {
            WebElement checkOnlineBtn = driver.findElement(By.xpath("//a[(contains(@href, 'viewCalc'))]"));
            wait.until(ExpectedConditions.elementToBeClickable(checkOnlineBtn));
            checkOnlineBtn.click();
        }

        //Выбираем сумму страховой защиты - "минимальная"
        driver.findElement(By.xpath("//h3[text() = 'Минимальная']")).click();

        //Нажимаем кнопку "Оформить"
        WebElement checkoutBtn = driver.findElement(By.xpath("//button[text() = 'Оформить']"));
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();

        //На вкладке "Оформление" в разделе "Застрахованные" заполняем поля:  фамилия, имя, дата рождения
        fillField(By.id("name_vzr_ins_0"), "Иван");
        fillField(By.id("surname_vzr_ins_0"), "Иванов");
        fillField(By.id("birthDate_vzr_ins_0"), "20.02.1984");

        //В разделе "Страхователь" заполнить необходимые поля
        driver.findElement(By.xpath("//label[text()= 'гражданин РФ']")).click();
        fillField(By.id("person_lastName"), "Петров");
        fillField(By.id("person_firstName"), "Петр");
        fillField(By.id("person_middleName"), "Петрович");
        fillField(By.id("person_birthDate"), "10.07.1982");

        //Убираем всплывающее окно даты рождения кликом в другое поле:)
        driver.findElement(By.id("person_middleName")).click();

        //Выбираем мужской пол
        driver.findElement(By.xpath("//label[text()= 'Мужской']")).click();

        //В разделе "Паспортные данные" заполняем необходимые поля
        fillField(By.id("passportNumber"), "645249");
        fillField(By.id("passportSeries"), "4217");
        fillField(By.id("documentIssue"), "УВД района Москвы");
        fillField(By.id("documentDate"), "12.07.2002");

        //Нажимаем кнопку "Продолжить"
        driver.findElement(By.xpath("//button[contains(text(), 'Продолжить')]")).click();

        //Проверяем корректность заполнения в разделе "Застрахованные"
        Assert.assertEquals("Иван",
                driver.findElement(By.id("name_vzr_ins_0")).getAttribute("value"));
        Assert.assertEquals("Иванов",
                driver.findElement(By.id("surname_vzr_ins_0")).getAttribute("value"));
        Assert.assertEquals("20.02.1984",
                driver.findElement(By.id("birthDate_vzr_ins_0")).getAttribute("value"));

        //Проверяем корректность заполнения в разделе "Страхователь"
        Assert.assertEquals("Петров",
                driver.findElement(By.id("person_lastName")).getAttribute("value"));
        Assert.assertEquals("Петр",
                driver.findElement(By.id("person_firstName")).getAttribute("value"));
        Assert.assertEquals("Петрович",
                driver.findElement(By.id("person_middleName")).getAttribute("value"));
        Assert.assertEquals("10.07.1982",
                driver.findElement(By.id("person_birthDate")).getAttribute("value"));

        //Проверяем корректность заполнения в разделе "Паспортные данные"
        Assert.assertEquals("645249",
                driver.findElement(By.id("passportNumber")).getAttribute("value"));
        Assert.assertEquals("4217",
                driver.findElement(By.id("passportSeries")).getAttribute("value"));
        Assert.assertEquals("УВД района Москвы",
                driver.findElement(By.id("documentIssue")).getAttribute("value"));
        Assert.assertEquals("12.07.2002",
                driver.findElement(By.id("documentDate")).getAttribute("value"));

        //Проверить, что появилось сообщение "При заполнении данных произошла ошибка"
        Assert.assertEquals("При заполнении данных произошла ошибка",
                driver.findElement(By.xpath("//div[contains(@class , 'alert-form-error')]")).getText());
    }

    public void fillField(By locator, String value) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

    @After
    public void afterTest() {
        driver.quit();
    }
}
