import lib.CoreTestCase;
import lib.ui.MainPageObject;
import lib.ui.SearchPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;

import java.util.List;

public class FirstTest extends CoreTestCase {
    private MainPageObject mainPageObject;

    protected void setUp() throws Exception
    {
        super.setUp();

        mainPageObject = new MainPageObject(driver);
    }

    @Test
    public void testSearch()
    {
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchInput();
        searchPageObject.typeSearchString("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testCheckHintBeforeSearch1()
    {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementWithText(
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
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        String searchFieldHint = mainPageObject.getElementText(
                mainPageObject.waitForElementPresent(
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
        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.waitForCancelButtonToAppear();
        searchPageObject.clickCancelSearch();
        searchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testCompareArticleText()
    {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language'!",
                SERVER_INTERACTION_TIMEOUT);


        WebElement titleElement = mainPageObject.waitForElementPresent(
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

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(String.format("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='%s']", searchText)),
                String.format("Cannot find '%s' article!", searchText),
                SERVER_INTERACTION_TIMEOUT);

        mainPageObject.swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find the end of the article",
                20
        );
    }

    @Test
    public void testCancelSearchEx()
    {
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        List<WebElement> elements = mainPageObject.waitForElementsPresent(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Search results not found!",
                SERVER_INTERACTION_TIMEOUT
        );

        Assert.assertTrue("Only one search result found!",
                elements.toArray().length > 1);

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementNotPresent(
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

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        List<WebElement> elements_title = mainPageObject.waitForElementsPresent(
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
    public void testSaveFirstArticleToMyList()
    {
        String folderName = "Learning programming";
        String searchText = "Java";
        String articleTitle = "Object-oriented programming language";

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input on main page",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(String.format("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='%s']", articleTitle)),
                "Cannot find article: '" + articleTitle + "'!",
                SERVER_INTERACTION_TIMEOUT);

        mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article header!",
                SERVER_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find More options button!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot add article to reading list, item not found",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot click on 'Got It' button!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find 'Name of list' input element!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                folderName,
                "Cannot find 'Name of list' input, cannot input text",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot click 'Ok' button!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article (button X not found!)",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot open my lists page",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(String.format("//android.widget.TextView[@text='%s']", folderName)),
                "Cannot find folder: " + folderName,
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.swapElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article"
        );

        mainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete article",
                UI_INTERACTION_TIMEOUT
        );
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults()
    {
        String searchText = "Java";
        String articleTitle = "Object-oriented programming language";

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input on main page",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(String.format("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='%s']", articleTitle)),
                "Cannot find article: '" + articleTitle + "'!",
                SERVER_INTERACTION_TIMEOUT);

        String titleBeforeRotation = mainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                SERVER_INTERACTION_TIMEOUT
        );


        System.out.println("Current orientation: " + driver.getOrientation().toString());

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String titleAfterRotation = mainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                SERVER_INTERACTION_TIMEOUT
        );

        Assert.assertEquals(
                "Article have been changed after screen rotation",
                titleBeforeRotation,
                titleAfterRotation
        );

        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.runAppInBackground(5);

        mainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article after restore",
                SERVER_INTERACTION_TIMEOUT
        );
    }

    @Test
    public void testSaveTwoArticlesAndDelete()
    {
        String folderName = "ex5";

        String searchTextToDelete = "Baseballs";
        String articleTitleToDelete = "The Baseballs";
        String searchTextToReopen = "Appium";
        String articleTitleToReopen = "Appium";

        findArticle(searchTextToDelete, articleTitleToDelete);
        addToReadingList(folderName);
        closeArticle();
        findArticle(searchTextToReopen, articleTitleToReopen);
        addToReadingList(folderName);
        closeArticle();

        openMyListsFolder(folderName);
        deleteArticleFromFolder(articleTitleToDelete);

        // Ensure that second article still in list
        int articleCount = mainPageObject.getAmountOfElements(By.xpath(String.format("//*[@text='%s']", articleTitleToReopen)));

        Assert.assertTrue(
                String.format("Article '%s' not found!", articleTitleToReopen),
                articleCount == 1
        );

        reopenArticleFromList(articleTitleToReopen);
    }

    @Test
    public void testAssertTitle()
    {
        String searchText = "Java";
        String articleText = "Java (programming language)";

        String articleLocatorJava = String.format("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='%s']", articleText);

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input on main page",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(articleLocatorJava),
                "Cannot find article: '" + articleText + "'!",
                SERVER_INTERACTION_TIMEOUT);

        mainPageObject.assertElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Article title not found!");
    }

    /**
     * Reopen article form list and check title. Precondition: call openMyListsFolder
     * @param articleTitle article to open
     */
    private void reopenArticleFromList(String articleTitle)
    {
        // Open article and check content
        mainPageObject.waitForElementAndClick(
                By.xpath(String.format("//*[@text='%s']", articleTitle)),
                "Cannot open article: " + articleTitle,
                UI_INTERACTION_TIMEOUT);

        String articleTitleText = mainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find article title",
                SERVER_INTERACTION_TIMEOUT);

        Assert.assertEquals(
                "Article title doesn't match, after open from saved lists",
                articleTitle,
                articleTitleText
        );

    }

    /**
     * Open My lists and click specified folder
     * @param folderName folder name to click on
     */
    private void openMyListsFolder(String folderName)
    {
        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot open my lists page",
                UI_INTERACTION_TIMEOUT
        );

        String folderLocator = String.format("//*[@resource-id='org.wikipedia:id/item_title'][@text='%s']", folderName);

        // Added for reliability, because waitForElementAndClick completed successfully but no click performed
        mainPageObject.isElementPresentAndDisplayed(By.xpath(folderLocator));

        mainPageObject.waitForElementAndClick(
                By.xpath(folderLocator),
                "Cannot find folder: " + folderName,
                UI_INTERACTION_TIMEOUT
        );
    }

    /**
     * Delete specified article. Preconditions: call openMyListsFolder
     * @param articleText article to delete
     */
    private void deleteArticleFromFolder(String articleText)
    {
        mainPageObject.swapElementToLeft(
                By.xpath(String.format("//*[@text='%s']", articleText)),
                "Cannot find saved article"
        );

        mainPageObject.waitForElementNotPresent(
                By.xpath(String.format("//*[@text='%s']", articleText)),
                "Cannot delete article: " + articleText,
                UI_INTERACTION_TIMEOUT
        );
    }

    /**
     * Open More Options -> Add to reading list
     * If called for the first time, click on Got IT
     * Note: will fail, when trying to add article into not existing folder!
     * @param folderName folder to add article (folder should already exist!)
     */
    private void addToReadingList(String folderName)
    {
        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find More options button!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.TextView[@text='Add to reading list']"),
                "Cannot add article to reading list, item not found",
                UI_INTERACTION_TIMEOUT
        );

        // Check if we already create reading list(s)
        if (mainPageObject.isElementPresentAndDisplayed(By.id("org.wikipedia:id/onboarding_button")))
        {
            mainPageObject.waitForElementAndClick(
                    By.id("org.wikipedia:id/onboarding_button"),
                    "Cannot click on 'Got It' button!",
                    UI_INTERACTION_TIMEOUT
            );

            mainPageObject.waitForElementAndClear(
                    By.id("org.wikipedia:id/text_input"),
                    "Cannot find 'Name of list' input element!",
                    UI_INTERACTION_TIMEOUT
            );

            mainPageObject.waitForElementAndSendKeys(
                    By.id("org.wikipedia:id/text_input"),
                    folderName,
                    "Cannot find 'Name of list' input, cannot input text",
                    UI_INTERACTION_TIMEOUT
            );

            mainPageObject.waitForElementAndClick(
                    By.xpath("//*[@text='OK']"),
                    "Cannot click 'Ok' button!",
                    UI_INTERACTION_TIMEOUT
            );
        }
        else
        {
            mainPageObject.waitForElementAndClick(
                    By.xpath(String.format("//*[@resource-id='org.wikipedia:id/item_title'][@text='%s']", folderName)),
                    String.format("Cannot find folder '%s' in reading list", folderName),
                    UI_INTERACTION_TIMEOUT
            );
        }
    }

    /**
     * Find and open article
     * @param searchText search text
     * @param articleText exact article title
     */
    private void findArticle(String searchText, String articleText)
    {
        String articleLocatorJava = String.format("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='%s']", articleText);

        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search input on main page",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                searchText,
                "Cannot find search input!",
                UI_INTERACTION_TIMEOUT
        );

        mainPageObject.waitForElementAndClick(
                By.xpath(articleLocatorJava),
                "Cannot find article: '" + articleText + "'!",
                SERVER_INTERACTION_TIMEOUT);

        mainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title: " + articleText,
                SERVER_INTERACTION_TIMEOUT
        );
    }

    /**
     * Close open article. Preconditions: call findArticle
     */
    private void closeArticle()
    {
        // For reliability
        mainPageObject.isElementPresentAndDisplayed(By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"));

        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article (button X not found!)",
                UI_INTERACTION_TIMEOUT
        );
    }
}
