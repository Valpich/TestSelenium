package fr.eseo.vp.selenium;

import static org.testng.Assert.*;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class TestEseo {

  private WebDriver driver; // For JUNIT must be static!

  @BeforeClass
  public void beforeClass() {
    File phantomJSPath =
        new File("/Users/valentinpichavant/javaLibraries/chrome/chromedriver");
    if (!phantomJSPath.exists()) {
      fail("Failed to find the phantomJS executable.");
    }
    Capabilities caps = new DesiredCapabilities();
    ((DesiredCapabilities) caps).setJavascriptEnabled(true);
    ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
    ((DesiredCapabilities) caps).setCapability(
        PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJSPath.getAbsolutePath());
    driver = new PhantomJSDriver(caps);
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    driver.manage().window().setSize(new Dimension(1024, 768));
  }

  @Test
  public void testTitreEseo() {
    driver.get("http://www.eseo.fr");
    assertEquals(driver.getTitle(),
        "Ecole d'ingenieur informatique electronique telecoms reseaux biomedical environnement");
    try {
      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      ImageIO.write(ImageIO.read(screenshot), "PNG", new File("screenshot.png"));
    } catch (IOException ioe) {
      fail("Screenshot not saved");
    }
  }
  
  @AfterClass
  public void afterClass() {
    driver.quit();
  }
  
}
