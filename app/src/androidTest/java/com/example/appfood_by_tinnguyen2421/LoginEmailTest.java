package com.example.appfood_by_tinnguyen2421;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class LoginEmailTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    @Test
    public void TestCase1() {
        performLogin("tinnguyen2421@gmail.com", "tinboy1707");
    }

    @Test
    public void TestCase2() {
        performLogin("tinnguyen2421@gmail.haha", "tinboy1707");
    }

    @Test
    public void TestCase3() {
        performLogin("######", "tinboy1707");
    }

    @Test
    public void TestCase4() {
        performLogin("", "tinboy1707");
    }

    @Test
    public void TestCase5() {
        performLogin("tinnguyen2421@gmail.com", "tinboy123");
    }

    private void performLogin(String email, String password) {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<LoginEmail> scenario = ActivityScenario.launch(LoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText(email));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText(password));
        onView(withId(R.id.button4)).perform(ViewActions.click());
        // Chờ cho hoạt động đăng nhập hoàn thành trước khi tiếp tục
        new Thread(() -> {
            try {
                // Chờ 3 giây
                Thread.sleep(3000);
                // Giảm đếm của CountDownLatch xuống 0
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // Chờ cho đến khi CountDownLatch đếm xuống 0
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed())).withFailureHandler((error, viewMatcher) -> scenario.close()).perform(ViewActions.pressBack()) != null ||
                onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed())).withFailureHandler((error, viewMatcher) -> scenario.close()).perform(ViewActions.pressBack()) != null ||
                onView(withId(R.id.delivery_bottom_navigation)).check(matches(isDisplayed())).withFailureHandler((error, viewMatcher) -> scenario.close()).perform(ViewActions.pressBack()) != null) {
            scenario.close();
        }
    }

}
