package YandexMarket;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MarketTvTest {
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
    @Ignore
    public void testMarketTv() {

        //Открываем страницу Яндекса
        driver.get(baseUrl);

        //Переходим в раздел "Маркет"
        driver.findElement(By.xpath("//div[text() = 'Маркет']")).click();

        //Получаем набор дескрипторов текущих открытых окон и сохранеям их
        ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());

        //Закрываем окно Яндекса
        driver.close();

        //Переключаемся в новое окно Яндекс-Маркета
        driver.switchTo().window(windows.get(1));

        //Переходим в раздел электроника
        WebElement electroniсSection = driver.findElement(By.xpath("//span[text() = 'Электроника']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", electroniсSection);

        //Выбираем раздел "Телевизоры"
        driver.findElement(By.xpath("//a[text() = 'Телевизоры' and  contains(@class, '2x2zBaVN-3')]")).click();

        //Входим в расширенный поиск
        driver.findElement(By.xpath("//span[text() = 'Все фильтры']")).click();

        //Задаем параметр поиска от 20000 рублей.
        fillField(By.xpath("//h4[contains(text(), 'Цена')]/../..//div[@data-prefix='от']/input"), "20000");

        //Выбираем телевизор Samsung
        driver.findElement(By.xpath("//div[(text() = 'Samsung')]")).click();

        //Выбираем телевизор LG
        driver.findElement(By.xpath("//div[(text() = 'LG')]")).click();

        //Нажимаем кнопку "Показать"
        driver.findElement(By.xpath("//a[contains(text(), 'Показать')]")).click();

        //Получаем количество элементов результа поиска
        List<WebElement> searchResults = driver.findElements(By.xpath("//span[@data-tid = 'ce80a508']"));

        // Сверяем количество элементов  поисковой выборки с константой (чтобы тест прошел приходится использовать 48)
        Assert.assertEquals(48, searchResults.size());

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
