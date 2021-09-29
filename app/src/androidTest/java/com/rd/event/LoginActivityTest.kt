package com.rd.event

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rd.event.presentation.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest : BaseUiTest() {
    @get:Rule
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var userName: String
    private lateinit var userEmail: String

    @Test
    fun success_login() {
        userName = "Lucas"
        userEmail = "lucas@gmail.com"

        onView(withId(R.id.et_name)).perform(clearText(), typeText(userName))
        onView(withId(R.id.et_email)).perform(clearText(), typeText(userEmail))
        closeSoftKeyboard()
        onView(withId(R.id.btn_login)).perform(click())
    }

    @Test
    fun error_login_name() {
        userName = ""
        userEmail = "lucas@gmail.com"

        onView(withId(R.id.et_name)).perform(clearText(), typeText(userName))
        onView(withId(R.id.et_email)).perform(clearText(), typeText(userEmail))
        closeSoftKeyboard()
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.et_name)).check(matches(hasErrorText("Nome Inválido")))
    }

    @Test
    fun error_login_email() {
        userName = "Lucas"
        userEmail = "lucas"

        onView(withId(R.id.et_name)).perform(clearText(), typeText(userName))
        onView(withId(R.id.et_email)).perform(clearText(), typeText(userEmail))
        closeSoftKeyboard()
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.et_email)).check(matches(hasErrorText("E-mail Inválido")))
    }
}