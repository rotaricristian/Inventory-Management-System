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

public class Orders41To55 extends BaseTest {
	
	@Test
	public void orders41To55Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product 
		 * 5. Add Order and edit the value input fields with an XSS attack
		 * 6. Edit order and check for 12 XSS attacks
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
		
		//remove the other 2 products 
		WebElement deleteProduct2Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct2Button.click();
		WebElement deleteProduct3Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct3Button.click();
		
		
		//set the attack values through JS to inputs as Selenium can not handle some hidden elements
		RemoteWebDriver r=(RemoteWebDriver) driver;
		String discount="document.getElementById('discount').value='1\"><script>alert(1)</script><br'";
		r.executeScript(discount);
		String paid="document.getElementById('paid').value='1\"><script>alert(2)</script><br'";
		r.executeScript(paid);
		String subTotalValue="document.getElementById('subTotalValue').value='1\"><script>alert(3)</script>'";
		r.executeScript(subTotalValue);
		String totalAmountValue="document.getElementById('totalAmountValue').value='1\"><script>alert(4)</script><br'";
		r.executeScript(totalAmountValue);
		String grandTotalValue="document.getElementById('grandTotalValue').value='1\"><script>alert(5)</script><br'";
		r.executeScript(grandTotalValue);
		String vatValue="document.getElementById('vatValue').value='1\"><script>alert(6)</script><br'";
		r.executeScript(vatValue);
		String dueValue="document.getElementById('dueValue').value='1\"><script>alert(7)</script><br'";
		r.executeScript(dueValue);
	
		
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
		//press the edit button
		editButton.click();
		
		//accept 12 alerts 
		int count= 0;
		try 
	    { 
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				driver.switchTo().alert().accept();
		        count++;
			}while(true);
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	    	//no more exceptions
	    }  
		
		//Must be 0 when code is fully patched
		assertEquals(12, count);	
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
