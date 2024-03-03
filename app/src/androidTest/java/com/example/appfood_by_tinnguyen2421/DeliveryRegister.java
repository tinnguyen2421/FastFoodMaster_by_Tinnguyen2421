package com.example.appfood_by_tinnguyen2421;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.appfood_by_tinnguyen2421.DeliveryAccount.DeliveryRegisteration;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeliveryRegister {

    @Rule
    public ActivityScenarioRule<DeliveryRegisteration> mActivityScenarioRule =
            new ActivityScenarioRule<>(DeliveryRegisteration.class);

    @Test
    public void mainActivityTest() throws InterruptedException {
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.SignUp), withText("Đăng kí"),
//                        childAtPosition(
//                                allOf(withId(R.id.linearlayout),
//                                        childAtPosition(
//                                                withId(R.id.main_menu),
//                                                1)),
//                                4),
//                        isDisplayed()));
//        materialButton.perform(click());
//
//        ViewInteraction materialButton2 = onView(
//                allOf(withId(R.id.delivery), withText("Bạn là Người giao hàng"),
//                        childAtPosition(
//                                allOf(withId(R.id.linearlayout),
//                                        childAtPosition(
//                                                withId(R.id.back3),
//                                                0)),
//                                3),
//                        isDisplayed()));
//        materialButton2.perform(click());

        ViewInteraction textInputEditText = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.fname),
                                0),
                        0));
        textInputEditText.perform(scrollTo(), replaceText("Nguyen"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.lname),
                                0),
                        0));
        textInputEditText2.perform(scrollTo(), replaceText("Tin"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.Emailid),
                                0),
                        0));
        textInputEditText3.perform(scrollTo(), replaceText("tinnguyen2421@gmail.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.password),
                                0),
                        0));
        textInputEditText4.perform(scrollTo(), replaceText("tinboy1707"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.confirmpassword),
                                0),
                        0));
        textInputEditText5.perform(scrollTo(), replaceText("tinboy1707"), closeSoftKeyboard());

        ViewInteraction textInputEditText6 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.mobileno),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(replaceText("0961703373"), closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.Houseno),
                                0),
                        0));
        textInputEditText7.perform(scrollTo(), replaceText("50PCL"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.State),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        materialTextView.perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.Signupp)).perform(click());

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
