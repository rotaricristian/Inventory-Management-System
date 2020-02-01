package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchCategories extends BaseTest {
	
	@Test
	public void FetchCategoriesTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Check category name in the Category page
		 */
		
		login("admin", "admin");
		addCategory("<h1>CategoryTest</h1>", true);
		
		//find the button from the sibling of the table data that contains the name of the category 
		WebElement categoryName = driver.findElement(By.xpath("//td[contains(.,'CategoryTest')]"));
		
		String innerHtml = categoryName.getAttribute("innerHTML");
		
		assertEquals("<h1>CategoryTest</h1>", innerHtml);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
	
	}

}
