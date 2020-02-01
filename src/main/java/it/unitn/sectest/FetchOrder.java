package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchOrder extends BaseTest {
	
	@Test
	public void fetchOrderTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product
		 * 5. Add Order with XSS in client name and contact
		 * 6. Check on the manage orders page that the name and contact contains the attack
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "<h1>ClientNameTest</h1>", "<h1>ClientContactTest</h1>","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton.click();
		//Press the Manage Orders menu button
		WebElement manageOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavManageOrder\"]/a"));
		manageOrdersMenuButton.click();
		
		//find the table data that contains the test name and its following sibling 
		WebElement clientName = driver.findElement(By.xpath("//td[contains(.,'ClientNameTest')]"));
		WebElement clientContact = driver.findElement(By.xpath("//td[contains(.,'ClientNameTest')]/following-sibling::td[1]"));
		
		String nameInnerHtml = clientName.getAttribute("innerHTML");
		String contactInnerHtml = clientContact.getAttribute("innerHTML");
		
		assertEquals("<h1>ClientNameTest</h1>", nameInnerHtml);	
		assertEquals("<h1>ClientContactTest</h1>", contactInnerHtml);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
