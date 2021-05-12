package com.qa;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseTest {
	
	protected static AppiumDriver driver;
	protected static Properties props;
	InputStream inputStream;
	
	public BaseTest() {
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	@Parameters({"platformName", "platformVersion", "deviceName"})
	@BeforeTest
	public void setup(String platformName, String platformVersion, String deviceName) {
		try {
			props = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
			caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
			caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
			caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
			URL appURL = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
			caps.setCapability("app", "C:\\Users\\shash\\Downloads\\Android.SauceLabs.Mobile.Sample.app.2.7.1.apk");
			
			URL url = new URL(props.getProperty("appiumURL"));
			
			driver = new AppiumDriver(url, caps);
			String sessionId = driver.getSessionId().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void waitForVisibility(MobileElement e) {
		WebDriverWait wait = new WebDriverWait(driver, TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(e));
	}
	
	public void click(MobileElement e) {
		waitForVisibility(e);
		e.click();
	}
	
	public void sendKeys(MobileElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
	}
	
	public String getAttribute(MobileElement e, String attribute) {
		waitForVisibility(e);
		return e.getAttribute(attribute);
	}
	
	public String getText(MobileElement e) {
		waitForVisibility(e);
		return e.getText();
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}