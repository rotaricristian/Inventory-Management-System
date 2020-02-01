package utils;

import static org.junit.Assert.fail;

import java.sql.*;

import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.firefox.FirefoxDriver;


import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	protected static WebDriver driver;
	protected static String URL = "http://localhost/inventory-management-system/";

	
	/*
	 * Initialize the Firefox driver and connect to the main page
	 * 
	 * */
	@BeforeClass
	public static void setUp() throws InterruptedException {
		driver = getDriver();
		driver.get(URL);
	}

	/*
	 * Terminate the driver after all the tests
	 * */
	@AfterClass
	public static void tearDown() {
		driver.quit();
		driver = null;
	}

	public static WebDriver getDriver() {
		if (driver == null) {
			// Uncomment to use Opera (suggested alternative)
			// WebDriverManager.operadriver().setup();
			// driver = new OperaDriver();
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.get(URL);
		}
		return driver;
	}
	
	protected void login(String username, String password) {
		WebElement usernameTextBox = driver.findElement(By.id("username"));
		WebElement passwordTextBox = driver.findElement(By.id("password"));
		usernameTextBox.sendKeys(username);
		passwordTextBox.sendKeys(password);
		WebElement submitButton = driver.findElement(By.xpath("/html/body/div/div/div/div/div[2]/form/fieldset/div[3]/div/button"));
		submitButton.click();
	}
	
	protected void logout() {
		WebElement dropDownMenuButton = driver.findElement(By.xpath("//*[@id=\"navSetting\"]/a"));
		dropDownMenuButton.click();
		WebElement logOutButton = driver.findElement(By.xpath("//*[@id=\"topNavLogout\"]/a"));
		logOutButton.click();
	}
	
	protected void addUser(String username, String password, String uemail) {
		
		//Press the dropdown Menu button
		WebElement dropDownMenuButton = driver.findElement(By.xpath("//*[@id=\"navSetting\"]/a"));
		dropDownMenuButton.click();
		//Press the Add User menu button
		WebElement addUserMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[7]/ul/li[1]/a"));
		addUserMenuButton.click();
		//Press the in-page Add User button
		WebElement addUserButton = driver.findElement(By.xpath("//*[@id=\"addUserModalBtn\"]"));
		addUserButton.click();
		//Wait for the modal to appear
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Fill in the fields with data
		WebElement usernameTextBox = driver.findElement(By.xpath("//*[@id=\"userName\"]"));
		WebElement passwordTextBox = driver.findElement(By.xpath("//*[@id=\"upassword\"]"));
		WebElement userEmailTextBox = driver.findElement(By.xpath("//*[@id=\"uemail\"]"));
		usernameTextBox.sendKeys(username);
		passwordTextBox.sendKeys(password);
		userEmailTextBox.sendKeys(uemail);
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createUserBtn\"]"));
		saveChangesButton.click();
		
		//Close the modal
		WebElement closeModalButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/form/div[1]/button"));
		closeModalButton.click();
		
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void deleteUser(String name) {
		
		//Press the dropdown Menu button
		WebElement dropDownMenuButton = driver.findElement(By.xpath("//*[@id=\"navSetting\"]/a"));
		dropDownMenuButton.click();
		//Press the Add User menu button
		WebElement addUserMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[7]/ul/li[1]/a"));
		addUserMenuButton.click();
		
		//find the button from the sibling of the table data that has the name of the category
		WebElement actionButton = driver.findElement(By.xpath("//td[contains(., '"+name+"')]/following-sibling::td[1]/div/button"));
		actionButton.click();
		
		WebElement removeButton = driver.findElement(By.xpath("//td[contains(., '"+name+"')]/following-sibling::td[1]/div/ul/li[2]/a"));
		removeButton.click();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeProductBtn\"]"));
		saveChangesButton.click(); 
		
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
protected void addOrder(String date, String clientName, String clientContact, String product, String quantity, String discount, String paidAmount, String paymentType,
		String paymentStatus, String paymentPlace) {
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("//*[@id=\"navOrder\"]/a"));
		dropDownOrdersButton.click();
		//Press the Add Order menu button
		WebElement addOrdersMenuButton = driver.findElement(By.xpath("//*[@id=\"topNavAddOrder\"]/a"));
		addOrdersMenuButton.click();
		
		//accept all alerts
		try 
	    { 
	        do{
	        	try {
	        		Thread.sleep(100);
	        	} catch (InterruptedException e) {
	        		e.printStackTrace();
	        	}
	        	driver.switchTo().alert().accept(); 
	        }while(true);

	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    }  
		
		//Fill in the fields with data
		WebElement dateTextBox = driver.findElement(By.xpath("//*[@id=\"orderDate\"]"));
		WebElement clientNameBox = driver.findElement(By.xpath("//*[@id=\"clientName\"]"));
		WebElement clientContactTextBox = driver.findElement(By.xpath("//*[@id=\"clientContact\"]"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"productName1\"]")));
		Select productDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"productName1\"]")));
		WebElement productQuantityTextBox = driver.findElement(By.xpath("//*[@id=\"quantity1\"]"));
		WebElement discountTextBox = driver.findElement(By.xpath("//*[@id=\"discount\"]"));
		WebElement paidAmountTextBox = driver.findElement(By.xpath("//*[@id=\"paid\"]"));
		Select paymentTypeDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentType\"]")));
		Select paymentStatusDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentStatus\"]")));
		Select paymentPlaceDropdown = new Select(driver.findElement(By.xpath("//*[@id=\"paymentPlace\"]")));
		
		dateTextBox.sendKeys(date);
		clientNameBox.sendKeys(clientName);
		clientNameBox.click();
		try {
    		Thread.sleep(100);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
		clientContactTextBox.sendKeys(clientContact);
		
		//check which option is the desired one. Select by visible text fails sometimes when the name contains a script
        java.util.List<WebElement> options = productDropdown.getOptions(); 
        int index = -1;
        for(WebElement item:options) 
        {   
        	if(item.getText().contains(product)) {
        		index = options.indexOf(item);
        	}    
        }
        
        if(index>=0) {
        	productDropdown.selectByIndex(index);
        } else {
        	return;
        }
		
		productQuantityTextBox.sendKeys(quantity);
		discountTextBox.sendKeys(discount);
		paidAmountTextBox.sendKeys(paidAmount);
		paymentTypeDropdown.selectByVisibleText(paymentType);
		paymentStatusDropdown.selectByVisibleText(paymentStatus);
		paymentPlaceDropdown.selectByVisibleText(paymentPlace);
		
		//remove the other 2 products 
		WebElement deleteProduct2Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct2Button.click();
		WebElement deleteProduct3Button = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/form/table/tbody/tr[2]/td[6]/button"));
		deleteProduct3Button.click();
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createOrderBtn\"]"));
		saveChangesButton.click();
		
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected void deleteOrder(String date, String clientName, String contact, String totalOrderItem, String paymentStatus) {
		
		//Press the dropdown Orders button
		WebElement dropDownOrdersButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[5]/a"));
		dropDownOrdersButton.click();
		//Press the Manage Order menu button
		WebElement manageOrdersMenuButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[5]/ul/li[2]/a"));
		manageOrdersMenuButton.click();
		
		int rowNum = driver.findElements(By.xpath("//*[@id=\"manageOrderTable\"]/tbody/tr")).size();
		
		WebElement actionButton = null;
		WebElement removeButton = null;
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='manageOrderTable']/tbody")));
		
		for(int i =1; i<=rowNum; i++) {
			if( driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[2]")).getText().contains(date) &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[3]")).getText().contains(clientName) &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[4]")).getText().contains(contact) &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[5]")).getText().contains(totalOrderItem) &&
					driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[6]")).getText().contains(paymentStatus)) {
				
				actionButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/button"));
				removeButton = driver.findElement(By.xpath("//table[@id='manageOrderTable']/tbody/tr[" + i +"]/td[7]/div/ul/li[4]/a"));
				actionButton.click();
				break;
			}

		}
		
		if(actionButton == null) fail("Row not found");
		
		removeButton.click();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeOrderBtn\"]"));
		saveChangesButton.click(); 
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
}
	
protected void addCategory(String name, Boolean available) {
		
		//Press the Category dashboard button
		WebElement categoryButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[3]/a"));
		categoryButton.click();
		//Press the in-page Add Categories button
		WebElement addCategoriesButton = driver.findElement(By.xpath("//*[@id=\"addCategoriesModalBtn\"]"));
		addCategoriesButton.click();
		//Wait for the modal to appear
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Fill in the fields with data
		WebElement categoriesNameTextBox = driver.findElement(By.xpath("//*[@id=\"categoriesName\"]"));
		Select categoriesStatus = new Select(driver.findElement(By.xpath("//*[@id=\"categoriesStatus\"]")));
		categoriesNameTextBox.sendKeys(name);
		if(available) {
			categoriesStatus.selectByVisibleText("Available");
		} else {
			categoriesStatus.selectByVisibleText("Not Available");
		}
		
		//Press Save Changes button
		WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createCategoriesBtn\"]"));
		saveChangesButton.click();
		
		//accept all alerts
		try 
	    { 
	        do{
	        	try {
	        		Thread.sleep(100);
	        	} catch (InterruptedException e) {
	        		e.printStackTrace();
	        	}
	        	driver.switchTo().alert().accept(); 
	        }while(true);

	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        //no more alerts
	    } 
		
		//Close the modal
		WebElement closeModalButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/form/div[1]/button"));
		closeModalButton.click();	
		//Wait until the modal disappears
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

protected void deleteCategory(String name) {
	
	//Press the Category dashboard button
	WebElement categoryButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[3]/a"));
	categoryButton.click();
	
	//accept all alerts
	try 
    { 
        do{
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	driver.switchTo().alert().accept(); 
        }while(true);

    }   
    catch (NoAlertPresentException Ex) 
    { 
        //no more alerts
    } 
	
	//find the button from the sibling of the table data that has the name of the category
	WebElement actionButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[2]/div/button"));
	actionButton.click();
	
	WebElement removeButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[2]/div/ul/li[2]/a"));
	removeButton.click();
	
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Press Save Changes button
	WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeCategoriesBtn\"]"));
	saveChangesButton.click(); 
	//Wait until the modal disappears
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}

protected void addBrand(String name, Boolean available) {
	
	//Press the Brand dashboard button
	WebElement brandButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[2]/a"));
	brandButton.click();

	//Press the in-page Add Brand button
	WebElement addBrandButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div[2]/div[2]/button"));
	addBrandButton.click();
	
	//Wait for the modal to appear
	try {
		Thread.sleep(300);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Fill in the fields with data
	WebElement brandNameTextBox = driver.findElement(By.xpath("//*[@id=\"brandName\"]"));

	Select brandStatus = new Select(driver.findElement(By.xpath("//*[@id=\"brandStatus\"]")));
	brandNameTextBox.sendKeys(name);
	if(available) {
		brandStatus.selectByVisibleText("Available");
	} else {
		brandStatus.selectByVisibleText("Not Available");
	}
	
	//Press Save Changes button
	WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createBrandBtn\"]"));
	saveChangesButton.click();
	
	//accept all alerts
	try 
    { 
        do{
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	driver.switchTo().alert().accept(); 
        }while(true);

    }   
    catch (NoAlertPresentException Ex) 
    { 
        //no more alerts
    }   
	
	//Close the modal
	WebElement closeModalButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/form/div[1]/button/span"));
	closeModalButton.click();
	
	//Wait until the modal disappears
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}

protected void deleteBrand(String name) {
	
	//Press the Brand dashboard button
	WebElement brandButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[2]/a"));
	brandButton.click();

	//accept all alerts
	try 
    { 
        do{
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	driver.switchTo().alert().accept(); 
        }while(true);

    }   
    catch (NoAlertPresentException Ex) 
    { 
        //no more alerts
    } 
	
	
	//find the button from the sibling of the table data that contains the name of the brand 
	WebElement actionButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[2]/div/button"));
	actionButton.click();
	
	//find the remove button
	WebElement removeButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[2]/div/ul/li[2]/a"));
	removeButton.click();
	
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Press Save Changes button
	WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeBrandBtn\"]"));
	saveChangesButton.click(); 
	
	//Wait until the modal disappears
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}

protected void addProduct(String name, String quantity, String rate, String brand, String category, Boolean available, String filePath) {
	
	//Press the Product dashboard button
	WebElement productButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[4]/a"));
	productButton.click();
	//Press the in-page Add Product button
	WebElement addProductButton = driver.findElement(By.xpath("//*[@id=\"addProductModalBtn\"]"));
	addProductButton.click();
	//Wait for the modal to appear
	try {
		Thread.sleep(300);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Fill in the fields with data
	WebElement productNameTextBox = driver.findElement(By.xpath("//*[@id=\"productName\"]"));
	WebElement quantityTextBox = driver.findElement(By.xpath("//*[@id=\"quantity\"]"));
	WebElement rateTextBox = driver.findElement(By.xpath("//*[@id=\"rate\"]"));
	WebElement fileImageInput = driver.findElement(By.xpath("//*[@id=\"productImage\"]"));
	Select productBrand = new Select(driver.findElement(By.xpath("//*[@id=\"brandName\"]")));
	Select productCategory = new Select(driver.findElement(By.xpath("//*[@id=\"categoryName\"]")));
	Select productStatus = new Select(driver.findElement(By.xpath("//*[@id=\"productStatus\"]")));
	
	fileImageInput.sendKeys(filePath);
	productNameTextBox.sendKeys(name);
	quantityTextBox.sendKeys(quantity);
	rateTextBox.sendKeys(rate);
	
	//check which brand is the desired one. Select by visible text fails sometimes when the name contains a script
    java.util.List<WebElement> options = productBrand.getOptions(); 
    int index = -1;
    for(WebElement item:options) 
    {   
    	if(item.getText().contains(brand)) {
    		index = options.indexOf(item);
    	}    
    }
    
    if(index>=0) {
    	productBrand.selectByIndex(index);
    } else {
    	return;
    }
	
    //check which category is the desired one
    java.util.List<WebElement> catOptions = productCategory.getOptions(); 
    int catIndex = -1;
    for(WebElement item:catOptions) 
    {   
    	if(item.getText().contains(category)) {
    		catIndex = catOptions.indexOf(item);
    	}    
    }
    
    if(catIndex>=0) {
    	productCategory.selectByIndex(catIndex);
    } else {
    	return;
    }

	if(available) {
		productStatus.selectByVisibleText("Available");
	} else {
		productStatus.selectByVisibleText("Not Available");
	}
	
	
	//Press Save Changes button
	WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"createProductBtn\"]"));
	saveChangesButton.click();
	
	//accept all alerts
	try 
    { 
        do{
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	driver.switchTo().alert().accept(); 
        }while(true);

    }   
    catch (NoAlertPresentException Ex) 
    { 
        //no more alerts
    }   
	
	//Wait for the modal to appear
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Close the modal
	WebElement closeModalButton = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/form/div[1]/button/span"));
	closeModalButton.click();
	
	//Wait until the modal disappears
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}

protected void deleteProduct(String name) {
	
	//Press the Product dashboard button
	WebElement productButton = driver.findElement(By.xpath("/html/body/nav/div/div[2]/ul/li[4]/a"));
	productButton.click();
	
	
	//accept all alerts
	try 
    { 
        do{
        	try {
        		Thread.sleep(100);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	driver.switchTo().alert().accept(); 
        }while(true);

    }   
    catch (NoAlertPresentException Ex) 
    { 
        //no more alerts
    }  


	
	//find the button from the sibling of the table data that has the name of the product
	WebDriverWait wait = new WebDriverWait(driver, 5);
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[6]/div/button")));
	WebElement actionButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[6]/div/button"));
	actionButton.click();
	
	//find the remove button
	WebElement removeButton = driver.findElement(By.xpath("//td[contains(.,'"+name+"')]/following-sibling::td[6]/div/ul/li[2]/a"));
	//*[@id="removeProductModalBtn"]
	removeButton.click();
	
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	//Press Save Changes button
	WebElement saveChangesButton = driver.findElement(By.xpath("//*[@id=\"removeProductBtn\"]"));
	saveChangesButton.click();
	
	//Wait until the modal disappears
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
}


protected void executeDatabaseQuery(String query) {
	
	String url = "jdbc:mysql://localhost:3306/store";
	String username = "root";
	String password = "";

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    System.out.println("Driver loaded!");
	} catch (ClassNotFoundException e) {
	    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
	}
	
	try (Connection con = DriverManager.getConnection(url, username, password)) {
	    System.out.println("Database connected!");
		Statement stmt = null;
		
		try {
	        stmt = con.createStatement();
	        boolean rs = stmt.execute(query);

		} catch (SQLException e ) {
		    e.printStackTrace();
		} finally {
		    if (stmt != null) { stmt.close(); }
		}
	} catch (SQLException e) {
	    System.out.println(e);
	}
	

}
	
	
	

}