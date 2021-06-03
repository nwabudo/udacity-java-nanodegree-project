package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	// Sign Up and Login Details
	public String username = "budos";
	public String password = "1234ABCD";
	public String firstName = "Emmanuel";
	public String lastName = "Nwabudo";

	// Note Page Details
	public String title = "Covid-19";
	public String description = "Dangerous and Harmful to Man";

	public String editTitle = "Hustle Everyday";
	public String editDescription = "Sweet and Chilling to Man";

	// Credential Page Details
	public String url = "www.google.com";
	public String userName = "biafra";
	public String urlPassword = "kitanma";

	public String editUrl = "www.udacity.com";
	public String editUserName = "unity_matters";
	public String editUlPassword = "kitanma_123";

	// Level
	public final int LEVEL = 1;

	private By signUpLink = By.id("signup-link");

	public String baseURL;
	public String homeURL;
	boolean status = false;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void openBrowser() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;

		homeURL = baseURL + "/home";

		driver.get(baseURL + "/auth/login");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

//	@BeforeMethod
//	public void openBrowser(){
//		driver.manage().deleteAllCookies();
//
//		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
//
//		WebElement signUpButton = driver.findElement(By.xpath("//a[contains(text(),'Free Signup')]"));
//		signUpButton.click();
//
//	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
			driver = null;
		}
	}

	// Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible.
	@Test
	@Order(1)
	public void getLoginAndRegistrationPage() throws InterruptedException {
		// Write a test that verifies that an unauthorized user can only access the login and signup pages.
		String actualUrl = driver.getCurrentUrl();
		String loginUrl = baseURL + "/auth/login";

		assertEquals("Login", driver.getTitle());
		assertEquals(loginUrl, actualUrl);

		WebElement signUpElement = driver.findElement(signUpLink);
		signUpElement.click();
		actualUrl = driver.getCurrentUrl();
		assertEquals("Sign Up", driver.getTitle());

		SignUpDetails signupObject = new SignUpDetails();
		signupObject.setFirstName(firstName);
		signupObject.setLastName(lastName);
		signupObject.setUsername(username);
		signupObject.setPassword(password);
		SignInPage signInPage = new SignUpPage(driver).initiateSignUp(signupObject);

		HomePage homePage = signInPage.doLogin(username, password);
		homePage.logOut();

		// Try to navigate to the homePage by url without signing in
		String homeLink = baseURL + "/home";
		driver.get(baseURL + "/home");
		actualUrl = driver.getCurrentUrl();
		assertNotEquals(homeLink, actualUrl);
	}

	// Write a test that creates a note, and verifies it is displayed.
	@Test
	@Order(2)
	public void noteCreation(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		NotePage notePage = homePage.returnNotePage();
		ResultPage result = notePage.createNote(title, description);
		homePage = result.redirectToHome(homeURL);

		List<NoteForm> notes = homePage.returnNotePage().readNotes(LEVEL);
		assertEquals(notes.size(), 1);
		NoteForm note = notes.get(LEVEL - 1);

		assertEquals(title, note.getNoteTitle());
		assertEquals(description, note.getNoteDescription());
		homePage.logOut();
	}

	// Write a test that edits an existing note and verifies that the changes are displayed.
	@Test
	@Order(3)
	public void noteManipulation(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		NotePage notePage = homePage.returnNotePage();
		ResultPage result = notePage.editNote(editTitle, editDescription, LEVEL);
		homePage = result.redirectToHome(homeURL);

		List<NoteForm> notes = homePage.returnNotePage().readNotes(LEVEL);
		assertEquals(notes.size(), 1);
		NoteForm note = notes.get(LEVEL - 1);

		assertEquals(editTitle, note.getNoteTitle());
		assertEquals(editDescription, note.getNoteDescription());
		homePage.logOut();
	}

	// Write a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	@Order(4)
	public void noteDelete(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		NotePage notePage = homePage.returnNotePage();
		ResultPage result = notePage.deleteNote(LEVEL);
		homePage = result.redirectToHome(homeURL);

		List<NoteForm> notes = homePage.returnNotePage().readNotes(LEVEL);
		assertEquals(notes.size(), 0);
		homePage.logOut();
	}

	// Write a test that creates a set of credentials, verifies that they are displayed,
	// and verifies that the displayed password is encrypted.
	@Test
	@Order(5)
	public void createCredential(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		CredentialPage credPage = homePage.returnCredentialPage();
		ResultPage result = credPage.addNewCredentials(url, userName, urlPassword);
		homePage = result.redirectToHome(homeURL);

		List<CredForm> creds = homePage.returnCredentialPage().readTableCreds(LEVEL);
		assertEquals(creds.size(), 1);

		CredForm cred = creds.get(LEVEL - 1);
		assertEquals(url, cred.getUrl());
		assertEquals(userName, cred.getUsername());
		assertNotEquals(urlPassword, cred.getPassword());

		homePage.logOut();
	}

	// Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and
	// verifies that the changes are displayed.
	@Test
	@Order(6)
	public void manipulateCredential(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		CredentialPage credPage = homePage.returnCredentialPage();

		CredForm cred = credPage.readModalCreds(LEVEL);
		assertEquals(urlPassword, cred.getPassword());

		ResultPage result = credPage.editCredentials(editUrl, editUserName, editUlPassword, LEVEL);
		homePage = result.redirectToHome(homeURL);

		List<CredForm> creds = homePage.returnCredentialPage().readTableCreds(LEVEL);
		assertEquals(creds.size(), 1);

		cred = creds.get(LEVEL - 1);
		assertEquals(editUrl, cred.getUrl());
		assertEquals(editUserName, cred.getUsername());
		assertNotEquals(editUlPassword, cred.getPassword());

		homePage.logOut();
	}

	// Write a test that deletes an existing set of credentials and
	// verifies that the credentials are no longer displayed.
	@Test
	@Order(7)
	public void deleteCredential(){
		HomePage homePage = new SignInPage(driver).doLogin(username, password);

		CredentialPage credPage = homePage.returnCredentialPage();
		ResultPage result = credPage.deleteCredential(LEVEL);
		homePage = result.redirectToHome(homeURL);

		List<CredForm> notes = homePage.returnCredentialPage().readTableCreds(LEVEL);
		assertEquals(notes.size(), 0);
		homePage.logOut();
	}

}
