package com.Veiled;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import com.Veiled.Activities.IntroActivity;
import com.Veiled.Components.ConnectionChecker.IConnectionChecker;
import com.Veiled.Components.Rememberer.IRememberer;
import com.Veiled.Components.ScreenDetails.IScreenDetails;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.Veiled.IntroActivityTest \
 * com.Veiled.tests/android.test.InstrumentationTestRunner
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class IntroActivityTest extends ActivityInstrumentationTestCase2<IntroActivity> {

    public IntroActivityTest() {
        super("com.Veiled", IntroActivity.class);
    }

    private IntroActivity activity;

    @Test
    public void testRealScreenDetails() throws InterruptedException {
        activity =getActivity();
        activity.initScreenDetails();
        IScreenDetails screenDetails = activity.getScreenInstance();
        Point p = screenDetails.getScreenDetails();
        assertFalse(p.x == 0);
        assertFalse(p.y == 0);
    }

    @Test
    public void testScaleImageLogoByScreenSizeTest() throws InterruptedException {
        activity = getActivity();
        ImageView logo_mare = (ImageView) activity.findViewById(R.id.logoImageView);
        activity.initScreenDetails(1240, 1920);
        activity.scaleLabels();
        Thread.sleep(500);
        int width = logo_mare.getWidth();
        int height = logo_mare.getHeight();

        assertEquals(width, 1240 / 2);
        assertEquals(height, 1920 / 2);
    }

    @Test
    public void testCheckInternetConnectivity() throws InterruptedException {
        activity = getActivity();

        IConnectionChecker mockedConnection = Mockito.mock(IConnectionChecker.class);
        Mockito.when(mockedConnection.isNetworkAvailable(activity.getApplicationContext())).thenReturn(false);
        activity.setConnectionChecker(mockedConnection);
        assertEquals(activity.isNetworkConnected(), false);

        Mockito.when(mockedConnection.isNetworkAvailable(activity.getApplicationContext())).thenReturn(true);
        activity.setConnectionChecker(mockedConnection);
        assertEquals(activity.isNetworkConnected(), true);
    }

    @Test
    public void testRememberMeOption() throws InterruptedException {
        activity = getActivity();

        IRememberer mockedRememberer = Mockito.mock(IRememberer.class);
        Mockito.when(mockedRememberer.hasBeenChecked()).thenReturn(true);
        activity.setRememberer(mockedRememberer);
        activity.isRememberChecked();

        Mockito.verify(mockedRememberer, Mockito.times(1)).getSavedCredentials();
    }
}
