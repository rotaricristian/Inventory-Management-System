package it.unitn.sectest;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchProductData extends BaseTest {
	
	@Test
	public void fetchProductDataTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category 
		 * 3. Add Brand
		 * 4. Add Product with XSS alert in name
		 * 5. Access Add Orders page and accept 3 alerts
		 * 6. On the Add Orders page press Add Row button and check that the alert took place
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("<script>alert(123456)</script>","1", "2", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton.click();
		//Press the Add Order menu button
		WebElement addOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavAddOrder\"]/a"));
		addOrdersMenuButton.click();
		
		//accept 3 possible alerts
		try 
	    { 
			while(true) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				driver.switchTo().alert().accept();
			}
	         
	    }catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    }  
		
		//press Add Row button
		WebElement addRowButton = driver.findElement(By.xpath("//*[@id=\"addRowBtn\"]"));
		addRowButton.click();

		
		//check that the alert is present
		boolean present= false;
		try 
	    { 
	        driver.switchTo().alert().accept(); 
	        present = true;
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        present = false;
	    }  
		
		
		assert(present);	

		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("123456");
	
	}

}
