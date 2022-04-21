package testmammoth.Listeners;


import java.util.Set;
import java.util.logging.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import testmammoth.Common.CommonFunction;
import testmammoth.SetUp.AllConstant;


public class Listener implements ITestListener {
	Logger log = Logger.getLogger(Listener.class.getName());
	AllConstant var =new AllConstant();
	CommonFunction functionObj = new CommonFunction();
    @Override
    public void onTestStart(ITestResult iTestResult) {
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
    }
    
    @Override
    public void onTestFailure(ITestResult iTestResult) {
    	log.info("----------Test was failed----------");
    	functionObj.switchToLanding();

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
    	log.info("----------Test was skipped----------");
    	functionObj.switchToLanding();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext context) {
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
		Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();

		for (ITestResult temp : failedTests) {

			ITestNGMethod method = temp.getMethod();	
			if (context.getFailedTests().getResults(method).size() > 1) {
				 System.out.println("failed test case remove as duplicate :" + failedTests.getClass().toString());
				failedTests.remove(temp);
			} 
			else {
				if (context.getPassedTests().getResults(method).size() > 0) {
				
					  System.out.println("failed test case remove as pass retry:" + failedTests.getClass().toString());
					failedTests.remove(temp);
				}

			}

		}
		
		for (ITestResult temp : skippedTests) {

			ITestNGMethod method = temp.getMethod();

			if (context.getSkippedTests().getResults(method).size() > 1) {
			

				skippedTests.remove(temp);

			} else {

				if (context.getPassedTests().getResults(method).size() > 0) {
					skippedTests.remove(temp);

				}
				int skippedSizeAfterRemove=skippedTests.size();
				

			}

		}
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
}