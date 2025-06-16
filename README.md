
# 🌟 Magento QA Automation Practice Tests

This project automates test scenarios on the [Magento Demo Website](https://magento.softwaretestingboard.com) using **Java**, **Selenium WebDriver**, **TestNG**, and **MySQL**.

---

## 📄 Table of Contents

- [Project Setup](#project-setup)
- [Technologies Used](#technologies-used)
- [Test Cases Overview](#test-cases-overview)
- [Detailed Test Scenarios](#detailed-test-scenarios)
- [Notes](#notes)

---

## ✅ Project Setup

- Install [Java JDK 8+](https://adoptium.net/)
- Install [TestNG](https://testng.org/)
- Install [MySQL Server](https://dev.mysql.com/downloads/mysql/)
- Download [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/) and set it in your system path
- Add Selenium, TestNG, and MySQL connector libraries (or use Maven)
- Clone or import the Java files into Eclipse/IntelliJ
- Run tests via IDE or TestNG XML suite

---

## 🛠️ Technologies Used

- **Java**
- **Selenium WebDriver**
- **TestNG**
- **MySQL**
- **ChromeDriver**
- **Maven**

---

## 🧪 Test Cases Overview

| #  | Test Method                             | Description                                         |
|----|------------------------------------------|-----------------------------------------------------|
| 1  | SignInFromDatabase                       | Register new users using MySQL data                |
| 2  | errorHandlingOnInvalidInputsTest         | Test error messages for incomplete registration    |
| 3  | LogInFromDatabase                        | Login with valid credentials from database         |
| 4  | logoutWithActions                        | Log out using hover menu                           |
| 5  | SearchForProduct                         | Search for products like "Radiant Tee"             |
| 6  | ViewProductDetails                       | Navigate categories and view product details       |
| 7  | AddProductToCartFromDetailsPage          | Add product with random size/color/quantity        |
| 8  | ProceedToCheckoutTest                    | Simulate the checkout process                      |
| 9  | EditCartQuantityOrRemoveItemRandomly     | Update or delete cart items                        |
| 10 | PrivacyPolicyAccessibilityTest           | Open and verify Privacy Policy page                |

---

## 🔍 Detailed Test Scenarios

### 1. 🧾 SignInFromDatabase
- Reads user data from MySQL table
- Fills the registration form and submits it

---

### 2. ❌ errorHandlingOnInvalidInputsTest
- Leaves required fields blank
- Checks for "This is a required field" messages

---

### 3. 🔐 LogInFromDatabase
- Logs in with MySQL credentials
- Asserts welcome message

---

### 4. 🚪 logoutWithActions
- Uses hover menu to click "Sign Out"
- Verifies user is redirected to login/homepage

---

### 5. 🔎 SearchForProduct
- Enters product name in search bar
- Checks if results appear

---

### 6. 📦 ViewProductDetails
- Randomly picks category and sub-category
- Opens product and verifies image, price, stock, and details tab

---

### 7. 🛒 AddProductToCartFromDetailsPage
- Randomly selects product size, color, quantity
- Adds to cart and checks for success message

---

### 8. 💳 ProceedToCheckoutTest
- Opens mini cart
- Proceeds to checkout and verifies shipping section

---

### 9. 🔁 EditCartQuantityOrRemoveItemRandomly
- Either increases quantity or deletes an item from the cart
- Verifies price change or empty cart message

---

### 10. 🔐 PrivacyPolicyAccessibilityTest
- Scrolls to footer and clicks on Privacy Policy
- Verifies that the correct page loads

---

## 📝 Notes

- `Thread.sleep()` is used for simple waits, can be replaced with `WebDriverWait`
- Database used: `classicmodels`, table: `customers`
- Test data is dynamically pulled from MySQL
- ChromeDriver must match your browser version
- Adjust MySQL credentials in the `setup()` method

---

## 🙏 Acknowledgments

Special thanks to:
- **Dr. Abdulrahim Alsaqqa** – for technical support
- **Mr. Basheer** – for guidance
- **Ms. Madonna** – for continued encouragement

---

# 🚀 Happy Testing! 🚀
