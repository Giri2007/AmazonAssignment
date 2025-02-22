package com.selenium.SeleniumTr;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;

public class testNgAmazon {

	WebDriver driver = null;
	String url = "http://www.amazon.in/";
	int MrpCounter = 0;

	
	@BeforeMethod
	public void amazonPage() {
	System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chrome\\chromedriver.exe");
	driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.get(url);
	}
	
	public void explicitWait(WebElement webElement) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}

	
//	@Test
	public void clickSearch() throws InterruptedException {
		WebElement requiredValue = driver.findElement(By.xpath("//*[@class='nav-search-field ']/label"));
		JavascriptExecutor exc = (JavascriptExecutor) driver;
		//exc.executeScript("arguments[0].setAttribute('style','display: unset;');",requiredValue);
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style','display: block;');",requiredValue);
		explicitWait(requiredValue);
		Assert.assertEquals(requiredValue.getText(), "Search Amazon.in");
		System.out.println(requiredValue.getText());
		//requiredValue.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
		exc.executeScript("arguments[0].value =' ';", requiredValue);
		//requiredValue.clear();
		driver.findElement(By.id("nav-search-submit-button")).click();
		WebElement searchox = driver.findElement(By.cssSelector("#twotabsearchtextbox"));
		searchox.sendKeys("EarPhones");	
	}
	
	public void performScroll(WebElement element) {
		Actions actions = new Actions(driver);
		actions.scrollToElement(element);
	}
	
	public void ApplyFilters(String element) throws InterruptedException {
		WebElement TypeFilter = driver.findElement(By.xpath(element));
		explicitWait(TypeFilter);
		performScroll(TypeFilter);
		TypeFilter.click();
		Thread.sleep(2000);
	}
	
	
	@Test
	public void products() throws InterruptedException {
		Actions actions = new Actions(driver);
		WebElement Searchbox = driver.findElement(By.xpath("//*[contains(@class,'nav-search-field ')]//*[@role='searchbox']"));
		Searchbox.sendKeys("Wrist Watches");
		driver.findElement(By.cssSelector("#nav-search-submit-button")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
			
		ApplyFilters(xpaths.AnalougeFilter);//*[@class='a-offscreen']
		ApplyFilters(xpaths.BandMaterial);
		ApplyFilters(xpaths.Brand);
		Thread.sleep(3000);
		WebElement lowerSlider = driver.findElement(By.xpath(xpaths.lowerPrice));
		WebElement upperSlider = driver.findElement(By.xpath(xpaths.upperPrice));
		actions.clickAndHold(lowerSlider).moveByOffset(150, 0).release().perform();
		actions.moveToElement(upperSlider).clickAndHold().moveByOffset(-100, 0).release().perform();
		ApplyFilters(xpaths.discount);
		
		List<WebElement> products = driver.findElements(By.xpath(xpaths.Products));
		for (int i=0;i<products.size();i++) {
			MrpCounter++;
			if (i == 2) {
				int Counter = i+1;
				String PriceLoc = xpaths.Products + "[" + Counter + "]" + xpaths.Price;
				String PriceOfProduct = driver.findElement(By.xpath(PriceLoc)).getText();
				System.out.println(PriceOfProduct);
				String MRPLoc = xpaths.Products + "[" + Counter + "]" + xpaths.MrpPrice;
				String MrpOfProduct = driver.findElement(By.xpath(MRPLoc)).getText();
				System.out.println(MrpOfProduct);
				String PercentLoc = xpaths.Products + "[" + Counter + "]" + xpaths.percentOff;
				String PercOfProduct = driver.findElement(By.xpath(PercentLoc)).getText();
				System.out.println(PercOfProduct);
			}
		}
		Thread.sleep(5000);
		
	}
	
//	@AfterTest
	public void closeBrowser() {
		driver.quit();
	}
}

class xpaths {

	public static String AnalougeFilter = "//*[text()='Watch Display Type']//ancestor::*[@role='group']//*[contains(@class,'list-item')]//*[text()='Analogue']";
	public static String BandMaterial = "//*[text()='Band Material']//ancestor::*[@role='group']//*[contains(@class,'list-item')]//*[text()='Leather']";
	public static String Brand = "//*[text()='Brands']//ancestor::*[@role='group']//*[contains(@class,'list-item')]//*[text()='Titan']" ;
	public static String discount = "//*[text()='Discount']//ancestor::*[@role='group']//*[contains(@class,'list-item')]//*[text()='25% Off or more']";
	public static String PriceFilter = "//*[text()='Price']//ancestor::*[@role='group']//*[contains(@class,'list-item')]//*[text()]";
	public static String Products = "(//*[@role='listitem']//*[contains(@class,'a-spacing-small')])";
	public static String Price = "//*[@data-cy='price-recipe']//*[contains(@class,'price-whole')]";
	public static String MrpPrice = "//*[@data-cy='price-recipe']//*[contains(@class,'a-text-price')]//*[@aria-hidden]";
	public static String percentOff = "//following-sibling::span[contains(text(),'off')]";
	public static String lowerPrice = "//*[@id='priceRefinements']//*[contains(@id,'lower-bound-slider')]";
	public static String upperPrice = "//*[@id='priceRefinements']//*[contains(@id,'upper-bound-slider')]";

}
