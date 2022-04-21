package testmammoth;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FilterTextColEndCase {
	Logger log = Logger.getLogger(FilterTextColEndCase.class.getName());
	FileHandler fileHandler;  
	String currDir  =	System.getProperty("user.dir");
	File file =new File(currDir);
	final String url = System.getenv("MAMMOTH_TEST_SITE");
	public static String valuestr ;
	CommonFunctions functionObj =new CommonFunctions();

	WebDriver driver;
	@BeforeMethod
	public void setup() throws Exception {
		driver =functionObj.browserSetUP();

		try { 
			//Create log file to see the result
			Path logFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Result","FilterTextColEndCase.log");
			String stringFile= logFile.toString();
			// This block configure the logger with handler and formatter  
			fileHandler = new FileHandler(stringFile);  
			log.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);  

			// the following statement is used to log any messages  
			log.info("Log file for Filter Numeric Column function in Data view");  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		try{
			JSONArray strConstArray1  = new JSONArray();
			JSONArray strConstArray2  = new JSONArray();
			JSONArray strConstArray3  = new JSONArray();



			JSONParser strConstParser =new JSONParser();

			Path pathToEleIDFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Environment","elementsId.json");
			String stringFile =pathToEleIDFile.toString();
			Object object = strConstParser.parse(new FileReader(stringFile));
			String objToString =object.toString();

			JSONObject jObj =new JSONObject(objToString);
			strConstArray1=jObj.getJSONArray("GlobalElements");
			strConstArray2 =jObj.getJSONArray("AccountLanding");
			strConstArray3 =jObj.getJSONArray("DataView");


			JSONObject jObj1 =strConstArray1.getJSONObject(0);
			JSONObject jObj2 =strConstArray2.getJSONObject(0);
			JSONObject jObj3 =strConstArray3.getJSONObject(0);

			ConstString.uid =jObj1.getString("uid");	
			ConstString.passid=jObj1.getString("passid");
			ConstString.signbuttonid =jObj1.getString("signbuttonid");
			ConstString.cornersettingid =jObj1.getString("cornerSettingMenu");
			ConstString.logoutbuttonid=jObj1.getString("logoutButtonId");
			ConstString.overlayId=jObj1.getString("overlayId");

			ConstString.Addnewbuttonid  = jObj2.getString("addNewButtonId");
			ConstString.fileuploadbuttonid= jObj2.getString("fileUploadButtonId");
			ConstString.Addnewfileid =jObj2.getString("addNewFileId");
			ConstString.deleteButtonInWindow =jObj2.getString("deleteButtonInWindow");
			ConstString.deleteMsgOnTop =jObj2.getString("deleteMsgOnTop");

			ConstString.addtaskButton=jObj3.getString("addtaskButton");
			ConstString.taskPanel=jObj3.getString("taskPanel");
			ConstString.OnlyRowInstatus=jObj3.getString("OnlyRowInstatus");
			ConstString.anyRuleDropDown = jObj3.getString("anyRuleDropDown");	
			ConstString.anyRuleNameInPipeline = jObj3.getString("anyRuleNameInPipeline");	
			ConstString.stepDescriptionInPipeline = jObj3.getString("stepDescriptionInPipeline");	
			ConstString.infoIconInPipeline = jObj3.getString("infoIconInPipeline");	
			
			JSONArray jArrayRemove= jObj3.getJSONArray("RemoveDuplicates");
			JSONObject jObjRemove =jArrayRemove.getJSONObject(0);
			ConstString.totalStepsInPipeline = jObjRemove.getString("totalStepsInPipeline");	
			ConstString.applyButtonInRemove = jObjRemove.getString("applyButtonInRemove");
			
			ConstString.deleteInPipeline =jObjRemove .getString("deleteInPipeline");
			ConstString.deletetaskTitle =jObjRemove .getString("deletetaskTitle");

			JSONArray jArrayGroup= jObj3.getJSONArray("GroupPivotRule");
			JSONObject jObjGroup =jArrayGroup.getJSONObject(0);
			ConstString.engineColumn =jObjGroup.getString("engineColumn");

			JSONArray jArrayFilter= jObj3.getJSONArray("Filter");
			JSONObject jObjFilter =jArrayFilter.getJSONObject(0);
			ConstString.filterFileID= jObjFilter .getString("filterFileID");	
			ConstString.filter= jObjFilter .getString("filter");	
			ConstString.filterTitle= jObjFilter .getString("filterTitle");	
			ConstString.columnDropDown= jObjFilter .getString("columnDropDown");	
			ConstString.operatorDropDown= jObjFilter .getString("operatorDropDown");	
			ConstString.textBoxFilter= jObjFilter .getString("textBoxFilter");	
			ConstString.outputBoxForCondition= jObjFilter .getString("outputBoxForCondition");	
			ConstString.createFilterInPipeline= jObjFilter .getString("createFilterInPipeline");
			
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





	@Test
	public void textColEndCaseFilter() throws InterruptedException, FileNotFoundException, IOException, ParseException {
		driver.get(url);

		driver=functionObj.signIN();
		driver.manage().window().maximize();
		//click on add new button
		(new WebDriverWait(driver,5)).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.Addnewbuttonid))).click();

		driver=functionObj.filter();
		log.info("\t-------------------New Window is opening-------------------"+"\n");
		functionObj.overlay();
		functionObj.pageTitle();


		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj =null;
		JSONParser parser = new JSONParser();  
		Path pathToFile = Paths.get( file.getParentFile().getParent(),"test_suites","Modules","Data View","Table Functions","Filter_values.json");
		String stringFile =pathToFile.toString();
		Object obj;
		obj = parser.parse(new FileReader(stringFile));	
		String fileInString=obj.toString(); 
		jsonObj =new JSONObject(fileInString); 

		jsonArray=jsonObj.getJSONArray("TextCol");// Get  array from json file

		JSONObject subObj  = jsonArray.getJSONObject(0);
		JSONArray endCaseArray =subObj.getJSONArray("EndWithCase");
		ArrayList<String> arrayOfEngineEndWith=	new ArrayList<String>();
		ArrayList<String> arrayOfEngineNotEndWith=		new ArrayList<String>();


		//Fetching data from positive_case
		for(int i=0; i<endCaseArray.length(); i++) 
		{

			JSONObject response = endCaseArray.getJSONObject(i);//Json array contains multiple json objects
			int id= response.getInt("Test_ID");
			String filterCol=response.getString("Filter_Field");
			String condition =response.getString("condition");
			valuestr = response.getString("value_to_verify");
			int rows = response.getInt("rows_After_Rule");


			if(condition.equals("ends with")){
				arrayOfEngineEndWith=	functionObj.commonTextFilter("End_With_Value");
			}
			else if(condition.equals("does NOT end with")){

				arrayOfEngineNotEndWith=functionObj.commonTextFilter("Does_Not_End_With_Value");
			}
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addtaskButton))).click();
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.filter))).click();
			//Assert for title
			Assert.assertTrue(new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.filterTitle))).isDisplayed(), "Filter window don't get displayed");  

			//Select Campaign name column
			Select select =new Select(driver.findElement(By.xpath(ConstString.columnDropDown)));
			select.selectByVisibleText(filterCol);
			//select operator from dropdown
			Select selectOperator =new Select(driver.findElement(By.xpath(ConstString.operatorDropDown)));
			selectOperator.selectByVisibleText(condition);
			driver.findElement(By.xpath(ConstString.textBoxFilter)).sendKeys(valuestr);
			Thread.sleep(500);
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.applyButtonInRemove))).click();
			functionObj.overlay();
			if(id==1){
				 new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.taskPanel))).click();
				 }
			//find filter  in pipeline
			Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.createFilterInPipeline))).isDisplayed(), "Create filter don't get displayed");
			functionObj.engineColResultantDataInFilter();

			if(id==1){

				log.info("Engine column values that ends with sn are :"+arrayOfEngineEndWith);
				for(int k=0;k<functionObj.engineColResultantDataInFilter().size();k++){
				Assert.assertEquals(functionObj.engineColResultantDataInFilter().get(k), arrayOfEngineEndWith.get(k), "Engine col values does not end with text value  "+valuestr);
				}
				log.info("Updated Text Engine column contains only those values that are  end with value  :"+valuestr);
				WebElement row_status= driver.findElement(By.xpath(ConstString.OnlyRowInstatus));
				String updatedRows =row_status.getText().toString();
				String textRemove =updatedRows.replace("rows", "");
				String finalStringValue= textRemove.trim();
				int rowStatus =Integer.valueOf(finalStringValue);
				Assert.assertEquals(rowStatus, rows, "Number of rows after applying filter is not equal to values provided in json");
				log.info("Total number of rows that end with sn in Engine col is :"+rowStatus);

				// Again Click on pipeline dropdown
				functionObj.deleteRuleInPipeline();
			}


			if(id==2){  

				log.info("Engine column values that is not end with" +valuestr +"in list are :"+arrayOfEngineNotEndWith);
				for(int k=0;k<functionObj.engineColResultantDataInFilter().size();k++){
				Assert.assertEquals(functionObj.engineColResultantDataInFilter().get(k), arrayOfEngineNotEndWith.get(k), "Engine col  still shows the  values that ends with text  sn");
				}
				log.info("Updated Text Engine column contains only those values that ends with text :"+valuestr);
				WebElement row_status= driver.findElement(By.xpath(ConstString.OnlyRowInstatus));
				String updatedRows =row_status.getText().toString();
				String textRemove =updatedRows.replace("rows", "");
				String finalStringValue= textRemove.trim();
				int rowStatus =Integer.valueOf(finalStringValue);
				Assert.assertEquals(rowStatus, rows, "Number of rows after applying filter is not equal to values provided in json");
				log.info("Total number of rows that ends with text value sn is :"+rowStatus);

			}


		}
		functionObj.softAssert.assertAll();
	}
}

