package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Product1And3 extends BaseTest {
	
	@Test
	public void product1And3Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Brand with XSS in name
		 * 3. go to product page and check if 2 XSS attacks are present
		 */
		
		login("admin", "admin");
		addBrand("BrandTest <script>alert(1)</script>", true);
		
		//Press the Product dashboard button
		WebElement productButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[4]/a"));
		productButton.click();
 
		
		int  count= 0;
		try 
	    { 
	        driver.switchTo().alert().accept(); 
	        count++;
	        driver.switchTo().alert().accept();
	        count++;
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	    	//no alerts
	    }  
		
		
		assertEquals(2,count);	
		
	}
	
	@After
	public void reset() {		

		//delete Brand with extra sleep time
		//Press the Brand dashboard button
		WebElement brandButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[2]/a"));
		brandButton.click();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//accept all alerts
		try 
	    { 
	        do{
	        	try {
	        		Thread.sleep(1000);
	        	} catch (InterruptedException e) {
	        		e.printStackTrace();
	        	}
	        	driver.switchTo().alert().accept(); 
	        }while(true);

	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    } 
		
		
		//find the button from the sibling of the table data that contains the name of the brand 
		WebElement actionButton = driver.findElement(By.xpath("//td[contains(.,'BrandTest')]/following-sibling::td[2]/div/button"));
		actionButton.click();
		
		//find the remove button
		WebElement removeButton = driver.findElement(By.xpath("//td[contains(.,'BrandTest')]/following-sibling::td[2]/div/ul/li[2]/a"));
		removeButton.click();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeBrandBtn\"]"));
		saveChangesButton.click(); 
		
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}

}
