package com.example.recyclerviewtest.screen.roomlist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.recyclerviewtest.R;
import com.example.recyclerviewtest.content.Room;
import com.example.recyclerviewtest.screen.general.LoadingDialog;
import com.example.recyclerviewtest.screen.general.LoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;

public class RoomsActivity extends AppCompatActivity implements RoomsView, RoomsAdapter.OnItemClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerVIew;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LoadingView mLoadingView;

    private RoomsAdapter mAdapter;

    private RoomsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        ButterKnife.bind(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        mRecyclerVIew.setLayoutManager(new GridLayoutManager(this, getCountOfColumns()));

        mAdapter = new RoomsAdapter(new ArrayList<>(), this);

        mRecyclerVIew.setAdapter(mAdapter);

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this,getSupportLoaderManager());
        mPresenter = new RoomsPresenter(lifecycleHandler, this);
        mPresenter.init();
    }

    @Override
    public void onItemClick(@NonNull Room room) { mPresenter.onItemClick(room);}

    @Override
    public void showRooms(@NonNull List<Room> rooms) {
        mAdapter.ChangeDataSet(rooms); }

    @Override
    public void showRoomDetail(@NonNull Room room) {
        //TODO
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Some Problem", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() { mLoadingView.showLoading();}

    @Override
    public void hideLoading() { mLoadingView.hideLoading();}

    @Override
    public void onRefresh() {
        mPresenter.init();
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