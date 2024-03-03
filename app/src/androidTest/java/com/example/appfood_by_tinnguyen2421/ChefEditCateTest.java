package com.example.appfood_by_tinnguyen2421;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChefEditCateTest {

    @Rule
    public ActivityScenarioRule<LoginEmail> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginEmail.class);

    @Test
    public void chefEditCate() {
        // Đăng nhập
        Espresso.onView(ViewMatchers.withId(R.id.Lemaill))
                .perform(replaceText("tinnguyen2421@gmail.com"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.Lpasswordd))
                .perform(replaceText("tinboy1707"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button4))
                .perform(click());
        // Đợi 3 giây
        SystemClock.sleep(3000);


        // Nhấp vào nút "Sửa"
        Espresso.onView(ViewMatchers.withId(R.id.editButton))
                .perform(click());
        // Đợi 3 giây
        SystemClock.sleep(3000);
        Espresso.onView(ViewMatchers.withId(R.id.Cate_id))
                .perform(replaceText("Demo"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.cate_name))
                .perform(replaceText("Demo"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.MotaCate))
                .perform(replaceText("Demo"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.Updatedishh))
                .perform(click());
    }
}
