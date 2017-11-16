package space.dotcat.assistant.screen.auth;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.AuthorizationAnswer;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.roomList.RoomsActivity;
import space.dotcat.assistant.utils.InputLayoutErrorMatcher;
import space.dotcat.assistant.utils.RxJavaTestRule;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    @Rule
    public final ActivityTestRule<AuthActivity> mTestRule =
            new ActivityTestRule<>(AuthActivity.class);

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    private final static AuthorizationAnswer ERROR =
            new AuthorizationAnswer("ERROR", "ERROR");

    @Before
    public void init() {
        Intents.init();
    }

    @After
    public void clear() {
        Intents.release();
        RepositoryProvider.provideAuthRepository().deleteToken();
    }

    @Test
    public void testEmptyFieldsCreated() throws Exception {
        onView(withId(R.id.editText_url)).check(matches(allOf(isFocusable(),
                isClickable(),
                isEnabled(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withText(""))));

        onView(withId(R.id.editText_login)).check(matches(allOf(isFocusable(),
                isClickable(),
                isEnabled(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withText(""))));

        onView(withId(R.id.editText_password)).check(matches(allOf(isFocusable(),
                isClickable(),
                isEnabled(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT),
                withText(""))));
    }

    @Test
    public void testLogInButtonCreated() throws Exception {
        onView(withId(R.id.bt_logIn)).check(matches(allOf(isClickable(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                isEnabled(),
                withText(R.string.log_in))));
    }

    @Test
    public void testTypeTextIntoFields() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText("https://url/"));

        closeSoftKeyboard(); //close keyboard because it can overlay fields and espresso may not see them

        onView(withId(R.id.editText_login)).perform(typeText("login"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_password)).perform(typeText("password"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login)).check(matches(withText("login")));

        onView(withId(R.id.editText_password)).check(matches(withText("password")));
    }

    @Test
    public void testUrlIsNotCorrectErrorDisplayed() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText("http:/url/"));

        closeSoftKeyboard();

        onView(withId(R.id.bt_logIn)).perform(click());

        onView(withId(R.id.editText_url))
                .check(matches(InputLayoutErrorMatcher.matchError(R.string.url_is_not_correct_error)));
    }

    @Test
    public void testEmptyUrlErrorDisplayed() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.editText_url))
                .check(matches(InputLayoutErrorMatcher.matchError(R.string.url_empty_error)));
    }

    @Test
    public void testEmptyLoginErrorDisplayed() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText("https://url/"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login))
                .check(matches(InputLayoutErrorMatcher.matchError(R.string.login_empty_error)));
    }

    @Test
    public void testEmptyPasswordErrorDisplayed() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText("https://url/"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login)).perform(typeText("login"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_password)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.editText_password))
                .check(matches(InputLayoutErrorMatcher.matchError(R.string.password__empty_error)));
    }

    @Test
    public void testSuccessAuth() throws Exception {
        onView(withId(R.id.editText_url)).perform(typeText("https://url/"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login)).perform(typeText("login"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_password)).perform(typeText("pass"));

        closeSoftKeyboard();

        onView(withId(R.id.bt_logIn)).perform(click());

        Intents.intended(hasComponent(RoomsActivity.class.getName()));
    }

    @Test
    public void testErrorAuth() throws Exception {
        RepositoryProvider.provideAuthRepository().saveAuthorizationAnswer(ERROR);

        onView(withId(R.id.editText_url)).perform(typeText("https://url/"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_login)).perform(typeText("login"));

        closeSoftKeyboard();

        onView(withId(R.id.editText_password)).perform(typeText("pass"));

        closeSoftKeyboard();

        onView(withId(R.id.bt_logIn)).perform(click());

        onView(withText("Server is not available"))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
