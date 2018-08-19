package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {
    @Test
    public void testSaveFirstArticleToMyList()
    {
        String folderName = "Learning programming";
        String searchText = "Java";
        String articleDescription = "Object-oriented programming language";

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchString(searchText);
        searchPageObject.clickByArticleWithSubstring(articleDescription);

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();
        String articleTitle = articlePageObject.getArticleTitle();

        articlePageObject.addArticleToMyList(folderName);
        articlePageObject.closeArticle();

        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(folderName);
        myListsPageObject.swipeArticleToDelete(articleTitle);
    }

    @Test
    public void testSaveTwoArticlesAndDelete()
    {
        String folderName = "ex5";

        String searchTextToDelete = "Baseballs";
        String articleTitleToDelete = "The Baseballs";
        String searchTextToReopen = "Appium";
        String articleTitleToReopen = "Appium";

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchString(searchTextToDelete);
        searchPageObject.clickByArticleWithSubstring(articleTitleToDelete);

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        articlePageObject.waitForTitleElement();

        articlePageObject.addArticleToMyList(folderName);
        articlePageObject.closeArticle();

        // Second article
        searchPageObject.initSearchInput();
        searchPageObject.typeSearchString(searchTextToReopen);
        searchPageObject.clickByArticleWithSubstring(articleTitleToReopen);

        articlePageObject.addArticleToMyList(folderName);
        articlePageObject.closeArticle();

        NavigationUI navigationUI = new NavigationUI(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
        myListsPageObject.openFolderByName(folderName);
        myListsPageObject.swipeArticleToDelete(articleTitleToDelete);

        // Ensure that second article still in list and open it
        myListsPageObject.openArticleFromReadingList(articleTitleToReopen);
        String reopenedArticleTitle = articlePageObject.getArticleTitle();

        assertEquals(
                "Article title doesn't match, after open from saved lists",
                articleTitleToReopen,
                reopenedArticleTitle
        );
    }
}
