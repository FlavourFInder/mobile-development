package com.example.flavorfinder.view.ui.signin

import android.widget.Toast
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flavorfinder.R
import com.example.flavorfinder.ToastMatcher
import com.example.flavorfinder.view.ui.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SigninActivityTest {
    private val dummyEmail = "test3@gmail.com"
    private val dummyPassword = "test1234"

    @Before
    fun setup(){
        ActivityScenario.launch(SigninActivity::class.java)
    }

    @Test
    fun testSuccessfulLogin() {
        onView(withId(R.id.emailOrUsernameEditText)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText(dummyPassword), closeSoftKeyboard())

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withText("User Success Login"))
            .inRoot(ToastMatcher.isToast())
            .check(matches(isDisplayed()))
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateToRegister() {
        onView(withId(R.id.GoToRegisterPageTextButton)).perform(click())

        onView(withId(R.id.register_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateToForgotPassword() {
        onView(withId(R.id.ForgotPasswordTextButton)).perform(click())

        onView(withId(R.id.forgotPassword_activity)).check(matches(isDisplayed()))
    }
}
