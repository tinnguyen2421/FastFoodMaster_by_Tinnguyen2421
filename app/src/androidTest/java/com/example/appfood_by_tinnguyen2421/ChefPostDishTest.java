package com.example.appfood_by_tinnguyen2421;


import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;

import androidx.activity.result.ActivityResult;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityOptionsCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.core.app.ActivityOptionsCompat;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.appfood_by_tinnguyen2421.Account.LoginEmail;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPostCate;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPostDish;
import com.google.android.apps.common.testing.accessibility.framework.replacements.Point;

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
public class ChefPostDishTest {

    @Rule
    public ActivityScenarioRule<LoginEmail> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginEmail.class);
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE");
    @Before
    public void setUp() throws Exception {
        Intents.init();
    }
    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
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

        ViewInteraction textInputEditText3 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.DishName),
                                0),
                        0));
        textInputEditText3.perform(scrollTo(), replaceText("Ga Ran Cay"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.DishPrice),
                                0),
                        0));
        textInputEditText4.perform(scrollTo(), replaceText("30000"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.DishDetail),
                                0),
                        0));
        textInputEditText5.perform(scrollTo(), replaceText("Rat Ngon"), closeSoftKeyboard());

        ViewInteraction textInputEditText6 = onView(
                allOf(withText("Rat Ngon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.DishDetail),
                                        0),
                                0)));
        textInputEditText6.perform(scrollTo(), click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withText("Rat Ngon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.DishDetail),
                                        0),
                                0)));
        textInputEditText7.perform(scrollTo(), replaceText("Rat Ngon"));

        ViewInteraction textInputEditText8 = onView(
                allOf(withText("Rat Ngon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.DishDetail),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction switchCompat = onView(
                allOf(withId(R.id.DishAvailable), withText("Còn món"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        switchCompat.perform(scrollTo(), click());

        ViewInteraction switchCompat2 = onView(
                allOf(withId(R.id.DiscountSwitch), withText("Giảm giá?"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                7)));
        switchCompat2.perform(scrollTo(), click());
        ViewInteraction textInputEditText9 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(R.id.DishDiscount),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("20000"), closeSoftKeyboard());

//

//        onView(withId(R.id.imageupload)).perform(click());
//        Thread.sleep(2000);
//        onView(isRoot()).perform(actionWithAssertions(new GeneralClickAction(
//                Tap.SINGLE,
//                new CoordinatesProvider() {
//                    @Override
//                    public float[] calculateCoordinates(View view) {
//                        int[] locationOnScreen = new int[2];
//                        view.getLocationOnScreen(locationOnScreen);
//                        float x = locationOnScreen[0] + 500; // Xác định tọa độ X trên màn hình
//                        float y = locationOnScreen[1] + 500; // Xác định tọa độ Y trên màn hình
//                        return new float[]{x, y};
//                    }
//                },
//                Press.FINGER
//        )));
        // GIVEN


// Tạo Intent dự kiến
        Intent expectedIntent = new Intent(Intent.ACTION_PICK);
        expectedIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

// Tạo androidx.activity.result.ActivityResult từ androidx.core.app.ActivityOptionsCompat
        androidx.activity.result.ActivityResult activityResult =
                new androidx.activity.result.ActivityResult(
                        Activity.RESULT_OK,
                        expectedIntent
                );

// Chuyển đổi từ androidx.activity.result.ActivityResult sang android.app.Instrumentation.ActivityResult
        android.app.Instrumentation.ActivityResult instrumentationActivityResult =
                new android.app.Instrumentation.ActivityResult(
                        activityResult.getResultCode(),
                        activityResult.getData()
                );

// Sử dụng intending() để kịch bản phản hồi cho Intent dự kiến
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_PICK))
                .respondWith(instrumentationActivityResult);

// Xác nhận rằng Intent dự kiến đã được gửi đi
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_PICK));

// Thực hiện hành động click trên view có id là R.id.imageupload
        onView(withId(R.id.imageupload)).perform(click());

// Xác nhận rằng Intent dự kiến đã được gửi đi
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_PICK));

// Kết thúc Intents sau khi test hoàn thành

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.PostDish), withText("Đăng món"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));
        appCompatButton2.perform(scrollTo(), click());
        Thread.sleep(3000);
        onView(withText("Đăng món thành công"))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()));

    }
    private ActivityResult createGalleryPickActivityResultStub() {
        Resources resources = InstrumentationRegistry.getInstrumentation().getContext().getResources();
        Uri imageUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceTypeName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceEntryName(R.drawable.ic_launcher_background)
        );
        Intent resultIntent = new Intent();
        resultIntent.setData(imageUri);
        return new ActivityResult(RESULT_OK, resultIntent);
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
