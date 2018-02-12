package space.dotcat.assistant.utils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;

public class RxJavaTestRule implements TestRule {

    private final Func1<Scheduler, Scheduler> mMockFunc = new Func1<Scheduler, Scheduler>() {
        @Override
        public Scheduler call(Scheduler scheduler) {
            return Schedulers.immediate();
        }
    };

    private final RxAndroidSchedulersHook mAndroidSchedulersHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaHooks.reset();
                RxJavaHooks.setOnIOScheduler(mMockFunc);
                RxJavaHooks.setOnNewThreadScheduler(mMockFunc);

                RxAndroidPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().registerSchedulersHook(mAndroidSchedulersHook);

                base.evaluate();

                RxJavaHooks.reset();
                RxAndroidPlugins.getInstance().reset();
            }
        };
    }
}
