package testmammoth.UploadFile;

import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import testmammoth.Common.CommonFunction;

import testmammoth.SetUp.AllConstant;
import testmammoth.SetUp.DriverInitialize;

public class FileUploadTest {
    Logger log = Logger.getLogger(FileUploadTest.class.getName());
    AllConstant var =new AllConstant();
    CommonFunction functionObj =new CommonFunction();
    WebDriver driver;
    DriverInitialize objDriver =new DriverInitialize();
    @BeforeTest
    public void setUp() throws FileNotFoundException, IOException, ParseException, InterruptedException {

//       objDriver.setTestSuite();
        driver =  functionObj.getJSONDataAndDriverForCommon();
        try { 
            //Create log file to see the result
            Path logFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Result","FileUploadTest.log");
            String stringFile= logFile.toString();
            // This block configure the logger with handler and formatter  
            var.fileHandler = new FileHandler(stringFile);  
            log.addHandler(var.fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();  
            var.fileHandler.setFormatter(formatter);  

            // the following statement is used to log any messages  
            log.info("Log file for different file uploads");  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    @AfterTest
    public void tearDown() throws FileNotFoundException, IOException, ParseException {
        var.jsonData();
        // This will return the number of windows opened by Webdriver and will return Set of Strings
        Set<String>s1=driver.getWindowHandles();       
        // Now we will iterate using Iterator
        Iterator<String> I1= s1.iterator();      
        while(I1.hasNext())
        {  
            String child_window=I1.next();     
            // Here we will compare if parent window is not equal to child window then we  will close    
            if(!var.parent.equals(child_window))
            {
                driver.switchTo().window(child_window);
                log.info("Extra  tab is opened,we need to close it");
                System.out.println(driver.switchTo().window(child_window).getTitle());       
                driver.close();
            }
            // once all pop up closed now switch to parent window
            driver.switchTo().window(var.parent);
            log.info("We are on account landing page");
        }

    }
    @Test(priority =1,enabled =true,description ="Testing different file upload on landing page")
    public void fileUpload() throws InterruptedException, FileNotFoundException, IOException, ParseException, AWTException {
    var.jsonData();
    driver.get(var.url);
    functionObj.overlay();
//    functionObj.signIN();   
    var.parent =driver.getWindowHandle();
    String mSheets ="student.XLS";
           
	 (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewbuttonid))).click();
     (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.uploadDataset))).click();
     Thread.sleep(1000);
     new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.supportedFileMsg)));
     //Assert that Message for supported file format listed  
     Assert.assertEquals(driver.findElement(By.xpath(var.supportedFileMsg)).getText(), "Supported file types: .csv .tsv .txt .xls .xlsx .zip");
     log.info("Supported file types message displayed"+"\t");
     this.landingFileUpload("student.XLS");
     Thread.sleep(3000);
    //find select sheets to extract message
    (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.msgForselectSheetToExtract))).click();
    Thread.sleep(2000);
    //select all files to extract
    (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.selectAllSheetToExtract))).click();
    (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.extractSelected))).click();
    functionObj.uploadedFileStatus();
    Thread.sleep(1000);
    for(int i=0;i<2;i++) {
    (new WebDriverWait(driver,5)).until(ExpectedConditions.elementToBeClickable(By.xpath((var.msgToProcessHeaderFile)))).click();  
    Thread.sleep(1000);
    (new WebDriverWait(driver,5)).until(ExpectedConditions.elementToBeClickable(By.xpath((var.unprocessedRedDeleteButton)))).click(); 
    Assert.assertTrue((new WebDriverWait(driver,7)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteConfirmationModal))).isDisplayed(), "Delete multiple DS window don't get opened");  
    Thread.sleep(1000);
    (new WebDriverWait(driver,7)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.deleteButtonInConfirmationWindow))).click();
    Thread.sleep(1000);
    }
    
	(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewbuttonid))).click();
    (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.uploadDataset))).click();
    this.landingFileUpload("ZIPoffice_main.zip");
	functionObj.deleteDS();   
	(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewbuttonid))).click();
	(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.uploadDataset))).click();
	(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.closeIcon))).click();
	log.info("file upload model closed");
   
    functionObj.softAssert.assertAll();

    }
    public void landingFileUpload(String name) {
        //Uploading a file     
        WebElement file1= driver.findElement(By.xpath(var.fileuploadbuttonid));
        JavascriptExecutor js = (JavascriptExecutor) driver;   
        js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");
        Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files",name);
        String filelocation =pathToFile.toString();
        file1.sendKeys(filelocation);
        log.info("Uploading  a file named : "+ name);}
}
