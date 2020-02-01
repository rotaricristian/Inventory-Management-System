package it.unitn.sectest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class FetchProduct extends BaseTest {
	
	@Test
	public void fetchProductTest() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Add Category with name containing XSS attack
		 * 3. Add Brand with name containing XSS attack
		 * 4. Add Product with XSS attack in name, rate and quantity starting with an integer bigger than 0
		 * 5. Check on the Products page that the parameters contain the attack
		 */
		
		login("admin", "admin");
		addCategory("<h1>CategoryTest</h1>", true);
		addBrand("<h1>BrandTest</h1>", true);
		//the rate has to start with a digit because of product.quantity>0 condition in the SELECT statement
		addProduct("<h1>TestProduct</h1>","1000<h1>TestQuantity1</h1>", "<h1>TestRate</h1>", "BrandTest", "CategoryTest", true, System.getProperty("user.dir")+"/src/main/java/resources/mob.jpg");
		
		
		//change image URL in the DB
		executeDatabaseQuery("Update product set product_image='aaa''><h1>ImageTest</h1>' where product_name='<h1>TestProduct</h1>' and quantity='1000<h1>TestQuantity1</h1>';");
		
		//Press the Product dashboard button
		WebElement productButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[4]/a"));
		productButton.click();
		
		//find the table data that contains the attack elements and count their number
		
		int count = 0;
		try{
			//product name
			driver.findElement(By.xpath("//td/h1[contains(.,'TestProduct')]"));
			count++;
		}catch(Exception e) {		}
		
		try{
			//product rate
			driver.findElement(By.xpath("//td/h1[contains(.,'TestRate')]"));
			count++;
		}catch(Exception e) {		}
		
		try{
			//product quantity
			driver.findElement(By.xpath("//td/h1[contains(.,'TestQuantity1')]"));
			count++;
		}catch(Exception e) {		}
		
		try{
			//product brand
			driver.findElement(By.xpath("//td/h1[contains(.,'BrandTest')]"));
			count++;
		}catch(Exception e) {		}
		
		try{
			//product category
			driver.findElement(By.xpath("//td/h1[contains(.,'CategoryTest')]"));
			count++;
		}catch(Exception e) {		}
		
		try{
			//product image
			driver.findElement(By.xpath("//td/h1[contains(.,'ImageTest')]"));
			count++;
		}catch(Exception e) {		}

		//check that 6 attacks are present
		assertEquals(6, count);
		
	}
	
	@After
	public void reset() {		

		deleteCategory("CategoryTest");
		deleteBrand("BrandTest");
		deleteProduct("TestProduct");
	
	}

}
