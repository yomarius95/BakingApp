package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class NavActivityTabletTest {
    @Rule
    public ActivityTestRule<NavActivity> mActivityTestRule
            = new ActivityTestRule<>(NavActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpenRecipeActivity(){
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView((withId(R.id.tv_recipe_ingredients))).check(matches(isDisplayed()));
        onView((withId(R.id.rv_recipe_step_list))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.step_video_view), withClassName(is(SimpleExoPlayerView.class.getName())))).check(matches(isDisplayed()));
        onView((withId(R.id.tv_step_description))).check(matches(isDisplayed()));
        onView((withId(R.id.previous_step_button))).check(matches(isDisplayed())).check(matches(withText("Previous")));
        onView((withId(R.id.next_step_button))).check(matches(isDisplayed())).check(matches(withText("Next")));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
