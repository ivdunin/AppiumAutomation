import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RotationTest {
    private AppiumDriver driver;

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
        capabilities.setCapability("commandTimeouts", 30);  // avoid infinite waiting, when POST hangs

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        // or move to setUp
        if (driver.getOrientation() == ScreenOrientation.LANDSCAPE)
            driver.rotate(ScreenOrientation.PORTRAIT);

        driver.quit();
    }

    @Test
    public void testAssertAfterRotatate()
    {
        driver.rotate(ScreenOrientation.LANDSCAPE);
        throw new AssertionError("Test assert");

        // driver.rotate(ScreenOrientation.PORTRAIT);
    }

    @Test
    public void testScreenOrientation()
    {
        Assert.assertEquals(
                "Incorrect screen orientation",
                ScreenOrientation.PORTRAIT,
                driver.getOrientation()
        );
    }
}