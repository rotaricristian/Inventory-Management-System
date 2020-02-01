package it.unitn.sectest;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.BaseTest;

public class Settings3 extends BaseTest {
	
	@Test
	public void settings3Test() {
		
		/* Steps:
		 * 1. Log in as Admin
		 * 2. Change bio to obtain well-formed HTML and XSS attack
		 * 3. Refresh and check for XSS attack
		 */
		
		login("admin", "admin");
		
		//Press the drop down Menu button
		WebElement dropDownMenuButton = driver.findElement(By.xpath("//*[@id=\"navSetting\"]/a"));
		dropDownMenuButton.click();
		
		//Press the Add User menu button
		WebElement settingsMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[7]/ul/li[2]/a"));
		settingsMenuButton.click();
		
		//change bio with XSS attack
		WebElement usernameTextBox = driver.findElement(By.xpath("//*[@id=\"bio\"]"));
		usernameTextBox.clear();
		usernameTextBox.sendKeys("\"><h1>TestBio</h1><br");
		
		//Press Save Changes button
		WebElement changeUsernameButton = driver.findElement(By.xpath("//*[@id=\"changeBioBtn\"]"));
		changeUsernameButton.click();
				
		//refresh page
		driver.navigate().refresh();
		
		//try to locate the h1 element with attack
		try {
			driver.findElement(By.xpath("//h1[contains(.,'TestBio')]"));
		}catch(Exception e) {
			fail();
		}	
		
		//if the element is found then the attack is present and the test passes
	}
	
	@After
	public void reset() {		

		//change bio with XSS attack
		WebElement usernameTextBox = driver.findElement(By.xpath("//*[@id=\"bio\"]"));
		usernameTextBox.clear();
		usernameTextBox.sendKeys("bio");
		
		//Press Save Changes button
		WebElement changeUsernameButton = driver.findElement(By.xpath("//*[@id=\"changeBioBtn\"]"));
		changeUsernameButton.click();
	}

}
