package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    private static final String
        TITLE = "org.wikipedia:id/view_page_title_text",
        FOOTER_ELEMENT = "//*[@text='View page in browser']",
        OPTIONS_BUTTON = "//android.widget.ImageView[@content-desc='More options']",
        OPTIONS_ADD_TO_MY_READ_LIST_BUTTON = "//android.widget.TextView[@text='Add to reading list']",
        ADD_TO_MY_LIST_OVERLAY = "org.wikipedia:id/onboarding_button",
        MY_LIST_NAME_INPUT = "org.wikipedia:id/text_input",
        MY_LIST_OK_BUTTON = "//*[@text='OK']",
        CLOSE_ARTICLE_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']";

    public ArticlePageObject(AppiumDriver driver)
    {
        super(driver);
    }

    public WebElement waitForTitleElement()
    {
        return this.waitForElementPresent(By.id(TITLE), "Cannot find article title element!", SERVER_INTERACTION_TIMEOUT);
    }

    public String getArticleTitle()
    {
        WebElement titleElement = waitForTitleElement();
        return titleElement.getText();
    }

    public void swipeUpToFooter()
    {
        this.swipeUpToFindElement(By.xpath(FOOTER_ELEMENT),
                "Cannot find the end of article",
                20);
    }

    public void addArticleToMyList(String folderName)
    {
        this.waitForElementAndClick(
                By.xpath(OPTIONS_BUTTON),
                "Cannot find More options button!",
                UI_INTERACTION_TIMEOUT
        );

        this.waitForElementAndClick(
                By.xpath(OPTIONS_ADD_TO_MY_READ_LIST_BUTTON),
                "Cannot add article to reading list, button not found",
                UI_INTERACTION_TIMEOUT
        );

        this.waitForElementAndClick(
                By.id(ADD_TO_MY_LIST_OVERLAY),
                "Cannot click on 'Got It' button!",
                UI_INTERACTION_TIMEOUT
        );

        this.waitForElementAndClear(
                By.id(MY_LIST_NAME_INPUT),
                "Cannot find 'Name of list' input element!",
                UI_INTERACTION_TIMEOUT
        );

        this.waitForElementAndSendKeys(
                By.id(MY_LIST_NAME_INPUT),
                folderName,
                "Cannot find 'Name of list' input, cannot input text",
                UI_INTERACTION_TIMEOUT
        );

        this.waitForElementAndClick(
                By.xpath(MY_LIST_OK_BUTTON),
                "Cannot click 'Ok' button!",
                UI_INTERACTION_TIMEOUT
        );
    }

    public void waitForCloseArticleButtonPresent()
    {
        this.waitForElementPresent(By.xpath(CLOSE_ARTICLE_BUTTON),
                "Cannot find close article button!");
    }

    public void closeArticle()
    {
        waitForCloseArticleButtonPresent();

        this.waitForElementAndClick(
                By.xpath(CLOSE_ARTICLE_BUTTON),
                "Cannot close article (button X not found!)",
                UI_INTERACTION_TIMEOUT
        );

    }
}
