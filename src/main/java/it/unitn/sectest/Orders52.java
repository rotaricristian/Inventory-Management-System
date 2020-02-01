package it.unitn.sectest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import utils.BaseTest;

public class Orders52 extends BaseTest {
	
	@Test
	public void orders52Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product 
		 * 5. Add Order 
		 * 6. Edit order and insert XSS attack in GSTN field
		 * 7. Refresh page and check for XSS attack
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "ClientNameTest", "ClientContactTest","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
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
		//press the edit button
		editButton.click();
		
		WebElement gstnField = driver.findElement(By.xpath("//*[@id=\"gstn\"]"));
		gstnField.sendKeys("\"/><script>alert(1)</script>");
		
		Select paymentStatusDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentStatus\"]")));
		paymentStatusDropdown.selectByVisibleText("No Payment");
		
		try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Press the Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"editOrderBtn\"]"));
		saveChangesButton.click();
		
		try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		driver.navigate().refresh();
		
		//accept 1 alert
		int count= 0;
		try 
	    { 
			do {
				driver.switchTo().alert().accept();
		        count++;
			}while(true);
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	    	//no more alerts
	    }  
		
		//Must be 0 when code is fully patched
		assertEquals(1, count);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
