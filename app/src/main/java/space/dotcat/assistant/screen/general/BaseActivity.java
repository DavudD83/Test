package space.dotcat.assistant.screen.general;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ApiError;
import space.dotcat.assistant.repository.RepositoryProvider;
import space.dotcat.assistant.screen.auth.AuthActivity;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private static final int INVALID_ACCESS_TOKEN = 2101;

    private Snackbar mSnackbar;

    private final View.OnClickListener mInvalidAccessTokenHandler = onClick -> {
        RepositoryProvider.provideAuthRepository().deleteToken();
        AuthActivity.start(this);
        finish();
    };

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        if(mToolbar == null){
            Log.d(TAG, "Toolbar not found");
        }

        setSupportActionBar(mToolbar);
    }

    public void showBaseError(Throwable throwable, View view){
        //TODO look at it function later

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

    public Snackbar getSnackBar(){
        return mSnackbar;
    }
}
