package com.codinginflow.imagesearchapp

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.codinginflow.imagesearchapp.idling.CountingIdlingResourceSingleton
import com.codinginflow.imagesearchapp.ui.gallery.UnsplashPhotoAdapter
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    @get:Rule
    var activityScenarioRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance()
            .register(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance()
            .unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }

    @Test
    fun test10Item() {
        // Check fragment gallery was opened and shows recycler
        onView(withId(R.id.fragment_gallery)).check(
            matches(
                allOf(
                    isDisplayed(),
                    hasDescendant(
                        allOf(
                            withId(R.id.recycler_view),
                            isDisplayed()
                        )
                    )
                )
            )
        )

        // Check that recycler got 30 items after download
        onView(withId(R.id.recycler_view)).check(hasExpectedItemsCount(30))

        // Scroll to 10 item
        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<UnsplashPhotoAdapter.PhotoViewHolder>(10))

        // Check that 10 item has some text and image
        onView(withId(R.id.recycler_view)).check(
            matches(
                atPosition(
                    10,
                    allOf(
                        hasDescendant(
                            allOf(
                                withId(R.id.text_view_user_name),
                                not(withText(""))
                            )
                        ),
                        hasDescendant(
                            allOf(
                                withId(R.id.image_view),
                                not(withDrawable(R.drawable.ic_error))
                            )
                        )
                    )
                )
            )
        )

        // Click on 10 item
        onView(withId(R.id.recycler_view)).perform(
            actionOnItemAtPosition<UnsplashPhotoAdapter.PhotoViewHolder>(
                10,
                click()
            )
        )

        // Check fragment details was opened and shows creator and image
        onView(withId(R.id.fragment_details)).check(
            matches(
                allOf(
                    isDisplayed(),
                    hasDescendant(
                        allOf(
                            withId(R.id.text_view_creator),
                            not(withText(""))
                        )
                    ),
                    hasDescendant(
                        allOf(
                            withId(R.id.image_view),
                            not(withDrawable(R.drawable.ic_error))
                        )
                    )
                )
            )
        )

        // Click to author TextView
        onView(withId(R.id.text_view_creator)).perform(click())

        // Check outcome intent
        intended(hasAction(Intent.ACTION_VIEW))
    }

    @Test
    fun testSearch() {
        // Press search button
        onView(withId(R.id.action_search)).perform(click())

        // Type dog in search view and click ENTER
        onView(withId(R.id.search_src_text))
            .perform(
                typeText("dog"),
                pressKey(KeyEvent.KEYCODE_ENTER),
                closeSoftKeyboard()
            )

        // Check that recycler got 30 items after download
        onView(withId(R.id.recycler_view)).check(hasExpectedItemsCount(30))
    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    private fun hasExpectedItemsCount(expectedCount: Int): ViewAssertion =
        ViewAssertion { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            val itemCount = adapter!!.itemCount
            assertThat(itemCount, `is`(expectedCount))
        }

    private fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            val expectedBitmap = context.getDrawable(id)?.toBitmap()

            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }

}