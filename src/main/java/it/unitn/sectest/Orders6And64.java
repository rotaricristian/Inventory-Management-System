package it.unitn.sectest;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.NoAlertPresentException;
import utils.BaseTest;

public class Orders6And64 extends BaseTest {
	
	@Test
	public void orders6And64Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Access the URL that has XSS attack as ID parameter in the GET request
		 * 3. Check for 2 alerts as the orders.php file has 2 identical vulnerabilities on different lines
		 * (Sometimes only one alert is present due to some error in the database: re-run the test to get both alerts
		 */
		
		login("admin", "admin");
		driver.get("http://localhost/inventory-management-system/orders.php?o=editOrd&i=\"/><script>alert(1)</script>");
		
		//accept 2 alerts 
		int count= 0;
		try 
	    { 
			do {
				driver.switchTo().alert().accept();
		        count++;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}while(true);
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	    	//no more alerts
	    }  
		
		//Must be 0 when code is fully patched
		assertEquals(2, count);	
		
	}


}
