package space.dotcat.assistant.screen.auth;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.BuildConfig;
import space.dotcat.assistant.content.Url;
import space.dotcat.assistant.repository.AuthRepository;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.R;
import space.dotcat.assistant.repository.authRepository.localAuthDataSource.LocalAuthSource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AuthActivityWithSavedUrlTest {

    private LocalAuthSource mLocalAuthSource;

    @Rule
    public ActivityTestRule<AuthActivity> mTestRule =
            new ActivityTestRule<AuthActivity>(AuthActivity.class, false,
                    false);

    @Before
    public void init() {
        mLocalAuthSource = AppDelegate.getInstance()
                .plusDataLayerComponent()
                .getFakeLocalAuthSource();

        mLocalAuthSource.saveUrl(BuildConfig.URL_DEFAULT_VALUE);

    }

    @After
    public void clear() {
      /*  Realm.getDefaultInstance().executeTransaction(transaction -> {
            transaction.delete(Url.class);
        });*/
    }

    @Test
    public void testFieldUrlFilledIfUrlSaved() throws Exception {
        launchActivity();

        onView(withId(R.id.editText_url)).check(matches(withText("https://api.ks-cube.tk/")));
    }

    private void launchActivity() {
        Context context = InstrumentationRegistry.getContext();

        Intent intent = new Intent(context, AuthActivity.class);

        mTestRule.launchActivity(intent);
    }
}
