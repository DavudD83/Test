package space.dotcat.assistant.screen.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.di.activitiesComponents.authActivity.AuthActivityModule;
import space.dotcat.assistant.screen.general.BaseActivity;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomList.RoomsActivity;

public class AuthActivity extends BaseActivity implements AuthViewContract {

    @Inject
    LoadingView mLoadingView;

    @Inject
    AuthPresenter mAuthPresenter;

    @BindView(R.id.bt_logIn)
    Button mButton;

    @BindView(R.id.constraint_container)
    ConstraintLayout mContainer;

    @BindView(R.id.editText_url)
    TextInputEditText mUrl;

    @BindView(R.id.editText_login)
    TextInputEditText mLogin;

    @BindView(R.id.editText_password)
    TextInputEditText mPassword;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthPresenter.init();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuthPresenter.unsubscribe();
    }

    @Override
    protected void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusAuthComponent(new AuthActivityModule(this, getSupportFragmentManager()))
                .inject(this);
    }

    @Override
    protected void setupToolbar() {
        Toolbar toolbar = getToolbar();

        toolbar.setTitle(getString(R.string.app_name));

        setNewToolbar(toolbar);

        super.setupToolbar();
    }

    @Override
    public void showLoading() {
        mLoadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        mLoadingView.hideLoading();
    }

    @Override
    public void showRoomList() {
        RoomsActivity.start(this);
        finish();
    }

    @Override
    public void showExistingUrl(@NonNull String url) {
        mUrl.setText(url);
    }

    @Override
    public void showUrlEmptyError() {
        mUrl.setError(getString(R.string.url_empty_error));
    }

    @Override
    public void showUrlNotCorrectError() {
        mUrl.setError(getString(R.string.url_is_not_correct_error));
    }

    @Override
    public void showLoginError() {
        mLogin.setError(getString(R.string.login_empty_error));
    }

    @Override
    public void showPasswordError() {
        mPassword.setError(getString(R.string.password__empty_error));
    }

    @Override
    public void showAuthError(Throwable t) {
        super.showBaseError(t, mContainer);
    }

    @OnClick(R.id.bt_logIn)
    public void tryLogIn(View view) {
        mAuthPresenter.tryLogin(mUrl.getText().toString(), mLogin.getText().toString(),
                mPassword.getText().toString());
    }
}
