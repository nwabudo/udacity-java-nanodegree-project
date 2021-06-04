package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    protected WebDriver driver;

    @FindBy(id = "nav-files-tab")
    private WebElement fileTabInput;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTabInput;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credTabInput;

    @FindBy(id = "log-out")
    private WebElement logOutInput;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        if(!driver.getTitle().equals("Home")) {
            throw new IllegalStateException("Log In failed, This is not Home Page, " +
                    " current page is: " + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public void createNote(String title, String description){
        this.noteTabInput.click();
    }

    public NotePage returnNotePage() throws InterruptedException {
        this.noteTabInput.click();

        Thread.sleep(3000);

        return new NotePage(driver);
    }

    public CredentialPage returnCredentialPage() throws InterruptedException {
        this.credTabInput.click();

        Thread.sleep(3000);
        return new CredentialPage(driver);
    }

    public LoginPage logOut(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logOutInput);
        new WebDriverWait(driver,4).until(ExpectedConditions.titleIs("Login"));
        return new LoginPage(driver);
    }

}
