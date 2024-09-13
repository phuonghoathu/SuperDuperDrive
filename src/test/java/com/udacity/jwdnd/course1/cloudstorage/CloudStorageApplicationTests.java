package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpyBean({
        EncryptionService.class
})
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private EncryptionService encryptionService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void testAccessHome_NoLogin() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testAccessHomePageLogin_andLogout() {
        // Create a test account
        doMockSignUp("Home", "Test", "PageLogin", "123");
        doLogIn("PageLogin", "123");

        // Confirm display home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        // Click logout button
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testCreateNote() {
        // Create a test account
        doMockSignUp("Note", "Test", "CreateNote", "123");
        doLogIn("CreateNote", "123");

        // Confirm display home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note and create note
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title", "Note description");

        // Confirm display success screen create note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());
        driver.findElement(By.id("successId")).click();

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("CreateNote", "123");
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");

        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th.dspNoteTitle")).getText().contains("Note title"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td.dspNoteDescription")).getText().contains("Note description"));
    }

    @Test
    public void testEditNote() {
        // Create a test account
        doMockSignUp("Note", "Test", "EditNote", "123");
        doLogIn("EditNote", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open note tab and create note
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title", "Note description");
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title 2", "Note description 2");
        driver.findElement(By.id("successId")).click();
        //	openTab(webDriverWait,"nav-notes-tab","nav-notes");

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("EditNote", "123");

        // Edit note
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(2) > td:nth-child(1) > button")).click();
        // Fill note information
        fillTextInfo(webDriverWait, "note-title", " edit");
        fillTextInfo(webDriverWait, "note-description", " edit");
        driver.findElement(By.id("noteStyleSubmit")).click();

        // Confirm display success screen edit note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());

        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");

        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(2) > th.dspNoteTitle")).getText().contains("Note title 2 edit"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(2) > td.dspNoteDescription")).getText().contains("Note description 2 edit"));
    }

    @Test
    public void testDeleteNote() {
        // Create a test account
        doMockSignUp("Note", "Test", "DeleteNote", "123");
        doLogIn("DeleteNote", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note and create
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title", "Note description");
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title 2", "Note description 2");
        driver.findElement(By.id("successId")).click();

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("DeleteNote", "123");

        // Delete note tab
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(1) > a")).click();

        // Confirm display success screen delete note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());

        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");

        // Confirm first row is Note title 2
        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th.dspNoteTitle")).getText().contains("Note title 2"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td.dspNoteDescription")).getText().contains("Note description 2"));
    }

    private void createNote(WebDriverWait webDriverWait, String title, String description) {
        driver.findElement(By.id("addNoteButton")).click();

        // Fill note information
        fillTextInfo(webDriverWait, "note-title", title);
        fillTextInfo(webDriverWait, "note-description", description);
        driver.findElement(By.id("noteStyleSubmit")).click();
    }

    @Test
    public void testEditNoteError() {
        // Create a test account
        doMockSignUp("Note", "Test", "NoteError", "123");
        doLogIn("NoteError", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");
        createNote(webDriverWait, "Note title", "Note description");
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-notes-tab", "nav-notes");

        // Edit tab
        driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(1) > button")).click();
        // Fill note information
        fillTextInfo(webDriverWait, "note-title", " edit");
        fillTextInfo(webDriverWait, "note-description", " edit");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.getElementById('note-id').setAttribute('type', 'text');");
        driver.findElement(By.id("note-id")).clear();
        driver.findElement(By.id("note-id")).sendKeys("7");
        driver.findElement(By.id("noteStyleSubmit")).click();

        // Confirm display success screen create note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?error", driver.getCurrentUrl());
    }

    @Test
    public void testCreateCredential() {
        // Create a test account
        doMockSignUp("Note", "Test", "CreateCred", "123");
        doLogIn("CreateCred", "123");

        // Confirm display home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note and create credential
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        // Mock encryptValue and decryptValue
        Mockito.doReturn("encryptValue").when(encryptionService).encryptValue(Mockito.anyString(), Mockito.anyString());
        Mockito.doReturn("encryptValue").when(encryptionService).decryptValue(Mockito.anyString(), Mockito.anyString());
        createCredential(webDriverWait, "https://learn.udacity.com/", "phuong", "abc123");

        // Confirm display success screen create note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());
        driver.findElement(By.id("successId")).click();

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("CreateCred", "123");
        //Mockito.when(encryptionService.encryptValue(Mockito.anyString(), Mockito.anyString())).thenReturn("encryptValue");
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");

        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > th")).getText().contains("https://learn.udacity.com/"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td.dspCredUsername")).getText().contains("phuong"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td.dspCredPassword")).getText().contains("encryptValue"));
    }

    @Test
    public void testEditCredential() {
        // Create a test account
        doMockSignUp("Note", "Test", "EditCred", "123");
        doLogIn("EditCred", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note and create credential
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        createCredential(webDriverWait, "https://learn.udacity.com/", "phuong", "abc123");
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        createCredential(webDriverWait, "https://www.google.com/", "phuong2", "123456");
        driver.findElement(By.id("successId")).click();

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("EditCred", "123");

        // Edit note
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td:nth-child(1) > button")).click();
        // Fill note information
        fillTextInfo(webDriverWait, "credential-url", "nanodegrees/nd035");
        fillTextInfo(webDriverWait, "credential-username", "new");
        // Confirm password display decryptValue
        Assertions.assertTrue(driver.findElement(By.id("credential-password")).getAttribute("value").contains("abc123"));

        fillTextInfo(webDriverWait, "credential-password", "4");
        driver.findElement(By.id("credentialStyleSubmit")).click();

        // Confirm display success screen edit credential
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());

        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");

        // Confirm URL and Username edit
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > th")).getText().contains("https://learn.udacity.com/nanodegrees/nd035"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td.dspCredUsername")).getText().contains("phuongnew"));

        // Confirm password edit
        driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td:nth-child(1) > button")).click();
        Assertions.assertTrue(driver.findElement(By.id("credential-password")).getAttribute("value").contains("abc1234"));
    }

    @Test
    public void testDeleteCredential() {
        // Create a test account
        doMockSignUp("Note", "Test", "DeleteCred", "123");
        doLogIn("DeleteCred", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note and create credential
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        createCredential(webDriverWait, "https://learn.udacity.com/", "phuong", "abc123");
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        createCredential(webDriverWait, "https://www.google.com/", "phuong2", "123456");
        driver.findElement(By.id("successId")).click();

        // Logout and login again
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        doLogIn("DeleteCred", "123");

        // Delete note tab
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");
        driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td:nth-child(1) > a")).click();

        // Confirm display success screen delete note
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());

        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-credentials-tab", "nav-credentials");

        // Confirm first row is phuong2
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > th")).getText().contains("https://www.google.com/"));
        Assertions.assertTrue(driver.findElement(By.cssSelector("#credentialTable > tbody > tr:nth-child(1) > td.dspCredUsername")).getText().contains("phuong2"));
    }

    private void createCredential(WebDriverWait webDriverWait, String url, String username, String password) {
        driver.findElement(By.id("addCredentialButton")).click();

        // Fill note information
        fillTextInfo(webDriverWait, "credential-url", url);
        fillTextInfo(webDriverWait, "credential-username", username);
        fillTextInfo(webDriverWait, "credential-password", password);
        driver.findElement(By.id("credentialStyleSubmit")).click();
    }


    @Test
    public void testUploadFile() {
        // Create a test account
        doMockSignUp("Note", "Test", "UploadFile", "123");
        doLogIn("UploadFile", "123");

        // Confirm display home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Open tab note
        openTab(webDriverWait, "nav-files-tab", "nav-files");

        String fileName = "project-rubric.png";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("File upload failed");
        }

        // Confirm display success screen upload file
        Assertions.assertEquals("http://localhost:" + this.port + "/home/result?success", driver.getCurrentUrl());
        driver.findElement(By.id("successId")).click();
        openTab(webDriverWait, "nav-files-tab", "nav-files");

        Assertions.assertTrue(driver.findElement(By.cssSelector("#fileTable > tbody > tr > th")).getText().contains("project-rubric.png"));
    }


    private void openTab(WebDriverWait webDriverWait, String tabId, String tabContentId) {
        driver.findElement(By.id(tabId)).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(tabContentId)));
        driver.findElement(By.id(tabContentId)).click();
    }

    private void fillTextInfo(WebDriverWait webDriverWait, String elemId, String data) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elemId)));
        WebElement inputTitle = driver.findElement(By.id(elemId));
        inputTitle.click();
        inputTitle.sendKeys(data);
    }
}
