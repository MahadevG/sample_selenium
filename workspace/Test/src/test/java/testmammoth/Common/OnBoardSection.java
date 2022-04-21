package testmammoth.Common;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import testmammoth.SetUp.AllConstant;
import testmammoth.SetUp.DriverInitialize;

public class OnBoardSection {
    Logger log = Logger.getLogger(OnBoardSection.class.getName());
    AllConstant var =new AllConstant();
    CommonFunction functionObj =new CommonFunction();
    WebDriver driver;
    DriverInitialize objDriver =new DriverInitialize();
    @BeforeTest
    public void setUp() throws FileNotFoundException, IOException, ParseException, InterruptedException {
       
       // objDriver.setTestSuite();
        //  driver = DriverInitialize.getDriver();
        driver =  functionObj.getJSONDataAndDriverForCommon();
        try { 
            //Create log file to see the result
            Path logFile = Paths.get( var.file.getParentFile().getParent(),"test_suites", "Result","OnBoardSection.log");
            String stringFile= logFile.toString();
            // This block configure the logger with handler and formatter  
            var.fileHandler = new FileHandler(stringFile);  
            log.addHandler(var.fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();  
            var.fileHandler.setFormatter(formatter);  

            // the following statement is used to log any messages  
            log.info("Log file for On-board");  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
	@Test(priority =1,enabled =true,description ="disabling on-board status  here")
	public void onBoardStatus() throws InterruptedException, FileNotFoundException, IOException, ParseException {
	    var.jsonData();
        // functionObj.getJSONDataAndDriverForCommon();
        driver.get(var.url);
        functionObj.signIN(); 
		driver.get(var.url+"#landing?disableOnBoarding=1");
		functionObj.overlay();
		// find corner setting menu on left side
		   functionObj.signOut();
		log.info("Onboard functionality disabled successfully");

	}
}