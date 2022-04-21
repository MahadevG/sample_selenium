package testmammoth;

import java.util.Iterator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
	Logger log = Logger.getLogger(TestListener.class.getName());
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

	// Following are all the method stubs that you do not have to implement

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailure(ITestResult result) {
		// TODO  Auto-generated method stub
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
	}
}  // ends the class

