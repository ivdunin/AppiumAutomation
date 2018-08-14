package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class CoreTestCase extends TestCase {
    private static String appiumURL = "http://127.0.0.1:4723/wd/hub";
    protected AppiumDriver driver;
    protected int SERVER_INTERACTION_TIMEOUT = 15;
    protected int UI_INTERACTION_TIMEOUT = 5;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/home/dunin/Projects/JavaAppiumAutomation/apks/org.wikipedia.apk");
        capabilities.setCapability("commandTimeouts", 30);  // avoid infinite waiting, when POST hangs

        driver = new AndroidDriver(new URL(appiumURL), capabilities);
        this.rotateScreenPortrait();
    }

    @Override
    protected void tearDown() throws Exception
    {
        driver.quit();

        super.tearDown();
    }

    protected void rotateScreenPortrait()
    {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    protected void rotateScreenLandscape()
    {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    protected void moveAppToBackground(int seconds)
    {
        driver.runAppInBackground(seconds);
    }
}
