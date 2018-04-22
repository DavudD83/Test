package space.dotcat.assistant.screen.roomList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.di.activitiesComponents.roomsActivity.RoomsModule;
import space.dotcat.assistant.screen.general.BaseActivityWithSettingsMenu;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;

public class RoomsActivity extends BaseActivityWithSettingsMenu implements RoomsViewContract, RoomsAdapter.OnItemClick,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RoomsAdapter mAdapter;

    @Inject
    RoomsPresenter mPresenter;

    @Inject
    LoadingView mLoadingView;

    @BindView(R.id.recyclerViewRooms)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, RoomsActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getCountOfColumns()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter.init();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPresenter.unsubscribe();
    }

    @Override
    protected void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusRoomsComponent(new RoomsModule(this, this,
                        getSupportFragmentManager()))
                .inject(this);
    }

    @Override
    protected void setupToolbar() {
        Toolbar toolbar = getToolbar();
        toolbar.setTitle(getString(R.string.rooms_activity_title));

        setNewToolbar(toolbar);

        super.setupToolbar();
    }

    @Override
    public void onItemClick(@NonNull Room room) {
        mPresenter.onItemClick(room);
    }

    @Override
    public void showRooms(@NonNull List<Room> rooms) {
        if (getSnackBar() != null) {
            if (getSnackBar().isShown()) {
                getSnackBar().dismiss();
            }
        }

        mRecyclerView.setVisibility(View.VISIBLE);

        mErrorMessage.setVisibility(View.INVISIBLE);

        mAdapter.changeDataSet(rooms);
    }

    @Override
    public void showRoomDetail(@NonNull Room room) {
        RoomDetailsActivity.start(this, room);
    }

    @Override
    public void showError(Throwable throwable) {
        super.showBaseError(throwable, mSwipeRefreshLayout);
    }

    @Override
    public void showEmptyRoomsMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(getResources().getString(R.string.error_empty_rooms));
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
    public void onRefresh() {
        mPresenter.reloadRooms();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private int getCountOfColumns() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 3;

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 2;

        } else
            return 0;
    }
}
