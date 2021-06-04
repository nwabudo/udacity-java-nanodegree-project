package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.pageObject.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private NoteService noteService;

	@Autowired
	private UserService userService;

	private static WebDriver driver;

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
	public String editUrlPassword = "kitanma_123";

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
		driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
		homeURL = baseURL + "/home";

		// Delete all existing Credentials
		this.credentialService.deleteAll();

		// Delete all existing Notes
		this.noteService.deleteAll();
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	// Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible.
	@Test
	public void testUserSignUp() throws InterruptedException {
		// Write a test that verifies that an unauthorized user can only access the login and signup pages.

		String loginUrl = baseURL + "/auth/login";
		driver.get(loginUrl);

		Thread.sleep(2000);
		String actualUrl = driver.getCurrentUrl();
		assertEquals("Login", driver.getTitle());
		assertEquals(loginUrl, actualUrl);

		driver.findElement(signUpLink).click();
		Thread.sleep(5000);
		new WebDriverWait(driver,4).until(ExpectedConditions.titleIs("Sign Up"));
		assertEquals("Sign Up", driver.getTitle());

		SignUpPage signUpPage = new SignUpPage(driver);

		// Fill the Sign Up Page
		signUpPage.fillSignUpPage(firstName, lastName, username, password);

		// Confirm that the Values contained in the SignUp form are valid and same as the objects passed
		assertEquals(firstName, signUpPage.getFirstNameField());
		assertEquals(lastName, signUpPage.getLastNameField());
		assertEquals(username, signUpPage.getUsernameField());
		assertEquals(password, signUpPage.getPasswordField());

		// Wait for 2 seconds
		Thread.sleep(2000);

		new SignUpPage(driver).initiateSignUp();

		Thread.sleep(3000);

		assertEquals("Login", driver.getTitle());
	}

	// Try to Login
	@Test
	public void testLogin() throws InterruptedException {
		testUserSignUp();

		driver.get(baseURL + "/auth/login");

		new LoginPage(driver).doLogin(username, password);

		assertEquals("Home", driver.getTitle());
	}

	// Try to navigate to the homePage by url without signing in
	@Test
	public void testUnauthorizedAccessRestrictions() throws InterruptedException{
		//Try accessing home page without Logging in
		String homeLink = baseURL + "/home";
		driver.get(homeLink);

		//Verifies that such attempt redirects user to login page
		assertNotEquals(homeLink, driver.getCurrentUrl());
		assertEquals("Login", driver.getTitle());
	}

	/* Write a test that signs up a new user, logs in,
	verifies that the home page is accessible, logs out,
	and verifies that the home page is no longer accessible.*/
	@Test
	public void testUserSignUpLoginHomePageAccessLogOutHomePageRestrict()
			throws InterruptedException{
		// Sign up
		testUserSignUp();

		// Go to login page
		driver.get(baseURL + "/auth/login");

		//Initialize driver for login page
		LoginPage loginPage = new LoginPage(driver);

		//Have user fill login fields
		HomePage homePage = loginPage.doLogin(username, password);

		//Check that user has access to home page
		assertEquals("Home", driver.getTitle());

		//Wait for 2 seconds
		Thread.sleep(2000);

		//Click log out button to exit home page
		homePage.logOut();

		assertEquals("Login", driver.getTitle());

		Thread.sleep(2000);

		// verifies that the home page is no longer accessible
		testUnauthorizedAccessRestrictions();
	}


	// Write a test that creates a note, and verifies it is displayed.
	@Test
	public void noteCreation() throws InterruptedException {
		testLogin();

		Thread.sleep(2000);

		assertEquals("Home", driver.getTitle());
		NotePage notePage = new HomePage(driver).returnNotePage();

		Thread.sleep(2000);
		ResultPage result = notePage.createNote(title, description);
		HomePage homePage = result.redirectToHome(homeURL);

		Thread.sleep(2000);
		List<NoteForm> notes = homePage.returnNotePage().readNotes(LEVEL);
		assertEquals(notes.size(), 1);

		Thread.sleep(2000);
		NoteForm note = notes.get(LEVEL - 1);
		assertThat(note)
			.isNotNull()
			.satisfies(u -> {
				assertThat(u.getNoteTitle()).isEqualTo(title);
				assertThat(u.getNoteDescription()).isEqualTo(description);
			});
	}

	// Write a test that edits an existing note and verifies that the changes are displayed.
	@Test
	public void noteManipulation() throws InterruptedException {
		noteCreation();

		Thread.sleep(2000);

		User user = this.userService.getUserByUsername(this.username);
		int size = this.noteService.getNotesByUserId(user.getUserId()).size();
		assertEquals("Home", driver.getTitle());
		NotePage notePage = new HomePage(driver).returnNotePage();

		Thread.sleep(2000);
		ResultPage result = notePage.editNote(editTitle, editDescription, LEVEL);
		HomePage homePage = result.redirectToHome(homeURL);

		Thread.sleep(2000);
		List<NoteForm> notes = homePage.returnNotePage().readNotes(size);
		assertEquals(notes.size(), size);

		Thread.sleep(2000);
		NoteForm note = notes.get(LEVEL - 1);
		assertThat(note)
			.isNotNull()
			.satisfies(u -> {
				assertThat(u.getNoteTitle()).isEqualTo(editTitle);
				assertThat(u.getNoteDescription()).isEqualTo(editDescription);
			});
	}

	// Write a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	public void noteDelete() throws InterruptedException {
		noteCreation();

		Thread.sleep(2000);

		User user = this.userService.getUserByUsername(this.username);
		int size = this.noteService.getNotesByUserId(user.getUserId()).size();

		assertEquals("Home", driver.getTitle());
		NotePage notePage = new HomePage(driver).returnNotePage();

		Thread.sleep(2000);
		ResultPage result = notePage.deleteNote(LEVEL);
		HomePage homePage = result.redirectToHome(homeURL);

		Thread.sleep(2000);
		List<NoteForm> notes = homePage.returnNotePage().readNotes(LEVEL);
		assertEquals(notes.size(), size - 1);
	}

	// Write a test that creates a set of credentials, verifies that they are displayed,
	// and verifies that the displayed password is encrypted.
	@Test
	public void createCredential() throws InterruptedException {
		testLogin();

		Thread.sleep(2000);

		assertEquals("Home", driver.getTitle());

		CredentialPage credPage = new HomePage(driver).returnCredentialPage();

		Thread.sleep(2000);
		ResultPage result = credPage.addNewCredentials(url, userName, urlPassword);
		HomePage homePage = result.redirectToHome(homeURL);

		Thread.sleep(2000);
		List<CredForm> creds = homePage.returnCredentialPage().readTableCreds(LEVEL);
		assertEquals(creds.size(), 1);

		Thread.sleep(2000);
		CredForm cred = creds.get(LEVEL - 1);
		assertThat(cred)
			.isNotNull()
			.satisfies(u -> {
				assertThat(u.getUrl()).isEqualTo(url);
				assertThat(u.getUsername()).isEqualTo(userName);
				assertThat(u.getPassword()).isNotEqualTo(urlPassword);
			});
	}

	// Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and
	// verifies that the changes are displayed.
	@Test
	public void manipulateCredential() throws InterruptedException {
		createCredential();
		Thread.sleep(5000);

		assertEquals("Home", driver.getTitle());
		CredentialPage credPage = new HomePage(driver).returnCredentialPage();

		Thread.sleep(3000);
		CredForm cred = credPage.readModalCreds(LEVEL);
		assertThat(cred)
			.isNotNull()
			.satisfies(u -> {
				assertThat(u.getPassword()).isEqualTo(urlPassword);
			});

		ResultPage result = credPage.editCredentials(editUrl, editUserName, editUrlPassword, LEVEL);
		result.redirectToHome(homeURL);

		Thread.sleep(3000);
		assertEquals("Home", driver.getTitle());

		User user = this.userService.getUserByUsername(this.username);
		int size = this.credentialService.getCredentialsByUserId(user.getUserId()).size();

		List<CredForm> creds = new HomePage(driver).returnCredentialPage().readTableCreds(size);
		assertEquals(creds.size(), size);

		Thread.sleep(3000);
		cred = creds.get(LEVEL - 1);
		assertThat(cred)
			.isNotNull()
			.satisfies(u -> {
				assertThat(u.getUrl()).isEqualTo(editUrl);
				assertThat(u.getUsername()).isEqualTo(editUserName);
				assertThat(u.getPassword()).isNotEqualTo(editUrlPassword);
			});
	}

	// Write a test that deletes an existing set of credentials and
	// verifies that the credentials are no longer displayed.
	@Test
	public void deleteCredential() throws InterruptedException {
		createCredential();
		Thread.sleep(5000);

		User user = this.userService.getUserByUsername(this.username);
		int size = this.credentialService.getCredentialsByUserId(user.getUserId()).size();

		assertEquals("Home", driver.getTitle());
		HomePage homePage = new HomePage(driver);

		Thread.sleep(3000);
		CredentialPage credPage = homePage.returnCredentialPage();
		ResultPage result = credPage.deleteCredential(LEVEL);
		homePage = result.redirectToHome(homeURL);

		Thread.sleep(3000);
		List<CredForm> creds = homePage.returnCredentialPage().readTableCreds(LEVEL);
		assertEquals(creds.size(), size - 1);
	}

}
