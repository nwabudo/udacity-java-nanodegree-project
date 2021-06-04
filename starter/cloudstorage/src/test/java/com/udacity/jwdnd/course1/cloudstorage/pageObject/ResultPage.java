package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    protected WebDriver driver;

    @FindBy(className = "continue")
    private WebElement continueButton;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
        if(!driver.getTitle().equals("Result")) {
            throw new IllegalStateException("Result redirect failed, this is not Result Page, " +
                    " current page is: " + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public HomePage redirectToHome(String url) throws InterruptedException {
        this.continueButton.click();
        Thread.sleep(3000);
        driver.get(url);
        Thread.sleep(3000);
        return new HomePage(driver);
    }
}
