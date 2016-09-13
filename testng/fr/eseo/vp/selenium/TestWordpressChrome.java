package fr.eseo.vp.selenium;

import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestWordpressChrome {

  private WebDriver driver; // For JUNIT must be static!
  private final static String POST_TITLE = "Coucou clarène";

  @BeforeClass
  public void beforeClass() {
    File phantomJSPath = new File("/Users/valentinpichavant/javaLibraries/phantomjs/bin/phantomjs");
    if (!phantomJSPath.exists()) {
      fail("Failed to find the phantomJS executable.");
    }
    System.setProperty("webdriver.chrome.driver", "/Users/valentinpichavant/javaLibraries/chrome/chromedriver");
    driver = new ChromeDriver();
  }

  @Test(priority=1)
  public void testTitre() {
    driver.get("http://192.168.4.10/www/wordpress");
    // Assert that the web page title is correct
  }


  @Test(priority=2)
  public void testMetaData() {
    List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='meta-2']/ul/li"));
    // Assert that there are four elements in the list
    // Assert that the first element's getText() returns "Log in"
  }

  @Test(priority=3)
  public void testLogin() {
    List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='meta-2']/ul/li"));
    webElements.get(0).findElement(By.tagName("a")).sendKeys(Keys.ARROW_DOWN);;
    WebElement loginLink = webElements.get(0).findElement(By.tagName("a"));
    loginLink.click();
    // assert the page title is correct
    WebElement usernameBoxElement = driver.findElement(By.name("log"));
    // possibly assert the userbox is present
    WebElement passwordBox = driver.findElement(By.name("pwd"));
    // possibly assert password box is present
    WebElement loginButton = driver.findElement(By.name("wp-submit"));
    // possibly assert login button is present
  }

  @Test(priority=4)
  public void testAdmin() {
    WebElement usernameBoxElement = driver.findElement(By.name("log"));
    WebElement passwordBox = driver.findElement(By.name("pwd"));
    WebElement loginButton = driver.findElement(By.name("wp-submit"));
    usernameBoxElement.sendKeys("ldcr");
    passwordBox.sendKeys("network");
    loginButton.click();
    WebElement howdy = driver.findElement(By.id("wp-admin-bar-my-account"));
    // assert that howdy contains the text "Howdy, ldcr"

  }

  @Test(priority=5)
  public void testAddPost() {
    Actions action = new Actions(driver);
    WebElement postMenu = driver.findElement(By.xpath("//*[@id='menu-posts']/a"));
    action.moveToElement(postMenu).build().perform();
    WebElement addPost = driver.findElement(By.linkText("Add New"));
    addPost.click();
    // assert the page title is correct
  }

  @Test(priority=6)
  public void testPublish() {
    WebElement postTitle = driver.findElement(By.name("post_title"));
    WebElement postText = driver.findElement(By.xpath("//*[@id='content-html']"));
    postText.click();
    WebElement postBody = driver.findElement(By.className("wp-editor-area"));
    WebElement publish = driver.findElement(By.id("publish"));
    postTitle.clear();
    postTitle.sendKeys(POST_TITLE);
    postBody.sendKeys("This is an example automated post");
    publish.click();
  }

  @Test(priority=7)
  public void testFindAndViewPost() {
    Actions action = new Actions(driver);
    WebElement postMenu = driver.findElement(By.xpath("//*[@id='menu-posts']/a"));
    action.moveToElement(postMenu).build().perform();
    WebElement allPosts = driver.findElement(By.linkText("All Posts"));
    allPosts.click();
    assertEquals(driver.getTitle(), "Posts ‹ The Greatest Web Application Ever (R) 1 — WordPress",
        "not on login page?");
    WebElement searchPosts = driver.findElement(By.id("post-search-input"));
    searchPosts.clear();
    searchPosts.sendKeys(POST_TITLE);
    WebElement searchButton = driver.findElement(By.id("search-submit"));
    searchButton.click();
    List<WebElement> postsList = driver.findElements(By.xpath("//*[@id='the-list']/tr"));
    int i = 0;
    WebElement viewPost = null;
    while (viewPost == null && i < postsList.size()) {
      WebElement rowTitle = postsList.get(i).findElement(By.tagName("td"))
          .findElement(By.tagName("strong")).findElement(By.tagName("a"));
      if (rowTitle.getAttribute("title").contains(POST_TITLE)) {
        action.moveToElement(rowTitle).build().perform();
        viewPost =
            postsList.get(i).findElement(By.tagName("td")).findElement(By.className("row-actions"))
                .findElement(By.className("view")).findElement(By.tagName("a"));
      }
      i++;
    }
    // assert that viewPost is not null
    viewPost.click();
    // assert title is correct
    WebElement entryTitle = driver.findElement(By.className("entry-title"));
    // assert entryTitle text is correct should be POST_TITLE
  }

  @Test(priority=8)
  public void testLogOff() {
    Actions action = new Actions(driver);
    WebElement userMenu = driver.findElement(By.xpath("//*[@id='wp-admin-bar-my-account']"));
    action.moveToElement(userMenu).build().perform();
    WebElement logout = driver.findElement(By.xpath("//*[@id='wp-admin-bar-logout']/a"));
    logout.click();
    // assert the title is correct
  }

  @AfterClass
  public void afterClass() {
    driver.quit();
  }

}
