package com.example.appfood_by_tinnguyen2421;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPostDish;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Before
    public void setUp() {
        // Khởi tạo Intents trước khi chạy mỗi test
        Intents.init();
    }
    @After
    public void tearDown() {
        // Giải phóng Intents sau khi mỗi test hoàn thành
        Intents.release();
    }


    @Rule
    public ActivityScenarioRule<LoginEmail> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginEmail.class);
    public IntentsTestRule<LoginEmail> activityRule = new IntentsTestRule<>(LoginEmail.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE");

    @Test
    public void mainActivityTest2() throws InterruptedException {


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
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.CateLayout),
                        childAtPosition(
                                allOf(withId(R.id.SwipeRevealLayout),
                                        childAtPosition(
                                                withId(R.id.Recycle_cate),
                                                0)),
                                1),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.themMoi),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

//        Uri someImageUri = Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/1000000262");
//
//        onView(withId(R.id.imageupload)).perform(click());
//
//        // Verify that an intent to pick an image from the gallery is sent
//        intended(allOf(
//                hasAction(Intent.ACTION_PICK),
//                hasType("image/*")
//        ));
//
//        // Mock a result for the intent (in this case, selecting an image)
//        Uri imageUri = Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/1000000262");
//        Intent resultData = new Intent();
//        resultData.setData(imageUri);
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
//
//        // Stub the result for the intent
//        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);
//
//        // Verify that the selected image is displayed in the UI
//        onView(withId(R.id.imageupload)).check(matches(isDisplayed()));
        // Click vào nút mở thư viện ảnh
        onView(withId(R.id.imageupload)).perform(click());

        // Xác định rằng Intent ACTION_PICK đã được gửi từ activity hiện tại
        intended(allOf(
                hasAction(Intent.ACTION_PICK),
                toPackage(activityRule.getActivity().getPackageName())
        ));

        // Giả lập kết quả cho Intent (chọn một ảnh)
        Uri imageUri = Uri.parse("content://media/external/images/media/123");
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Giả lập kết quả trả về từ Intent
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);
        // Xác định rằng ảnh đã được chọn được hiển thị trong giao diện người dùng
        onView(withId(R.id.imageupload)).check(matches(isDisplayed()));

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
