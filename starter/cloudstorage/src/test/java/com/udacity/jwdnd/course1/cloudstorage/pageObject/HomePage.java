package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public NotePage returnNotePage(){
        this.noteTabInput.click();
        return new NotePage(driver);
    }

    public CredentialPage returnCredentialPage(){
        this.credTabInput.click();
        return new CredentialPage(driver);
    }

    public SignInPage logOut(){
        this.logOutInput.click();
        return new SignInPage(driver);
    }

}
