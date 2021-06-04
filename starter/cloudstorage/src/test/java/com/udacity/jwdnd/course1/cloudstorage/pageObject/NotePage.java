package com.udacity.jwdnd.course1.cloudstorage.pageObject;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotePage {

    protected WebDriver driver;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTabInput;

    @FindBy(id = "note-title")
    private WebElement noteTitleElem;

    @FindBy(id = "addNote")
    private WebElement addNoteElem;

    @FindBy(id = "editNote")
    private List<WebElement> editNoteElem;

    @FindBy(id = "deleteNote")
    private List<WebElement> deleteNoteElem;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionElem;

    @FindBy(id = "noteSubmitButton")
    private WebElement saveChanges;

    @FindBy(id = "log-out")
    private WebElement logOutInput;

    private WebDriverWait wait;
    private By titleText = By.cssSelector("#noteTable th.titleText");
    private By descText = By.cssSelector("#noteTable td.descText");
    private By editNoteBy = By.id("editNote");
    private By delNoteBy = By.id("deleteNote");

    public NotePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, 5);
        PageFactory.initElements(driver, this);
    }

    public ResultPage createNote(String title, String description) throws InterruptedException {
        // this.noteTabInput.click();
        this.addNoteElem.click();

        Thread.sleep(2000);

        this.noteTitleElem.sendKeys(title);
        this.noteDescriptionElem.sendKeys(description);

        Thread.sleep(2000);

        this.saveChanges.click();

        Thread.sleep(2000);
        return new ResultPage(driver);
    }

    public ResultPage editNote(String title, String description, int level) throws InterruptedException {

        List<WebElement> marker = wait.until(webDriver -> webDriver.findElements(editNoteBy));
        marker.get(level - 1).click();
        // this.editNoteElem.get(level - 1).click();
        Thread.sleep(2000);

        this.noteTitleElem.clear();
        this.noteTitleElem.sendKeys(title);

        this.noteDescriptionElem.clear();
        this.noteDescriptionElem.sendKeys(description);

        Thread.sleep(2000);

        this.saveChanges.click();

        Thread.sleep(2000);
        return new ResultPage(driver);
    }

    public ResultPage deleteNote(int level) throws InterruptedException {
        this.noteTabInput.click();
        this.deleteNoteElem.get(level - 1).click();
        Thread.sleep(2000);
        return new ResultPage(driver);
    }

    public List<NoteForm> readNotes(int level) throws InterruptedException {
        //this.noteTabInput.click();
        List<WebElement> titleTexts = wait.until(webDriver -> webDriver.findElements(titleText));
        List<WebElement> descTexts = wait.until(webDriver -> webDriver.findElements(descText));

        Thread.sleep(3000);
        List<NoteForm> returnList = new ArrayList<>();

        if(titleTexts == null || titleTexts.isEmpty()) return Collections.emptyList();
        for(int i = 0; i < level; i++){
            String noteTitle = titleTexts.get(i).getText();
            String noteDesc = descTexts.get(i).getText();
            returnList.add(new NoteForm(noteTitle, noteDesc));
        }
        return returnList;
    }

}
