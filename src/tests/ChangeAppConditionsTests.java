package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class ChangeAppConditionsTests extends CoreTestCase {
    @Test
    public void testChangeScreenOrientationOnSearchResults()
    {
        String searchText = "Java";
        String articleDescription = "Object-oriented programming language";

        SearchPageObject searchPageObject = new SearchPageObject(driver);
        searchPageObject.initSearchInput();
        searchPageObject.typeSearchString(searchText);
        searchPageObject.clickByArticleWithSubstring(articleDescription);

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);

        String titleBeforeRotation = articlePageObject.getArticleTitle();

        rotateScreenLandscape();

        String titleAfterRotation = articlePageObject.getArticleTitle();

        assertEquals(
                "Article have been changed after screen rotation",
                titleBeforeRotation,
                titleAfterRotation
        );

        rotateScreenPortrait();
        moveAppToBackground(5);

        String titleAfterRestore = articlePageObject.getArticleTitle();

        assertEquals(
                "Article have been changed after restoring from background",
                titleAfterRotation,
                titleAfterRestore
        );
    }

}
