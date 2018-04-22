package space.dotcat.assistant.screen.general;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.repository.authRepository.AuthRepository;
import space.dotcat.assistant.screen.auth.AuthActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public AuthRepository mAuthRepository;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Snackbar mSnackbar;

    private static final String TAG = "BaseActivity";

    private static final int INVALID_ACCESS_TOKEN = 2101;

    private final View.OnClickListener mInvalidAccessTokenHandler = onClick -> {
        mAuthRepository.deleteToken();

        mAuthRepository.destroyApiService();

        AuthActivity.start(this);

        finish();
    };

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        initDependencyGraph();

        ButterKnife.bind(this);

        setupToolbar();
    }

    abstract protected void initDependencyGraph();

    protected void setupToolbar() {
        if(mToolbar == null) {
            Log.d(TAG, "Toolbar is not found");
        }

        setSupportActionBar(mToolbar);
    }

    protected void setNewToolbar(Toolbar toolbar) {
        if(toolbar == null) {
            Log.d(TAG, "Can not set nullable toolbar");
            return;
        }

        mToolbar = toolbar;
    }

    protected Snackbar getSnackBar(){
        return mSnackbar;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public void showBaseError(Throwable throwable, View view){
        mSnackbar = Snackbar.make(view, "Error",
                Snackbar.LENGTH_INDEFINITE);

        mSnackbar.setAction("Try Again", onClick -> mSnackbar.dismiss());

        if(throwable instanceof  ApiError) {
            ApiError error = (ApiError) throwable;

            if (error.getErrorId() == INVALID_ACCESS_TOKEN) {
                mSnackbar.setAction("Log in again", mInvalidAccessTokenHandler);
            }

            mSnackbar.setText(error.getUserMessage());
        } else {
            Log.d(TAG, throwable.getMessage());

            mSnackbar.setText("Error. Contact to developer");
        }

        mSnackbar.show();
    }

    public void setErrorHandler(String actionName, View.OnClickListener action){
        if(mSnackbar == null)
            return;

        mSnackbar.setAction(actionName, action);
    }
}
