package com.udacity.bakingapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.activities.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeActivityTests {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void showRecipe() {
        //Verify that all recipes are displayed

        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Cheesecake"))));

        //Verify that the recipes are clickable

        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Brownies"))))
                .perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Yellow Cake"))))
                .perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Cheesecake"))))
                .perform(click());
    }
}
