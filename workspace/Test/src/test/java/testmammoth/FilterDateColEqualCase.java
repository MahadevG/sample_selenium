package testmammoth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FilterDateColEqualCase {
	Logger log = Logger.getLogger(FilterDateColEqualCase.class.getName());
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
			Path logFile = Paths.get( file.getParentFile().getParent(),"test_suites", "Result","FilterDateColEqualityCase.log");
			String stringFile= logFile.toString();
			// This block configure the logger with handler and formatter  
			fileHandler = new FileHandler(stringFile);  
			log.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);  

			// the following statement is used to log any messages  
			log.info("Log file for Filter Date Column function in Data view");  

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
			ConstString.taskPanel=jObj3.getString("taskPanel");
			ConstString.addtaskButton=jObj3.getString("addtaskButton");
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
		
			ConstString.updatedDateCol= jObjFilter .getString("updatedDateCol");		
			ConstString.yearInputBox= jObjFilter .getString("yearInputBox");
			ConstString.currentMonthName= jObjFilter .getString("currentMonthName");	
			ConstString.daysInList= jObjFilter .getString("daysInList");	
			ConstString.nextMonthArrow= jObjFilter .getString("nextMonthArrow");	
			ConstString.dateInputBoxInDateInputWindow= jObjFilter .getString("dateInputBoxInDateInputWindow");
			
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
	public void dateColEqualityCaseFilter() throws InterruptedException, FileNotFoundException, IOException, ParseException, java.text.ParseException {
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

		jsonArray=jsonObj.getJSONArray("DateCol");// Get  array from json file

		JSONObject subObj  = jsonArray.getJSONObject(0);
		JSONArray equalCaseArray =subObj.getJSONArray("EqualCase");
		ArrayList<Date> arrayOfDateEqual=	new ArrayList<Date>();
		ArrayList<Date> arrayOfDateNotEqual= new ArrayList<Date>();
		SimpleDateFormat sdfoutDate = new SimpleDateFormat("MM/dd/yyyy");
		ArrayList<String> list=new ArrayList<String>();

		//Fetching data from positive_case
		for(int i=0; i<equalCaseArray.length(); i++) 
		{

			JSONObject response = equalCaseArray.getJSONObject(i);//Json array contains multiple json objects
			int id= response.getInt("Test_ID");
			String filterCol=response.getString("Filter_Field");
			String condition =response.getString("condition");
			int year =response.getInt("select_year");
			String month= response.getString ("select_month");
			int day = response.getInt ("select_day");
			int rows = response.getInt("rows_After_Rule");
			valuestr = response.getString ("completeDate_Verify" );
			//casting int to string for year
			String year_String = String.valueOf(year);

			if(condition.equals("is equal to")){
				arrayOfDateEqual=	functionObj.commonDateFilter("Is_Equal_to");
				for(int j=0;j<arrayOfDateEqual.size();j++){
					list.add(sdfoutDate.format(arrayOfDateEqual.get(j)));
				}

			}
			else if(condition.equals("is NOT equal to")){

				arrayOfDateNotEqual=functionObj.commonDateFilter("Is_Not_Equal_to");
				for(int j=0;j<arrayOfDateNotEqual.size();j++){

					if(arrayOfDateNotEqual.get(j)!=null){

						list.add(sdfoutDate.format(arrayOfDateNotEqual.get(j)));
					}
					else{
						list.add(""); //adding empty value when null date comes
					}				
				}
			}
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.addtaskButton))).click();
			new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.filter))).click();
			//Assert for title
			Assert.assertTrue( (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.filterTitle))).isDisplayed(), "Filter window don't get displayed");  
			//Select date column
			Select select =new Select(driver.findElement(By.xpath(ConstString.columnDropDown)));
			select.selectByVisibleText(filterCol);
			//select operator from dropdown
			Select selectOperator =new Select(driver.findElement(By.xpath(ConstString.operatorDropDown)));
			selectOperator.selectByVisibleText(condition);
			//new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.clickToChoose))).click();		
			//Assert for date input  title
			//Assert.assertTrue( (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.dateInputWindowTitle))).isDisplayed(), "Date input window don't get displayed");  	
			ArrayList<String>monthList= new ArrayList<String>();
			monthList.add("January");
			monthList.add("February");
			monthList.add("March");
			monthList.add("April");
			monthList.add("May");
			monthList.add("June");
			monthList.add("July");
			monthList.add("August");
			monthList.add("September");
			monthList.add("October");
			monthList.add("November");
			monthList.add("December");
			//click on date input box
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.dateInputBoxInDateInputWindow))).click();  
			WebElement curr_month =driver.findElement(By.xpath(ConstString.currentMonthName));
			String currMonth =curr_month.getText().toString();	
			//find  next month
			WebElement next_Month =(new WebDriverWait(driver, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.nextMonthArrow)));

			for(int j=0;j<monthList.size();j++){
				if(!currMonth.equals(month)){
					next_Month.click();
					WebElement updatedcurr_month =driver.findElement(By.xpath(ConstString.currentMonthName));	
					currMonth =updatedcurr_month.getText().toString();	
				}
				else if(currMonth.equals(month)){

					break;

				}	

			}

			//find year
			driver.findElement(By.xpath(ConstString.yearInputBox)).clear();
			driver.findElement(By.xpath(ConstString.yearInputBox)).sendKeys(year_String);
			//picking day from list
			List<WebElement> selectDate =(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(ConstString.daysInList)));
			for(WebElement we :selectDate){
				String date =we.getText().toString();
				int dayVal =Integer.parseInt(date);
				if(dayVal==day){
					we.click();	
					break;
				}

			}

			Thread.sleep(500);
			//click on done
		//	driver.findElement(By.xpath(ConstString.doneInDateInputWindow)).click();
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.applyButtonInRemove))).click();
			functionObj.overlay();
			if(id==1){
			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(ConstString.taskPanel))).click();
			}
			//find filter  in pipeline
			Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ConstString.createFilterInPipeline))).isDisplayed(), "Create filter don't get displayed");
			functionObj.dateColResultantDataInFilter();

			if(id==1){

				log.info("Date column  value  in list  that is equal to 9/29/2008  are    :"+list);
				for(int j=0;j<functionObj.dateColResultantDataInFilter().size();j++){
					Assert.assertEquals(functionObj.dateColResultantDataInFilter().get(j), list.get(j), "Date col value in list and updated date col values are not equal");
				}
				log.info("Updated Date column shows only value that is equal to :9/29/2008 ");
				WebElement row_status= driver.findElement(By.xpath(ConstString.OnlyRowInstatus));
				String updatedRows =row_status.getText().toString();
				String textRemove =updatedRows.replace("rows", "");
				String finalStringValue= textRemove.trim();
				int rowStatus =Integer.valueOf(finalStringValue);
				Assert.assertEquals(rowStatus, rows, "Number of rows after applying filter is not equal to values provided in json");
				log.info("Total number of rows that is equal to 9/29/2008  is :"+rowStatus);

				// Again Click on pipeline dropdown
				functionObj.deleteRuleInPipeline();
				Thread.sleep(3000);
				list.clear();

			}

			if(id==2){  
				log.info("Date column  value  in list  that is not  equal to 9/29/2008  are "+list);
				for(int j=0;j<functionObj.dateColResultantDataInFilter().size();j++){
					Assert.assertEquals(functionObj.dateColResultantDataInFilter().get(j).trim(), list.get(j).trim(), "Date col value in list and updated date col values are not equal");
				}
				log.info("Updated Date column shows only value that is  not equal to :9/29/2008 ");
				WebElement row_status= driver.findElement(By.xpath(ConstString.OnlyRowInstatus));
				String updatedRows =row_status.getText().toString();
				String textRemove =updatedRows.replace("rows", "");
				String finalStringValue= textRemove.trim();
				int rowStatus =Integer.valueOf(finalStringValue);
				Assert.assertEquals(rowStatus, rows, "Number of rows after applying filter is not equal to values provided in json");
				log.info("Total number of rows that is  not equal to 9/29/2008  is :"+rowStatus);
			}				
		}
		functionObj.softAssert.assertAll();
	}
}