package com.wechat.core;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.android.AndroidDriver;

public class WeChat {

	public WebDriver driver;
	private Logger LOGGER = LoggerFactory.getLogger(WeChat.class);

	public WeChat() throws MalformedURLException {
		setup();
	}

	private void setup() throws MalformedURLException {

		//adb connect 127.0.0.1:7555 to enable appium connection to mumu
		//adb shell
		//
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "4.4.4");
		// desired_caps['deviceName'] = 'Android Emulator'
		// miu miu installed port
		capabilities.setCapability("deviceName", "localhost:7555");
		capabilities.setCapability("appPackage", "com.tencent.mm");
		capabilities.setCapability("appActivity", "com.tencent.mm.ui.LauncherUI");
		capabilities.setCapability("noReset", true);
		driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
	}

	public boolean addFriend(String account, String message, String comment) throws InterruptedException {
		Random random = new Random();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		driver.findElement(By.id("com.tencent.mm:id/g_")).click();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		driver.findElements(By.className("android.widget.LinearLayout")).get(2).click();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		driver.findElement(By.id("com.tencent.mm:id/ht")).click();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));

		List<WebElement> acct_inputs = driver.findElements(By.id("com.tencent.mm:id/ht"));
		if (acct_inputs != null) {
			acct_inputs.get(0).sendKeys(account);
		}

		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		driver.findElement(By.id("com.tencent.mm:id/ko")).click();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		try {
			WebElement sendmessagebutton = driver.findElement(By.id("com.tencent.mm:id/ba4"));
			if (sendmessagebutton != null) {
				LOGGER.info("account not found");
				return false;
			}
		} catch (NoSuchElementException e) {
			LOGGER.info("acount found continue");
		}

		try {
			TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
			WebElement sendmessagebutton = driver.findElement(By.id("com.tencent.mm:id/anc"));
			if (sendmessagebutton != null) {
				LOGGER.info("friends added");
				return true;
			}
		} catch (NoSuchElementException e) {
			LOGGER.info("not your own account continue");
		}

		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		driver.findElement(By.id("com.tencent.mm:id/anb")).click();
		TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		try {
			// already be your freind
			TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
			WebElement sendmessagebutton = driver.findElement(By.id("com.tencent.mm:id/anc"));
			if (sendmessagebutton != null) {
				LOGGER.info("friends added");
				return true;
			}
		} catch (NoSuchElementException e) {
			List<WebElement> messageElement = driver.findElements(By.id("com.tencent.mm:id/d0j"));
			TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
			if (messageElement != null) {
				messageElement.get(0).sendKeys(message);
			}
			List<WebElement> memoElement = driver.findElements(By.id("com.tencent.mm:id/d0n"));
			memoElement.get(0).sendKeys(comment);
			TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
			driver.findElement(By.id("com.tencent.mm:id/hd")).click();
			TimeUnit.MICROSECONDS.sleep(random.nextInt(1000));
		}

		return false;

	}
	
	public static void main(String []args) throws MalformedURLException, InterruptedException{
		WeChat wechat = new WeChat();
		wechat.addFriend("sanlingdd", "Hello", "William");
	}

}
