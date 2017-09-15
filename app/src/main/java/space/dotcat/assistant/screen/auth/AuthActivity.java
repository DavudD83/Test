package space.dotcat.assistant.screen.auth;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import space.dotcat.assistant.R;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomlist.RoomsActivity;

public class AuthActivity extends AppCompatActivity implements AuthView {

    private LoadingView mLoadingView;

    private AuthPresenter mAuthPresenter;

    @BindView(R.id.bt_logIn)
    Button mButton;

    @BindView(R.id.constraint_container)
    ConstraintLayout mContainer;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.editText_login)
    TextInputEditText mLogin;

    @BindView(R.id.editText_password)
    TextInputEditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.app_name));


        mButton.setOnClickListener(view ->  tryLogIn(view));

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this,
                getSupportLoaderManager());

        mAuthPresenter = new AuthPresenter(this, lifecycleHandler);

        mAuthPresenter.init();
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
    public void showLoginError() {
        mLogin.setError(getString(R.string.login_error));
    }

    @Override
    public void showPasswordError() {
        mPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void showAuthError() {

        Snackbar snackbar = Snackbar.make(mContainer, "Authorization error",
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Try Again", onClick -> {
            snackbar.dismiss();
        });

        snackbar.show();
    }


    public void tryLogIn(View view){

        mAuthPresenter.tryLogin(mLogin.getText().toString() , mPassword.getText().toString());
    }
}
