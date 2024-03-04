package com.example.appfood_by_tinnguyen2421;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

import android.view.InputDevice;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import org.hamcrest.Matcher;

public class CustomViewActions {
    public static ViewAction tapAt(final float x, final float y) {
        ViewAction viewAction = actionWithAssertions(new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        return new float[]{x, y};
                    }
                },
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN, // Đặt nguồn đầu vào là InputDevice.SOURCE_UNKNOWN
                0 // Độ chính xác (ở đây là null)
        ));
        return viewAction;
    }
}
