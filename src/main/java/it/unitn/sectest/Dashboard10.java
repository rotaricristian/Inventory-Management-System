package it.unitn.sectest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Dashboard10 extends BaseTest {
	
	@Test
	public void dashboard10Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add User with script in name
		 * 3. Add Brand
		 * 4. Add Category
		 * 5. Add Product
		 * 6. Log Out
		 * 7. Log in as User
		 * 8. Add Order
		 * 9. Log out
		 * 10.Log in as Admin 
		 * 11.Check user name in the dashboard page
		 */
		
		login("admin", "admin");
		addUser("<h1>TestUser</h1>", "test", "test@test.com");
		addBrand("BrandTest", true);
		addCategory("CategoryTest", true);
		addProduct("TestProduct","1000", "1", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		logout();
		login("<h1>TestUser</h1>", "test");
		addOrder("01/15/2020", "ClientNameTest", "ClientContactTest","TestProduct", "1", "1", "1", "Cash", "No Payment", "In Gujarat");
		logout();
		login("admin", "admin");
		
		//get the number of user Orders
		WebElement userName = null;
		int rowNum = driver.findElements(By.xpath("//table[@id='productTable']/tbody/tr")).size();
		
		//get the username that corresponds to the inserted one (might be different after fixing the vulnerability)
		for(int i =1; i<=rowNum; i++) {
			if( driver.findElement(By.xpath("//table[@id='productTable']/tbody/tr[" + i +"]/td[1]")).getText().contains("TestUser")) {
				
				userName = driver.findElement(By.xpath("//table[@id='productTable']/tbody/tr[" + i +"]/td[1]"));
			}
		}
		
		if(userName == null) fail("User not found");
		
		String innerHtml = userName.getAttribute("innerHTML");
		
		assertEquals("<h1>TestUser</h1>", innerHtml);	
		
	}
	
	@After
	public void reset() {		

		deleteUser("TestUser");
		deleteBrand("BrandTest");
		deleteCategory("CategoryTest");
		deleteProduct("TestProduct");
		deleteOrder("2020-01-15", "ClientNameTest", "ClientContactTest", "1", "No Payment");
	}

}
