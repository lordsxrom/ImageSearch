package com.codinginflow.imagesearchapp.steps;

import android.view.KeyEvent;

import androidx.test.espresso.contrib.RecyclerViewActions;

import com.codinginflow.imagesearchapp.R;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.codinginflow.imagesearchapp.utils.UtilsKt.hasExpectedItemsCount;

public class Steps {

    @Then("check view {word} is visible")
    public void checkViewIsVisible(String name) {
        onView(withId(getViewIdByName(name))).check(matches(isDisplayed()));
    }

    @When("check {word} has {int} items")
    public void checkRecyclerHasExpectedItems(String name, int count) {
        onView(withId(getViewIdByName(name))).check(hasExpectedItemsCount(count));
    }

    @Then("scroll {word} to {int} item")
    public void scrollRecyclerToItem(String name, int position) {
        onView(withId(getViewIdByName(name)))
                .perform(RecyclerViewActions.scrollToPosition(position));
    }

    @And("click {word} {int} item")
    public void clickRecyclerItem(String name, int position) {
        onView(withId(getViewIdByName(name))).perform(
                actionOnItemAtPosition(
                        position,
                        click()
                )
        );
    }

    private int getViewIdByName(String name) {
        switch (name) {
            case "Gallery": {
                return R.id.fragment_gallery;
            }
            case "Recycler": {
                return R.id.recycler_view;
            }
            case "Details": {
                return R.id.fragment_details;
            }
            case "Find": {
                return R.id.action_search;
            }
            case "SearchField": {
                return R.id.search_src_text;
            }
            default: {
                return -1;
            }
        }
    }

    @And("click {word}")
    public void clickFind(String name) {
        onView(withId(getViewIdByName(name))).perform(click());
    }

    @Then("write {string} in {word}")
    public void writeInSearchField(String query, String name) {
        // Type dog in search view and click ENTER
        onView(withId(getViewIdByName(name)))
                .perform(
                        typeText(query),
                        pressKey(KeyEvent.KEYCODE_ENTER),
                        closeSoftKeyboard()
                );
    }
}
