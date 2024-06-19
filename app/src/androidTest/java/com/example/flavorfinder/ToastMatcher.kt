package com.example.flavorfinder

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class ToastMatcher : BoundedMatcher<Root, Root>(Root::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            return windowToken === appToken
        }
        return false
    }

    companion object {
        fun isToast(): Matcher<Root> {
            return ToastMatcher()
        }
    }
}

