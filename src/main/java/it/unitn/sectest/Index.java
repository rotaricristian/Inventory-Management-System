package it.unitn.sectest;

import org.junit.Test;
import org.openqa.selenium.NoAlertPresentException;

import utils.BaseTest;

public class Index extends BaseTest {
	
	@Test
	public void indexTest() {
		
		driver.get("http://localhost/inventory-management-system/index.php/%22%3E%3Cscript%3Ealert(%27XSS%27)%3C/script%3E");
		boolean present= false;
		try 
	    { 
	        driver.switchTo().alert().accept(); 
	        present = true;
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        present = false;
	    }  
		
		
		assert(present);	
		
	}


}
