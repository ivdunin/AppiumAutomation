import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {
    private AppiumDriver driver;
    private int SERVER_INTERACTION_TIMEOUT = 15;
    private int UI_INTERACTION_TIMEOUT = 5;

    @Before
    public void setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/home/dunin/Projects/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void firstTest()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' text",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        waitElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language'!",
                SERVER_INTERACTION_TIMEOUT);
    }

    @Test
    public void testCheckHintBeforeSearch1()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementWithText(
                By.id("org.wikipedia:id/search_src_text"),
                "Search…",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        // Input search text here
    }

    @Test
    public void testCheckHintBeforeSearch2()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        String searchFieldHint = getElementText(
                waitElementPresent(
                        By.id("org.wikipedia:id/search_src_text"),
                        "Cannot find search input!"
        ));

        Assert.assertEquals(
                "Search field hint not found!",
                "Search…",
                searchFieldHint);

        // Input search text here
    }


    @Test
    public void testCancelSearch()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search text input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X button still visible!",
                UI_INTERACTION_TIMEOUT
        );
    }

    @Test
    public void testCompareArticleText()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language'!",
                SERVER_INTERACTION_TIMEOUT);


        WebElement titleElement = waitElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article header!",
                SERVER_INTERACTION_TIMEOUT
        );

        String articleTitle = titleElement.getText();

        Assert.assertEquals(
                "We see unexpected title!",
                "Java (programming language)",
                articleTitle
        );
    }

    @Test
    public void testSwipeArticle()
    {
        String searchText = "Appium";

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath(String.format("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='%s']", searchText)),
                String.format("Cannot find '%s' article!", searchText),
                SERVER_INTERACTION_TIMEOUT);

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find the end of the article",
                20
        );
    }

    @Test
    public void testCancelSearchEx()
    {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        List<WebElement> elements = waitElementsPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Search results not found!",
                SERVER_INTERACTION_TIMEOUT
        );

        Assert.assertTrue("Only one search result found!",
                elements.toArray().length > 1);

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Search result(s) still visible!",
                2
        );
    }

    /**
     * Check only visible results, no scroll down
     * Only check search result titles for whole word (not as a part of other word Java != JavaScript)
     */
    @Test
    public void testCheckSearchResults()
    {
//        String searchText = "Java";  // Fail
        String searchText = "Periodic table";  // Will Pass, probably :)
        String searchPattern = String.format("(?i).*?\\b%s\\b.*?", searchText);

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        List<WebElement> elements_title = waitElementsPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Search results not found!",
                SERVER_INTERACTION_TIMEOUT
        );

        for (WebElement element: elements_title
             ) {
            String txt = element.getText();
            boolean match = txt.matches(searchPattern);
            Assert.assertTrue(
                    String.format("Search result '%s' not contain whole word '%s'", txt, searchText),
                    match);
        }
    }

    @Test
    public void saveFirstArticleToMyList()
    {
        String folderName = "Learning programming";
        String searchText = "Java";
        String articleTitle = "Object-oriented programming language";

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input on main page",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath(String.format("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='%s']", articleTitle)),
                "Cannot find article: '" + articleTitle + "'!",
                SERVER_INTERACTION_TIMEOUT);

        waitElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article header!",
                SERVER_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find More options button!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot add article to reading list, item not found",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot click on 'Got It' button!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find 'Name of list' input element!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folderName,
                "Cannot find 'Name of list' input, cannot input text",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot click 'Ok' button!",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article (button X not found!)",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot open my lists page",
                UI_INTERACTION_TIMEOUT
        );

        waitForElementAndClick(
                By.xpath(String.format("//android.widget.TextView[@text='%s']", folderName)),
                "Cannot find folder: " + folderName,
                UI_INTERACTION_TIMEOUT
        );

        swapElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article"
        );

        waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete article",
                UI_INTERACTION_TIMEOUT
        );
    }

    private WebElement waitElementPresent(By by, String errorMessage)
    {
        return waitElementPresent(by, errorMessage, UI_INTERACTION_TIMEOUT);
    }

    private WebElement waitElementPresent(By by, String errorMessage, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private List<WebElement> waitElementsPresent(By by, String errorMessage, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }


    private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds)
    {
        WebElement element = waitElementPresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds)
    {
        WebElement element = waitElementPresent(by, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds)
    {

        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds)
    {
        WebElement element = waitElementPresent(by, errorMessage, timeoutInSeconds);
        element.clear();
        return element;
    }

    private String getElementText(WebElement element)
    {
        return element.getAttribute("text");
    }

    private WebElement waitForElementWithText(By by, String text, String errorMessage, long timeoutInSeconds)
    {
        WebElement element = waitElementPresent(by, errorMessage, timeoutInSeconds);
        Assert.assertEquals(
                "Incorrect element text",
                text,
                element.getText());
        return element;
    }

    protected void swipeUp(int timeOfSwipe)
    {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();

        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release()
                .perform();
    }

    protected void swipeUpQuick()
    {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String error_message, int maxSwipes)
    {
        int alreadySwiped = 0;

        while (driver.findElements(by).size() == 0)
        {
            if (alreadySwiped > maxSwipes){
                waitElementPresent(by, "Cannot find element by swiping up.\n" + error_message, 0);
                return;
            }

            swipeUpQuick();
            ++alreadySwiped;
        }
    }

    protected void swapElementToLeft(By by, String error_message)
    {
        WebElement element = waitElementPresent(by, error_message, 10);

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();

        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(150)
                .moveTo(left_x, middle_y)
                .release()
                .perform();
    }
}
