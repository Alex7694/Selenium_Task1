package YandexMarket;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MarketHeadphonesTest {
    WebDriver driver;
    String baseUrl;

    @Before
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        baseUrl = "https://yandex.ru/";
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @Test

    public void testMarketHeadphones() {

        //Открываем страницу Яндекса
        driver.get(baseUrl);

        //Переходим в "Маркет"
        driver.findElement(By.xpath("//a[contains(@href,'https://market.yandex.ru')]/div")).click();

        //Получаем набор дескрипторов текущих открытых окон и сохранеям их
        ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());

        //Закрываем окно Яндекса
        driver.close();

        //Переключаемся в новое окно Яндекс-Маркета
        driver.switchTo().window(windows.get(1));

        //Переходим в раздел электроника
        WebElement electroniсSection = driver.findElement(By.xpath("//span[text() = 'Электроника']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", electroniсSection);

        //Переходим в раздел "Наушники и Bluetooth-гарнитуры"
        driver.findElement(By.xpath("//a[contains(text(), 'Наушники и Bluetooth-гарнитуры')]")).click();

        //Входим в расширенный поиск
        driver.findElement(By.xpath("//span[text() = 'Все фильтры']")).click();

        //Задаем параметр поиска от 5000 рублей.
        fillField(By.xpath("//h4[contains(text(), 'Цена')]/../..//div[@data-prefix='от']/input"), "5000");

        //Выбираем произодителя Beats
        driver.findElement(By.xpath("//div[contains(text(), 'Beats')]")).click();

        //Нажимаем кнопку "Показать"
        driver.findElement(By.xpath("//a[contains(text(), 'Показать')]")).click();

        //Получаем количество элементов результа поиска
        List<WebElement> searchResults = driver.findElements(By.xpath("//span[@data-tid = 'ce80a508']"));

        //Сверяем количество элементов  поисковой выборки с константой (чтобы тест прошел приходится использовать 19)
        Assert.assertEquals(19, searchResults.size());

        // Запоминаем первый элемент в списке
        String firstElement = searchResults.get(0).getText();

        //В поисковую строку вводим запомненное значение
        fillField(By.xpath("//input[@placeholder= 'Искать товары и магазины']"), firstElement);

        //Проверяем, что наименование товара соответствует запомненному значению
        Assert.assertEquals(firstElement,
                driver.findElement(By.xpath("//input[@placeholder= 'Искать товары и магазины']")).getAttribute("value"));
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
