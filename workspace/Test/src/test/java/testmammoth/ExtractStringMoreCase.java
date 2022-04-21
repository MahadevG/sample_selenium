package testmammoth;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExtractStringMoreCase {
	Logger log = Logger.getLogger(ExtractStringMoreCase.class.getName());
	FileHandler fileHandler;  
	String currDir  =	System.getProperty("user.dir");
	File file =new File(currDir);
	final String url = System.getenv("MAMMOTH_TEST_SITE");

	CommonFunctions functionObj =new CommonFunctions();
	static String positionToStart="";
	WebDriver driver;
	@BeforeMethod
	public void setup() throws Exception {
		driver =functionObj.browserSetUP();

		try { 
			//Create log file to see the result
			Path logFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Result","ExtractStringMorecases.log");
			String stringFile= logFile.toString();
			// This block configure the logger with handler and formatter  
			fileHandler = new FileHandler(stringFile);  
			log.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);  

			// the following statement is used to log any messages  
			log.info("Log file for Extract string more cases  in Data view");  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		try{
			JSONArray strConstArray1  = new JSONArray();
			JSONArray strConstArray2  = new JSONArray();
			JSONArray strConstArray3  = new JSONArray();
			JSONArray strConstArray4  = new JSONArray();
			JSONParser strConstParser =new JSONParser();

			Path pathToEleIDFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Environment","elementsId.json");
			String stringFile =pathToEleIDFile.toString();
			Object object = strConstParser.parse(new FileReader(stringFile));
			String objToString =object.toString();

			JSONObject jObj =new JSONObject(objToString);
			strConstArray1=jObj.getJSONArray("GlobalElements");
			strConstArray2 =jObj.getJSONArray("AccountLanding");
			strConstArray3 =jObj.getJSONArray("DataView");
			strConstArray4 =jObj.getJSONArray("FileUpload");

			JSONObject jObj1 =strConstArray1.getJSONObject(0);
			JSONObject jObj2 =strConstArray2.getJSONObject(0);
			JSONObject jObj3 =strConstArray3.getJSONObject(0);
			JSONObject jObj4 =strConstArray4.getJSONObject(0);

			ConstString.uid =jObj1.getString("uid");	
			ConstString.passid=jObj1.getString("passid");
			ConstString.signbuttonid =jObj1.getString("signbuttonid");
			ConstString.cornersettingid =jObj1.getString("cornerSettingMenu");
			ConstString.logoutbuttonid=jObj1.getString("logoutButtonId");
			ConstString.overlayId=jObj1.getString("overlayId");

			ConstString.Addnewbuttonid  = jObj2.getString("addNewButtonId");
			ConstString.fileuploadbuttonid= jObj2.getString("fileUploadButtonId");
			ConstString.Addnewfileid =jObj2.getString("addNewFileId");
			ConstString.firstRowID =jObj2.getString("firstRowID");


			ConstString.stepId =jObj4.getString("stepId");
			ConstString.impColNameID =jObj4.getString("impColNameID");

			ConstString.addtaskButton=jObj3.getString("addtaskButton");
			ConstString.taskPanel=jObj3.getString("taskPanel");
			ConstString.anyRuleDropDown = jObj3.getString("anyRuleDropDown");	
			ConstString.anyRuleNameInPipeline = jObj3.getString("anyRuleNameInPipeline");	
			ConstString.stepDescriptionInPipeline = jObj3.getString("stepDescriptionInPipeline");	
			ConstString.infoIconInPipeline = jObj3.getString("infoIconInPipeline");	
			ConstString.numericDropDown=jObj3.getString("numericDropDown");
			JSONArray jArrayRemove= jObj3.getJSONArray("RemoveDuplicates");
			JSONObject jObjRemove =jArrayRemove.getJSONObject(0);
			ConstString.totalStepsInPipeline = jObjRemove.getString("totalStepsInPipeline");
			ConstString.applyButtonInRemove = jObjRemove.getString("applyButtonInRemove");
			JSONArray jArrayGroup= jObj3.getJSONArray("GroupPivotRule");
			JSONObject jObjGroup =jArrayGroup.getJSONObject(0);
			ConstString.engineColumn =jObjGroup.getString("engineColumn");
			ConstString.impColId =jObjGroup.getString("impColId");
			ConstString.groupPivotFileId =jObjGroup.getString("groupPivotFileId");

			JSONArray jArrayAddCol= jObj3.getJSONArray("AddColumn");
			JSONObject jObjAdd =jArrayAddCol.getJSONObject(0);
			ConstString.columnFunctionFileId= jObjAdd .getString("columnFunctionFileId");	

			JSONArray jArrayInsert= jObj3.getJSONArray("InsertCustomValue");
			JSONObject jObjInsert =jArrayInsert.getJSONObject(0);
			ConstString.newColRadioButton= jObjInsert .getString("newColRadioButton");
			ConstString.inputBoxNewColumn = jObjInsert .getString("inputBoxNewColumn");
			ConstString.newlyInsertedColumn = jObjInsert .getString("newlyInsertedColumn");
			ConstString.existingColRadioButton = jObjInsert .getString("existingColRadioButton");
			ConstString.existingColDropDown = jObjInsert .getString("existingColDropDown");	

			JSONArray jArrayMath= jObj3.getJSONArray("MathRule");
			JSONObject jObjMath =jArrayMath.getJSONObject(0);
			ConstString.mathRuleFileId= jObjMath .getString("mathRuleFileId");	


			JSONArray jArrayExtract= jObj3.getJSONArray("ExtractString");
			JSONObject jObjExtract =jArrayExtract.getJSONObject(0);
			ConstString.extractStringFileID= jObjExtract .getString("extractStringFileID");	
			ConstString.extractStringRule= jObjExtract .getString("extractStringRule");	
			ConstString.extractStringTitle= jObjExtract .getString("extractStringTitle");	
			ConstString.extractTextFromDropdown= jObjExtract .getString("extractTextFromDropdown");	
			ConstString.whatToExtractDropdown= jObjExtract .getString("whatToExtractDropdown");	
			ConstString. allOrSpecificDropdown= jObjExtract .getString("allOrSpecificDropdown");	
			ConstString.leftOrRightDropdown= jObjExtract .getString("leftOrRightDropdown");	
			ConstString.extractStringInPipeline =jObjExtract .getString("extractStringInPipeline");	
			
			ConstString.wildCardInputBox =jObjExtract .getString("wildCardInputBox");	
			ConstString.numCharacters =jObjExtract .getString("numCharacters");
			ConstString.position =jObjExtract .getString("position");

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	@AfterMethod

	public void closebrowser()	{
		//Close session

		driver.close();
		driver.quit();

	}

	private String[][]jsonData(String jsonPath)throws Exception{
		String[][]tabArray =null;
		JSONArray extractArray =new JSONArray();
		JSONObject jsonObj=null;
		JSONParser parser=new JSONParser();
		Object obj =parser.parse(new FileReader(jsonPath));
		String fileInstring =obj.toString();
		jsonObj=new JSONObject(fileInstring);
		extractArray =jsonObj.getJSONArray("Characters_In_Certains_Pos");
		//JSONObject subObj =extractArray.getJSONObject(0);
		tabArray=new String[extractArray.length()][6];
		int var=0;
		for(int i=0;i<extractArray.length();i++){
			JSONObject response= extractArray.getJSONObject(i);
			String id=response.getString("test_ID");
			String extract =response.getString("what_To_Extract");
			String charac =response.getString("characters");
			String fromSide =response.getString("from");
			Integer pos =response.getInt("position");
			String numInSpecific =response.getString("numInSpecific");
			//String testId= Integer.toString(id);
			tabArray[var][0]=id;
			tabArray[var][1]=extract;
			tabArray[var][2]=charac;
			tabArray[var][3]=fromSide;
			tabArray[var][4]=Integer.toString(pos);
			tabArray[var][5]=numInSpecific;

			var++;

		}

		//contains whole fetched data from add_col.json file
		return(tabArray);
	}

	//Data provider definition
	@DataProvider(name = "DP")
	public Object[][] createData() throws Exception{
		//Fetching data from json file
		Path pathToJSON =Paths.get(file.getParentFile().getParent(),"test_suites","Modules","Data View","Column Functions","Extract_String.json");
		String extractStringFile =pathToJSON.toString();
		Object[][] retObjArr=jsonData(extractStringFile);
		return(retObjArr);
	}
	@Test(dataProvider="DP",description="Extract string with Characters in a certain position different cases")
	public void extractStringMoreCases(String tId,String extract,String character,String fromSide,String position,String numInSpecificCase) throws InterruptedException {
		positionToStart=position;
		driver.get(url);
		driver=functionObj.signIN();
		driver.manage().window().maximize();
		//click on add new button
		(new WebDriverWait(driver,5)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.Addnewbuttonid))).click();
		functionObj.extractStringUloadFile();
		log.info("\t-------------------New Window is opening-------------------"+"\n");
		functionObj.overlay();
		functionObj.pageTitle();	

		ArrayList<String> campaignContentListFromSourceFileAfterExtrct =functionObj.extractStringFunction(Integer.parseInt(tId));
		log.info("Test started with test  ID : "+tId+"  ");

		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addtaskButton))).click();
		Thread.sleep(500);
		new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.extractStringRule))).click();
		//Assert for title
		Assert.assertTrue( new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.extractStringTitle))).isDisplayed(), "Extract String rule window don't get displayed");
		//select column name from which we have to extract
		Select textFrom =new Select(driver.findElement(By.xpath(ConstString.extractTextFromDropdown)));
		log.info("----------------Dropdown lists only text column test started---------------");
		for(WebElement valueInList:textFrom.getOptions()){
			String optionValue=valueInList.getText();
			if (!(valueInList.getAttribute("value")).equals("")){
				Assert.assertTrue(optionValue.contains("(txt)"), "Only text columns is not  listed in the dropdown");
			}
		}
		log.info("------------------Extract text from dropdown conatins only text column------------------");
		textFrom.selectByVisibleText("CampaignName (txt)");
		//Select what we have to extract
		Select whatToExtractFrom =new Select(driver.findElement(By.xpath(ConstString.whatToExtractDropdown)));
		whatToExtractFrom.selectByVisibleText(extract);
		log.info("------------------User able to select What to extract from------------------");
		//select all or specific characters
		Select charcterCount =new Select(driver.findElement(By.xpath(ConstString.allOrSpecificDropdown)));
		charcterCount.selectByVisibleText(character);

		//extract characters from left 
		Select charcterFrom =new Select(driver.findElement(By.xpath(ConstString.leftOrRightDropdown)));
		charcterFrom.selectByVisibleText(fromSide);
		if(tId.equals("2")||tId.equals("4")){

			driver.findElement(By.xpath(ConstString.numCharacters)).sendKeys(numInSpecificCase);
		}
		if(!tId.equals("2")||!tId.equals("4")){
			//enter position in input box
			driver.findElement(By.xpath(ConstString.position)).sendKeys(positionToStart);
		}
		if(tId.equals("1")||tId.equals("2")){

			driver.findElement(By.xpath(ConstString.newColRadioButton)).isSelected();	
			//find text box to enter new col name
			driver.findElement(By.xpath(ConstString.inputBoxNewColumn)).clear();
			driver.findElement(By.xpath(ConstString.inputBoxNewColumn)).sendKeys("Extracted Result");
		}
		if(tId.equals("3")||tId.equals("4")){ 
			//find an existing column radio button and click it	
			driver.findElement(By.xpath(ConstString.existingColRadioButton)).click();	
			//find Column Name dropdown
			Select select =new Select(driver.findElement(By.xpath(ConstString.existingColDropDown)));
			select.selectByVisibleText("CampaignName (txt)");
		}

		driver.findElement(By.xpath(ConstString.applyButtonInRemove)).click();
		functionObj.overlay();
		functionObj.overlay();
		new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.taskPanel))).click();
		
		//find extract string  rule in pipeline
		Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.extractStringInPipeline))).isDisplayed(), "Extracted String rule not added in pipeline");
		//find math rule details
		functionObj.stepDescrition();
		Wait<WebDriver> waitForData=	functionObj.fluentWait();
		if(tId.equals("1")||tId.equals("2")){
			List<WebElement> resultantColCampData =	waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='slick-cell l2 r2']")));
			ArrayList<String> resultantCampaignList =new ArrayList<String>();
			int sizeOfResultantColumn=resultantColCampData.size();
			if(sizeOfResultantColumn>0) {    
				for(int j=0;j<sizeOfResultantColumn;j++){
					if(j>4){
						break;
					}
					String value =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='slick-cell l2 r2']"))).get(j).getText().toString();
					resultantCampaignList.add(value);

				}
			}

			for(int i=0;i<resultantCampaignList.size();i++){
				Assert.assertEquals(resultantCampaignList.get(i),campaignContentListFromSourceFileAfterExtrct.get(i), "Resultant campaign list content after extracting string and logically formed list content are not equal");
			}
			log.info("------------Resultant Campaign column  list contains expected results-----------");
		}
		if(tId.equals("3")||tId.equals("4")){
			List<WebElement> resultantColCampData =	waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.firstRowID)));
			ArrayList<String> resultantCampaignList =new ArrayList<String>();
			int sizeOfResultantColumn=resultantColCampData.size();
			if(sizeOfResultantColumn>0) {    
				for(int j=0;j<sizeOfResultantColumn;j++){
					if(j>4){
						break;
					}
					String value =waitForData.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.firstRowID))).get(j).getText().toString();
					resultantCampaignList.add(value);

				}
			}

			for(int i=0;i<resultantCampaignList.size();i++){
				Assert.assertEquals(resultantCampaignList.get(i),campaignContentListFromSourceFileAfterExtrct.get(i), "Resultant campaign list content after extracting string and logically formed list content are not equal");
			}
			log.info("------------Resultant Campaign column  list contains expected results-----------");
		}

		functionObj.softAssert.assertAll();
	}
}
