package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CredentialPage {

    protected WebDriver driver;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credTabInput;

    @FindBy(id = "addCred")
    private WebElement addCredElem;

    @FindBy(id = "editCred")
    private List<WebElement> editCredElem;

    @FindBy(id = "deleteCred")
    private List<WebElement> deleteCredElem;

    @FindBy(id = "credential-url")
    private WebElement credUrlField;

    @FindBy(id = "credential-username")
    private WebElement credUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credPasswordField;

    @FindBy(id = "credSubmit")
    private WebElement saveChanges;

    @FindBy(id = "credCancel")
    private WebElement cancel;

    @FindBy(id = "log-out")
    private WebElement logOutInput;

    private WebDriverWait wait;
    private By urlText = By.cssSelector("#credentialTable th.urlText");
    private By usernameText = By.cssSelector("#credentialTable td.usernameText");
    private By encPassword = By.cssSelector("#credentialTable td.encPassword");
    private By editCredBy = By.id("editCred");
    private By delCredBy = By.id("deleteCred");

    public CredentialPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, 5);
        PageFactory.initElements(driver, this);
    }

    public ResultPage addNewCredentials(String url, String username, String password) throws InterruptedException {

        this.addCredElem.click();
        Thread.sleep(2000);

        this.credUrlField.sendKeys(url);
        this.credUsernameField.sendKeys(username);
        this.credPasswordField.sendKeys(password);

        this.saveChanges.click();
        Thread.sleep(2000);

        return new ResultPage(driver);
    }

    public ResultPage editCredentials(String url, String username, String password, int level) throws InterruptedException {

        List<WebElement> marker = this.wait.until(webDriver -> webDriver.findElements(editCredBy));
        marker.get(level - 1).click();
        Thread.sleep(3000);

        this.credUrlField.sendKeys(Keys.CONTROL + "a");
        this.credUrlField.sendKeys(Keys.DELETE);
        this.credUrlField.sendKeys(url);

        this.credUsernameField.sendKeys(Keys.CONTROL + "a");
        this.credUsernameField.sendKeys(Keys.DELETE);
        this.credUsernameField.sendKeys(username);

        this.credPasswordField.sendKeys(Keys.CONTROL + "a");
        this.credPasswordField.sendKeys(Keys.DELETE);
        this.credPasswordField.sendKeys(password);

        this.saveChanges.click();

        Thread.sleep(3000);
        return new ResultPage(driver);
    }

    public ResultPage deleteCredential(int level) throws InterruptedException {

        this.deleteCredElem.get(level - 1).click();
        Thread.sleep(3000);
        return new ResultPage(driver);
    }

    public List<CredForm> readTableCreds(int level) throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> urlTexts = wait.until(webDriver -> webDriver.findElements(urlText));
        List<WebElement> userNameTexts = wait.until(webDriver -> webDriver.findElements(usernameText));
        List<WebElement> passwordTexts = wait.until(webDriver -> webDriver.findElements(encPassword));

        List<CredForm> returnList = new ArrayList<>();

        if(urlTexts == null || urlTexts.isEmpty()) return Collections.emptyList();
        for(int i = 0; i < level; i++){
            String url = urlTexts.get(i).getText();
            String username = userNameTexts.get(i).getText();
            String password = passwordTexts.get(i).getText();

            returnList.add(new CredForm(url, username, password));
        }
        return returnList;
    }

    public CredForm readModalCreds(int level) throws InterruptedException {
        List<WebElement> marker = wait.until(webDriver -> webDriver.findElements(editCredBy));
        marker.get(level - 1).click();
        Thread.sleep(3000);

        String url = this.credUrlField.getAttribute("value");
        String username = this.credUsernameField.getAttribute("value");
        String password = this.credPasswordField.getAttribute("value");

        this.cancel.click();
        Thread.sleep(2000);
        return new CredForm(url, username, password);
    }

    public LoginPage logOut(){
        this.logOutInput.click();
        return new LoginPage(driver);
    }
}
