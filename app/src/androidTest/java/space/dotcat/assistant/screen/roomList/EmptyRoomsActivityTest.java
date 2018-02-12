package space.dotcat.assistant.screen.roomList;

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

import io.realm.Realm;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class EmptyRoomsActivityTest {

    private static final AuthorizationAnswer ERROR = new AuthorizationAnswer("ERROR",
            "ERROR_EMPTY_ROOMS");

    @Rule
    public ActivityTestRule<RoomsActivity> mTestRule =
            new ActivityTestRule<>(RoomsActivity.class, false, false);

    @Before
    public void init() {
        RepositoryProvider.provideAuthRepository().saveUrl(new Url("https://api.ks-cube.tk/"));
        Realm.getDefaultInstance().executeTransaction(transaction -> transaction.delete(Room.class));
    }

    @After
    public void clear() {
        RepositoryProvider.provideAuthRepository().deleteToken();
    }

    @Test
    public void testEmptyRecyclerCreated() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        launchActivity();

        onView(withId(R.id.tv_error_message)).check(matches(allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withText(R.string.error_empty_rooms))));

        onView(withId(R.id.recyclerViewRooms)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    private void launchActivity() {
        Context context = InstrumentationRegistry.getContext();

        Intent intent = new Intent(context, RoomsActivity.class);

        mTestRule.launchActivity(intent);
    }
}
