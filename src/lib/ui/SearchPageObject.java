package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {

    private static final String
        SEARCH_INIT_ELEMENT = "//*[contains(@text, 'Search Wikipedia')]",
        SEARCH_INPUT = "//*[contains(@text, 'Search…')]",
        SEARCH_INPUT_ID = "org.wikipedia:id/search_src_text",
        SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
        SEARCH_RESULT_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
        SEARCH_ITEM_CONTAINER = "org.wikipedia:id/page_list_item_container";

    public SearchPageObject(AppiumDriver driver)
    {
        super(driver);
    }

    /* TEMPLATES METHODS */
    private static String getResultSearchElement(String substring)
    {
        return SEARCH_RESULT_TPL.replace("{SUBSTRING}", substring);
    }
    /* TEMPLATES METHODS */

    public void initSearchInput()
    {
        this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT),
                "Cannot find and click search init element!",
                UI_INTERACTION_TIMEOUT
                );
        this.waitForElementPresent(By.xpath(SEARCH_INPUT),
                "Cannot find search input after clicking search init element!");
    }

    public void typeSearchString(String searchString)
    {
        this.waitForElementAndSendKeys(By.xpath(SEARCH_INPUT),
                searchString,
                "Cannot find and type into search input!",
                UI_INTERACTION_TIMEOUT);
    }

    public void waitForSearchStringWithText(String hint)
    {
        this.waitForElementWithText(By.id(SEARCH_INPUT_ID),
                hint,
                "Cannot find search element with hint " + hint,
                UI_INTERACTION_TIMEOUT);
    }

    public void waitForSearchResult(String substring)
    {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementPresent(By.xpath(searchResultXpath),
                "Cannot find search result with substring: " + substring,
                SERVER_INTERACTION_TIMEOUT);
    }

    public void clickByArticleWithSubstring(String substring)
    {
        String searchResultXpath = getResultSearchElement(substring);
        this.waitForElementAndClick(By.xpath(searchResultXpath),
                "Cannot find and click search result with substring: " + substring,
                SERVER_INTERACTION_TIMEOUT);
    }

    public void waitForCancelButtonToAppear()
    {
        this.waitForElementPresent(By.id(SEARCH_CANCEL_BUTTON), "Cannot find search cancel button!",
                UI_INTERACTION_TIMEOUT);
    }

    public void waitForCancelButtonToDisappear()
    {
        this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON), "Search cancel button is still present!",
                UI_INTERACTION_TIMEOUT);
    }

    public void clickCancelSearch()
    {
        this.waitForElementAndClick(By.id(SEARCH_CANCEL_BUTTON), "Cannot find and click search cancel button",
                UI_INTERACTION_TIMEOUT);
    }

    public int getNumberOfFoundElements()
    {
        List<WebElement> elements = this.waitForElementsPresent(
            By.id(SEARCH_ITEM_CONTAINER),
            "Search results not found!",
            SERVER_INTERACTION_TIMEOUT
        );

        return elements.toArray().length;
    }

    public void waitForSearchResultDisappear()
    {
        this.waitForElementNotPresent(
                By.id(SEARCH_ITEM_CONTAINER),
                "Search result still visible",
                UI_INTERACTION_TIMEOUT
        );
    }
}
