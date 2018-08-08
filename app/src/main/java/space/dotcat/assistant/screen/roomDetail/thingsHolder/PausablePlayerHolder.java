package space.dotcat.assistant.screen.roomDetail.thingsHolder;

import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import space.dotcat.assistant.R;
import space.dotcat.assistant.content.PausablePlayer;
import space.dotcat.assistant.content.Player;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsAdapter;

public class PausablePlayerHolder extends PlayerHolder {

    @BindView(R.id.bt_pause)
    Button mPause;

    private final String PAUSED_STATE = "paused";

    public static final String PAUSED_COMMAND = "pause";

    public PausablePlayerHolder(View itemView, RoomDetailsAdapter.PlayerStateListener playerStateListener) {
        super(itemView, playerStateListener);

        mPause.setOnClickListener(v-> {
            int position = (int) v.getTag();

            playerStateListener.onPlayerStateChange(position, PAUSED_STATE, PAUSED_COMMAND);
        });
    }

    @Override
    protected void bind(Player item) {
        super.bind(item);

        mPause.setTag(getAdapterPosition());
    }
}
