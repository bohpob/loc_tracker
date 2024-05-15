package cz.cvut.fit.poberboh.loc_tracker

import android.Manifest
import android.telephony.TelephonyManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        // Grant necessary permissions for the test
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_PHONE_STATE
    )

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("cz.cvut.fit.poberboh.loc_tracker", appContext.packageName)
    }

    // Test to verify the visibility of the 5G icon after updating the network to 5G.
    @Test
    fun testFiveGIconVisibility() {
        // Run network icon update to 5G on the main thread
        activityRule.activity.runOnUiThread {
            activityRule.activity.updateNetworkIcon(TelephonyManager.NETWORK_TYPE_NR)
        }

        // Check if the 5G icon is now visible
        onView(withId(R.id.fiveGIcon)).check(matches(isDisplayed()))
    }

    // Test to verify the change in visibility of the 5G icon when switching from 2G to 5G.
    @Test
    fun testFiveGIconVisibilityChange() {
        // Simulate a change in network type to 2G
        activityRule.activity.runOnUiThread {
            activityRule.activity.updateNetworkIcon(TelephonyManager.NETWORK_TYPE_GSM)
        }

        // Check if the 5G icon is still hidden
        onView(withId(R.id.fiveGIcon)).check(matches(not(isDisplayed())))

        // Simulate a change in network type to 5G
        activityRule.activity.runOnUiThread {
            activityRule.activity.updateNetworkIcon(TelephonyManager.NETWORK_TYPE_NR)
        }

        // Check if the 5G icon is now visible
        onView(withId(R.id.fiveGIcon)).check(matches(isDisplayed()))
    }

    @Test
    fun testStartButtonNavigation() {
        // Click on the start button
        onView(withId(R.id.button_start)).perform(ViewActions.click())

        // Verify that navigation to the map screen occurred
        onView(withId(R.id.mapFragment)).check(matches(isDisplayed()))
    }

    // Test to verify that the fragment layout is inflated correctly
    @Test
    fun testFragmentLayoutInflation() {
        // Check if the start button is displayed
        onView(withId(R.id.button_start)).check(matches(isDisplayed()))
    }
}
