package testmammoth.SanitySuite;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import testmammoth.Common.CommonFunction;
import testmammoth.SetUp.AllConstant;
import testmammoth.SetUp.DriverInitialize;

public class Lookup_sanity {
    Logger log = Logger.getLogger(Lookup_sanity.class.getName());
    AllConstant var =new AllConstant();
    CommonFunction functionObj =new CommonFunction();
    DriverInitialize objDriver =new DriverInitialize();
    WebDriver driver;

    @BeforeTest
    public void setUp() throws FileNotFoundException, IOException, ParseException, InterruptedException {
//       objDriver.setTestSuite();
        driver =  functionObj.getJSONDataAndDriverForCommon();
        try { 
            //Create log file to see the result
            Path logFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Result","Lookup_sanity.log");
            String stringFile= logFile.toString();
            // This block configure the logger with handler and formatter  
            var.fileHandler = new FileHandler(stringFile);  
            log.addHandler(var.fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();  
            var.fileHandler.setFormatter(formatter);  

            // the following statement is used to log any messages  
            log.info("Log file for lookup  rule ");  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

    }
    @AfterTest
    public void tearDown() throws FileNotFoundException, IOException, ParseException {
        var.jsonData();
        // This will return the number of windows opened by Webdriver and will return Set of St//rings
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
    @Test
    public void lookupRuleTest() throws FileNotFoundException, IOException, ParseException, InterruptedException {
        var.jsonData();
        driver.get(var.url);
//        functionObj.signIN();
        var.parent =driver.getWindowHandle();
        String [] fileName={"cust1_extra_col.xls","cust1_less_col.xls"};
        for(int i=0;i<fileName.length;i++) {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewbuttonid))).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.uploadDataset))).click();
        functionObj.landingFileUpload(fileName[i]);
        }
        
        functionObj.jumpToDataView(var.parent);      
        functionObj.makePipelineAutoRunOn();
        
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.addtaskButton))).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.mergeAndBranchOut))).click();
        
        new WebDriverWait(driver,5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.lookUPRule))).click();
        Assert.assertTrue((new WebDriverWait(driver,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.lookUPTitle))).isDisplayed(), "Lookup rule title window don't get displayed");
        //select this table
        Select selectThisTable =new Select(new WebDriverWait(driver,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.selectThisTable))));    
        selectThisTable.selectByVisibleText("Customer name (text)");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'ui-select-match')]/span"))).click();
      
        //select lookup table
        List<WebElement> selectLookTable =new WebDriverWait(driver,5).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.selectLookUpTable)));
        //selectLookTable.selectByVisibleText("Cust1 Extra Col.Xls Test02");
       

        for(int i=0;i< selectLookTable.size();i++){

            String textContent = selectLookTable.get(i).getText();

            if(textContent.toLowerCase().contains("Cust1_Extra".toLowerCase()))
            {

            	selectLookTable.get(i).click();
                break;
            }
            else if(i==selectLookTable.size()-1){
                Assert.fail("Cust1_Extra_Col.Xls Test02 file is not present");

            }
        }

        //select key  column
        Select selectKeyColumn =new Select(new WebDriverWait(driver,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.selectKeyColumn))));
        selectKeyColumn.selectByVisibleText("Customer name (text)");
        Thread.sleep(500);
        //select value column
        Select selectValueColumn =new Select(new WebDriverWait(driver,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.selectValueColumn))));
        selectValueColumn.selectByVisibleText("Country (text)"); 
        Thread.sleep(500);

        driver.findElement(By.xpath(var.inputBoxNewColumn)).clear();
        driver.findElement(By.xpath(var.inputBoxNewColumn)).sendKeys("ResultantColumn_Lookup");
        Thread.sleep(2000);
        //find Column Name dropdown
        
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.applyButtonInRemove))).click();
        functionObj.overlay();  
        functionObj.taskPanel();
        (new WebDriverWait(driver, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.lookUpRuleFromOtherInPipeline)));
        //find applied rule details
        String details = functionObj.stepDescrition();
        log.info("Added lookup rule detail is :"+details);
        log.info("--------------Lookup rule in new column applied successfully---------");  
        driver.close();
        driver.switchTo().window(var.parent);
        Thread.sleep(2000);
        functionObj.deleteDS();
        functionObj.deleteDS();
        functionObj.softAssert.assertAll();

    }}