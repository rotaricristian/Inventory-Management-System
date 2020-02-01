package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchUser extends BaseTest {
	
	@Test
	public void fetchUserTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add User with XSS in name
		 * 3. Check user name in the Manage Users Page
		 */
		
		login("admin", "admin");
		addUser("<h1>UserTest</h1>", "user", "test@test.com");
		
		//find the button from the sibling of the table data that contains the name of the brand 
		WebElement userName = driver.findElement(By.xpath("//td[contains(.,'UserTest')]"));
		
		String innerHtml = userName.getAttribute("innerHTML");
		
		assertEquals("<h1>UserTest</h1>", innerHtml);	
		
	}
	
	@After
	public void reset() {		

		deleteUser("UserTest");
	
	}

}
