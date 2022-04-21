package testmammoth;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

public class RetryListener implements IAnnotationTransformer  {
 

@Override
public void transform(ITestAnnotation annotation, Class testClass,
		Constructor testConstructor, Method testMethod) {
	// This will set retryAnalyzer to the test methods if not set in class level
			if (annotation.getRetryAnalyzerClass() == null) {

				annotation.setRetryAnalyzer(Retry.class);

			}
	
}
}
