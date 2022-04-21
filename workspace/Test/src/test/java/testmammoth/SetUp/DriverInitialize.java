package testmammoth.SetUp;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


public class DriverInitialize {
 
    AllConstant varFix =new AllConstant();
     static WebDriver driver;
     public Path downloadFile;
    @BeforeSuite
    public void setTestSuite() throws MalformedURLException, InterruptedException {

        int browser = Integer.parseInt(varFix.browsers, 16); 
        String currdir  =    System.getProperty("user.dir");
        File file =new File(currdir);
        if ((browser & varFix.FIREFOX) != 0) {
            //beta 2 version
            System.setProperty("webdriver.firefox.marionette", varFix.geckodriver);
            System.out.println("Firefox driver used");
            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 


        }
        if ((browser & varFix.CHROME) != 0) {
            System.out.println("Chrome driver used");

            //to disable developer extension mode
            ChromeOptions options= new ChromeOptions();
            options.addArguments("--headless","--disable-gpu");
            //options.setProxy(null);
            options.addArguments("chrome.switches","--disable-extensions");
            options.addArguments("chrome.switches","--disable-notifications");
            options.addArguments("disable-infobars");
            System.setProperty("webdriver.chrome.driver",varFix.chromedriver);
             downloadFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Download_dir");
            varFix.downloadFilepath= downloadFile.toString();

            //To download a file
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("profile.default_content_setting_values.notifications", 0);
            chromePrefs.put("download.default_directory", varFix.downloadFilepath);

            HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--test-type");
            options.addArguments("--disable-extensions"); //to disable browser extension popup
            options.addArguments("--window-size=1920,1080");
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
            cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            cap.setCapability(ChromeOptions.CAPABILITY, options);
            cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
            // Chrome browser initialization
            driver = new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
            System.out.println(driver.manage().window().getSize());

        }
        if ((browser & varFix.SAFARI) != 0) {
            System.out.println("Safari driver used");
            // Safari browser initialization
            driver = new SafariDriver();
            DesiredCapabilities dc = DesiredCapabilities.safari();
            dc.setCapability(SafariOptions.CAPABILITY, browser);

        }


    }

    @AfterSuite
    public void endTestSuite() {
        driver.quit();
    }

    public static   WebDriver getDriver(){
        return driver;
    }

}