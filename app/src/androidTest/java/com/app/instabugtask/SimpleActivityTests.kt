package com.app.instabugtask
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.utility.getRootViewKeyboard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleActivityTests {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @get: Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun make_sure_that_activity_is_running() {
        onView(withId(R.id.allWords)).check(matches(isDisplayed()))
    }

    @Test
    fun check_visibility_progress_text_loading() {
        onView(withId(R.id.allWords))
            .check(matches(isDisplayed()))
        onView(withId(R.id.progress))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun check_keyboard_visibility() {
        onView(withId(R.id.search))
            .check(matches(isNotFocused()))
        onView(withId(R.id.search)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeTextIntoFocusedView("dd"))

        activityScenario.scenario.onActivity {
            assertThat(it.getRootViewKeyboard()).isTrue()
        }

        onView(withId(androidx.appcompat.R.id.search_src_text))
            .check(matches(isFocused()))
        onView(withId(androidx.appcompat.R.id.search_close_btn))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(isFocused()))

        onView(withContentDescription(androidx.appcompat.R.string.abc_toolbar_collapse_description)).perform(click())

        onView(withId(R.id.search))
            .check(matches(isNotFocused()))
    }

    @Test
    fun useAppContext() {
        assertEquals("com.app.instabugtask", context.packageName)
    }
}