package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchBrand extends BaseTest {
	
	@Test
	public void FetchBrandTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Brand
		 * 3. Check brand name in the Brand Page
		 */
		
		login("admin", "admin");
		addBrand("<h1>BrandTest</h1>", true);
		
		//find the table data that contains the test name
		WebElement brandName = driver.findElement(By.xpath("//td[contains(.,'BrandTest')]"));
		
		String innerHtml = brandName.getAttribute("innerHTML");
		
		assertEquals("<h1>BrandTest</h1>", innerHtml);	
		
	}
	
	@After
	public void reset() {		

		deleteBrand("BrandTest");
	
	}

}
