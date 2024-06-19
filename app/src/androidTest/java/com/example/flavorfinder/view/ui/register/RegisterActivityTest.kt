package com.example.flavorfinder.view.ui.register

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flavorfinder.R
import com.example.flavorfinder.ToastMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(RegisterActivity::class.java)
    }

    @Test
    fun testNavigateToSignIn() {
        onView(withId(R.id.GoToSignInPageTextButton)).perform(click())

        onView(withId(R.id.signIn_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidFormSubmission() {
        onView(withId(R.id.nameEditText)).perform(typeText(""), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withText("Field tidak boleh kosong"))
            .inRoot(ToastMatcher.isToast())
            .check(matches(isDisplayed()))

        onView(withId(R.id.register_activity)).check(matches(isDisplayed()))
    }
}
