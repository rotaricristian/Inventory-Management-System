package it.unitn.sectest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class GetOrderReport extends BaseTest {
	
	@Test
	public void getOrderReportTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category
		 * 3. Add Brand
		 * 4. Add Product
		 * 5. Add Order with XSS in client name, contact and inject in the DB XSS in grand total field
		 * 6. Generate report
		 * 7. Check in the new window that the client, contact and grand total contain the XSS
		 * 
		 * Note: at the end of the test the execution still continues for several seconds for unknown reasons, even if the test is successful
		 */
		
		login("admin", "admin");
		addCategory("CategoryTest", true);
		addBrand("BrandTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		addOrder("01/15/2020", "<h1>ClientNameTestWithGrandTotal</h1>", "<h1>ClientContactTest</h1>","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		
		//change values in the DB
		executeDatabaseQuery("Update orders set grand_total='<h1>1TestGrandTotal</h1>' where client_name='<h1>ClientNameTestWithGrandTotal</h1>';");
		
		//Press the report button
		WebElement reportButton = driver.findElement(By.xpath("//*[@id=\"navReport\"]/a"));
		reportButton.click();
		
		//Set start and end date which should be the same because of a logical error in the PHP file
		WebElement startDateField = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));
		WebElement endDateField = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));

		startDateField.sendKeys("01/15/2020");
		endDateField.sendKeys("01/15/2020");
		
		//save current window handler
		String  base= driver.getWindowHandle();
		
		//Press the generate report button
		WebElement generateReportButton = driver.findElement(By.xpath("//*[@id=\"generateReportBtn\"]"));
		generateReportButton.click();
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//switch to the different window
		for (String h : driver.getWindowHandles()) {
			 if(!base.equals(h)) {
				 driver.switchTo().window(h);
			 }
		}
		
		//look for vulnerable elements in the page even if they are not visible in the popped dialog
		try{
			WebElement clientName  = driver.findElement(By.xpath("//td[contains(.,'ClientNameTestWithGrandTotal')]/center"));
			WebElement clientContact  = driver.findElement(By.xpath("//td[contains(.,'ClientContactTest')]/center"));
			WebElement grandTotal  = driver.findElement(By.xpath("//td[contains(.,'1TestGrandTotal')]/center"));
			
			String clientNameInnerHtml = clientName.getAttribute("innerHTML");
			String clientContactInnerHtml = clientContact.getAttribute("innerHTML");
			String grandTotalInnerHtml = grandTotal.getAttribute("innerHTML");
			
			assertEquals("<h1>ClientNameTestWithGrandTotal</h1>", clientNameInnerHtml);
			assertEquals("<h1>ClientContactTest</h1>", clientContactInnerHtml);
			assertEquals("<h1>1TestGrandTotal</h1>", grandTotalInnerHtml);
			
			//switch to the base window
			driver.close();
			driver.switchTo().window(base);
		} catch(Exception e) {
			//switch to the base window
			driver.close();
			driver.switchTo().window(base);
			//unable to find elements
			fail();
		}
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTestWithGrandTotal", "ClientContactTest", "1", "No Payment");
	
	}

}
