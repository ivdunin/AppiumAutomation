package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject{

    private static final String
        FOLDER_NAME_TPL = "//android.widget.TextView[@text='{FOLDER_NAME}']",
        ARTICLE_BY_TITLE_TPL = "//*[@text='{ARTICLE_TITLE}']";

    /* TEMPLATES METHODS */
    private static String getFolderXpathByName(String substring)
    {
        return FOLDER_NAME_TPL.replace("{FOLDER_NAME}", substring);
    }

    private static String getArticleXpathByTitle(String substring)
    {
        return ARTICLE_BY_TITLE_TPL.replace("{ARTICLE_TITLE}", substring);
    }
    /* TEMPLATES METHODS */

    public MyListsPageObject(AppiumDriver driver)
    {
        super(driver);
    }

    public void waitForFolderAppear(String folderName)
    {
        String folderElementXpath = getFolderXpathByName(folderName);
        this.waitForElementPresent(
                By.xpath(folderElementXpath),
                "Cannot find folder by name: " + folderName,
                UI_INTERACTION_TIMEOUT
        );
    }

    public void openFolderByName(String folderName)
    {
        waitForFolderAppear(folderName);

        String folderElementXpath = getFolderXpathByName(folderName);
        this.waitForElementAndClick(
                By.xpath(folderElementXpath),
                "Cannot find and click folder by name: " + folderName,
                UI_INTERACTION_TIMEOUT
        );
    }

    public void swipeArticleToDelete(String articleTitle)
    {
        this.waitForArticleToAppearByTitle(articleTitle);

        String articleXpath = getArticleXpathByTitle(articleTitle);

        this.swipeElementToLeft(
                By.xpath(articleXpath),
                "Cannot find saved article by title: " + articleTitle
        );

        this.waitForArticleToDisappearByTitle(articleTitle);
    }

    public void waitForArticleToAppearByTitle(String articleTitle)
    {
        String articleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementPresent(By.xpath(articleXpath),
                "Article element not found in list!",
                SERVER_INTERACTION_TIMEOUT);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle)
    {
        String articleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementNotPresent(By.xpath(articleXpath),
                "Saved article still in list!",
                SERVER_INTERACTION_TIMEOUT);
    }
}
