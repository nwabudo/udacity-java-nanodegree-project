package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpPage {

    protected WebDriver driver;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(name = "username")
    private WebElement userNameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(css = "#success-msg")
    private WebElement successMessage;

    @FindBy(css = "#error-msg")
    private WebElement errorMessage;

    private By signUpFailureMessage = By.cssSelector("#error-msg span");

    @FindBy(id = "login-link")
    private WebElement loginLink;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        if(!driver.getTitle().equals("Sign Up")) {
            throw new IllegalStateException("Sign Up redirect failed, this is not Sign Up Page, " +
                    " current page is: " + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public void fillSignUpPage(String firstName, String lastName, String username, String password) throws InterruptedException {
        this.firstNameInput.sendKeys(firstName);
        this.lastNameInput.sendKeys(lastName);
        this.userNameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
    }

    public String getFirstNameField() {
        return firstNameInput.getAttribute("value");
    }

    public String getLastNameField() {
        return lastNameInput.getAttribute("value");
    }

    public String getUsernameField() {
        return userNameInput.getAttribute("value");
    }

    public String getPasswordField() {
        return passwordInput.getAttribute("value");
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public void clickSignUpButton(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
    }

    public void clickLogInButton(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginLink);
    }

    public LoginPage initiateSignUp() throws InterruptedException {

        clickSignUpButton();

        Thread.sleep(2000);

         try{
             if(getErrorMessage())
                 System.out.println(this.driver.findElement(signUpFailureMessage).getText());
         }catch(Exception ex){
             clickLogInButton();
         }

        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        if(!url.contains("login")){
            url = url.split("/(?!.*/)")[0] + "/login";
            driver.get(url);
        }
        new WebDriverWait(driver,4).until(ExpectedConditions.titleIs("Login"));
        return new LoginPage(driver);
    }

    public boolean getErrorMessage() {
        return this.errorMessage.isDisplayed();
    }
}
