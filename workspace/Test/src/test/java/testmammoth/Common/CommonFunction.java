package testmammoth.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.opencsv.CSVReader;

import testmammoth.FilterDateColEqualCase;
import testmammoth.FilterDateColLessGreaterCase;
import testmammoth.FilterDateInBetween;
import testmammoth.FilterDateLessGreaterEqualCase;
import testmammoth.FilterTextColContainCase;
import testmammoth.FilterTextColEndCase;
import testmammoth.FilterTextColEqualityCase;
import testmammoth.FilterTextColStartWithCase;
import testmammoth.NumColLessGreaterEqual;
import testmammoth.NumColLessGreaterFilter;
import testmammoth.SanitySuite.ApplyFilter_Sanity;
import testmammoth.SetUp.AllConstant;
import testmammoth.SetUp.DriverInitialize;

public class CommonFunction extends DriverInitialize { 
	WebDriver  driverValue= null;
	public String  winHandleBefore;
	public ArrayList<String> impColArrayInString=new ArrayList<String>();
	public ArrayList<String> clicksColArrayInString=new ArrayList<String>();
	public ArrayList<Float> arrayOfAvgPosMathRule =new ArrayList<Float>();
	public ArrayList<String> arrayOfKeywordGroupMathRule =new ArrayList<String>();
	public ArrayList<String> arrayOfEngineMathRule =new ArrayList<String>();
	public ArrayList<String>   arrayOfDateInString=new ArrayList<String>();
	public ArrayList<String>   arrayOfCampaignMathRule=new ArrayList<String>();
	public AllConstant var =new AllConstant();


	Logger log = Logger.getLogger(CommonFunction.class.getName());
	public SoftAssert softAssert = new SoftAssert();

	@BeforeMethod
	public WebDriver getJSONDataAndDriverForCommon() throws FileNotFoundException, IOException, ParseException {
		var.jsonData();
		return driverValue =DriverInitialize.getDriver();
	}

	public void overlay() throws InterruptedException{

		driverValue.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);  
		try{
			long oldTime=System.currentTimeMillis();
			log.info("overlay is there");
			outerloop:
				while(driverValue.findElement(By.xpath("//spinner[@class='ng-scope']")).isDisplayed()){   
					if(System.currentTimeMillis()- oldTime>var.SEVENTEEN_SECONDS){
						driverValue.navigate().refresh();
						try{
							//again find overlay after refreshing
							while(driverValue.findElement(By.xpath("//spinner[@class='html5spinner ng-scope']")).isDisplayed()){
								long oldTimeAfterRefresh=System.currentTimeMillis();
								if(System.currentTimeMillis()- oldTimeAfterRefresh>var.SEVENTEEN_SECONDS){
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
							//softAssert.assertTrue(false, "Current test required browser refresh to work");//we are disabling this case as we are not doing anything with these failures.
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
			Assert.assertTrue(val,"Overlay is still present");
		} 
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);  
		}    
	}
	public  WebDriver signIN() throws InterruptedException{


		//Enter user name fetched from json file
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.uid))).sendKeys(var.userName);
		//clear password field
		//(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.id(var.passid))).clear();  
		//Enter password
		(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.passid))).clear();;
		(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.passid))).sendKeys(var.passwd); 
		Thread.sleep(700);
		//click on sign in button
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.signbuttonid))).click();
		Thread.sleep(1000);

		this.overlay();
		Thread.sleep(1000);
		if(isElementPresent(By.xpath("//div[@class='no-data']"))){
			//if(driverValue.findElement(By.xpath("//div[@class='no-data']")).isDisplayed() && driverValue.findElement(By.xpath("//div[@class='no-data']")).isDisplayed()){
			WebElement welcomePage =driverValue.findElement(By.xpath("//div[@class='no-data']/h1"));
			String mainText =welcomePage.getText().toString();
			log.info(mainText);
			log.info("It is a Fresh Account.You can add data ");
			//find file upload
			(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewfileid))).click();
			WebElement uploadId= driverValue.findElement(By.xpath(var.fileuploadbuttonid));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;   
			js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");
			Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files","homePageFile.csv");
			String fileLocation =pathToFile.toString();
			uploadId.sendKeys(fileLocation);
			this.uploadedFileStatus();
			Thread.sleep(2000);
			//new WebDriverWait(driverValue, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'first-landing-item highlight')]")));
			//driverValue.get("https://qa.mammoth.io/#/landing?onboarding=1");
			//check if on boarding section is present
			/*if(isElementPresent(By.xpath("//div[contains(@class,'introjs-tooltip product-tour')]"))){
                log.info("---------------------On boarding 
 is present----------------");
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
	public void signOut() {
		// find corner setting menu on left side
		WebElement logout=  driverValue.findElement(By.xpath(var.cornersettingid));
		logout.click();
		//click on logout button
		(new WebDriverWait(driverValue, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.logoutbuttonid))).click();
		log.info("logout successfully");

	}
	public boolean isElementPresent(By by) {   
		driverValue.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);  
		Boolean isPresent = driverValue.findElements(by).size() > 0;
		return isPresent;

	}
	public void uploadedFileStatus(){
		try{
			long oldTimeForProcessing=System.currentTimeMillis();
			driverValue.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);  
			log.info("Uploading 1 file message is there");
			while(driverValue.findElement(By.xpath("//div[contains(@class,'ds-single processing') and not (contains(@class,'status-attention'))]")).isDisplayed()){
				if(System.currentTimeMillis()- oldTimeForProcessing>120*1000){
					Assert.fail("Looking like file got stuck in processing messages");

				}
			}
		}
		catch(Exception e){
			log.info("Uploading one file message  gets invisible now");
		}
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
		} 
		try{
			long oldTimeForProcessing=System.currentTimeMillis();
			driverValue.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);  
			log.info("Upload processing messages is there");
			while(driverValue.findElement(By.xpath("//div[@class='ds-title processing']//div[@class='status-wrapper']/div[contains(@class,'status')]/p")).isDisplayed()){
				if(System.currentTimeMillis()- oldTimeForProcessing>120*1000){
					Assert.fail("---------------Looking like file got stuck in processing messages----------------");

				}
			}
		}
		catch(Exception e){
			log.info("Upload Processing messages gets invisible now");
		}
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
		}       
	}
	public void viewCreationLoader(){
		try{
			driverValue.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);  
			while(driverValue.findElement(By.xpath("//small-inline-loader[@class='small-inline-loader']")).isDisplayed()){
				log.info("On creating views loader displayed to user");
			}
		}
		catch(Exception e){
			log.info("Loader gets invisible now");
		}
		finally  
		{  
			driverValue.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);  
		}  

	}
	public void previewSection(){
		System.out.println("preview section is not there.");
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("(//h5//span[@class='ng-binding ng-scope'])[1]"))).click();
		System.out.println("preview is there");
		Boolean isPresent = driverValue.findElements(By.xpath("//div[@class='prev-body']//section[contains(@class,'preview')]")).size()>0;
		Assert.assertTrue(isPresent.equals(true), "preview section doesn't get open");
	}
	public void OpenpreviewSectionJumpToDataview(){
		List<WebElement> elems = driverValue.findElements(By.xpath("//div[@class='prev-body']//section[contains(@class,'preview')]"));
		if (elems.size() == 0) {
			System.out.println("preview section is not there.");
			(new WebDriverWait(driverValue, 4)).until(ExpectedConditions.elementToBeClickable(By.xpath("//h5//span[@class='ng-binding']"))).click();
		} else
			System.out.println("preview is there");
	}

	public void pageTitle(){
		//this.waitToLoad();

		Wait<WebDriver> waitForElement= this.fluentWait();
		//waitForElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//title[contains(text(),'View')]")));
		waitForElement.until(ExpectedConditions.titleContains("View"));
	}
	public Wait<WebDriver>  fluentWait(){
		Wait<WebDriver> waitForElement = new FluentWait<WebDriver>(driverValue)
				.withTimeout(Duration.ofSeconds(60))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);
		return waitForElement;
	}
	public void taskPanel(){
		log.info("Check if task panel is already opened");
		driverValue.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);  
		Boolean val = driverValue.findElements(By.xpath("//div[contains(@class,'toggle-steps active')]")).size() > 0;
		if(val.equals(true)) {
			log.info("Task panel opened automatically");
		}
		else {
			new WebDriverWait(driverValue, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(var.taskPanel))).click();
			log.info("Clicked on green dot to open task panel");
		}

	}
	public void scrollHorizontalOnDataView() throws InterruptedException{
		Wait<WebDriver> waitForElement= this.fluentWait();
		JavascriptExecutor jse =    (JavascriptExecutor) driverValue;
		List<WebElement> lastColumn=    waitForElement.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='ui-widget-content slick-row odd' or @class='ui-widget-content slick-row even'][last()]/child::div")));
		//log.info("Column size is  " +lastColumn.size());
		for(int j=0;j<=lastColumn.size();j++){
			if(j==lastColumn.size()-1){

				jse.executeScript("var g=window.angular.element('.active-workspace .ds-grid.grid-container').scope().vm.mainGridUnit.gridInstance.grid;var c=g.getColumns();g.scrollCellIntoView(0,c.length-1)",lastColumn.get(j));//scroll function to see last column of table
				//jse.executeScript("arguments[0].scrollIntoView(true);",lastColumn.get(j)); //scroll function to see last column of table
			}
		}

	}
	public void deleteDS() throws InterruptedException {
		Thread.sleep(2000);
		//check if open button is there
		if(this.isElementPresent(By.xpath(var.openButtonSingleView))==true) {
			Thread.sleep(2000);
			(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.deleteIcon))).click();
			Assert.assertTrue((new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteConfirmationModal))).isDisplayed(), "Delete multiple DS window don't get opened");  
			Thread.sleep(1000);
			(new WebDriverWait(driverValue,7)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.deleteButtonInWindow))).click();
			Thread.sleep(1000);
			log.info("Delete  confirmation button clicked ");
			new WebDriverWait(driverValue, 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(.,'Deleting dataset')]")));
			new WebDriverWait(driverValue, 45).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//p[contains(.,'Deleting dataset')]")));
			//        Wait<WebDriver> waitForDeletionMsgOnTop =this.fluentWait();      
			//        WebElement deleteDSMsg=  waitForDeletionMsgOnTop.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteMsgOnTop)));
			//        String Msg =deleteDSMsg.getText().toString();
			//        log.info(Msg);  
			// Thread.sleep(3000);
		}
		else {
			//check for preview section
			this.previewSection();
			(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.deleteIcon))).click();
			Assert.assertTrue((new WebDriverWait(driverValue,7)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteConfirmationModal))).isDisplayed(), "Delete multiple DS window don't get opened");  
			Thread.sleep(1000);
			(new WebDriverWait(driverValue,7)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.deleteButtonInConfirmationWindow))).click();
			Thread.sleep(1000);
			log.info("Delete  confirmation button clicked ");   
			//        Wait<WebDriver> waitForDeletionMsgOnTop =this.fluentWait();      
			//        WebElement deleteDSMsg=  waitForDeletionMsgOnTop.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteMsgOnTop)));
			//        String Msg =deleteDSMsg.getText().toString();
			//        log.info(Msg);  



		}
	}
	public void landingFileUpload(String name) {
		//Uploading a file     
		WebElement file1= driverValue.findElement(By.xpath(var.fileuploadbuttonid));
		JavascriptExecutor js = (JavascriptExecutor) driverValue;   
		js.executeScript("document.querySelector('.fileSelectHiddenInput').setAttribute('style',display='block','opacity:1; width:10px; height:10px; z-index:100')");
		Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files",name);
		String filelocation =pathToFile.toString();
		file1.sendKeys(filelocation);
		log.info("Uploading  a file named : "+ name);

		this.uploadedFileStatus();
		this.previewSection();   



	} 
	public  void jumpToDataView(String LandingWinHandle) throws InterruptedException {
        LandingWinHandle = driverValue.getWindowHandle();
        Thread.sleep(800);
        By single_view = By.xpath(var.openButtonSingleView);
        By multi_view = By.xpath(var.openButtonMultiView);
        if(this.isElementPresent(single_view)==true)
        	driverValue.findElement(single_view).click();
        else if(this.isElementPresent(multi_view)==true) {
        	driverValue.findElement(multi_view).click();
        	new WebDriverWait(driverValue,10).until(ExpectedConditions.elementToBeClickable(By.xpath(var.firstViewInOpenButton))).click();
        }
		Actions action = new Actions(driverValue);
		// Store the current window handle

		//  action.moveToElement(uploadedfile).doubleClick().perform();
		// Switch to new window opened
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(LandingWinHandle)){
				driverValue.switchTo().window(winHandle);
			}
		}
		log.info("\t--New Window is opening--"+"\n");
		this.overlay();
		this.pageTitle();

	} 

	public String stepDescrition() throws InterruptedException{

		//find added columns details
		Actions act =new Actions(driverValue);
		act.moveToElement(driverValue.findElement(By.xpath(var.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(var.infoIconInPipeline))).build().perform();
		Thread.sleep(500);
		String ruleDetails=new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.stepDescriptionInPipeline))).getText();
		Assert.assertTrue(!ruleDetails.isEmpty(), "There is no  detail for this rule");
		return ruleDetails;


	}
	public void addNoteInPipline(String note) throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(var.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		//Click on pipeline dropdown
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.anyRuleDropDown))).click(); 
		//Find Add Note In pipeline
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.addNoteInDropdown))).click();

		//Find Add note title window
		WebElement addnoteTitle = (new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.addNoteTitleWindow)));
		Assert.assertTrue(addnoteTitle.isDisplayed(), "Rename Step title window don't get displayed ");
		//Write something in text area
		driverValue.findElement(By.xpath(var.textAreaAddNote)).sendKeys(note);
		Thread.sleep(500);
		//click on done button
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.submitActionInPipeline))).click();
		Assert.assertTrue((new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.addedNoteInPipeline))).isDisplayed(), " Added Note is not present");
		String noteContent =(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.addedNoteInPipeline))).getText();
		Assert.assertTrue(!noteContent.isEmpty(), "Note couldn't be added");
		log.info("Added note is : "+noteContent);
	}

	public void renameInPipeline(String name) throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(1000);
		act.moveToElement(driverValue.findElement(By.xpath(var.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		//Click on pipeline dropdown
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.anyRuleDropDown))).click();
		//Click on rename
		new WebDriverWait(driverValue, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.renameInPipeline))).click();
		//Find Rename title window
		WebElement renameTitle = (new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.renameTitleWindowPipeline)));
		Assert.assertTrue(renameTitle.isDisplayed(), "Rename Step title window don't get displayed ");
		driverValue.findElement(By.xpath(var.renameInputBoxInPipeline)).sendKeys(name);
		//click on rename button
		driverValue.findElement(By.xpath(var.submitActionInPipeline)).click();
		log.info("-----------Clicked on rename button---------");

	}
	public void deleteRuleInPipeline() throws InterruptedException{
		Actions act =new Actions(driverValue);
		Thread.sleep(500);
		act.moveToElement(driverValue.findElement(By.xpath(var.anyRuleNameInPipeline))).build().perform();
		Thread.sleep(500);
		(new WebDriverWait(driverValue, 15)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.anyRuleDropDown))).click();
		//delete rule from pipeline
		WebElement deleteInDropDown2= driverValue.findElement(By.xpath(var.deleteInPipeline));
		deleteInDropDown2.click();
		WebElement deleteTaskTitle2 =(new WebDriverWait(driverValue,5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deletetaskTitle)));
		Assert.assertTrue(deleteTaskTitle2.isDisplayed(), "Delete task window don't get opened");
		driverValue.findElement(By.xpath(var.deleteButtonInWindow)).click();
		WebDriverWait waitForDeletionMsgOnTop =new WebDriverWait(driverValue,10) ;      
		//        WebElement deleteDSMsg=  waitForDeletionMsgOnTop.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.deleteMsgOnTop)));
		//        String Msg =deleteDSMsg.getText().toString();
		//        log.info(Msg);
		log.info("Rule deleted from pipeline");
		this.overlay();

	}


	public ArrayList<Integer> impColResultantDataInFilter(){
		//fetch resultant column data
		Wait<WebDriver> waitForData=    this.fluentWait();
		int sizeOfResultantColumn=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.impColId))).size();
		ArrayList<Integer> updatedarrayOfImp =new ArrayList<Integer>(); 
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}
				for(int findCount=0;findCount<2;findCount++){
					try{
						columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.impColId))).get(j).getText().toString();
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
		Wait<WebDriver> waitForData=    this.fluentWait();
		//fetch resultant column data
		List<WebElement> updatedEngineCol =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.engineColumn)));
		int sizeOfResultantColumn=updatedEngineCol.size();
		ArrayList<String> updatedarrayOfEngine =new ArrayList<String>();
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}


				columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.engineColumn))).get(j).getText().toString();
				updatedarrayOfEngine.add(columnData);

			}
		}
		return updatedarrayOfEngine;
	}
	public ArrayList<String> dateColResultantDataInFilter(){
		Wait<WebDriver> waitForData=    this.fluentWait();
		//fetch resultant column data
		List<WebElement> updatedDateCol =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.updatedDateCol)));
		int sizeOfResultantColumn=updatedDateCol.size();
		ArrayList<String> updatedarrayOfDate =new ArrayList<String>();  
		String columnData="";
		if(sizeOfResultantColumn>0) {    
			for(int j=0;j<sizeOfResultantColumn;j++){
				if(j>4){
					break;
				}

				columnData=waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(var.updatedDateCol))).get(j).getText().toString();
				log.info("Element found") ;
				updatedarrayOfDate.add(columnData);

			}
		}
		return updatedarrayOfDate;
	}

	public   ArrayList<Integer> commonNumericFilter(String name) throws InterruptedException{

		Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
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

		if(var.MaxMinImpFlag==1){
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
					int k=  arrayOfImp.get(i).compareTo(NumColLessGreaterFilter.value);

					if(k==-1){ // if value return -1 means it is less than compared one value

						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
				}
				break;

			case "Greater_than_Value":
				if(!arrayOfImp.get(i).equals(-1)){
					int j=  arrayOfImp.get(i).compareTo(NumColLessGreaterFilter.value); // if value return 1 means it is greater than compared one value
					if(j==1){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}
				}
				break;

			case "less_than_Or_Equal_Value":
				if(!arrayOfImp.get(i).equals(-1)){
					int n=  arrayOfImp.get(i).compareTo(NumColLessGreaterEqual.value);
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
					int p=  arrayOfImp.get(i).compareTo(NumColLessGreaterEqual.value);
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

			case "Is_the_minimum_value" :


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((arrayOfImp.get(i)==smallest)){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}
				break;

			case "Is_Not_the_minimum_value" :


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((!(arrayOfImp.get(i)==smallest))){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}


				break;
			case "Is_the_maximum_value" :


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((arrayOfImp.get(i)==largest)){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}
				break;

			case "Is_Not_the_maximum_value" :


				if(!arrayOfImp.get(i).equals(-1)){ 


					if ((!(arrayOfImp.get(i)==largest))){
						returnedArrayOfImp.add(arrayOfImp.get(i));
					}

				}

				break;

			case    "In_Between":

				if(!arrayOfImp.get(i).equals(-1)){ 

					if(arrayOfImp.get(i)>=ApplyFilter_Sanity.value1&&arrayOfImp.get(i)<=ApplyFilter_Sanity.value2){

						returnedArrayOfImp.add(arrayOfImp.get(i));      

					}

				}

			}

		}   
		return returnedArrayOfImp;

	}
	public   ArrayList<String> commonTextFilter(String name) throws InterruptedException{

		Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
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
		Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files","FilterFile.csv");
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
		String  max=null;
		String  min=null;

		Date datemax=null;
		Date datemin=null;

		if(var.MaxMinDateFlag==1){

			//Sorting of date that will sort dates in ascending order
			Collections.sort(dateArray);    
			//now find last element of array which will be minimum value of date
			max =dateArray.get(dateArray.size()-1).toString();
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

				int result    = dateArray.get(i).compareTo(sdfDate.parse(FilterDateColLessGreaterCase.valuestr));
				if(result <0){
					returnedArrayOfDate.add(dateArray.get(i));
				}

				break;

			case "Greater_Than":

				int results    =    dateArray.get(i).compareTo(sdfDate.parse(FilterDateColLessGreaterCase.valuestr));
				if(results >0){
					returnedArrayOfDate.add(dateArray.get(i));
				}   
				break;
			case "Less_Than_Or_Equal_to":

				int response    =   dateArray.get(i).compareTo(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr));
				if(response <0 ||dateArray.get(i).equals(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr))){
					returnedArrayOfDate.add(dateArray.get(i));
				}

				break;

			case "Greater_Than_Or_Equal_to":

				int responseVal    =    dateArray.get(i).compareTo(sdfDate.parse(FilterDateLessGreaterEqualCase.valuestr));
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
	public WebDriver mathRuleFunction(){
		Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Test Files","MathRule.csv");
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
					impColArrayInString.add(fileArray[2]);  //fetching 2 nd position( Imp column) data from i th row and store it to an array list
					clicksColArrayInString.add(fileArray[8]); //fetch clicks col data from csv file
					avgPosColArrayInString.add(fileArray[9]); //fetch avgPos col data from csv file
					keywordGroup.add(fileArray[5]);//fetch keyword Group col data from csv file
					arrayOfEngineMathRule.add(fileArray[4]);//fetch keyword Group col data from csv file
					arrayOfDateInString.add(fileArray[3]);//fetch date
					arrayOfCampaignMathRule.add(fileArray[0]);//fetch date
				}

			}
			log.info("imp column array in string in comon function"+impColArrayInString);
			for(String value: impColArrayInString){
				if(value.equals("")){
					value="0";
				}
				int intValue =Integer.parseInt(value);
				if(intValue!=0){
					arrayOfImpMathRule.add(intValue);
				}
			}
			for(String value: clicksColArrayInString){
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
		for(float avgPosVal: arrayOfAvgPosMathRule){//calculating sum of avgPos from csv file
			sumOfAvg =sumOfAvg+avgPosVal;
		}

		var.integerValueOfSumImp = (int) sumOfImp;
		log.info("Sum of Imp column values  in integer  is  :  " + var.integerValueOfSumImp);
		var.integerValueOfClicks = (int) sumOfClicks;
		log.info("Sum of Clicks column values  in integer  is  :  " + var.integerValueOfClicks);
		log.info("Sum of Avgpos column values  in double  is  :  " +sumOfAvg);

		var.integerValueOfAvgPos = (int) sumOfAvg;
		log.info("Sum of AvgPos column values  in integer  is  :  " + var.integerValueOfAvgPos);

		return driverValue;
	}
	public void createDatasetFromFileIfAlreadyExist(String filenameforxpath,String filenametoupload) throws InterruptedException {

		boolean value =  this.isElementPresent(By.xpath("//h5//span[contains(text(),"+filenameforxpath+")]"));
		log.info("File check value is "+value);
		if (value == true) {
			String dsId ="//h5//span[contains(text(),"+filenameforxpath+")]";
			log.info("File name is :"+dsId);
			String fileGettext =(new WebDriverWait(driverValue,5)).until(ExpectedConditions.elementToBeClickable(By.xpath(dsId))).getText();
			log.info("Extracted name  is :"+fileGettext);
			(new WebDriverWait(driverValue,5)).until(ExpectedConditions.elementToBeClickable(By.xpath(dsId))).click();
			this.overlay();
			String LandingWinHandle = driverValue.getWindowHandle();
			new WebDriverWait(driverValue,5).until(ExpectedConditions.elementToBeClickable(By.xpath(var.openButtonSingleView))).click();
			// Switch to new window opened
			for(String winHandle : driverValue.getWindowHandles()){
				if(!winHandle.equals(LandingWinHandle)){
					driverValue.switchTo().window(winHandle);
				}
			}
			log.info("\tNew Window is opening"+"\n");
			this.overlay();
			this.pageTitle();
			(new WebDriverWait(driverValue,10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.viewDropdown))).click();
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.resetInView))).click();
			Assert.assertTrue( new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.resetViewTitle))).isDisplayed(), "Reset view window don't get displayed");
			//click on Reset view
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Reset']"))).click();
			this.overlay();
			this.overlay();
			log.info("View has been reset");
			driverValue.switchTo().window(LandingWinHandle);
			//log.info("------------------------------Switched to landing page after reseting view------------------");
		}
		else {
			//Assert.fail("Required File is not present on landing page");
			(new WebDriverWait(driverValue,5)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.Addnewbuttonid))).click();
			Thread.sleep(500);
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.uploadDataset))).click();
			this.landingFileUpload(filenametoupload);

		}
		//h5//span[contains(.,'Mathrule')]

	}
	public  String saveTemplate() throws IOException, InterruptedException{
		Path downloadFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Download_dir");
		var.downloadFilepath= downloadFile.toString();
		Thread.sleep(2000);
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.viewDropdown))).click();
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.saveAsTemplateInView))).click();
		String name=this.getDownloadedDocumentName(var.downloadFilepath,"mammoth");    
		log.info("Downloaded template name is------------- ------------ "+name);
		Assert.assertTrue(!name.isEmpty(), "Downloaded file name is empty,Download gets failed");
		return name;
	}
	public  void applyTemplate(String nameOfTemplate) throws IOException, InterruptedException{
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.viewDropdown))).click();
		try{

			WebElement file1= driverValue.findElement(By.xpath(var.applyTemplate));
			JavascriptExecutor js = (JavascriptExecutor) driverValue;           
			WebElement upload =null;
			js.executeScript("return document.getElementsByTagName('file-reader-interface');",upload);      
			Path pathToFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Download_dir",nameOfTemplate);
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
		File filesToDelete=new File(var.downloadFilepath,nameOfTemplate);
		Boolean success=filesToDelete.delete();
		Assert.assertTrue(success, "This file couldn't be delete");
		log.info("--------------After applying template ,it gets  deleted successfully from the directory---------");

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
	public void waitForPageLoad(WebDriver driver ) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() 
		{
			public Boolean apply(WebDriver driver)
			{
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			}
		};
		Wait<WebDriver> wait = new WebDriverWait(driver,30);
		try
		{
			wait.until(expectation);
		}
		catch(Throwable error)
		{
			Assert.assertFalse(true, "Timeout waiting for Page Load Request to complete.");
		}
	}
	public void switchToLanding() {
		WebDriver driver = DriverInitialize.getDriver();
		// iterate through open tabs. And switch to Account Landing tab.
		for (String windowHandle : driver.getWindowHandles()) {
			driver.switchTo().window(windowHandle);
			String title = driver.getTitle();
			if (title.contains("Home")) {
				driver.switchTo().window(windowHandle);
				log.info("On Account Landing page now.");
			}
			else {
				driver.close();
			}
		}
	}
	public void switchToWindow(String LandingWinHandle) throws InterruptedException {
		for(String winHandle : driverValue.getWindowHandles()){
			if(!winHandle.equals(LandingWinHandle)){
				driverValue.switchTo().window(winHandle);
			}
		}
		log.info("\t--New Window is opening--"+"\n");
		this.overlay();
		this.pageTitle();    	
	}
	/**
	 * Assuming you are already on DataView page.
	 * This method will reset the opened view.
	 * @throws InterruptedException 
	 */
	public void resetView() throws InterruptedException {
		log.info("Resetting the current view");
		this.overlay();
		(new WebDriverWait(driverValue,10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.viewDropdown))).click();
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(var.resetInView))).click();
		Assert.assertTrue( new WebDriverWait(driverValue, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(var.resetViewTitle))).isDisplayed(), "Reset view window don't get displayed");
		//click on Reset view
		(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Reset']"))).click();
		this.overlay();
		this.overlay();
		log.info("View has been reset");
	}
	
	public void makePipelineAutoRunOn() throws InterruptedException {
		this.overlay();
		WebElement stepPanel = driverValue.findElement(By.xpath("//div[contains(@class,'toggle-steps')]"));
		JavascriptExecutor js = (JavascriptExecutor) driverValue;
		String res = js.executeScript("return angular.element(arguments[0]).scope().vm.stepsPanel.isOpen;", stepPanel).toString();
		boolean step_panel_status_open =Boolean.parseBoolean(res);  
		By auto_run_already_on = By.xpath("//div[@class='status-bar draft_mode']//input[@checked='checked']");
		By onBoarding_message=By.xpath("//div[@class='introjs-tooltipbuttons']//a[@role='button']");
		if(step_panel_status_open==true) {
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@type='button' and contains(@class,'toggle-steps')]"))).click();
		}
		if(step_panel_status_open==false) {
//			open task panel
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'toggle-steps')]"))).click();	
			if(this.isElementPresent(onBoarding_message)) {
//			Click on obboarding 'Got it' button for draft mode
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='introjs-tooltipbuttons']//a[@role='button']"))).click();
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='status-bar draft_mode']//input"))).click();
			log.info("Enabled pipeline Auto-run");
			}		
			
		}
		if(!this.isElementPresent(auto_run_already_on)) {
			(new WebDriverWait(driverValue, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='status-bar draft_mode']//input"))).click();
			log.info("Enabled pipeline Auto-run");
		}
		
		else {
			log.info("Pipeline auto-rub already on");
		}		
	}
}

