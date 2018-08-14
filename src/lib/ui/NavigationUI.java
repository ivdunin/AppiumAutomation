package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class NavigationUI extends MainPageObject {

    private static final String
        MY_LISTS_BUTTON = "//android.widget.FrameLayout[@content-desc='My lists']";

    public NavigationUI(AppiumDriver driver)
    {
        super(driver);
    }

    public void clickMyLists()
    {
        this.waitForElementAndClick(
                By.xpath(MY_LISTS_BUTTON),
                "Cannot open my lists page",
                UI_INTERACTION_TIMEOUT
        );

    }
}
