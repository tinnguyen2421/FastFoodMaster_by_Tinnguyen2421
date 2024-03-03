package com.example.appfood_by_tinnguyen2421;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordTest {


    @Rule
    public ActivityScenarioRule<LoginEmail> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginEmail.class);

    @Test
    public void TestCase1()
    {
        forgotPasswordTest("tpvinh157@gmail.com");
    }
    @Test
    public void TestCase2()
    {
        forgotPasswordTest("tpvinh157@gmail.ohio");
    }
    @Test
    public void TestCase3()
    {
        forgotPasswordTest("");
    }
    @Test
    public void TestCase4()
    {
        forgotPasswordTest("           ");
    }
    @Test
    public void TestCase5()
    {
        forgotPasswordTest("0981031901");
    }
    @Test
    public void TestCase6()
    {
        forgotPasswordTest("$%*@(");
    }
    private void forgotPasswordTest(String email) {
        onView(withId(R.id.forgotpass))
                .perform(click());

        onView(withId(R.id.Emailedt))
                .perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.button2))
                .perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(withId(android.R.id.message), withText("Mật khẩu đã được gửi đến Email của bạn")))
                .check(matches(isDisplayed()));
        mActivityScenarioRule.getScenario().close();

    }
}
