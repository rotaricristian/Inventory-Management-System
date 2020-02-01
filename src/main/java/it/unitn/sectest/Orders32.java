package it.unitn.sectest;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Orders32 extends BaseTest {
	
	@Test
	public void orders32Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product 
		 * 5. Add Order
		 * 6. Edit product to contain XSS in quantity field
		 * 7. Edit order and check for XSS attack
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct32","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "ClientNameTest32", "ClientContactTest","TestProduct32", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
		//Press the Product dashboard button
		WebElement productButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[4]/a"));
		productButton.click();

		int rowNumProd = driver.findElements(By.xpath("//*[@id=\"manageProductTable\"]/tbody/tr")).size();
		
		WebElement productActionButton = null;
		WebElement productEditButton = null;
		
		//find the edit button for the product
		for(int i =1; i<=rowNumProd; i++) {
			if( driver.findElement(By.xpath("//table[@id='manageProductTable']/tbody/tr[" + i +"]/td[2]")).getText().contains("TestProduct32") ) {
				
				productActionButton = driver.findElement(By.xpath("//table[@id='manageProductTable']/tbody/tr[" + i +"]/td[8]/div/button"));
				productActionButton.click();
				productEditButton = driver.findElement(By.xpath("//table[@id='manageProductTable']/tbody/tr[" + i +"]/td[8]/div/ul/li[1]/a"));

				break;
			}

		}
		
		if(productActionButton == null) fail("Row not found");
		
		productEditButton.click();
		//wait for the modal to appear
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebElement 	productInfoButton = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[2]/div[2]/ul/li[2]/a"));
		productInfoButton.click();
		
		//Insert attack
		WebElement 	quantityField = driver.findElement(By.xpath("//*[@id=\"editQuantity\"]"));
		quantityField.sendKeys("900</p><script>alert(12345)</script><p>");
		
		//Save changes
		WebElement 	saveChangesButton = driver.findElement(By.xpath("//*[@id=\"editProductBtn\"]"));
		saveChangesButton.click();
		
		//accept the possible alert the alert
		try 
	    { 
	        driver.switchTo().alert().accept(); 
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no alert present
	    }   
		
		//close modal
		WebElement 	closeModalButton = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[1]/button"));
		closeModalButton.click();
		//wait for the modal to disappear
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton2 = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton2.click();
		//Press the Manage Orders menu button
		WebElement manageOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavManageOrder\"]/a"));
		manageOrdersMenuButton.click();
		
		
		int rowNum = driver.findElements(By.xpath("//*[@id=\"manageOrderTable\"]/tbody/tr")).size();
		
		WebElement actionButton = null;
		WebElement editButton = null;
		
		//find the edit button for the order
		for(int i =1; i<=rowNum; i++) {
			if( driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[2]")).getText().contains("2020-01-15") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[3]")).getText().contains("ClientNameTest32") &&
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
		//check for the alert
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	

		try 
	    { 
	        driver.switchTo().alert().accept(); 
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        fail("No alert present");
	    }  
			
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
