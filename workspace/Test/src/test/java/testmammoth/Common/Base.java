package testmammoth.Common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testmammoth.SetUp.DriverInitialize;


public class Base {
	
	public WebElement getElement(String locator) {
		WebDriver driver = DriverInitialize.getDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if (locator.contains("//"))
			return driver.findElement(By.xpath(locator));
		return null;
	}
	
	public String getTextOfAnElement(String locator) {
		WebElement element = getElement(locator);
		return element.getText();
	}
	
	public void moveToElement(String locator, WebDriver driver) {
		Actions action = new Actions(driver);
		action.moveToElement(getElement(locator)).build().perform();;

	}
	
	public WebDriverWait waits() {
		return (new WebDriverWait(DriverInitialize.getDriver(), 20));
	}
	
	public WebElement waitForClickable(String locator) {
		if(locator.contains("//"))
			return waits().until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		return null;
	}
	
	public WebElement waitForVisibility(String locator) {
		if(locator.contains("//"))
			return waits().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		return null;
	}
	
	public Boolean waitForInvisibility(String locator) {
		if(locator.contains("//"))
			return waits().until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		return null;
	}
	
	public List<WebElement> waitForAllElementsVisible(String locator) {
		if(locator.contains("//"))
			return waits().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator)));
		return null;
	}
	
	public void click(String locator) throws InterruptedException {
		if (locator.contains("//")) {
			try {
			waitForClickable(locator).click(); }
			catch (ElementClickInterceptedException e) {
				Thread.sleep(5000);
				waitForClickable(locator).click();
			}
		}
	}
	
	public void clearTextField(String locator) {
		if (locator.contains("//")) {
			waitForVisibility(locator).clear();
		}
	}
	
	public void type(String locator, String textToType) {
		if(locator.contains("//"))
			waitForVisibility(locator).sendKeys(textToType);
	}
	
	public List<WebElement> getOptions(String locator){
		if(locator.contains("//"))
			return ( new Select(waitForVisibility(locator)).getOptions() );
		return null;
	}
	public Select select(String locator) {
		if(locator.contains("//"))
			return (new Select(waitForVisibility(locator)));
		return null;
	}
	
	public void selectByText(String locator, String option) {
		if(locator.contains("//"))
			new Select(waitForVisibility(locator)).selectByVisibleText(option);
	}
	
	public void selectByIndex(String locator, int index) {
		if(locator.contains("//"))
			new Select(waitForVisibility(locator)).selectByIndex(index);
	}
	
	public void pressKeyboard(String locator, Keys key) {
		if(locator.contains("//"))
			waitForVisibility(locator).sendKeys(key);
	}
	
	public boolean elementIsDisplayed(String locator) {
		if (locator.contains("//")) {
		return waitForVisibility(locator).isDisplayed();	}
		return false;
	}
	
}
