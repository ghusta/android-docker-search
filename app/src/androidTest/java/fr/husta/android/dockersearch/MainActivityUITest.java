package fr.husta.android.dockersearch;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

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
                .perform(typeText("tomcat"), pressKey(KeyEvent.KEYCODE_ENTER));

        try
        {
            Thread.sleep(2000L);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        View listView = activityRule.getActivity().findViewById(R.id.listView);

        onView(withId(R.id.listView))
                .check(matches(isDisplayed()));

        onData(withId(R.id.listView))
                .onChildView(hasMinimumChildCount(5));

        String firstImageName = "tomcat";
        // click 1st element
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.listitem_image_name))
                .check(matches(withText(firstImageName)))
                .perform(click());

        // click 2nd element
        // group id = 2 and not 1 when 1st group is open
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(2)
                .onChildView(withId(R.id.listitem_image_name))
                .perform(click());

        try
        {
            Thread.sleep(100L);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
