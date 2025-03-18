package com.example.matchcolor
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.startsWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class easyCircleTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun positiveTests(){
        onView(allOf(
            withId(R.id.btnStart),
            withText("start"),
            withParent(isAssignableFrom(ConstraintLayout::class.java)),
            withParent(withId(R.id.clstartwindow))
        )
        ).check(matches(isDisplayed()))
        onView(withId(R.id.btnStart)).perform(click())
        Thread.sleep(550)
        onView(withId(R.id.clstartwindow)).check(matches(not(isDisplayed())))
        onView(withId(R.id.cvEasyFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.cvEasySecond)).check(matches(isDisplayed()))
        onView(withId(R.id.cvEasyThird)).check(matches(isDisplayed()))
        onView(withId(R.id.pb)).check(matches(isDisplayed()))
        onView(allOf(
            withId(R.id.tvScore),
            withText("score: 0")
        )
        ).check(matches(isDisplayed()))
        repeat(20) { i ->
            onView(withContentDescription("imposter")).perform(click())
            Thread.sleep(540)
            onView(withId(R.id.tvScore)).check(matches(withText("SCORE: ${i + 1}")))

        }
        onView(allOf(
            isAssignableFrom(Button::class.java),
            withId(R.id.btnRestart),
            withText("restart"),
            withParent(isAssignableFrom(ConstraintLayout::class.java)),
            withParent(withId(R.id.clRestartWindow)),
        )
        ).check(matches(isDisplayed()))
        Thread.sleep(350)
        onView(withId(R.id.btnRestart)).perform(click())
        repeat(20) {
            onView(withContentDescription("imposter")).perform(click())
        }
    }
}