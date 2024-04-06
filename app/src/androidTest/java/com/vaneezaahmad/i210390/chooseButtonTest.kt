package com.vaneezaahmad.i210390

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class chooseButtonTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(ActivityChoose::class.java)

    @Before
    fun setUp() {
        // Initialize Intents before each test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Intents after each test
        Intents.release()
    }

    @Test
    fun testUser() {

        onView(withId(R.id.user))
            .perform(click())

        // Check the expected outcome
        onView(withId(R.id.loginText))
            .check(matches(withText("Login")))
    }

    @Test
    fun testMentor() {

        onView(withId(R.id.mentorButton))
            .perform(click())

        // Check the expected outcome
        onView(withId(R.id.mentorLogin))
            .check(matches(withText("Mentor Login")))
    }
}