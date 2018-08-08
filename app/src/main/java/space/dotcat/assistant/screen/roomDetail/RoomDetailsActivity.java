package space.dotcat.assistant.screen.roomDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import space.dotcat.assistant.AppDelegate;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.ColorTemperatureLamp;
import space.dotcat.assistant.content.DimmableLamp;
import space.dotcat.assistant.content.DoorLock;
import space.dotcat.assistant.content.Lamp;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.RGBLamp;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.content.Thing;
import space.dotcat.assistant.di.activitiesComponents.roomDetails.RoomDetailsModule;
import space.dotcat.assistant.screen.general.BaseActivityWithSettingsMenu;
import space.dotcat.assistant.screen.general.LoadingView;

public class RoomDetailsActivity extends BaseActivityWithSettingsMenu implements RoomDetailsViewContract,
        RoomDetailsAdapter.OnItemClickListener<Thing>, RoomDetailsAdapter.OnThingClick, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    LoadingView mLoadingView;

    @Inject
    RoomDetailsAdapter mRoomDetailsAdapter;

    @Inject
    RoomDetailsPresenter mRoomDetailsPresenter;

    @BindView(R.id.swipeRefreshDetails)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recyclerViewDetails)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;

    private Room mRoom;

    private final static String EXTRA_ROOM = "room";

    public static Intent getIntent(@NonNull Context activity, @NonNull Room room) {
        Intent intent = new Intent(activity, RoomDetailsActivity.class);
        intent.putExtra(EXTRA_ROOM, room);

        return intent;
    }

    public static void start(@NonNull Activity activity, @NonNull Room room) {
        Intent launchIntent = getIntent(activity, room);

        activity.startActivity(launchIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        mRoom = getIntent().getParcelableExtra(EXTRA_ROOM);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRoom.getFriendlyName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRoomDetailsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRoomDetailsPresenter.init(mRoom.getId());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mRoomDetailsPresenter.unsubscribe();
    }

    @Override
    protected void initDependencyGraph() {
        AppDelegate.getInstance()
                .plusDataLayerComponent()
                .plusRoomDetailsComponent(new RoomDetailsModule(this, this,
                        this, getSupportFragmentManager()))
                .inject(this);
    }

    @Override
    protected View getViewForErrorSnackbar() {
        return mSwipeRefreshLayout;
    }

    @Override
    public void showThings(@NonNull List<Thing> things) {
//        if (getSnackBar() != null) {
//            if (getSnackBar().isShown()) {
//                getSnackBar().dismiss();
//            }
//        }

        mRecyclerView.setVisibility(View.VISIBLE);

        mErrorMessage.setVisibility(View.INVISIBLE);

        mRoomDetailsAdapter.updateData(things);
    }

    @Override
    public void showError(Throwable throwable) {
        super.showBaseError(throwable);
    }

    @Override
    public void showEmptyThingsError() {
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(getResources().getString(R.string.error_empty_things));
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
        mRoomDetailsPresenter.reloadThings(mRoom.getId());

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Thing item) {
        mRoomDetailsPresenter.onItemChange(item);
    }

    @Override
    public void onLampSwitchClick(Lamp lamp) {
        mRoomDetailsPresenter.onLampSwitchClick(lamp);
    }

    @Override
    public void onDoorLockSwitchClick(DoorLock doorLock) {
        mRoomDetailsPresenter.onDoorSwitchClick(doorLock);
    }

    @Override
    public void onBrightnessLevelChange(DimmableLamp oldLamp, DimmableLamp newLamp) {
        mRoomDetailsPresenter.onBrightnessLevelChange(oldLamp, newLamp);
    }

    @Override
    public void onColorTemperatureLevelChange(ColorTemperatureLamp oldLamp, ColorTemperatureLamp newLamp) {
        mRoomDetailsPresenter.onColorTemperatureLevelChange(oldLamp, newLamp);
    }

    @Override
    public void onColorChange(RGBLamp oldValue, RGBLamp newValue) {
        mRoomDetailsPresenter.onColorChange(oldValue, newValue);
    }

    @Override
    public void onPlayerChange(Player oldItem, Player newItem, String command) {
        mRoomDetailsPresenter.onThingChanged(oldItem, newItem, command);
    }
}
