package space.dotcat.assistant.screen.roomList;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RoomsActivityTest {

    @Rule
    public final ActivityTestRule<RoomsActivity> mTestRule =
            new ActivityTestRule<>(RoomsActivity.class);

    @Before
    public void init() {
        RepositoryProvider.provideAuthRepository().saveUrl(new Url("https://api.ks-cube.tk/"));
        Intents.init();
    }

    @After
    public void clear() {
        Intents.release();
    }

    @Test
    public void testRecyclerViewCreated() throws Exception {
        onView(withId(R.id.recyclerViewRooms))
                .check(matches((withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))));

        onView(withId(R.id.tv_error_message))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testClickOnRoom() throws Exception {
        onView(withId(R.id.recyclerViewRooms)).perform(actionOnItemAtPosition(0, click()));

        Intents.intended(hasComponent(RoomDetailsActivity.class.getName()));
    }

    @Test
    public void testScrollRecyclerView() throws Exception {
        onView(withId(R.id.recyclerViewRooms)).perform(scrollToPosition(2))
                .perform(scrollToPosition(0))
                .perform(scrollToPosition(4));
    }

    private void launchActivity() {
        Intent intent = new Intent(InstrumentationRegistry.getContext(), RoomsActivity.class);

        mTestRule.launchActivity(intent);
    }
}
