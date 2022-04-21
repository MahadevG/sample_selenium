package testmammoth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.IResultMap;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.ResultMap;

public class Retry implements IRetryAnalyzer {
	 /* private static final int MAX_RETRY_COUNT = 1;

	  private final Map<Integer, AtomicInteger> counts = new HashMap<>();

	  private AtomicInteger getCount(ITestResult result) {
	    int id = Arrays.hashCode(result.getParameters());
	    AtomicInteger count = counts.get(id);
	    if (count == null) {
	      count = new AtomicInteger(MAX_RETRY_COUNT);
	      counts.put(id, count);
	    }
	    return count;
	  }

	  @Override
	  public boolean retry(ITestResult result) {
	    int retriesRemaining = getCount(result).getAndDecrement();
	    return retriesRemaining > 0;
	  }*/
    private int retryCount         = 0;
    private int maxRetryCount     = 1;   // retry a failed test 1 additional times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <maxRetryCount) {
        	System.out.println("Retrying test " + result.getName() + " for the " + (retryCount+1) + " time(s).");
            retryCount++;
            return true;
        }
        return false;
    }
	/*private static final int MAX_COUNT = 1;
	  private Map<String, AtomicInteger> retries = new HashMap<String, AtomicInteger>();
	  private boolean isRetryHandleNeeded;
	  private IResultMap failedCases = new ResultMap();

	  public AtomicInteger getCount(ITestNGMethod result, Object[] params) {
	    String id = getId(result, params);
	    if (retries.get(id) == null) {
	      retries.put(id, new AtomicInteger(MAX_COUNT));
	    }
	    return retries.get(id);
	  }

	  public String getId(ITestNGMethod result, Object[] params) {
	    return result.getConstructorOrMethod().getMethod().toGenericString() +
	      ":" + ArrayUtils.toString(params);
	  }

	  public boolean retry(ITestResult result) {
	    boolean ret = false;
	    if (getCount(result.getMethod(), result.getParameters()).intValue() > 0) {
	      System.out.println("Retry test: " + result.getInstanceName() + ":" + result.getName());
	      getCount(result.getMethod(), result.getParameters()).decrementAndGet();
	      ret = true;
	    } else {
	      System.out.println("Finish retrying: " + result.getInstanceName() + ":" + result.getName());
	    }
	    return ret;
	  }

	  public void onTestStart(ITestResult result) {
	  }

	  public void onTestSuccess(ITestResult result) {
	  }

	  public void onTestFailure(ITestResult result) {
	    if (result.getMethod().getRetryAnalyzer() != null) {
	      Retry testRetryAnalyzer = (Retry) result.getMethod().getRetryAnalyzer();
	      if (testRetryAnalyzer.getCount(result.getMethod(), result.getParameters()).intValue() > 0) {
	        result.setStatus(ITestResult.SKIP);
	        Reporter.setCurrentTestResult(null);
	      } else {
	        failedCases.addResult(result, result.getMethod());
	      }
	      isRetryHandleNeeded = true;
	    }
	  }

	  public void onTestSkipped(ITestResult result) {
	  }

	  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	  }

	  public void onStart(ITestContext context) {
	  }

	  public void onFinish(ITestContext context) {
	    if (isRetryHandleNeeded) {
	      removeIncorrectlySkippedTests(context);
	      removeFailedTestsInTestNG(context);
	    }
	  }

	  private void removeIncorrectlySkippedTests(ITestContext test) {
	    List<ITestNGMethod> skipsToRemove = new ArrayList<ITestNGMethod>();
	    IResultMap skippedTests = test.getSkippedTests();
	    for (ITestResult result : skippedTests.getAllResults()) {
	      ITestNGMethod m = getMatchingMethod(result, failedCases.getAllResults());
	      if (m != null) {
	        skipsToRemove.add(m);
	      } else {
	        m = getMatchingMethod(result, test.getPassedTests().getAllResults());
	        if (m != null) {
	          skipsToRemove.add(m);
	        }
	      }
	    }
	    for (ITestNGMethod method : skipsToRemove) {
	      System.out.println("Matches: "+countMatches(skippedTests, method));
	      skippedTests.removeResult(method);
	    }
	  }

	  private int countMatches(IResultMap skippedTests, Object m){
	    int ret = 0;
	    for (ITestNGMethod entry : skippedTests.getAllMethods()) {
	      if (entry.equals(m)) {
	        ret++;
	      }
	    }
	    return ret;
	  }

	  private ITestNGMethod getMatchingMethod(ITestResult toFind, Set<ITestResult> results) {
	    for (ITestResult result : results) {
	      if (getId(toFind).equals(getId(result))) {
	        return result.getMethod();
	      }
	    }
	    return null;
	  }

	  private void removeFailedTestsInTestNG(ITestContext test) {
	    IResultMap failedTests = test.getFailedTests();
	    Comparator<ITestResult> comparator = new Comparator<ITestResult>() {
	      public int compare(ITestResult o1, ITestResult o2) {
	        return getId(o1).compareTo(getId(o2));
	      }
	    };
	    List<ITestNGMethod> duplicates = new ArrayList<ITestNGMethod>();
	    Set<ITestResult> resultsSet = new TreeSet<ITestResult>(comparator);
	    for (ITestResult failed : failedTests.getAllResults()) {
	      if (!resultsSet.add(failed)) {
	        duplicates.add(failed.getMethod());
	      }

	    }
	    for (ITestNGMethod testMethod : duplicates) {
	      failedTests.removeResult(testMethod);
	    }
	  }

	  private String getId(ITestResult result) {
	    return getId(result.getMethod(), result.getParameters());
	  }*/

	
	
}