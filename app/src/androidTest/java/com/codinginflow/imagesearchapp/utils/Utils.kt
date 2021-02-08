package com.codinginflow.imagesearchapp.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
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

fun hasExpectedItemsCount(expectedCount: Int): ViewAssertion =
    ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        val itemCount = adapter!!.itemCount
        ViewMatchers.assertThat(itemCount, CoreMatchers.`is`(expectedCount))
    }

fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
    }

    override fun matchesSafely(view: View): Boolean {
        val context = view.context
        val expectedBitmap = context.getDrawable(id)?.toBitmap()

        return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
    }
}