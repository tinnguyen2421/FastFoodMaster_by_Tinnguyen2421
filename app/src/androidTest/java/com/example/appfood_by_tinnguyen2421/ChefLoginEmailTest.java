package com.example.appfood_by_tinnguyen2421;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.provider.Telephony;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefFoodPanel_BottomNavigation;
import com.example.appfood_by_tinnguyen2421.ChefAccount.ChefLoginEmail;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ChefLoginEmailTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    @Test
    public void TestCase1() {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<ChefLoginEmail> scenario = ActivityScenario.launch(ChefLoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText("tinnguyen2421@gmail.com"));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText("tinboy1707"));
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
        // Kiểm tra xem đã chuyển sang màn hình ChefFoodPanel_BottomNavigation hay không
        onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed()));
        // Đóng kịch bản Activity của ChefLoginEmail
        scenario.close();
    }
    @Test
    public void TestCase2() {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<ChefLoginEmail> scenario = ActivityScenario.launch(ChefLoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText("tinnguyen2421@gmail.haha"));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText("tinboy1707"));
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
        // Kiểm tra xem đã chuyển sang màn hình ChefFoodPanel_BottomNavigation hay không
        onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed()));
        // Đóng kịch bản Activity của ChefLoginEmail
        scenario.close();
    }
    @Test
    public void TestCase3() {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<ChefLoginEmail> scenario = ActivityScenario.launch(ChefLoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText("######"));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText("tinboy1707"));
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
        // Kiểm tra xem đã chuyển sang màn hình ChefFoodPanel_BottomNavigation hay không
        onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed()));
        // Đóng kịch bản Activity của ChefLoginEmail
        scenario.close();
    }
    @Test
    public void TestCase4() {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<ChefLoginEmail> scenario = ActivityScenario.launch(ChefLoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText(""));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText("tinboy1707"));
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
        // Kiểm tra xem đã chuyển sang màn hình ChefFoodPanel_BottomNavigation hay không
        onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed()));
        // Đóng kịch bản Activity của ChefLoginEmail
        scenario.close();
    }
    @Test
    public void TestCase5() {
        CountDownLatch latch = new CountDownLatch(1);
        // Khởi tạo ActivityScenario cho ChefLoginEmail
        ActivityScenario<ChefLoginEmail> scenario = ActivityScenario.launch(ChefLoginEmail.class);
        // Thực hiện đăng nhập
        onView(withId(R.id.Lemaill)).perform(ViewActions.typeText("tinnguyen2421@gmail.com"));
        onView(withId(R.id.Lpasswordd)).perform(ViewActions.typeText("tinboy1707"));
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
        // Kiểm tra xem đã chuyển sang màn hình ChefFoodPanel_BottomNavigation hay không
        onView(withId(R.id.chef_bottom_navigation)).check(matches(isDisplayed()));
        // Đóng kịch bản Activity của ChefLoginEmail
        scenario.close();
    }

}
