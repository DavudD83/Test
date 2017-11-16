package space.dotcat.assistant.screen.roomDetail;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
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
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EmptyRoomDetailActivityTest {

    private final static AuthorizationAnswer ERROR_EMPTY =
            new AuthorizationAnswer("ERROR", "ERROR_EMPTY_THINGS");

    @Before
    public void init() {
        RepositoryProvider.provideAuthRepository().saveUrl(new Url("https://api.ks-cube.tk/"));
    }

    @After
    public void clear() {
        RepositoryProvider.provideAuthRepository().deleteToken();
    }

    @Rule
    public ActivityTestRule<RoomDetailsActivity> mTestRule =
            new ActivityTestRule<>(RoomDetailsActivity.class, false, false);


    @Test
    public void testNoThingsInRoom() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR_EMPTY);

        launchActivity();

        onView(withId(R.id.recyclerViewDetails))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.tv_error_message)).check(matches(withText(R.string.error_empty_actions)));
    }

    private void launchActivity() {
        Context context = InstrumentationRegistry.getContext();

        Intent intent = new Intent(context, RoomDetailsActivity.class);

        Room room = new Room("Kitchen", "R2",
                "https://cdn.pixabay.com/photo/2016/05/26/04/17/home-1416381_960_720.jpg");

        intent.putExtra("room", room);

        mTestRule.launchActivity(intent);
    }
}
