package MachineTest;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AutomationMachineTest {

	WebDriver driver;

	@BeforeMethod
	public void testSetup() throws Exception {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://testffc.nimapinfotech.com/");
		Thread.sleep(3000);

	}

	// Login page
	@Test(priority = 1, dataProvider = "loginData")
	public void loginTest(String mobilenumber, String password) throws Exception {
		perFormLogin(mobilenumber, password);

	}

	@DataProvider(name = "loginData")
	public Object[][] provideLoginData() {

		Object[][] data = { { "9146593814", "Savita@98" }, { "Savitat1912@gmail.com", "Savita@98" },
				{ "914659", "savita12" }, { "", "" } };
		return data;

	}

	private void perFormLogin(String mobilenumber, String password) throws Exception {

		Thread.sleep(3000);
		driver.findElement(By.xpath(
				"/html/body/kt-auth/div/div/div[2]/kt-login/div[2]/div/form/div[1]/mat-form-field/div/div[1]/div/input"))
				.sendKeys(mobilenumber);

		driver.findElement(By.xpath(
				"/html/body/kt-auth/div/div/div[2]/kt-login/div[2]/div/form/div[2]/mat-form-field/div/div[1]/div/input"))
				.sendKeys(password);
		Thread.sleep(3000);

		System.out.println("Please enter the CAPTCHA characters: ");

		Scanner scanner = new Scanner(System.in);
		String captcha = scanner.nextLine();

		Thread.sleep(7000);

		WebElement element1 = driver.findElement(By.xpath(
				"/html/body/kt-auth/div/div/div[2]/kt-login/div[2]/div/form/div[3]/kt-captcha/div/div/form/input"));
		element1.sendKeys(captcha);
		Thread.sleep(2000);

		driver.findElement(By.id("kt_login_signin_submit")).click();

		Thread.sleep(3000);

		boolean isLoggedIn = driver.getCurrentUrl().equals("https://testffc.nimapinfotech.com/dashboard");
		try {
			Assert.assertTrue(isLoggedIn, "Login failed for user: " + mobilenumber);

		} catch (Exception e) {

		}

	}

	// Adding New Customer
	@Test(dependsOnMethods = "loginTest", priority = 1, dataProvider = "customerData")
	public void addCustomerTest(String customerName, String refNo, String contactPersonName, String mobileNumber,
			String telePhoneNumber, String email, String designation) throws Exception {
		this.loginTest(null, null);
		NewCustomer(customerName, refNo, contactPersonName, mobileNumber, telePhoneNumber, email, designation);

	}

	@DataProvider(name = "customerData")
	public Object[][] getLoginData() throws IOException {
		Object[][] data = { { "Savita", "1234", "XYZ", "9978065432", "02550223040", "savita@gmail.com", "Seller" } };
		return data;
	}

	private void NewCustomer(String customerName, String refNo, String contactPersonName, String mobileNumber,
			String telePhoneNumber, String email, String designation) throws Exception {
		driver.findElement(By.xpath("//a[@ng-reflect-router-link='/customers']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[normalize-space()='New Customer']")).click();

		// adding data to field
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-67']")).sendKeys(customerName);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-68']")).sendKeys(refNo);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-70']")).sendKeys(contactPersonName);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-71']")).sendKeys(mobileNumber);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-72']")).sendKeys(telePhoneNumber);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-73']")).sendKeys(email);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='mat-input-74']")).sendKeys(designation);
		Thread.sleep(1000);

		driver.findElement(By.xpath("//span[normalize-space()='Save']")).click();
	}

//validate punchIn button
	@Test(dependsOnMethods = "loginTest", priority = 2)
	public void popupMessage() {
		driver.findElement(By.xpath("//span[normalize-space()='Punch In']")).click();
	}

	@AfterMethod
	public void browserClose() {
		driver.quit();
	}

}
