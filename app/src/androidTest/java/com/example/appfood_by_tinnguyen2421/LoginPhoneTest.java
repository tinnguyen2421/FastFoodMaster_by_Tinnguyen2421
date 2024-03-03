package com.example.appfood_by_tinnguyen2421;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.appfood_by_tinnguyen2421.Account.LoginPhone;
import com.example.appfood_by_tinnguyen2421.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginPhoneTest {

    @Rule
    public ActivityScenarioRule<LoginPhone> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginPhone.class);

    @Test
    public void TestCase1() throws InterruptedException {
        loginPhoneTest("0961703373");
    }
    @Test
    public void TestCase2() throws InterruptedException {
        loginPhoneTest("");
    }
    @Test
    public void TestCase3() throws InterruptedException {
        loginPhoneTest("               ");
    }
    @Test
    public void TestCase5() throws InterruptedException {
        loginPhoneTest("abcdhd");
    }
    @Test
    public void TestCase6() throws InterruptedException {
        loginPhoneTest("#######");
    }
    @Test
    public void TestCase7() throws InterruptedException {
        loginPhoneTest("1010101010101010101");
    }
    @Test
    public void TestCase8() throws InterruptedException {
        loginPhoneTest("a");
    }
    private void loginPhoneTest(String phoneNumber) throws InterruptedException {
        onView(withId(R.id.number)).perform(replaceText(phoneNumber), closeSoftKeyboard());

        onView(withId(R.id.otp)).perform(click());

        Thread.sleep(2000);
        // Kiểm tra xem nếu đang hiển thị layout activity_sendotp thì pass
        onView(withId(R.id.phoneno)).check(matches(isDisplayed()));
        mActivityScenarioRule.getScenario().close();
    }
}
