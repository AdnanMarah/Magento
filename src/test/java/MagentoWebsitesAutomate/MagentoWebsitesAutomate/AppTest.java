package MagentoWebsitesAutomate.MagentoWebsitesAutomate;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class AppTest {

	WebDriver driver = new ChromeDriver();
	Connection con; 
	Statement stmt;
	ResultSet rs;

	String FirstName;
	String LastName;
	String Email;
	String Password;
	Random rand;
	String selectedProductName;

	@BeforeTest
	public void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "1234");
		driver.get("https://magento.softwaretestingboard.com");
		driver.manage().window().maximize();
	}

	@Test(priority = 1, enabled = true)
	public void SignInFromDatabase() throws SQLException, InterruptedException {
		String query = "SELECT * FROM customers WHERE customerNumber IN (282);";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			FirstName = rs.getString("contactFirstName");
			LastName = rs.getString("contactLastName");
			Email = FirstName.trim() + "." + LastName.trim() + "@gmail.com";
			Password = rs.getString("addressLine1");

			System.out.println(Email);

			driver.get("https://magento.softwaretestingboard.com");
			driver.manage().window().maximize();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account"))).click();

			driver.findElement(By.id("firstname")).sendKeys(FirstName);
			Thread.sleep(2000);
			driver.findElement(By.id("lastname")).sendKeys(LastName);
			Thread.sleep(2000);
			driver.findElement(By.id("email_address")).sendKeys(Email);
			Thread.sleep(2000);
			driver.findElement(By.id("password")).sendKeys(Password);
			Thread.sleep(2000);
			driver.findElement(By.id("password-confirmation")).sendKeys(Password);
			Thread.sleep(2000);

			driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

			Thread.sleep(2000); 

			List<WebElement> errorMessages = driver.findElements(By.cssSelector(".message-error.error.message"));

			if (!errorMessages.isEmpty()) {
				String errorMsg = errorMessages.get(0).getText().trim();
				if (errorMsg.contains("There is already an account with this email address")) {
					System.out.println("There is already an account with this email address");
					continue;
				}
			}

			WebElement successMessage = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success.success.message")));
			String message = successMessage.getText().trim();
			System.out.println(message);

		}
	}

	@Test(priority = 2, enabled = true)
	public void errorHandlingOnInvalidInputsTest() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Create an Account"))).click();

		driver.findElement(By.id("firstname")).sendKeys("Funn");
		driver.findElement(By.id("email_address")).sendKeys("funn@example.com");
		Thread.sleep(2000);

		WebElement registerButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[title='Create an Account']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
		Thread.sleep(500);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerButton);
		Thread.sleep(1500);

		boolean foundError = driver.findElements(By.className("mage-error")).stream()
				.anyMatch(e -> e.getText().contains("This is a required field"));

		System.out.println(foundError ? "This is a required field" : "The expected error messages did not appear");
	}

	@Test(priority = 3, enabled = true)

	public void LogInFromDatabase() throws SQLException, InterruptedException {

		String query = "SELECT * FROM customers WHERE customerNumber IN ( 311);";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			String firstName = rs.getString("contactFirstName").trim();
			String lastName = rs.getString("contactLastName").trim();
			String email = firstName + "." + lastName + "@gmail.com";
			String password = rs.getString("addressLine1");

			driver.get("https://magento.softwaretestingboard.com");
			driver.manage().window().maximize();
			driver.manage().window().setSize(new Dimension(1920, 1080));

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
			signInLink.click();

			driver.findElement(By.id("email")).sendKeys(email);
			Thread.sleep(2000);
			driver.findElement(By.id("pass")).sendKeys(password);
			Thread.sleep(2000);
			driver.findElement(By.id("send2")).click();
			Thread.sleep(3000);

			boolean isLoggedIn = driver.getPageSource().contains("Welcome, " + firstName + " " + lastName + "!");
			Assert.assertTrue(isLoggedIn, "Login failed for: " + email);
			System.out.println("Welcome, " + firstName + " " + lastName + "!");
		}
	}

	@Test(priority = 4, enabled = true)
	public void logoutWithActions() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.manage().window().maximize();

		WebElement dropdownToggle = wait.until(
				ExpectedConditions.elementToBeClickable(By.cssSelector("li.customer-welcome button.action.switch")));
		dropdownToggle.click();

		WebElement signOutLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign Out")));

		Actions actions = new Actions(driver);
		actions.moveToElement(signOutLink).perform();
		Thread.sleep(2000);
		actions.click().perform();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign In")));

		System.out.println("You are signed out");
		Thread.sleep(1500);

	}

	@Test(priority = 5, enabled = true)
	public void SearchForProduct() throws InterruptedException {
		driver.get("https://magento.softwaretestingboard.com/");
		driver.manage().window().maximize();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement searchBox = driver.findElement(By.id("search"));

		searchBox.sendKeys("Radiant Tee");
		searchBox.sendKeys(Keys.ENTER);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));
		List<WebElement> results = driver.findElements(By.cssSelector(".product-item"));

		if (results.isEmpty()) {
			System.out.println("No results appeared.");
			Assert.fail("No results appeared.");
		}

		WebElement totalResults = driver.findElement(By.cssSelector("#toolbar-amount .toolbar-number:last-of-type"));
		System.out.println("Total products found: " + totalResults.getText());

		System.out.println("Search was successful and results are displayed.");
	}

	@SuppressWarnings("deprecation")
	@Test(priority = 6, enabled = true)
	public void ViewProductDetails() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Actions actions = new Actions(driver);
		Random rand = new Random();


		String selectedMenu = "Women";
		WebElement womenMenu = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Women']/parent::a")));
		actions.moveToElement(womenMenu).perform();
		System.out.println("Selected Main Menu: " + selectedMenu);
		Thread.sleep(1000);

		String[] parentCategories = { "Tops", "Bottoms" };
		String selectedParent = parentCategories[rand.nextInt(parentCategories.length)];
		WebElement parentElement = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[span[text()='" + selectedParent + "']]")));
		actions.moveToElement(parentElement).perform();
		System.out.println("Selected Parent Category: " + selectedParent);
		Thread.sleep(1000);

		String[] subCategories = selectedParent.equals("Tops")
				? new String[] { "Jackets", "Hoodies & Sweatshirts", "Tees", "Bras & Tanks" }
				: new String[] { "Pants", "Shorts" };
		String selectedSubCategory = subCategories[rand.nextInt(subCategories.length)];

		WebElement subCategoryElement = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//a[span[text()='" + selectedSubCategory + "']]")));
		actions.moveToElement(subCategoryElement).pause(Duration.ofSeconds(1)).click().perform();

		System.out.println("Selected SubCategory: " + selectedSubCategory);
		Thread.sleep(2000);
		List<WebElement> products = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("a.product-item-link")));
		Assert.assertFalse(products.isEmpty(), "No products found!");
		WebElement randomProduct = products.get(rand.nextInt(products.size()));
		// String selectedProductName = randomProduct.getText().trim();
		this.selectedProductName = randomProduct.getText().trim();

		randomProduct.click();
		Thread.sleep(2000);

		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img.fotorama__img")))
				.isDisplayed());
		System.out.println("The images appeared successfully");

		Assert.assertTrue(
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.price"))).isDisplayed());
		System.out.println("The price appeared successfully");

		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".stock.available")))
				.isDisplayed());
		System.out.println("Availability showed up successfully");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);",
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-addtocart-button"))));
		Thread.sleep(500);
		Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-addtocart-button")))
				.isDisplayed());

		List<WebElement> moreInfoTabs = driver.findElements(By.id("tab-label-additional"));
		if (!moreInfoTabs.isEmpty()) {
			WebElement moreInfoTab = moreInfoTabs.get(0);
			if ("false".equals(moreInfoTab.getAttribute("aria-expanded"))) {
				moreInfoTab.click();
				Thread.sleep(1000);
			}

			WebElement moreInfoContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("additional")));
			Assert.assertTrue(moreInfoContent.isDisplayed(), "More information tab content not visible");
			System.out.println("More Information tab content is visible");
		} else {
			System.out.println("More Information tab not found");
		}

		System.out.println("Selected Product: " + this.selectedProductName);
	}

	@SuppressWarnings("deprecation")
	@Test(priority = 7, enabled = true)
	public void AddProductToCartFromDetailsPage() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		Random rand = new Random();

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 300);");
		Thread.sleep(500);

		List<WebElement> sizes = driver.findElements(By.cssSelector(".swatch-attribute.size .swatch-option"));
		if (!sizes.isEmpty()) {
			WebElement selectedSize = sizes.get(rand.nextInt(sizes.size()));
			selectedSize.click();
			System.out.println("Selected Size: " + selectedSize.getAttribute("aria-label"));
			Thread.sleep(2000);
		} else {
			System.out.println("No size options available.");
		}

		List<WebElement> colors = driver.findElements(By.cssSelector(".swatch-attribute.color .swatch-option"));
		if (!colors.isEmpty()) {
			WebElement selectedColor = colors.get(rand.nextInt(colors.size()));
			selectedColor.click();
			System.out.println("Selected Color: " + selectedColor.getAttribute("aria-label"));
			Thread.sleep(2000);
		} else {
			System.out.println("No color options available.");
		}

		int randomQty = rand.nextInt(5) + 1;
		WebElement qtyInput = driver.findElement(By.id("qty"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qtyInput);
		Thread.sleep(500);
		qtyInput.clear();
		qtyInput.sendKeys(String.valueOf(randomQty));
		System.out.println("Quantity: " + randomQty);
		Thread.sleep(1000);

		WebElement addToCartBtn = wait
				.until(ExpectedConditions.elementToBeClickable(By.id("product-addtocart-button")));
		addToCartBtn.click();
		Thread.sleep(3000);

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
		Thread.sleep(1000);

		WebElement successMsg = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.message-success")));
		Assert.assertTrue(successMsg.isDisplayed(), "Success message not displayed!");
		System.out.println("Product added to cart successfully.");
		Thread.sleep(2000);
	}

	@Test(priority = 8, enabled = true)
	public void ProceedToCheckoutTest() throws InterruptedException {
		driver.get("https://magento.softwaretestingboard.com");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.action.showcart")));
		cartIcon.click();
		Thread.sleep(3000); 

		if (driver.getPageSource().contains("You have no items in your shopping cart.")) {
			System.out.println("You have no items in your shopping cart");
			return; 
		}

		WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("top-cart-btn-checkout")));
		actions.moveToElement(checkoutBtn).pause(Duration.ofMillis(800)).click().perform();

		WebElement shippingStepTitle = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.step-title[data-role='title']")));

		Assert.assertEquals(shippingStepTitle.getText().trim(), "Shipping Address",
				"❌ لم تظهر خطوة العنوان بشكل صحيح.");
		System.out.println("Shipping Address step is displayed correctly.");
		Thread.sleep(3000);

		js.executeScript("window.scrollBy(0, 1000)");
		Thread.sleep(2000); 

		WebElement shippingTable = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-checkout-shipping-method")));
		System.out.println("Shipping methods table is visible.");
	}

	@SuppressWarnings("deprecation")
	@Test(priority = 9, enabled = true)
	public void EditCartQuantityOrRemoveItemRandomly() throws InterruptedException {
		driver.get("https://magento.softwaretestingboard.com");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Random random = new Random();

		driver.findElement(By.cssSelector(".showcart")).click();

		if (driver.getPageSource().contains("You have no items in your shopping cart")) {
			System.out.println("The cart is empty. No actions were performed.");
			return;
		}

		WebElement viewEditCart = wait
				.until(ExpectedConditions.elementToBeClickable(By.linkText("View and Edit Cart")));
		viewEditCart.click();

		List<WebElement> products = driver.findElements(By.cssSelector(".cart.item"));
		Assert.assertTrue(!products.isEmpty(), "There is no product in the cart");

		String priceBefore = driver.findElement(By.cssSelector("td.col.subtotal span.price")).getText();
		System.out.println("Price before modification: " + priceBefore);

		int choice = random.nextInt(2); // 0 = تعديل كمية، 1 = حذف منتج

		if (choice == 0) {
			System.out.println("The quantity has been selected");

			WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.qty")));
			int currentQty = Integer.parseInt(qtyInput.getAttribute("value"));
			int newQty = currentQty == 5 ? 4 : currentQty + 1;

			qtyInput.clear();
			qtyInput.sendKeys(String.valueOf(newQty));
			System.out.println("New quantity: " + newQty);

			WebElement updateBtn = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//span[text()='Update Shopping Cart']/parent::button")));
			js.executeScript("arguments[0].style.border='3px solid green';", updateBtn);
			updateBtn.click();

			wait.until(ExpectedConditions
					.not(ExpectedConditions.textToBe(By.cssSelector("td.col.subtotal span.price"), priceBefore)));

			String priceAfter = driver.findElement(By.cssSelector("td.col.subtotal span.price")).getText();
			System.out.println("Price after modification: " + priceAfter);

			Assert.assertNotEquals(priceBefore, priceAfter, "The price has not changed after adjusting the quantity");
		} else {
			System.out.println("Delete product has been selected");

			WebElement deleteBtn = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.action-delete")));
			js.executeScript("arguments[0].style.border='3px solid red';", deleteBtn);
			deleteBtn.click();

			List<WebElement> confirmPopup = driver.findElements(By.cssSelector("button.action-primary.action-accept"));
			if (!confirmPopup.isEmpty()) {
				confirmPopup.get(0).click();
			}

			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(15));
			wait2.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-empty p")));
			Thread.sleep(3000);

		}
	}

	@Test(priority = 10, enabled = true)
	public void PrivacyPolicyAccessibilityTest() throws InterruptedException {
		driver.get("https://magento.softwaretestingboard.com");

		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.END).perform();

		Thread.sleep(1500);

		WebElement privacyLink = driver.findElement(By.linkText("Privacy and Cookie Policy"));
		privacyLink.click();
		Thread.sleep(2000);

		String actualTitle = driver.getTitle();
		System.out.println("Current page title " + actualTitle);
		Assert.assertTrue(actualTitle.toLowerCase().contains("privacy"),
				" The privacy policy page did not load correctly");
	}

	@AfterTest
	public void tearDown() throws SQLException {
		if (driver != null) {
			driver.quit();
		}
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

}