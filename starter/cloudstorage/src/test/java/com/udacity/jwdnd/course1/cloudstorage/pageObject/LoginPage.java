package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    protected WebDriver driver;

    @FindBy(name = "username")
    private WebElement userNameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(css = "#error-msg")
    private WebElement errorMessage;

    private By loginFailureMsg = By.cssSelector("#error-msg");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        if(!driver.getTitle().equals("Login")) {
            throw new IllegalStateException("Log In Redirect failed, This is not Login Page, " +
                    " current page is: " + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public HomePage doLogin(String userName, String password) throws InterruptedException {

        this.userNameInput.sendKeys(userName);
        this.passwordInput.sendKeys(password);

        this.clickLoginButton();
        try{
            if(getErrorMessage())
                System.out.println(this.errorMessage.getText());

            throw new IllegalStateException("Login Failed");
        }catch(Exception ex){
            System.out.println("Login Successful");
        }
        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        if(!url.contains("home")){
            url = url.split("/(?!.*/)")[0] + "/home";
            driver.get(url);
        }
        new WebDriverWait(driver,4).until(ExpectedConditions.titleIs("Home"));

        return new HomePage(driver);
    }

    public void clickLoginButton(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.submitButton);
    }

    public boolean getErrorMessage() {
        return this.errorMessage.isDisplayed();
    }
}
