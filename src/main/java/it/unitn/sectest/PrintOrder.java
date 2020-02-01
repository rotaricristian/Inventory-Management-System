package it.unitn.sectest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import utils.BaseTest;

public class PrintOrder extends BaseTest {
	
	@Test
	public void printOrderTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product with XSS in rate and product name
		 * 5. Add Order with client name, contact to contain XSS
		 * 6. Edit order and add GSTN, rate and sub total value with XSS
		 * 7. Edit quantity and total amount in the database as the user can change them through SQL injection
		 * 7. Print order and try to locate all the XSS elements in the page
		 */
				
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("<h1>TestProduct<h1>","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "<h1>ClientNameTest</h1>", "<h1>ClientContactTest</h1>","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
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
		gstnField.sendKeys("<h1>GSTNTest</h1>");
		
		Select paymentStatusDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentStatus\"]")));
		paymentStatusDropdown.selectByVisibleText("No Payment");
		
		//set the attack values through JS to inputs as Selenium can not handle some hidden elements
		RemoteWebDriver r=(RemoteWebDriver) driver;
		String subTotalValue="document.getElementById('subTotalValue').value='<h1>SubTotalTest</h1>'";
		r.executeScript(subTotalValue);
		String rateValue="document.getElementById('rateValue1').value='<h1>1rateTest<h1>'";
		r.executeScript(rateValue);
			
		//Press the Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"editOrderBtn\"]"));
		saveChangesButton.click();
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//get order ID from page
		String script = "return document.getElementById('orderId').getAttribute('value');";
		String value = r.executeScript(script).toString();
		
		//change values in the DB
		executeDatabaseQuery("Update order_item set quantity='<h1>TestQuantity</h1>', total='<h1>TotalAmountValue<h1>' where order_id =" + value + ";");
		
		//Print Order
		//Press the dropdown Orders button
		dropDownOrdersButton2 = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton2.click();
		//Press the Manage Orders menu button
		manageOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavManageOrder\"]/a"));
		manageOrdersMenuButton.click();
		
		
		rowNum = driver.findElements(By.xpath("//*[@id=\"manageOrderTable\"]/tbody/tr")).size();
		
		actionButton = null;
		WebElement printButton = null;
		
		//find the edit button for the order
		for(int i =1; i<=rowNum; i++) {
			if( driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[2]")).getText().contains("2020-01-15") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[3]")).getText().contains("ClientNameTest") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[4]")).getText().contains("ClientContactTest") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[5]")).getText().contains("1") &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[6]")).getText().contains("No Payment")) {
				
				actionButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/button"));
				actionButton.click();
				printButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/ul/li[3]/a"));

				break;
			}

		}
		
		if(actionButton == null) fail("Row not found");
		
		//save current window handler
		String  base= driver.getWindowHandle();
		
		//press the edit button
		printButton.click();
		
		//switch to the different window
		for (String h : driver.getWindowHandles()) {
			 if(!base.equals(h)) {
				 driver.switchTo().window(h);
			 }
		}
		
		//look for vulnerable elements in the page even if they are not visible in the popped dialog
		
		int count=0;

		//try to locate the h1 elements and count the number of successes
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'ClientNameTest')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'ClientContactTest')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'1rateTest')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'GSTNTest')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'TestProduct')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'SubTotalTest')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'TestQuantity')]"));
			count++;
		}catch(Exception e) {}
		
		try {
			driver.findElement(By.xpath("//td/h1[contains(.,'TotalAmountValue')]"));
			count++;
		}catch(Exception e) {}
		
		//switch to the base window
		driver.close();
		driver.switchTo().window(base);
		
		//check that 8 elements have been detected
		assertEquals(8, count);

	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	
	}

}
