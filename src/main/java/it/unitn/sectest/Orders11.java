package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Orders11 extends BaseTest {
	
	@Test
	public void orders11Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product with XSS in name
		 * 5. Check on the Add Order page that the XSS attack is successful 3 times as there are 3 dropdown menues 
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct<script>alert(1)</script>","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
 
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton.click();
		//Press the Add Orders menu button
		WebElement addOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavAddOrder\"]/a"));
		addOrdersMenuButton.click();
		
		int count= 0;
		try 
	    { 
	        driver.switchTo().alert().accept(); 
	        count++;
	        driver.switchTo().alert().accept();
	        count++;
	        driver.switchTo().alert().accept();
	        count++;
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //not enough alerts
	    }  
		
		
		assertEquals(3, count);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
	
	}

}
