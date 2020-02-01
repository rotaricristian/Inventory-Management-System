package it.unitn.sectest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.BaseTest;

public class Orders35_37And39 extends BaseTest {
	
	@Test
	public void orders35_37And39Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product 
		 * 5. Add Order and edit the total value input fields with an XSS attack
		 * 6. Edit order and get orderID, then change the quantity in the DB
		 * 6. Refresh the edit order page and check for 3 attacks - one for each input
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		//Add Order 
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton.click();
		//Press the Add Order menu button
		WebElement addOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavAddOrder\"]/a"));
		addOrdersMenuButton.click();
		
		//Fill in the fields with data
		WebElement dateTextBox = driver.findElement(By.xpath("//*[@id=\"orderDate\"]"));
		WebElement clientNameBox = driver.findElement(By.xpath("//*[@id=\"clientName\"]"));
		WebElement clientContactTextBox = driver.findElement(By.xpath("//*[@id=\"clientContact\"]"));
		Select productDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"productName1\"]")));
		WebElement productQuantityTextBox = driver.findElement(By.xpath("//*[@id=\"quantity1\"]"));
		WebElement discountTextBox = driver.findElement(By.xpath("//*[@id=\"discount\"]"));
		WebElement paidAmountTextBox = driver.findElement(By.xpath("//*[@id=\"paid\"]"));
		Select paymentTypeDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentType\"]")));
		Select paymentStatusDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentStatus\"]")));
		Select paymentPlaceDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentPlace\"]")));
		
		dateTextBox.sendKeys("01/15/2020");
		clientNameBox.sendKeys("ClientNameTest");
		clientNameBox.click();
		clientContactTextBox.sendKeys("ClientContactTest");
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productDropdown.selectByVisibleText("TestProduct");
		productQuantityTextBox.sendKeys("1");
		discountTextBox.sendKeys("1");
		paidAmountTextBox.sendKeys("1");
		paymentTypeDropdown.selectByVisibleText("Cash");
		paymentStatusDropdown.selectByVisibleText("No Payment");
		paymentPlaceDropdown.selectByVisibleText("In Gujarat");
		
		//set the attack value through JS to both inputs as Selenium can not handle hidden elements
		RemoteWebDriver r=(RemoteWebDriver) driver;
		String s2="document.getElementById('totalValue1').value='1\"><script>alert(1)</script>'";
		r.executeScript(s2);
		
		//remove the other 2 products 
		WebElement deleteProduct2Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct2Button.click();
		WebElement deleteProduct3Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct3Button.click();
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createOrderBtn\"]"));
		saveChangesButton.click();
		
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
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
		
		//accept already present 2 alerts
		try 
	    { 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        driver.switchTo().alert().accept(); 
	        try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        driver.switchTo().alert().accept(); 
	       
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    }  
		
		//get order ID from page
		String script = "return document.getElementById('orderId').getAttribute('value');";
		String value = r.executeScript(script).toString();
		
		//change the quantity in the DB
		executeDatabaseQuery("Update order_item set quantity='\"><script>alert(1)</script>' where order_id =" + value + ";");	
		
		//reload page
		driver.navigate().refresh();
		
		//accept 3 alerts - for each input 1 alert
		int count =0;
		try 
	    { 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        driver.switchTo().alert().accept(); 
	        count++;
	        try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        driver.switchTo().alert().accept(); 
	        count++;
	        try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        driver.switchTo().alert().accept();
	        count++;
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    }  
		
		assertEquals(3, count);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
