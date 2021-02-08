package com.codinginflow.imagesearchapp.steps;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.rule.ActivityTestRule;

import com.codinginflow.imagesearchapp.MainActivity;
import com.codinginflow.imagesearchapp.idling.CountingIdlingResourceSingleton;

import org.junit.Assert;
import org.junit.Rule;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;


public class Hooks {

    @Rule
    ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void beforeScenario() {
        rule.launchActivity(null);
        IdlingRegistry.getInstance()
                .register(CountingIdlingResourceSingleton.countingIdlingResource);
    }

    @After
    public void afterScenario() {
        IdlingRegistry.getInstance()
                .unregister(CountingIdlingResourceSingleton.countingIdlingResource);
        rule.getActivity().finish();
    }

    @Given("visible activity")
    public void assertActivity() {
        Assert.assertNotNull(rule.getActivity());
    }

}
