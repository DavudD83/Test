package space.dotcat.assistant.screen.roomDetail;


import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
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
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.roomList.RoomsActivity;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RoomDetailActivityTest {

    private final static AuthorizationAnswer ERROR = new AuthorizationAnswer("ERROR",
            "ERROR");

    @Rule
    public RxJavaTestRule mRxTestRule = new RxJavaTestRule();

    @Before
    public void init() {
        Intents.init();
    }

    @After
    public void clear() {
        Intents.release();
        RepositoryProvider.provideAuthRepository().deleteToken();
    }

    @Rule
    public ActivityTestRule<RoomDetailsActivity> mTestRule =
            new ActivityTestRule<RoomDetailsActivity>(RoomDetailsActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context context = InstrumentationRegistry.getContext();

                    Intent intent = new Intent(context, RoomDetailsActivity.class);

                    Room room = new Room("Kitchen", "R2",
                            "https://cdn.pixabay.com/photo/2016/05/26/04/17/home-1416381_960_720.jpg");

                    intent.putExtra("room", room);

                    return intent;
                }
            };

    @Test
    public void testThingsDisplayed() throws Exception {
        SystemClock.sleep(100);
        onView(withId(R.id.recyclerViewDetails))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.tv_error_message))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testThingSuccesfulChanged() throws Exception {
        onView(allOf(withId(R.id.switchThing), withText("F1"))).check(matches(isNotChecked()));

        onView(allOf(withId(R.id.switchThing), withText("F1"))).perform(swipeRight());

        onView(allOf(withId(R.id.switchThing), withText("F1"))).check(matches(isChecked()));
    }

    @Test
    public void testThingChangedWithError() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        onView(allOf(withId(R.id.switchThing), withText("F1"))).perform(swipeRight());

        onView(withText("Server is not available"))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
