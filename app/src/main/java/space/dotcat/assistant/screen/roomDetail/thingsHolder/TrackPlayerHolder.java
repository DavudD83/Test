package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.content.TrackPlayer;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class TrackPlayerHolder extends PausablePlayerHolder {

    @BindView(R.id.tv_playing_track_name)
    TextView mTrackPlaying;

    @BindView(R.id.bt_switch_to_previous)
    Button mSwitchToPrevious;

    @BindView(R.id.bt_switch_to_next)
    Button mSwitchToNext;

    public static final String COMMAND_PREVIOUS = "previous";

    public static final String COMMAND_NEXT = "next";

    public TrackPlayerHolder(View itemView, RoomDetailsAdapter.PlayerStateListener playerStateListener,
                             RoomDetailsAdapter.PlayerTrackListener playerTrackListener) {
        super(itemView, playerStateListener);

        mSwitchToPrevious.setOnClickListener(v-> {
            int position = (int) v.getTag();

            playerTrackListener.onTrackChange(position, COMMAND_PREVIOUS);
        });

        mSwitchToNext.setOnClickListener(v-> {
            int position = (int) v.getTag();

            playerTrackListener.onTrackChange(position, COMMAND_NEXT);
        });
    }

    @Override
    protected void bind(Player item) {
        super.bind(item);

        TrackPlayer trackPlayer = (TrackPlayer) item;

        mTrackPlaying.setText(trackPlayer.getTrackInfo());

        mSwitchToNext.setTag(getAdapterPosition());
        mSwitchToPrevious.setTag(getAdapterPosition());
    }
}
