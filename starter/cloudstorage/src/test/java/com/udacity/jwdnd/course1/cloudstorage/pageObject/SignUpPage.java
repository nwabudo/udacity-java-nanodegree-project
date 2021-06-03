package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    private By signUpSuccess = By.id("success-msg");

    private By signUpFailure = By.cssSelector("#error-msg span");

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

    public SignInPage initiateSignUp(SignUpDetails signupObject) throws InterruptedException {
        this.firstNameInput.sendKeys(signupObject.getFirstName());
        this.lastNameInput.sendKeys(signupObject.getLastName());
        this.userNameInput.sendKeys(signupObject.getUsername());
        this.passwordInput.sendKeys(signupObject.getPassword());

        this.submitButton.submit();

        try{
            this.loginLink.click();
        }catch(Exception ex){
            String errorMsg = driver.findElement(signUpFailure).getText();
            System.out.println(errorMsg);
        }
        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        url = url.split("/(?!.*/)")[0] + "/login";
        driver.get(url);
        return new SignInPage(driver);
    }
}
