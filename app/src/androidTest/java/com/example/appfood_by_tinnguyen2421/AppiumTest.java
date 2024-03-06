//package com.example.appfood_by_tinnguyen2421;
//
//import io.appium.java_client.MobileElement;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.remote.MobileCapabilityType;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import java.net.URL;
//import java.util.concurrent.TimeUnit;
//
//public class AppiumTest {
//    public static void main(String[] args) {
//        AndroidDriver<MobileElement> driver = null;
//        try {
//            // Set desired capabilities
//            DesiredCapabilities caps = new DesiredCapabilities();
//            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
//            caps.setCapability("appPackage", "com.example.appfood_by_tinnguyen2421");
//            caps.setCapability("appActivity", ".MainActivity");
//            caps.setCapability("autoGrantPermissions", true);
//            caps.setCapability("noReset", true);
//
//            // Initialize AndroidDriver instance
//            URL url = new URL("http://localhost:4723/wd/hub");
//            driver = new AndroidDriver<>(url, caps);
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//            // Find and click on element with id "com.example.appfood_by_tinnguyen2421:id/SignwithEmail"
//            MobileElement el1 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/SignwithEmail");
//            el1.click();
//
//            // Find and fill in email field
//            MobileElement el2 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/Lemaill");
//            el2.sendKeys("tinnguyen2421@gmail.com");
//
//            // Find and fill in password field
//            MobileElement el3 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/Lpasswordd");
//            el3.sendKeys("tinboy1707");
//
//            // Find and click on login button
//            MobileElement el4 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/button4");
//            el4.click();
//
//            // Find and click on 'Thêm Mới' element
//            MobileElement el5 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/themMoi");
//            el5.click();
//
//            // Find and click on 'imageupload' element
//            MobileElement el6 = driver.findElementById("com.example.appfood_by_tinnguyen2421:id/imageupload");
//            el6.click();
//
//            // Find and click on an image (assuming there are multiple images)
//            MobileElement el7 = driver.findElementByXPath("(//android.widget.ImageView[@resource-id=\"com.google.android.apps.docs:id/entry_thumbnail\"])[4]");
//            el7.click();
//
//            // Find and click on another image
//            MobileElement el8 = driver.findElementByXPath("(//android.widget.ImageView[@resource-id=\"com.google.android.apps.docs:id/entry_thumbnail\"])[1]");
//            el8.click();
//
//            // Find and click on 'positive_button' element
//            MobileElement el9 = driver.findElementById("com.google.android.apps.docs:id/positive_button");
//            el9.click();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (driver != null) {
//                driver.quit();
//            }
//        }
//    }
//}
