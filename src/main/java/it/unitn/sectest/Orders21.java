package it.unitn.sectest;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Orders21 extends BaseTest {
	
	@Test
	public void orders21Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product 
		 * 5. Add Order with XSS attack in client name, closing the already opened input tag
		 * 5. Edit the added order and check that XSS attack is active
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "\"><h1>ClientNameTest</h1>", "ClientContactTest","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[5]/a"));
		dropDownOrdersButton.click();
		//Press the Manage Order menu button
		WebElement manageOrdersMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[5]/ul/li[2]/a"));
		manageOrdersMenuButton.click();
		
		int rowNum = driver.findElements(By.xpath("//*[@id=\"manageOrderTable\"]/tbody/tr")).size();
		
		WebElement actionButton = null;
		WebElement editButton = null;
		
		for(int i =1; i<=rowNum; i++) {
			if( driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[2]")).getText().contains("2020-01-15") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[3]")).getText().contains("ClientNameTest") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[4]")).getText().contains("ClientContactTest") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[5]")).getText().contains("1") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[6]")).getText().contains("No Payment")) {
				
				actionButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/button"));
				actionButton.click();
				editButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/ul/li[1]/a"));

				break;
			}

		}
		
		if(actionButton == null) fail("Row not found");
		
		editButton.click();
		
		WebElement attack =null;
		try {
			attack = driver.findElement(By.xpath("//h1[contains(., 'ClientNameTest')]"));
		}  catch (Exception Ex) {
			
		}

		assertNotEquals(null, attack);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
