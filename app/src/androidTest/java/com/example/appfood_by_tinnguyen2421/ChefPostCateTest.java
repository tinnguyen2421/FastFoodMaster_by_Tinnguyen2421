package com.example.appfood_by_tinnguyen2421;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPostCate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChefPostCateTest {
    @Rule
    public ActivityTestRule<ChefPostCate> mActivityScenarioRule1 =
            new ActivityTestRule<>(ChefPostCate.class);
    @Rule
    public ActivityScenarioRule<LoginEmail> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginEmail.class);

    @Test
    public void TestCase1() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga Ran","Rat ngon");
    }
    @Test
    public void TestCase2() throws InterruptedException {
        chefPostCateTest("0910930","Ga Ran","Rat ngon");
    }
    @Test
    public void TestCase3() throws InterruptedException {
        chefPostCateTest("####","Ga Ran","Rat ngon");
    }
    @Test
    public void TestCase4() throws InterruptedException {
        chefPostCateTest("        ","Ga Ran","Rat ngon");
    }
    @Test
    public void TestCase5() throws InterruptedException {
        chefPostCateTest("","Ga Ran","Rat ngon");
    }
    @Test
    public void TestCase6() throws InterruptedException {
        chefPostCateTest("Ga Ran","","Rat ngon");
    }
    @Test
    public void TestCase7() throws InterruptedException {
        chefPostCateTest("Ga Ran","2401901","Rat ngon");
    }
    @Test
    public void TestCase8() throws InterruptedException {
        chefPostCateTest("Ga Ran","$@$!@","Rat ngon");
    }
    @Test
    public void TestCase9() throws InterruptedException {
        chefPostCateTest("Ga Ran","        ","Rat ngon");
    }
    @Test
    public void TestCase10() throws InterruptedException {
        chefPostCateTest("Ga Ran","xin chaooooooooooooooooooo","Rat ngon");
    }
    @Test
    public void TestCase11() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","Rat ngon");
    }
    @Test
    public void TestCase12() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","");
    }
    @Test
    public void TestCase13() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","        ");
    }@Test
    public void TestCase14() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","019301301");
    }
    @Test
    public void TestCase15() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","$$$$$$");
    }
    @Test
    public void TestCase16() throws InterruptedException {
        chefPostCateTest("Ga Ran","Ga ran","demooooooooooooooooooooooo");
    }
    private void chefPostCateTest(String cateID,String cateName,String describe) throws InterruptedException {

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.Lemaill),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Lemail),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("tinnguyen2421@gmail.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.Lpasswordd),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Lpassword),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("tinboy1707"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button4), withText("Đăng nhập"),
                        childAtPosition(
                                allOf(withId(R.id.linearlayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());
        Thread.sleep(3000);

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.themMoi),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.menu),
                                        2),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());
        Thread.sleep(500);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.dishes_ID),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(cateID), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.nameCate),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText(cateName), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.Mo_ta),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText(describe), closeSoftKeyboard());

        //
        // Trực tiếp lấy Drawable từ tài nguyên và gán vào ImageView
        mActivityScenarioRule1.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = mActivityScenarioRule1.getActivity().getResources().getDrawable(R.drawable.logo);
                ImageView imageView = mActivityScenarioRule1.getActivity().findViewById(R.id.imageupload);
                imageView.setImageDrawable(drawable);
            }
        });
        onView(withId(R.id.imageupload)).check(matches(isDisplayed()));
        //
        Thread.sleep(2000);




        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.postCate), withText("Thêm thể loại"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        Thread.sleep(2000);


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
