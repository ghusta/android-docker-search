package fr.husta.android.dockersearch;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.pressMenuKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

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

    @Test
    public void shouldDisplayAboutDialog()
    {
        onView(withId(R.id.activity_main))
                .perform(pressMenuKey());

        try
        {
            Thread.sleep(200L);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        onView(withText(R.string.about))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.about_dialog))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

}
