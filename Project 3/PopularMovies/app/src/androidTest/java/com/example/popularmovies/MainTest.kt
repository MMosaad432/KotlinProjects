package com.example.popularmovies

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.example.popularmovies.screens.MainActivity.MainActivity
import com.example.popularmovies.screens.MainActivity.MoviesRecyclerViewAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MainTest{
    @get:Rule
    val activityScenarioRule : ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    object EspressoHelper {
        fun getCurrentActivity(): Activity? {
            var currentActivity: Activity? = null
            getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
            return currentActivity
        }
    }

    @Test
    fun test_isRecyclerViewVisible_onAppLaunch() {
        onView(withId(R.id.moviesRecyclerView)).check(matches(isDisplayed()))

    }
    @Test
    fun testMan(){
        val latch = CountDownLatch(1)
        latch.await(2, TimeUnit.SECONDS)
        onView(withId(R.id.moviesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<MoviesRecyclerViewAdapter.MoviesRecyclerViewAdapterViewHolder>(1,click()))
    }

}