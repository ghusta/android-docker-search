package fr.husta.android.dockersearch;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest
{

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldDisplayListOnSearch()
    {
        onView(withId(R.id.menu_search))
                .perform(click());

        onView(isAssignableFrom(EditText.class))
                .perform(typeText("tomcat")
                        /*, pressKey(KeyEvent.KEYCODE_ENTER) */
                        , closeSoftKeyboard());

        onView(withId(R.id.listView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.listView))
                .check(matches(isAssignableFrom(AdapterView.class)));

//        onData(withId(R.id.listView))
//                .atPosition(1).perform(click());

    }

}
