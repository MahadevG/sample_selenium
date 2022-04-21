package testmammoth;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.opencsv.CSVReader;

public class CommonFunctions {
	WebDriver driverValue=null;
	final String chromedriver =System.getenv("MAMMOTH_CHROME_DRIVER");
	final String geckodriver =System.getenv("MAMMOTH_FIREFOX_DRIVER");
	final String url = System.getenv("MAMMOTH_TEST_SITE");
	final String browsers =System.getenv("MAMMOTH_BROWSER");
	final String frequencies =System.getenv("MAMMOTH_FREQUENCY");
	final String secondaryUser =System.getenv("MAMMOTH_SECONDARY_USER");
	final String  seleniumAccountEmailPassword=System.getenv("MAMMOTH_SECONDARY_EMAIL_PASSWORD");
	final String salesforceUser =System.getenv("SALESFORCE_SELENIUM_USER");
	final String salesforcePass =System.getenv("SALESFORCE_SELENIUM_PASSWORD");

	Logger log = Logger.getLogger(CommonFunctions.class.getName());
	public static final int FIREFOX = 1;//0001;  
	public static final int CHROME =2; //0010;
	public static final int SAFARI =4;// 0100;

	public static final int MINUTE = 1;//0001;  
	public static final int HOUR =2; //0010;
	public static final int DAY =4;// 0100;
	public static final int WEEK =8;// 1000;
	final String userName = System.getenv("MAMMOTH_USERNAME");
	final String passwd = System.getenv("MAMMOTH_PASSWORD");
	final String hostName = System.getenv("MAMMOTH_HOST_NAME");
	final String postgrsqlUser = System.getenv("MAMMOTH_POSTGRESQL_USER");
	final String postgresqlPassword = System.getenv("MAMMOTH_POSTGRESQL_PASSWORD");
	//final String  onBoardstatus=System.getenv("MAMMOTH_ON_BOARD_STATUS");
	//final String postgresqlDBName = System.getenv("MAMMOTH_DATABASE_NAME");
	private static final long TEN_SECONDS=13*1000;
	public SoftAssert softAssert = new SoftAssert();
	String winHandleBefore;
	int impValue ;
	int MaxMinImpFlag;
	int MaxMinDateFlag;
	Integer integerValueOfSumImp;
	Integer integerValueOfClicks;
	Integer integerValueOfAvgPos;
	String downloadFilepath;
	public String tableName="";
	ArrayList<String> impColArrayInString=new ArrayList<String>();
	ArrayList<String> clicksColArrayInString=new ArrayList<String>();
	ArrayList<Float> arrayOfAvgPosMathRule =new ArrayList<Float>();
	ArrayList<String> arrayOfKeywordGroupMathRule =new ArrayList<String>();
	ArrayList<String> arrayOfEngineMathRule =new ArrayList<String>();
	ArrayList<String> 	arrayOfDateInString=new ArrayList<String>();
	ArrayList<String> 	arrayOfCampaignMathRule=new ArrayList<String>();

	public int getImpValue() {
		return impValue;
	}
	public void setImpValue(int impValue) {
		this.impValue = impValue;
	}
	String currdir  =	System.getProperty("user.dir");
	File file =new File(currdir);
	public WebDriver browserSetUP(){
		int browser = Integer.parseInt(browsers, 16); 

		if ((browser & FIREFOX) != 0) {
			//beta 2 version
			System.setProperty("webdriver.firefox.marionette", geckodriver);
			System.out.println("Firefox driver used");
			driverValue = new FirefoxDriver();
			driverValue.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 


		}
		if ((browser & CHROME) != 0) {
			System.out.println("Chrome driver used");

			//to disable developer extension mode
			ChromeOptions options= new ChromeOptions();
			//options.setProxy(null);
			options.addArguments("chrome.switches","--disable-extensions");
			options.addArguments("chrome.switches","--disable-notifications");
			options.addArguments("disable-infobars");
			System.setProperty("webdriver.chrome.driver",chromedriver);
			Path downloadFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Download_dir");
			downloadFilepath= downloadFile.toString();

			//To download a file
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);

			HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--test-type");
			options.addArguments("--disable-extensions"); //to disable browser extension popup
			options.addArguments("window-size=1400,800");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			// Chrome browser initialization
			driverValue = new ChromeDriver(options);

			driverValue.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driverValue.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
			System.out.println(driverValue.manage().window().getSize());

		}
		if ((browser & SAFARI) != 0) {
			System.out.println("Safari driver used");
			// Safari browser initialization
			driverValue = new SafariDriver();
			DesiredCapabilities dc = DesiredCapabilities.safari();
			dc.setCapability(SafariOptions.CAPABILITY, browser);

		}
		return driverValue;

	}
	public void waitToLoad() {
		WebDriverWait wait = new WebDriverWait(driverValue, 15);
		wait.until( new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				JavascriptExecutor js = (JavascriptExecutor) d;
				return (Boolean) js.executeScript("return angular.element(document.body).injector().get(\'$http\').pendingRequests.length == 0");
				//return (Boolean) js.executeScript("return $.active == 0") &&(Boolean) js.executeScript("return angular.element(document.body).injector().get(\'$http\').pendingRequests.length == 0");


			}
		});
	}
	public void overlay() throws InterruptedException{
		driverValue.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);  
		try{
			long oldTime=System.currentTimeMillis();
			outerloop:
				while(driverValue.findElement(By.xpath("//spinner[@class='ng-scope']")).isDisplayed()){

					log.info("overlay is there");	

					if(System.currentTimeMillis()- oldTime>TEN_SECONDS){

						driverValue.navigate().refresh();
						try{
							//again find overlay after refreshing
							while(driverValue.findElement(By.xpath("//spinner[@class='html5spinner ng-scope']")).isDisplayed()){
								long oldTimeAfterRefresh=System.currentTimeMillis();
								if(System.currentTimeMillis()- oldTimeAfterRefresh>TEN_SECONDS){
									Assert.fail("The current test case took much longer than expected : loader stayed on the screen");
									break outerloop;

								}
								else {

									log.info("Overlay is there");
								}
							}
						}

						catch(Exception e){
							log.info("Exception :Overlay is not present After refreshing");	
							//Overlay is not present on screen but test should mark as fail,as it required browser refresh to work.
							softAssert.assertTrue(false, "Current test required browser refresh to work");
							break outerloop;
						}

					}
					Thread.sleep(200);
					// return;
				}
			log.info("Overlay is invisible now");	

		}

		catch(Exception e){

			log.info("Exception :Overlay element id is not present.");
			Thread.sleep(1000);
			Boolean val=(new WebDriverWait(driverValue,10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'view-landing')]/spinner[@class='ng-scope ng-hide']")))!=null;
			Assert.assertTrue(val,"Overlay is still present.Logic failed");

		} 
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);  
		}  

	}
	public Wait<WebDriver>  fluentWait(){
		Wait<WebDriver> waitForElement = new FluentWait<WebDriver>(driverValue)
				.withTimeout(120, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		return waitForElement;
	}
	public void scrollHorizontalOnDataView() throws InterruptedException{
		Wait<WebDriver> waitForElement=	this.fluentWait();
		JavascriptExecutor jse =	(JavascriptExecutor) driverValue;
		List<WebElement> lastColumn=	waitForElement.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='ui-widget-content slick-row odd' or @class='ui-widget-content slick-row even'][last()]/child::div")));
		//log.info("Column size is  " +lastColumn.size());
		for(int j=0;j<=lastColumn.size();j++){
			if(j==lastColumn.size()-1){

				jse.executeScript("var g=window.angular.element('.active-workspace .ds-grid.grid-container').scope().vm.mainGridUnit.gridInstance.grid;var c=g.getColumns();g.scrollCellIntoView(0,c.length-1)",lastColumn.get(j));//scroll function to see last column of table
				//jse.executeScript("arguments[0].scrollIntoView(true);",lastColumn.get(j)); //scroll function to see last column of table
			}
		}

	}
	public void pageTitle(){
		//this.waitToLoad();

		Wait<WebDriver> waitForElement=	this.fluentWait();
		//waitForElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//title[contains(text(),'View')]")));
		waitForElement.until(ExpectedConditions.titleContains("View"));
	}
	public  WebDriver signIN() throws InterruptedException{

		WebElement uname=	(new WebDriverWait(driverValue, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(ConstString.uid)));
		//Enter user name fetched from json file
		uname.sendKeys(userName);
		//clear password field
		driverValue.findElement(By.id(ConstString.passid)).clear();  
		//Enter password
		driverValue.findElement(By.id(ConstString.passid)).sendKeys(passwd); 
		//click on sign in button
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.signbuttonid))).click();
		Thread.sleep(1000);

		this.overlay();
		Thread.sleep(1000);
		if(isElementPresent(By.xpath("//div[@class='no-data']"))){
			//if(driverValue.findElement(By.xpath("//div[@class='no-data']")).isDisplayed() && driverValue.findElement(By.xpath("//div[@class='no-data']")).isDisplayed()){
			WebElement welcomePage =driverValue.findElement(By.xpath("//div[@class='no-data']/h1"));
			String mainText =welcomePage.getText().toString();
			log.info(mainText);
			log.info("---------It is a Fresh Account.You can add data------- ");
			//find file upload
			(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.Addnewfileid))).click();
			WebElement uploadId= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");
			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","homePageFile.csv");
			String fileLocation =pathToFile.toString();
			uploadId.sendKeys(fileLocation);
			this.uploadedFileStatus();
			Thread.sleep(2000);
			//new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'first-landing-item highlight')]")));
			//driverValue.get("https://qa.mammoth.io/#/landing?onboarding=1");
			//check if on boarding section is present
			/*if(isElementPresent(By.xpath("//div[contains(@class,'introjs-tooltip product-tour')]"))){
				log.info("---------------------On boarding section is present----------------");
				log.info("Please click on exit ");
				(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class,'introjs-skipbutton')]"))).click();
				log.info("---------------------Clicked on exit----------------");
				Thread.sleep(2000);
			}
			 */
		}

		else{

			log.info("---------There is already data on account landing you can proceed further------- ");	

		}



		return driverValue;
	}

	boolean isElementPresent(By by) {
	    //WebDriver driver;
	  //  driver = DriverInitializer.getDriver();
	 //   driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  
		Boolean isPresent = driverValue.findElements(by).size() > 0;
		return isPresent;
		/*try  
		{  
			driverValue.findElement(by);  
			return true;  
		}  
		catch(NoSuchElementException e)  
		{  
			return false;  
		}  
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);  
		}  */
	}
	public void viewCreationLoader(){
		try{
			driverValue.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);  
			while(driverValue.findElement(By.xpath("//small-inline-loader[@class='small-inline-loader']")).isDisplayed()){
				log.info("On creating views loader displayed to user");
			}
		}
		catch(Exception e){
			log.info("--------------------------------Loader gets invisible now-------------------");
		}
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
		}  

	}

	public void addNoteInPipline(String note) throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(ConstString.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		//Click on pipeline dropdown
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.anyRuleDropDown))).click();	
		//Find Add Note In pipeline
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addNoteInDropdown))).click();

		//Find Add note title window
		WebElement addnoteTitle = (new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.addNoteTitleWindow)));
		Assert.assertTrue(addnoteTitle.isDisplayed(), "Rename Step title window don't get displayed ");
		//Write something in text area
		driverValue.findElement(By.xpath(ConstString.textAreaAddNote)).sendKeys(note);
		Thread.sleep(500);
		//click on done button
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.doneInAddNote))).click();
		Assert.assertTrue((new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.addedNoteInPipeline))).isDisplayed(), " Added Note is not present");
		String noteContent =(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.addedNoteInPipeline))).getText();
		Assert.assertTrue(!noteContent.isEmpty(), "Note couldn't be added");
		log.info("Added note is : "+noteContent);
	}

	public void renameInPipeline(String name) throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(1000);
		act.moveToElement(driverValue.findElement(By.xpath(ConstString.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		//Click on pipeline dropdown
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.anyRuleDropDown))).click();
		//Click on rename
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.renameInTopBottomDrop))).click();
		//Find Rename title window
		WebElement renameTitle = (new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.renameTitleTopBottom)));
		Assert.assertTrue(renameTitle.isDisplayed(), "Rename Step title window don't get displayed ");
		driverValue.findElement(By.xpath(ConstString.renameInputTopBottom)).sendKeys(name);
		//click on rename button
		driverValue.findElement(By.xpath(ConstString.renameButtonTopBottom)).click();
		log.info("-----------Clicked on rename button---------");

	}
	public void deleteRuleInPipeline() throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(ConstString.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.anyRuleDropDown))).click();
		//delete rule from pipeline
		WebElement deleteInDropDown2= driverValue.findElement(By.xpath(ConstString.deleteInPipeline));
		deleteInDropDown2.click();
		WebElement deleteTaskTitle2 =(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.deletetaskTitle)));
		Assert.assertTrue(deleteTaskTitle2.isDisplayed(), "Delete task window don't get opened");
		driverValue.findElement(By.xpath(ConstString.deleteButtonInWindow)).click();
		WebDriverWait waitForDeletionMsgOnTop =new WebDriverWait(driverValue,10) ;		
		WebElement deleteDSMsg=	 waitForDeletionMsgOnTop.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.deleteMsgOnTop)));
		String Msg =deleteDSMsg.getText().toString();
		log.info(Msg);
		log.info("Rule deleted from pipeline");
		this.overlay();

	}

	public void stepDescrition() throws InterruptedException{

		//find added columns details
		Actions act =new Actions(driverValue);
		act.moveToElement(driverValue.findElement(By.xpath(ConstString.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(ConstString.infoIconInPipeline))).build().perform();
		Thread.sleep(500);
		String ruleDetails=new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.stepDescriptionInPipeline))).getText();
		Assert.assertTrue(!ruleDetails.isEmpty(), "There is no  detail for this rule");
		log.info("Added Rule details  :  "  +ruleDetails);


	}
	public  WebDriver group() throws InterruptedException{


		(new WebDriverWait(driverValue,15)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addtaskButton))).click();

		(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.groupPivot))).click();
		Assert.assertTrue((new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.groupPivotTitle))).isDisplayed(), "Group/Pivot title window don't get displayed");

		List <WebElement> groupByColumnList = (new WebDriverWait(driverValue, 10)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ConstString.groupByColList)));
		for(WebElement we :groupByColumnList){
			if(we.getText().toString().equals("Engine")){
				we.findElement(By.xpath("./a")).click();
				Thread.sleep(500);

			}

			else if(we.getText().toString().equals("Keyword Group")){
				we.findElement(By.xpath("./a")).click();


			}

		}

		//Assert that Aggregate By section  is present
		Assert.assertTrue(driverValue.findElement(By.xpath(ConstString.aggregateBySection)).isDisplayed(), "And aggregate by section is not present");
		Wait<WebDriver> waitForData=	this.fluentWait();
		List <WebElement> aggregateByColumnList =waitForData.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ConstString.aggregateByColList)));
		for(WebElement webElement :aggregateByColumnList){
			String s=webElement.getText().toString();

			if(webElement.getText().toString().contains("Imps")){
				webElement.findElement(By.xpath("./li[2]/a")).click();	      
			}

		}	
		List <WebElement> resultSection = waitForData.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ConstString.resultColumnList)));
		for(WebElement we :resultSection){
			String s =we.getText().toString();
			log.info(s);
			if(we.getText().toString().equals(".2 ,")){
				we.findElement(By.xpath(ConstString.decimalBoxresultSection)).click();
				Assert.assertTrue(driverValue.findElement(By.xpath(ConstString.decimalPlaceWindow)).isDisplayed(), "Decimal places not present to change");
				Select select =new Select(driverValue.findElement(By.xpath(ConstString.decimalDropDownInWindow)));
				select.selectByIndex(1);

				driverValue.findElement(By.xpath(ConstString.goButtonDecimalWindow)).click();	

			}

		}
		driverValue.findElement(By.xpath(ConstString.applyButtonInRemove)).click();
		return driverValue;

	}
	public void uploadedFileStatus(){
		long oldTimeForProgress=System.currentTimeMillis();
		boolean progressbarVisibility=	isElementPresent(By.xpath("//div[@class='progress progress-mini']"));//if visible
		if(progressbarVisibility==true){
			try{
				driverValue.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);  

			}
			catch(Exception e){
				log.info("--------------------------------Progress bar gets invisible now-------------------");
			}
			try{
				long oldTimeForProcessing=System.currentTimeMillis();
				driverValue.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);  
				while(driverValue.findElement(By.xpath("//div[@class='ds-title processing']//div[@class='status-wrapper']/div[contains(@class,'status')]/p")).isDisplayed()){
					log.info("Upload processing messages is there");
					if(System.currentTimeMillis()- oldTimeForProcessing>120*1000){
						Assert.fail("---------------Looking like file got stuck in processing messages----------------");

					}
				}
			}
			catch(Exception e){
				log.info("--------------------------------Upload Processing messages gets invisible now-------------------");
			}
			finally  
			{  
				driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
			}  


		}
		else{
			try{
				long oldTimeForProcessing=System.currentTimeMillis();
				driverValue.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);  
				while(driverValue.findElement(By.xpath("//div[contains(@class,'ds-single processing') and not (contains(@class,'status-attention'))]")).isDisplayed()){
					log.info("Upload processing messages is there");
					if(System.currentTimeMillis()- oldTimeForProcessing>120*1000){
						Assert.fail("---------------Looking like file got stuck in processing messages----------------");

					}
				}
			}
			catch(Exception e){
				log.info("--------------------------------Processing messages gets invisible now-------------------");
			}
			finally  
			{  
				driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
			} 
		}
	}
	public void previewSection(){
		try{
			driverValue.manage().timeouts().implicitlyWait(4, TimeUnit.MILLISECONDS);
			(new WebDriverWait(driverValue, 7)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='prev-body']//section[contains(@class,'preview')]")));
		}
		catch(Exception e){
			e.printStackTrace()	;
			(new WebDriverWait(driverValue, 7)).until(ExpectedConditions.elementToBeClickable(By.xpath("//h5//span[@class='ng-binding']"))).click();

		}
		finally
		{
			driverValue.manage().timeouts().implicitlyWait(8, TimeUnit.MILLISECONDS);
		}
	}
	public  WebDriver groupUploadFile() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","GroupPivotFile.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);


			log.info("Uploading  a file named : GroupPivotFile.csv");


		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();
		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.groupPivotFileId)));

		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}
		return driverValue;

	}

	public WebDriver topBottomUploadFile() throws InterruptedException{
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.Addnewbuttonid))).click();
		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","CSVoffice_main.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);
			log.info("Uploading  a file named : "+"CSVoffice_main.csv");
		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();
		this.uploadedFileStatus();
		this.previewSection();
		return driverValue;

	}

	public  WebDriver filter() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);


			log.info("Uploading  a file named : FilterFile.csv");


		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();

		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.filterFileID)));

		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}
		return driverValue;

	}	

	public  WebDriver ColumnFileUpload() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","ColumnFunctionFile.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);


			log.info("Uploading  a file named : ColumnFunctionFile.csv");


		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}
		this.uploadedFileStatus();
		this.previewSection();
		//driverValue.navigate().refresh();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.columnFunctionFileId)));
		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}
		return driverValue;

	}	

	public  WebDriver insertCustomValFileUpload() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","InsertCustomValue.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);
			log.info("Uploading  a file named : InsertCustomValue.csv");
		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();
		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.insertCustomFileId)));

		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}
		return driverValue;

	}	
	public  WebDriver extractStringUloadFile() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","ExtractString.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);
			log.info("Uploading  a file named : ExtractString.csv");

		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();
		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.extractStringFileID)));
		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}

		return driverValue;

	}	
	public  WebDriver addSubtractDateFileUpload() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","AddSubDateFile.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);

			log.info("Uploading  a file named : AddSubDateFile.csv");

		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}

		this.uploadedFileStatus();
		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.addsubtractFileId)));
		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}
		return driverValue;

	}	
	public ArrayList<Integer> impColResultantDataInFilter(){
		//fetch resultant column data
		Wait<WebDriver> waitForData=	this.fluentWait();
		int sizeOfResultantColumn=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.impColId))).size();
		ArrayList<Integer> updatedarrayOfImp =new ArrayList<Integer>();	
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}
				for(int findCount=0;findCount<2;findCount++){
					try{
						columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.impColId))).get(j).getText().toString();
						log.info("Element found") ;
						break;
					}
					catch(Exception e){
						e.getMessage();
					}	
				}
				updatedarrayOfImp.add(Integer.parseInt(columnData));

			}
		}
		return updatedarrayOfImp;
	}
	public ArrayList<String> engineColResultantDataInFilter(){
		Wait<WebDriver> waitForData=	this.fluentWait();
		//fetch resultant column data
		List<WebElement> updatedEngineCol =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.engineColumn)));
		int sizeOfResultantColumn=updatedEngineCol.size();
		ArrayList<String> updatedarrayOfEngine =new ArrayList<String>();
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}


				columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.engineColumn))).get(j).getText().toString();
				updatedarrayOfEngine.add(columnData);

			}
		}
		return updatedarrayOfEngine;
	}
	public ArrayList<String> dateColResultantDataInFilter(){
		Wait<WebDriver> waitForData=	this.fluentWait();
		//fetch resultant column data
		List<WebElement> updatedDateCol =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.updatedDateCol)));
		int sizeOfResultantColumn=updatedDateCol.size();
		ArrayList<String> updatedarrayOfDate =new ArrayList<String>();	
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}

				columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.updatedDateCol))).get(j).getText().toString();
				log.info("Element found") ;
				updatedarrayOfDate.add(columnData);

			}
		}
		return updatedarrayOfDate;
	}

	public   ArrayList<Integer> commonNumericFilter(String name) throws InterruptedException{

		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
		String filelocation =pathToFile.toString();
		ArrayList<Integer> arrayOfImp =new ArrayList<Integer>();

		//fetch resultant column data
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 
			ArrayList<String> impColArrayInString=new ArrayList<String>();
			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] myArray=list.get(i); //getting csv file data row wise
					//fetching 7 th position( Imp column) data from i th row and store it to an array list
					impColArrayInString.add(myArray[7]);
				}

			}

			for(String value:impColArrayInString){
				if(value.equals("")){
					value="-1";
				}
				int intValue =Integer.parseInt(value);
				arrayOfImp.add(intValue);
			}

		}

		catch(Exception e){
			e.printStackTrace();

		}
		ArrayList<Integer> returnedArrayOfImp =new ArrayList<Integer>();
		int smallest = arrayOfImp.get(0);
		int largest =arrayOfImp.get(0);

		if(MaxMinImpFlag==1){
			for(int i=0;i<arrayOfImp.size();i++){

				if(!(i==20)){
					if(( arrayOfImp.get(i+1)> largest)&&!arrayOfImp.get(i+1).equals(-1)){
						largest = arrayOfImp.get(i+1);

					}

					else if ((arrayOfImp.get(i+1)<smallest)&&!arrayOfImp.get(i+1).equals(-1)){
						smallest = arrayOfImp.get(i+1);
					}
				}

			}	

			log.info("Largest Number In Imp is : " + largest);
			log.info("Smallest Number in Imp is : " + smallest);

		}

		for(int i=0;i<arrayOfImp.size();i++){

			switch (name){
			case "equality" :
				if(arrayOfImp.get(i).equals(1)){

					returnedArrayOfImp.add(arrayOfImp.get(i));
				}
				break;

			case "unequal":
				if(!arrayOfImp.get(i).equals(1)){//&&!arrayOfImp.get(i).equals(-1)){ //Imp col value equal to -1 will not considered here because it is a replacement of null value
					returnedArrayOfImp.add(arrayOfImp.get(i));
				}
				break;


			case "less_than_Value" :
				if(!arrayOfImp.get(i).equals(-1)){
					int k=	arrayOfImp.get(i).compareTo(NumColLessGreaterFilter.value);

					if(k==-1){ // if value return -1 means it is less than compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
				}
				break;

			case "Greater_than_Value":
				if(!arrayOfImp.get(i).equals(-1)){
					int j=	arrayOfImp.get(i).compareTo(NumColLessGreaterFilter.value); // if value return 1 means it is greater than compared one value
					if(j==1){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
				}
				break;

			case "less_than_Or_Equal_Value":
				if(!arrayOfImp.get(i).equals(-1)){
					int n=	arrayOfImp.get(i).compareTo(NumColLessGreaterEqual.value);
					if(n==-1){ // if value return -1 means it is less than compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
					else if(n==0){ //if value return is 0 means value is equal to compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));	
					}
				}
				break;

			case "Greater_than_Or_Equal_Value":
				if(!arrayOfImp.get(i).equals(-1)){
					int p=	arrayOfImp.get(i).compareTo(NumColLessGreaterEqual.value);
					if(p==1){ // if value return 1 means it is greater than compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
					else if(p==0){//if value return is 0 means value is equal to compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));	
					}
				}
				break;
			case "Empty_Value":

				if(arrayOfImp.get(i).equals(-1)){ 


					returnedArrayOfImp.add(arrayOfImp.get(i));

				}

				break;	
			case "Is_Not_Empty_Value":

				if(!arrayOfImp.get(i).equals(-1)){ 

					returnedArrayOfImp.add(arrayOfImp.get(i));
				}

				break;

			case "Is_the_minimum_value"	:


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((arrayOfImp.get(i)==smallest)){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}
				break;

			case "Is_Not_the_minimum_value"	:


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((!(arrayOfImp.get(i)==smallest))){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}


				break;
			case "Is_the_maximum_value"	:


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((arrayOfImp.get(i)==largest)){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}
				break;

			case "Is_Not_the_maximum_value"	:


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((!(arrayOfImp.get(i)==largest))){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}

				break;

			case	"In_Between":

				if(!arrayOfImp.get(i).equals(-1)){ 

					if(arrayOfImp.get(i)>=FilterNumInBetween.value1&&arrayOfImp.get(i)<=FilterNumInBetween.value2){

						returnedArrayOfImp.add(arrayOfImp.get(i));		

					}

				}

			}

		}	
		return returnedArrayOfImp;

	}
	public   ArrayList<String> commonTextFilter(String name) throws InterruptedException{

		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
		String filelocation =pathToFile.toString();
		ArrayList<String> arrayOfEngine=new ArrayList<String>();
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 

			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] filterFileArray=list.get(i); //getting csv file data row wise
					//fetching 7 th position( Engine column) data from i th row and store it to an array list
					arrayOfEngine.add(filterFileArray[3]);
				}

			}
		}

		catch(Exception e){
			e.printStackTrace();

		}

		ArrayList<String> returnedArrayOfEngine =new ArrayList<String>();

		for(int i=0;i<arrayOfEngine.size();i++){

			switch (name){
			case "Has_Empty_Value":

				if(arrayOfEngine.get(i).equals("")){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}

				break;	
			case "Does_Not_Have_Empty_Value":

				if(!arrayOfEngine.get(i).equals("")){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}

				break;

			case "End_With_Value":

				if(arrayOfEngine.get(i).endsWith(FilterTextColEndCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}
				break;	
			case "Does_Not_End_With_Value":

				if(!arrayOfEngine.get(i).endsWith(FilterTextColEndCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));

				}

				break;	
			case "Start_With_Value":

				if(arrayOfEngine.get(i).startsWith(FilterTextColStartWithCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}

				break;	
			case "Does_Not_Start_With_Value":

				if(!arrayOfEngine.get(i).contains(FilterTextColStartWithCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));

				}

				break;	
			case "Contains":

				if(arrayOfEngine.get(i).contains(FilterTextColContainCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}

				break;	
			case "Does_Not_Contains":

				if(!arrayOfEngine.get(i).contains(FilterTextColContainCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));
				}

				break;	
			case "Is_Equal_to":

				if(arrayOfEngine.get(i).equalsIgnoreCase(FilterTextColEqualityCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));

				}

				break;	
			case "Is_Not_Equal_to":

				if(!arrayOfEngine.get(i).equalsIgnoreCase(FilterTextColEqualityCase.valuestr)){ 
					returnedArrayOfEngine.add(arrayOfEngine.get(i));

				}

				break;	
			}
		}

		return returnedArrayOfEngine;	
	}

	public   ArrayList<Date> commonDateFilter(String name) throws InterruptedException, java.text.ParseException{
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
		String filelocation =pathToFile.toString();
		ArrayList<String> dateColumn=new ArrayList<String>();
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 

			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] filterFileArray=list.get(i); //getting csv file data row wise
					//fetching 2nd position( Date column) data from i th row and store it to an array list
					dateColumn.add(filterFileArray[2]);
				}

			}
		}

		catch(Exception e){
			e.printStackTrace();

		}

		ArrayList<String> arrayOfDateString =new ArrayList<String>();
		for(String dateObj:dateColumn){
			arrayOfDateString.add(dateObj);		
		}
		ArrayList<Date> returnedArrayOfDate =new ArrayList<Date>();
		ArrayList<Date> dateArray =new ArrayList<Date>();
		//Convert string array of date to Date array
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy

		if (name.equals("Is_Not_Equal_to")){   // is not equal to case of date column consider null value as well.
			for (String dateString : arrayOfDateString) {
				if(null!= dateString&&dateString.length()!=0)
				{
					//long date_l=Date.parse(dateString);  //getting date in long format 1222626600000 

					Date date=new Date(dateString);  // getting date in Mon Sep 29 00:00:00 IST 2008 format
					String dateText = sdfDate.format(date);  

					dateArray.add(sdfDate.parse(dateText)); //got date array in  [Mon Sep 29 00:00:00 IST 2008]
				}
				else // if date value is null
				{
					Date date=new Date(System.currentTimeMillis());  // getting current date which will be added in place of null date
					String dateText = sdfDate.format(date);  
					dateArray.add(null);
				}
			}
		}
		else {

			for (String dateString : arrayOfDateString) {
				if(null!= dateString&&dateString.length()!=0)
				{
					long date_l=Date.parse(dateString);  //getting date in long format 1222626600000 

					Date date=new Date(date_l);  // getting date in Mon Sep 29 00:00:00 IST 2008 format
					String dateText = sdfDate.format(date);  

					dateArray.add(sdfDate.parse(dateText)); //got date array in  [Mon Sep 29 00:00:00 IST 2008]
				} 
			}
		} 
		String	max=null;
		String	min=null;

		Date datemax=null;
		Date datemin=null;

		if(MaxMinDateFlag==1){

			//Sorting of date that will sort dates in ascending order
			Collections.sort(dateArray);	
			//now find last element of array which will be minimum value of date
			max	=dateArray.get(dateArray.size()-1).toString();
			min=dateArray.get(0).toString();
			log.info("Largest Date value is : " + max);
			log.info("Smallest Date valueis : " + min);
			SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy"); 
			datemax = (Date)formatter.parse(max);
			datemin = (Date)formatter.parse(min);
		}

		for(int i=0;i<dateArray.size();i++){

			switch (name){
			case "Is_Equal_to":
				if(dateArray.get(i).equals(sdfDate.parse(FilterDateColEqualCase.valuestr))){
					returnedArrayOfDate.add(dateArray.get(i));
				}


				break;	

			case "Is_Not_Equal_to":
				if(dateArray.get(i)==null){ //here we want to add null value in date array
					returnedArrayOfDate.add(dateArray.get(i));
				}
				else if(dateArray.get(i)!=null&&!dateArray.get(i).equals(sdfDate.parse(FilterDateColEqualCase.valuestr))){ 


					returnedArrayOfDate.add(dateArray.get(i));

				}

				break;	

			case "Is_the_Maximum":

				if (dateArray.get(i).equals(datemax)){
					returnedArrayOfDate.add(dateArray.get(i));
				}

				break;	


			case "Is_the_Minimum":

				if (dateArray.get(i).equals(datemin)){
					returnedArrayOfDate.add(dateArray.get(i));
				}


				break;	

			case "Less_Than":

				int result    =	dateArray.get(i).compareTo(sdfDate.parse(FilterDateColLessGreaterCase.valuestr));
				if(result <0){
					returnedArrayOfDate.add(dateArray.get(i));
				}

				break;

			case "Greater_Than":

				int results    =	dateArray.get(i).compareTo(sdfDate.parse(FilterDateColLessGreaterCase.valuestr));
				if(results >0){
					returnedArrayOfDate.add(dateArray.get(i));
				}	
				break;
			case "Less_Than_Or_Equal_to":

				int response    =	dateArray.get(i).compareTo(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr));
				if(response <0 ||dateArray.get(i).equals(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr))){
					returnedArrayOfDate.add(dateArray.get(i));
				}

				break;

			case "Greater_Than_Or_Equal_to":

				int responseVal    =	dateArray.get(i).compareTo(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr));
				if(responseVal >0||dateArray.get(i).equals(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr))){
					returnedArrayOfDate.add(dateArray.get(i));
				}	
				break;

			case "In_Between_Case":

				if((dateArray.get(i).after(sdfDate.parse(FilterDateInBetween.valuestr1))||dateArray.get(i).equals(sdfDate.parse(FilterDateInBetween.valuestr1))) && (dateArray.get(i).before(sdfDate.parse(FilterDateInBetween.valuestr2))||dateArray.get(i).equals(sdfDate.parse(FilterDateInBetween.valuestr2)))){


					returnedArrayOfDate.add(dateArray.get(i));	
				}
			}
		}

		return returnedArrayOfDate;	

	}

	public  WebDriver mathRuleFileUpload() throws InterruptedException{
		//Uploading a file

		try{
			WebElement file1= driverValue.findElement(By.xpath(ConstString.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;	
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");

			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","MathRule.csv");
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);


			log.info("Uploading  a file named : MathRule.csv");


		}
		catch(Exception e){
			log.severe("Exception occure:File not uploaded"+e);
		}
		this.uploadedFileStatus();
		this.previewSection();
		WebElement uploadedfile =new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.mathRuleFileId)));

		winHandleBefore = driverValue.getWindowHandle();
		Actions action = new Actions(driverValue);
		// Store the current window handle

		action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driverValue.switchTo().window(winHandle);
			}
		}

		return driverValue;

	}
	public WebDriver mathRuleFunction(){
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","MathRule.csv");
		String filelocation =pathToFile.toString();
		ArrayList<Integer> arrayOfImpMathRule =new ArrayList<Integer>();
		int sumOfImp=0,sumOfClicks=0;
		float sumOfAvg=0;
		ArrayList<Integer> arrayOfClicksMathRule =new ArrayList<Integer>();
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 


			ArrayList<String> avgPosColArrayInString=new ArrayList<String>();	
			ArrayList<String> keywordGroup=new ArrayList<String>();
			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] fileArray=list.get(i); //getting csv file data row wise
					impColArrayInString.add(fileArray[2]);	//fetching 2 nd position( Imp column) data from i th row and store it to an array list
					clicksColArrayInString.add(fileArray[8]); //fetch clicks col data from csv file
					avgPosColArrayInString.add(fileArray[9]); //fetch avgPos col data from csv file
					keywordGroup.add(fileArray[5]);//fetch keyword Group col data from csv file
					arrayOfEngineMathRule.add(fileArray[4]);//fetch keyword Group col data from csv file
					arrayOfDateInString.add(fileArray[3]);//fetch date
					arrayOfCampaignMathRule.add(fileArray[0]);//fetch date
				}

			}
			for(String value:impColArrayInString){
				if(value.equals("")){
					value="0";
				}
				int intValue =Integer.parseInt(value);
				if(intValue!=0){
					arrayOfImpMathRule.add(intValue);
				}
			}
			for(String value:clicksColArrayInString){
				if(value.equals("")){
					value="0";
				}
				int intValue =Integer.parseInt(value);
				if(intValue!=0){
					arrayOfClicksMathRule.add(intValue);
				}
			}
			for(String value:avgPosColArrayInString){
				if(value.equals("")){
					value="0";
				}
				Float floatValue =Float.parseFloat(value);
				if(floatValue!=0){
					arrayOfAvgPosMathRule.add(floatValue);
				}
			}
			for(String value:keywordGroup){

				arrayOfKeywordGroupMathRule.add(value);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		for(int impVal:arrayOfImpMathRule){ //calculating sum of imp from csv file
			sumOfImp =sumOfImp+impVal;
		}
		for(int clicksVal:arrayOfClicksMathRule){//calculating sum of clicks from csv file
			sumOfClicks =sumOfClicks+clicksVal;
		}
		for(float avgPosVal:arrayOfAvgPosMathRule){//calculating sum of avgPos from csv file
			sumOfAvg =sumOfAvg+avgPosVal;
		}

		integerValueOfSumImp = (int) sumOfImp;
		log.info("Sum of Imp column values  in integer  is  :  " +integerValueOfSumImp);
		integerValueOfClicks = (int) sumOfClicks;
		log.info("Sum of Clicks column values  in integer  is  :  " +integerValueOfClicks);
		log.info("Sum of Avgpos column values  in double  is  :  " +sumOfAvg);

		integerValueOfAvgPos = (int) sumOfAvg;
		log.info("Sum of AvgPos column values  in integer  is  :  " +integerValueOfAvgPos);

		return driverValue;
	}

	public ArrayList<String> extractStringFunction(Integer ID){
		ExtractStringMoreCase extrct =new ExtractStringMoreCase();
		String specificPosition = extrct.positionToStart;
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","ExtractString.csv");
		String filelocation =pathToFile.toString();
		ArrayList<String> arrayOfCampaign =new ArrayList<String>();

		try {
			// extract string csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 
			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] fileArray=list.get(i); //getting csv file data row wise
					arrayOfCampaign.add(fileArray[0]);	//fetching 2 nd position( campaign column) data from i th row and store it to an array list

				}

			}

		}
		catch(Exception e){
			e.printStackTrace();
		}

		String[] splited =null;
		ArrayList<String> campaignContentAfterExtrct =new ArrayList<String>();

		for(int i=0;i<arrayOfCampaign.size();i++){
			int stringLength =arrayOfCampaign.get(i).length();
			int startPosition =1;

			switch(ID){
			// all left of  a position 3
			case 1:
				campaignContentAfterExtrct.add(StringUtils.left(arrayOfCampaign.get(i), Integer.parseInt(specificPosition)-startPosition));
				//campaignContentAfterExtrct.add(arrayOfCampaign.get(i).substring(1-1, 1));
				log.info(""+campaignContentAfterExtrct);
				break;
			case 2:
				// specific 2 left of  a position  4 
				//campaignContentAfterExtrct.add(StringUtils.left(arrayOfCampaign.get(i), Integer.parseInt(specificPosition)-2));
				campaignContentAfterExtrct.add(arrayOfCampaign.get(i).substring(1,3));
				break;
			case 3:
				// all right  of  a position 2
				campaignContentAfterExtrct.add(StringUtils.right(arrayOfCampaign.get(i), stringLength-Integer.parseInt(specificPosition)));
				//campaignContentAfterExtrct.add(arrayOfCampaign.get(i).substring(1, stringLength-1));
				log.info(""+campaignContentAfterExtrct);
				break;
			case 4:
				// specific 2 right of  a position 4
				campaignContentAfterExtrct.add(arrayOfCampaign.get(i).substring(4,6));
				//campaignContentAfterExtrct.add(StringUtils.right(arrayOfCampaign.get(i), Integer.parseInt(specificPosition)-startPosition));
				break;
			} 
		}
		return campaignContentAfterExtrct;
	}

	public   ArrayList<String> extractDateFunctions(String name) throws InterruptedException, java.text.ParseException{

		//fetching source file date column content
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","AddSubDateFile.csv");
		String filelocation =pathToFile.toString();
		ArrayList<String> arrayOfTransactionDate =new ArrayList<String>();
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 

			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] fileArray=list.get(i); //getting csv file data row wise
					//fetching 1st position( Transaction column) data from i th row and store it to an array list
					arrayOfTransactionDate.add(fileArray[0]);
				}

			}
		}

		catch(Exception e){
			e.printStackTrace();

		}

		ArrayList<String> returnedArrayOfDate =new ArrayList<String>();
		ArrayList<Date> dateArray =new ArrayList<Date>();
		//Convert string array of date to Date array
		SimpleDateFormat sdfoutDate = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
		for(String val:arrayOfTransactionDate){
			if(null!= val&&val.length()!=0){
				Date parsedDate=sdfoutDate.parse(val); // getting date in Mon Sep 29 00:00:00 IST 2008 format
				dateArray.add(parsedDate); //added all dates to a date array list
			}
			else{
				Date date=new Date(System.currentTimeMillis());  // getting current date which will be added in place of null date
				String dateText = sdfoutDate.format(date);  
				dateArray.add(null);
			}
		}

		Calendar calender=Calendar.getInstance();	
		Calendar calender2=Calendar.getInstance(Locale.GERMAN);	
		//now extract different values like year,month,day etc from date column array
		for(int i=0;i<dateArray.size();i++){

			switch(name){	

			case "Year (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get year in integer form
					String year=    new SimpleDateFormat("yyyy").format(calender.getTime());
					returnedArrayOfDate.add(year);

				}
				else{
					returnedArrayOfDate.add("");
				}

				break;


			case "Month (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get month as integer form

					int month=(calender.get(Calendar.MONTH))+1;

					returnedArrayOfDate.add(String.valueOf(month));

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;


			case "Month Name (txt)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get month name as text

					String mothName=    new SimpleDateFormat("MMMM").format(calender.getTime());

					returnedArrayOfDate.add(String.valueOf(mothName));

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;


			case "Quarter (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get quarter in text form
					int quarter=(calender.get(Calendar.MONTH)/3)+1;

					returnedArrayOfDate.add(String.valueOf(quarter));

				}
				else{
					returnedArrayOfDate.add("");
				}

				break;


			case "Day of year (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get day of year from date
					int dayofYear=calender.get(Calendar.DAY_OF_YEAR);

					returnedArrayOfDate.add(String.valueOf(dayofYear));

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;


			case "Day (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get day of month in text form
					int dayofMonth=calender.get(Calendar.DAY_OF_MONTH);

					returnedArrayOfDate.add(String.valueOf(dayofMonth));

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;

			case "Week of Year (num)":

				if(dateArray.get(i)!=null){
					calender2.setTime(dateArray.get(i));
					//Get week of year in text form
					int weekOfYear=calender2.get(Calendar.WEEK_OF_YEAR);

					returnedArrayOfDate.add(String.valueOf(weekOfYear));

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;			
			}
		}
		return returnedArrayOfDate;	
	}
	public   ArrayList<String> extractDateHrsMinSec(String name) throws InterruptedException, java.text.ParseException{

		//fetching source file date column content
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Test Files","AddSubDateFile.csv");
		String filelocation =pathToFile.toString();
		ArrayList<String> arrayOfAccountCreated =new ArrayList<String>();
		try {
			// filter csv file containing data
			CSVReader reader = new CSVReader(new FileReader(filelocation));   
			List<String[]> list=reader.readAll(); //read whole csv file 

			for(int i=0;i<list.size();i++)
			{
				if(i>0) //At zero position column header of file,so we won't consider it
				{
					String[] fileArray=list.get(i); //getting csv file data row wise
					//fetching 1st position( Account created column) data from i th row and store it to an array list
					arrayOfAccountCreated.add(fileArray[8]);
				}

			}
		}

		catch(Exception e){
			e.printStackTrace();

		}

		ArrayList<String> returnedArrayOfDate =new ArrayList<String>();
		ArrayList<Date> dateArray =new ArrayList<Date>();
		//Convert string array of date to Date array

		SimpleDateFormat sdfoutDate = new SimpleDateFormat("MM/dd/yy HH:mm");

		for(String val:arrayOfAccountCreated){
			if(null!= val&&val.length()!=0){
				Date parsedDate=sdfoutDate.parse(val); // getting date in specified format
				dateArray.add(parsedDate); //added all dates to a date array list
			}
			else{
				Date date=new Date(System.currentTimeMillis());  // getting current date which will be added in place of null date
				String dateText = sdfoutDate.format(date);  
				dateArray.add(null);
			}
		}
		Calendar calender=Calendar.getInstance();	
		//now extract different values like hrs,min,sec from date column(account created) array
		for(int i=0;i<dateArray.size();i++){

			switch(name){	

			case "Hour (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get hrs
					int hour=    calender.get(calender.HOUR_OF_DAY);
					String hrValInString=String.valueOf(hour);
					returnedArrayOfDate.add(hrValInString);

				}
				else{
					returnedArrayOfDate.add("");
				}

				break;


			case "Minutes (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get min
					int min=    calender.get(calender.MINUTE);
					String minuteValInString=String.valueOf(min);
					returnedArrayOfDate.add(minuteValInString);
				}
				else{
					returnedArrayOfDate.add("");
				}
				break;


			case "Seconds (num)":
				if(dateArray.get(i)!=null){
					calender.setTime(dateArray.get(i));
					//Get sec
					int sec=    calender.get(calender.SECOND);
					String secondValInString=String.valueOf(sec);
					returnedArrayOfDate.add(secondValInString);

				}
				else{
					returnedArrayOfDate.add("");
				}
				break;
			}
		}
		return returnedArrayOfDate;	
	}

	public String exportMenuDownloadCSVCommon() throws InterruptedException, IOException{
		//check for the current count in notification area
		//String notificationCountBeforeExport=new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.countOfNotification))).getText();
		//int countBeforeExport=Integer.parseInt(notificationCountBeforeExport);
		new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.exportButtonBottom))).click();
		Thread.sleep(500);
		new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.applyButtonInRemove))).click();
		/*Assert.assertTrue(new WebDriverWait(driverValue, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.sharedAsNotificationBox))).isDisplayed(),"CSV is reday to download message don't get displayed");
		log.info("----------------CSV is reday to download message displayed to user-------------");

		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.sharedAsNotificationBox))).click();
		Thread.sleep(4000);*/
		//check for the current count in notification area
		/*String notificationCountAfterExport=new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.countOfNotification))).getText();
		int countAfterExport=Integer.parseInt(notificationCountAfterExport);
		Assert.assertEquals(countAfterExport, (countBeforeExport+1), "The count in notification area doesn't go up by one count.");
		log.info("-----The count in notification area goes up by one count.---------------");
		Thread.sleep(3000);	
		new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.countOfNotification))).click();*/
		String notificationContent=new WebDriverWait(driverValue,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.notificationUnreadContent))).getText();
		log.info("Notification contains message :"+notificationContent);
		Thread.sleep(2000);

		Assert.assertTrue(new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.clickHereLinkInNotification))).isDisplayed(), "The notification doesn't contain link to the download csv at here");
		//click on link to download csv
		new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.clickHereLinkInNotification))).click();
		String name=this.getDownloadedDocumentName(this.downloadFilepath,"csv");
		log.info("Downloaded file name is------------- ------------ "+name);
		Assert.assertTrue(!name.isEmpty(), "Downloaded file name is empty,Download gets failed");
		return name;
	}

	public String downloadAnyDS() throws InterruptedException, IOException{

		//Load URL to check reset password link has received
		driverValue.get("https://www.gmail.com");
		if ((this.browsers).equals("2")) { //if browser is chrome

			try{
				driverValue.findElement(By.xpath(ConstString.signInLinkForChrome));
				driverValue.findElement(By.xpath(ConstString.signInLinkForChrome)).click();
			}
			catch(Exception e){
				log.severe("Exception occured: Link to signin in chrome is not present")	;

			}

		}
		log.info("----------------Gmail new look is there-------------------");
		//Enter your email id
		driverValue.findElement(By.xpath(ConstString.userIdOfGmail)).sendKeys(secondaryUser);			
		//click on next button
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.nextIdOfGmail))).click();
		Thread.sleep(500);
		//Enter password
		(new WebDriverWait(driverValue, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.passIdOfGmail))).sendKeys(seleniumAccountEmailPassword);

		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.nextIdOfGmail))).click(); //click on sign in	
		//Assert that "Mail" logo is present on left corner
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try {
				Assert.assertTrue(isElementPresent(By.xpath(ConstString.mailLogo)));
				break;
			}
			catch(Exception e){
				log.severe("Exception occure: Mail logo is not present");
			}
		}
		//Wait for mail present in your inbox with subject line "Reset your Password".
		WebElement recievedMailToDownload = (new WebDriverWait(driverValue, 40)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ConstString.recievedMailToDownload))));
		// open received mail by clicking it
		recievedMailToDownload.click();	
		//Find  link received in opened mail
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.recievedLinkToDownload))).click();
		String nameOfDownloadedDS=this.getDownloadedDocumentName(this.downloadFilepath,"csv");
		log.info("Downloaded file name is------------- ------------ "+nameOfDownloadedDS);
		Assert.assertTrue(!nameOfDownloadedDS.isEmpty(), "Downloaded file name is empty,Download gets failed");
		log.info("-----------Now Delete all mails----------");
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.goBackArrowInInbox))).click();
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.checkBoxInGmail))).click(); //check on select all mail
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.deleteMailInGmail))).click(); //delete now
		Thread.sleep(2000);
		driverValue.findElement(By.xpath(ConstString.gmailCornerSetting)).click();// upper right corner for logout setting in Gmail
		driverValue.findElement(By.xpath(ConstString.gmailLogout)).click(); // click on sign out from gmail
		log.info("---Logout successfully from personal account----"+"\t");
		return nameOfDownloadedDS;
	}

	public String getDownloadedDocumentName(String downloadPath,String fileExtension ) throws IOException{
		String downloadedFileName=null;
		boolean valid=true;
		boolean found=false;

		//default timeout in seconds
		long timeOut = 50; 
		try {
			Path downloadFolderPath=Paths.get(downloadPath);

			WatchService watchService=FileSystems.getDefault().newWatchService();
			downloadFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			long startTime=System.currentTimeMillis();
			do 
			{
				WatchKey watchKey;
				watchKey = watchService.poll(timeOut,TimeUnit.SECONDS);
				long currentTime = (System.currentTimeMillis()-startTime)/1000;
				if(currentTime>timeOut)
				{
					log.info("Download operation timed out.. Expected file was not downloaded");
					return downloadedFileName;
				}

				for (WatchEvent<?> event : watchKey.pollEvents())
				{
					WatchEvent.Kind<?> kind = event.kind();

					if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) 
					{
						String fileName = event.context().toString();
						System.out.println("New File Created:" + fileName);
						if(fileName.endsWith(fileExtension))
						{
							downloadedFileName = fileName;
							log.info("Downloaded file found with extension " + fileExtension + ". File name is " + fileName);
							Thread.sleep(500);
							found = true;
							break;
						}
					}
				}
				if(found)
				{
					return downloadedFileName;
				}
				else
				{
					currentTime = (System.currentTimeMillis()-startTime)/1000;
					if(currentTime>timeOut)
					{
						log.info("Failed to download expected file");
						return downloadedFileName;
					}
					valid = watchKey.reset();
				}
			} while (valid);
		} 

		catch (InterruptedException e) 
		{
			log.info("Interrupted error - " + e.getMessage());
			e.printStackTrace();
		}
		catch (NullPointerException e) 
		{
			log.info("Download operation timed out.. Expected file was not downloaded");
		}
		catch (Exception e)
		{
			log.info("Error occured - " + e.getMessage());
			e.printStackTrace();
		}

		return downloadedFileName;
	}
	public void makeConnectionThirdPartyAPIPostgreSql(String postgresqlDBName) throws InterruptedException{
		Wait<WebDriver> waitfordata=this.fluentWait();
		if(isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==false){
			//find  connection
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection)));
		}
		else{
			//click on new connection
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection))).click();	
		}
		Thread.sleep(1000);
		//enter hostname
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.enterHostName))).sendKeys(hostName);
		//enter username
		driverValue.findElement(By.xpath(ConstString.enterUserName)).sendKeys(postgrsqlUser);
		//enter password
		driverValue.findElement(By.xpath(ConstString.enterPassword)).sendKeys(postgresqlPassword);
		driverValue.findElement(By.xpath(ConstString.enterDBName)).sendKeys(postgresqlDBName);
		log.info("Database name entered--------------->  " +postgresqlDBName);
		//}
		//enter display name
		//driverValue.findElement(By.xpath(ConstString.enterDisplayName)).sendKeys("Test");
		//connect to DB button
		driverValue.findElement(By.xpath(ConstString.connectButton)).click();
		this.overlay();
		Thread.sleep(6000);
		Assert.assertTrue(waitfordata.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.queryAreaBlank))).isDisplayed(), "Query area after making connection using DB name don't get displayed");	
	}
	public  void selectDB() throws InterruptedException{
		Wait<WebDriver> waitfordata=this.fluentWait();
		//select sample live db from list
		List<WebElement> dbList=waitfordata.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.databaseList)));
		for(int i=0;i<dbList.size();i++){
			if(dbList.get(i).getText().equals("samplelivedb")){
				dbList.get(i).click();

			}

		}	
		this.overlay();
		JavascriptExecutor jse=	(JavascriptExecutor) driverValue;
		List<WebElement> tableList=waitfordata.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.tableListUnderDB)));
		for(int i=0;i<tableList.size();i++){
			jse.executeScript("arguments[0].scrollIntoView(true);",tableList.get(i));
			if(tableList.get(i).getText().equals(tableName)){
				tableList.get(i).click();
			}
		}	
		Thread.sleep(3000);
		this.overlay();
		new WebDriverWait(driverValue,10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.highlightedQueryArea)));
		//click on run test query
		driverValue.findElement(By.xpath(ConstString.runTestQuery)).click();
		this.overlay();

		//click on next
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.nextButtonInThirdParty))).click();
		Thread.sleep(2000);
		log.info("--------Next button clicked---------");


	}
	public   String[] schedulingFrequencyData() throws InterruptedException{
		WebElement freqCol =(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='slick-column-name' and text()='frequency']")));
		Actions act =new Actions(driverValue);
		act.moveToElement(freqCol).build().perform();
		act.moveToElement(freqCol.findElement(By.xpath(ConstString.columnStatOfAnyCol))).click().build().perform();
		Thread.sleep(2000);
		Assert.assertTrue((new WebDriverWait(driverValue, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.frequencyColumnName))).isDisplayed(), "Frequency Stat is not present");
		Thread.sleep(10000);
		//find hourly count 
		WebElement hourlyCount =driverValue.findElement(By.xpath(ConstString.hourCountInFrequencyStat));
		String numberofHours =hourlyCount.getText().toString();
		String hrs=numberofHours.replaceAll("\\D+","");
		/*Calendar calObj= Calendar.getInstance();
		Date dateObj = calObj.getTime();
		long t= dateObj.getTime();
		Date afterAddingExtraMins=new Date(t + (4 * 60000));//adding 4 minutes to system current time as start time of scheduling 4 minutes exceed than system time
		SimpleDateFormat newFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateInString=newFormat.format(afterAddingExtraMins);
		capturedDateTimeToStoreInCsv=newFormat.parse(currentDateInString);
		log.info("Current time+ 4 minute, when Storing : "+capturedDateTimeToStoreInCsv);*/


		//find minute count
		WebElement minuteCount =driverValue.findElement(By.xpath(ConstString.minuteCountFrequencyStat));
		String numberofMinute =minuteCount.getText().toString();
		String minutes=numberofMinute.replaceAll("\\D+","");
		//find daily count
		WebElement dailyCount =driverValue.findElement(By.xpath(ConstString.dailyCountInFrequencyStat));
		String dailyCountNumber =dailyCount.getText().toString();
		String daily=dailyCountNumber.replaceAll("\\D+","");
		String weekly="";
		try{
			//find weekly count
			WebElement weeklyCount =driverValue.findElement(By.xpath(ConstString.weeklyCountInFrequencyStat));
			String weeklyCountNumber =weeklyCount.getText().toString();
			weekly=weeklyCountNumber.replaceAll("\\D+","");
		}
		catch(Exception e){
			e.printStackTrace();
			log.info("----------------------------Curretly there is no week count in stat---------------");

		}
		log.info(hrs);
		log.info(minutes);
		log.info(daily);
		log.info(weekly);
		driverValue.findElement(By.xpath(ConstString.closeColStats)).click();
		Thread.sleep(2000);
		String[] freqArray={minutes,hrs,daily,weekly};
		//GetDataScheduling data1=new GetDataScheduling("",""," ","","");
		//return numberofHours,numberofMinute,dailyCountNumber,weeklyCountNumber;
		return freqArray;

	}
	public void checkstatusWhenDataPulledInScheduling() throws InterruptedException{
		if(isElementPresent(By.xpath("//div[@class='status-wrapper']/div[@class='status ng-scope']/p[@class='ng-binding ng-scope']"))){
			log.info("Wait for few seconds..........Data set on account landing getting updated as per their schedule");
			Thread.sleep(10000);
			this.uploadedFileStatus();

		}
		else{
			log.info("There is no DS getting updated.You can proceed further");
		}
	}
	public void makeConnectionSSHPostgreSql(String postgresqlDBName) throws InterruptedException{

		Wait<WebDriver> waitfordata=this.fluentWait();
		//click on new connection
		new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection)));
		//enter hostname
		driverValue.findElement(By.xpath(ConstString.enterHostName)).sendKeys(hostName);
		//enter username
		driverValue.findElement(By.xpath(ConstString.enterUserName)).sendKeys(postgrsqlUser);
		//enter password
		driverValue.findElement(By.xpath(ConstString.enterPassword)).sendKeys(postgresqlPassword);

		//enter database name
		driverValue.findElement(By.xpath(ConstString.enterDBName)).sendKeys(postgresqlDBName);
		log.info("Database name entered--------------->  " +postgresqlDBName);

		//enter display name
		driverValue.findElement(By.xpath(ConstString.enterDisplayName)).sendKeys("Test");
		//check on to ssh tunnel
		driverValue.findElement(By.xpath("//input[contains(@id,'ssh_enabled')]")).click();
		//enter proxy host
		driverValue.findElement(By.xpath("//input[contains(@id,'input_proxy_host')]")).sendKeys("testdb.mammoth.io");
		//enter proxy user
		driverValue.findElement(By.xpath("//input[contains(@id,'proxy_user')]")).sendKeys("sdk");
		Select selectAuthType=new Select(new WebDriverWait(driverValue,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@id,'auth_type')]"))));
		selectAuthType.selectByIndex(1);//select Key pair(open SSH)
		JavascriptExecutor jse=(JavascriptExecutor)driverValue;
		jse.executeScript("arguments[0].scrollIntoView(true);",driverValue.findElement(By.xpath("//textarea[contains(@id,'textarea_private_key')]")));

		String privateKey="-----BEGIN RSA PRIVATE KEY-----\n";
		privateKey+="MIIEogIBAAKCAQEA5NkklHaHmleBkEUN6ki+D6MWPJT+wwrXBKWmBjDXnWMSqhA1\n\n";

		privateKey+="XMo1IyFXuHRkqsDiGJtK0lQklXZ2ZneYCCt7Gd50YS6BzZFuzzPXUNw+TeB+q/zB\n\n";

		privateKey+="/0f4xB5k1zkrxOjCPHc1UXGNno8uvS/8lOe4jQop4wD3QrI1pgO8tT5FFJEI8GQu\n\n";

		privateKey+="1BV1LUX4QgWL8W4ijb56r+TYltoG+O9Rkg3Cz/wuc0cJFoeGE3GS6CgsWr1GzcFK\n\n";

		privateKey+="FsRwJqf7CJaYbEARLWKo9KPpIxU4TSjP61dQqv9CZ0IXLt/ZomrIdFbWO0FWZPv0\n\n";

		privateKey+="B81lA6m1Vk4VCZ9ja6ZPLf6vACfkAdnTo39xuQIDAQABAoIBACkKTAYX75+dO84M\n\n";

		privateKey+="pD80xmj1fWJl8f42bnMV5d1MWw6NmuZGu9slJDxlmV6p+PeqWaHCD7wCfQEU2ozr\n\n";

		privateKey+="u6cTbSMpgsN+3KZqA25DRMTqin3a2bysOno2hQVe0gTK6u+IMnU3h6dQbu5VjTM+\n\n";

		privateKey+="bNSjbbRVc0eTf8RFQ1u/ZdM3FJpoRUWdQkKAW1kvOcDzHdm4/8gCRwX6VJUsJKiw\n\n";

		privateKey+="49sZjLxXj6zNtOmyajqvfR8HDBKhwS6PjbKXYqJVDoOSCi0Wjjay4nHlnvJfYom2\n\n";

		privateKey+="+HPPlOrMsUxQX4e3XHBHfvobBCO6JaI+zIO9qwJ5UpUUzFfdew56yCfNY5diK2GZ\n\n";

		privateKey+="b4L5acUCgYEA/MrAQHRMOBEJGBH0XnEZpeHyqNcOa3KgGUKs36snwRHrVcjBMWNr\n\n";

		privateKey+="7uVq37dE+T+Lg9J5dUMe3YvwFS/X1va4F83OWf3q6U4lPCOaxiEP/q91Tw/BKkB+\n\n";

		privateKey+="/8CoLpo9kw78bKfrhuxT8seqWW3fEJr5NobO29m6rg7aeuuAeLGN+UMCgYEA58Ca\n\n";

		privateKey+="+0vBW2WWAfeiNFgf1GhQtYDltHGZyxq/jAS1b/AzZEdXsYHwfHkoRQ00mHadpnv4\n\n";

		privateKey+="9ExQv/tTqXRGqDYb1+Ou23BDUP2Nk83dXQtiHhyy9pviKYwwXJn9sGeXsx1SGtkA\n\n";

		privateKey+="KRfay0DCrI6CuHF8DSMY3mq7gtp6+54eQlg7S1MCgYB7RQtW4t8//96lZCv72b8e\n\n";

		privateKey+="Cv5fou8fKjgz2TWfpECwf2J45HqA8PBzAGCLUz1JJdmMoJglvGNSuaiLTFc+NfUz\n\n";

		privateKey+="dmKHs7BXsImrt9kDgJ/iHAFiWlZhmi3T3Uil4QThr08cVQ4fLyMIoqDJFzDNUiTw\n\n";

		privateKey+="l/BLePL1abzUb/6n3D1VLQKBgAczIJ11+QfR3Bsrikp+Al8i7n71Bi3Y6aI5roG3\n\n";

		privateKey+="E7vqhm4zGTJWixYS3YFnq56g8LzfVRih1lBginTVO+y14Z+PXztJ/Y1ikydoWdwu\n\n";

		privateKey+="Ilo1IgovRZNs24PE9PCqBzh1TLuwyiR8rTtrQ6EAvGbmf6gY0wj13+VszC1M+Duv\n\n";

		privateKey+="aggpAoGALLVyfT/zBTe+hBT49XX3AvhztftOwATc86uAWbAjleKY6l6rUX/50Xa3\n\n";

		privateKey+="Kr2RuPfSGk200pByF5vyx+w5vQQ0pQ0CuKZnTORvasYD1+B/p89uCJLztTQRvR9X\n\n";

		privateKey+="iB21m3ungO8VAcZkkz2EZjdR91sKq1WuPA4epOF47HCD+xkPhqo=\n\n";

		privateKey+="-----END RSA PRIVATE KEY-----";
		new WebDriverWait(driverValue,5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//textarea[contains(@id,'textarea_private_key')]"))).sendKeys(privateKey);

		jse.executeScript("arguments[0].scrollIntoView(true);",new WebDriverWait(driverValue,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.connectButton))));
		//connect to DB button
		new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.connectButton))).click();
		Thread.sleep(4000);
		Assert.assertTrue(waitfordata.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.queryAreaBlank))).isDisplayed(), "Query area after making connection using DB name don't get displayed");
	}
	public void connectionExistenceForSampleLiveDBInAPI(String dbname) throws InterruptedException{
		if(this.isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==true){
			List<WebElement>connectedDBList =new WebDriverWait(driverValue,10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.connectionAvailable)));
			String varEx="";
			String varSam="";
			for(int i=0;i<connectedDBList.size();i++){
				if(connectedDBList.get(i).getText().contains("export")){varEx="export";}
				if(connectedDBList.get(i).getText().contains("sample")){varSam="sample";}

			}
			// if connection name is samplelivedb then click on it among various connection
			if(varSam=="sample"){
				log.info("Connection already exist.You can proceed further");
				//click on samplelivedb connection
				for(int i=0;i<connectedDBList.size();i++){
					if(connectedDBList.get(i).getText().contains("sample")){
						connectedDBList.get(i).click();
						break;
					}
					else if(connectedDBList.get(i).getText().contains("exportdb")){
						log.info("Do nothing with this database.Search again for samplelivedb");
					}
					else{
						Assert.fail("-----------------Required Samplelive databse is not present in list-------------------");
					}

				}
			}
			else if(varEx=="export"){

				log.info("Create new connection");
				this.makeConnectionThirdPartyAPIPostgreSql(dbname);		
			}

		}
		else{
			log.info("Create new connection");
			//call make connection using-postgresql
			this.makeConnectionThirdPartyAPIPostgreSql(dbname);

		}


	}
	public void connectionExistenceForExportDBInAPI(String dbname) throws InterruptedException{
		if(isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==true){
			List<WebElement>connectedDBList =new WebDriverWait(driverValue,10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.connectionAvailable)));
			String varEx="";
			String varSam="";
			for(int i=0;i<connectedDBList.size();i++){
				if(connectedDBList.get(i).getText().contains("export")){varEx="export";}
				if(connectedDBList.get(i).getText().contains("sample")){varSam="sample";}

			}

			if(varEx=="export"){
				log.info("Connection already exist.You can proceed further");
				new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.closeIcon))).click();//click on cancel

				//call export DB-postgresql
				//this.exportDBUsingMySql();

			}
			else if(varSam=="sample"){
				//log.info("Do nothing with this database.Search again for exportdb");
				log.info("Create new connection");
				this.makeConnectionThirdPartyAPIPostgreSql(dbname);
				driverValue.findElement(By.xpath(ConstString.closeIcon)).click();//click on close button to close modal
				//call export DB-mysql

			}

		}
		else{
			log.info("Create new connection");
			this.makeConnectionThirdPartyAPIPostgreSql(dbname);
			driverValue.findElement(By.xpath(ConstString.closeIcon)).click();//click on close button to close modal

		}


	}
	public void mixpanelConnection() throws InterruptedException{
		Wait<WebDriver> waitfordata=this.fluentWait();
		//select API service
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addByAPI))).click();
		//click on service that is coming from env
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.mixPanelLogo))).click();
		Thread.sleep(500);
		this.overlay();
		//this.overlay();
		if(isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==true){
			log.info("Connection already exist.You can proceed further");


		}
		else{
			log.info("Create new connection");

			//click on new connection
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection)));
			driverValue.findElement(By.xpath(ConstString.apiSecretInputBox)).clear();
			driverValue.findElement(By.xpath(ConstString.apiSecretInputBox)).sendKeys("5a284d91a8ab1a30f58722301f55c722");
			//select timezone
			Select timeZone =new Select(driverValue.findElement(By.xpath(ConstString.projectTimeZone)));
			timeZone.selectByVisibleText("UTC");
			log.info("User able to select timezone");
			//enter display name
			driverValue.findElement(By.xpath(ConstString.enterDisplayName)).sendKeys("Test");
			//connect to DB button
			driverValue.findElement(By.xpath(ConstString.connectButton)).click();
			this.overlay();
			//click on previous button
			new WebDriverWait(driverValue, 7).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'BACK')]"))).click();
			//wait for connection name to be present on screen
			Assert.assertTrue(waitfordata.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.connectionNameInMixpanel))).isDisplayed(), "Connection name don't get displayed after making connection ");
			log.info("---------------New connection using mixpanel has been created------------");
		}

	}
	public void surveyMonkeyConnection() throws InterruptedException{
		Wait<WebDriver> waitfordata=this.fluentWait();
		//click on service that is coming from env
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.surveymonkeyLogo))).click();
		this.overlay();
		if(isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==true){
			log.info("Connection already exist.You can proceed further");
		}
		else{
			log.info("Create new connection");
			String window1=driverValue.getWindowHandle();
			//click on new connection
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection))).click();
			for(String window2:driverValue.getWindowHandles()){
				if(!window2.equals(window1)){
					log.info("--------------------new window found------------");
					driverValue.switchTo().window(window2);					
				}				
			}
			driverValue.findElement(By.xpath(ConstString.userNameInOtherAPIUrl)).sendKeys("devmammoth");
			driverValue.findElement(By.xpath(ConstString.passwordNameInOtherAPIUrl)).sendKeys("meghanam104");
			driverValue.findElement(By.xpath(ConstString.SignInsurveymonkeyAPIUrl)).click();
			this.overlay();
			//click on add new button
			/*new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.Addnewbuttonid))).click();
			Thread.sleep(2000);
			//select API service
			new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addByAPI))).click();
			new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.surveymonkeyLogo))).click();*/
			this.overlay();	
			new WebDriverWait(driverValue, 8).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.displayNameIDToCheckConnectionExistence)));
			log.info("---------------New connection using surveyMonkey has been created------------");
		}

	}
	public void salesForceConnection() throws InterruptedException{
		Wait<WebDriver> waitfordata=this.fluentWait();
		//click on service that is coming from env
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.salesForceLogo))).click();
		this.overlay();
		if(isElementPresent(By.xpath(ConstString.displayNameIDToCheckConnectionExistence))==true){
			log.info("Connection already exist.You can proceed further");
		}
		else{
			log.info("Create new connection");
			String window1=driverValue.getWindowHandle();
			//click on new connection
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.newConnection))).click();
			for(String window2:driverValue.getWindowHandles()){
				if(!window2.equals(window1)){
					log.info("--------------------new window found------------");
					driverValue.switchTo().window(window2);					
				}				
			}
			driverValue.findElement(By.xpath(ConstString.userNameInOtherAPIUrl)).sendKeys(salesforceUser);
			driverValue.findElement(By.xpath(ConstString.passwordNameInOtherAPIUrl)).sendKeys(salesforcePass);
			driverValue.findElement(By.xpath(ConstString.loginInSalesforceAPIUrl)).click();
			//here we are providing sleep for 5 seconds to capture screenshot to check connection page
			Thread.sleep(5000);
			//taking screenshot
			File scrFile = ((TakesScreenshot)driverValue).getScreenshotAs(OutputType.FILE);
			Path captureToVerifyCode= Paths.get( file.getParentFile().getParent(),"test_suites", "Failure Screenshot","salesforceToCheckVerificationWindow.jpeg");
			String img1 =captureToVerifyCode.toString();
			try {
				FileUtils.copyFile(scrFile, new File(img1),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new WebDriverWait(driverValue, 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.verifyCodeInputBoxSalesforceAPIUrl)));
			String tab = driverValue.getWindowHandle();
			log.info("----------Windows handle taken------");
			Thread.sleep(2000);
			//JavascriptExecutor js =(JavascriptExecutor)driver;
			((JavascriptExecutor)driverValue).executeScript("window.open()");
			//Open new tab
			//driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
			Thread.sleep(2000);
			log.info("New Tab process complete");
			log.info("-------------------New tab is opening-------------------"+"\n");
			for(String winHandle : driverValue.getWindowHandles()){
				if(!winHandle.equals(tab)){
					driverValue.switchTo().window(winHandle);
				}
			}

			//Load URL to check reset password link has received
			driverValue.get("https://www.gmail.com");

			if ((this.browsers).equals("2")) {

				try{
					driverValue.findElement(By.xpath(ConstString.signInLinkForChrome));
					driverValue.findElement(By.xpath(ConstString.signInLinkForChrome)).click();
				}
				catch(Exception e){
					log.severe("Exception occured: Link to signin in chrome is not present")	;

				}

			}
			//Enter your email id
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.userIdOfGmail))).sendKeys(secondaryUser);			
			//click on next button
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.nextIdOfGmail))).click();
			Thread.sleep(500);
			//Enter password
			(new WebDriverWait(driverValue, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.passIdOfGmail))).sendKeys(seleniumAccountEmailPassword);

			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.nextIdOfGmail))).click(); //click on sign in	
			log.info("-------------User logged in-----------");
			//Assert that "Mail" logo is present on left corner
			for (int second = 0;; second++) {
				if (second >= 120) fail("timeout");
				try {
					Assert.assertTrue(isElementPresent(By.xpath(ConstString.mailLogo)));
					break;
				}
				catch(Exception e){
					log.severe("Exception occure: Mail logo is not present");
				}
			}
			Thread.sleep(2000);

			Path capture= Paths.get( file.getParentFile().getParent(),"test_suites", "Failure Screenshot","salesforceAfterLogin.jpeg");
			String img2 =capture.toString();
			try {
				FileUtils.copyFile(scrFile, new File(img2),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Wait for mail present in your inbox with subject line "verify your identity in salesforce".
			(new WebDriverWait(driverValue, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//div[@class = 'y6']/span[contains(.,'Verify your identity in Salesforce')]")))).click();
			//Find  link received in opened mail
			String wholeMailContent=	(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'a3s') and not (contains(@class,'undefined'))]"))).getAttribute("innerText");
			int indexForCode=  wholeMailContent.indexOf("Verification Code: ");
			log.info("index is :"+indexForCode);
			String codeToEnter =wholeMailContent.substring(indexForCode+18, indexForCode+28);
			String codeNumeric=codeToEnter.replaceAll("\\D+","");
			log.info("Verification code fetched from mail is "+codeNumeric);
			log.info("-----------Now Delete all mails----------");
			Thread.sleep(1000);
			new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.goBackArrowInInbox))).click();
			Thread.sleep(1000);
			new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.checkBoxInGmail))).click(); //check on select all mail
			new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.deleteMailInGmail))).click(); //delete now
			Thread.sleep(2000);
			driverValue.findElement(By.xpath(ConstString.gmailCornerSetting)).click();// upper right corner for logout setting in Gmail
			driverValue.findElement(By.xpath(ConstString.gmailLogout)).click(); // click on sign out from gmail
			driverValue.switchTo().window(tab);
			new WebDriverWait(driverValue, 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.verifyCodeInputBoxSalesforceAPIUrl))).sendKeys(codeNumeric);
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.verifyButtonSalesforceAPIUrl))).click();
			Thread.sleep(3000);
			this.overlay();
			//new WebDriverWait(driverValue, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.Addnewbuttonid))).click();
			Thread.sleep(1000);
			//new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addByAPI))).click();
			//new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.salesForceLogo))).click();

		}

	}
	public  String saveTemplate() throws IOException{
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.viewDropdown))).click();
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.saveAsTemplateInView))).click();
		String name=this.getDownloadedDocumentName(this.downloadFilepath,"mammoth");	
		log.info("Downloaded template name is------------- ------------ "+name);
		Assert.assertTrue(!name.isEmpty(), "Downloaded file name is empty,Download gets failed");
		return name;
	}
	public  void applyTemplate(String nameOfTemplate) throws IOException, InterruptedException{
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.viewDropdown))).click();
		try{

			WebElement file1= driverValue.findElement(By.xpath(ConstString.applyTemplate));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;			
			WebElement upload =null;
			js.executeScript("return document.getElementsByTagName('file-reader-interface');",upload);		
			Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Download_dir",nameOfTemplate);
			String filelocation =pathToFile.toString();
			file1.sendKeys(filelocation);
			Thread.sleep(2000);
			log.info("Uploading  a template named : "+nameOfTemplate);

		}
		catch(Exception e){
			log.severe("Exception occure: template not uploaded"+e);
			Assert.fail("Template could not be upload");
		}
		this.overlay();	
		log.info("--------------Template  applied successfully  ---------");
		//clean the directory now where files gets download
		File filesToDelete=new File(this.downloadFilepath,nameOfTemplate);
		Boolean success=filesToDelete.delete();
		Assert.assertTrue(success, "This file couldn't be delete");
		log.info("--------------After applying template ,it gets  deleted successfully from the directory---------");

	}
}
