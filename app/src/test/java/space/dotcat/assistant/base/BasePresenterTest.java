package space.dotcat.assistant.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import space.dotcat.assistant.screen.general.BasePresenter;
import space.dotcat.assistant.screen.general.BaseRxPresenter;
import space.dotcat.assistant.utils.RxJavaTestRule;

@RunWith(JUnit4.class)
public abstract class BasePresenterTest<P extends BasePresenter> {

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    protected P mPresenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mPresenter = createPresenterForTesting();
    }

    @After
    public void clear() {
        mPresenter = null;
    }

    protected abstract P createPresenterForTesting();
}
