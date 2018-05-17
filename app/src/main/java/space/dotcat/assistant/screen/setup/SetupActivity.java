package space.dotcat.assistant.screen.setup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.di.activitiesComponents.setupActivity.SetupActivityModule;
import space.dotcat.assistant.screen.auth.AuthActivity;
import space.dotcat.assistant.screen.general.BaseActivity;

public class SetupActivity extends BaseActivity implements SetupViewContract {

    @Inject
    SetupPresenter mSetupPresenter;

    @BindView(R.id.editText_host)
    TextInputEditText mHost;

    @BindView(R.id.editText_port)
    TextInputEditText mPort;

    @BindView(R.id.cb_is_secure_connection)
    CheckBox mIsSecured;

    @BindView(R.id.bt_completeSetup)
    Button mCompleteSetup;

    @BindView(R.id.cl_container)
    ConstraintLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mSetupPresenter.init();
    }

    @Override
    protected void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusSetupComponent(new SetupActivityModule(this))
                .inject(this);
    }

    @Override
    protected void setupToolbar() {
        Toolbar toolbar = getToolbar();
        toolbar.setTitle(getResources().getString(R.string.setup_activity_label));

        super.setupToolbar();
    }

    @Override
    protected View getViewForErrorSnackbar() {
        return mContainer;
    }

    @OnClick(R.id.bt_completeSetup)
    public void tryCompleteSetup(View view) {
        boolean isSecured = mIsSecured.isChecked();

        String host = mHost.getText().toString();
        String port = mPort.getText().toString();

        mSetupPresenter.completeSetup(isSecured, host, port);
    }

    @Override
    public void showAuthActivity() {
        AuthActivity.start(this);
        finish();
    }

    @Override
    public void showHostError() {

    }

    @Override
    public void showPortError() {

    }

    @Override
    public void showEmptyHost() {
        mHost.setError(getString(R.string.host_empty_error));
    }

    @Override
    public void showEmptyPort() {
        mPort.setError(getString(R.string.port_empty_error));
    }

    @Override
    public void showExistingHost(String host) {
        mHost.setText(host);
    }

    @Override
    public void showExistingPort(String port) {
        mPort.setText(port);
    }
}
