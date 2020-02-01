package it.unitn.sectest;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Settings1 extends BaseTest {
	
	@Test
	public void settings1Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Change user name to obtain well-formed HTML and XSS attack
		 * 3. Refresh and check for XSS attack
		 */
		
		login("admin", "admin");
		
		//Press the drop down Menu button
		WebElement dropDownMenuButton = driver.findElement(By.xpath("//*[@id=\"navSetting\"]/a"));
		dropDownMenuButton.click();
		
		//Press the Add User menu button
		WebElement settingsMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[7]/ul/li[2]/a"));
		settingsMenuButton.click();
		
		//change user name with XSS attack
		WebElement usernameTextBox = driver.findElement(By.xpath("//*[@id=\"username\"]"));
		usernameTextBox.clear();
		usernameTextBox.sendKeys("\"><h1>TestAdmin</h1><br");
		
		//Press the Save Changes button
		WebElement changeUsernameButton = driver.findElement(By.xpath("//*[@id=\"changeUsernameBtn\"]"));
		changeUsernameButton.click();
				
		//refresh page
		driver.navigate().refresh();
		
		//try to locate the h1 element with attack
		try {
			driver.findElement(By.xpath("//h1[contains(.,'TestAdmin')]"));
		}catch(Exception e) {
			fail();
		}	
		
		//if the element is found then the attack is present and the test passes
	}
	
	@After
	public void reset() {		

		//change user name with XSS attack
		WebElement usernameTextBox = driver.findElement(By.xpath("//*[@id=\"username\"]"));
		usernameTextBox.clear();
		usernameTextBox.sendKeys("admin");
		
		//Press the Save Changes button
		WebElement changeUsernameButton = driver.findElement(By.xpath("//*[@id=\"changeUsernameBtn\"]"));
		changeUsernameButton.click();
	}

}
